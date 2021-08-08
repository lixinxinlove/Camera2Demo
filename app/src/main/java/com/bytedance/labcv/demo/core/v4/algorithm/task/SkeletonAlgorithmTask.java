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
import com.bytedance.labcv.effectsdk.BefSkeletonInfo;
import com.bytedance.labcv.effectsdk.SkeletonDetect;

/**
 * Created by QunZhang on 2020/7/30 17:23
 */
public class SkeletonAlgorithmTask extends AlgorithmTask {
    public static final TaskKey SKELETON = TaskKeyFactory.create("skeleton", true);

    static {
        register();
    }

    private SkeletonDetect mDetector;

    public SkeletonAlgorithmTask(Context context, AlgorithmResourceProvider resourceProvider) {
        super(context, resourceProvider);

        mDetector = new SkeletonDetect();
    }

    @Override
    public TaskKey getKey() {
        return SKELETON;
    }

    @Override
    public ProcessInput.Size preferBufferSize() {
        return null;
    }

    @Override
    public int init() {
        int ret = mDetector.init(mContext, mResourceProvider.getModelPath(AlgorithmResourceHelper.SKELETON), mResourceProvider.getLicensePath());
        if (!checkResult("initSkeleton", ret)) return ret;
        return ret;
    }

    @Override
    public ProcessOutput process(ProcessInput input) {
        LogTimerRecord.RECORD("detectSkeleton");
        BefSkeletonInfo skeletonInfo = mDetector.detectSkeleton(input.buffer, input.pixelFormat,
                input.bufferSize.getWidth(), input.bufferSize.getHeight(), input.bufferStride,
                input.sensorRotation);
        LogTimerRecord.STOP("detectSkeleton");
        ProcessOutput output = super.process(input);
        output.skeletonInfo = skeletonInfo;
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
        TaskFactory.register(SKELETON, new TaskFactory.TaskGenerator<AlgorithmResourceProvider>() {
            @Override
            public AlgorithmTask create(Context context, AlgorithmResourceProvider provider) {
                return new SkeletonAlgorithmTask(context, provider);
            }
        });
    }

    public interface SkeletonAlgorithmInterface {

    }
}
