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
import com.bytedance.labcv.effectsdk.BefFaceInfo;
import com.bytedance.labcv.effectsdk.BefHeadSegInfo;
import com.bytedance.labcv.effectsdk.HeadSegment;

import java.util.Collections;
import java.util.List;

import static com.bytedance.labcv.demo.core.v4.algorithm.task.FaceAlgorithmTask.FACE_106;

/**
 * Created by QunZhang on 2020/7/28 14:10
 */
public class HeadSegmentAlgorithmTask extends AlgorithmTask {
    public static final TaskKey HEAD_SEGMENT = TaskKeyFactory.create("headSeg", true);

    static {
        register();
    }

    private HeadSegment mDetector;

    public HeadSegmentAlgorithmTask(Context context, AlgorithmResourceProvider resourceProvider) {
        super(context, resourceProvider);
        mDetector = new HeadSegment();
    }

    @Override
    public ProcessOutput process(ProcessInput input) {
        BefFaceInfo faceInfo = (BefFaceInfo) input.faceInfo;
        if (faceInfo == null) {
            return super.process(input);
        }
        LogTimerRecord.RECORD("headSegment");
        BefHeadSegInfo headSegInfo = mDetector.process(input.buffer, input.pixelFormat, input.bufferSize.getWidth(), input.bufferSize.getHeight(), input.bufferStride, input.sensorRotation, faceInfo.getFace106s());
        LogTimerRecord.STOP("headSegment");
        ProcessOutput output = super.process(input);
        if (hasUserSettingConfig(HEAD_SEGMENT)) {
            output.headSegInfo = headSegInfo;
        }
        return output;
    }

    @Override
    public TaskKey getKey() {
        return HEAD_SEGMENT;
    }

    @Override
    public List<TaskKey> getDependency() {
        return Collections.singletonList(FACE_106);
    }

    @Override
    public ProcessInput.Size preferBufferSize() {
        return null;
    }

    @Override
    public int init() {
        int ret = mDetector.init(mContext, mResourceProvider.getModelPath(AlgorithmResourceHelper.HEADSEGMENT), mResourceProvider.getLicensePath());
        if (!checkResult("initHeadSegment", ret)) return ret;
        return ret;
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
        TaskFactory.register(HEAD_SEGMENT, new TaskFactory.TaskGenerator<AlgorithmResourceProvider>() {

            @Override
            public AlgorithmTask create(Context context, AlgorithmResourceProvider provider) {
                return new HeadSegmentAlgorithmTask(context, provider);
            }
        });
    }

    public interface HeadSegmentAlgorithmTaskInterface {

    }
}
