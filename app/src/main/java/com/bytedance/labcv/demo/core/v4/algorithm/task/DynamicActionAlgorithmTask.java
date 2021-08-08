package com.bytedance.labcv.demo.core.v4.algorithm.task;

import android.content.Context;

import com.bytedance.labcv.demo.core.v4.algorithm.AlgorithmResourceHelper;
import com.bytedance.labcv.demo.core.v4.algorithm.AlgorithmTask;
import com.bytedance.labcv.demo.core.v4.base.ProcessInput;
import com.bytedance.labcv.demo.core.v4.base.ProcessOutput;
import com.bytedance.labcv.demo.core.v4.base.Task;
import com.bytedance.labcv.demo.core.v4.base.util.TaskFactory;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKey;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKeyFactory;
import com.bytedance.labcv.demo.utils.timer_record.LogTimerRecord;
import com.bytedance.labcv.effectsdk.BefDynamicActionInfo;
import com.bytedance.labcv.effectsdk.BytedEffectConstants;
import com.bytedance.labcv.effectsdk.DynamicActionDetect;

/**
 * Created by QunZhang on 2020/8/4 16:38
 */
public class DynamicActionAlgorithmTask extends AlgorithmTask {
    public static final TaskKey DYNAMIC_ACTION = TaskKeyFactory.create("dynamicAction", true);

    static {
        register();
    }

    private static final int DETECT_CONFIG = 0;
    private static final int FRAME_COUNT = 0;

    private DynamicActionDetect mDetector;

    public DynamicActionAlgorithmTask(Context context, AlgorithmResourceProvider resourceProvider) {
        super(context, resourceProvider);

        mDetector = new DynamicActionDetect();
    }

    @Override
    public ProcessInput.Size preferBufferSize() {
        return null;
    }

    @Override
    public int init() {
        int ret = mDetector.init(mContext, DETECT_CONFIG, mResourceProvider.getLicensePath());
        if (!checkResult("initSkeleton", ret)) return ret;
        ret = mDetector.setModel(BytedEffectConstants.DynamicActionModelType.BEF_AI_DYNAMIC_ACTION_MODEL_SK, mResourceProvider.getModelPath(AlgorithmResourceHelper.SKELETON));
        if (!checkResult("initSkeleton", ret)) return ret;
        ret = mDetector.setModel(BytedEffectConstants.DynamicActionModelType.BEF_AI_DYNAMIC_ACTION_MODEL_DYNAMIC_ACTION, "");
        if (!checkResult("initSkeleton", ret)) return ret;
        ret = mDetector.setParam(BytedEffectConstants.DynamicActionParamType.BEF_AI_DYNAMIC_ACTION_MAX_PERSON_NUM, 1);
        if (!checkResult("initSkeleton", ret)) return ret;
        return ret;
    }

    @Override
    public ProcessOutput process(ProcessInput input) {
        LogTimerRecord.RECORD("dynamicAction");
        BefDynamicActionInfo dynamicActionInfo = mDetector.detectDynamicAction(input.buffer,
                input.pixelFormat, input.bufferSize.getWidth(), input.bufferSize.getHeight(),
                input.bufferStride, input.sensorRotation, DETECT_CONFIG, FRAME_COUNT);
        LogTimerRecord.STOP("dynamicAction");
        ProcessOutput output = super.process(input);
        output.dynamicActionInfo = dynamicActionInfo;
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

    @Override
    public TaskKey getKey() {
        return DYNAMIC_ACTION;
    }

    public static void register() {
        TaskFactory.register(DYNAMIC_ACTION, new TaskFactory.TaskGenerator<AlgorithmResourceProvider>() {
            @Override
            public Task<AlgorithmResourceProvider> create(Context context, AlgorithmResourceProvider provider) {
                return new DynamicActionAlgorithmTask(context, provider);
            }
        });
    }

    public interface DynamicActionInterface {

    }
}
