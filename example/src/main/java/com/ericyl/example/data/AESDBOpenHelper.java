package com.ericyl.example.data;

import android.content.Context;

import com.ericyl.example.util.AESTableUtils;
import com.ericyl.example.util.DatabaseUtils;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;


public class AESDBOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;


    public AESDBOpenHelper(Context context) {
        super(context, DatabaseUtils.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createSearchSuggestionTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private static void createSearchSuggestionTable(SQLiteDatabase sdb) {
        String sql = "CREATE TABLE IF NOT EXISTS " + AESTableUtils.TABLE_NAME + " ( "
                + AESTableUtils.INDEX + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + AESTableUtils.NAME + " VARCHAR, "
                + AESTableUtils.KEY_TYPE + " VARCHAR, "
                + AESTableUtils.KEY_STORE_PWD + " VARCHAR, "
                + AESTableUtils.SECRET_KEY_ENTRY_ALIAS + " VARCHAR, "
                + AESTableUtils.SECRET_KEY_ENTRY_PWD + " VARCHAR )";

        sdb.execSQL(sql);
    }


}