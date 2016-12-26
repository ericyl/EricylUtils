package com.ericyl.example.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.IBinder;

import com.ericyl.example.util.AESTableUtils;
import com.ericyl.example.util.AppProperties;
import com.ericyl.example.util.DatabaseUtils;

import net.sqlcipher.database.SQLiteDatabase;


public class InitService extends Service {

    private InitBinder binder = new InitBinder();

    public InitService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    public class InitBinder extends Binder {
        public InitService getService() {
            return InitService.this;
        }
    }

    public void initSystem() {
        DatabaseUtils.createDatabase(this);
        initData();
    }

    private void initData(){
        SQLiteDatabase.loadLibs(this);
        Cursor cursor = null;
        SQLiteDatabase database = null;
        try {
            database = SQLiteDatabase.openDatabase(AppProperties.getDatabaseDir(DatabaseUtils.DATABASE).getAbsolutePath(), "test", null, SQLiteDatabase.OPEN_READWRITE);

            cursor = database.query(AESTableUtils.TABLE_NAME, null, AESTableUtils.NAME + " = ? ", new String[]{"example.keystore"}, null, null, null);
            if (cursor != null && cursor.moveToFirst())
                return;
            ContentValues keyStoreCV = new ContentValues();
            keyStoreCV.put(AESTableUtils.NAME, "example.keystore");
            keyStoreCV.put(AESTableUtils.KEY_TYPE, "BKS");
            keyStoreCV.put(AESTableUtils.KEY_STORE_PWD, "example");
            keyStoreCV.put(AESTableUtils.SECRET_KEY_ENTRY_ALIAS, "example");
            keyStoreCV.put(AESTableUtils.SECRET_KEY_ENTRY_PWD, "example");
            database.insert(AESTableUtils.TABLE_NAME, null, keyStoreCV);

        } finally {
            if (cursor != null)
                cursor.close();
            if (database != null)
                database.close();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
