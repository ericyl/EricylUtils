package com.ericyl.example.model.ui;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;


public class BarcodeInHomeModel {

    @StringRes
    private final int title;
    @StringRes
    private final int iconText;
    @ColorInt
    private final int iconTextColor;
    @ColorInt
    private final int iconBackgroundColor;

    public BarcodeInHomeModel(Context context, @StringRes int title, @StringRes int iconText, @ColorRes int iconTextColor, @ColorRes int iconBackgroundColor) {
        this(title, iconText, ContextCompat.getColor(context, iconTextColor), ContextCompat.getColor(context, iconBackgroundColor));
    }

    public BarcodeInHomeModel(@StringRes int title, @StringRes int iconText, @ColorInt int iconTextColor, @ColorInt int iconBackgroundColor) {
        this.title = title;
        this.iconText = iconText;
        this.iconTextColor = iconTextColor;
        this.iconBackgroundColor = iconBackgroundColor;
    }

    public int getTitle() {
        return title;
    }

    public int getIconText() {
        return iconText;
    }

    public int getIconTextColor() {
        return iconTextColor;
    }

    public int getIconBackgroundColor() {
        return iconBackgroundColor;
    }
}
