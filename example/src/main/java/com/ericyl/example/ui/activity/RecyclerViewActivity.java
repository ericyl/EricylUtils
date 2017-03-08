package com.ericyl.example.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.ericyl.example.R;
import com.ericyl.example.model.ui.TabInfo;
import com.ericyl.example.ui.adapter.VPRecyclerViewAdapter;
import com.ericyl.example.ui.fragment.recyclerview.MultilayerRecyclerViewFragment;
import com.ericyl.example.ui.fragment.recyclerview.SingleRecyclerViewFragment;
import com.ericyl.example.util.AppProperties;
import com.ericyl.utils.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.layout_tab)
    TabLayout layoutTab;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    private static final List<TabInfo> tabInfos;

    static {
        tabInfos = new ArrayList<TabInfo>() {{
            add(new TabInfo(StringUtils.getString(AppProperties.getContext(), R.string.single_recycler_view), new SingleRecyclerViewFragment()));
            add(new TabInfo(StringUtils.getString(AppProperties.getContext(), R.string.multilayer_recycler_view), new MultilayerRecyclerViewFragment()));
        }};

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initActionBar(toolbar);

        VPRecyclerViewAdapter adapter = new VPRecyclerViewAdapter(getSupportFragmentManager(), tabInfos);
        vpContent.setAdapter(adapter);
        layoutTab.setupWithViewPager(vpContent);
    }

}
