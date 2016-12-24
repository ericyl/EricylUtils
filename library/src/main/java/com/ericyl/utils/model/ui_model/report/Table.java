package com.ericyl.utils.model.ui_model.report;

import java.util.List;

public class Table {
    private HeaderItem header;
    private List<BodyItem> body;

    public HeaderItem getHeader() {
        return header;
    }

    public void setHeader(HeaderItem header) {
        this.header = header;
    }

    public List<BodyItem> getBody() {
        return body;
    }

    public void setBody(List<BodyItem> body) {
        this.body = body;
    }


}
