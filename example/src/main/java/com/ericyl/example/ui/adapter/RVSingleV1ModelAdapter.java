package com.ericyl.example.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.ericyl.example.model.ui.SingleV1Model;
import com.ericyl.example.model.ui.holder.SingleV1ModelViewHolder;
import com.ericyl.example.model.ui.holder.FooterViewHolder;
import com.ericyl.example.ui.fragment.recyclerview.IClickListener;
import com.ericyl.utils.ui.widget.support.recyclerview.v1.BaseRecyclerViewAdapter;
import com.ericyl.utils.ui.widget.support.recyclerview.v1.BaseViewHolder;
import com.ericyl.utils.ui.widget.support.recyclerview.v1.IViewHolderType;

import java.util.List;


public class RVSingleV1ModelAdapter extends BaseRecyclerViewAdapter<SingleV1Model> {

    private Context context;

    private IClickListener itemClickListener;
    private FooterViewHolder.ILoadingClickListener footerClickListener;


    public void setItemClickListener(IClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setFooterClickListener(FooterViewHolder.ILoadingClickListener footerClickListener) {
        this.footerClickListener = footerClickListener;
    }

    public RVSingleV1ModelAdapter(Context context, List<SingleV1Model> singleV1Models) {
        super(singleV1Models);
        this.context = context;
    }

    @Override
    public BaseViewHolder getViewHolderByType(ViewGroup viewGroup, int type) {
        BaseViewHolder viewHolder = null;
        switch (type) {
            case IViewHolderType.BODY:
                viewHolder = new SingleV1ModelViewHolder(context, getList(), viewGroup);
                ((SingleV1ModelViewHolder) viewHolder).setClickListener(itemClickListener);
                break;
            case IViewHolderType.FOOTER:
                viewHolder = new FooterViewHolder(context, viewGroup);
                ((FooterViewHolder) viewHolder).setLoadingClickListener(footerClickListener);
                break;
        }
        return viewHolder;
    }


}