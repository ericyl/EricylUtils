package com.ericyl.utils.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.widget.HorizontalScrollView;

import com.ericyl.utils.ui.widget.CHScrollView;

import java.util.ArrayList;
import java.util.List;

public class BaseReportActivity extends AppCompatActivity {


    public HorizontalScrollView touchView;
    public List<CHScrollView> chScrollViews = new ArrayList<>();

    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        for (CHScrollView scrollView : chScrollViews) {
            if (touchView != scrollView)
                scrollView.smoothScrollTo(l, t);
        }
    }


}
