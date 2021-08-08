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
import com.bytedance.labcv.effectsdk.BefLightclsInfo;
import com.bytedance.labcv.effectsdk.LightClsDetect;

/**
 * Created by QunZhang on 2020/7/30 17:53
 */
public class LightClsAlgorithmTask extends AlgorithmTask {
    public static final TaskKey LIGHT_CLS = TaskKeyFactory.create("lightCls", true);

    static {
        register();
    }

    private static final int FPS = 5;

    private LightClsDetect mDetector;

    public LightClsAlgorithmTask(Context context, AlgorithmResourceProvider resourceProvider) {
        super(context, resourceProvider);

        mDetector = new LightClsDetect();
    }

    @Override
    public TaskKey getKey() {
        return LIGHT_CLS;
    }

    @Override
    public ProcessInput.Size preferBufferSize() {
        return null;
    }

    @Override
    public int init() {
        int ret = mDetector.init(mContext, mResourceProvider.getModelPath(AlgorithmResourceHelper.LIGHTCLS), mResourceProvider.getLicensePath(), FPS);
        if (!checkResult("initLightCls", ret)) return ret;
        return ret;
    }

    @Override
    public ProcessOutput process(ProcessInput input) {
        LogTimerRecord.RECORD("detectLight");
        BefLightclsInfo lightclsInfo = mDetector.detectLightCls(input.buffer, input.pixelFormat,
                input.bufferSize.getWidth(), input.bufferSize.getHeight(), input.bufferStride,
                input.sensorRotation);
        LogTimerRecord.STOP("detectLight");
        ProcessOutput output = super.process(input);
        output.lightclsInfo = lightclsInfo;
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
        TaskFactory.register(LIGHT_CLS, new TaskFactory.TaskGenerator<AlgorithmResourceProvider>() {
            @Override
            public AlgorithmTask create(Context context, AlgorithmResourceProvider provider) {
                return new LightClsAlgorithmTask(context, provider);
            }
        });
    }

    public interface LightClsAlgorithmInterface {

    }
}
