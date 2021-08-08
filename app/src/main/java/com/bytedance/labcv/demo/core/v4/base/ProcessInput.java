package com.bytedance.labcv.demo.core.v4.base;

import com.bytedance.labcv.effectsdk.BefFaceInfo;
import com.bytedance.labcv.effectsdk.BytedEffectConstants;

import java.nio.ByteBuffer;

/**
 * Created by QunZhang on 2020/7/14 20:55
 */
public class ProcessInput {
    public int texture;
    public ByteBuffer buffer;
    public Size textureSize;
    public Size bufferSize = new Size(0, 0);
    public int bufferStride;
    public BytedEffectConstants.PixlFormat pixelFormat = BytedEffectConstants.PixlFormat.RGBA8888;
    public BytedEffectConstants.TextureFormat textureFormat = BytedEffectConstants.TextureFormat.Texure2D;
    public BytedEffectConstants.Rotation sensorRotation = BytedEffectConstants.Rotation.CLOCKWISE_ROTATE_0;
    public int cameraRotation = 0;
    public boolean frontCamera = true;
    public long timeStamp;

    public BefFaceInfo faceInfo;

    public static class Size {
        private int width;
        private int height;

        public Size(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public void merge(Size s) {
            width = Math.max(width, s.width);
            height = Math.max(height, s.height);
        }

        @SuppressWarnings("SuspiciousNameCombination")
        public void revert() {
            int tmp = width;
            width = height;
            height = tmp;
        }

        @Override
        public String toString() {
            return "Size{" +
                    "width=" + width +
                    ", height=" + height +
                    '}';
        }
    }
}
