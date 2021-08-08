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
import com.bytedance.labcv.effectsdk.BytedEffectConstants;
import com.bytedance.labcv.effectsdk.FaceDetect;

import java.util.Map;

import static com.bytedance.labcv.effectsdk.BytedEffectConstants.BEF_DETECT_SMALL_MODEL;
import static com.bytedance.labcv.effectsdk.BytedEffectConstants.FaceAction.BEF_DETECT_FULL;
import static com.bytedance.labcv.effectsdk.BytedEffectConstants.FaceAttribute.BEF_FACE_ATTRIBUTE_AGE;
import static com.bytedance.labcv.effectsdk.BytedEffectConstants.FaceAttribute.BEF_FACE_ATTRIBUTE_ATTRACTIVE;
import static com.bytedance.labcv.effectsdk.BytedEffectConstants.FaceAttribute.BEF_FACE_ATTRIBUTE_CONFUSE;
import static com.bytedance.labcv.effectsdk.BytedEffectConstants.FaceAttribute.BEF_FACE_ATTRIBUTE_EXPRESSION;
import static com.bytedance.labcv.effectsdk.BytedEffectConstants.FaceAttribute.BEF_FACE_ATTRIBUTE_GENDER;
import static com.bytedance.labcv.effectsdk.BytedEffectConstants.FaceAttribute.BEF_FACE_ATTRIBUTE_HAPPINESS;
import static com.bytedance.labcv.effectsdk.BytedEffectConstants.FaceExtraModel.BEF_MOBILE_FACE_240_DETECT;
import static com.bytedance.labcv.effectsdk.BytedEffectConstants.FaceExtraModel.BEF_MOBILE_FACE_280_DETECT;

import static com.bytedance.labcv.effectsdk.BytedEffectConstants.FaceSegmentConfig.BEF_MOBILE_FACE_MOUTH_MASK;
import static com.bytedance.labcv.effectsdk.BytedEffectConstants.FaceSegmentConfig.BEF_MOBILE_FACE_TEETH_MASK;
import static com.bytedance.labcv.effectsdk.BytedEffectConstants.FaceSegmentConfig.BEFF_MOBILE_FACE_REST_MASK;

import static com.bytedance.labcv.effectsdk.BytedEffectConstants.FaceSegmentType.BEF_FACE_MOUTH_MASK;
import static com.bytedance.labcv.effectsdk.BytedEffectConstants.FaceSegmentType.BEF_FACE_TEETH_MASK;
import static com.bytedance.labcv.effectsdk.BytedEffectConstants.FaceSegmentType.BEF_FACE_FACE_MASK;

/**
 * Created by QunZhang on 2020/7/15 11:05
 */
public class FaceAlgorithmTask extends AlgorithmTask {
    public static final TaskKey FACE_106 = TaskKeyFactory.create("face", true);
    public static final TaskKey FACE_280 = TaskKeyFactory.create("face280");
    public static final TaskKey FACE_ATTR = TaskKeyFactory.create("faceAttr");
    public static final TaskKey FACE_MASK = TaskKeyFactory.create("faceMask");
    public static final TaskKey MOUTH_MASK = TaskKeyFactory.create("mouthMask");
    public static final TaskKey TEETH_MASK = TaskKeyFactory.create("teethMouth");

    static {
        register();
    }

    private FaceDetect mDetector;

    public FaceAlgorithmTask(Context context, AlgorithmResourceProvider resourceProvider) {
        super(context, resourceProvider);
        mDetector = new FaceDetect();
    }

    @Override
    public TaskKey getKey() {
        return FACE_106;
    }

    @Override
    public ProcessInput.Size preferBufferSize() {
        if (hasConfig(FACE_ATTR) || hasConfig(FACE_280) || hasConfig(FACE_MASK)) {
            return new ProcessInput.Size(360, 640);
        } else {
            return new ProcessInput.Size(128, 224);
        }
    }

    @Override
    public int init() {
        // TODO: initialization can be moved to setDetectConfig for memory testing
        int ret = mDetector.init(mContext, mResourceProvider.getModelPath(AlgorithmResourceHelper.FACE), BEF_DETECT_SMALL_MODEL | BEF_DETECT_FULL, mResourceProvider.getLicensePath());
        if (!checkResult("initFace", ret)) return ret;
        ret = mDetector.initExtra(mContext, mResourceProvider.getModelPath(AlgorithmResourceHelper.FACEEXTA), BEF_MOBILE_FACE_280_DETECT);
        if (!checkResult("initFaceExtra", ret)) return ret;
        ret = mDetector.initAttri(mContext, mResourceProvider.getModelPath(AlgorithmResourceHelper.FACEATTRI), mResourceProvider.getLicensePath());
        if (!checkResult("initFaceAttr", ret)) return ret;
        return ret;
    }

    @Override
    public ProcessOutput process(ProcessInput input) {
        LogTimerRecord.RECORD("detectFace");
        BefFaceInfo faceInfo = mDetector.detectFace(input.buffer, input.pixelFormat, input.bufferSize.getWidth(), input.bufferSize.getHeight(), input.bufferStride, input.sensorRotation);
        LogTimerRecord.STOP("detectFace");

        input.faceInfo = faceInfo;
        ProcessOutput output = super.process(input);
        // only result not being depended on can be moved to output
        if (hasUserSettingConfig(FACE_106)) {
            output.faceInfo = faceInfo;

            if (hasUserSettingConfig(FACE_MASK)) {
                mDetector.getFaceMask(output.faceInfo, BEF_FACE_FACE_MASK);
            }

            if (hasUserSettingConfig(MOUTH_MASK)) {
                mDetector.getFaceMask(output.faceInfo, BEF_FACE_MOUTH_MASK);
            }

            if (hasUserSettingConfig(TEETH_MASK)) {
                mDetector.getFaceMask(output.faceInfo, BEF_FACE_TEETH_MASK);
            }

        }

        return output;
    }

    @Override
    public int destroy() {
        mDetector.release();
        mDetector = null;
        mContext = null;
        return 0;
    }

    @Override
    public int getPriority() {
        return 1000;
    }

    @Override
    protected void setConfig(Map<TaskKey, Object> config) {
        super.setConfig(config);

        // refresh algorithm detection config with config and dependencyConfig
        int detectConfig = (BytedEffectConstants.FaceAction.BEF_FACE_DETECT | BytedEffectConstants.DetectMode.BEF_DETECT_MODE_VIDEO | BytedEffectConstants.FaceAction.BEF_DETECT_FULL);
        if (hasConfig(FACE_280)) {
            detectConfig |= BEF_MOBILE_FACE_280_DETECT;
        }

        if (hasConfig(FACE_MASK)) {
            detectConfig |= BEF_MOBILE_FACE_240_DETECT | BEFF_MOBILE_FACE_REST_MASK;
        }
        if (hasConfig(MOUTH_MASK)) {
            detectConfig |= BEF_MOBILE_FACE_240_DETECT | BEF_MOBILE_FACE_MOUTH_MASK;
        }
        if (hasConfig(TEETH_MASK)) {
            detectConfig |= BEF_MOBILE_FACE_240_DETECT | BEF_MOBILE_FACE_TEETH_MASK;
        }

        mDetector.setFaceDetectConfig(detectConfig);


        int attrConfig = 0;
        if (hasConfig(FACE_ATTR)) {
            attrConfig |= (BEF_FACE_ATTRIBUTE_EXPRESSION | BEF_FACE_ATTRIBUTE_HAPPINESS |
                    BEF_FACE_ATTRIBUTE_AGE | BEF_FACE_ATTRIBUTE_GENDER | BEF_FACE_ATTRIBUTE_ATTRACTIVE |
                    BEF_FACE_ATTRIBUTE_CONFUSE);
        }
        mDetector.setAttriDetectConfig(attrConfig);
    }

    public static void register() {
        TaskFactory.register(FACE_106, new TaskFactory.TaskGenerator<AlgorithmResourceProvider>() {
            @Override
            public AlgorithmTask create(Context context, AlgorithmResourceProvider provider) {
                return new FaceAlgorithmTask(context, provider);
            }
        });
    }

    public interface FaceAlgorithmInterface {

    }
}
