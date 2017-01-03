package com.ericyl.example.receiver;

import android.app.DownloadManager;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;

import com.ericyl.utils.model.DownloadCurrentInfo;
import com.ericyl.utils.model.DownloadCurrentStatus;
import com.ericyl.utils.util.DownloadUtils;

public class DownloadChangeObserver extends ContentObserver {

    private Handler handler;
    private long downloadID;
    private Integer handlerWhat;
    private Context context;

    public DownloadChangeObserver(Handler handler, Context context, long downloadID, Integer handlerWhat) {
        super(handler);
        this.context = context;
        this.downloadID = downloadID;
        this.handler = handler;
        this.handlerWhat = handlerWhat;
    }

    public void setDownloadID(long downloadID) {
        this.downloadID = downloadID;
    }

    @Override
    public void onChange(boolean selfChange) {
        DownloadCurrentInfo[] info = DownloadUtils.getDownloadCurrentInfo(context, downloadID);
        DownloadCurrentStatus currentStatus;
        if (info == null || info[0] == null)
            currentStatus = new DownloadCurrentStatus(0, DownloadManager.STATUS_FAILED, 0, 0, null, null, null);
        else
            currentStatus = DownloadUtils.getDownloadCurrentStatus(context, info[0]);
        handler.obtainMessage(handlerWhat, currentStatus).sendToTarget();
    }

}
