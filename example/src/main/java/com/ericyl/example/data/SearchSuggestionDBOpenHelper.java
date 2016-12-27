package com.ericyl.example.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ericyl.example.util.DatabaseUtils;
import com.ericyl.example.util.SearchSuggestionTableUtils;


public class SearchSuggestionDBOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;


    public SearchSuggestionDBOpenHelper(Context context) {
        super(context, DatabaseUtils.DATA_DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createSearchSuggestionTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private static void createSearchSuggestionTable(SQLiteDatabase sdb) {
        String sql = "CREATE TABLE IF NOT EXISTS " + SearchSuggestionTableUtils.TABLE_NAME + " ( "
                + SearchSuggestionTableUtils.INDEX + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SearchSuggestionTableUtils.NAME + " VARCHAR )";
        sdb.execSQL(sql);
    }


}