package com.ericyl.example.content;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.ericyl.example.util.AppProperties;

import java.io.File;
import java.util.List;

public class ExampleContentProvider extends ContentProvider {
    private SQLiteDatabase connection;
    private Context context;

    public ExampleContentProvider() {
    }

    @Override
    public int delete(@NonNull Uri uri, String whereClause, String[] whereArgs) {
        openDatabase(uri, SQLiteDatabase.OPEN_READWRITE);
        return connection.delete(getTableName(uri), whereClause, whereArgs);
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        openDatabase(uri, SQLiteDatabase.OPEN_READWRITE);
        long rowId;
        rowId = connection.insert(getTableName(uri), null, values);
        if (rowId > 0) {
            return ContentUris.withAppendedId(uri, rowId);
            //getContext().getContentResolver().notifyChange(noteUri, null);
        }
        throw new IllegalArgumentException("Unknown URI" + uri);
    }


    @Override
    public boolean onCreate() {
        context = getContext();
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        try {
            openDatabase(uri, SQLiteDatabase.OPEN_READONLY | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
            return connection.query(getTableFrom(uri), projection, selection, selectionArgs, null, null, sortOrder);
        } catch (Exception e) {
            e.printStackTrace();
            return new MatrixCursor(new String[]{"failed"});
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        openDatabase(uri, SQLiteDatabase.OPEN_READWRITE);
        return connection.update(getTableName(uri), values, selection, selectionArgs);
    }


    private String getTableName(Uri uri) {
        return uri.getPathSegments().get(uri.getPathSegments().size() - 1);
    }

    private void openDatabase(Uri uri, int flag) {
        openDatabase(getFileFrom(uri), flag);
    }

    @SuppressLint("DefaultLocale")
    private File getFileFrom(@NonNull Uri uri) {
        String fileName = uri.getPathSegments().get(0) + ".db";
        return AppProperties.getDatabaseDir(fileName);
    }

    private String getTableFrom(@NonNull Uri uri) {
        List<String> a = uri.getPathSegments();
        if (uri.getPathSegments().size() > 2) {
            return a.get(uri.getPathSegments().size() - 1);
        }
        return a.get(1);
    }


    private void openDatabase(File file, int flags) {
        if (connection != null && connection.isOpen())
            connection.close();
//        SQLiteDatabase conn = null;
        try {
            connection = SQLiteDatabase.openDatabase(file.toString(), null, flags);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return conn;
    }


    @Override
    public void onLowMemory() {
        this.releaseDB();
        super.onLowMemory();
    }

    public void releaseDB() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        connection = null;
    }
}
