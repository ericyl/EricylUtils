package com.ericyl.utils.ui.widget.support.recyclerview.v1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ericyl.utils.annotation.LoadStatus;


public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(Context context, int id, ViewGroup viewGroup) {
        super(LayoutInflater.from(context).inflate(id, viewGroup, false));
    }

    protected abstract void fitEvents(int position);

    /**
     * 对指定position的item进行数据的适配
     *
     * @param position position
     */
    protected abstract void fitData(int position);

    /**
     * 对FooterView进行数据的适配
     *
     * @param loadStatus data status {@link LoadStatus}
     */
    protected abstract void fitFooterData(@LoadStatus int loadStatus);
}
