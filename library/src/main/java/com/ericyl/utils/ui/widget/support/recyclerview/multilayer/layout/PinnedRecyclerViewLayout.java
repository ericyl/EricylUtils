package com.ericyl.utils.ui.widget.support.recyclerview.multilayer.layout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ericyl.utils.ui.widget.support.recyclerview.multilayer.adapter.BaseMultiRecyclerViewAdapter;


public class PinnedRecyclerViewLayout extends RelativeLayout {

    private BaseMultiRecyclerViewAdapter adapter;

    private View pinnedView;
    private LinearLayoutManager layoutManager;
    private int type;

    private OnRecyclerViewPinnedViewListener onRecyclerViewPinnedViewListener;

    public void setOnRecyclerViewPinnedViewListener(OnRecyclerViewPinnedViewListener onRecyclerViewPinnedViewListener) {
        this.onRecyclerViewPinnedViewListener = onRecyclerViewPinnedViewListener;
    }

    public PinnedRecyclerViewLayout(Context context) {
        super(context);
    }

    public PinnedRecyclerViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PinnedRecyclerViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initRecyclerPinned(@NonNull RecyclerView recyclerView, @NonNull View pinnedView, int type) {
        if (recyclerView.getAdapter() == null)
            throw new IllegalArgumentException("getAdapter() is null");

        if (!(recyclerView.getAdapter() instanceof BaseMultiRecyclerViewAdapter))
            throw new IllegalArgumentException("the RecyclerView adapter must be extends BaseMultiRecyclerViewAdapter");
        else
            adapter = (BaseMultiRecyclerViewAdapter) recyclerView.getAdapter();

        if (recyclerView.getLayoutManager() == null)
            throw new IllegalArgumentException("getLayoutManager() is null");

        if (!(recyclerView.getLayoutManager() instanceof LinearLayoutManager))

            throw new IllegalArgumentException("the RecyclerView layoutManager must be extends LinearLayoutManager");

        this.layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        this.type = type;

        pinnedView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.pinnedView = pinnedView;
        addView(this.pinnedView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                refreshPinnedView();
            }
        });
        this.pinnedView.setVisibility(GONE);
    }


    public void refreshPinnedView() {
        if (pinnedView == null || layoutManager == null || onRecyclerViewPinnedViewListener == null || adapter == null)
            return;

        if (pinnedView.getVisibility() != VISIBLE)
            pinnedView.setVisibility(VISIBLE);

        int curPosition = layoutManager.findFirstVisibleItemPosition();
        if (RecyclerView.NO_POSITION == curPosition)
            return;


        View curItemView = layoutManager.findViewByPosition(curPosition);
        if (curItemView == null)
            return;

        onRecyclerViewPinnedViewListener.initPinnedView(this, pinnedView,
                adapter.getHeaderPosition(curPosition));

        int displayTop;
        int itemHeight = curItemView.getHeight();
        int curTop = curItemView.getTop();
        int floatHeight = pinnedView.getHeight();
        if (curTop < floatHeight - itemHeight)
            displayTop = itemHeight + curTop - floatHeight;
        else
            displayTop = 0;
        LayoutParams lp = (LayoutParams) pinnedView.getLayoutParams();
        if (adapter.getItemViewType(curPosition + 1) == type)
            lp.topMargin = displayTop;
        else
            lp.topMargin = 0;
        pinnedView.setLayoutParams(lp);
        pinnedView.invalidate();


    }

    public interface OnRecyclerViewPinnedViewListener {
        void initPinnedView(PinnedRecyclerViewLayout pinnedRecyclerViewLayout, View pinnedView, int position);
    }

}