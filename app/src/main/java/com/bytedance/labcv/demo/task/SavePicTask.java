package com.bytedance.labcv.demo.task;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.bytedance.labcv.demo.ui.BaseEffectActivity;
import com.bytedance.labcv.demo.model.CaptureResult;
import com.bytedance.labcv.demo.utils.BitmapUtils;
import com.bytedance.labcv.demo.utils.ToastUtils;
import com.bytedance.labcv.effectsdk.library.LogUtils;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by QunZhang on 2020/8/4 15:52
 */

public class SavePicTask extends AsyncTask<CaptureResult, Void, String> {
    private WeakReference<Context> mContext;
    private boolean mFinishFlag;

    public SavePicTask(Context context, Boolean finish) {
        mContext = new WeakReference<>(context);
        mFinishFlag = finish;
    }

    @Override
    protected String doInBackground(CaptureResult... captureResults) {
        LogUtils.d("SavePicTask doInBackground enter");
        if (captureResults.length == 0) return "captureResult arrayLength is 0";
        Bitmap bitmap = Bitmap.createBitmap(captureResults[0].getWidth(), captureResults[0].getHeight(), Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(captureResults[0].getByteBuffer().position(0));
        File file = BitmapUtils.saveToLocal(bitmap);
        LogUtils.d("SavePicTask doInBackground finish");

        if (file != null && file.exists()) {
            return file.getAbsolutePath();
        } else {
            return "";
        }
    }

    @Override
    protected void onPostExecute(String path) {
        super.onPostExecute(path);
        if (TextUtils.isEmpty(path)) {
            ToastUtils.show("图片保存失败");
            return;
        }
        if (mContext.get() == null) {
            try {
                new File(path).delete();
            } catch (Exception ignored) {
            }
            ToastUtils.show("图片保存失败");
            return;
        }
        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, path);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
            mContext.get().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        ToastUtils.show("保存成功，路径：" + path);
        if (mFinishFlag) {
            ((BaseEffectActivity) mContext.get()).finish();
        }
    }
}