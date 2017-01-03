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
import com.ericyl.example.ui.activity.DownloadActivity;
import com.ericyl.example.ui.activity.RecyclerViewActivity;
import com.ericyl.example.ui.activity.RootActivity;
import com.ericyl.utils.util.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RVUtilsEntriesAdapter extends RecyclerView.Adapter<RVUtilsEntriesAdapter.ViewHolder> {

    private String[] titles;
    @IdRes
    private int typeIdRes;

    private Context context;

    private static final int[] uiIds;
    private static final int[] networkIds;
    private static final int[] osIds;
    private static final int[] otherIds;

    static {
        uiIds = new int[]{R.id.recyclerview_utils, R.id.report_utils};
        networkIds = new int[]{R.id.download_utils, R.id.wifi_utils};
        osIds = new int[]{R.id.os_info_utils, R.id.package_utils, R.id.phone_manufacturer_utils, R.id.clear_cache_utils};
        otherIds = new int[]{R.id.root_utils, R.id.others_utils};
    }


    public RVUtilsEntriesAdapter(Context context, String[] titles, @IdRes int typeIdRes) {
        this.context = context;
        this.titles = titles;
        this.typeIdRes = typeIdRes;
    }

    public RVUtilsEntriesAdapter(Context context, @ArrayRes int titlesIdRes, @IdRes int typeIdRes) {
        this.context = context;
        this.titles = context.getResources().getStringArray(titlesIdRes);
        this.typeIdRes = typeIdRes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rv_utils_in_home_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvTitle.setText(titles[position]);
        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idRes = getUtilsInHomeEvent(position);
                jumpActivityByIdRes(idRes);
            }
        });
    }

    public int getUtilsInHomeEvent(int position) {
        int idRes;
        switch (typeIdRes) {
            default:
            case R.id.rv_ui:
                idRes = uiIds[position];
                break;
            case R.id.rv_network:
                idRes = networkIds[position];
                break;
            case R.id.rv_os:
                idRes = osIds[position];
                break;
            case R.id.rv_other:
                idRes = otherIds[position];
                break;
        }
        return idRes;
    }

    private void jumpActivityByIdRes(@IdRes int idRes) {
        switch (idRes) {
            case R.id.recyclerview_utils:
                ActivityUtils.jumpActivity(context, RecyclerViewActivity.class);
                break;
            case R.id.root_utils:
                ActivityUtils.jumpActivity(context, RootActivity.class);
                break;
            case R.id.download_utils:
                ActivityUtils.jumpActivity(context, DownloadActivity.class);
                break;
        }

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
