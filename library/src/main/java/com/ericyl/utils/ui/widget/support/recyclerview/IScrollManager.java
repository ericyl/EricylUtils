package com.ericyl.utils.ui.widget.support.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

public interface IScrollManager {
    boolean isTop(@NonNull RecyclerView recyclerView);

    boolean isBottom(@NonNull RecyclerView recyclerView);
}