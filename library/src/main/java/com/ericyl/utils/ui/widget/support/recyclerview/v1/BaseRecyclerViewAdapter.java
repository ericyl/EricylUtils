package com.ericyl.utils.ui.widget.support.recyclerview.v1;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ericyl.utils.annotation.LoadStatus;

import java.util.List;

public abstract class BaseRecyclerViewAdapter<T extends IViewHolderType> extends RecyclerView.Adapter<BaseViewHolder> implements IFooterController {

    private List<T> list;
    private IViewHolderType footerView = new IViewHolderType() {
        @Override
        public int type() {
            return IViewHolderType.FOOTER;
        }
    };
    @LoadStatus
    private int loadStatus;

    public List<T> getList() {
        return list;
    }

    private boolean hasFooter() {
        return list != null && list.size() > 0 && list.get(list.size() - 1).type() == IViewHolderType.FOOTER;
    }

    @LoadStatus
    public int getLoadStatus() {
        return loadStatus;
    }

    public void setLoadStatus(@LoadStatus int loadStatus) {
        this.loadStatus = loadStatus;
        if (hasFooter())
            notifyItemChanged(list.size() - 1);
    }

    public BaseRecyclerViewAdapter(@NonNull List<T> list) {
        this.list = list;
    }

    @Override
    public void addFooterView() {
        if (!hasFooter()) {
            list.add((T) footerView);
            notifyItemInserted(list.size());
        }
    }

    @Override
    public void removeFooterView() {
        if (hasFooter()) {
            list.remove(list.size() - 1);
            notifyItemRemoved(list.size());
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return getViewHolderByType(viewGroup, viewType);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (list.size() == 0 || position < 0)
            return;
        if (hasFooter() && position == list.size() - 1)
            holder.fitFooterData(getLoadStatus());
        else
            holder.fitData(position);
        holder.fitEvents(position);
    }

    public abstract BaseViewHolder getViewHolderByType(ViewGroup viewGroup, int type);

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type();
    }

}