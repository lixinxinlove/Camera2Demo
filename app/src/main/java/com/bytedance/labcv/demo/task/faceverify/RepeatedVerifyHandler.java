package com.bytedance.labcv.demo.task.faceverify;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;

import com.bytedance.labcv.demo.model.FaceVerifyResult;
import com.bytedance.labcv.effectsdk.library.LogUtils;

public class RepeatedVerifyHandler extends Handler {

    public interface RepeatedVerifyCallback {
        void onVerifyCallback(FaceVerifyResult result);
        void onPicChoose(int validNum);
        void requestVerifyFrame(Message msg);
    }

    private Context mContext;
    private RepeatedVerifyHandler.RepeatedVerifyCallback mCallback;

    private FaceVerifyThreadHandler mThreadHandler;
    private Messenger mMessenger;


    public RepeatedVerifyHandler(Context context, RepeatedVerifyHandler.RepeatedVerifyCallback callback) {
        mContext = context;
        this.mCallback = callback;
    }

    @Override
    public void handleMessage(Message msg) {
        int what = msg.what;
        switch (what) {
            case FaceVerifyThreadHandler.SUCCESS:
                if (null != mCallback) {
                    FaceVerifyResult result = (FaceVerifyResult) msg.obj;
                    mCallback.onVerifyCallback(result);

                }
                requestOneFrame();
                break;

            case FaceVerifyThreadHandler.FACE_DETECT:
                if (null != mCallback){
                    mCallback.onPicChoose(msg.arg1);
                }
                // 上传的图片中有且仅有一张才开始检测
                // start detect when only one of the uploaded images is detected
                if (msg.arg1 == 1){
                    requestOneFrame();
                }else {
                    if (null != mCallback) {
                        // 复位比对结果
                        // reset
                        mCallback.onVerifyCallback(null);

                    }
                    LogUtils.e("the  bitmap uploaded contais contains no face or more than one face!!");
                }
                break;
        }
    }

    private void requestOneFrame() {
        if (mThreadHandler == null || mMessenger == null || mCallback == null) {
            return;
        }

        Message msg = mThreadHandler.obtainMessage(FaceVerifyThreadHandler.VERIFY);
        msg.replyTo = mMessenger;

        mCallback.requestVerifyFrame(msg);
    }

    public void pause() {
        if (mThreadHandler != null) {
            mThreadHandler.pause();
            removeCallbacksAndMessages(null);
        }
    }

    public void resume() {
        if (mThreadHandler == null) {
            mThreadHandler = FaceVerifyThreadHandler.createFaceVerifyHandlerThread(mContext);
        }
        if (mMessenger == null) {
            mMessenger = new Messenger(this);
        }
        mThreadHandler.resume();
        requestOneFrame();
    }

    /**
     * 设置选择的底图
     * Set the selected image to compare
     *
     * @param originBitmap
     */
    public void setOriginalBitmap(Bitmap originBitmap) {
        if (originBitmap != null) {
            Message msg = mThreadHandler.obtainMessage(FaceVerifyThreadHandler.SET_ORIGINAL, originBitmap);
            msg.replyTo = mMessenger;
            msg.sendToTarget();
        }
    }

    public void release() {
        if (mThreadHandler != null) {
            mThreadHandler.quit();
            mThreadHandler = null;
        }

        if (mMessenger != null) {
            mMessenger = null;
        }
        removeCallbacksAndMessages(null);
    }
}