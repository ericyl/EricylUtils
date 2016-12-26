package com.ericyl.example.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDelegate;

import com.ericyl.example.R;
import com.ericyl.example.service.InitService;
import com.ericyl.example.ui.fragment.SplashFragment;
import com.ericyl.example.util.AppProperties;
import com.ericyl.utils.util.ActivityUtils;
import com.ericyl.utils.util.StringUtils;

public class SplashActivity extends FragmentActivity {

    private SplashFragment splashFragment;

    private InitService initService;


    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent();
        intent.setAction(getString(R.string.init_service));
        intent.setPackage(getPackageName());
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean disableSplashScreen = sharedPreferences.getBoolean(StringUtils.getString(AppProperties.getContext(), R.string.key_disable_splash_screen), false);
        int splashScreenSecond = sharedPreferences.getInt(StringUtils.getString(AppProperties.getContext(), R.string.key_splash_screen_second), 2);
        int themeSelection = Integer.parseInt(sharedPreferences.getString(StringUtils.getString(AppProperties.getContext(), R.string.key_theme_selection), "0"));
        setNightMode(themeSelection);
        if (disableSplashScreen)
            jumpHomeActivity();
        else {
            splashFragment = (SplashFragment) getSupportFragmentManager().findFragmentByTag("splashFragment");
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (splashFragment == null) {
                splashFragment = SplashFragment.newInstance(splashScreenSecond);
                transaction.add(android.R.id.content, splashFragment, "splashFragment").commitAllowingStateLoss();
            } else
                transaction.show(splashFragment);
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

    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            InitService.InitBinder binder = (InitService.InitBinder) service;
            initService = binder.getService();
            initService.initSystem();
        }

        public void onServiceDisconnected(ComponentName className) {
            initService = null;
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if (serviceConnection != null) {
            Intent intent = new Intent();
            intent.setAction(getString(R.string.init_service));
            intent.setPackage(getPackageName());
            unbindService(serviceConnection);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (splashFragment != null)
            splashFragment = null;
    }
}
