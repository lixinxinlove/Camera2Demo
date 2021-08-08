package com.bytedance.labcv.demo.core.v4.effect.task;

import android.content.Context;

import com.bytedance.labcv.demo.core.v4.base.ResourceProvider;
import com.bytedance.labcv.demo.core.v4.base.Task;
import com.bytedance.labcv.demo.core.v4.base.task.VideoTextureFormatTask;
import com.bytedance.labcv.demo.core.v4.base.util.TaskFactory;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKey;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKeyFactory;

import java.util.Collections;
import java.util.List;

/**
 * Created by QunZhang on 2020/7/30 11:04
 */
public class VideoEffectTask extends EffectTask {
    public static final TaskKey VIDEO_EFFECT = TaskKeyFactory.create("videoEffect", true);

    static {
        TaskFactory.register(VIDEO_EFFECT, new TaskFactory.TaskGenerator() {
            @Override
            public Task create(Context context, ResourceProvider provider) {
                return new VideoEffectTask();
            }
        });
    }

    @Override
    public List<TaskKey> getDependency() {
        return Collections.singletonList(VideoTextureFormatTask.VIDEO_TEXTURE_FORMAT);
    }

    @Override
    public TaskKey getKey() {
        return VIDEO_EFFECT;
    }
}
