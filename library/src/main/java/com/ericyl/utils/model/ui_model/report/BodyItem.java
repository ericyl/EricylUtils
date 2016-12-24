package com.ericyl.utils.model.ui_model.report;

import java.util.List;

public class BodyItem {
    private Item title;
    private List<Item> values;

    public BodyItem() {
    }

    public Item getTitle() {
        return title;
    }

    public void setTitle(Item title) {
        this.title = title;
    }

    public List<Item> getValues() {
        return values;
    }

    public void setValues(List<Item> values) {
        this.values = values;
    }
}
