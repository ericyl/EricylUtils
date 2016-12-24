package com.ericyl.utils.ui.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

@Deprecated
public class NoScrollerListView extends ListView {

    public NoScrollerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mExpandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }

}
