package com.ericyl.example.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ericyl.example.R;
import com.ericyl.utils.ui.widget.support.recyclerview.multilayer.adapter.BaseMultiRecyclerViewAdapter;
import com.ericyl.utils.util.ColorUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RVMultiAdapter extends BaseMultiRecyclerViewAdapter<RVMultiAdapter.ViewHolder> {
    public static final int HEADER = 1;
    public static final int BODY = 2;
    private List<String> list;
    private List<String> headers;
    private List<List<String>> bodies;


    public RVMultiAdapter(List<String> headers, List<List<String>> bodies) {
        this.headers = headers;
        this.bodies = bodies;
        list = new ArrayList<>();
        for (int i = 0; i < headers.size(); i++) {
            String header = headers.get(i);
            if (header == null)
                continue;
            list.add(header);
            list.addAll(bodies.get(i));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rv_main, parent, false);
        switch (viewType) {
            default:
            case BODY:
                view.setBackgroundColor(ColorUtils.COLOR_YELLOW);
                viewHolder = new ViewHolder(view);
                break;
            case HEADER:
                view.setBackgroundColor(ColorUtils.COLOR_RED);
                viewHolder = new ViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTitle.setText(list.get(position));
    }


    @Override
    public int getItemViewType(int position) {
        if (list.get(position).startsWith("header"))
            return HEADER;
        else
            return BODY;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public List<Integer> getViewItemCount() {
        List<Integer> counts = new ArrayList<>();
        for (int i = 0; i < headers.size(); i++) {
            String header = headers.get(i);
            if (header == null)
                continue;
            counts.add(1 + bodies.get(i).size());
        }
        return counts;
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