package com.ericyl.example;

import android.app.Application;
import android.os.HandlerThread;

import com.facebook.drawee.backends.pipeline.Fresco;


public class ExampleApplication extends Application {
    private HandlerThread localHandlerThread;
    private static ExampleApplication applicationContext;


    public static ExampleApplication getApplication() {
        return applicationContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
//        Set<RequestListener> requestListeners = new HashSet<>();
//        requestListeners.add(new RequestLoggingListener());
//        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
//                // other setters
//                .setRequestListeners(requestListeners)
//                .build();
//        Fresco.initialize(this, config);
//        FLog.setMinimumLoggingLevel(FLog.VERBOSE);
        applicationContext = this;
//		startUncaugthExceptionHandler();
    }

    public void exitApp(int exCode) {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(exCode);

    }

    private void startUncaugthExceptionHandler() {
        localHandlerThread = new HandlerThread(this.getClass().getName());
        localHandlerThread.start();
        Thread.setDefaultUncaughtExceptionHandler(new ExampleUncaughtExceptionHandler(
                getApplicationContext()));
    }

}
