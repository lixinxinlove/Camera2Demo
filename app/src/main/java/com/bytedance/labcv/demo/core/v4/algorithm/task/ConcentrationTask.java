package com.bytedance.labcv.demo.core.v4.algorithm.task;

import android.content.Context;

import com.bytedance.labcv.demo.core.v4.algorithm.AlgorithmTask;
import com.bytedance.labcv.demo.core.v4.base.ProcessInput;
import com.bytedance.labcv.demo.core.v4.base.ProcessOutput;
import com.bytedance.labcv.demo.core.v4.base.Task;
import com.bytedance.labcv.demo.core.v4.base.util.TaskFactory;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKey;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKeyFactory;
import com.bytedance.labcv.effectsdk.BefFaceInfo;

import java.util.Collections;
import java.util.List;

/**
 * Created by QunZhang on 2020/8/31 12:17
 */
public class ConcentrationTask extends AlgorithmTask {
    public static final TaskKey CONCENTRATION = TaskKeyFactory.create("concentration", true);
    public static final int INTERVAL = 1000;
    public static final float MIN_YAW = -14;
    public static final float MAX_YAW = 7;
    public static final float MIN_PITCH = -12;
    public static final float MAX_PITCH = 12;

    static {
        TaskFactory.register(CONCENTRATION, new TaskFactory.TaskGenerator<AlgorithmResourceProvider>() {
            @Override
            public Task<AlgorithmResourceProvider> create(Context context, AlgorithmResourceProvider provider) {
                return new ConcentrationTask(context, provider);
            }
        });
    }

    private int mTotalCount;
    private int mConcentrationCount;
    private long mLastProcess;

    public ConcentrationTask(Context context, AlgorithmResourceProvider resourceProvider) {
        super(context, resourceProvider);
    }

    @Override
    public int init() {
        mTotalCount = 0;
        mConcentrationCount = 0;
        return 0;
    }

    @Override
    public ProcessOutput process(ProcessInput input) {
        BefFaceInfo faceInfo = input.faceInfo;
        if ((System.currentTimeMillis() - mLastProcess) < INTERVAL) {
            return super.process(input);
        }
        mLastProcess = System.currentTimeMillis();
        if (faceInfo != null && faceInfo.getFace106s().length > 0) {
            BefFaceInfo.Face106 face106 = faceInfo.getFace106s()[0];
            boolean available = face106.getYaw() <= MAX_YAW && face106.getYaw() >= MIN_YAW && face106.getPitch() <= MAX_PITCH && face106.getPitch() >= MIN_PITCH;
            mTotalCount += 1;
            if (available) {
                mConcentrationCount += 1;
            }
        } else {
            mConcentrationCount = 0;
            mTotalCount = 0;
        }
        ProcessOutput output = super.process(input);
        output.concentrationInfo = new BefConcentrationInfo(mTotalCount, mConcentrationCount);
        return output;
    }

    @Override
    public List<TaskKey> getDependency() {
        return Collections.singletonList(FaceAlgorithmTask.FACE_106);
    }

    @Override
    public int destroy() {
        mTotalCount = 0;
        mConcentrationCount = 0;
        return 0;
    }

    @Override
    public int getPriority() {
        return 500;
    }

    @Override
    public TaskKey getKey() {
        return CONCENTRATION;
    }

    public static class BefConcentrationInfo {
        public int total;
        public int concentration;

        public BefConcentrationInfo(int total, int concentration) {
            this.total = total;
            this.concentration = concentration;
        }
    }
}
