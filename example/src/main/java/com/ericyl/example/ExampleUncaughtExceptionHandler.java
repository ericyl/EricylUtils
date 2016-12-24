package com.ericyl.example;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Properties;

public class ExampleUncaughtExceptionHandler implements UncaughtExceptionHandler {
    private Properties _deviceCrashInfo = new Properties();
    private static final String VERSION_NAME = "VERSIONNAME";
    private static final String VERSION_CODE = "VERSIONCODE";
    private static final String STACK_TRACE = "STACK_TRACE";
    private static final String CRASH_REPORTER_EXTENSION = ".crs";

    private Context context;

    public ExampleUncaughtExceptionHandler() {

    }

    public ExampleUncaughtExceptionHandler(Context context) {
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
        collectCrashDeviceInfo(context);
        saveCrashInfoToFile(ex);
        if (context != null) {
            ExampleApplication app = (ExampleApplication) context
                    .getApplicationContext();
            app.exitApp(-1);
        }
    }

    private void collectCrashDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                _deviceCrashInfo.put(VERSION_NAME,
                        pi.versionName == null ? "not set" : pi.versionName);
                _deviceCrashInfo.put(VERSION_CODE,
                        String.valueOf(pi.versionCode));
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                _deviceCrashInfo.put(field.getName(), field.get(null)
                        .toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String saveCrashInfoToFile(Throwable ex) {
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        ex.printStackTrace(printWriter);

        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }

        String result = info.toString();
        printWriter.close();
        _deviceCrashInfo.put(STACK_TRACE, result);

        try {
            long timestamp = System.currentTimeMillis();
            String fileName = "crash-" + timestamp + CRASH_REPORTER_EXTENSION;

            File file = null;
            if (Environment.getExternalStorageDirectory() != null) {
                file = new File(context.getExternalFilesDir("crash") + File.separator
                        + fileName);
            }

            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            FileOutputStream trace = new FileOutputStream(file);
            _deviceCrashInfo.store(trace, "");
            trace.close();
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
