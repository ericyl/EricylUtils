package com.ericyl.example.ui.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ericyl.example.R;
import com.ericyl.example.event.JumpHomeTabEvent;
import com.ericyl.example.model.ui_model.TabInfo;
import com.ericyl.example.ui.BaseFragment;
import com.ericyl.example.ui.adapter.VPHomeAdapter;
import com.ericyl.example.util.AppProperties;
import com.ericyl.example.util.BusProvider;
import com.ericyl.utils.util.StringUtils;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.layout_tab)
    TabLayout layoutTab;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    private static final ArrayList<TabInfo> tabInfos;
    private static final List<Integer> tabIds;

    private AppCompatActivity activity;

    static {
        tabInfos = new ArrayList<>();
        tabInfos.add(new TabInfo(StringUtils.getString(AppProperties.getContext(), R.string.main_page), new MainFragment()));
        tabInfos.add(new TabInfo(StringUtils.getString(AppProperties.getContext(), R.string.utils), new UtilsInHomeFragment()));
        tabInfos.add(new TabInfo(StringUtils.getString(AppProperties.getContext(), R.string.network), new NetworkInHomeFragment()));
        tabInfos.add(new TabInfo(StringUtils.getString(AppProperties.getContext(), R.string.barcode), new BarcodeInHomeFragment()));
        tabInfos.add(new TabInfo(StringUtils.getString(AppProperties.getContext(), R.string.crypto), new CryptoInHomeFragment()));
        tabIds = new ArrayList<Integer>() {
            {
                add(R.id.tab_main);
                add(R.id.tab_utils);
                add(R.id.tab_network);
                add(R.id.tab_barcode);
                add(R.id.tab_crypto);
            }
        };
    }


    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (AppCompatActivity) getActivity();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_home;
    }


    @Override
    protected void init(View view, @Nullable Bundle savedInstanceState) {
        super.init(view, savedInstanceState);
        initActionBar();

        VPHomeAdapter adapter = new VPHomeAdapter(getChildFragmentManager(), tabInfos);
        vpContent.setAdapter(adapter);
        layoutTab.setupWithViewPager(vpContent);

    }

//    private void animateColors(int previousColor, int color) {
//        ValueAnimator anim = new ValueAnimator();
//        anim.setIntValues(previousColor, color);
//        anim.setEvaluator(new ArgbEvaluator());
//        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                setColors((Integer) valueAnimator.getAnimatedValue());
//            }
//        });
//
//        anim.setDuration(150);
//        anim.start();
//    }
//
//    private void setColors(int color) {
//        layoutTab.setBackgroundColor(color);
////        toolbar_tab.setSelectedTabIndicatorColor(colorBurn(vibrant.getRgb()));
//        toolbar.setBackgroundColor(color);
//
//    }


    private void initActionBar() {
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar == null)
            return;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);

    }

    @Subscribe
    public void jumpOtherTabEvent(JumpHomeTabEvent event) {
        vpContent.setCurrentItem(tabIds.indexOf(event.getIdRes()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }


}
