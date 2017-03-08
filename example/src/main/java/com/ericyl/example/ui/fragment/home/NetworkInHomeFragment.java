package com.ericyl.example.ui.fragment.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ericyl.example.R;
import com.ericyl.example.ui.adapter.RVNetworkInHomeAdapter;
import com.ericyl.example.ui.fragment.BaseFragment;
import com.ericyl.example.util.AppProperties;
import com.ericyl.example.util.BusProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NetworkInHomeFragment extends BaseFragment {

    @BindView(R.id.rv_content)
    RecyclerView rvContent;

    private static final String[] arrays;

    static {
        arrays = AppProperties.getContext().getResources().getStringArray(R.array.network_entries);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_network_in_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    protected void init(View view, @Nullable Bundle savedInstanceState) {
        super.init(view, savedInstanceState);

        rvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvContent.hasFixedSize();
        rvContent.setAdapter(new RVNetworkInHomeAdapter(arrays));

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

}
