package com.bytedance.labcv.demo.core.v4.base.task;

import android.content.Context;

import com.bytedance.labcv.demo.core.v4.base.ProcessInput;
import com.bytedance.labcv.demo.core.v4.base.ProcessOutput;
import com.bytedance.labcv.demo.core.v4.base.ResourceProvider;
import com.bytedance.labcv.demo.core.v4.base.Task;
import com.bytedance.labcv.demo.core.v4.base.util.TaskFactory;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKey;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKeyFactory;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

/**
 * Created by QunZhang on 2020/8/5 17:45
 */
public class BufferCaptureTask extends Task<ResourceProvider> {
    public static final TaskKey BUFFER_CAPTURE = TaskKeyFactory.create("bufferCapture", true);

    static {
        TaskFactory.register(BUFFER_CAPTURE, new TaskFactory.TaskGenerator() {
            @Override
            public Task create(Context context, ResourceProvider provider) {
                return new BufferCaptureTask();
            }
        });
    }

    @Override
    public List<TaskKey> getDependency() {
        return Collections.singletonList(BufferConvertTask.BUFFER_CONVERT_TASK);
    }

    @Override
    public int init() {
        return 0;
    }

    @Override
    public ProcessOutput process(ProcessInput input) {
        ProcessOutput output = super.process(input);
        output.bufferCaptureTaskResult = new BufferCaptureTaskResult(input.buffer, input.bufferSize);
        return output;
    }

    @Override
    public int destroy() {
        return 0;
    }

    @Override
    public int getPriority() {
        return 1000;
    }

    @Override
    public TaskKey getKey() {
        return BUFFER_CAPTURE;
    }

    public interface BufferCaptureInterface {

    }

    public static class BufferCaptureTaskResult {
        public ByteBuffer buffer;
        public ProcessInput.Size size;

        public BufferCaptureTaskResult(ByteBuffer buffer, ProcessInput.Size size) {
            this.buffer = buffer;
            this.size = size;
        }
    }
}
