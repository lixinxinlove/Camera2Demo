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
import com.bytedance.labcv.effectsdk.AnimojiDetect;
import com.bytedance.labcv.effectsdk.BefAnimojiInfo;

import java.util.Collections;
import java.util.List;

/**
 * Created by QunZhang on 2020/11/16 15:29
 */
public class AnimojiAlgorithmTask extends AlgorithmTask {
    public static final TaskKey ANIMOJI = TaskKeyFactory.create("animoji", true);
    public static final int INPUT_WIDTH = 256;
    public static final int INPUT_HEIGHT = 256;

    static {
        TaskFactory.register(ANIMOJI, new TaskFactory.TaskGenerator<AlgorithmResourceProvider>() {
            @Override
            public Task<AlgorithmResourceProvider> create(Context context, AlgorithmResourceProvider provider) {
                return new AnimojiAlgorithmTask(context, provider);
            }
        });
    }

    private AnimojiDetect mDetector;

    public AnimojiAlgorithmTask(Context context, AlgorithmResourceProvider resourceProvider) {
        super(context, resourceProvider);

        mDetector = new AnimojiDetect();
    }

    @Override
    public int init() {
        int ret = mDetector.init(mResourceProvider.getLicensePath());
        if (!checkResult("init animoji", ret)) return ret;
        ret = mDetector.setModel(mResourceProvider.getModelPath(AlgorithmResourceHelper.ANIMOJI_MODEL), INPUT_WIDTH, INPUT_HEIGHT);
        if (!checkResult("init animoji set model", ret)) return ret;
        return ret;
    }

    @Override
    public ProcessOutput process(ProcessInput input) {
        BefAnimojiInfo info;

        LogTimerRecord.RECORD("animoji");
        info = mDetector.detect(input.buffer, input.pixelFormat, input.bufferSize.getWidth(), input.bufferSize.getHeight(), input.bufferStride, input.sensorRotation);
        LogTimerRecord.STOP("animoji");

        ProcessOutput output = super.process(input);
        output.animojiInfo = info;
        return output;
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

    @Override
    public TaskKey getKey() {
        return ANIMOJI;
    }
}
