package com.ericyl.example.event;

import android.support.annotation.IdRes;


public class JumpNavPageEvent {
    @IdRes
    private int idRes;
    private boolean checked;
    @IdRes
    private int tabIdRes;

    public JumpNavPageEvent() {
    }

    public JumpNavPageEvent(@IdRes int idRes) {
        this.idRes = idRes;
        this.checked = true;
        this.tabIdRes = 0;
    }

    public JumpNavPageEvent(@IdRes int idRes, boolean checked) {
        this.idRes = idRes;
        this.checked = checked;
        this.tabIdRes = 0;
    }

    public JumpNavPageEvent(@IdRes int idRes, @IdRes int tabIdRes) {
        this.idRes = idRes;
        this.checked = true;
        this.tabIdRes = tabIdRes;
    }

    public JumpNavPageEvent(@IdRes int idRes, boolean checked, @IdRes int tabIdRes) {
        this.idRes = idRes;
        this.checked = checked;
        this.tabIdRes = tabIdRes;
    }

    public int getIdRes() {
        return idRes;
    }

    public void setIdRes(int idRes) {
        this.idRes = idRes;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getTabIdRes() {
        return tabIdRes;
    }

    public void setTabIdRes(int tabIdRes) {
        this.tabIdRes = tabIdRes;
    }
}
