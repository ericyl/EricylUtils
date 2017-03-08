package com.ericyl.utils.ui.widget.support.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class BaseLinearLayoutManager extends LinearLayoutManager implements IScrollManager {

    /**
     * @param context Current context, will be used to access resources.
     */
    public BaseLinearLayoutManager(Context context) {
        super(context);
    }

    /**
     * @param context     Current context, will be used to access resources.
     * @param orientation {@link #HORIZONTAL} or {@link #VERTICAL}
     */
    public BaseLinearLayoutManager(Context context, int orientation) {
        super(context);
        this.setOrientation(orientation);
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