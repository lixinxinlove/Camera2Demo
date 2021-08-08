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

/**
 * Created by QunZhang on 2020/8/3 18:21
 */
public class VideoTextureFormatTask extends TextureFormatTask {
    public static final TaskKey VIDEO_TEXTURE_FORMAT = TaskKeyFactory.create("videoTextureFormat", true);

    static {
        TaskFactory.register(VIDEO_TEXTURE_FORMAT, new TaskFactory.TaskGenerator() {
            @Override
            public Task create(Context context, ResourceProvider provider) {
                return new VideoTextureFormatTask();
            }
        });
    }

    @Override
    public ProcessOutput process(ProcessInput input) {
//        LogTimerRecord.RECORD("videoTextureFormat");
        boolean flipHorizontal = input.cameraRotation % 180 == 90;
        boolean flipVertical = !flipHorizontal;
        input.texture = mTextureFormatter.drawFrameOffScreen(input.texture, input.textureFormat,
                input.textureSize.getWidth(), input.textureSize.getHeight(),
                input.cameraRotation, flipHorizontal, flipVertical);
        input.textureFormat = BytedEffectConstants.TextureFormat.Texure2D;
//        LogTimerRecord.STOP("videoTextureFormat");
        return super.process(input);
    }

    @Override
    public TaskKey getKey() {
        return VIDEO_TEXTURE_FORMAT;
    }
}
