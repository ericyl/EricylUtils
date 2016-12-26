package com.ericyl.example.util;

import android.content.Context;

import com.ericyl.example.data.AESDBOpenHelper;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;

/**
 * Created by ericyl on 16/9/13.
 */
public class DatabaseUtils {

    private static final String URI = "content://com.ericyl.example.provider.content/";
    public static final String DATABASE_NAME = "data";
    public static final String DATABASE = "data.db";

//    private static final String URI_DATA_PATH = DATABASE_NAME
//            + File.separator + DatabaseUtils.TABLE_NAME;
//
//    //    com.ericyl.example.provider.data/data/data
//    public static final String URI_DATA = URI + URI_DATA_PATH;


    public static void createDatabase(Context context) {
        SQLiteDatabase database = null;
        try {
            SQLiteDatabase.loadLibs(context);
            File file = AppProperties.getDatabaseDir(DatabaseUtils.DATABASE);
            database = SQLiteDatabase.openOrCreateDatabase(file, "test", null);
            new AESDBOpenHelper(context).onCreate(database);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null)
                database.close();
        }
    }

}
