package com.ericyl.utils.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({android.app.DownloadManager.STATUS_FAILED, android.app.DownloadManager.STATUS_PAUSED,
        android.app.DownloadManager.STATUS_PENDING, android.app.DownloadManager.STATUS_RUNNING, android.app.DownloadManager.STATUS_SUCCESSFUL})
@Retention(RetentionPolicy.SOURCE)
public @interface DownloadStatus {
}