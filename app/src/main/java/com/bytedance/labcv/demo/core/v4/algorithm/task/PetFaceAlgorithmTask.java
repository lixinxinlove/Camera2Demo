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
import com.bytedance.labcv.effectsdk.BefPetFaceInfo;
import com.bytedance.labcv.effectsdk.PetFaceDetect;

import static com.bytedance.labcv.effectsdk.BytedEffectConstants.PetFaceDetectConfig.BEF_PET_FACE_DETECT_CAT;
import static com.bytedance.labcv.effectsdk.BytedEffectConstants.PetFaceDetectConfig.BEF_PET_FACE_DETECT_DOG;

/**
 * Created by QunZhang on 2020/7/30 17:16
 */
public class PetFaceAlgorithmTask extends AlgorithmTask {
    public static final TaskKey PET_FACE = TaskKeyFactory.create("petFace", true);

    static {
        register();
    }

    public static final int DETECT_CONFIG = BEF_PET_FACE_DETECT_CAT | BEF_PET_FACE_DETECT_DOG;

    private PetFaceDetect mDetector;

    public PetFaceAlgorithmTask(Context context, AlgorithmResourceProvider resourceProvider) {
        super(context, resourceProvider);
        mDetector = new PetFaceDetect();
    }

    @Override
    public TaskKey getKey() {
        return PET_FACE;
    }

    @Override
    public ProcessInput.Size preferBufferSize() {
        return null;
    }

    @Override
    public int init() {
        int ret = mDetector.init(mContext, mResourceProvider.getModelPath(AlgorithmResourceHelper.PETFACE), DETECT_CONFIG, mResourceProvider.getLicensePath());
        if (!checkResult("initPetFace", ret)) return ret;
        return ret;
    }

    @Override
    public ProcessOutput process(ProcessInput input) {
        LogTimerRecord.RECORD("petFace");
        BefPetFaceInfo petFaceInfo = mDetector.detectFace(input.buffer, input.pixelFormat,
                input.bufferSize.getWidth(), input.bufferSize.getHeight(), input.bufferStride,
                input.sensorRotation);
        LogTimerRecord.STOP("petFace");
        ProcessOutput output = super.process(input);
        output.petFaceInfo = petFaceInfo;
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
        TaskFactory.register(PET_FACE, new TaskFactory.TaskGenerator<AlgorithmResourceProvider>() {
            @Override
            public AlgorithmTask create(Context context, AlgorithmResourceProvider provider) {
                return new PetFaceAlgorithmTask(context, provider);
            }
        });
    }

    public interface PetFaceAlgorithmInterface {

    }
}
