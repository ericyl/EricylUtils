package com.ericyl.example.util;

import android.app.DownloadManager;
import android.content.Context;

import com.ericyl.example.ExampleApplication;
import com.ericyl.utils.annotation.DownloadAllowedNetworkTypes;
import com.ericyl.utils.util.DownloadUtils;

import java.io.File;

public class AppProperties {

    public static String ACTION_DOWNLOAD_ON = "com.ericyl.example.DOWNLOAD_ON";
    public static String ACTION_DOWNLOAD_OFF = "com.ericyl.example.DOWNLOAD_OFF";

    public static Context getContext() {
        return ExampleApplication.getApplication();
    }

    public static File getDatabaseDir(String databasePath) {
        return getContext().getDatabasePath(databasePath);
    }

    public static long download(String name, String title, String url, String info) {
        return DownloadUtils.download(getContext(), name, url, title, info, "file", true, DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION, DownloadAllowedNetworkTypes.ALL, null, true);
    }

}
