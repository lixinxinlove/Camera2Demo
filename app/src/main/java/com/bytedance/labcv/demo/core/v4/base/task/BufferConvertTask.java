package com.bytedance.labcv.demo.core.v4.base.task;

import android.content.Context;

import com.bytedance.labcv.demo.core.v4.base.BufferConvert;
import com.bytedance.labcv.demo.core.v4.base.util.TaskFactory;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKey;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKeyFactory;
import com.bytedance.labcv.demo.core.v4.base.ProcessInput;
import com.bytedance.labcv.demo.core.v4.base.ProcessOutput;
import com.bytedance.labcv.demo.core.v4.base.ResourceProvider;
import com.bytedance.labcv.demo.core.v4.base.Task;
import com.bytedance.labcv.demo.utils.timer_record.LogTimerRecord;
import com.bytedance.labcv.effectsdk.BytedEffectConstants;
import com.bytedance.labcv.effectsdk.library.LogUtils;

import java.util.Map;

/**
 * Created by QunZhang on 2020/8/3 10:55
 */
public class BufferConvertTask extends Task<ResourceProvider> {
    public static final TaskKey BUFFER_CONVERT_TASK = TaskKeyFactory.create("bufferConvert", true);
    public static final TaskKey BUFFER_PREFER_SIZE = TaskKeyFactory.create("bufferPreferSize");
    public static final TaskKey BUFFER_CONVERT = TaskKeyFactory.create("bufferConverter");

    static {
        register();
    }

    private ProcessInput.Size mPreferSize;
    private BufferConvert mBufferConvert;

    public BufferConvertTask(Context context, ResourceProvider resourceProvider) {
        super(context, resourceProvider);
    }

    @Override
    public int init() {
        return 0;
    }

    @Override
    public int destroy() {
        return 0;
    }

    @Override
    public int getPriority() {
        return 10000;
    }

    @Override
    public ProcessOutput process(ProcessInput input) {
        if (mBufferConvert == null) {
            LogUtils.e(getTag() + " buffer converter is null");
            return super.process(input);
        }

        LogTimerRecord.RECORD("bufferConvert");
        ProcessInput.Size preferSize = mPreferSize;
        if (input.buffer == null || input.bufferSize == null) {
            if (preferSize == null) {
                preferSize = input.textureSize;
            }
            ProcessInput.Size inputSize = input.textureSize;
            float ratio = Math.max((float)preferSize.getWidth() / inputSize.getWidth(), (float)preferSize.getHeight() / inputSize.getHeight());
            mBufferConvert.setResizeRatio(ratio);
            input.buffer = mBufferConvert.getResizeOutputTextureBuffer(input.texture);
            input.bufferSize = new ProcessInput.Size((int)(inputSize.getWidth() * ratio), (int)(inputSize.getHeight() * ratio));
            input.bufferStride = input.bufferSize.getWidth() * 4;
            input.pixelFormat = BytedEffectConstants.PixlFormat.RGBA8888;
        }
        LogTimerRecord.STOP("bufferConvert");

        return super.process(input);
    }

    @Override
    protected void setConfig(Map<TaskKey, Object> config) {
        super.setConfig(config);

        if (hasConfig(BUFFER_PREFER_SIZE)) {
            mPreferSize = (ProcessInput.Size) config.get(BUFFER_PREFER_SIZE);
        }

        if (hasConfig(BUFFER_CONVERT)) {
            mBufferConvert = (BufferConvert) config.get(BUFFER_CONVERT);
        }
    }

    @Override
    public TaskKey getKey() {
        return BUFFER_CONVERT_TASK;
    }

    public static void register() {
        TaskFactory.register(BUFFER_CONVERT_TASK, new TaskFactory.TaskGenerator<ResourceProvider>() {
            @Override
            public Task<ResourceProvider> create(Context context, ResourceProvider provider) {
                return new BufferConvertTask(context, provider);
            }
        });
    }
}
