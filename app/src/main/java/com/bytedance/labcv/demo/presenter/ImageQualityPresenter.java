package com.bytedance.labcv.demo.presenter;

import android.content.Context;

import com.bytedance.labcv.demo.R;
import com.bytedance.labcv.demo.model.ImageQualityItem;
import com.bytedance.labcv.demo.presenter.contract.ImageQualityContract;
import com.bytedance.labcv.demo.ui.DemoApplication;
import com.bytedance.labcv.effectsdk.BytedEffectConstants;

import java.util.ArrayList;
import java.util.List;

public class ImageQualityPresenter extends ImageQualityContract.Presenter {
    private  static  final int [] IMAGES = new int[]{
            R.drawable.zhengchang,
            R.drawable.baixi,
            R.drawable.baixi
    };

    private static final BytedEffectConstants.ImageQualityType[] ImageQulityTypes =
            new BytedEffectConstants.ImageQualityType []{
                    BytedEffectConstants.ImageQualityType.IMAGE_QUALITY_TYPE_NONE,
                    BytedEffectConstants.ImageQualityType.IMAGE_QUALITY_TYPE_VIDEO_SR,
                    BytedEffectConstants.ImageQualityType.IMAGE_QUALITY_TYPE_NIGHT_SCENE,
    };

    private List<ImageQualityItem> mItems;
    @Override
    public List<ImageQualityItem> getItems() {
        if (mItems != null){
            return mItems;
        }

        mItems = new ArrayList<>();
        Context context = DemoApplication.context();
        String[] IMAGEQUALITY_TITLE = new String[]{
                context.getString(R.string.filter_normal),
            context.getString(R.string.title_video_sr),
            context.getString(R.string.title_night_scene),
        };

        for (int i = 0; i < IMAGEQUALITY_TITLE.length; i++){
            mItems.add(new ImageQualityItem(IMAGEQUALITY_TITLE[i],
                    IMAGES[i], ImageQulityTypes[i]));
        }
        return mItems;
    }
}
