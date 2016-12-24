package com.ericyl.utils.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

import com.ericyl.utils.ui.activity.BaseReportActivity;

public class CHScrollView extends HorizontalScrollView {

    public BaseReportActivity activity;

    public CHScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        activity = (BaseReportActivity) context;
    }

    public CHScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (BaseReportActivity) context;
    }

    public CHScrollView(Context context) {
        super(context);
        activity = (BaseReportActivity) context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        activity.touchView = this;
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (activity.touchView == this) {
            activity.onScrollChanged(l, t, oldl, oldt);
        } else {
            super.onScrollChanged(l, t, oldl, oldt);
        }
    }
}
