package com.ericyl.example.util;

import android.content.Context;

import com.ericyl.example.ExampleApplication;

import java.io.File;

public class AppProperties {
    public static Context getContext() {
        return ExampleApplication.getApplication();
    }

    public static File getDatabaseDir(String databasePath) {
        return getContext().getDatabasePath(databasePath);
    }

}
