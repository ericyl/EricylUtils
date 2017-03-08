package com.ericyl.example.ui.fragment.recyclerview;


import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ericyl.example.R;
import com.ericyl.example.model.ui.SingleV1Model;
import com.ericyl.example.model.ui.holder.FooterViewHolder;
import com.ericyl.example.ui.adapter.RVSingleV1ModelAdapter;
import com.ericyl.example.ui.fragment.BaseFragment;
import com.ericyl.utils.annotation.LoadStatus;
import com.ericyl.utils.ui.widget.support.recyclerview.BaseLinearLayoutManager;
import com.ericyl.utils.util.DisplayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SingleV1RecyclerViewFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, IClickListener, FooterViewHolder.ILoadingClickListener {

    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    @BindView(R.id.nsw_error)
    NestedScrollView nswError;
    @BindView(R.id.srl_content)
    SwipeRefreshLayout srlContent;

    private BaseLinearLayoutManager layoutManager;
    private RVSingleV1ModelAdapter adapter;

    private List<SingleV1Model> singleV1Models = new ArrayList<>();

    private Subscription subscription;

    private boolean isRefresh = true;
    private boolean isLoading = false;

    private int maxCount = 10;
    private int count = 0;

    public SingleV1RecyclerViewFragment() {
        // Required empty public constructor
    }

    public static SingleV1RecyclerViewFragment newInstance() {
        SingleV1RecyclerViewFragment fragment = new SingleV1RecyclerViewFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_recyclerview_v1, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    protected void init(View view, Bundle savedInstanceState) {
        initSwipeRefreshLayout();
        initRecyclerView();
        nswError.setVisibility(View.GONE);
    }

    private void initRecyclerView() {
        layoutManager = new BaseLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL);
        rvContent.setLayoutManager(layoutManager);
        rvContent.hasFixedSize();
        adapter = new RVSingleV1ModelAdapter(getActivity(), singleV1Models);
        adapter.setItemClickListener(this);
        adapter.setFooterClickListener(this);
        rvContent.setAdapter(adapter);
        rvContent.addOnScrollListener(new RecyclerView.OnScrollListener() {

            public void onScrollStateChanged(RecyclerView recyclerView, int var2) {
            }

            public void onScrolled(RecyclerView recyclerView, int var2, int var3) {
                srlContent.setEnabled(layoutManager.isTop(recyclerView));
                if (layoutManager.isBottom(recyclerView) && !isLoading && adapter.getLoadStatus() == LoadStatus.LOADING) {
                    isLoading = true;
                    loadMore();
                }
            }
        });
    }

    private void initSwipeRefreshLayout() {
        srlContent.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        srlContent.setOnRefreshListener(this);
        srlContent.setProgressViewOffset(false, 0, (int) DisplayUtils.dp2px(getActivity(), 24, 0.0F));
        srlContent.setRefreshing(true);
        loadMore();
    }


    @Override
    public void onRefresh() {
        isRefresh = true;
        count = 0;
        if (isLoading && subscription != null)
            subscription.unsubscribe();
        loadMore();
    }

    private void loadMore() {
        subscription = Observable.timer(2, TimeUnit.SECONDS, Schedulers.io()).map(new Func1<Long, List<SingleV1Model>>() {
            @Override
            public List<SingleV1Model> call(Long aLong) {
                return setData(count + 1);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<SingleV1Model>>() {
            @Override
            public void call(List<SingleV1Model> localSingleV1Models) {
                if (localSingleV1Models != null) {
                    count += 1;
                    nswError.setVisibility(View.GONE);
                    int num = singleV1Models.size();
                    singleV1Models.addAll(num == 0 ? 0 : num - 1, localSingleV1Models);
                    adapter.notifyItemRangeInserted(num == 0 ? 0 : num - 1, localSingleV1Models.size());
                    if (count + 1 > maxCount)
                        adapter.setLoadStatus(LoadStatus.FINISH);
                    else
                        adapter.setLoadStatus(LoadStatus.LOADING);
                    adapter.addFooterView();
                } else {
                    if (isRefresh)
                        nswError.setVisibility(View.VISIBLE);
                    else {
                        nswError.setVisibility(View.GONE);
                        adapter.setLoadStatus(LoadStatus.FAILED);
                    }
                }
                if (isRefresh) {
                    srlContent.setRefreshing(false);
                    isRefresh = false;
                } else
                    isLoading = false;
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
                if (isRefresh) {
                    srlContent.setRefreshing(false);
                    nswError.setVisibility(View.VISIBLE);
                    isRefresh = false;
                } else {
                    nswError.setVisibility(View.GONE);
                    adapter.setLoadStatus(LoadStatus.FAILED);
                    isLoading = false;
                }
            }
        });
    }

    private List<SingleV1Model> setData(int localCount) {
        List<SingleV1Model> singleV1Models = null;
        if ((new Random().nextInt(100) % 3) != 1) {
            singleV1Models = new ArrayList<>();
            if (localCount <= maxCount) {
                for (int i = 0; i < 20; i++) {
                    singleV1Models.add(new SingleV1Model("aaa-" + count + "-" + i));
                }
            }
        }
        return singleV1Models;
    }

    @Override
    public void onItemClickListener(int position) {
        SingleV1Model singleV1Model = singleV1Models.get(position);
        Log.v("commonProblem", singleV1Model.getTitle());
    }

    @Override
    public void onErrorClickListener() {
        adapter.setLoadStatus(LoadStatus.LOADING);
        loadMore();
    }
}
