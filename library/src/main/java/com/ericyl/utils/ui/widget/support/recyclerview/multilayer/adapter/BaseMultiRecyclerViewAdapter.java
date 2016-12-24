package com.ericyl.utils.ui.widget.support.recyclerview.multilayer.adapter;

import android.support.v7.widget.RecyclerView;

import com.ericyl.utils.ui.widget.support.recyclerview.multilayer.IMultiRecyclerViewPosition;

import java.util.List;

public abstract class BaseMultiRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements IMultiRecyclerViewPosition {

    @Override
    public int getHeaderPosition(int position) {
        if (position < 0)
            throw new IllegalArgumentException("position is negative number");
        List<Integer> counts = getViewItemCount();
        int num = 0;
        for (int i = 0; i < counts.size(); i++) {
            num += counts.get(i);
            if (num > position)
                return i;
        }
        return 0;
    }

    @Override
    public int getBodyPosition(int position) {
        if (position < 0)
            throw new IllegalArgumentException("position is negative number");
        int headerCount = getHeaderPosition(position);
        List<Integer> counts = getViewItemCount();
        int num = 0;
        for (int i = 0; i < headerCount; i++) {
            num += counts.get(i);
        }
        return position - num - 1;
    }

}