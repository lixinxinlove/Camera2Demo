package com.bytedance.labcv.demo.core.v4.base.task;

import com.bytedance.labcv.demo.core.v4.base.util.TaskKey;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKeyFactory;
import com.bytedance.labcv.demo.core.v4.base.Task;
import com.bytedance.labcv.demo.core.v4.effect.EffectInterface;
import com.bytedance.labcv.demo.core.v4.effect.TextureFormatter;

import java.util.Map;

/**
 * Created by QunZhang on 2020/8/3 12:18
 */
public abstract class TextureFormatTask extends Task<EffectInterface.EffectResourceProvider> {
    public static final TaskKey TEXTURE_FORMAT = TaskKeyFactory.create("textureFormat", true);

    protected TextureFormatter mTextureFormatter;

    @Override
    public int init() {
        return 0;
    }

    @Override
    protected void setConfig(Map<TaskKey, Object> config) {
        super.setConfig(config);

        if (hasConfig(TEXTURE_FORMAT)) {
            mTextureFormatter = (TextureFormatter) config.get(TEXTURE_FORMAT);
        }
    }

    @Override
    public int destroy() {
        return 0;
    }

    @Override
    public int getPriority() {
        return 20000;
    }

}
