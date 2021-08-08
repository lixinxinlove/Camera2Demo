package com.bytedance.labcv.demo.core.v4.effect.task;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.bytedance.labcv.demo.core.v4.base.ProcessInput;
import com.bytedance.labcv.demo.core.v4.base.ProcessOutput;
import com.bytedance.labcv.demo.core.v4.base.Task;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKey;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKeyFactory;
import com.bytedance.labcv.demo.core.v4.effect.EffectInterface;
import com.bytedance.labcv.demo.utils.BitmapUtils;
import com.bytedance.labcv.demo.utils.timer_record.LogTimerRecord;
import com.bytedance.labcv.effectsdk.BytedEffectConstants;
import com.bytedance.labcv.effectsdk.library.LogUtils;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Created by QunZhang on 2020/8/3 14:36
 */
public abstract class EffectTask extends Task<EffectInterface.EffectResourceProvider> {
    public static final TaskKey EFFECT_INTERFACE = TaskKeyFactory.create("effectInterface");

    private EffectInterface mEffectInterface;

    @Override
    public int init() {
        return 0;
    }

    @Override
    public ProcessOutput process(ProcessInput input) {
        LogTimerRecord.RECORD("effectProcess");
        int dstTexture = mEffectInterface.prepareTexture(input.textureSize.getWidth(),
                input.textureSize.getHeight());
        if (!mEffectInterface.processTexture(input.texture, dstTexture, input.textureSize.getWidth(),
                input.textureSize.getHeight(), input.sensorRotation, input.timeStamp)) {
            mEffectInterface.copyTexture(input.texture, dstTexture,
                    input.textureSize.getWidth(), input.textureSize.getHeight());
        }
        LogTimerRecord.STOP("effectProcess");
        ProcessOutput output = super.process(input);
        output.texture = dstTexture;
        return output;
    }

    @Override
    public int destroy() {
        return 0;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    protected void setConfig(Map<TaskKey, Object> config) {
        super.setConfig(config);

        if (hasConfig(EFFECT_INTERFACE)) {
            mEffectInterface = (EffectInterface) config.get(EFFECT_INTERFACE);
        }
    }

    public interface EffectInterface {
        int prepareTexture(int width, int height);
        void copyTexture(int srcTexture, int dstTexture, int width, int height);
        boolean processTexture(int texture, int dstTexture, int width, int height, BytedEffectConstants.Rotation rotation, long timeStamp);
        ByteBuffer captureRenderResult(int textureId, int imageWidth, int imageHeight);
    }
}
