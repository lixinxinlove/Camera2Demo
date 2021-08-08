package com.bytedance.labcv.demo.core.v4.base.util;

import android.content.Context;

import com.bytedance.labcv.demo.core.v4.base.ResourceProvider;
import com.bytedance.labcv.demo.core.v4.base.Task;
import com.bytedance.labcv.effectsdk.library.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by QunZhang on 2020/7/23 17:16
 * <p>
 * algorithm task factory
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class TaskFactory {
    private static Map<TaskKey, TaskGenerator<ResourceProvider>> sRegister = new HashMap<>();

    /*
     * all AlgorithmTask must register itself here
     */
    static {
//        FaceAlgorithmTask.register();
//        HandAlgorithmTask.register();
//        SkeletonAlgorithmTask.register();
//        HeadSegmentAlgorithmTask.register();
//        PortraitMattingAlgorithmTask.register();
//        HairParserAlgorithmTask.register();
//        LightClsAlgorithmTask.register();
//        HumanDistanceAlgorithmTask.register();
//        BufferConvertTask.register();
//        DynamicActionAlgorithmTask.register();
//        PetFaceAlgorithmTask.register();
//        register(PreviewEffectTask.PREVIEW_EFFECT, new TaskGenerator<ResourceProvider>() {
//            @Override
//            public Task create(Context context, ResourceProvider provider) {
//                return new PreviewEffectTask();
//            }
//        });
//        register(VideoEffectTask.VIDEO_EFFECT, new TaskGenerator() {
//            @Override
//            public Task create(Context context, ResourceProvider provider) {
//                return new VideoEffectTask();
//            }
//        });
//        register(ImageEffectTask.IMAGE_EFFECT, new TaskGenerator() {
//            @Override
//            public Task create(Context context, ResourceProvider provider) {
//                return new ImageEffectTask();
//            }
//        });
//        register(PreviewTextureFormatTask.PREVIEW_TEXTURE_FORMAT, new TaskGenerator() {
//            @Override
//            public Task create(Context context, ResourceProvider provider) {
//                return new PreviewTextureFormatTask();
//            }
//        });
//        register(VideoTextureFormatTask.VIDEO_TEXTURE_FORMAT, new TaskGenerator() {
//            @Override
//            public Task create(Context context, ResourceProvider provider) {
//                return new VideoTextureFormatTask();
//            }
//        });
//        register(BufferCaptureTask.BUFFER_CAPTURE, new TaskGenerator() {
//            @Override
//            public Task create(Context context, ResourceProvider provider) {
//                return new BufferCaptureTask();
//            }
//        });
    }

    public static <T extends Task<ResourceProvider>> T create(TaskKey key, Context context, ResourceProvider resourceProvider) {
        if (sRegister.containsKey(key)) {
            return (T) sRegister.get(key).create(context, resourceProvider);
        } else {
            LogUtils.e("task key " + key.getId() + " not found");
        }
        return null;
    }

    public static <RP extends ResourceProvider, T extends Task<RP>> List<T> createAll(Map<TaskKey, Object> config, List<T> exists, Context context, RP resourceProvider) {
        List<T> tasks = new ArrayList<>();
        Set<Object> existKey = new HashSet<>();
        for (T task : exists) {
            existKey.add(task.getKey());
        }
        for (Map.Entry<TaskKey, TaskGenerator<ResourceProvider>> entry : sRegister.entrySet()) {
            if (config.containsKey(entry.getKey()) && !existKey.contains(entry.getKey())) {
                tasks.add((T) entry.getValue().create(context, resourceProvider));
            }
        }
        return tasks;
    }

    public static <RP extends ResourceProvider, T extends Task<RP>> List<T> createAll(Set<TaskKey> keys, Context context, RP resourceProvider) {
        List<T> tasks = new ArrayList<>();
        for (TaskKey key : keys) {
            tasks.add((T) create(key, context, resourceProvider));
        }
        return tasks;
    }

    public static <RP extends ResourceProvider, T extends Task<RP>> List<T> destroyAll(Map<TaskKey, Object> config, List<T> exists) {
        List<T> tasks = new ArrayList<>();
        for (T task : exists) {
            if (!config.containsKey(task.getKey())) {
                tasks.add(task);
            }
        }
        return tasks;
    }

    public static void register(TaskKey key, TaskGenerator generate) {
        sRegister.put(key, generate);
    }

    public interface TaskGenerator<T extends ResourceProvider> {
        Task<T> create(Context context, T provider);
    }
}
