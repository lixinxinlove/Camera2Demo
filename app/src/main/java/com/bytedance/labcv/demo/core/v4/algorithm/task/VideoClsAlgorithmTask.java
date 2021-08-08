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
import com.bytedance.labcv.effectsdk.BefVideoClsInfo;
import com.bytedance.labcv.effectsdk.BytedEffectConstants;
import com.bytedance.labcv.effectsdk.VideoClsDetect;

/**
 * Created by QunZhang on 2020/8/17 17:48
 */
public class VideoClsAlgorithmTask extends AlgorithmTask {
    public static final TaskKey VIDEO_CLS = TaskKeyFactory.create("videoCls", true);
    public static final int FRAME_INTERVAL = 5;

    static {
        TaskFactory.register(VIDEO_CLS, new TaskFactory.TaskGenerator<AlgorithmResourceProvider>() {
            @Override
            public AlgorithmTask create(Context context, AlgorithmResourceProvider provider) {
                return new VideoClsAlgorithmTask(context, provider);
            }
        });
    }

    private VideoClsDetect mDetector;
    private long mFrameCount = 0;

    public VideoClsAlgorithmTask(Context context, AlgorithmResourceProvider resourceProvider) {
        super(context, resourceProvider);

        mDetector = new VideoClsDetect();
    }

    @Override
    public ProcessInput.Size preferBufferSize() {
        return null;
    }

    @Override
    public int init() {
        int ret = mDetector.init(BytedEffectConstants.VideoClsModelType.BEF_AI_kVideoClsModel1,
                mResourceProvider.getModelPath(AlgorithmResourceHelper.VIDEO_CLS),
                mResourceProvider.getLicensePath());
        if (!checkResult("initVideoCls", ret)) return ret;
        return ret;
    }

    @Override
    public ProcessOutput process(ProcessInput input) {
        LogTimerRecord.RECORD("videoCls");
        boolean isLast = (++mFrameCount % FRAME_INTERVAL) == 0;
        BefVideoClsInfo info = mDetector.detect(input.buffer, input.pixelFormat, input.bufferSize.getWidth(),
                input.bufferSize.getHeight(), input.bufferStride, isLast, input.sensorRotation);
        LogTimerRecord.STOP("videoCls");
        ProcessOutput output = super.process(input);
        if (isLast) {
            output.videoClsInfo = info;
        }
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
        return VIDEO_CLS;
    }

    public interface VideoClsAlgorithmInterface {

    }
}
