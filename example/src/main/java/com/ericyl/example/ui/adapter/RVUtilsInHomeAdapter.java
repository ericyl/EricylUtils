package com.ericyl.example.ui.adapter;

import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ericyl.example.R;
import com.ericyl.example.event.JumpNavPageEvent;
import com.ericyl.example.util.BusProvider;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RVUtilsInHomeAdapter extends RecyclerView.Adapter<RVUtilsInHomeAdapter.ViewHolder> {

    private String[] titles;
    private int[] ids;
    private Context context;

    public RVUtilsInHomeAdapter(Context context, @ArrayRes int titleIdRes, int[] ids) {
        this.context = context;
        this.titles = context.getResources().getStringArray(titleIdRes);
        this.ids = ids;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rv_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvTitle.setText(titles[position]);
        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = ids[position];
                if (id == R.id.more_utils){
                    BusProvider.getInstance().post(new JumpNavPageEvent(R.id.nav_utils, 0));
                }else{

                }
            }
        });
    }





    @Override
    public int getItemCount() {
        return titles.length;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
