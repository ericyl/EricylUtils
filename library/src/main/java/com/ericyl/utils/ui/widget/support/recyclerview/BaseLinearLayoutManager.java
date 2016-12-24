package com.ericyl.utils.ui.widget.support.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class BaseLinearLayoutManager extends LinearLayoutManager implements IScrollManager {

    public BaseLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public boolean isTop(@NonNull RecyclerView recyclerView) {
        return 0 == findFirstVisibleItemPosition();
    }

    @Override
    public boolean isBottom(@NonNull RecyclerView recyclerView) {
        int lastVisiblePosition = findLastCompletelyVisibleItemPosition();
        int lastPosition = recyclerView.getAdapter().getItemCount() - 1;
        return lastVisiblePosition == lastPosition;
    }

}