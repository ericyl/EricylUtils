package com.ericyl.example.model.ui;

import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;


public class UtilsInHomeEntry {

    @DrawableRes
    private int logoIdRes;
    @ColorRes
    private int logoBackgroundColorRes;
    @StringRes
    private int titleIdRes;
    @ArrayRes
    private int contents;
    @IdRes
    private int typeIdRes;

    public UtilsInHomeEntry(@DrawableRes int logoIdRes, @ColorRes int logoBackgroundColorRes, @StringRes int titleIdRes, @ArrayRes int contents, @IdRes int typeIdRes) {
        this.logoIdRes = logoIdRes;
        this.logoBackgroundColorRes = logoBackgroundColorRes;
        this.titleIdRes = titleIdRes;
        this.contents = contents;
        this.typeIdRes = typeIdRes;
    }

    public int getLogoIdRes() {
        return logoIdRes;
    }

    public void setLogoIdRes(int logoIdRes) {
        this.logoIdRes = logoIdRes;
    }

    public int getLogoBackgroundColorRes() {
        return logoBackgroundColorRes;
    }

    public void setLogoBackgroundColorRes(int logoBackgroundColorRes) {
        this.logoBackgroundColorRes = logoBackgroundColorRes;
    }

    public int getTitleIdRes() {
        return titleIdRes;
    }

    public void setTitleIdRes(int titleIdRes) {
        this.titleIdRes = titleIdRes;
    }

    public int getContents() {
        return contents;
    }

    public void setContents(int contents) {
        this.contents = contents;
    }

    public int getTypeIdRes() {
        return typeIdRes;
    }

    public void setTypeIdRes(int typeIdRes) {
        this.typeIdRes = typeIdRes;
    }
}
