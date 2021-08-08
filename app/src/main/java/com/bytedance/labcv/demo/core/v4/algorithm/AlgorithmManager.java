package com.bytedance.labcv.demo.core.v4.algorithm;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.widget.TextView;

import com.bytedance.labcv.demo.core.v4.base.ProcessInput;
import com.bytedance.labcv.demo.core.v4.base.ProcessOutput;
import com.bytedance.labcv.demo.core.v4.base.ResourceProvider;
import com.bytedance.labcv.demo.core.v4.base.Task;
import com.bytedance.labcv.demo.core.v4.base.TaskContainer;
import com.bytedance.labcv.demo.core.v4.base.task.BufferConvertTask;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKey;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKeyFactory;
import com.bytedance.labcv.demo.model.CaptureResult;
import com.bytedance.labcv.demo.utils.BitmapUtils;
import com.bytedance.labcv.effectsdk.RenderManager;
import com.bytedance.labcv.effectsdk.library.LogUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.bytedance.labcv.demo.core.v4.algorithm.AlgorithmTask.USER_SETTING;
import static com.bytedance.labcv.demo.core.v4.algorithm.task.HumanDistanceAlgorithmTask.ALGORITHM_FOV;
import static com.bytedance.labcv.demo.core.v4.algorithm.task.HumanDistanceAlgorithmTask.HUMAN_DISTANCE_FRONT;

/**
 * Created by QunZhang on 2020/7/23 15:57
 * container for algorithm tasks, use user-setting config to manager children,
 * or send user-setting params to each task through mConfig
 * contains logic of config dispatching, task generating
 */
public class AlgorithmManager extends TaskContainer<ResourceProvider, Task<ResourceProvider>> implements AlgorithmInterface {
    public static final TaskKey ALGORITHM_MANAGER = TaskKeyFactory.create("algorithmManager", true);

    private AlgorithmRender mRender;
    private List<ResultCallback<?>> mResultCallbacks;

    private int mImageWidth;
    private int mImageHeight;
    private int mSurfaceWidth;
    private int mSurfaceHeight;
    private int mFrameCounts;
    private boolean mDrawOnOriginalTexture = true;
    private boolean mUsePipeline = false;
    private boolean mDrawAlgorithmResult = true;
    private ProcessOutput mLastProcessOutput;

    public AlgorithmManager(Context context) {
        this(context, new AlgorithmResourceHelper(context), new AlgorithmRender(context));
    }

    public AlgorithmManager(Context context, ResourceProvider resourceProvider) {
        this(context, resourceProvider, new AlgorithmRender(context));
    }

    public AlgorithmManager(Context context, ResourceProvider resourceProvider, AlgorithmRender glRender) {
        super(context, resourceProvider);
        mRender = glRender;
    }

    @Override
    public int init() {
        mRender.init(mImageWidth, mImageHeight);
        LogUtils.d("Effect SDK version = " + new RenderManager().getSDKVersion());
        // initially add task BufferConvertTask and set param
//        addOnChainRebuiltListener(new OnChainRebuiltListener() {
//            @Override
//            public void onChainRebuilt() {
//                addParam(BufferConvertTask.BUFFER_PREFER_SIZE, preferSize());
//            }
//        });
        addParam(BufferConvertTask.BUFFER_CONVERT, mRender);
        return 0;
    }

    @Override
    public int destroy() {
        mRender.destroy();
        mLastProcessOutput = null;
        return super.destroy();
    }

    @Override
    public void setFov(float fov) {
        addParam(ALGORITHM_FOV, fov);
    }

    @Override
    public void setFront(boolean front) {
        addParam(HUMAN_DISTANCE_FRONT, front);
    }

    @Override
    public <T> void addResultCallback(ResultCallback<T> callback) {
        if (mResultCallbacks == null) {
            mResultCallbacks = new ArrayList<>();
        }
        mResultCallbacks.add(callback);
    }

    @Override
    public <T> void removeResultCallback(ResultCallback<T> callback) {
        if (mResultCallbacks == null) return;
        Iterator<ResultCallback<?>> iterator = mResultCallbacks.iterator();
        while (iterator.hasNext()) {
            ResultCallback<?> c = iterator.next();
            if (c == callback) {
                iterator.remove();
            }
        }
    }

    @Override
    public void setDrawAlgorithmResult(boolean drawAlgorithmResult) {
        mDrawAlgorithmResult = drawAlgorithmResult;
    }

    @Override
    public CaptureResult capture() {
        if (mRender == null) return null;
        return new CaptureResult(mRender.captureRenderResult(), mImageWidth, mImageHeight);
    }

    @Override
    public void drawFrame(int texture) {
        GLES20.glViewport(0, 0, mSurfaceWidth, mSurfaceHeight);
        mRender.onDrawFrame(texture);
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        if (width != 0 && height != 0) {
            mSurfaceHeight = height;
            mSurfaceWidth = width;
            GLES20.glViewport(0, 0, width, height);
            mRender.setViewSize(width, height);
        }
    }

    @Override
    public void adjustTextureBuffer(int orientation, boolean flipHorizontal, boolean flipVertical) {
        mRender.adjustTextureBuffer(orientation, flipHorizontal, flipVertical);
    }

    @Override
    public void setImageSize(int imageWidth, int imageHeight) {
        mImageWidth = imageWidth;
        mImageHeight = imageHeight;
        mRender.calculateVertexBuffer(mSurfaceWidth, mSurfaceHeight, mImageWidth, mImageHeight);
        mRender.init(mImageWidth, mImageHeight);
        mRender.setGlProgramWidthAndHeight(imageWidth, imageHeight);
    }

    @Override
    public float getRatio() {
        return mRender.getResizeRatio();
    }

    @Override
    public ProcessOutput process(ProcessInput input) {
        if (!hasChildren()) {
            mLastProcessOutput = super.process(input);
            return mLastProcessOutput;
        }

        mFrameCounts++;
        if (mFrameCounts == 1000000) {
            mFrameCounts = 0;
        }

        ProcessOutput output = super.process(input);
        ProcessOutput availableOutput = mUsePipeline ? mLastProcessOutput : output;

        if (output.texture == -1) {
            if (mDrawOnOriginalTexture) {
                output.texture = input.texture;
            } else {
                output.texture = mRender.prepareTexture(input.textureSize.getWidth(), input.textureSize.getHeight());
                mRender.copyTexture(input.texture, output.texture, input.textureSize.getWidth(), input.textureSize.getHeight());
            }
        }

        if (availableOutput != null) {
            if (mDrawAlgorithmResult) {
                if (availableOutput.faceInfo != null) {
                    mRender.drawFaceSegment(availableOutput.faceInfo, output.texture);
                    mRender.drawFaces(availableOutput.faceInfo, output.texture);
                }

                if (availableOutput.headSegInfo != null) {
                    mRender.drawHeadSegment(availableOutput.headSegInfo, output.texture);
                }

                if (availableOutput.handInfo != null) {
                    mRender.drawHands(availableOutput.handInfo, output.texture);
                }

                if (availableOutput.distanceInfo != null) {
                    mRender.drawHumanDist(availableOutput.distanceInfo, output.texture);
                }

                if (availableOutput.skeletonInfo != null) {
                    mRender.drawSkeleton(availableOutput.skeletonInfo, output.texture);
                }

                if (availableOutput.portraitMatting != null) {
                    mRender.drawMattingMask(availableOutput.portraitMatting, output.texture);
                }

                if (availableOutput.hairMask != null) {
                    mRender.drawHairMask(availableOutput.hairMask, output.texture);
                }

                if (availableOutput.petFaceInfo != null) {
                    mRender.drawPetFaces(availableOutput.petFaceInfo, output.texture);
                }

                if (availableOutput.gazeEstimationInfo != null) {
                    mRender.drawGazeEstimation(availableOutput.gazeEstimationInfo, output.texture);
                }
                if (availableOutput.carDetectInfo != null) {
                    mRender.drawCarInfo(availableOutput.carDetectInfo, output.texture);
                }
                if (availableOutput.skyInfo != null) {
                    mRender.drawSkyMask(availableOutput.skyInfo, output.texture);
                }
            }

            dispatchResults(availableOutput.availableResults());
        }

        mLastProcessOutput = output;

        return output;
    }

    @Override
    public TaskKey getKey() {
        return ALGORITHM_MANAGER;
    }

    private void dispatchResults(List<Object> results) {
        for (Object o : results) {
            dispatchResult(o.getClass(), o, mFrameCounts);
        }
    }

    public <T> void dispatchResult(Class<T> clz, Object o, int frameCounts) {
        if (mResultCallbacks != null) {
            for (ResultCallback<?> callback : mResultCallbacks) {
                Class<?> type = callback.getRealGenericType();
                if (type != null && type.getName().equals(clz.getName())) {
                    ((ResultCallback<T>) callback).doResult((T) o, frameCounts);
                }
            }
        }
    }

    @Override
    public void setIsDrawOnOriginalTexture(boolean onOriginalTexture) {
        mDrawOnOriginalTexture = onOriginalTexture;
    }

    @Override
    public void recoverStatus() {
        refreshConfig(true);
    }

    @Override
    public boolean setPipeline(boolean usePipeline) {
        mUsePipeline = usePipeline;
        return true;
    }

    @Override
    public void onCameraChanged() {
        mLastProcessOutput = null;
    }

    public void addAlgorithmTask(TaskKey config, boolean flag) {
        addConfig(config, flag, USER_SETTING);
    }

    public ProcessInput.Size preferSize() {
        if (children == null || children.size() == 0) return null;

        ProcessInput.Size size = null;
        for (int i = 0; i < children.size(); i++) {
            Task task = children.get(i);
            if (task instanceof AlgorithmTask) {
                if (((AlgorithmTask) task).preferBufferSize() == null) continue;
                if (size == null) {
                    size = ((AlgorithmTask) task).preferBufferSize();
                    continue;
                }
                size.merge(((AlgorithmTask) task).preferBufferSize());
            }
        }

        return size;
    }
}
