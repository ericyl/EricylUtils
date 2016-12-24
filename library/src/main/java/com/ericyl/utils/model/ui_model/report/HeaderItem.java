package com.ericyl.utils.model.ui_model.report;

import java.util.List;

public class HeaderItem {
    private Item title;
    private List<HeaderItem> values;

    public HeaderItem() {
    }

    public Item getTitle() {
        return title;
    }

    public void setTitle(Item title) {
        this.title = title;
    }

    public List<HeaderItem> getValues() {
        return values;
    }

    public void setValues(List<HeaderItem> values) {
        this.values = values;
    }
}
