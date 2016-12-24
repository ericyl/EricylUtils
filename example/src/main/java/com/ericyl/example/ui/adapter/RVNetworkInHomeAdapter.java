package com.ericyl.example.ui.adapter;

import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ericyl.example.R;
import com.ericyl.example.event.JumpNavPageEvent;
import com.ericyl.example.util.AppProperties;
import com.ericyl.example.util.BusProvider;
import com.ericyl.utils.ui.widget.CircleTextView;
import com.squareup.otto.Produce;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ericyl on 2016/10/15.
 */

public class RVNetworkInHomeAdapter extends RecyclerView.Adapter<RVNetworkInHomeAdapter.ViewHolder> {


    private String[] entries;
//    private static final int colors = int[]{R.color.}

    public RVNetworkInHomeAdapter(Context context, @ArrayRes int arrayIdRes) {
        this.entries = context.getResources().getStringArray(arrayIdRes);
    }

    public RVNetworkInHomeAdapter(String[] entries) {
        this.entries = entries;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rv_network_in_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvTitle.setText(entries[position]);
        holder.ctvIcon.setText(entries[position].substring(0, 1));
        holder.ctvIcon.setRandomBackgroundColor(AppProperties.getContext());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusProvider.getInstance().post(getPageChangeEvent(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return entries.length;
    }

    @Produce
    public JumpNavPageEvent getPageChangeEvent(int position) {
        @IdRes int idRes;
        switch (position) {
            default:
            case 0:
                idRes = R.id.tab_volley;
                break;
            case 1:
                idRes = R.id.tab_okhttp;
                break;
            case 2:
                idRes = R.id.tab_ksoap;
                break;
        }
        return new JumpNavPageEvent(R.id.nav_network, idRes);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ctv_icon)
        CircleTextView ctvIcon;
        @BindView(R.id.tv_title)
        TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
