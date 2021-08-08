package com.bytedance.labcv.demo.core.v4.base.util;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by QunZhang on 2020/7/28 10:41
 */
public class TaskKey implements Serializable {
    private int id;
    private String desc;
    private boolean isTask;

    public TaskKey(int id) {
        this.id = id;
    }

    public TaskKey(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public TaskKey(int id, String desc, boolean isTask) {
        this.id = id;
        this.desc = desc;
        this.isTask = isTask;
    }

    public int getId() {
        return id;
    }

    /**
     * if the key represents a task in BETaskFactory, it's YES
     * if the key just represents a param or something else, it's NO
     * @return is task key
     */
    public boolean isTask() {
        return isTask;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskKey that = (TaskKey) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
