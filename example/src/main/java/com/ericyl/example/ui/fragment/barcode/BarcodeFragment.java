package com.ericyl.example.ui.fragment.barcode;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
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
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.ericyl.example.R;
import com.ericyl.example.event.InitFabEvent;
import com.ericyl.example.event.SendFabEvent;
import com.ericyl.example.model.ui_model.FabButton;
import com.ericyl.example.model.ui_model.TabInfo;
import com.ericyl.example.ui.BaseFragment;
import com.ericyl.example.ui.adapter.VPBarcodeAdapter;
import com.ericyl.example.util.AppProperties;
import com.ericyl.example.util.BusProvider;
import com.ericyl.utils.util.StringUtils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class BarcodeFragment extends BaseFragment {

    private AppCompatActivity activity;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.layout_tab)
    TabLayout layoutTab;
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    @BindView(R.id.fab_more_menu)
    FloatingActionMenu fabMoreMenu;

    private static final List<TabInfo> tabInfos;
    private static final List<Integer> tabIds;
    private int tabIdRes = R.id.tab_build_barcode;

    private static BuildBarcodeFragment buildBarcodeFragment;
    private static ScanBarcodeFragment scanBarcodeFragment;

    static {
        buildBarcodeFragment = new BuildBarcodeFragment();
        scanBarcodeFragment = new ScanBarcodeFragment();
        tabInfos = new ArrayList<TabInfo>() {{
            add(new TabInfo(StringUtils.getString(AppProperties.getContext(), R.string.build_barcode), buildBarcodeFragment, R.id.tab_build_barcode));
            add(new TabInfo(StringUtils.getString(AppProperties.getContext(), R.string.distinguish_barcode), scanBarcodeFragment, R.id.tab_scan_barcode));
        }};
        Integer[] tabs = new Integer[]{R.id.tab_build_barcode, R.id.tab_scan_barcode};
        tabIds = new ArrayList<>(Arrays.asList(tabs));
    }


    @Override
    public boolean setTabIdRes(int tabIdRes) {
        boolean flag = super.setTabIdRes(tabIdRes);
        if (flag)
            this.tabIdRes = tabIdRes;
        return flag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (AppCompatActivity) getActivity();
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_barcode;
    }


    @Override
    protected void init(View view, @Nullable Bundle savedInstanceState) {
        super.init(view, savedInstanceState);
        initActionBar();
        if (tabIdRes == R.id.tab_build_barcode)
            buildBarcodeFragment.setFabMoreMenu(fabMoreMenu);
        else
            scanBarcodeFragment.setFabMoreMenu(fabMoreMenu);

        VPBarcodeAdapter adapter = new VPBarcodeAdapter(getChildFragmentManager(), tabInfos);
        vpContent.setAdapter(adapter);
        layoutTab.setupWithViewPager(vpContent);
        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                fabMoreMenu.removeAllMenuButtons();
                BusProvider.getInstance().post(new InitFabEvent(tabIds.get(position), fabMoreMenu));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        fabMoreMenu.setClosedOnTouchOutside(true);
        createCustomAnimation();
        vpContent.setCurrentItem(tabIds.indexOf(tabIdRes));
    }


    private void createCustomAnimation() {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(fabMoreMenu.getMenuIconView(), "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(fabMoreMenu.getMenuIconView(), "scaleY", 1.0f, 0.2f);

        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(fabMoreMenu.getMenuIconView(), "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(fabMoreMenu.getMenuIconView(), "scaleY", 0.2f, 1.0f);

        scaleOutX.setDuration(200);
        scaleOutY.setDuration(200);

        scaleInX.setDuration(200);
        scaleInY.setDuration(200);


        scaleInX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                fabMoreMenu.getMenuIconView().setImageResource(fabMoreMenu.isOpened()
                        ? R.drawable.ic_close_white_24dp : R.drawable.ic_more_horiz_white_24dp);
            }

        });

        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator());

        fabMoreMenu.setIconToggleAnimatorSet(set);
    }

    private void initActionBar() {
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar == null)
            return;
        actionBar.setTitle(R.string.barcode);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
    }


    public static List<FloatingActionButton> createFloatingActionButton(Context context, FabButton... events) {
        List<FloatingActionButton> buttons = new ArrayList<>();
        for (FabButton event : events) {
            FloatingActionButton fab = new FloatingActionButton(context);
            fab.setId(event.getIdRes());
            fab.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            fab.setImageResource(event.getImageRes());
            fab.setColorNormalResId(R.color.blue);
            fab.setColorPressedResId(R.color.dark_blue);
            fab.setColorRippleResId(R.color.white);
            fab.setLabelText(context.getString(event.getLabelIdRes()));
            fab.setButtonSize(FloatingActionButton.SIZE_MINI);
            View.OnClickListener clickListener = event.getOnClickListener();
            if (clickListener != null)
                fab.setOnClickListener(clickListener);
            buttons.add(fab);
        }
        return buttons;
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

    @Subscribe
    public void aaa1(SendFabEvent event) {
        BusProvider.getInstance().post(new InitFabEvent(event.getIdRes(), fabMoreMenu));
    }

}
