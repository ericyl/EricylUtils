package com.ericyl.example.model.ui_model;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;


public class TabInfo {
    private String title;
    private Fragment fragment;
    @IdRes
    private int idRes;

    public TabInfo(String title, Fragment fragment) {
        this.title = title;
        this.fragment = fragment;
    }

    public TabInfo(String title, Fragment fragment, @IdRes int idRes) {
        this.title = title;
        this.fragment = fragment;
        this.idRes = idRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public int getIdRes() {
        return idRes;
    }

    public void setIdRes(int idRes) {
        this.idRes = idRes;
    }
}
