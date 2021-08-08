package com.bytedance.labcv.demo.presenter.contract;

import android.util.SparseArray;
import android.util.SparseIntArray;

import com.bytedance.labcv.demo.base.BasePresenter;
import com.bytedance.labcv.demo.base.IView;
import com.bytedance.labcv.demo.model.ComposerNode;
import com.bytedance.labcv.demo.model.EffectButtonItem;
import com.bytedance.labcv.demo.ui.BaseEffectActivity;

import java.util.List;
import java.util.Set;

/**
 * Created by QunZhang on 2019-07-22 13:45
 */
public interface EffectContract {

    interface View extends IView {
        BaseEffectActivity.EffectType getEffectType();
    }

    abstract class Presenter extends BasePresenter<View> {

        public static class TabItem {
            public int id;
            public int title;

            public TabItem(int id, int title) {
                this.id = id;
                this.title = title;
            }
        }

        abstract public String[][] generateComposerNodesAndTags(Set<EffectButtonItem> selectNodes);

        abstract public List<EffectButtonItem> getDefaultItems();

        /**
         * 获取某一功能默认值
         * @param type 功能 id
         * @return 默认值
         */
        abstract public float[] getDefaultValue(int type);

        /**
         * 根据类型返回一个 {@link EffectButtonItem} 列表
         * Returns a list of {@link EffectButtonItem} based on type
         * @param type menu type
         * @return ButtonItem item
         */
        public abstract EffectButtonItem getItem(int type);

        public abstract List<TabItem> getTabItems();
    }
}
