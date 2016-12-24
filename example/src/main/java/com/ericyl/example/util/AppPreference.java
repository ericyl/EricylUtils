package com.ericyl.example.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreference {
    private String username;

    private static final String APP_PREFERENCE = "Preference";
    private static final String USER_NAME = "Username";

    public AppPreference() {
    }


    public AppPreference(String username) {
        super();
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public static AppPreference Read() {
        Context context = AppProperties.getContext();
        AppPreference appPreference = new AppPreference();
        try {
            SharedPreferences share = context.getSharedPreferences(
                    APP_PREFERENCE, Context.MODE_PRIVATE);
            appPreference.setUsername(share.getString(USER_NAME, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appPreference;
    }

    public static void Write(AppPreference appPreference) {
        Context context = AppProperties.getContext();
        try {
            SharedPreferences share = context.getSharedPreferences(
                    APP_PREFERENCE, Context.MODE_PRIVATE);
            if (share != null) {
                Editor editor = share.edit();
                String username = appPreference.getUsername();
                if (username != null && !username.equals("")) {
                    editor.putString(USER_NAME, username);
                }
                editor.apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
