package com.ericyl.utils.ui.widget.support;

import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.NestedScrollView.OnScrollChangeListener;
import android.view.View;
import android.view.ViewGroup;

public class NestedEndlessScrollListener implements OnScrollChangeListener {

    private boolean loading = false;
    private int visibleThresholdDistance = 0;

    private IEndlessLoadingListener loadingListener;

    public NestedEndlessScrollListener(IEndlessLoadingListener loadingListener) {
        this.loadingListener = loadingListener;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }


    @Override
    public void onScrollChange(NestedScrollView scrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        ViewGroup layout = (ViewGroup) scrollView.getChildAt(0);
        View lastView = layout.getChildAt(layout.getChildCount() - 1);

        int distanceToEnd = (layout.getBottom() - (scrollView.getHeight() + scrollY));

        if (visibleThresholdDistance == 0)
            visibleThresholdDistance = lastView.getHeight() + lastView.getPaddingBottom();

        if (!loading && scrollY > oldScrollY && distanceToEnd <= visibleThresholdDistance)
            loadingListener.load();
    }


    public interface IEndlessLoadingListener {
        void load();
    }

}

