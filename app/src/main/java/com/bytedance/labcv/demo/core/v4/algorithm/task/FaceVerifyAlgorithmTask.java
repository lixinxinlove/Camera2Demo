package com.bytedance.labcv.demo.core.v4.algorithm.task;

import android.content.Context;

import com.bytedance.labcv.demo.core.v4.algorithm.AlgorithmResourceHelper;
import com.bytedance.labcv.demo.core.v4.algorithm.AlgorithmTask;
import com.bytedance.labcv.demo.core.v4.base.ProcessInput;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKey;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKeyFactory;
import com.bytedance.labcv.effectsdk.BefFaceFeature;
import com.bytedance.labcv.effectsdk.BytedEffectConstants;
import com.bytedance.labcv.effectsdk.FaceVerify;

import java.nio.ByteBuffer;

/**
 * Created by QunZhang on 2020/7/30 18:02
 */
public class FaceVerifyAlgorithmTask extends AlgorithmTask {
    public static final TaskKey FACE_VERIFY = TaskKeyFactory.create("faceVerify", true);

    private static final int MAX_FACE = 10;

    private FaceVerify mDetector;

    public FaceVerifyAlgorithmTask(Context context, AlgorithmResourceProvider resourceProvider) {
        super(context, resourceProvider);

        mDetector = new FaceVerify();
    }

    @Override
    public TaskKey getKey() {
        return FACE_VERIFY;
    }

    @Override
    public ProcessInput.Size preferBufferSize() {
        return null;
    }

    @Override
    public int init() {
        int ret = mDetector.init(mContext, mResourceProvider.getModelPath(AlgorithmResourceHelper.FACE),
                mResourceProvider.getModelPath(AlgorithmResourceHelper.FACEVERIFY), MAX_FACE, mResourceProvider.getLicensePath());
        if (!checkResult("initFaceVerify", ret)) return ret;
        return ret;
    }

    @Override
    public int destroy() {
        mDetector.release();
        return 0;
    }

    @Override
    public int getPriority() {
        return 500;
    }

    public BefFaceFeature extractFeatureSingle(ByteBuffer buffer, BytedEffectConstants.PixlFormat pixelFormat, int width, int height, int stride, BytedEffectConstants.Rotation rotation) {
        return mDetector.extractFeatureSingle(buffer, pixelFormat, width, height, stride, rotation);
    }

    public BefFaceFeature extractFeature(ByteBuffer buffer, BytedEffectConstants.PixlFormat pixelFormat, int width, int height, int stride, BytedEffectConstants.Rotation rotation) {
        return mDetector.extractFeature(buffer, pixelFormat, width, height, stride, rotation);
    }

    public double verify(float[] f1, float[] f2) {
        return mDetector.verify(f1, f2);
    }

    public double distToScore(double dist) {
        return mDetector.distToScore(dist);
    }

}
