package com.ericyl.example.ui.fragment.recyclerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dgreenhalgh.android.simpleitemdecoration.linear.EndOffsetItemDecoration;
import com.ericyl.example.R;
import com.ericyl.example.ui.adapter.RVSingleAdapter;
import com.ericyl.example.ui.fragment.BaseFragment;
import com.ericyl.utils.annotation.LoadStatus;
import com.ericyl.utils.ui.widget.LoadingLayout;
import com.ericyl.utils.ui.widget.support.recyclerview.BaseLinearLayoutManager;
import com.ericyl.utils.util.DisplayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SingleRecyclerViewFragment extends BaseFragment {


    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    @BindView(R.id.layout_loading)
    LoadingLayout layoutLoading;
    @BindView(R.id.layout_swr)
    SwipeRefreshLayout srlRecyclerView;

    private boolean loading = false;
    private boolean refreshing = true;


    private RVSingleAdapter adapter;
    private BaseLinearLayoutManager layoutManager;
    private EndOffsetItemDecoration endOffsetItemDecoration;


    List<String> strings = new ArrayList<>();

    private int maxCount = 10;
    private int count = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init(final View view, @Nullable Bundle savedInstanceState) {
        super.init(view, savedInstanceState);


        adapter = new RVSingleAdapter(strings);

        rvContent.setHasFixedSize(true);
        layoutManager = new BaseLinearLayoutManager(getContext());
        rvContent.setLayoutManager(layoutManager);
        adapter = new RVSingleAdapter(strings);
        endOffsetItemDecoration = new EndOffsetItemDecoration((int) DisplayUtils.dp2px(getContext(), 48f, 0f));
        rvContent.addItemDecoration(endOffsetItemDecoration);
        rvContent.setAdapter(adapter);
        rvContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (layoutManager.isBottom(recyclerView)) {
                    if (!loading)
                        load();
                    layoutLoading.setVisibility(View.VISIBLE);
                } else {
                    layoutLoading.setVisibility(View.GONE);
                }

            }
        });

        layoutLoading.setOnFailedClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load();
            }
        });

        srlRecyclerView.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        srlRecyclerView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshing = true;
                loading = false;
                bbb();
            }
        });
        srlRecyclerView.setProgressViewOffset(false, 0, (int) DisplayUtils.dp2px(this.getContext(), 24, 0.0F));
        srlRecyclerView.setRefreshing(true);

        refreshing = true;
        bbb();
    }

    private void initData() {
        if (count < maxCount) {
            for (int i = 0; i < 10; i++) {
                strings.add("aaa-" + count + "-" + i);
            }
            count++;
        }
    }

    private void bbb() {
        Observable.timer(4, TimeUnit.SECONDS, Schedulers.io()).map(new Func1<Long, Boolean>() {
            @Override
            public Boolean call(Long aLong) {
                count = 1;
                strings.clear();
                initData();
                return true;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aVoid) {
                adapter.notifyDataSetChanged();
                srlRecyclerView.setRefreshing(false);
                refreshing = false;
            }

        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    private void aaa() {
        Observable.timer(2, TimeUnit.SECONDS, Schedulers.io()).map(new Func1<Long, Boolean>() {
            @Override
            public Boolean call(Long aLong) {
                boolean flag = true;
                int i = new Random().nextInt(100);
                if ((i % 2) == 1)
                    flag = false;
                if (flag)
                    initData();
                return flag;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean flag) {
                if (flag) {
                    adapter.notifyDataSetChanged();
                    loading = false;
                } else {
                    layoutLoading.setStatus(LoadStatus.FAILED);
                    loading = true;
                }
                if (count + 1 > maxCount) {
                    layoutLoading.setVisibility(View.GONE);
                    if (endOffsetItemDecoration != null)
                        rvContent.removeItemDecoration(endOffsetItemDecoration);
                    loading = true;
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public void load() {
        aaa();
        layoutLoading.setStatus(LoadStatus.LOADING);
        loading = true;
    }
}
