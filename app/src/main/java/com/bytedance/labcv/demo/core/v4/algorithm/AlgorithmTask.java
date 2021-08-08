package com.bytedance.labcv.demo.core.v4.algorithm;


import android.content.Context;

import com.bytedance.labcv.demo.core.v4.base.ProcessInput;
import com.bytedance.labcv.demo.core.v4.base.ResourceProvider;
import com.bytedance.labcv.demo.core.v4.base.Task;
import com.bytedance.labcv.demo.core.v4.base.task.BufferConvertTask;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKey;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKeyFactory;

import java.util.Collections;
import java.util.List;

/**
 * Created by QunZhang on 2020/7/15 10:19
 *
 * base class for algorithm task
 */
public abstract class AlgorithmTask extends Task<AlgorithmTask.AlgorithmResourceProvider> {
    public static final TaskKey USER_SETTING = TaskKeyFactory.create("userSetting");
    public static final TaskKey ALGORITHM_FOV = TaskKeyFactory.create("fov");

    public AlgorithmTask(Context context, AlgorithmResourceProvider resourceProvider) {
        super(context, resourceProvider);
    }

    /**
     * preferable size of buffer
     * @return {@link ProcessInput.Size}
     */
    public ProcessInput.Size preferBufferSize() {
        return null;
    }

    @Override
    public List<TaskKey> getDependency() {
        return Collections.singletonList(BufferConvertTask.BUFFER_CONVERT_TASK);
    }

    /**
     * whether has use-setting detect config key
     * @param key AlgorithmKey
     * @return true if set
     */
    protected boolean hasUserSettingConfig(TaskKey key) {
        return mConfig.containsKey(key) && mConfig.get(key) == USER_SETTING;
    }

    public interface AlgorithmResourceProvider extends ResourceProvider {
        String getModelPath(String modelName);
    }
}
