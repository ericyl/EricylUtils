package com.ericyl.utils.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({android.app.DownloadManager.Request.VISIBILITY_HIDDEN, android.app.DownloadManager.Request.VISIBILITY_VISIBLE,
        android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED, android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION})
@Retention(RetentionPolicy.SOURCE)
public @interface DownloadNotificationVisibility {
}