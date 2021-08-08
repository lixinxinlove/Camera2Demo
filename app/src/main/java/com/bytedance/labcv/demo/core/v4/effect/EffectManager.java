package com.bytedance.labcv.demo.core.v4.effect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLES20;
import android.text.TextUtils;

import com.bef.effectsdk.message.MessageCenter;
import com.bytedance.labcv.demo.core.v4.base.ProcessInput;
import com.bytedance.labcv.demo.core.v4.base.ProcessOutput;
import com.bytedance.labcv.demo.core.v4.base.Task;
import com.bytedance.labcv.demo.core.v4.base.TaskContainer;
import com.bytedance.labcv.demo.core.v4.base.task.TextureFormatTask;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKey;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKeyFactory;
import com.bytedance.labcv.demo.core.v4.effect.task.EffectTask;
import com.bytedance.labcv.demo.core.v4.effect.task.ImageEffectTask;
import com.bytedance.labcv.demo.core.v4.effect.task.PreviewEffectTask;
import com.bytedance.labcv.demo.core.v4.effect.task.VideoEffectTask;
import com.bytedance.labcv.demo.model.CaptureResult;
import com.bytedance.labcv.demo.model.ComposerNode;
import com.bytedance.labcv.demo.utils.AppUtils;
import com.bytedance.labcv.demo.utils.timer_record.LogTimerRecord;
import com.bytedance.labcv.effectsdk.BefFaceInfo;
import com.bytedance.labcv.effectsdk.BefHandInfo;
import com.bytedance.labcv.effectsdk.BefSkeletonInfo;
import com.bytedance.labcv.effectsdk.BytedEffectConstants;
import com.bytedance.labcv.effectsdk.RenderManager;
import com.bytedance.labcv.effectsdk.library.LogUtils;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.bytedance.labcv.effectsdk.BytedEffectConstants.BytedResultCode.BEF_RESULT_SUC;

/**
 * Created by QunZhang on 2020/7/15 10:30
 * <p>
 * contains all effect tasks
 * for user who need only effect
 */
public class EffectManager extends TaskContainer<EffectInterface.EffectResourceProvider,
        Task<EffectInterface.EffectResourceProvider>> implements EffectInterface {
    public static final TaskKey EFFECT_MANAGER = TaskKeyFactory.create("effectManager", true);

    public static final boolean USE_PIPELINE = true;

    public enum EffectType {
        PREVIEW,
        IMAGE,
        VIDEO
    }

    public static class EffectMsg {
        public EffectMsg(int msgId, long arg1, long arg2, String arg3) {
            this.msgId = msgId;
            this.arg1 = arg1;
            this.arg2 = arg2;
            this.arg3 = arg3;
        }

        public int msgId;
        public long arg1;
        public long arg2;
        public String arg3;
    }

    protected RenderManager mRenderManager;
    private volatile boolean initedEffect = false;
    protected int mImageWidth;
    protected int mImageHeight;
    protected EffectRender mEffectRender;

    private OnEffectListener mOnEffectListener;

    private String mFilterResource;
    private String[] mComposeNodes = new String[0];
    private String mStickerResource;
    private Set<SavedComposerItem> mSavedComposerNodes = new HashSet<>();
    private float mFilterIntensity = 0f;
    protected volatile boolean isEffectOn = true;
    protected Context mContext;
    private EffectType mEffectType;
    private boolean mDrawOnOriginalTexture;

    public EffectManager(Context context, EffectType effectType) {
        this(context, new EffectResourceHelper(context), effectType);
    }

    public EffectManager(Context context, EffectResourceProvider resourceProvider, EffectType effectType) {
        this(context, resourceProvider, effectType, new EffectRender());
    }

    public EffectManager(Context context, EffectResourceProvider resourceProvider, EffectType effectType, EffectRender effectRender) {
        super(context, resourceProvider);
        mContext = context;
        mRenderManager = new RenderManager();

        mEffectRender = effectRender;
        mEffectType = effectType;
    }

    private void initEffectType(EffectType type) {
        switch (type) {
            case PREVIEW:
                addTask(PreviewEffectTask.PREVIEW_EFFECT);
                break;
            case IMAGE:
                addTask(ImageEffectTask.IMAGE_EFFECT);
                break;
            case VIDEO:
                addTask(VideoEffectTask.VIDEO_EFFECT);
                break;
        }
    }

    private int initEffect(Context context) {
        LogUtils.d("Effect SDK version =" + mRenderManager.getSDKVersion());
        int ret = mRenderManager.init(context, mResourceProvider.getModelPath(), mResourceProvider.getLicensePath(), USE_PIPELINE);
        if (ret != BEF_RESULT_SUC) {
            LogUtils.e("mRenderManager.init failed!! ret =" + ret);
        }
        if (mOnEffectListener != null) {
            mOnEffectListener.onEffectInitialized();
        }
        return ret;
    }

    private void initParams() {
        addParam(TextureFormatTask.TEXTURE_FORMAT, mEffectRender);
        addParam(EffectTask.EFFECT_INTERFACE, new EffectTask.EffectInterface() {
            @Override
            public int prepareTexture(int width, int height) {
                return mEffectRender.prepareTexture(width, height);
            }

            @Override
            public void copyTexture(int srcTexture, int dstTexture, int width, int height) {
                mEffectRender.copyTexture(srcTexture, dstTexture, width, height);
            }

            @Override
            public boolean processTexture(int texture, int dstTexture, int width, int height, BytedEffectConstants.Rotation rotation, long timeStamp) {
                LogTimerRecord.RECORD("processTexture");
                boolean ret = mRenderManager.processTexture(texture, dstTexture, width, height, rotation, timeStamp);
                LogTimerRecord.STOP("processTexture");
                return ret;
            }

            @Override
            public ByteBuffer captureRenderResult(int textureId, int imageWidth, int imageHeight) {
                return mEffectRender.captureRenderResult(textureId, imageWidth, imageHeight);
            }
        });
    }

    private void initMessage() {
        MessageCenter.init();
        MessageCenter.setListener(new MessageCenter.Listener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onMessageReceived(int messageType, int arg1, int arg2, String arg3) {
                LogUtils.i(String.format("message received, type: %d, arg: %d, %d, %s", messageType, arg1, arg2, arg3));
            }
        });
    }

    @Override
    public int destroy() {
        super.destroy();
        LogUtils.d("destroyEffectSDK");
        mRenderManager.release();
        mEffectRender.release();
        initedEffect = false;
        LogUtils.d("destroyEffectSDK finish");
        return 0;
    }

    public void sendMessage(EffectMsg msg) {
        mRenderManager.sendMessage(msg.msgId, msg.arg1, msg.arg2, msg.arg3);
    }

    @Override
    public int init() {
        if (initedEffect)
            return 0;
        if (AppUtils.getAppType() != AppUtils.AppType.ALGORITHM) {
            int ret = initEffect(mContext);
            checkResult("initEffect", ret);
        }
        initEffectType(mEffectType);
        initParams();
        initMessage();
        MessageCenter.init();
        return 0;
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        if (width != 0 && height != 0) {
            mEffectRender.setViewSize(width, height);
        }
    }

    public void setOnEffectListener(OnEffectListener listener) {
        mOnEffectListener = listener;
    }

    @Override
    public boolean setFilter(String path) {
        if (!TextUtils.isEmpty(path)) {
            path = mResourceProvider.getFilterPath(path);
        }
        mFilterResource = path;
        return mRenderManager.setFilter(path);
    }

    @Override
    public boolean updateFilterIntensity(float intensity) {
        boolean result = mRenderManager.updateIntensity(BytedEffectConstants.IntensityType.Filter.getId(), intensity);
        if (result) {
            mFilterIntensity = intensity;
        }
        return result;
    }

    @Override
    public boolean setComposeNodes(String[] nodes) {
        return setComposeNodes(nodes, null);
    }

    @Override
    public boolean setComposeNodes(String[] nodes, String[] tags) {
        if (nodes.length == 0) {
            mSavedComposerNodes.clear();
        }
        String prefix = mResourceProvider.getComposePath();
        String[] path = new String[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            path[i] = prefix + nodes[i];
        }
        mComposeNodes = path;
        return mRenderManager.setComposerNodesWithTags(path, tags) == BEF_RESULT_SUC;
    }

    @Override
    public boolean updateComposeNode(ComposerNode node, boolean update) {
        throw new RuntimeException("deprecated method, use updateComposerNodeIntensity instead");
//        if (update) {
//            mSavedComposerNodes.remove(node);
//            mSavedComposerNodes.add(node);
//        }
//        String path = mResourceProvider.getComposePath() + node.getNode();
//        return mRenderManager.updateComposerNodes(path, node.getKey(), node.getValue()) == BEF_RESULT_SUC;
    }

    @Override
    public boolean updateComposerNodeIntensity(String node, String key, float intensity) {
        mSavedComposerNodes.add(new SavedComposerItem(node, key, intensity));

        String path = mResourceProvider.getComposePath() + node;
        return mRenderManager.updateComposerNodes(path, key, intensity) == BEF_RESULT_SUC;
    }

    @Override
    public boolean setSticker(String path) {
        if (!TextUtils.isEmpty(path)) {
            path = mResourceProvider.getStickerPath(path);
        }
        mStickerResource = path;
        return mRenderManager.setSticker(path);
    }

    @Override
    public boolean setStickerAbs(String absPath) {
        mStickerResource = absPath;
        return mRenderManager.setSticker(absPath);
    }

    @Override
    public boolean getAvailableFeatures(String[] features) {
        return mRenderManager.getAvailableFeatures(features);
    }

    @Override
    public BefFaceInfo getFaceDetectResult() {
        return mRenderManager.getFaceDetectResult();
    }

    @Override
    public BefFaceInfo getFaceMaskResult(BytedEffectConstants.FaceMaskType type) {
        BefFaceInfo faceInfo = new BefFaceInfo();
        mRenderManager.getFaceMaskResult(type, faceInfo);
        return faceInfo;
    }

    @Override
    public BefHandInfo getHandDetectResult() {
        return mRenderManager.getHandDetectResult();
    }

    @Override
    public BefSkeletonInfo getSkeletonDetectResult() {
        return mRenderManager.getSkeletonDetectResult();
    }

    @Override
    public boolean processTouchEvent(float x, float y) {
        return mRenderManager.processTouchEvent(x, y) == BEF_RESULT_SUC;
    }

    @Override
    public boolean processTouchEvent(BytedEffectConstants.EventCode eventCode, float x, float y, float extra) {
        return mRenderManager.processTouchEvent(eventCode, x, y, extra) == BEF_RESULT_SUC;
    }

    @Override
    public boolean setPipeline(boolean usePipeline) {
        return mRenderManager.setPipeline(usePipeline);
    }

    @Override
    public boolean set3Buffer(boolean use3Buffer) {
        return mRenderManager.set3Buffer(use3Buffer);
    }

    @Override
    public void onCameraChanged() {
        mRenderManager.cleanPipeline();
    }

    @Override
    public void setCameraPosition(boolean isFront) {
        if (null == mRenderManager) return;
        mRenderManager.setCameraPostion(isFront);
    }

    @Override
    public void setEffectOn(boolean isOn) {
        isEffectOn = isOn;
    }

    @Override
    public void recoverStatus() {
        LogUtils.e("recover status");
        if (!TextUtils.isEmpty(mFilterResource)) {
            mRenderManager.setFilter(mFilterResource);
        }
        if (!TextUtils.isEmpty(mStickerResource)) {
            mRenderManager.setSticker(mStickerResource);
        }

        if (mComposeNodes.length > 0) {
            boolean flag = mRenderManager.setComposerNodes(mComposeNodes) == BEF_RESULT_SUC;
            LogUtils.d("setComposeNodes return " + flag);

            for (SavedComposerItem item : mSavedComposerNodes) {
                updateComposerNodeIntensity(item.node, item.key, item.intensity);
            }
        }
        updateFilterIntensity(mFilterIntensity);
    }

    @Override
    public CaptureResult capture() {
        return captureImpl();
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public void setIsDrawOnOriginalTexture(boolean onOriginalTexture) {
        mDrawOnOriginalTexture = onOriginalTexture;
    }

    @Override
    public ProcessOutput process(ProcessInput input) {
        mImageWidth = input.textureSize.getWidth();
        mImageHeight = input.textureSize.getHeight();
        if (!isEffectOn) {
            ProcessOutput output = new ProcessOutput();
            output.texture = input.texture;
            if (!mDrawOnOriginalTexture) {
                int texture = mEffectRender.prepareTexture(input.textureSize.getWidth(), input.textureSize.getHeight());
                if (mEffectRender.copyTexture(input.texture, texture, input.textureSize.getWidth(), input.textureSize.getHeight())) {
                    output.texture = texture;
                }
            }
            return output;
        }

        return super.process(input);
    }

    @Override
    public TaskKey getKey() {
        return EFFECT_MANAGER;
    }

    protected CaptureResult captureImpl() {
        if (null == mEffectRender) {
            return null;
        }
        if (0 == mImageWidth * mImageHeight) {
            return null;
        }
        return new CaptureResult(mEffectRender.captureRenderResult(mImageWidth, mImageHeight), mImageWidth, mImageHeight);
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

    public void drawFrame(int textureId, BytedEffectConstants.TextureFormat textureFormat,
                          int srcTextureWidth, int srcTextureHeight, int cameraRotation,
                          boolean flipHorizontal, boolean flipVertical) {
        mEffectRender.drawFrameOnScreen(textureId, textureFormat, srcTextureWidth, srcTextureHeight, cameraRotation, flipHorizontal, flipVertical);
    }

    public void drawFrameCenter(int textureId, BytedEffectConstants.TextureFormat textureFormat, int width, int height) {
        if (!GLES20.glIsTexture(textureId)) return;
        mEffectRender.drawFrameCentnerInside(textureId, textureFormat, width, height);
    }



    private static class SavedComposerItem {
        String node;
        String key;
        float intensity;

        public SavedComposerItem(String node, String key, float value) {
            this.node = node;
            this.key = key;
            this.intensity = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SavedComposerItem that = (SavedComposerItem) o;
            return Objects.equals(node, that.node) &&
                    Objects.equals(key, that.key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(node, key);
        }
    }
}
