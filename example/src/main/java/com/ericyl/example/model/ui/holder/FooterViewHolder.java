package com.ericyl.example.model.ui.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.ericyl.example.R;
import com.ericyl.utils.annotation.LoadStatus;
import com.ericyl.utils.ui.widget.LoadingLayout;
import com.ericyl.utils.ui.widget.support.recyclerview.v1.BaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FooterViewHolder extends BaseViewHolder {

    @BindView(R.id.layout_loading)
    LoadingLayout layoutLoading;

    private ILoadingClickListener loadingClickListener;

    public void setLoadingClickListener(ILoadingClickListener loadingClickListener) {
        this.loadingClickListener = loadingClickListener;
    }

    public FooterViewHolder(Context context,
                            ViewGroup viewGroup) {
        super(context, R.layout.layout_loading_more, viewGroup);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void fitEvents(int position) {
//        Log.v("clickListener", (clickListener == null) + "");
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickListener.onErrorClickListener();
//            }
//        });
//        if (clickListener != null)

        if (loadingClickListener != null && layoutLoading.getLoadStatus() == LoadStatus.FAILED) {
            layoutLoading.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadingClickListener.onErrorClickListener();
                }
            });
        } else {
            layoutLoading.setClickable(false);
        }

    }

    @Override
    public void fitData(int position) {
        layoutLoading.setStatus(LoadStatus.LOADING);

    }

    @Override
    public void fitFooterData(@LoadStatus int dataStatus) {
        layoutLoading.setStatus(dataStatus);


    }

    public interface ILoadingClickListener {
        void onErrorClickListener();
    }
}
