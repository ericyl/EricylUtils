package com.ericyl.utils.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.Toast;

import com.ericyl.utils.R;

import java.io.File;

public class ActivityUtils {

    public static void jumpActivity(@NonNull Context context, @NonNull String packageName,
                                    @NonNull String startActivityName) {
        jumpActivity(context, packageName, startActivityName, null);
    }

    public static void jumpActivity(@NonNull Context context, @NonNull String packageName,
                                    @NonNull String startActivityName, @Nullable Bundle bundle) {
        jumpActivity(context, packageName, startActivityName, bundle, Intent.ACTION_VIEW, Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * Activity jump to other package activity
     *
     * @param context           getContext()
     * @param packageName       other activity package name like{com.ericyl.utils.example}
     * @param startActivityName other activity name like{com.ericyl.utils.example.DemoActivity}
     * @param bundle            data bundle
     * @param action            intent.action link{Intent.ACTION_*}
     * @param flags             intent.flag link{Intent.ACTION_*}
     *                          default{Intent.FLAG_ACTIVITY_NEW_TASK}
     */
    public static void jumpActivity(@NonNull Context context, @NonNull String packageName,
                                    @NonNull String startActivityName, @Nullable Bundle bundle, String action, int flags) {
        Intent intent = new Intent();
        if (action != null)
            intent.setAction(action);
        if (flags == 0)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        else
            intent.setFlags(flags);
        intent.setComponent(new ComponentName(packageName, startActivityName));
        if (bundle != null)
            intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void jumpActivity(@NonNull Context context,
                                    @NonNull Class<? extends Activity> cls) {
        jumpActivity(context, cls, null);
    }

    public static void jumpActivity(@NonNull Context context,
                                    @NonNull Class<? extends Activity> cls, @Nullable Bundle extras) {
        jumpActivity(context, cls, extras, null, Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * Activity jump to other activity
     *
     * @param context getContext()
     * @param cls     other activity Class name like{DemoActivity.class}
     * @param extras  data bundle
     * @param action  intent.action link{Intent.ACTION_*}
     * @param flags   intent.flag {Intent.FLAG_*}
     *                default{Intent.FLAG_ACTIVITY_NEW_TASK}
     */
    public static void jumpActivity(@NonNull Context context,
                                    @NonNull Class<? extends Activity> cls, @Nullable Bundle extras, String action, int flags) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        if (action != null)
            intent.setAction(action);
        if (flags == 0)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        else
            intent.setFlags(flags);
        if (extras != null)
            intent.putExtras(extras);
        context.startActivity(intent);
    }

    public static void jumpActivityWithResult(@NonNull Activity activity, @NonNull String packageName,
                                              @NonNull String startActivityName, int request) {
        jumpActivityWithResult(activity, packageName, startActivityName, null, request);
    }

    public static void jumpActivityWithResult(@NonNull Activity activity, @NonNull String packageName,
                                              @NonNull String startActivityName, @Nullable Bundle bundle, int request) {
        jumpActivityWithResult(activity, packageName, startActivityName, bundle, Intent.ACTION_VIEW, Intent.FLAG_ACTIVITY_NEW_TASK, request);
    }

    public static void jumpActivityWithResult(@NonNull Activity activity, @NonNull String packageName,
                                              @NonNull String startActivityName, @Nullable Bundle bundle, String action, int flags, int request) {
        Intent intent = new Intent();
        if (action != null)
            intent.setAction(action);
        if (flags == 0)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        else
            intent.setFlags(flags);
        intent.setComponent(new ComponentName(packageName, startActivityName));
        if (bundle != null)
            intent.putExtras(bundle);
        activity.startActivityForResult(intent, request);
    }

    public static void jumpActivity(@NonNull Activity activity,
                                    @NonNull Class<? extends Activity> cls, int request) {
        jumpActivityWithResult(activity, cls, null, request);
    }

    public static void jumpActivityWithResult(@NonNull Activity activity,
                                              @NonNull Class<? extends Activity> cls, @Nullable Bundle extras, int request) {
        jumpActivityWithResult(activity, cls, extras, null, Intent.FLAG_ACTIVITY_NEW_TASK, request);

    }

    public static void jumpActivityWithResult(@NonNull Activity activity,
                                              @NonNull Class<? extends Activity> cls, @Nullable Bundle extras, String action, int flags, int request) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        if (action != null)
            intent.setAction(action);
        if (flags == 0)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        else
            intent.setFlags(flags);
        if (extras != null)
            intent.putExtras(extras);
        activity.startActivityForResult(intent, request);
    }


    /**
     * @param context          getContext
     * @param mimeType         media type
     * @param fileProviderName like{BuildConfig.APPLICATION_ID + ".fileProvider"}
     * @param fileLocalUri     file local Uri
     */
    public static void openFile(@NonNull Context context, String mimeType, String fileProviderName, String fileLocalUri) {
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.setAction(Intent.ACTION_VIEW);
            if (TextUtils.isEmpty(mimeType))
                mimeType = "*";

            Uri contentUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                contentUri = FileProvider.getUriForFile(context, fileProviderName, new File(fileLocalUri));
            } else {
                contentUri = Uri.parse(fileLocalUri);
            }
            intent.setDataAndType(contentUri, mimeType);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, R.string.can_not_open_file, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, R.string.open_file_failed, Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * @param context          getContext
     * @param mimeType         media type
     * @param fileProviderName like{BuildConfig.APPLICATION_ID + ".fileProvider"}
     * @param fileLocalUri     file local Uri
     */
    public static void chooseAppOpenFile(@NonNull Context context, String mimeType, String fileProviderName, String fileLocalUri) {
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.setAction(Intent.ACTION_VIEW);
            if (TextUtils.isEmpty(mimeType))
                mimeType = "*";
            Uri contentUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                contentUri = FileProvider.getUriForFile(context, fileProviderName, new File(fileLocalUri));
            } else {
                contentUri = Uri.parse(fileLocalUri);
            }
            intent.setDataAndType(contentUri, mimeType);
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_open_file)));
        } catch (Exception e) {
            Toast.makeText(context, R.string.open_file_failed, Toast.LENGTH_SHORT).show();
        }
    }


    public static void openGallery(Activity activity, int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        activity.startActivityForResult(Intent.createChooser(intent, activity.getString(R.string.choose_open_file)), requestCode);
    }

}
