package com.ericyl.example.ui.fragment.network;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ericyl.example.R;
import com.ericyl.example.model.ui.TabInfo;
import com.ericyl.example.ui.adapter.VPHomeAdapter;
import com.ericyl.example.ui.fragment.BaseFragment;
import com.ericyl.example.ui.fragment.BlankFragment;
import com.ericyl.example.util.AppProperties;
import com.ericyl.utils.util.StringUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NetworkFragment extends BaseFragment {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.layout_tab)
    TabLayout layoutTab;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    private AppCompatActivity activity;

    private static final ArrayList<TabInfo> tabInfos;


    @IdRes
    private int tabIdRes = R.id.tab_volley;
    private static final String PARAM_TAB_ID_RES = "tabIdRes";

    static {
        tabInfos = new ArrayList<>();
        tabInfos.add(new TabInfo(StringUtils.getString(AppProperties.getContext(), R.string.volley), new BlankFragment(), R.id.tab_volley));
//        tabInfos.add(new TabInfo(EricylUtils.getString(AppProperties.getContext(), R.string.ksoap), new BlankFragment(), R.id.tab_ksoap));
        tabInfos.add(new TabInfo(StringUtils.getString(AppProperties.getContext(), R.string.okHttp), new BlankFragment(), R.id.tab_okhttp));
    }

    public static NetworkFragment newInstance(@IdRes int tabIdRes) {
        NetworkFragment networkFragment = new NetworkFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(PARAM_TAB_ID_RES, tabIdRes);
        networkFragment.setArguments(arguments);
        return networkFragment;
    }

    @Override
    public boolean setTabIdRes(@IdRes int tabIdRes) {
        boolean flag = super.setTabIdRes(tabIdRes);
        if (flag)
            this.tabIdRes = tabIdRes;
        return flag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_network, container, false);
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

        VPHomeAdapter adapter = new VPHomeAdapter(getChildFragmentManager(), tabInfos);
        vpContent.setAdapter(adapter);
        layoutTab.setupWithViewPager(vpContent);

        int position = getPositionInViewPage(tabIdRes);
        vpContent.setCurrentItem(position);
    }

    private void initActionBar() {
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar == null)
            return;
        actionBar.setTitle(R.string.network);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
    }

    private int getPositionInViewPage(@IdRes int tabIdRes) {
        for (int i = 0; i < tabInfos.size(); i++) {
            TabInfo tabInfo = tabInfos.get(i);
            if (tabInfo.getIdRes() == tabIdRes)
                return i;
        }
        return 0;
    }

}
