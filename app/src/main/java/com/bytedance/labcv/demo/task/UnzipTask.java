package com.bytedance.labcv.demo.task;

import android.content.Context;
import android.os.AsyncTask;

import com.bytedance.labcv.effectsdk.library.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by QunZhang on 2019-07-20 13:05
 */
public class UnzipTask extends AsyncTask<String, Void, Boolean> {
    public static final String DIR = "resource";

    public interface IUnzipViewCallback {
        Context getContext();
        void onStartTask();
        void onEndTask(boolean result);
    }

    private WeakReference<IUnzipViewCallback> mCallback;

    public UnzipTask(IUnzipViewCallback callback) {
        mCallback = new WeakReference<>(callback);
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        IUnzipViewCallback callback = mCallback.get();
        if (callback == null) return false;
        String path = strings[0];
        File dstFile = callback.getContext().getExternalFilesDir("assets");
        FileUtils.clearDir(new File(dstFile, path));

        try {
            FileUtils.copyAssets(callback.getContext().getAssets(), path, dstFile.getAbsolutePath());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

//        return FileUtils.unzipAssetFile(mCallback.get().getContext(), zipPath, dstFile);
    }

    @Override
    protected void onPreExecute() {
        IUnzipViewCallback callback = mCallback.get();
        if (callback == null) return;
        callback.onStartTask();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        IUnzipViewCallback callback = mCallback.get();
        if (callback == null) return;
        callback.onEndTask(result);
    }
}
