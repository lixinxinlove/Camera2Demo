package com.bytedance.labcv.demo.core.v4.base.util;

/**
 * Created by QunZhang on 2020/7/28 10:42
 */
public class TaskKeyFactory {
    private static int sKeyIndex;

    public static TaskKey create() {
        return new TaskKey(sKeyIndex++);
    }

    public static TaskKey create(String desc) {
        return new TaskKey(sKeyIndex++, desc);
    }

    public static TaskKey create(String desc, boolean isTask) {
        return new TaskKey(sKeyIndex++, desc, isTask);
    }
}
