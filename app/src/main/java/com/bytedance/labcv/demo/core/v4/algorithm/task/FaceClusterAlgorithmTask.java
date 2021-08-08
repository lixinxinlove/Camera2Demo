package com.bytedance.labcv.demo.core.v4.algorithm.task;

import android.content.Context;

import com.bytedance.labcv.demo.core.v4.base.util.TaskKey;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKeyFactory;
import com.bytedance.labcv.demo.core.v4.algorithm.AlgorithmTask;
import com.bytedance.labcv.demo.core.v4.base.ProcessInput;
import com.bytedance.labcv.effectsdk.FaceCluster;

/**
 * Created by QunZhang on 2020/7/30 20:23
 */
public class FaceClusterAlgorithmTask extends AlgorithmTask {
    public static final TaskKey FACE_CLUSTER = TaskKeyFactory.create("faceCluster", true);

    private FaceCluster mDetector;

    public FaceClusterAlgorithmTask(Context context, AlgorithmResourceProvider resourceProvider) {
        super(context, resourceProvider);

        mDetector = new FaceCluster();
    }

    @Override
    public TaskKey getKey() {
        return FACE_CLUSTER;
    }

    @Override
    public ProcessInput.Size preferBufferSize() {
        return null;
    }

    @Override
    public int init() {
        int ret = mDetector.init(mContext, mResourceProvider.getLicensePath());
        if (!checkResult("initFaceCluster", ret)) return ret;
        return ret;
    }

    @Override
    public int destroy() {
        mDetector.release();
        return 0;
    }

    @Override
    public int getPriority() {
        return 1000;
    }

    public int[] cluster(float[][] features, int size) {
        return mDetector.cluster(features, size);
    }
}
