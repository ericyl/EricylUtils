package com.ericyl.example.event;

import android.support.annotation.IdRes;

import com.github.clans.fab.FloatingActionMenu;


public class InitFabEvent {

    @IdRes
    private int idRes;
    private FloatingActionMenu floatingActionMenu;

    public InitFabEvent(@IdRes int idRes, FloatingActionMenu floatingActionMenu) {
        this.idRes = idRes;
        this.floatingActionMenu = floatingActionMenu;
    }

    public int getIdRes() {
        return idRes;
    }

    public FloatingActionMenu getFloatingActionMenu() {
        return floatingActionMenu;
    }
}
