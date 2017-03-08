package com.ericyl.example.model.ui.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ericyl.example.R;
import com.ericyl.example.model.ui.SingleV1Model;
import com.ericyl.example.ui.fragment.recyclerview.IClickListener;
import com.ericyl.utils.annotation.LoadStatus;
import com.ericyl.utils.ui.widget.support.recyclerview.v1.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SingleV1ModelViewHolder extends BaseViewHolder {


    @BindView(R.id.tv_title)
    TextView tvTitle;

    private List<SingleV1Model> singleV1Models;

    private IClickListener clickListener;


    public void setClickListener(IClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public SingleV1ModelViewHolder(Context context,
                                   List<SingleV1Model> singleV1Models, ViewGroup viewGroup) {
        super(context, R.layout.layout_single_recyclerview_v1_rv_item, viewGroup);
        ButterKnife.bind(this, itemView);
        this.singleV1Models = singleV1Models;
    }


    @Override
    public void fitEvents(final int position) {
        if (clickListener != null)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClickListener(position);
                }
            });
    }

    @Override
    public void fitData(int position) {
        SingleV1Model singleV1Model = singleV1Models.get(position);
        tvTitle.setText(singleV1Model.getTitle());
    }

    @Override
    public void fitFooterData(@LoadStatus int loadStatus) {
    }


}
