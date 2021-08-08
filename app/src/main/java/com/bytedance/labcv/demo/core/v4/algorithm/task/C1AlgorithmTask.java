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
import com.bytedance.labcv.effectsdk.BefC1Info;
import com.bytedance.labcv.effectsdk.BytedEffectConstants;
import com.bytedance.labcv.effectsdk.C1Detect;

/**
 * Created by QunZhang on 2020/8/17 16:57
 */
public class C1AlgorithmTask extends AlgorithmTask {
    public static final TaskKey C1 = TaskKeyFactory.create("c1", true);

    static {
        TaskFactory.register(C1, new TaskFactory.TaskGenerator<AlgorithmResourceProvider>() {
            @Override
            public Task<AlgorithmResourceProvider> create(Context context, AlgorithmResourceProvider provider) {
                return new C1AlgorithmTask(context, provider);
            }
        });
    }

    private C1Detect mDetector;

    public C1AlgorithmTask(Context context, AlgorithmResourceProvider resourceProvider) {
        super(context, resourceProvider);

        mDetector = new C1Detect();
    }

    @Override
    public ProcessInput.Size preferBufferSize() {
        return null;
    }

    @Override
    public int init() {
        int ret = mDetector.init(BytedEffectConstants.C1ModelType.BEF_AI_C1_MODEL_SMALL,
                mResourceProvider.getModelPath(AlgorithmResourceHelper.C1), mResourceProvider.getLicensePath());
        if (!checkResult("initC1", ret)) return ret;
        ret = mDetector.setParam(BytedEffectConstants.C1ParamType.BEF_AI_C1_USE_MultiLabels, 1);
        if (!checkResult("initC1", ret)) return ret;
        return ret;
    }

    @Override
    public ProcessOutput process(ProcessInput input) {
        LogTimerRecord.RECORD("c1");
        BefC1Info c1Info = mDetector.detect(input.buffer, input.pixelFormat, input.bufferSize.getWidth(), input.bufferSize.getHeight(), input.bufferStride, input.sensorRotation);
        LogTimerRecord.STOP("c1");
        ProcessOutput output = super.process(input);
        output.c1Info = c1Info;
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
        return C1;
    }

    public interface C1AlgorithmInterface {

    }
}
