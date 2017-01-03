package com.ericyl.example.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ericyl.example.IDownloadManagerService;
import com.ericyl.example.R;
import com.ericyl.example.util.AppProperties;

public class DownloadActivity extends AppCompatActivity {

    private IDownloadManagerService downloadService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        Intent intent = new Intent();
        intent.setAction("com.ericyl.example.DownloadManagerService");
        intent.setPackage(AppProperties.getContext().getPackageName());
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        startService(intent);
//        AppProperties.download("QQMacMgr_2.4.1.dmg", "aaa", "http://dlied6.qq.com/invc/xfspeed/mac/verupdate/QQMacMgr_2.4.1.dmg", "info");
//        AppProperties.download("QQBrowser_for_Mac.4.1.dmg", "bbb", "http://dldir1.qq.com/invc/tt/QQBrowser_for_Mac.dmg", "info bbb");

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            downloadService = IDownloadManagerService.Stub.asInterface(service);
            try {
                downloadService.invokeDownload("QQMacMgr_2.4.1.dmg", "aaa", "http://dlied6.qq.com/invc/xfspeed/mac/verupdate/QQMacMgr_2.4.1.dmg", "info");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            downloadService = null;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceConnection != null) {
            Intent intent = new Intent();
            intent.setAction("com.ericyl.example.DownloadManagerService");
            intent.setPackage(AppProperties.getContext().getPackageName());
//            stopService(intent);
            unbindService(serviceConnection);
        }
    }
}
