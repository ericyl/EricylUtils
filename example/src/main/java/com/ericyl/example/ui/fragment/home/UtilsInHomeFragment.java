package com.ericyl.example.ui.fragment.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.ericyl.example.R;
import com.ericyl.example.ui.BaseFragment;
import com.ericyl.example.ui.adapter.RVUtilsInHomeAdapter;
import com.ericyl.example.ui.fragment.searchview.SearchDialogFragment;
import com.ericyl.example.util.BusProvider;

import butterknife.BindView;

public class UtilsInHomeFragment extends BaseFragment {

    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    SearchDialogFragment searchDialogFragment;

    private static final int[] ids;

    static {
        ids = new int[]{R.id.recyclerview_utils, R.id.download_utils, R.id.permission_utils, R.id.wps_office_utils, R.id.more_utils};
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_utils_in_home;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                if (searchDialogFragment == null)
                    searchDialogFragment = SearchDialogFragment.newInstance();
                if (!searchDialogFragment.isVisible())
                    searchDialogFragment.show(getActivity().getSupportFragmentManager(), "searchDialogFragment");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);

    }

}
