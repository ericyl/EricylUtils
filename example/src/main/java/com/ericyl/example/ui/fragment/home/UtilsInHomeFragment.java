package com.ericyl.example.ui.fragment.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ericyl.example.R;
import com.ericyl.example.ui.adapter.RVUtilsInHomeAdapter;
import com.ericyl.example.ui.fragment.BaseSearchFragment;
import com.ericyl.example.util.BusProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UtilsInHomeFragment extends BaseSearchFragment {

    @BindView(R.id.rv_content)
    RecyclerView rvContent;

    private static final int[] ids;

    static {
        ids = new int[]{R.id.recyclerview_utils, R.id.download_utils, R.id.more_utils};
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_utils_in_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init(View view, @Nullable Bundle savedInstanceState) {
        super.init(view, savedInstanceState);

        rvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvContent.hasFixedSize();
        rvContent.setAdapter(new RVUtilsInHomeAdapter(getContext(), R.array.utils_entries, ids));
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_utils, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);

    }

}
