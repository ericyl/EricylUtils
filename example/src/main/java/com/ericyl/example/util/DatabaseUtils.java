package com.ericyl.example.util;

import android.content.Context;

import com.ericyl.example.data.AESDBOpenHelper;
import com.ericyl.example.data.SearchSuggestionDBOpenHelper;

import java.io.File;

public class DatabaseUtils {

    private static final String URI = "content://com.ericyl.example.provider.content/";
    public static final String DATA_DATABASE_NAME = "data";

    public static final String SEARCH_DATABASE_NAME = "SearchSuggestion";

    private static final String URI_SEARCH_SUGGESTION_PATH = SEARCH_DATABASE_NAME
            + File.separator + SearchSuggestionTableUtils.TABLE_NAME;

    public static final String URI_SEARCH_SUGGESTION = URI + URI_SEARCH_SUGGESTION_PATH;


    public static void createDataDatabase(Context context) {
        net.sqlcipher.database.SQLiteDatabase database = null;
        try {
            net.sqlcipher.database.SQLiteDatabase.loadLibs(context);
            File file = AppProperties.getDatabaseDir(DatabaseUtils.DATA_DATABASE_NAME + ".db");
            database = net.sqlcipher.database.SQLiteDatabase.openOrCreateDatabase(file, "test", null);
            new AESDBOpenHelper(context).onCreate(database);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null)
                database.close();
        }
    }

    public static void createSearchDatabase(Context context) {
        android.database.sqlite.SQLiteDatabase database = null;
        try {
            File file = AppProperties.getDatabaseDir(DatabaseUtils.SEARCH_DATABASE_NAME + ".db");
            if (!file.exists())
                file.createNewFile();
            database = android.database.sqlite.SQLiteDatabase.openDatabase(file.toString(), null, android.database.sqlite.SQLiteDatabase.OPEN_READWRITE);
            new SearchSuggestionDBOpenHelper(context).onCreate(database);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null)
                database.close();
        }
    }

}
