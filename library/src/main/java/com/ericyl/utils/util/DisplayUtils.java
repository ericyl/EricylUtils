package com.ericyl.utils.util;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Surface;

public class DisplayUtils {


    public static float getDensity(@NonNull Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.density;
    }

    private static float getScaledDensity(@NonNull Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.scaledDensity;
    }

    public static int getScreenWidth(@NonNull Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight(@NonNull Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static float dp2px(@NonNull Context context, float dp, float density) {
        if (density <= 0)
            density = getDensity(context);
        return dp * density + 0.5f * (dp >= 0 ? 1 : -1);
    }


    public static float px2dp(@NonNull Context context, float px, float density) {
        if (density <= 0)
            density = getDensity(context);

        return (px / density + 0.5f * (px >= 0 ? 1 : -1));
    }


    public static float sp2px(@NonNull Context context, float sp, float scaledDensity) {
        if (scaledDensity <= 0)
            scaledDensity = getDensity(context);

        return sp * scaledDensity + 0.5f * (sp >= 0 ? 1 : -1);
    }

    public static float px2sp(@NonNull Context context, float px, float scaledDensity) {
        if (scaledDensity <= 0)
            scaledDensity = getDensity(context);

        return px / scaledDensity + 0.5f * (px >= 0 ? 1 : -1);
    }

    /**
     * 获取当前屏幕旋转角度
     *
     * @param activity 当前activity
     * @return 0表示是竖屏; 90表示是左横屏; 180表示是反向竖屏; 270表示是右横屏
     */
    public static int getScreenRotation(@NonNull Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        switch (rotation) {
            default:
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
    }

    /**
     * 锁住屏幕，让屏幕不能旋转
     *
     * @param activity getActivity()
     */
    public static void lockActivity(@NonNull Activity activity) {
        lockActivity(activity, getScreenRotation(activity));
    }

    /**
     * 根据传入的角度设置当前屏幕的状态，使之不能旋转
     *
     * @param activity getActivity()
     * @param degree   旋转的角度。一般是通过{@link #getScreenRotation(Activity)}方法获得的数据
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void lockActivity(@NonNull Activity activity, int degree) {
        int display;
        switch (degree) {
            case 90:
                display = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                break;
            case 180:
                display = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                break;
            case 270:
                display = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                break;
            default:
            case 0:
                display = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                break;
        }
        activity.setRequestedOrientation(display);
    }

    /**
     * 判断系统设置是否打开了屏幕旋转
     *
     * @param context getContext()
     * @return is accelerometer rotation
     */
    public static boolean isAccelerometerRotation(@NonNull Context context) {
        return Settings.System.getInt(context.getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, 0) != 0;
    }

    /**
     * 判断是否横屏
     *
     * @param context getContext()
     * @return is landscape orientation
     */
    public static boolean isLandscapeOrientation(@NonNull Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

}
