package com.ericyl.utils.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({LoadStatus.LOADING, LoadStatus.FAILED, LoadStatus.FINISH})
@Retention(RetentionPolicy.SOURCE)
public @interface LoadStatus {
    int LOADING = 0;
    int FAILED = 1;
    int FINISH = 2;
}