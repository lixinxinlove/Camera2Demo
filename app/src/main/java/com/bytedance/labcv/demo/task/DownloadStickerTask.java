package com.bytedance.labcv.demo.task;

import android.content.Context;
import android.os.AsyncTask;

import com.bytedance.labcv.demo.R;
import com.bytedance.labcv.demo.model.sticker_encrypt.DownloadParam;
import com.bytedance.labcv.demo.model.sticker_encrypt.EncryptParam;
import com.bytedance.labcv.demo.model.sticker_encrypt.EncryptResult;
import com.bytedance.labcv.demo.model.sticker_encrypt.QRScanResult;
import com.bytedance.labcv.demo.ui.DemoApplication;
import com.bytedance.labcv.demo.utils.NetworkUtils;
import com.bytedance.labcv.effectsdk.library.FileUtils;
import com.bytedance.labcv.effectsdk.library.LogUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import static com.bytedance.labcv.demo.utils.Config.DOWNLOAD_URL;
import static com.bytedance.labcv.demo.utils.Config.ENCRYPT_URL;


/**
 * Created by QunZhang on 2021/1/4 14:06
 */
public class DownloadStickerTask
        extends AsyncTask<String, Float, DownloadStickerTask.DownloadStickerResult>
        implements NetworkUtils.DownloadProgressListener {

    private final WeakReference<DownloadStickerTaskCallback> mCallback;
    private NetworkUtils mNetwork;
    private final Gson mGson = new Gson();

    public DownloadStickerTask(DownloadStickerTaskCallback callback) {
        mCallback = new WeakReference<>(callback);
    }

    @Override
    protected void onPreExecute() {
        mNetwork = new NetworkUtils();
        mNetwork.setDownloadProgressCallback(this);
    }

    @Override
    protected DownloadStickerResult doInBackground(String... strings) {
        DownloadStickerResult result = new DownloadStickerResult();
        if (mCallback.get() == null || !(mCallback.get() instanceof Context)) {
            result.code = -1;
            result.msg = "invalid context";
            return result;
        }

        if (mCallback.get() != null && mCallback.get() instanceof Context) {
            boolean networkAvailable = NetworkUtils.isNetworkConnected((Context) mCallback.get());
            if (!networkAvailable) {
                result.code = -1;
                result.msg = mCallback.get().getString(R.string.network_error);
                return result;
            }
        }

        if (strings.length == 0 || strings[0] == null) {
            result.code = -1;
            result.msg = "qr text not found";
            return result;
        }

        LogUtils.i("sticker scan result: " + strings[0]);
        QRScanResult scanResult = mGson.fromJson(strings[0], QRScanResult.class);
        if (scanResult == null || scanResult.secId == null) {
            result.code = -1;
            result.msg = "secId not found";
            return result;
        }

        LogUtils.i("sticker secId: " + scanResult.secId);
        EncryptParam param = new EncryptParam.Builder()
                .setSecId(scanResult.secId)
                .build();

        EncryptResult encryptResult = null;
        try {
            encryptResult = mGson.fromJson(mNetwork.postWithJson(ENCRYPT_URL, mGson.toJson(param)), EncryptResult.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (encryptResult == null || encryptResult.base_response == null) {
            result.code = -1;
            result.msg = "error when get encrypted url";
            return result;
        }

        if (encryptResult.base_response.code != 0) {
            result.code = encryptResult.base_response.code;
            result.msg = encryptResult.base_response.message;
            return result;
        }

        if (encryptResult.data == null || encryptResult.data.encryptUrl == null) {
            result.code = -1;
            result.msg = "invalid data or encryptUrl";
            return result;
        }

        LogUtils.i("encryptUrl: " + encryptResult.data.encryptUrl);
        String url = encryptResult.data.encryptUrl;
        String filePath = generateFilePath(url);
        DownloadParam downloadParam = new DownloadParam.Builder()
                .setEncryptUrl(url)
                .build();
        try {
            String errorMsg = mNetwork.downloadFileWithJson(DOWNLOAD_URL, mGson.toJson(downloadParam), filePath);
//            String errorMsg = mNetwork.downloadFile(url, filePath);
            if (errorMsg != null) {
                result.code = -1;
                result.msg = errorMsg;
                return result;
            }
        } catch (IOException e) {
            result.code = -1;
            result.msg = e.getMessage();
            return result;
        }

        String dstDir = generateStickerDir(filePath);
        LogUtils.i("save sticker dir: " + dstDir);
        boolean unzipResult = FileUtils.unzipFile(filePath, new File(dstDir));
        if (!unzipResult) {
            result.code = -1;
            result.msg = "unzip sticker error";
            return result;
        }

        result.msg = dstDir;
        return result;
    }

    @Override
    protected void onPostExecute(DownloadStickerResult s) {
        DownloadStickerTaskCallback callback = mCallback.get();
        if (callback == null) return;
        if (s == null) {
            callback.onFail("fail");
        }
        assert s != null;
        if (s.code != 0) {
            callback.onFail(s.msg);
        }
        callback.onSuccess(s.msg);
    }

    @Override
    protected void onProgressUpdate(Float... values) {
        DownloadStickerTaskCallback callback = mCallback.get();
        if (callback == null) return;
        callback.onProgressUpdate(values[0]);
    }

    @Override
    public void onProgressUpdate(float progress) {
        publishProgress(progress);
    }

    private String generateFilePath(String url) {
        String[] splits = url.split("/");
        String fileName = splits[splits.length - 1];
        return FileUtils.generateCacheFile(fileName);
    }

    private String generateStickerDir(String filePath) {
        return filePath.substring(0, filePath.length() - 4);
    }

    public interface DownloadStickerTaskCallback {
        void onSuccess(String path);
        void onFail(String message);
        void onProgressUpdate(float progress);
        String getString(int id);
    }

    public static class DownloadStickerResult {
        int code = 0;
        String msg;
    }
}
