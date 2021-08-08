package com.bytedance.labcv.demo.core.v4.effect.task;

import android.content.Context;

import com.bytedance.labcv.demo.core.v4.base.ResourceProvider;
import com.bytedance.labcv.demo.core.v4.base.Task;
import com.bytedance.labcv.demo.core.v4.base.task.PreviewTextureFormatTask;
import com.bytedance.labcv.demo.core.v4.base.util.TaskFactory;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKeyFactory;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKey;

import java.util.Collections;
import java.util.List;

/**
 * Created by QunZhang on 2020/7/15 16:50
 */
public class PreviewEffectTask extends EffectTask {
    public static final TaskKey PREVIEW_EFFECT = TaskKeyFactory.create("previewEffect", true);

    static {
        TaskFactory.register(PREVIEW_EFFECT, new TaskFactory.TaskGenerator() {
            @Override
            public Task create(Context context, ResourceProvider provider) {
                return new PreviewEffectTask();
            }
        });
    }

    @Override
    public List<TaskKey> getDependency() {
        return Collections.singletonList(PreviewTextureFormatTask.PREVIEW_TEXTURE_FORMAT);
    }

    @Override
    public TaskKey getKey() {
        return PREVIEW_EFFECT;
    }
}
