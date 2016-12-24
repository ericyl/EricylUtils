package com.ericyl.example.ui.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
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

import com.ericyl.example.R;
import com.ericyl.example.event.ChangeThemeEvent;
import com.ericyl.example.model.FragmentTag;
import com.ericyl.example.ui.BaseActivity;
import com.ericyl.example.util.AppProperties;
import com.ericyl.example.util.BusProvider;
import com.ericyl.utils.util.StringUtils;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;


public class SettingActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private FragmentManager fragmentManager;
    private static final String TAG = "TAG_FRAGMENT";

    private static Class[] classes = new Class[]{SettingFragment.class, AppInfoFragment.class};

    private ArrayList<FragmentTag> tags = new ArrayList<>();

    private ActionBar actionBar;

    @Override
    public int getContentViewId() {
        return R.layout.activity_setting;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        initActionBar();
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
    public void initActionBar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
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


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_setting);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            getPreferenceManager().findPreference(getString(R.string.key_splash_screen_second)).setEnabled(!sharedPreferences.getBoolean(getString(R.string.key_disable_splash_screen), false));

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
            if (preference.getFragment() != null)
                BusProvider.getInstance().post(new FragmentTag(preference.getTitle(), preference.getFragment()));
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

            if (Build.VERSION.SDK_INT >= 11) {
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


