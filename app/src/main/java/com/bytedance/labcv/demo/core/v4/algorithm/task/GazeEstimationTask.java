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
import com.bytedance.labcv.effectsdk.BefFaceInfo;
import com.bytedance.labcv.effectsdk.BefGazeEstimationInfo;
import com.bytedance.labcv.effectsdk.BytedEffectConstants;
import com.bytedance.labcv.effectsdk.GazeEstimation;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by QunZhang on 2020/8/31 18:15
 */
public class GazeEstimationTask extends AlgorithmTask {
    public static final TaskKey GAZE_ESTIMATION = TaskKeyFactory.create("gaze estimation", true);
    public static final int LINE_LEN = 0;

    static {
        TaskFactory.register(GAZE_ESTIMATION, new TaskFactory.TaskGenerator<AlgorithmResourceProvider>() {
            @Override
            public Task<AlgorithmResourceProvider> create(Context context, AlgorithmResourceProvider provider) {
                return new GazeEstimationTask(context, provider);
            }
        });
    }

    private GazeEstimation mDetector;
    private float mFov = 60;

    public GazeEstimationTask(Context context, AlgorithmResourceProvider resourceProvider) {
        super(context, resourceProvider);

        mDetector = new GazeEstimation();
    }

    @Override
    public int init() {
        int ret = mDetector.init(mResourceProvider.getLicensePath());
        if (!checkResult("init gaze", ret)) return ret;
        ret = mDetector.setModel(BytedEffectConstants.GazeEstimationModelType.BEF_GAZE_ESTIMATION_MODEL1, mResourceProvider.getModelPath(AlgorithmResourceHelper.GAZE_ESTIMAION));
        if (!checkResult("init gaze", ret)) return ret;
        return ret;
    }

    @Override
    public ProcessOutput process(ProcessInput input) {
        BefFaceInfo faceInfo = input.faceInfo;
        BefGazeEstimationInfo info = new BefGazeEstimationInfo();
        if (faceInfo != null && faceInfo.getFace106s().length > 0) {
            mDetector.setParam(BytedEffectConstants.GazeEstimationParamType.BEF_GAZE_ESTIMATION_CAMERA_FOV, mFov);
            LogTimerRecord.RECORD("gazeEstimation");
            info = mDetector.detect(input.buffer, input.pixelFormat, input.bufferSize.getWidth(), input.bufferSize.getHeight(), input.bufferStride, input.sensorRotation, faceInfo, LINE_LEN);
            LogTimerRecord.STOP("gazeEstimation");
        }
        ProcessOutput output = super.process(input);
        output.gazeEstimationInfo = info;
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
        return GAZE_ESTIMATION;
    }

    @Override
    protected void setConfig(Map<TaskKey, Object> config) {
        super.setConfig(config);

        mFov = getFloatConfig(ALGORITHM_FOV, mFov);
    }

    @Override
    public List<TaskKey> getDependency() {
        return Collections.singletonList(FaceAlgorithmTask.FACE_106);
    }
}
