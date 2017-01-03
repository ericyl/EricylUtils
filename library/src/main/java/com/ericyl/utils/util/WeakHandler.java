package com.ericyl.utils.util;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

public class WeakHandler<T> extends Handler {

    private WeakReference<T> reference;
    private OnMessageListener<T> onMessageListener;

    public WeakHandler(T context, OnMessageListener<T> onMessageListener) {
        reference = new WeakReference<>(context);
        this.onMessageListener = onMessageListener;
    }

    @Override
    public void handleMessage(Message msg) {
        final T context = reference.get();
        onMessageListener.messageListener(context, msg);
    }

    public interface OnMessageListener<T> {
        void messageListener(T context, Message message);
    }
}
