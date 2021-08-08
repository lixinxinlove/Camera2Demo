package com.bytedance.labcv.demo.core.v4.base;

import java.nio.ByteBuffer;

/**
 * Created by QunZhang on 2020/8/3 11:14
 */
public interface BufferConvert {
    ByteBuffer getResizeOutputTextureBuffer(int texture);

    void setResizeRatio(float ratio);
}
