package com.bytedance.labcv.demo.core.v4.base;

import android.content.Context;

import com.bytedance.labcv.demo.core.v4.algorithm.AlgorithmTask;
import com.bytedance.labcv.demo.core.v4.base.util.TaskFactory;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by QunZhang on 2020/7/14 21:05
 */
@SuppressWarnings("ALL")
public abstract class TaskContainer<RP extends ResourceProvider, T extends Task<RP>> extends Task<RP> {
    /**
     * container task
     */
    protected List<T> children;
    /**
     * the first in task chain
     */
    protected Task chain;

    private OnConfigChanged mOnConfigChanged;

    private List<OnChainRebuiltListener> mOnChainRebuiltListeners;

    private boolean mNeedRefreshConfig;
    private boolean mNeedRebuildChain;

    public TaskContainer(Context context, RP resourceProvider) {
        super(context, resourceProvider);
        initChildren();
        mConfig = new HashMap<>();
    }

    protected void initChildren() {
        children = new ArrayList<>();
    }

    @Override
    public int destroy() {
        removeAllTask(new ArrayList<T>(children));
        // remove all dependency tasks
        Iterator<Map.Entry<TaskKey, Object>> it = mConfig.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<TaskKey, Object> entry = it.next();
            if (entry.getValue() == AlgorithmTask.DEPENDENCY) {
                it.remove();
            }
        }
        return 0;
    }

    @Override
    public int getPriority() {
        int max = 0;
        for (Task task : children) {
            max = Math.max(task.getPriority(), max);
        }
        return max + 1;
    }

    @Override
    protected void setConfig(Map<TaskKey, Object> config) {
        mConfig.putAll(config);
        for (T child : children) {
            child.setConfig(mConfig);
        }
    }

    @Override
    public ProcessOutput process(ProcessInput input) {
        if (mNeedRefreshConfig) {
            refreshConfig(mNeedRebuildChain);
            mNeedRefreshConfig = false;
            mNeedRebuildChain = false;
        }

        if (chain == null) return super.process(input);
        return chain.process(input);
    }

    public void addTask(TaskKey key) {
        addConfig(key, true, null);
    }

    public void removeTask(TaskKey key) {
        addConfig(key, false, null);
    }

    public void addConfig(TaskKey config, boolean flag, Object value) {
        if (flag) {
            mConfig.put(config, value);
        } else {
            mConfig.remove(config);
        }

        setNeedRefreshConfig(true);
        setNeedRebuildChain(config.isTask());
    }

    public void addParam(TaskKey key, Object value) {
        addConfig(key, true, value);
    }

    public void removeParam(TaskKey key) {
        addConfig(key, false, null);
    }

    protected void refreshConfig(boolean rebuildChain) {
        if (rebuildChain) {
            rebuildChain();
        }

        if (mOnConfigChanged != null) {
            mOnConfigChanged.onChanged(mConfig);
        }

        for (T child : children) {
            child.setConfig(mConfig);
        }

        if (rebuildChain) {
            afterBuildChain();
        }
    }

    /**
     * rebuild chain according to config
     * need remove unused tasks and add depended tasks
     */
    protected void rebuildChain() {
        Set<TaskKey> toRemove = removeDependenciesConfig();
        // add tasks not in children, but in mDetectConfig
        List<T> addTasks = TaskFactory.createAll(mConfig, children, mContext, mResourceProvider);
        addAllTask(addTasks, false);

        // add tasks which are dependency of tasks in mDetectConfig
        refreshDependency(toRemove);

        // remove tasks not in mDetectConfig and mDependencyConfig
        List<T> removeTasks = TaskFactory.destroyAll(mConfig, children);
        removeAllTask(removeTasks, false);

        // rebuild task chain
        if (isRoot()) {
            for (Task child : children) {
                if (child instanceof TaskContainer) {
                    ((TaskContainer) child).rebuildChain();
                }
            }

            buildChain();
        }
    }

    protected void afterBuildChain() {
        if (mOnChainRebuiltListeners != null) {
            for (OnChainRebuiltListener listener : mOnChainRebuiltListeners) {
                listener.onChainRebuilt();
            }
        }
    }

    protected void refreshDependency(Set<TaskKey> toRemove) {
        int algorithmAdd = -1;
        List<T> dependencies = new ArrayList<>();

        while (algorithmAdd != 0) {
            algorithmAdd = 0;
            for (Task<?> task : children) {
                if (mConfig.containsKey(task.getKey())) {
                    // this task has dependencies, and they are not all in mDependencyConfig already
                    if (!task.getDependency().isEmpty()) {
                        Set<TaskKey> toAdd = addAllDependencies(task.getDependency(), toRemove);
                        if (!toAdd.isEmpty()) {
                            algorithmAdd += toAdd.size();
                            List<T> tasks = TaskFactory.createAll(toAdd, mContext, mResourceProvider);
                            dependencies.addAll(tasks);
                        }
                    }
                }
            }
            addAllTask(dependencies);
            dependencies.clear();
        }
    }

    private Set<TaskKey> addAllDependencies(List<TaskKey> keys, Set<TaskKey> toRemove) {
        Set<TaskKey> toAdd = new HashSet<>();
        for (TaskKey key : keys) {
            if (!mConfig.containsKey(key)) {
                mConfig.put(key, DEPENDENCY);
                if (!toRemove.contains(key)) {
                    toAdd.add(key);
                }
            }
        }
        return toAdd;
    }

    private Set<TaskKey> removeDependenciesConfig() {
        Set<TaskKey> toRemove = new HashSet<>();
        for (Map.Entry<TaskKey, ?> entry : mConfig.entrySet()) {
            if (entry.getValue() == DEPENDENCY) {
                toRemove.add(entry.getKey());
            }
        }
        for (TaskKey key : toRemove) {
            mConfig.remove(key);
        }
        return toRemove;
    }

    /**
     * get all tasks from children, and build them one chain
     * only do it in root node
     */
    protected void buildChain() {
        if (children.size() == 0 || !isRoot()) {
            chain = null;
            return;
        }

        List<Task> tasks = getTasks();
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return o2.getPriority() - o1.getPriority();
            }
        });
        chain = tasks.get(0);
        Task cur = chain;
        for (int i = 1; i < tasks.size(); i++) {
            cur.next = tasks.get(i);
            cur = tasks.get(i);
        }
        cur.next = null;
    }

    public void addTask(T task, boolean withRebuild) {
        addAllTask(Collections.singletonList(task), withRebuild);
    }

    public void removeTask(T task, boolean withRebuild) {
        removeAllTask(Collections.singletonList(task), withRebuild);
    }

    public void addAllTask(List<T> tasks) {
        addAllTask(tasks, true);
    }

    public void addAllTask(List<T> tasks, boolean withRebuild) {
        if (tasks == null || tasks.isEmpty()) return;
        for (T task : tasks) {
            if (!mConfig.containsKey(task.getKey())) {
                mConfig.put(task.getKey(), null);
            }
            task.parent = this;
            task.init();
        }
        children.addAll(tasks);
        if (withRebuild) {
            buildChain();
        }
    }

    public void removeAllTask(List<T> tasks) {
        removeAllTask(tasks, true);
    }

    public void removeAllTask(List<T> tasks, boolean withRebuild) {
        if (tasks == null || tasks.isEmpty()) return;
        children.removeAll(tasks);
        for (T task : tasks) {
            task.destroy();
            task.parent = null;
        }
        if (withRebuild) {
            buildChain();
        }
    }

    public void setOnConfigChanged(OnConfigChanged onConfigChanged) {
        mOnConfigChanged = onConfigChanged;
    }

    public void addOnChainRebuiltListener(OnChainRebuiltListener listener) {
        if (mOnChainRebuiltListeners == null) {
            mOnChainRebuiltListeners = new ArrayList<>();
        }
        mOnChainRebuiltListeners.add(listener);
    }

    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        for (T child : children) {
            if (child instanceof TaskContainer) {
                tasks.addAll(((TaskContainer) child).getTasks());
                tasks.add(child);
            } else {
                tasks.add(child);
            }
        }
        return tasks;
    }

    private void setNeedRefreshConfig(boolean needRefreshConfig) {
        if (isRoot()) {
            mNeedRefreshConfig = needRefreshConfig;
        } else {
            ((TaskContainer)parent).setNeedRefreshConfig(needRefreshConfig);
        }
    }

    private void setNeedRebuildChain(boolean needRebuildChain) {
        if (isRoot()) {
            mNeedRebuildChain |= needRebuildChain;
        } else {
            ((TaskContainer)parent).setNeedRebuildChain(needRebuildChain);
        }
    }

    public interface OnConfigChanged {
        void onChanged(Map<TaskKey, Object> config);
    }

    public interface OnChainRebuiltListener {
        void onChainRebuilt();
    }
}
