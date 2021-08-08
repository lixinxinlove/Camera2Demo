package com.bytedance.labcv.demo.core.v4.base;

import android.content.Context;
import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bytedance.labcv.demo.core.v4.base.util.TaskKeyFactory;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKey;
import com.bytedance.labcv.demo.ui.LocalBroadcastReceiver;
import com.bytedance.labcv.effectsdk.library.LogUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by QunZhang on 2020/7/14 20:53
 */
public abstract class Task<T extends ResourceProvider> {
    public static final TaskKey DEPENDENCY = TaskKeyFactory.create("dependency");
    /**
     * next task, nullable
     */
    protected Task<?> next;

    protected Task<?> parent;

    protected Context mContext;

    protected T mResourceProvider;

    protected Map<TaskKey, Object> mConfig;

    public Task() {

    }

    public Task(Context context, T resourceProvider) {
        mContext = context;
        mResourceProvider = resourceProvider;
    }

    public abstract int init();

    public abstract int destroy();

    /**
     * priority of task, decide process order of tasks in TaskContainer
     * @return a int number
     */
    public abstract int getPriority();

    /**
     * make sure all dependencies' priority > this
     * @return config of dependency
     */
    public List<TaskKey> getDependency() {
        return Collections.emptyList();
    }


    protected void setConfig(Map<TaskKey, Object> config) {
        mConfig = config;
    }

    /**
     * key of this task, used by {@link TaskKeyFactory}
     * @return algorithm key of this task
     */
    public abstract TaskKey getKey();

    /**
     * implementation of process in this task
     * @param input process arguments
     * @return process output
     */
    public ProcessOutput process(ProcessInput input) {
        if (next != null) {
            return next.process(input);
        }
        return new ProcessOutput();
    }


    /**
     * whether has user-setting and dependent detect config key
     * @param key AlgorithmKey
     * @return true if set
     */
    protected boolean hasConfig(TaskKey key) {
        return mConfig.containsKey(key);
    }

    /**
     * get a bool config value
     * @param key config
     * @return config value, or false if not set
     */
    protected boolean getBooleanConfig(TaskKey key) {
        if (hasConfig(key)) {
            Object o = mConfig.get(key);
            if (o instanceof Boolean) {
                return (Boolean) o;
            }
        }
        return false;
    }

    /**
     * get a float config value
     * @param key config
     * @return config value, or 0 if not set
     */
    protected float getFloatConfig(TaskKey key) {
        return getFloatConfig(key, 0F);
    }

    protected float getFloatConfig(TaskKey key, float defaultV) {
        if (hasConfig(key)) {
            Object o = mConfig.get(key);
            if (o instanceof Float) {
                return (Float) o;
            }
        }
        return defaultV;
    }

    protected String getTag() {
        return this.getClass().getName();
    }

    protected boolean isRoot() {
        return parent == null;
    }

    protected boolean checkResult(String msg, int ret) {
        if (ret != 0) {
            String log = msg + " fail: " + ret;
            Intent intent = new Intent(LocalBroadcastReceiver.ACTION);
            intent.putExtra("msg", log);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            return false;
        }
        return true;
    }
}
