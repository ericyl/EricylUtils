package com.ericyl.utils.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class PermissionUtils {

    public static boolean checkPermission(@NonNull Context context, @NonNull String permission) {
        return (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED);
    }

    public static void requestPermission(@NonNull Activity activity, int requestCode, @NonNull String permission) {
        requestPermission(activity, requestCode, permission, null);

    }

    public static void requestPermission(@NonNull Activity activity, int requestCode, @NonNull String permission, @Nullable IShowRationaleListener showRationaleListener) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            if (showRationaleListener != null)
                showRationaleListener.showRequestPermissionRationale();
            else
                ActivityCompat.requestPermissions(activity,
                        new String[]{permission},
                        requestCode);
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{permission},
                    requestCode);
        }

    }

    public static void requestPermissions(@NonNull Activity activity, int requestCode, @NonNull String... permissions) {
        String[] requestPermissions = getRequestPermissions(activity, permissions);
        if (requestPermissions == null || requestPermissions.length == 0)
            return;
        ActivityCompat.requestPermissions(activity,
                requestPermissions,
                requestCode);

    }

    public static boolean checkPermissions(@NonNull Context context, @NonNull String... permissions) {
        boolean flag = true;
        for (String permission : permissions) {
            flag = flag && (checkPermission(context, permission));
        }
        return flag;
    }

    private static String[] getRequestPermissions(@NonNull Context context, @NonNull String... permissions) {
        String[] str;
        List<String> list = new ArrayList<>();
        for (String permission : permissions) {
            if (!checkPermission(context, permission))
                list.add(permission);
        }
        list.toArray(str = new String[list.size()]);
        return str;
    }


    public static Set<String> getAuthorityFromPermission(@NonNull Context context, @Nullable String permission) {
        Set<String> authorities = new HashSet<>();
        if (permission == null || permission.isEmpty())
            return null;
        List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
        if (packs != null) {
            for (PackageInfo pack : packs) {
                ProviderInfo[] providers = pack.providers;
                if (providers != null) {
                    for (ProviderInfo provider : providers) {
                        if (permission.equals(provider.readPermission))
                            authorities.add(provider.authority);
                        if (permission.equals(provider.writePermission))
                            authorities.add(provider.authority);
                    }
                }
            }
        }

        return authorities;
    }

    interface IShowRationaleListener {
        void showRequestPermissionRationale();
    }

}
