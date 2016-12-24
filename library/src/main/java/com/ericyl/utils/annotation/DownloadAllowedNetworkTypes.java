package com.ericyl.utils.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({android.app.DownloadManager.Request.NETWORK_MOBILE, android.app.DownloadManager.Request.NETWORK_WIFI})
@Retention(RetentionPolicy.SOURCE)
public @interface DownloadAllowedNetworkTypes {
}