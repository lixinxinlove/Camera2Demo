package com.bytedance.labcv.demo.core.v4.algorithm.task;

import android.content.Context;
import android.util.Log;

import com.bytedance.labcv.demo.core.v4.algorithm.AlgorithmResourceHelper;
import com.bytedance.labcv.demo.core.v4.algorithm.AlgorithmTask;
import com.bytedance.labcv.demo.core.v4.base.ProcessInput;
import com.bytedance.labcv.demo.core.v4.base.ProcessOutput;
import com.bytedance.labcv.demo.core.v4.base.util.TaskFactory;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKey;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKeyFactory;
import com.bytedance.labcv.demo.utils.timer_record.LogTimerRecord;
import com.bytedance.labcv.effectsdk.BefSkyInfo;
import com.bytedance.labcv.effectsdk.BytedEffectConstants;
import com.bytedance.labcv.effectsdk.SkySegment;

import java.nio.ByteBuffer;

import static com.bytedance.labcv.effectsdk.BytedEffectConstants.BytedResultCode.BEF_RESULT_SUC;

public class SkySegmentAlgorithmTask extends AlgorithmTask {

    public static final TaskKey SKY_SEGMENT = TaskKeyFactory.create("skySegment", true);

    static {
        register();
    }

    public static final boolean FLIP_ALPHA = false;
    public static final boolean SYK_CHECK = true;
    private SkySegment mDetector;


    public SkySegmentAlgorithmTask(Context context, AlgorithmResourceProvider resourceProvider) {
        super(context, resourceProvider);

        mDetector = new SkySegment();
    }

    @Override
    public ProcessInput.Size preferBufferSize() {
        return new ProcessInput.Size(128, 224);
    }

    @Override
    public int init() {
        int ret = mDetector.init(mContext, mResourceProvider.getModelPath(AlgorithmResourceHelper.SKY_SEGMENT), mResourceProvider.getLicensePath());
        if (!checkResult("initSkySegment", ret)){
            return ret;
        }
        ret = mDetector.setParam(preferBufferSize().getWidth(), preferBufferSize().getHeight());
        if (!checkResult("SetSkySegmentParam", ret)){
            return ret;
        }
        return ret;
    }

    @Override
    public ProcessOutput process(ProcessInput input) {
        LogTimerRecord.RECORD("skySegment");
        BefSkyInfo skyInfo = mDetector.detectSky(input.buffer, input.pixelFormat,
                input.bufferSize.getWidth(), input.bufferSize.getHeight(), input.bufferStride,
                input.sensorRotation, FLIP_ALPHA, SYK_CHECK);
        LogTimerRecord.STOP("skySegment");
        ProcessOutput output = super.process(input);
        output.skyInfo = skyInfo;
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
        return SKY_SEGMENT;
    }



    public static void register() {
        TaskFactory.register(SKY_SEGMENT, new TaskFactory.TaskGenerator<AlgorithmResourceProvider>() {
            @Override
            public AlgorithmTask create(Context context, AlgorithmResourceProvider provider) {
                return new SkySegmentAlgorithmTask(context, provider);
            }
        });
    }

    public interface SkySegmentAlgorithmInterface {

    }

}
