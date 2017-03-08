package com.ericyl.example.ui.fragment.recyclerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ericyl.example.R;
import com.ericyl.example.ui.adapter.RVMultiAdapter;
import com.ericyl.example.ui.fragment.BaseFragment;
import com.ericyl.utils.ui.widget.support.recyclerview.multilayer.layout.PinnedRecyclerViewLayout;
import com.ericyl.utils.util.ColorUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MultilayerRecyclerViewFragment extends BaseFragment implements PinnedRecyclerViewLayout.OnRecyclerViewPinnedViewListener {
    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    @BindView(R.id.parentPanel)
    PinnedRecyclerViewLayout parentPanel;


    private List<String> headers;
    private List<List<String>> bodys;

    private RVMultiAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_multilayer_recycler_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init(View view, @Nullable Bundle savedInstanceState) {
        super.init(view, savedInstanceState);

        rvContent.setHasFixedSize(true);
        rvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        initData();
        adapter = new RVMultiAdapter(headers, bodys);
        rvContent.setAdapter(adapter);
        parentPanel.initRecyclerPinned(rvContent, LayoutInflater.from(getActivity()).inflate(R.layout.layout_rv_main, null), 1);
        parentPanel.setOnRecyclerViewPinnedViewListener(this);
    }

    private void initData() {
        headers = new ArrayList<>();
        bodys = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            List<String> personInfos1 = new ArrayList<>();
            for (int j = 0; j < 6; j++) {
                String person = "body_" + i + "" + j;
                personInfos1.add(person);
            }
            headers.add("header_" + i);
            bodys.add(personInfos1);
        }
    }

    @Override
    public void initPinnedView(PinnedRecyclerViewLayout pinnedRecyclerViewLayout, View pinnedView, int position) {

        switch (pinnedRecyclerViewLayout.getId()) {
            case R.id.parentPanel:
                pinnedView.setBackgroundColor(ColorUtils.COLOR_RED);
                TextView tvTitle = (TextView) pinnedView.findViewById(R.id.tv_title);
                String header = headers.get(position);
                tvTitle.setText(header);
                break;
        }

    }
}
