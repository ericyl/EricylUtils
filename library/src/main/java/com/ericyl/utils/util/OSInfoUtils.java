package com.ericyl.utils.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;

import com.ericyl.utils.R;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.UUID;

public class OSInfoUtils {

    public static String getManufacturer() {
        return android.os.Build.MANUFACTURER;
    }

    @SuppressLint("HardwareIds")
    public static String getNativePhoneNumber(@NonNull Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getLine1Number();
    }

    @SuppressLint("HardwareIds")
    public static String getIMSI(@NonNull Context context) {
        String ProvidersName = null;
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = telephonyManager.getSubscriberId();
        if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
            ProvidersName = context.getString(R.string.cmcc);
        } else if (imsi.startsWith("46001")) {
            ProvidersName = context.getString(
                    R.string.china_unicom);
        } else if (imsi.startsWith("46003")) {
            ProvidersName = context.getString(
                    R.string.china_telecom);
        }
        return ProvidersName;
    }

    @SuppressLint("HardwareIds")
    public static String getSimCode(@NonNull Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getSubscriberId();
    }

    @SuppressLint("HardwareIds")
    public static UUID getUUID(@NonNull Context context) {
        UUID uuid = null;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String id = telephonyManager.getDeviceId();
            if (id != null)
                uuid = UUID.nameUUIDFromBytes(id.getBytes("utf8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uuid;
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId(@NonNull Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    /**
     * @return windows, linux, mac os x...
     */
    public static String getOS() {
        return System.getProperty("os.name").toLowerCase();
    }

    public static boolean checkHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        switch (PhoneManufacturerUtils.getKey()) {
            case PhoneManufacturerUtils.MEI_ZU_KEY:
                hasNavigationBar = checkHasNavigationBar(context);
                break;
            default:
                Resources resources = context.getResources();
                int showNavigationBarId = resources.getIdentifier("config_showNavigationBar", "bool", "android");
                if (showNavigationBarId > 0) {
                    hasNavigationBar = resources.getBoolean(showNavigationBarId);
                }
                try {
                    Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
                    Method method = systemPropertiesClass.getMethod("get", String.class);
                    String navBarOverride = (String) method.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
                    if ("1".equals(navBarOverride)) {
                        hasNavigationBar = false;
                    } else if ("0".equals(navBarOverride)) {
                        hasNavigationBar = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

        return hasNavigationBar;
    }

    private static boolean checkMeiZuHasNavigationBar(Context context) {
        return Settings.System.getInt(context.getContentResolver(),
                "mz_smartbar_auto_hide", 0) == 1;
    }

    public static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;
        switch (PhoneManufacturerUtils.getKey()) {
            case PhoneManufacturerUtils.MEI_ZU_KEY:
                navigationBarHeight = getMeiZuNavigationBarHeight(context);
                break;
            default:
                Resources resources = context.getResources();
                int navigationBarHeightId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
                if (navigationBarHeightId > 0 && checkHasNavigationBar(context))
                    navigationBarHeight = resources.getDimensionPixelSize(navigationBarHeightId);
                break;
        }
        return navigationBarHeight;
    }

    private static int getMeiZuNavigationBarHeight(Context context) {
        if (!checkMeiZuHasNavigationBar(context))
            return 0;
        else {
            try {
                Class c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("mz_action_button_min_height");
                int height = Integer.parseInt(field.get(obj).toString());
                return context.getResources().getDimensionPixelSize(height);
            } catch (Exception e) {
                return getNavigationBarHeight(context);
            }
        }
    }

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources resources = context.getResources();
        int statusBarHeightId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (statusBarHeightId > 0)
            statusBarHeight = resources.getDimensionPixelSize(statusBarHeightId);
        return statusBarHeight;
    }



}
