package com.bytedance.labcv.demo.core.v4.algorithm.task;

import android.content.Context;

import com.bytedance.labcv.demo.core.v4.algorithm.AlgorithmResourceHelper;
import com.bytedance.labcv.demo.core.v4.algorithm.AlgorithmTask;
import com.bytedance.labcv.demo.core.v4.base.ProcessInput;
import com.bytedance.labcv.demo.core.v4.base.ProcessOutput;
import com.bytedance.labcv.demo.core.v4.base.util.TaskFactory;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKey;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKeyFactory;
import com.bytedance.labcv.demo.utils.timer_record.LogTimerRecord;
import com.bytedance.labcv.effectsdk.BefC2Info;
import com.bytedance.labcv.effectsdk.BytedEffectConstants;
import com.bytedance.labcv.effectsdk.C2Detect;

/**
 * Created by QunZhang on 2020/8/17 17:44
 */
public class C2AlgorithmTask extends AlgorithmTask {
    public static final TaskKey C2 = TaskKeyFactory.create("c2", true);

    static {
        TaskFactory.register(C2, new TaskFactory.TaskGenerator<AlgorithmResourceProvider>() {
            @Override
            public AlgorithmTask create(Context context, AlgorithmResourceProvider provider) {
                return new C2AlgorithmTask(context, provider);
            }
        });
    }

    private C2Detect mDetector;

    public C2AlgorithmTask(Context context, AlgorithmResourceProvider resourceProvider) {
        super(context, resourceProvider);

        mDetector = new C2Detect();
    }

    @Override
    public ProcessInput.Size preferBufferSize() {
        return null;
    }

    @Override
    public int init() {
        int ret = mDetector.init(BytedEffectConstants.C2ModelType.BEF_AI_kC2Model1,
                mResourceProvider.getModelPath(AlgorithmResourceHelper.C2),
                mResourceProvider.getLicensePath());
        if (!checkResult("initC2", ret)) return ret;
        ret = mDetector.setParam(BytedEffectConstants.C2ParamType.BEF_AI_C2_USE_VIDEO_MODE, 1);
        if (!checkResult("initC2", ret)) return ret;
        ret = mDetector.setParam(BytedEffectConstants.C2ParamType.BEF_AI_C2_USE_MultiLabels, 1);
        if (!checkResult("initC2", ret)) return ret;
        return ret;
    }

    @Override
    public ProcessOutput process(ProcessInput input) {
        LogTimerRecord.RECORD("c2");
        BefC2Info info = mDetector.detect(input.buffer, input.pixelFormat, input.bufferSize.getWidth(),
                input.bufferSize.getHeight(), input.bufferStride, input.sensorRotation);
        LogTimerRecord.STOP("c2");
        ProcessOutput output = super.process(input);
        output.c2Info = info;
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
        return C2;
    }

    public interface C2AlgorithmInterface {

    }
}
