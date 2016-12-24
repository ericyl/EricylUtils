package com.ericyl.example.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatDelegate;

import com.ericyl.example.R;
import com.ericyl.example.ui.fragment.SplashFragment;
import com.ericyl.example.util.AppProperties;
import com.ericyl.utils.util.ActivityUtils;
import com.ericyl.utils.util.StringUtils;

public class SplashActivity extends FragmentActivity {

    private SplashFragment splashFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean disableSplashScreen = sharedPreferences.getBoolean(StringUtils.getString(AppProperties.getContext(), R.string.key_disable_splash_screen), false);
        int splashScreenSecond = sharedPreferences.getInt(StringUtils.getString(AppProperties.getContext(), R.string.key_splash_screen_second), 2);
        int themeSelection = Integer.parseInt(sharedPreferences.getString(StringUtils.getString(AppProperties.getContext(), R.string.key_theme_selection), "0"));
        setNightMode(themeSelection);
        if (disableSplashScreen)
            jumpHomeActivity();
        else {
            if (savedInstanceState == null) {
                splashFragment = SplashFragment.newInstance(splashScreenSecond);
                getSupportFragmentManager().beginTransaction().add(android.R.id.content, splashFragment, "splashFragment").commitAllowingStateLoss();
            } else
                splashFragment = (SplashFragment) getSupportFragmentManager().findFragmentByTag("splashFragment");
        }
    }


    public void jumpHomeActivity() {
        ActivityUtils.jumpActivity(this, HomeActivity.class);
        finish();
    }


    private void setNightMode(int themeSelection) {
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
        AppCompatDelegate.setDefaultNightMode(mode);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (splashFragment != null)
            splashFragment = null;
    }
}
