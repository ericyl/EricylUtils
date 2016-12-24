package com.ericyl.utils.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Build;
import android.os.Environment;
import android.os.RemoteException;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ClearCacheUtils {

    public static void clearCache(@NonNull Context context) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        clearApkCache(context);
        clearFreeStorageAndNotify(context);
    }


    public static void clearApkCache(@NonNull Context context) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<ApplicationInfo> apps = context.getPackageManager().getInstalledApplications(
                PackageManager.GET_META_DATA);
        for (ApplicationInfo info : apps) {
            clearCache(context, info.packageName);
        }
    }

    public static void clearCache(@NonNull Context context, @NonNull String packageName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (!TextUtils.isEmpty(packageName)) {
            String methodName = "getPackageSizeInfo";
            Class<?> parameterType1 = String.class;
            Class<?> parameterType2 = IPackageStatsObserver.class;
            Method getPackageSizeInfo = context.getPackageManager().getClass().getMethod(
                    methodName, parameterType1, parameterType2);
            getPackageSizeInfo.invoke(context.getPackageManager(),
                    packageName, new IPackageStatsObserver.Stub() {
                        @Override
                        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
                                throws RemoteException {
                        }
                    });
        }
    }

    public static void clearFreeStorageAndNotify(@NonNull Context context) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String methodName = "freeStorageAndNotify";
        Class<?> parameterType1 = Long.TYPE;
        Class<?> parameterType2 = IPackageDataObserver.class;
        Method freeStorageAndNotify = context.getPackageManager().getClass().getMethod(
                methodName, parameterType1, parameterType2);
        Long localLong = getDataDirectorySize();
        freeStorageAndNotify.invoke(context.getPackageManager(), localLong, new IPackageDataObserver.Stub() {
            public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
            }
        });
    }

    @SuppressWarnings("deprecation")
    private static long getDataDirectorySize() {
        File tmpFile = Environment.getDataDirectory();
        if (tmpFile == null)
            return 0;
        String strDataDirectoryPath = tmpFile.getPath();
        StatFs localStatFs = new StatFs(strDataDirectoryPath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
            return localStatFs.getBlockSizeLong() * localStatFs.getBlockCountLong();
        else
            return localStatFs.getBlockSize() * localStatFs.getBlockCount();
    }

}
