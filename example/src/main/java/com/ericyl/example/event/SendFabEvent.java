package com.ericyl.example.event;

import android.support.annotation.IdRes;


public class SendFabEvent {

    @IdRes
    private int idRes;

    public SendFabEvent(@IdRes int idRes) {
        this.idRes = idRes;
    }

    public int getIdRes() {
        return idRes;
    }

}
