package com.bytedance.labcv.demo.core.v4.effect;

import com.bytedance.labcv.effectsdk.BytedEffectConstants;

/**
 * Created by QunZhang on 2020/8/3 17:34
 */
public interface TextureFormatter {
    int drawFrameOffScreen(int texture, BytedEffectConstants.TextureFormat format, int width, int height, int cameraRotation, boolean flipHorizontal, boolean flipVertical);
}
