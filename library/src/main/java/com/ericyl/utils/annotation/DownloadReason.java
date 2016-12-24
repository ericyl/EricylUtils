package com.ericyl.utils.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        android.app.DownloadManager.ERROR_CANNOT_RESUME, android.app.DownloadManager.ERROR_DEVICE_NOT_FOUND, android.app.DownloadManager.ERROR_FILE_ALREADY_EXISTS,
        android.app.DownloadManager.ERROR_FILE_ERROR, android.app.DownloadManager.ERROR_HTTP_DATA_ERROR, android.app.DownloadManager.ERROR_INSUFFICIENT_SPACE,
        android.app.DownloadManager.ERROR_TOO_MANY_REDIRECTS, android.app.DownloadManager.ERROR_UNHANDLED_HTTP_CODE, android.app.DownloadManager.ERROR_UNKNOWN,
        android.app.DownloadManager.PAUSED_QUEUED_FOR_WIFI, android.app.DownloadManager.PAUSED_UNKNOWN, android.app.DownloadManager.PAUSED_WAITING_FOR_NETWORK, android.app.DownloadManager.PAUSED_WAITING_TO_RETRY})
@Retention(RetentionPolicy.SOURCE)
public @interface DownloadReason {
}