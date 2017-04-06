package com.ericyl.example.ui.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.ericyl.example.R;
import com.ericyl.example.event.ChangeThemeEvent;
import com.ericyl.example.model.FragmentTag;
import com.ericyl.example.util.AppProperties;
import com.ericyl.example.util.BusProvider;
import com.ericyl.example.util.CacheUtil;
import com.ericyl.example.util.DatabaseUtils;
import com.ericyl.utils.util.StringUtils;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class SettingActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private FragmentManager fragmentManager;
    private static final String TAG = "TAG_FRAGMENT";

    private static Class[] classes = new Class[]{SettingFragment.class, AppInfoFragment.class};

    private ArrayList<FragmentTag> tags = new ArrayList<>();

    private ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        initActionBar(toolbar);
        fragmentManager = getFragmentManager();
        if (savedInstanceState != null)
            tags = savedInstanceState.getParcelableArrayList(TAG);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TAG, tags);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setFragment();

    }

    private void setFragment() {
        FragmentTag fragmentTag;
        if (tags != null && tags.size() > 0) {
            fragmentTag = tags.get(tags.size() - 1);
        } else {
            fragmentTag = new FragmentTag(StringUtils.getString(AppProperties.getContext(), R.string.setting), SettingFragment.class.getName());
        }
        setFragment(fragmentTag);
    }

    @Subscribe
    public void setFragment(FragmentTag tag) {
        if (fragmentManager == null)
            fragmentManager = getFragmentManager();
        for (Class cls : classes) {
            if (cls.getName().equals(tag.getFragmentName())) {
                try {
                    Fragment fragment = fragmentManager.findFragmentByTag(cls.getSimpleName());
                    if (fragment == null)
                        fragment = (Fragment) cls.newInstance();
                    fragmentManager.beginTransaction().replace(R.id.content, fragment, cls.getSimpleName()).commit();
                    setTitleRes(tag.getTitle());
                    setTag(tag);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void setTitleRes(CharSequence title) {
        if (actionBar != null)
            actionBar.setTitle(title);
    }

    private void setTag(FragmentTag tag) {
        for (int i = 0; i < tags.size(); i++) {
            FragmentTag fragmentTag = tags.get(i);
            if (fragmentTag.equals(tag))
                tags.remove(i);
        }
        tags.add(tag);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);

    }

    @Override
    public void onBackPressed() {
//            super.onBackPressed();
        doBack();
    }

    private void doBack() {
        if (tags != null && tags.size() > 1) {
            tags.remove(tags.size() - 1);
            FragmentTag tag = tags.get(tags.size() - 1);
            setFragment(tag);
        } else
            finish();
    }

    public static class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        private PreferenceManager preferenceManager;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_setting);
            preferenceManager = getPreferenceManager();
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            preferenceManager.findPreference(getString(R.string.key_splash_screen_second)).setEnabled(!sharedPreferences.getBoolean(getString(R.string.key_disable_splash_screen), false));
            CacheUtil.getInstance().getCacheSize(AppProperties.getContext()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<CharSequence>() {
                @Override
                public void call(CharSequence charSequence) {
                    preferenceManager.findPreference(getString(R.string.key_clear_cache)).setSummary(String.format(getString(R.string.cache_size), charSequence));
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        }

        @Override
        public void onResume() {
            super.onResume();
            BusProvider.getInstance().register(this);
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            BusProvider.getInstance().unregister(this);
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);

        }

        @Override
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
            if (preference.getFragment() != null) {
                BusProvider.getInstance().post(new FragmentTag(preference.getTitle(), preference.getFragment()));
                return true;
            }
            if (preference.getKey().equals(getString(R.string.key_clear_search_history))) {
                Observable.create(new Observable.OnSubscribe<Void>() {

                    @Override
                    public void call(Subscriber<? super Void> subscriber) {
                        AppProperties.getContext().getContentResolver().delete(Uri.parse(DatabaseUtils.URI_SEARCH_SUGGESTION), null, null);
                        subscriber.onNext(null);
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Toast.makeText(AppProperties.getContext(), R.string.clear_search_history_success, Toast.LENGTH_SHORT).show();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(AppProperties.getContext(), R.string.clear_search_history_failed, Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (preference.getKey().equals(getString(R.string.key_clear_cache))) {
                preferenceManager.findPreference(getString(R.string.key_clear_cache)).setSummary(getString(R.string.starting_clear_cache));
                CacheUtil.getInstance().clearCache(AppProperties.getContext()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        preferenceManager.findPreference(getString(R.string.key_clear_cache)).setSummary(String.format(getString(R.string.cache_size), charSequence));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
            }
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }

        //
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            if (getString(R.string.key_disable_splash_screen).equals(s)) {
                getPreferenceManager().findPreference(getString(R.string.key_splash_screen_second)).setEnabled(!sharedPreferences.getBoolean(s, false));
            } else if (getString(R.string.key_theme_selection).equals(s)) {
                int themeSelection = Integer.parseInt(sharedPreferences.getString(s, "0"));
                int mode;
                switch (themeSelection) {
                    default:
                    case 0:
                        mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                        break;
                    case 1:
                        mode = AppCompatDelegate.MODE_NIGHT_NO;
                        break;
                    case 2:
                        mode = AppCompatDelegate.MODE_NIGHT_YES;
                        break;
                    case 3:
                        mode = AppCompatDelegate.MODE_NIGHT_AUTO;
                        break;
                }
                setNightMode(mode);
            }
        }

        private void setNightMode(@AppCompatDelegate.NightMode int nightMode) {
            AppCompatDelegate.setDefaultNightMode(nightMode);

            if (Build.VERSION.SDK_INT >= 15) {
                getActivity().recreate();
                BusProvider.getInstance().post(new ChangeThemeEvent());
            }
        }

    }

    public static class AppInfoFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_app_info);
        }


    }


}



