package com.bytedance.labcv.demo.presenter;

import com.bytedance.labcv.demo.core.v4.base.util.TaskKey;
import com.bytedance.labcv.demo.model.AlgorithmItem;
import com.bytedance.labcv.demo.presenter.contract.DetectContract;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by QunZhang on 2020/8/18 11:16
 */
public class DetectPresenter extends DetectContract.Presenter {
    private static List<AlgorithmItem> sItems = new ArrayList<>();
    private static List<IFragmentGenerator> sGenerators = new ArrayList<>();

    public static void register(AlgorithmItem item) {
        sItems.add(item);
    }

    public static void registerFragmentGenerator(IFragmentGenerator generator) {
        sGenerators.add(generator);
    }

    public static void clear() {
        sItems.clear();
        sGenerators.clear();
    }

    @Override
    public List<AlgorithmItem> getAlgorithmItems(HashSet<TaskKey> set) {
        List<AlgorithmItem> items = new ArrayList<>();
        for (AlgorithmItem item : sItems) {
            if (!set.contains(item.getKey())) {
                items.add(item);
            }
        }
        return items;
    }

    @Override
    public List<FragmentInfo> getAlgorithmFragments(HashSet<TaskKey> disable) {
        List<FragmentInfo> fragmentInfo = new ArrayList<>();
        for (IFragmentGenerator generator : sGenerators) {
            if (disable.contains(generator.key())) {
                continue;
            }
            fragmentInfo.add(new FragmentInfo(generator.create(), generator.title()));
        }
        return fragmentInfo;
    }
}
