package com.ericyl.example.event;

import android.support.annotation.IdRes;

import com.ericyl.example.R;



public class JumpUtilsTabEvent {
    @IdRes
    private int idRes;
    @IdRes
    private int tabIdRes;

    public JumpUtilsTabEvent() {
    }

    public JumpUtilsTabEvent(@IdRes int idRes) {
        this.idRes = idRes;
        this.tabIdRes = R.id.tab_ui_utils;
    }

    public JumpUtilsTabEvent(@IdRes int idRes, @IdRes int tabIdRes) {
        this.idRes = idRes;
        this.tabIdRes = tabIdRes;
    }

    public int getIdRes() {
        return idRes;
    }

    public void setIdRes(int idRes) {
        this.idRes = idRes;
    }

    public int getTabIdRes() {
        return tabIdRes;
    }

    public void setTabIdRes(int tabIdRes) {
        this.tabIdRes = tabIdRes;
    }
}
