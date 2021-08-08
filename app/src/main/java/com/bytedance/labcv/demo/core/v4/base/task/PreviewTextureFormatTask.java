package com.bytedance.labcv.demo.core.v4.base.task;

import android.content.Context;

import com.bytedance.labcv.demo.core.v4.base.ProcessInput;
import com.bytedance.labcv.demo.core.v4.base.ProcessOutput;
import com.bytedance.labcv.demo.core.v4.base.ResourceProvider;
import com.bytedance.labcv.demo.core.v4.base.Task;
import com.bytedance.labcv.demo.core.v4.base.util.TaskFactory;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKey;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKeyFactory;
import com.bytedance.labcv.demo.utils.timer_record.LogTimerRecord;
import com.bytedance.labcv.effectsdk.BytedEffectConstants;
import com.bytedance.labcv.effectsdk.library.LogUtils;

/**
 * Created by QunZhang on 2020/8/3 18:19
 */
public class PreviewTextureFormatTask extends TextureFormatTask {
    public static final TaskKey PREVIEW_TEXTURE_FORMAT = TaskKeyFactory.create("previewTextureFormat", true);

    static {
        TaskFactory.register(PREVIEW_TEXTURE_FORMAT, new TaskFactory.TaskGenerator() {
            @Override
            public Task create(Context context, ResourceProvider provider) {
                return new PreviewTextureFormatTask();
            }
        });
    }

    @Override
    public ProcessOutput process(ProcessInput input) {
        if (mTextureFormatter == null) {
            LogUtils.e(getTag() + " texture formatter is null");
            return super.process(input);
        }

//        LogTimerRecord.RECORD("pre previewTextureFormat");
        ProcessInput.Size textureSize = input.textureSize;
        if (input.cameraRotation % 180 == 90) {
            textureSize.revert();
        }
        input.texture = mTextureFormatter.drawFrameOffScreen(input.texture, input.textureFormat,
                textureSize.getWidth(), textureSize.getHeight(), -input.cameraRotation,
                input.frontCamera, true);
        input.textureFormat = BytedEffectConstants.TextureFormat.Texure2D;
//        LogTimerRecord.STOP("pre previewTextureFormat");

        ProcessOutput output = super.process(input);

//        LogTimerRecord.RECORD("post previewTextureFormat");
        if (input.cameraRotation % 180 == 90) {
            textureSize.revert();
        }
        output.texture = mTextureFormatter.drawFrameOffScreen(output.texture, BytedEffectConstants.TextureFormat.Texure2D,
                textureSize.getWidth(), textureSize.getHeight(),
                input.frontCamera ? -input.cameraRotation : input.cameraRotation, input.frontCamera, true);
//        LogTimerRecord.STOP("post previewTextureFormat");
        return output;
    }

    @Override
    public TaskKey getKey() {
        return PREVIEW_TEXTURE_FORMAT;
    }
}
