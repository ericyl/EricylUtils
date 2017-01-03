package com.ericyl.example.model.ui;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.view.View;

public class FabButton {
    @IdRes
    private int idRes;
    @DrawableRes
    private int imageRes;
    @StringRes
    private int labelIdRes;
    private View.OnClickListener onClickListener;

    public FabButton(@IdRes int idRes, @DrawableRes int imageRes, @StringRes int labelIdRes, View.OnClickListener onClickListener) {
        this.idRes = idRes;
        this.imageRes = imageRes;
        this.labelIdRes = labelIdRes;
        this.onClickListener = onClickListener;
    }

    @IdRes
    public int getIdRes() {
        return idRes;
    }

    public void setIdRes(@IdRes int idRes) {
        this.idRes = idRes;
    }

    @DrawableRes
    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(@DrawableRes int imageRes) {
        this.imageRes = imageRes;
    }

    @StringRes
    public int getLabelIdRes() {
        return labelIdRes;
    }

    public void setLabelIdRes(@StringRes int labelIdRes) {
        this.labelIdRes = labelIdRes;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
