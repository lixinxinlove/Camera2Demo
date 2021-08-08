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
import com.bytedance.labcv.effectsdk.BytedEffectConstants;
import com.bytedance.labcv.effectsdk.PortraitMatting;

import static com.bytedance.labcv.effectsdk.BytedEffectConstants.PortraitMatting.BEF_PORTAITMATTING_SMALL_MODEL;

/**
 * Created by QunZhang on 2020/7/30 17:29
 */
public class PortraitMattingAlgorithmTask extends AlgorithmTask {
    public static final TaskKey PORTRAIT_MATTING = TaskKeyFactory.create("portraitMatting", true);

    static {
        register();
    }

    public static final boolean FLIP_ALPHA = false;

    private PortraitMatting mDetector;

    public PortraitMattingAlgorithmTask(Context context, AlgorithmResourceProvider resourceProvider) {
        super(context, resourceProvider);

        mDetector = new PortraitMatting();
    }

    @Override
    public TaskKey getKey() {
        return PORTRAIT_MATTING;
    }

    @Override
    public ProcessInput.Size preferBufferSize() {
        return null;
    }

    @Override
    public int init() {
        int ret = mDetector.init(mContext, mResourceProvider.getModelPath(AlgorithmResourceHelper.PORTRAITMATTING), BEF_PORTAITMATTING_SMALL_MODEL, mResourceProvider.getLicensePath());
        if (!checkResult("initPortraitMatting", ret)) return ret;
        return ret;
    }

    @Override
    public ProcessOutput process(ProcessInput input) {
        LogTimerRecord.RECORD("detectMatting");
        PortraitMatting.MattingMask mattingMask = mDetector.detectMatting(input.buffer, input.pixelFormat,
                input.bufferSize.getWidth(), input.bufferSize.getHeight(), input.bufferStride,
                input.sensorRotation, FLIP_ALPHA);
        LogTimerRecord.STOP("detectMatting");
        ProcessOutput output = super.process(input);
        output.portraitMatting = mattingMask;
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
        TaskFactory.register(PORTRAIT_MATTING, new TaskFactory.TaskGenerator<AlgorithmResourceProvider>() {
            @Override
            public AlgorithmTask create(Context context, AlgorithmResourceProvider provider) {
                return new PortraitMattingAlgorithmTask(context, provider);
            }
        });
    }

    public interface PortraitMattingAlgorithmInterface {

    }
}
