package com.ericyl.utils.model;

import com.ericyl.utils.annotation.DownloadReason;
import com.ericyl.utils.annotation.DownloadStatus;

public class DownloadCurrentInfo {
    private long id;
    private long lastModifiedTimestamp;
    @DownloadStatus
    private int status;
    @DownloadReason
    private int reason;
    private long downloadBytes;
    private long fileBytes;

    public DownloadCurrentInfo() {
    }

    public DownloadCurrentInfo(long id, long lastModifiedTimestamp, int status, int reason, long downloadBytes, long fileBytes) {
        this.id = id;
        this.lastModifiedTimestamp = lastModifiedTimestamp;
        this.status = status;
        this.reason = reason;
        this.downloadBytes = downloadBytes;
        this.fileBytes = fileBytes;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    public void setLastModifiedTimestamp(long lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

    @DownloadStatus
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @DownloadReason
    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public long getDownloadBytes() {
        return downloadBytes;
    }

    public void setDownloadBytes(long downloadBytes) {
        this.downloadBytes = downloadBytes;
    }

    public long getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(long fileBytes) {
        this.fileBytes = fileBytes;
    }
}