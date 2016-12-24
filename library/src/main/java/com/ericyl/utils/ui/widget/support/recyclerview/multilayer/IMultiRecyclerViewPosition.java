package com.ericyl.utils.ui.widget.support.recyclerview.multilayer;

import java.util.List;

public interface IMultiRecyclerViewPosition {


    List<Integer> getViewItemCount();

    int getHeaderPosition(int position);

    /**
     * @param position get the list position
     * @return isHeader return -1
     */
    int getBodyPosition(int position);

}
