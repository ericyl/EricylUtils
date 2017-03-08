package com.ericyl.utils.util;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

public abstract class CountUpTimer {
    private static final int MSG = 1;
    private final long mCountUpInterval;
    private long mStopTimeInFuture;
    private long mPauseTimeInFuture;
    private boolean isStop = false;
    private boolean isPause = false;

    public CountUpTimer(long countUpInterval) {
        mCountUpInterval = countUpInterval;
    }

    private synchronized CountUpTimer start(long millisInFuture) {
        isStop = false;
        if (millisInFuture < 0)
            return null;
        mStopTimeInFuture = SystemClock.elapsedRealtime() - millisInFuture;
        mHandler.removeMessages(MSG);
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        return this;
    }

    public synchronized final void start() {
        start(0);
    }

    public synchronized final void stop() {
        isStop = true;
        mHandler.removeMessages(MSG);
    }

    public synchronized final void pause() {
        if (isStop) return;

        isPause = true;
        mPauseTimeInFuture = SystemClock.elapsedRealtime() - mStopTimeInFuture;
        mHandler.removeMessages(MSG);
    }

    public synchronized final void restart() {
        if (isStop || !isPause) return;

        isPause = false;
        start(mPauseTimeInFuture);
    }

    public synchronized final void recycledstart() {
        isStop = false;
        isPause = false;
        mHandler.removeMessages(MSG);
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
    }

    public abstract void onTick(long millisUntilFinished);


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            synchronized (CountUpTimer.this) {
                if (isStop || isPause)
                    return;

                final long millisLeft = SystemClock.elapsedRealtime() - mStopTimeInFuture;
                if (millisLeft < mCountUpInterval) {
                    sendMessageDelayed(obtainMessage(MSG), mCountUpInterval);
                } else {
                    onTick(millisLeft);
                    sendMessageDelayed(obtainMessage(MSG), mCountUpInterval);
                }
            }
        }
    };
}
