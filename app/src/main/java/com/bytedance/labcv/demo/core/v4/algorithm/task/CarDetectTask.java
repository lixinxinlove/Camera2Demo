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
import com.bytedance.labcv.effectsdk.BefCarDetectInfo;
import com.bytedance.labcv.effectsdk.BytedEffectConstants;
import com.bytedance.labcv.effectsdk.CarDetect;

import java.util.Map;

public class CarDetectTask extends AlgorithmTask {
    public static final TaskKey CAR_DETECT = TaskKeyFactory.create("car", true);
    public static final TaskKey CAR_RECOG = TaskKeyFactory.create("carDetect");
    public static final TaskKey BRAND_RECOG = TaskKeyFactory.create("carBrand");
    private CarDetect mDetector;

    public static final double GREY_THREHOLD = 40.0;
    public static final double BLUR_THREHOLD = 5.0;



    static {
        register();
    }

    public CarDetectTask(Context context, AlgorithmResourceProvider resourceProvider) {
        super(context, resourceProvider);

        mDetector = new CarDetect();
    }

    @Override
    public int init() {
        int ret = mDetector.createHandle(mResourceProvider.getLicensePath());
        if (!checkResult("initCarDetect", ret)) return ret;
        ret = mDetector.setModel(BytedEffectConstants.CarModelType.DetectModel, mResourceProvider.getModelPath(AlgorithmResourceHelper.CAR_DETECT));
        if (!checkResult("set DetectModel ", ret)) return ret;
        ret = mDetector.setModel(BytedEffectConstants.CarModelType.BrandNodel, mResourceProvider.getModelPath(AlgorithmResourceHelper.CAR_BRAND_DETECT));
        if (!checkResult("set BrandNodel ", ret)) return ret;
        ret = mDetector.setModel(BytedEffectConstants.CarModelType.OCRModel, mResourceProvider.getModelPath(AlgorithmResourceHelper.CAR_BRAND_OCR));
        if (!checkResult("set OCRModel ", ret)) return ret;
        ret = mDetector.setModel(BytedEffectConstants.CarModelType.TrackModel, mResourceProvider.getModelPath(AlgorithmResourceHelper.CAR_TRACK));
        if (!checkResult("set TrackModel ", ret)) return ret;
        return ret;
    }

    @Override
    public ProcessOutput process(ProcessInput input) {
        LogTimerRecord.RECORD("detectCar");
        BefCarDetectInfo carDetectInfo = mDetector.detect(input.buffer, input.pixelFormat,
                input.bufferSize.getWidth(), input.bufferSize.getHeight(), input.bufferStride,
                input.sensorRotation);
        LogTimerRecord.STOP("detectCar");
        ProcessOutput output = super.process(input);
        output.carDetectInfo = carDetectInfo;
        return output;
    }


    @Override
    protected void setConfig(Map<TaskKey, Object> config) {
        super.setConfig(config);
         mDetector.setParam(BytedEffectConstants.CarParamType.BEF_Car_Detect, hasConfig(CAR_RECOG)?1f:-1f);
         mDetector.setParam(BytedEffectConstants.CarParamType.BEF_Brand_Rec,hasConfig(BRAND_RECOG)?1f:-1f);



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
        return CAR_DETECT;
    }

    public static void register() {
        TaskFactory.register(CAR_DETECT, new TaskFactory.TaskGenerator<AlgorithmResourceProvider>() {
            @Override
            public AlgorithmTask create(Context context, AlgorithmResourceProvider provider) {
                return new CarDetectTask(context, provider);
            }
        });
    }
}
