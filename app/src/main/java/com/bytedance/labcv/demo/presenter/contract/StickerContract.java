package com.bytedance.labcv.demo.presenter.contract;

import com.bytedance.labcv.demo.base.BasePresenter;
import com.bytedance.labcv.demo.base.IView;
import com.bytedance.labcv.demo.model.StickerItem;

import java.util.List;

/**
 * Created by QunZhang on 2019-07-21 12:24
 */
public interface StickerContract {
    int OFFSET = 8;
    int MASK = ~0xff;
    int TYPE_STICKER = 1 << OFFSET;
    int TYPE_ANIMOJI = 2 << OFFSET;
//    int TYPE_ARSCAN = 3 << OFFSET;
    int TYPE_STICKER_2D = TYPE_STICKER + 1;
    int TYPE_STICKER_COMPLEX = TYPE_STICKER + 2;
    int TYPE_STICKER_3D = TYPE_STICKER + 3;
    int TYPE_STICKER_ADVANCED = TYPE_STICKER + 4;
//    int TYPE_ANIMOJI = TYPE_STICKER + 5;
    int TYPE_ARSCAN = TYPE_STICKER + 5;
    int TYPE_STICKER_STYLE_MAKEUP = TYPE_STICKER + 6;

    interface View extends IView {

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

//        public abstract List<StickerItem> getItems();

        public abstract List<StickerItem> getItems(int type);

        public abstract List<TabItem> getTabItems();
    }
}
