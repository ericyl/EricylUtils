package com.ericyl.example.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.ericyl.example.R;
import com.ericyl.example.model.ui.SearchInfo;
import com.ericyl.example.ui.fragment.recyclerview.IClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RVSearchSuggestionAdapter extends RecyclerView.Adapter<RVSearchSuggestionAdapter.ViewHolder> implements Filterable {
    private Filter filter;
    private List<SearchInfo> searchInfos;

    private IClickListener clickListener;

    public void setClickListener(IClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public RVSearchSuggestionAdapter(List<SearchInfo> searchInfos, Filter filter) {
        this.filter = filter;
        this.searchInfos = searchInfos;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }


    @Override
    public RVSearchSuggestionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_suggestion_item, parent, false);
        return new RVSearchSuggestionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RVSearchSuggestionAdapter.ViewHolder holder, final int position) {
        holder.tvTitle.setText(searchInfos.get(position).getTitle());
        holder.imgIcon.setImageResource(searchInfos.get(position).getIcon());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null)
                    clickListener.onItemClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchInfos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_icon)
        ImageView imgIcon;
        @BindView(R.id.tv_title)
        TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
