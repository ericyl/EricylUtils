package com.ericyl.example.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.ericyl.example.model.ui.TabInfo;

import java.util.List;


public class VPRecyclerViewAdapter extends FragmentStatePagerAdapter {

    private List<TabInfo> tabInfos;

    public VPRecyclerViewAdapter(FragmentManager fm){
        super(fm);
    }

    public VPRecyclerViewAdapter(FragmentManager fm, List<TabInfo> tabInfos) {
        super(fm);
        this.tabInfos = tabInfos;
    }

    @Override
    public int getCount() {
        return tabInfos.size();
    }

    @Override
    public Fragment getItem(int position) {
        return tabInfos.get(position).getFragment();
    }

    @Override
    public Object instantiateItem(ViewGroup arg0, int arg1) {
        return super.instantiateItem(arg0, arg1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabInfos.get(position).getTitle();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

}
