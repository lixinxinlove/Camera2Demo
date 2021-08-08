package com.bytedance.labcv.demo.task.facecluster;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.CountDownLatch;

public class FaceClusterThread extends Thread {

    private Context mContext;
    private FaceClusterHandler handler;
    private final CountDownLatch handlerInitLatch;

    public FaceClusterThread(Context context) {
        this.mContext = context;
        handlerInitLatch = new CountDownLatch(1);
    }

    public Handler getHandler() {
        try {
            handlerInitLatch.await();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        return handler;
    }

    @Override
    public void run() {
        Looper.prepare();
        handler = new FaceClusterHandler(mContext);
        handlerInitLatch.countDown();
        Looper.loop();
    }

    /**
     * Quit
     */
    public void quit() {

        getHandler().getLooper().quit();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler.release();
        }
        handler = null;

    }
}
