package com.ericyl.example.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Toast;


public class DownloadReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(id);
            Cursor cursor = manager.query(query);
            if (cursor.moveToFirst()) {
                String filename = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                if (filename != null) {
                    String minType = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));
//                    if (AppProperties.MIME_TYPE_APPLICATION.equals(minType)) {
//                        PackageUtils.installApk(context, filename);
//                    } else if (AppProperties.MIME_TYPE_OFFICE.equals(minType)) {
//                        AppProperties.openOfficeFile(context, filename);
//                    }
//                    EricylUtils.openFile(context, minType, FILE_PROVIDER, filename);
                }
            }
        } else if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(intent.getAction())) {
            long[] ids = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
            manager.remove(ids);
            Toast.makeText(context, "stop", Toast.LENGTH_SHORT).show();
        }

    }
}
