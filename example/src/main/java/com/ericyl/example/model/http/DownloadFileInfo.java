package com.ericyl.example.model.http;

import java.io.Serializable;

public class DownloadFileInfo implements Serializable {

    private String name;
    private String title;
    private String info;
    private String url;

    public DownloadFileInfo() {
    }

    public DownloadFileInfo(String name, String title, String info, String url) {
        this.name = name;
        this.title = title;
        this.info = info;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DownloadFileInfo)) return false;

        DownloadFileInfo that = (DownloadFileInfo) o;

        if (!name.equals(that.name)) return false;
        if (!title.equals(that.title)) return false;
        if (!info.equals(that.info)) return false;
        return url.equals(that.url);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + info.hashCode();
        result = 31 * result + url.hashCode();
        return result;
    }
}
