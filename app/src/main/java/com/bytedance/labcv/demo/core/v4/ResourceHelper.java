package com.bytedance.labcv.demo.core.v4;

import android.content.Context;

import com.bytedance.labcv.demo.core.v4.algorithm.AlgorithmResourceHelper;
import com.bytedance.labcv.demo.core.v4.algorithm.AlgorithmTask;
import com.bytedance.labcv.demo.core.v4.effect.EffectResourceHelper;
import com.bytedance.labcv.demo.utils.AppUtils;

/**
 * Created by QunZhang on 2020/7/30 14:22
 *
 * a delegate class
 */
public class ResourceHelper extends EffectResourceHelper implements AlgorithmTask.AlgorithmResourceProvider {
    private AlgorithmResourceHelper mAlgorithmHelper;

    public ResourceHelper(Context context) {
        super(context);
        mAlgorithmHelper = new AlgorithmResourceHelper(context);
    }

    @Override
    public String getLicensePath() {
        if (AppUtils.getAppType() == AppUtils.AppType.ALGORITHM) {
            return mAlgorithmHelper.getLicensePath();
        } else {
            return super.getLicensePath();
        }
    }

    @Override
    public String getModelPath(String modelName) {
        return mAlgorithmHelper.getModelPath(modelName);
    }
}
