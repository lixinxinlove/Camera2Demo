package com.bytedance.labcv.demo.core.v4;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.bytedance.labcv.demo.R;
import com.bytedance.labcv.demo.core.v4.algorithm.AlgorithmInterface;
import com.bytedance.labcv.demo.core.v4.algorithm.AlgorithmManager;
import com.bytedance.labcv.demo.core.v4.algorithm.AlgorithmRender;
import com.bytedance.labcv.demo.core.v4.base.ProcessInput;
import com.bytedance.labcv.demo.core.v4.base.ProcessOutput;
import com.bytedance.labcv.demo.core.v4.base.TaskContainer;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKey;
import com.bytedance.labcv.demo.core.v4.effect.EffectInterface;
import com.bytedance.labcv.demo.core.v4.effect.EffectManager;
import com.bytedance.labcv.demo.core.v4.image_quality.ImageQualityManager;
import com.bytedance.labcv.demo.model.CaptureResult;
import com.bytedance.labcv.demo.model.ComposerNode;
import com.bytedance.labcv.demo.utils.AppUtils;
import com.bytedance.labcv.demo.utils.timer_record.LogTimerRecord;
import com.bytedance.labcv.effectsdk.BefFaceInfo;
import com.bytedance.labcv.effectsdk.BefHandInfo;
import com.bytedance.labcv.effectsdk.BefSkeletonInfo;
import com.bytedance.labcv.effectsdk.BytedEffectConstants;
import com.bytedance.labcv.effectsdk.library.LogUtils;

import java.util.Arrays;

/**
 * Created by QunZhang on 2020/7/30 20:52
 * <p>
 * a delegate class
 * <p>
 * contains all effect tasks and algorithm tasks
 * for user who need both algorithm and effect
 */
@SuppressWarnings({"rawtypes"})
public class EffectHelper extends TaskContainer
        implements EffectInterface, AlgorithmInterface {
    private AlgorithmManager mAlgorithmDelegate;
    protected EffectManager mEffectDelegate;
    protected ImageQualityManager mImageQualityManager;

    public EffectHelper(Context context, EffectManager.EffectType effectType) {
        this(context, new ResourceHelper(context), effectType);
    }

    public EffectHelper(Context context, ResourceHelper resourceProvider, EffectManager.EffectType effectType) {
        super(context, resourceProvider);
        AlgorithmRender render = new AlgorithmRender(context);
        mAlgorithmDelegate = new AlgorithmManager(context, resourceProvider, render);
        mEffectDelegate = new EffectManager(context, resourceProvider, effectType, render);

        mImageQualityManager = new ImageQualityManager(context, resourceProvider);
        String dir = mContext.getExternalFilesDir("assets").getAbsolutePath();
        mImageQualityManager.init(dir);
    }

    @Override
    public int destroy() {
        mImageQualityManager.destory();
        return super.destroy();
    }

    @Override
    public void setFov(float fov) {
        mAlgorithmDelegate.setFov(fov);
    }

    @Override
    public void setFront(boolean front) {
        mEffectDelegate.setCameraPosition(front);
        mAlgorithmDelegate.setFront(front);
    }

    @Override
    public TaskKey getKey() {
        return null;
    }

    @Override
    public <T> void addResultCallback(ResultCallback<T> callback) {
        mAlgorithmDelegate.addResultCallback(callback);
    }

    @Override
    public <T> void removeResultCallback(ResultCallback<T> callback) {
        mAlgorithmDelegate.removeResultCallback(callback);
    }

    @Override
    public void setDrawAlgorithmResult(boolean drawAlgorithmResult) {
        mAlgorithmDelegate.setDrawAlgorithmResult(drawAlgorithmResult);
    }

    @Override
    public int init() {
        addAllTask(Arrays.asList(mAlgorithmDelegate, mEffectDelegate));
        setPipeline(true);
        set3Buffer(false);
        return 0;
    }

    @Override
    public void addAlgorithmTask(TaskKey config, boolean flag) {
        mAlgorithmDelegate.addAlgorithmTask(config, flag);
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        mEffectDelegate.onSurfaceChanged(width, height);
    }

    @Override
    public void setOnEffectListener(OnEffectListener listener) {
        mEffectDelegate.setOnEffectListener(listener);
    }

    @Override
    public void adjustTextureBuffer(int orientation, boolean flipHorizontal, boolean flipVertical) {
        mAlgorithmDelegate.adjustTextureBuffer(orientation, flipHorizontal, flipVertical);
    }

    @Override
    public void setImageSize(int imageWidth, int imageHeight) {
        mAlgorithmDelegate.setImageSize(imageWidth, imageHeight);
    }

    @Override
    public float getRatio() {
        return mAlgorithmDelegate.getRatio();
    }

    @Override
    public <T> void dispatchResult(Class<T> clz, Object o, int frameCounts) {
        mAlgorithmDelegate.dispatchResult(clz, o, frameCounts);
    }

    @Override
    public void setIsDrawOnOriginalTexture(boolean onOriginalTexture) {
        mAlgorithmDelegate.setIsDrawOnOriginalTexture(onOriginalTexture);
        mEffectDelegate.setIsDrawOnOriginalTexture(onOriginalTexture);
    }

    @Override
    public boolean setFilter(String path) {
        return mEffectDelegate.setFilter(path);
    }

    @Override
    public boolean updateFilterIntensity(float intensity) {
        return mEffectDelegate.updateFilterIntensity(intensity);
    }

    @Override
    public boolean setComposeNodes(String[] nodes) {
        return mEffectDelegate.setComposeNodes(nodes);
    }

    @Override
    public boolean setComposeNodes(String[] nodes, String[] tags) {
        return mEffectDelegate.setComposeNodes(nodes, tags);
    }

    @Override
    public boolean updateComposeNode(ComposerNode node, boolean update) {
        return mEffectDelegate.updateComposeNode(node, update);
    }

    @Override
    public boolean updateComposerNodeIntensity(String node, String key, float intensity) {
        return mEffectDelegate.updateComposerNodeIntensity(node, key, intensity);
    }

    @Override
    public boolean setSticker(String path) {
        return mEffectDelegate.setSticker(path);
    }

    @Override
    public boolean setStickerAbs(String absPath) {
        return mEffectDelegate.setStickerAbs(absPath);
    }

    @Override
    public boolean getAvailableFeatures(String[] features) {
        return mEffectDelegate.getAvailableFeatures(features);
    }

    @Override
    public BefFaceInfo getFaceDetectResult() {
        return mEffectDelegate.getFaceDetectResult();
    }

    @Override
    public BefFaceInfo getFaceMaskResult(BytedEffectConstants.FaceMaskType type) {
        return mEffectDelegate.getFaceMaskResult(type);
    }

    @Override
    public BefHandInfo getHandDetectResult() {
        return mEffectDelegate.getHandDetectResult();
    }

    @Override
    public BefSkeletonInfo getSkeletonDetectResult() {
        return mEffectDelegate.getSkeletonDetectResult();
    }

    @Override
    public boolean processTouchEvent(float x, float y) {
        return mEffectDelegate.processTouchEvent(x, y);
    }

    @Override
    public boolean processTouchEvent(BytedEffectConstants.EventCode eventCode, float x, float y, float extra) {
        return mEffectDelegate.processTouchEvent(eventCode, x, y, extra);
    }

    @Override
    public boolean setPipeline(boolean usePipeline) {
        boolean success = mEffectDelegate.setPipeline(usePipeline);
        if (success) {
            mAlgorithmDelegate.setPipeline(usePipeline);
        }
        return success;
    }

    @Override
    public boolean set3Buffer(boolean use3Buffer) {
        return mEffectDelegate.set3Buffer(use3Buffer);
    }

    @Override
    public void onCameraChanged() {
        mAlgorithmDelegate.onCameraChanged();
        mEffectDelegate.onCameraChanged();
    }

    @Override
    public void setCameraPosition(boolean isFront) {
        mEffectDelegate.setCameraPosition(isFront);
        mAlgorithmDelegate.setFront(isFront);
    }

    @Override
    public void setEffectOn(boolean isOn) {
        mEffectDelegate.setEffectOn(isOn);
        mImageQualityManager.setPause(!isOn);
    }

    @Override
    public void recoverStatus() {
        mEffectDelegate.recoverStatus();
        mAlgorithmDelegate.recoverStatus();
        mImageQualityManager.recoverStatus();
    }

    @Override
    public CaptureResult capture() {
        return mEffectDelegate.capture();
    }

    @Override
    public void addTask(TaskKey key) {
        mEffectDelegate.addTask(key);
    }

    @Override
    public ProcessOutput process(ProcessInput input) {
        LogTimerRecord.RECORD("totalProcess");
        ProcessOutput output = super.process(input);
        LogTimerRecord.STOP("totalProcess");

//        BefFaceInfo faceInfo = getFaceDetectResult();
//        if (faceInfo != null) {
//            LogUtils.e(faceInfo.toString());
//        }
//        BefHandInfo handInfo = getHandDetectResult();
//        if (handInfo != null) {
//            LogUtils.e(handInfo.toString());
//        }
//        BefSkeletonInfo skeletonInfo = getSkeletonDetectResult();
//        if (skeletonInfo != null) {
//            LogUtils.e(skeletonInfo.toString());
//        }

        return output;
    }

    public int processImageQualityTexture(int srcTextureId, BytedEffectConstants.TextureFormat srcTextureFormat,
                                          int srcTextureWidth, int srcTextureHeight, ImageQualityManager.ImageQualityResult result)
    {
        int ret = mImageQualityManager.processTexture(srcTextureId, srcTextureFormat, srcTextureWidth,srcTextureHeight,
                result);
        return ret;
    }
    public int processTexture(int srcTextureId, BytedEffectConstants.TextureFormat srcTextureFormat,
                              int srcTextureWidth, int srcTextureHeight, int cameraRotation,
                              boolean frontCamera, BytedEffectConstants.Rotation sensorRotation,
                              long timestamp) {
        ProcessInput input = new ProcessInput();
        input.texture = srcTextureId;
        input.textureSize = new ProcessInput.Size(srcTextureWidth, srcTextureHeight);
        input.cameraRotation = cameraRotation;
        input.textureFormat = srcTextureFormat;
        input.sensorRotation = sensorRotation;
        input.frontCamera = frontCamera;
        input.timeStamp = timestamp;
        return process(input).texture;
    }

    public int processImageTexture(int srcTextureId, int width, int height) {
        ProcessInput input = new ProcessInput();
        input.texture = srcTextureId;
        input.textureSize = new ProcessInput.Size(width, height);
        input.textureFormat = BytedEffectConstants.TextureFormat.Texure2D;
        input.timeStamp = System.nanoTime();
        return process(input).texture;
    }

    public int processVideoTexture(int srcTextureId, BytedEffectConstants.TextureFormat srcTextureFormat, int width, int height, int videoRotation) {
        ProcessInput input = new ProcessInput();
        input.texture = srcTextureId;
        input.textureFormat = srcTextureFormat;
        input.textureSize = new ProcessInput.Size(width, height);
        input.cameraRotation = videoRotation;
        input.timeStamp = System.nanoTime();
        return process(input).texture;
    }

    public void selectImageQuality(BytedEffectConstants.ImageQualityType type){
        if (mImageQualityManager != null){

            mImageQualityManager.selectImageQuality(type);
        }
    }
    @Override
    public void drawFrame(int textureId, BytedEffectConstants.TextureFormat textureFormat, int srcTextureWidth, int srcTextureHeight, int cameraRotation, boolean flipHorizontal, boolean flipVertical) {
        mEffectDelegate.drawFrame(textureId, textureFormat, srcTextureWidth, srcTextureHeight, cameraRotation, flipHorizontal, flipVertical);
    }

    @Override
    public void drawFrameCenter(int textureId, BytedEffectConstants.TextureFormat textureFormat, int width, int height) {
        mEffectDelegate.drawFrameCenter(textureId, textureFormat, width, height);
    }

    @Override
    public void drawFrame(int texture) {
        mAlgorithmDelegate.drawFrame(texture);
    }

}
