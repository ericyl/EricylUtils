package com.ericyl.utils.model;

import com.ericyl.utils.annotation.DownloadStatus;

public class DownloadCurrentStatus {
    private long id;
    @DownloadStatus
    private int status;
    private int nowC;
    private int maxC;
    private CharSequence downloadSize;
    private CharSequence fileSize;
    private String statusMsg;

    public DownloadCurrentStatus() {
    }

    public DownloadCurrentStatus(long id, int status, int nowC, int maxC, CharSequence downloadSize, CharSequence fileSize, String statusMsg) {
        this.id = id;
        this.status = status;
        this.nowC = nowC;
        this.maxC = maxC;
        this.downloadSize = downloadSize;
        this.fileSize = fileSize;
        this.statusMsg = statusMsg;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @DownloadStatus
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNowC() {
        return nowC;
    }

    public void setNowC(int nowC) {
        this.nowC = nowC;
    }

    public int getMaxC() {
        return maxC;
    }

    public void setMaxC(int maxC) {
        this.maxC = maxC;
    }

    public CharSequence getDownloadSize() {
        return downloadSize;
    }

    public void setDownloadSize(CharSequence downloadSize) {
        this.downloadSize = downloadSize;
    }

    public CharSequence getFileSize() {
        return fileSize;
    }

    public void setFileSize(CharSequence fileSize) {
        this.fileSize = fileSize;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }
}