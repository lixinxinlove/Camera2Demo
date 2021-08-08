package com.bytedance.labcv.demo.core.v4.effect.task;

import android.content.Context;

import com.bytedance.labcv.demo.core.v4.base.ResourceProvider;
import com.bytedance.labcv.demo.core.v4.base.Task;
import com.bytedance.labcv.demo.core.v4.base.util.TaskFactory;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKey;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKeyFactory;

/**
 * Created by QunZhang on 2020/7/30 10:43
 */
public class ImageEffectTask extends EffectTask {
    public static final TaskKey IMAGE_EFFECT = TaskKeyFactory.create("imageEffect", true);

    static {
        TaskFactory.register(IMAGE_EFFECT, new TaskFactory.TaskGenerator() {
            @Override
            public Task create(Context context, ResourceProvider provider) {
                return new ImageEffectTask();
            }
        });
    }

    @Override
    public TaskKey getKey() {
        return IMAGE_EFFECT;
    }
}
