package com.ericyl.utils.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class ImageLoaderUtils {
    private static ImageLoaderUtils imageLoaderUtils;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static Context context;

    private ImageLoaderUtils(@NonNull final Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
        imageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {
                    private final BitmapLruCache
                            cache = BitmapLruCache.newInstance(context);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized ImageLoaderUtils getInstance(@NonNull Context context) {
        if (imageLoaderUtils == null) {
            imageLoaderUtils = new ImageLoaderUtils(context);
        }
        return imageLoaderUtils;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}