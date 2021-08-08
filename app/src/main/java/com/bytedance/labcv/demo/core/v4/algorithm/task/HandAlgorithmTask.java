package com.bytedance.labcv.demo.core.v4.algorithm.task;

import android.content.Context;

import com.bytedance.labcv.demo.core.v4.algorithm.AlgorithmResourceHelper;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKey;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKeyFactory;
import com.bytedance.labcv.demo.core.v4.algorithm.AlgorithmTask;
import com.bytedance.labcv.demo.core.v4.base.util.TaskFactory;
import com.bytedance.labcv.demo.core.v4.base.ProcessInput;
import com.bytedance.labcv.demo.core.v4.base.ProcessOutput;
import com.bytedance.labcv.demo.utils.timer_record.LogTimerRecord;
import com.bytedance.labcv.effectsdk.BefHandInfo;
import com.bytedance.labcv.effectsdk.BytedEffectConstants;
import com.bytedance.labcv.effectsdk.HandDetect;

/**
 * Created by QunZhang on 2020/7/30 16:55
 */
public class HandAlgorithmTask extends AlgorithmTask {
    public static final TaskKey HAND = TaskKeyFactory.create("hand", true);

    static {
        register();
    }

    private static final int MAX_HAND = 1;
    private static final float ENLARGE_FACTOR = 2.f;
    private static final int NARUTO_GESTURE = 1;
    private static final int HAND_DETECT_DELAY_FRAME_COUNT = 0;

    private HandDetect mDetector;

    public HandAlgorithmTask(Context context, AlgorithmResourceProvider resourceProvider) {
        super(context, resourceProvider);
        mDetector = new HandDetect();
    }

    @Override
    public TaskKey getKey() {
        return HAND;
    }

    @Override
    public ProcessInput.Size preferBufferSize() {
        return null;
    }

    @Override
    public int init() {
        int ret = mDetector.createHandle(mContext, mResourceProvider.getLicensePath());
        if (!checkResult("initHand", ret)) return ret;
        ret = mDetector.setModel(BytedEffectConstants.HandModelType.BEF_HAND_MODEL_DETECT, mResourceProvider.getModelPath(AlgorithmResourceHelper.HAND_DETECT));
        if (!checkResult("initHand", ret)) return ret;
        ret = mDetector.setModel(BytedEffectConstants.HandModelType.BEF_HAND_MODEL_BOX_REG, mResourceProvider.getModelPath(AlgorithmResourceHelper.HAND_BOX));
        if (!checkResult("initHand", ret)) return ret;
        ret = mDetector.setModel(BytedEffectConstants.HandModelType.BEF_HAND_MODEL_GESTURE_CLS, mResourceProvider.getModelPath(AlgorithmResourceHelper.HAND_GESTURE));
        if (!checkResult("initHand", ret)) return ret;
        ret = mDetector.setModel(BytedEffectConstants.HandModelType.BEF_HAND_MODEL_KEY_POINT, mResourceProvider.getModelPath(AlgorithmResourceHelper.HAND_KEY_POINT));
        if (!checkResult("initHand", ret)) return ret;

        ret = mDetector.setParam(BytedEffectConstants.HandParamType.BEF_HAND_MAX_HAND_NUM, MAX_HAND);
        if (!checkResult("initHand", ret)) return ret;
        ret = mDetector.setParam(BytedEffectConstants.HandParamType.BEF_HNAD_ENLARGE_FACTOR_REG, ENLARGE_FACTOR);
        if (!checkResult("initHand", ret)) return ret;
        ret = mDetector.setParam(BytedEffectConstants.HandParamType.BEF_HAND_NARUTO_GESTUER, NARUTO_GESTURE);
        if (!checkResult("initHand", ret)) return ret;
        return ret;
    }

    @Override
    public ProcessOutput process(ProcessInput input) {
        LogTimerRecord.RECORD("detectHand");
        BefHandInfo handInfo = mDetector.detectHand(input.buffer, input.pixelFormat,
                input.bufferSize.getWidth(), input.bufferSize.getHeight(), input.bufferStride,
                input.sensorRotation, BytedEffectConstants.HandModelType.BEF_HAND_MODEL_DETECT.getValue() |
                        BytedEffectConstants.HandModelType.BEF_HAND_MODEL_BOX_REG.getValue() |
                        BytedEffectConstants.HandModelType.BEF_HAND_MODEL_GESTURE_CLS.getValue() |
                        BytedEffectConstants.HandModelType.BEF_HAND_MODEL_KEY_POINT.getValue(),
                HAND_DETECT_DELAY_FRAME_COUNT);
        LogTimerRecord.STOP("detectHand");
        ProcessOutput output = super.process(input);
        output.handInfo = handInfo;
        return output;
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

    public static void register() {
        TaskFactory.register(HAND, new TaskFactory.TaskGenerator<AlgorithmResourceProvider>() {
            @Override
            public AlgorithmTask create(Context context, AlgorithmResourceProvider provider) {
                return new HandAlgorithmTask(context, provider);
            }
        });
    }

    public interface HandAlgorithmInterface {

    }
}
