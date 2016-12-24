package com.ericyl.utils.util;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;


public class BitmapLruCache extends LruCache<String, Bitmap> {
    private static BitmapLruCache bitmapLruCache;

    public static BitmapLruCache newInstance() {
        return newInstance(null);
    }

    public static synchronized BitmapLruCache newInstance(@Nullable Context context) {
        if (bitmapLruCache == null) {
            int cacheSize;
            if (context != null)
                cacheSize = getCacheSize(context);
            else
                cacheSize = getCacheSize();
            bitmapLruCache = new BitmapLruCache(cacheSize);
        }
        return bitmapLruCache;

    }

    private static int getCacheSize() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        return maxMemory / 8;
    }

    private static int getCacheSize(@NonNull Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int availMemInBytes = am.getMemoryClass() * 1024 * 1024;
        return availMemInBytes / 8;
    }

    private BitmapLruCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        super.sizeOf(key, value);
        return value.getByteCount();
    }
}
