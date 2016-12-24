package com.ericyl.utils.ui.widget.translucent;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.design.internal.ScrimInsetsFrameLayout;
import android.util.AttributeSet;

import com.ericyl.utils.util.OSInfoUtils;


public class TransStatusBarFrameLayout extends ScrimInsetsFrameLayout {

    public TransStatusBarFrameLayout(Context context) {
        super(context);
        setStatusBarPadding(context);
    }

    public TransStatusBarFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setStatusBarPadding(context);
    }

    public TransStatusBarFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setStatusBarPadding(context);
    }

    private void setStatusBarPadding(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && (Build.VERSION.SDK_INT == Build.VERSION_CODES.N && !((Activity) context).isInMultiWindowMode()))
            setPadding(getPaddingLeft(), getPaddingTop() + OSInfoUtils.getStatusBarHeight(context), getPaddingRight(), getPaddingBottom());
    }

}
