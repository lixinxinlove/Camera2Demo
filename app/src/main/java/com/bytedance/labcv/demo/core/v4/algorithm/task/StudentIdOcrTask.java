//
// Created by luofei on 2020-09-06.
//

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
import com.bytedance.labcv.effectsdk.BytedEffectConstants;
import com.bytedance.labcv.effectsdk.BefStudentIdOcrInfo;
import com.bytedance.labcv.effectsdk.StudentIdOcr;

import java.nio.ByteBuffer;

public class StudentIdOcrTask extends AlgorithmTask {
    public static final TaskKey STUDENT_ID_OCR = TaskKeyFactory.create("student_id_ocr", true);

    static {
        TaskFactory.register(STUDENT_ID_OCR, new TaskFactory.TaskGenerator<AlgorithmResourceProvider>() {
            @Override
            public Task<AlgorithmResourceProvider> create(Context context, AlgorithmResourceProvider provider) {
                return new StudentIdOcrTask(context, provider);
            }
        });
    }

    private StudentIdOcr mDetector;
    private float mFov = 60;

    public StudentIdOcrTask(Context context, AlgorithmResourceProvider resourceProvider) {
        super(context, resourceProvider);
        mDetector = new StudentIdOcr();
    }

    @Override
    public int init() {
        int ret = mDetector.init(mResourceProvider.getLicensePath());
        if (!checkResult("init student_id_ocr", ret)) return ret;
        ret = mDetector.setModel(BytedEffectConstants.StudentIdOcrModelType.BEF_STUDENT_ID_OCR_MODEL, mResourceProvider.getModelPath(AlgorithmResourceHelper.STUDENT_ID_OCR));
        if (!checkResult("init student_id_ocr", ret)) return ret;
        return ret;
    }

    @Override
    public ProcessOutput process(ProcessInput input) {
        LogTimerRecord.RECORD("student_id_ocr");
        BefStudentIdOcrInfo info = mDetector.detect(input.buffer, input.pixelFormat, input.bufferSize.getWidth(), input.bufferSize.getHeight(), input.bufferStride, input.sensorRotation);
        LogTimerRecord.STOP("student_id_ocr");
        ProcessOutput output = super.process(input);
        output.studentIdOcrInfo = info;
        return output;
    }

    @Override
    public int destroy() {
        return mDetector.release();
    }

    @Override
    public TaskKey getKey() {
        return STUDENT_ID_OCR;
    }

    @Override
    public int getPriority() {
        return 1111;
    }


    public BefStudentIdOcrInfo studentIdOcrDetect(ByteBuffer buffer,
                                                  BytedEffectConstants.PixlFormat pixelFormat,
                                                  int width,
                                                  int height,
                                                  int stride,
                                                  BytedEffectConstants.Rotation rotation) {
        return mDetector.detect(buffer,
                pixelFormat,
                width,
                height,
                stride,
                rotation);
    }

}
