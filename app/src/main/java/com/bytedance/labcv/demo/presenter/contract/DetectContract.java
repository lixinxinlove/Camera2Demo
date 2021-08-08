package com.bytedance.labcv.demo.presenter.contract;

import androidx.fragment.app.Fragment;

import com.bytedance.labcv.demo.base.BasePresenter;
import com.bytedance.labcv.demo.base.IView;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKey;
import com.bytedance.labcv.demo.model.AlgorithmItem;

import java.util.HashSet;
import java.util.List;

/**
 * Created by QunZhang on 2020/8/18 11:15
 */
public interface DetectContract {
    interface View extends IView {

    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract List<AlgorithmItem> getAlgorithmItems(HashSet<TaskKey> disable);

        public abstract List<FragmentInfo> getAlgorithmFragments(HashSet<TaskKey> disable);

        public static class FragmentInfo {
            public Fragment fragment;
            public int title;

            public FragmentInfo(Fragment fragment, int title) {
                this.fragment = fragment;
                this.title = title;
            }
        }

        public interface IFragmentGenerator {
            Fragment create();
            int title();
            TaskKey key();
        }
    }
}
