package com.ericyl.example.model.ui;


import com.ericyl.utils.ui.widget.support.recyclerview.v1.IViewHolderType;

public class SingleV1Model implements IViewHolderType {

    private String title;

    public SingleV1Model() {
    }

    public SingleV1Model(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int type() {
        return BODY;
    }
}
