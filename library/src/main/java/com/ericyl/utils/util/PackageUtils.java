package com.ericyl.utils.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PackageUtils {


    public static boolean installApk(@NonNull Context context, @NonNull String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = null;
        if (!filePath.isEmpty())
            file = new File(filePath);
        if (file != null && file.exists() && file.isFile()) {
            intent.setDataAndType(Uri.parse("file://" + filePath),
                    "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    public static void uninstallApk(@NonNull Context context, @NonNull String packageName) {
        Uri packageURI = Uri.parse("package:" + packageName);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        context.startActivity(uninstallIntent);
    }

    @SuppressWarnings("deprecation")
    public static boolean findApk(@NonNull Context context, @NonNull String packageName) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context.getPackageManager().getApplicationInfo(packageName,
                        PackageManager.MATCH_UNINSTALLED_PACKAGES);
            } else {
                context.getPackageManager().getApplicationInfo(packageName,
                        PackageManager.GET_UNINSTALLED_PACKAGES);
            }
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getVersionName(@NonNull Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getVersionCode(@NonNull Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getVersionName(@NonNull Context context, @NonNull String packageName) {
        List<PackageInfo> packageInfoList = getAllPackageInfos(context);
        for (PackageInfo packageInfo : packageInfoList) {
            if (packageInfo.packageName.equals(packageName)) {
                return packageInfo.versionName;
            }
        }
        return null;

    }

    public static int getVersionCode(@NonNull Context context, @NonNull String packageName) {
        List<PackageInfo> packageInfoList = getAllPackageInfos(context);
        for (PackageInfo packageInfo : packageInfoList) {
            if (packageInfo.packageName.equals(packageName)) {
                return packageInfo.versionCode;
            }
        }
        return 0;

    }

    public static List<PackageInfo> getAllPackageInfos(@NonNull Context context) {
        return context.getPackageManager().getInstalledPackages(0);
    }

    public static List<PackageInfo> getSystemPackageInfos(@NonNull Context context) {
        List<PackageInfo> packageInfos = new ArrayList<>();
        List<PackageInfo> allPackageInfos = getAllPackageInfos(context);
        for (PackageInfo packageInfo : allPackageInfos) {
            ApplicationInfo appInfo = packageInfo.applicationInfo;
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0)
                packageInfos.add(packageInfo);
        }
        return packageInfos;

    }

    public static List<PackageInfo> getUserPackageInfos(@NonNull Context context) {
        List<PackageInfo> packageInfos = new ArrayList<>();
        List<PackageInfo> allPackageInfos = getAllPackageInfos(context);
        for (PackageInfo packageInfo : allPackageInfos) {
            ApplicationInfo appInfo = packageInfo.applicationInfo;
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0)
                packageInfos.add(packageInfo);
        }
        return packageInfos;
    }
}
