package com.bytedance.labcv.demo.task.facecluster;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.bytedance.labcv.demo.R;
import com.bytedance.labcv.demo.core.v4.ResourceHelper;
import com.bytedance.labcv.demo.core.v4.algorithm.task.FaceClusterAlgorithmTask;
import com.bytedance.labcv.demo.core.v4.algorithm.task.FaceVerifyAlgorithmTask;
import com.bytedance.labcv.demo.utils.BitmapUtils;
import com.bytedance.labcv.demo.utils.ToastUtils;
import com.bytedance.labcv.effectsdk.BefFaceFeature;
import com.bytedance.labcv.effectsdk.BytedEffectConstants;
import com.bytedance.labcv.effectsdk.library.LogUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.bytedance.labcv.effectsdk.BytedEffectConstants.BytedResultCode.BEF_RESULT_INVALID_LICENSE;
import static com.bytedance.labcv.effectsdk.BytedEffectConstants.BytedResultCode.BEF_RESULT_SUC;

/**
 * 在聚类线程控制聚类请求
 * The cluster thread which controls the cluster request
 */
public class FaceClusterHandler extends Handler {
    public static final int ADD = 3001;
    public static final int CLUSTER = 3002;
    public static final int CLEAR = 3003;
    public static final int SUCCESS = 3010;
    public static final int FACE_DETECT = 3011;
    public static final int ERROR_PHOTO = 3012;

    private FaceVerifyAlgorithmTask mFaceVerify;
    private FaceClusterAlgorithmTask mFaceCluster;
    private List<Integer> mFaceNumList = new LinkedList<>();
    private List<float[]> mFeatures = new LinkedList<>();
    private boolean isRunning = false;
    private List<String> mPhotoUri;
    private boolean mContainInValid;


    public FaceClusterHandler(Context context) {
        this.mFaceVerify = new FaceVerifyAlgorithmTask(context, new ResourceHelper(context));
        this.mFaceCluster = new FaceClusterAlgorithmTask(context, new ResourceHelper(context));

        int ret = initFaceCluster();
        if (ret == BEF_RESULT_INVALID_LICENSE) {
            ToastUtils.show(context.getResources().getString(R.string.tab_face_cluster) + context.getResources().getString(R.string.invalid_license_file));
        } else if (ret != BEF_RESULT_SUC) {
            ToastUtils.show("FaceCluster Initialization failed");
        }
    }

    private int initFaceCluster() {
        int ret = mFaceVerify.init();
        if (ret != BEF_RESULT_SUC){
            return ret;
        }
        ret = mFaceCluster.init();
        return ret;
    }

    private void addPicture(Bitmap bitmap){
        BefFaceFeature temp = getFaceFeature(bitmap);
        if (temp == null) return;
        LogUtils.d("cluster add pic facenum =" + temp.getValidFaceNum());
        mFaceNumList.add(temp.getValidFaceNum());
        if (null != temp.getFeatures()) {
            for (float[] feature : temp.getFeatures()) {
                mFeatures.add(feature);
            }
        }else{
            mContainInValid = true;
        }
    }

    /**
     * 每次聚类完清空缓存
     * clean all cache
     */
    private void cleanPictures(){
        mFaceNumList.clear();
        mFeatures.clear();
        mPhotoUri.clear();
        mContainInValid = false;
    }

    private BefFaceFeature getFaceFeature(Bitmap bitmap){
        if (bitmap != null){
            ByteBuffer buffer = BitmapUtils.bitmap2ByteBuffer(bitmap);
            return mFaceVerify.extractFeature(buffer, BytedEffectConstants.PixlFormat.RGBA8888, bitmap.getWidth(), bitmap.getHeight(), 4 * bitmap.getWidth(), BytedEffectConstants.Rotation.CLOCKWISE_ROTATE_0);
        }
        return null;
    }

    private List<List<String>> cluster(){
        LogUtils.d("start cluster feature num = "+ mFeatures.size());

        float[][] featuresTemp =  mFeatures.toArray(new float[mFeatures.size()][]);
        int[] clusters = mFaceCluster.cluster(featuresTemp, mFeatures.size());

        StringBuilder clusterResultStr = new StringBuilder();

        int clusterNums = 0;//cluster classes 聚类的类型
        if (null != clusters) {
            for (int cla : clusters) {
                if (cla > clusterNums) {
                    clusterNums = cla;
                }
                clusterResultStr.append(cla).append(",");
            }
            clusterNums++;
        }

        LogUtils.d("start cluster result = "+ clusterResultStr.toString());

        //初始化
        List<List<String>> clustersResult = new ArrayList<>(clusterNums);
        for (int i = 0; i < clusterNums; i++){
            List<String> temp = new LinkedList<>();
            clustersResult.add(temp);
        }
        //添加无法聚类的图片集合
        // Add a collection of images that cannot be clustered
        if (mContainInValid){
            List<String> temp = new LinkedList<>();
            clustersResult.add(temp);
        }

        int featureIndex = 0;
        int pictureIndex = 0;
        for (int num: mFaceNumList){
            if (0 == num){
                String temp = mPhotoUri.get(pictureIndex);
                clustersResult.get(clustersResult.size()-1).add(temp);
                pictureIndex++;
                continue;
            }
            for (int i = 0; i < num; i++){
                String temp = mPhotoUri.get(pictureIndex);
                List<String> clusterList = clustersResult.get(clusters[featureIndex++]);
                //防止同个类别图片重复
                //Prevent duplicates in the same category
                if (!clusterList.contains(temp)){
                    clusterList.add(temp);
                }
            }
            pictureIndex++;
        }

        return clustersResult;
    }

    public void release(){
        if (null != mFaceVerify){
            mFaceVerify.destroy();
        }
        if (null != mFaceCluster){
            mFaceCluster.destroy();
        }
    }


    @Override
    public void handleMessage(Message msg) {

        if (isRunning || null == mFaceVerify || null == mFaceCluster) {
            return;
        }
        Messenger messenger = msg.replyTo;
        switch (msg.what) {
            case CLUSTER:
                mPhotoUri = (List<String>) msg.obj;
                if (null == mPhotoUri)
                    return;
                isRunning = true;

                int process = 0;
                //逐张图片提取特征
                // Feature extraction by image
                Iterator<String> iter = mPhotoUri.iterator();
                while(iter.hasNext()){
                    if (!FaceClusterMgr.isRunning){
                        LogUtils.d("Handler stop cluster ");
                        cleanPictures();
                        isRunning = false;
                        return;
                    }
                    String pic = iter.next();
                    Bitmap bitmap = BitmapUtils.decodeBitmapFromFile(pic, 800, 800);
                    if (bitmap == null || bitmap.isRecycled()){
                        LogUtils.d("failed to get image = "+ pic);
                        //delete error picture
                        iter.remove();

                        Message resultMsg = obtainMessage();
                        resultMsg.what = ERROR_PHOTO;
                        try {
                            messenger.send(resultMsg);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    addPicture(bitmap);
                    sendProcess(process);
                }
                //开始对特征聚类
                // start clustering
                List<List<String>> result = cluster();
                boolean containInVaild = mContainInValid;
                cleanPictures();


                Message resultMsg = obtainMessage();
                try {
                    resultMsg.what = SUCCESS;
                    resultMsg.obj = result;
                    resultMsg.arg1 = containInVaild ? result.size() - 1: result.size();
                    messenger.send(resultMsg);
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }finally {
                    isRunning = false;
                }
                break;
        }


    }

    private void sendProcess(int process) {

    }
}
