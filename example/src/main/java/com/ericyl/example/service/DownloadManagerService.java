package com.ericyl.example.service;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.ericyl.example.IDownloadManagerService;
import com.ericyl.example.R;
import com.ericyl.example.model.http.DownloadFileInfo;
import com.ericyl.example.receiver.DownloadChangeObserver;
import com.ericyl.example.ui.activity.DownloadActivity;
import com.ericyl.example.util.AppProperties;
import com.ericyl.utils.model.DownloadCurrentStatus;
import com.ericyl.utils.util.DownloadUtils;
import com.ericyl.utils.util.WeakHandler;


public class DownloadManagerService extends Service {

    private static final int NOTIFICATION_ID = 1;
    private NotificationCompat.Builder builder;

    private long downloadID;
    private DownloadFileInfo downloadFileInfo;
//    boolean flag = false;

    private DownloadChangeObserver observer;
    public static final Uri CONTENT_URI = Uri.parse("content://downloads/my_downloads");
    private DownloadServiceReceiver receiver;

    public static final int MSG_UPDATE_PROGRESS = 1;

    private static String ACTION_DOWNLOAD_ON = "com.ericyl.example.DOWNLOAD_ON";
    private static String ACTION_DOWNLOAD_OFF = "com.ericyl.example.DOWNLOAD_OFF";

    private WeakHandler<DownloadManagerService> handler = new WeakHandler<>(this, new WeakHandler.OnMessageListener<DownloadManagerService>() {
        @Override
        public void messageListener(DownloadManagerService service, Message message) {
            if (service != null) {
                switch (message.what) {
                    case MSG_UPDATE_PROGRESS:
                        DownloadCurrentStatus moduleInfo = (DownloadCurrentStatus) message.obj;
                        switch (moduleInfo.getStatus()) {
                            case -1:
                            case DownloadManager.STATUS_FAILED:
                            case DownloadManager.STATUS_PAUSED:
                                if (observer != null)
                                    getContentResolver().unregisterContentObserver(observer);
                                changeAction(builder, true);
                                stopDownload();
                                break;
                            case DownloadManager.STATUS_PENDING:
                            case DownloadManager.STATUS_RUNNING:
                                break;
                            case DownloadManager.STATUS_SUCCESSFUL:
                                if (downloadID == 0)
                                    return;
                                downloadID = 0;
                                downloadFileInfo = null;
                                if (observer != null)
                                    getContentResolver().unregisterContentObserver(observer);
                                observer = null;
                                stopForeground(true);
                                stopSelf();
                                return;
                        }
                        updateProgress(builder, moduleInfo.getMaxC(), moduleInfo.getNowC(), moduleInfo.getStatusMsg());
                        break;
                }
            }

        }
    });

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_DOWNLOAD_ON);
        filter.addAction(ACTION_DOWNLOAD_OFF);
        if (receiver == null)
            receiver = new DownloadServiceReceiver();
        registerReceiver(receiver, filter);

    }

    public class DownloadServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_DOWNLOAD_ON.equals(intent.getAction())) {
                DownloadFileInfo item = (DownloadFileInfo) intent.getSerializableExtra("item");
                if (item != null) {
                    if (downloadID != 0)
                        stopDownload();
                    if (builder == null)
                        builder = createNotificationBuilder();
                    setBuilder(builder, item.getTitle(), item.getInfo());
                    startForeground(NOTIFICATION_ID, builder.build());
                    downloadID = AppProperties.download(item.getName(), item.getTitle(), item.getUrl(), item.getInfo());
                    if (observer != null)
                        observer.setDownloadID(downloadID);
                    else {
                        observer = new DownloadChangeObserver(handler, DownloadManagerService.this, downloadID, MSG_UPDATE_PROGRESS);
                        getContentResolver().registerContentObserver(CONTENT_URI, true, observer);
                    }
                    changeAction(builder, false);
                } else
                    throw new IllegalArgumentException("\"versionInfo\" can not be null.");
            } else if (ACTION_DOWNLOAD_OFF.equals(intent.getAction())) {
                stopDownload();
            }

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
//        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private final IDownloadManagerService.Stub binder = new IDownloadManagerService.Stub() {
        public void invokeDownload(String name, String title, String url, String info) {
            DownloadFileInfo tmpDownloadFileInfo = new DownloadFileInfo(name, title, info, url);
            if (downloadID != 0)
                stopDownload();
            downloadFileInfo = tmpDownloadFileInfo;
            if (builder == null)
                builder = createNotificationBuilder();
            setBuilder(builder, title, info);
            startForeground(NOTIFICATION_ID, builder.build());
            downloadID = AppProperties.download(name, title, url, info);
            if (observer != null) {
                observer.setDownloadID(downloadID);
            } else {
                observer = new DownloadChangeObserver(handler, DownloadManagerService.this, downloadID, MSG_UPDATE_PROGRESS);
                getContentResolver().registerContentObserver(CONTENT_URI, true, observer);
            }
            changeAction(builder, false);
//            removeRetryAction(builder);
//            flag = false;
        }

        public void invokeStopDownload() {
            stopDownload();
        }

    };

    private NotificationCompat.Builder createNotificationBuilder() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher);
        Intent resultIntent = new Intent(this, DownloadActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(DownloadActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        return builder;
    }

    private void setBuilder(NotificationCompat.Builder builder, String title, String info) {
        builder.setContentTitle(String.format(getString(R.string.download_something), title));
        builder.setContentText(String.format(getString(R.string.download_something), info));
        builder.setProgress(0, 0, true);
        builder.setContentInfo(getString(R.string.start));
        builder.addAction(R.drawable.ic_clear_black_24dp, getString(R.string.cancel), PendingIntent.getBroadcast(this, 0, new Intent(ACTION_DOWNLOAD_OFF), 0));

    }

    public void updateProgress(NotificationCompat.Builder builder, int maxC, int nowC, String info) {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        builder.setProgress(maxC, nowC, false);
        builder.setContentInfo(info);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void changeAction(NotificationCompat.Builder builder, boolean retry) {
        builder.mActions.clear();
        if (retry)
            builder.addAction(R.drawable.ic_replay_black_24dp, getString(R.string.retry), PendingIntent.getBroadcast(this, 0, new Intent(ACTION_DOWNLOAD_ON).putExtra("item", downloadFileInfo), 0));
        else
            builder.addAction(R.drawable.ic_clear_black_24dp, getString(R.string.cancel), PendingIntent.getBroadcast(this, 0, new Intent(ACTION_DOWNLOAD_OFF), 0));

    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null)
            unregisterReceiver(receiver);
        stopDownload();
    }


    private void stopDownload() {
        if (observer != null)
            getContentResolver().unregisterContentObserver(observer);
        observer = null;
        DownloadUtils.stopDownload(AppProperties.getContext(), downloadID);
        downloadID = 0;
        downloadFileInfo = null;
        stopForeground(true);
        stopSelf();
    }

}