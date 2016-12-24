package com.ericyl.example.util;

import android.content.Context;

import com.ericyl.example.ExampleApplication;

public class AppProperties {
    public static Context getContext() {
        return ExampleApplication.getApplication();
    }
}
