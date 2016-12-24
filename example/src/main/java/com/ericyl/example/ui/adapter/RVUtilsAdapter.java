package com.ericyl.example.ui.adapter;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ericyl.example.R;
import com.ericyl.example.model.ui_model.UtilsInHomeEntry;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ericyl on 2016/10/15.
 */

public class RVUtilsAdapter extends RecyclerView.Adapter<RVUtilsAdapter.ViewHolder> {


    private List<UtilsInHomeEntry> entries;
    private Context context;

    public RVUtilsAdapter(Context context, List<UtilsInHomeEntry> entries) {
        this.context = context;
        this.entries = entries;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rv_utils_in_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        UtilsInHomeEntry entry = entries.get(position);
        holder.civIcon.setImageResource(entry.getLogoIdRes());
//        ColorStateList colorStateList = AppCompatResources.getColorStateList(AppProperties.getContext(), entry.getLogoBackgroundColorRes());
        holder.civIcon.setFillColorResource(entry.getLogoBackgroundColorRes());
        holder.civIcon.setBorderColorResource(entry.getLogoBackgroundColorRes());

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
//        layoutManager.setSmoothScrollbarEnabled(true);
//        layoutManager.setAutoMeasureEnabled(true);

        holder.rvMain.setLayoutManager(layoutManager);
        holder.rvMain.hasFixedSize();
        holder.rvMain.setNestedScrollingEnabled(false);

        holder.rvMain.setAdapter(new RVUtilsEntriesAdapter(context, entry.getContents(), entry.getTypeIdRes()));
        holder.rvMain.addItemDecoration(new DividerItemDecoration(context, LinearLayout.VERTICAL));


        holder.tvTitle.setText(entries.get(position).getTitleIdRes());
    }


    @Override
    public int getItemCount() {
        return entries.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.civ_icon)
        CircleImageView civIcon;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.layout_title)
        LinearLayout layoutTitle;
        @BindView(R.id.rv_main)
        RecyclerView rvMain;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
