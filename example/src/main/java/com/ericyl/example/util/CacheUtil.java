package com.ericyl.example.util;

import android.content.Context;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.ericyl.example.util.FileUtil.getDirSize;


public class CacheUtil {
    private static CacheUtil cacheUtil;

    public static CacheUtil getInstance() {
        if (cacheUtil == null) {
            cacheUtil = new CacheUtil();
        }
        return cacheUtil;
    }

    public Observable<CharSequence> clearCache(Context context) {
        return Observable.just(context).subscribeOn(Schedulers.io()).map(new Func1<Context, Context>() {
            @Override
            public Context call(Context context) {
//                Glide.get(context).clearDiskCache();
                FileUtil.deleteFile(context.getExternalCacheDir());
                FileUtil.deleteFile(context.getCacheDir());
                return context;
            }
        })
//                .observeOn(AndroidSchedulers.mainThread()).map(new Func1<Context, Context>() {
//            @Override
//            public Context call(Context context) {
//                Glide.get(context).clearMemory();
//                return context;
//            }
//        })
                .flatMap(new Func1<Context, Observable<CharSequence>>() {
                    @Override
                    public Observable<CharSequence> call(Context context) {
                        return getCacheSize(context);
                    }
                });
    }


    public Observable<CharSequence> getCacheSize(Context context) {
        return Observable.just(context).subscribeOn(Schedulers.io()).map(new Func1<Context, CharSequence>() {
            @Override
            public CharSequence call(Context context) {
//                long size = getDirSize(new File(context.getCacheDir() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR));
//                size += getDirSize(new File(context.getExternalCacheDir() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR));
                long size = getDirSize(context.getCacheDir());
                size += getDirSize(context.getExternalCacheDir());
                return FileUtil.formatSize(size);
            }
        });
    }


}