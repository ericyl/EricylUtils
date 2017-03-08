package com.ericyl.example.ui.fragment.utils;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ericyl.example.R;
import com.ericyl.example.model.ui.UtilsInHomeEntry;
import com.ericyl.example.ui.adapter.RVUtilsAdapter;
import com.ericyl.example.ui.fragment.BaseSearchFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class UtilsFragment extends BaseSearchFragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.layout_appbar)
    AppBarLayout layoutAppbar;
    @BindView(R.id.rv_content)
    RecyclerView rvContent;

    private AppCompatActivity activity;

    private static final List<UtilsInHomeEntry> entries;

    static {
        entries = new ArrayList<>();
        entries.add(new UtilsInHomeEntry(R.drawable.ic_view_quilt_white_24dp, R.color.blue, R.string.ui, R.array.utils_ui_entries, R.id.rv_ui));
        entries.add(new UtilsInHomeEntry(R.drawable.ic_settings_ethernet_white_24dp, R.color.red, R.string.network, R.array.utils_network_entries, R.id.rv_network));
        entries.add(new UtilsInHomeEntry(R.drawable.ic_donut_small_white_24dp, R.color.orange_bright, R.string.os, R.array.utils_os_entries, R.id.rv_os));
        entries.add(new UtilsInHomeEntry(R.drawable.ic_extension_white_24dp, R.color.green, R.string.other, R.array.utils_other_entries, R.id.rv_other));

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_utils, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (AppCompatActivity) getActivity();
    }

    @Override
    protected void init(View view, @Nullable Bundle savedInstanceState) {
        super.init(view, savedInstanceState);
        initActionBar();

        rvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvContent.hasFixedSize();
        rvContent.setAdapter(new RVUtilsAdapter(getContext(), entries));

    }

    private void initActionBar() {
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar == null)
            return;
        actionBar.setTitle(R.string.utils);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_utils, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


}
