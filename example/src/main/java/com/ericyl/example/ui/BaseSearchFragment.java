package com.ericyl.example.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.ericyl.example.R;
import com.ericyl.example.ui.fragment.searchview.SearchDialogFragment;

public abstract class BaseSearchFragment extends BaseFragment {

    private SearchDialogFragment searchDialogFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

}
