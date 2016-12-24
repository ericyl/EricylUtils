package com.ericyl.example.ui.activity;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.ericyl.example.R;
import com.ericyl.example.model.FragmentTag;
import com.ericyl.example.ui.BaseActivity;
import com.ericyl.example.util.BusProvider;
import com.ericyl.utils.ui.widget.support.preference.RadioGroupPreference;
import com.squareup.otto.Subscribe;

import butterknife.BindView;


public class TakePhotoSettingActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    public int getContentViewId() {
        return R.layout.activity_setting;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        initActionBar();

    }

    @Override
    public void initActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        setFragment();

    }


    @Subscribe
    public void setFragment() {
        Fragment fragment = getFragmentManager().findFragmentByTag(SettingFragment.class.getSimpleName());
        if (fragment == null)
            fragment = new SettingFragment();
        getFragmentManager().beginTransaction().replace(R.id.content, fragment, SettingFragment.class.getSimpleName()).commit();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public static class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_take_photo_setting);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            boolean flag = sharedPreferences.getBoolean(getString(R.string.key_save_photo), false);
            RadioGroupPreference radioGroupPreference = (RadioGroupPreference) getPreferenceManager().findPreference(getString(R.string.key_save_storage));
            radioGroupPreference.setEnabled(flag);
            radioGroupPreference.setPreferenceSummary("1", getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath());
            radioGroupPreference.setPreferenceSummary("2", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());

            String radioValue = radioGroupPreference.getRadioValue();
            Preference addToGalleryPreference = getPreferenceManager().findPreference(getString(R.string.key_add_to_gallery));
            if (TextUtils.isEmpty(radioValue) || radioValue.equals("1")) {
                addToGalleryPreference.setEnabled(false);
            } else
                addToGalleryPreference.setEnabled(flag);

            getPreferenceManager().findPreference(getString(R.string.key_save_photo_other_function)).setEnabled(flag);

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

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            RadioGroupPreference radioGroupPreference = (RadioGroupPreference) getPreferenceManager().findPreference(getString(R.string.key_save_storage));
            CheckBoxPreference addToGalleryPreference = (CheckBoxPreference) getPreferenceManager().findPreference(getString(R.string.key_add_to_gallery));
            PreferenceCategory savePhotoOtherFunctionPreference = (PreferenceCategory) getPreferenceManager().findPreference(getString(R.string.key_save_photo_other_function));
            CheckBoxPreference cropPhotoPreference = (CheckBoxPreference) getPreferenceManager().findPreference(getString(R.string.key_crop_photo));
            if (getString(R.string.key_save_photo).equals(s)) {
                boolean flag = sharedPreferences.getBoolean(s, false);
                radioGroupPreference.setEnabled(flag);
                String radioValue = radioGroupPreference.getRadioValue();
                if (TextUtils.isEmpty(radioValue) || radioValue.equals("1")) {
                    addToGalleryPreference.setEnabled(false);
                    addToGalleryPreference.setChecked(false);
                } else
                    addToGalleryPreference.setEnabled(flag);
                if (!flag) {
                    addToGalleryPreference.setChecked(false);
                    cropPhotoPreference.setChecked(false);
                }
                savePhotoOtherFunctionPreference.setEnabled(flag);
            } else if (getString(R.string.key_save_storage).equals(s)) {
                int saveStorage = Integer.parseInt(sharedPreferences.getString(s, "1"));
                switch (saveStorage) {
                    default:
                    case 1:
                        addToGalleryPreference.setEnabled(false);
                        addToGalleryPreference.setChecked(false);
                        break;
                    case 2:
                    case 3:
                        addToGalleryPreference.setEnabled(true);
                        break;
                }
            }
        }
    }


}



