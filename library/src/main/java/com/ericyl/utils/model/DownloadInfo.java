package com.ericyl.utils.model;

public class DownloadInfo {
    private long id;
    private String title;
    private String description;
    private String uri;
    private String fileLocalUri;
    private String minType;
    private String mediaProviderUri;

    public DownloadInfo() {
    }

    public DownloadInfo(long id, String title, String description, String uri, String fileLocalUri, String minType, String mediaProviderUri) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.uri = uri;
        this.fileLocalUri = fileLocalUri;
        this.minType = minType;
        this.mediaProviderUri = mediaProviderUri;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getFileLocalUri() {
        return fileLocalUri;
    }

    public void setFileLocalUri(String fileLocalUri) {
        this.fileLocalUri = fileLocalUri;
    }

    public String getMinType() {
        return minType;
    }

    public void setMinType(String minType) {
        this.minType = minType;
    }

    public String getMediaProviderUri() {
        return mediaProviderUri;
    }

    public void setMediaProviderUri(String mediaProviderUri) {
        this.mediaProviderUri = mediaProviderUri;
    }

}