package com.bytedance.labcv.demo.core.v4.algorithm.task;

import android.content.Context;
import android.os.Build;

import com.bytedance.labcv.demo.core.v4.algorithm.AlgorithmResourceHelper;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKey;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKeyFactory;
import com.bytedance.labcv.demo.core.v4.algorithm.AlgorithmTask;
import com.bytedance.labcv.demo.core.v4.base.util.TaskFactory;
import com.bytedance.labcv.demo.core.v4.base.ProcessInput;
import com.bytedance.labcv.demo.core.v4.base.ProcessOutput;
import com.bytedance.labcv.demo.utils.timer_record.LogTimerRecord;
import com.bytedance.labcv.effectsdk.BefDistanceInfo;
import com.bytedance.labcv.effectsdk.BytedEffectConstants;
import com.bytedance.labcv.effectsdk.HumanDistance;

import java.util.Map;

/**
 * Created by QunZhang on 2020/7/30 13:48
 */
public class HumanDistanceAlgorithmTask extends AlgorithmTask {
    public static final TaskKey HUMAN_DISTANCE = TaskKeyFactory.create("humanDistance", true);
    public static final TaskKey HUMAN_DISTANCE_FRONT = TaskKeyFactory.create("humanDistanceFront");

    static {
        register();
    }

    private HumanDistance mDetector;
    private float mFov;
    private boolean mIsFront;

    public HumanDistanceAlgorithmTask(Context context, AlgorithmResourceProvider resourceProvider) {
        super(context, resourceProvider);
        mDetector = new HumanDistance();
    }

    @Override
    public TaskKey getKey() {
        return HUMAN_DISTANCE;
    }

    @Override
    public ProcessInput.Size preferBufferSize() {
        return null;
    }

    @Override
    public int init() {
        int ret = mDetector.init(mContext, mResourceProvider.getModelPath(AlgorithmResourceHelper.FACE),
                mResourceProvider.getModelPath(AlgorithmResourceHelper.FACEATTRI),
                mResourceProvider.getModelPath(AlgorithmResourceHelper.HUMANDIST), mResourceProvider.getLicensePath());
        if (!checkResult("initHumanDistance", ret)) return ret;
        return ret;
    }

    @Override
    public ProcessOutput process(ProcessInput input) {
        String deviceName = Build.MODEL;
        mDetector.setParam(BytedEffectConstants.HumanDistanceParamType.BEF_HumanDistanceCameraFov.getValue(), mFov);
        LogTimerRecord.RECORD("humanDistance");
        BefDistanceInfo distanceInfo = mDetector.detectDistance(input.buffer, input.pixelFormat,
                input.bufferSize.getWidth(), input.bufferSize.getHeight(),
                input.bufferStride, deviceName, mIsFront, input.sensorRotation);
        LogTimerRecord.STOP("humanDistance");
        ProcessOutput output = super.process(input);
        output.distanceInfo = distanceInfo;
        return output;
    }

    @Override
    protected void setConfig(Map<TaskKey, Object> config) {
        super.setConfig(config);
        mFov = getFloatConfig(ALGORITHM_FOV);
        mIsFront = getBooleanConfig(HUMAN_DISTANCE_FRONT);
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

    public static void register() {
        TaskFactory.register(HUMAN_DISTANCE, new TaskFactory.TaskGenerator<AlgorithmResourceProvider>() {
            @Override
            public AlgorithmTask create(Context context, AlgorithmResourceProvider provider) {
                return new HumanDistanceAlgorithmTask(context, provider);
            }
        });
    }

    public interface HumanDistanceAlgorithmInterface {
        void setFov(float fov);

        void setFront(boolean front);
    }
}
