package com.bytedance.labcv.demo.core.v4.image_quality;

import com.bytedance.labcv.demo.core.v4.base.ResourceProvider;
import com.bytedance.labcv.effectsdk.BytedEffectConstants;

public interface ImageQualityInterface {
    /**
     * 工作在渲染线程
     * Work on the render thread
     */
    int destory();

    /**
     * 初始化特效SDK，确保在gl线程中执行
     * dir 确定要求可读可写权限
     */
    int init(String dir);

    void selectImageQuality(BytedEffectConstants.ImageQualityType type);
    int processTexture(int srcTextureId, BytedEffectConstants.TextureFormat textureFormat,
                       int srcTextureWidth, int srcTextureHeight, ImageQualityManager.ImageQualityResult result);

    interface ImageQualityResourceProvider extends ResourceProvider{

    };
}
