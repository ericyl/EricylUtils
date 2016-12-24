package com.ericyl.example.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ericyl.example.R;
import com.ericyl.example.event.JumpHomeTabEvent;
import com.ericyl.example.util.BusProvider;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RVMainAdapter extends RecyclerView.Adapter<RVMainAdapter.ViewHolder> {

    private String[] titles;
    private int[] ids;

    public RVMainAdapter(String[] titles, int[] ids) {
        this.titles = titles;
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
                BusProvider.getInstance().post(new JumpHomeTabEvent(id));
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
