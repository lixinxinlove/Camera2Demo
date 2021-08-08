package com.bytedance.labcv.demo.presenter.contract;


import com.bytedance.labcv.demo.base.BasePresenter;
import com.bytedance.labcv.demo.base.IView;
import com.bytedance.labcv.demo.model.ImageQualityItem;

import java.util.List;

public interface ImageQualityContract {
    interface View extends IView {

    }

    abstract class Presenter extends BasePresenter<ImageQualityContract.View> {
        public abstract List<ImageQualityItem> getItems();
    }
}
