package com.ericyl.example.event;

import android.support.annotation.IdRes;


public class JumpHomeTabEvent {
    @IdRes
    private int idRes;

    public JumpHomeTabEvent() {
    }

    public JumpHomeTabEvent(@IdRes int idRes) {
        this.idRes = idRes;
    }

    public int getIdRes() {
        return idRes;
    }

    public void setIdRes(int idRes) {
        this.idRes = idRes;
    }
}
