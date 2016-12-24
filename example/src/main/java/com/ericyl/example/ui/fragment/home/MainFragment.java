package com.ericyl.example.ui.fragment.home;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ericyl.example.R;
import com.ericyl.example.event.JumpNavPageEvent;
import com.ericyl.example.ui.BaseFragment;
import com.ericyl.example.ui.adapter.RVMainAdapter;
import com.ericyl.example.util.AppProperties;
import com.ericyl.example.util.BusProvider;
import com.ericyl.utils.util.StringUtils;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class MainFragment extends BaseFragment {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_author)
    TextView tvAuthor;
    @BindView(R.id.rv_main)
    RecyclerView rvMain;

    private static final String[] titles;
    private static final int[] tabIds;



    static {
        titles = StringUtils.getString(AppProperties.getContext(), R.string.utils,
                R.string.network, R.string.barcode, R.string.crypto);
        tabIds = new int[]{R.id.tab_utils, R.id.tab_network, R.id.tab_barcode, R.id.tab_crypto};
    }

    public MainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_main;
    }


    @Override
    protected void init(View view, @Nullable Bundle savedInstanceState) {
        super.init(view, savedInstanceState);
        Typeface tfGloriaHallelujah = Typeface.createFromAsset(AppProperties.getContext().getAssets(), "fonts/Gloria_Hallelujah/GloriaHallelujah.ttf");
        tvTitle.setTypeface(tfGloriaHallelujah);
        Typeface tfIndieFlower = Typeface.createFromAsset(AppProperties.getContext().getAssets(), "fonts/Indie_Flower/IndieFlower.ttf");
        tvAuthor.setTypeface(tfIndieFlower);
        rvMain.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMain.hasFixedSize();
        rvMain.setAdapter(new RVMainAdapter(titles, tabIds));
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


    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);

    }

    @OnClick({R.id.layout_title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_title:
                Random random = new Random();
                String str = getResources().getStringArray(R.array.hello)[random.nextInt(9)];
                Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @OnLongClick({R.id.layout_title})
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.layout_title:
                BusProvider.getInstance().post(new JumpNavPageEvent(R.id.nav_about, false));
                break;
        }
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);

    }


}
