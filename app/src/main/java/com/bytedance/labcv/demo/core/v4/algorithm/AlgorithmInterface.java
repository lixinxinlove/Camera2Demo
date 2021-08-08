package com.bytedance.labcv.demo.core.v4.algorithm;

import com.bytedance.labcv.demo.core.v4.algorithm.task.C1AlgorithmTask;
import com.bytedance.labcv.demo.core.v4.algorithm.task.C2AlgorithmTask;
import com.bytedance.labcv.demo.core.v4.algorithm.task.DynamicActionAlgorithmTask;
import com.bytedance.labcv.demo.core.v4.algorithm.task.FaceAlgorithmTask;
import com.bytedance.labcv.demo.core.v4.algorithm.task.HairParserAlgorithmTask;
import com.bytedance.labcv.demo.core.v4.algorithm.task.HandAlgorithmTask;
import com.bytedance.labcv.demo.core.v4.algorithm.task.HeadSegmentAlgorithmTask;
import com.bytedance.labcv.demo.core.v4.algorithm.task.HumanDistanceAlgorithmTask;
import com.bytedance.labcv.demo.core.v4.algorithm.task.LightClsAlgorithmTask;
import com.bytedance.labcv.demo.core.v4.algorithm.task.PetFaceAlgorithmTask;
import com.bytedance.labcv.demo.core.v4.algorithm.task.PortraitMattingAlgorithmTask;
import com.bytedance.labcv.demo.core.v4.algorithm.task.SkeletonAlgorithmTask;
import com.bytedance.labcv.demo.core.v4.algorithm.task.SkySegmentAlgorithmTask;
import com.bytedance.labcv.demo.core.v4.algorithm.task.VideoClsAlgorithmTask;
import com.bytedance.labcv.demo.core.v4.base.task.BufferCaptureTask;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKey;
import com.bytedance.labcv.demo.model.CaptureResult;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by QunZhang on 2020/7/31 10:32
 */
public interface AlgorithmInterface extends
        FaceAlgorithmTask.FaceAlgorithmInterface,
        HandAlgorithmTask.HandAlgorithmInterface,
        PetFaceAlgorithmTask.PetFaceAlgorithmInterface,
        SkeletonAlgorithmTask.SkeletonAlgorithmInterface,
        HeadSegmentAlgorithmTask.HeadSegmentAlgorithmTaskInterface,
        PortraitMattingAlgorithmTask.PortraitMattingAlgorithmInterface,
        HairParserAlgorithmTask.HairParserAlgorithmInterface,
        LightClsAlgorithmTask.LightClsAlgorithmInterface,
        DynamicActionAlgorithmTask.DynamicActionInterface,
        BufferCaptureTask.BufferCaptureInterface,
        C1AlgorithmTask.C1AlgorithmInterface,
        C2AlgorithmTask.C2AlgorithmInterface,
        VideoClsAlgorithmTask.VideoClsAlgorithmInterface,
        HumanDistanceAlgorithmTask.HumanDistanceAlgorithmInterface,
        SkySegmentAlgorithmTask.SkySegmentAlgorithmInterface{
    /**
     * @brief 初始化 Manager 类
     * initialize Manger class
     */
    int init();

    /**
     * @brief 销毁 Manager 类
     * destroy Manager class
     */
    int destroy();

    /**
     * @brief 添加/移除算法
     * add/remove algorithm
     * @param config 算法对应的 TaskKey，每一个算法都有一个对应的 key
     *               TaskKey of algorithm, each algorithm has a specific key
     * @param flag true 表示添加算法，false 表示移除算法
     *             add algorihtm if true, remove if false
     */
    void addAlgorithmTask(TaskKey config, boolean flag);

    /**
     * @brief 添加参数
     * add algorithm parameter
     * @param key 参数对应的 TaskKey，每一个参数都有一个对应的 key
     *            TaskKey of parameter, each param has a specific key
     * @param value 参数值
     *              value
     */
    void addParam(TaskKey key, Object value);

    /**
     * @brief 添加算法回调
     * add algorithm result callback
     * @details 每一个算法都有一种特定类型的返回值，即这里的 T，
     * SDK 内部会根据 T 的类型决定这个 callback 接收哪个算法的结果
     * every algorithm has one specific type of result T,
     * SDK will dispatch algorithm results whose type is T to
     * this callback
     * @param callback 算法结果回调
     *                 callback of result
     * @param <T> 接收的结果类型
     *           type of result callback wants receive
     */
    <T> void addResultCallback(ResultCallback<T> callback);

    /**
     * @brief 移除算法回调
     * remove algorithm result callback
     * @details 每一个算法都有一种特定类型的返回值，即这里的 T，
     * SDK 内部会根据 T 的类型决定这个 callback 接收哪个算法的结果
     * every algorithm has one specific type of result T,
     * SDK will dispatch algorithm results whose type is T to
     * this callback
     * @param callback 算法结果回调
     *                 callback of result
     * @param <T> 接收的结果类型
     *           type of result callback wants receive
     */
    <T> void removeResultCallback(ResultCallback<T> callback);

    /**
     * @brief 是否绘制算法结果
     * whether draw result
     * @details SDK 内部会默认将算法的检测结果绘制到输入纹理中，比如人脸关键点等，
     * 此函数可控制是否绘制
     * SDK will draw algorithm results to input texture default,
     * such as face key points
     * @param drawAlgorithmResult 是否绘制算法结果
     *                            whether draw algorithm result
     */
    void setDrawAlgorithmResult(boolean drawAlgorithmResult);

    /**
     * @brief 截取当前帧
     * capture current frame
     */
    CaptureResult capture();

    /**
     * @brief 绘制纹理到屏幕
     * draw texture to screen
     * @param texture 纹理 ID
     *                texture ID
     */
    void drawFrame(int texture);

    /**
     * @brief 设置 surface 尺寸
     * set surface size
     * @param width width
     * @param height height
     */
    void onSurfaceChanged(int width, int height);

    /**
     * @brief 设置输入图像信息
     * set input image information
     * @details 设置纹理的旋转角度、是否需要翻转等信息
     * set rotation, flip information of texture
     * @param orientation 纹理旋转角度
     *                    orientation of texture
     * @param flipHorizontal 是否水平翻转
     *                       whether do horizontal flip
     * @param flipVertical 是否垂直翻转
     *                     whether do vertical flip
     */
    void adjustTextureBuffer(int orientation, boolean flipHorizontal, boolean flipVertical);

    /**
     * @brief 设置输入图像尺寸
     * set input image size
     * @param imageWidth width
     * @param imageHeight height
     */
    void setImageSize(int imageWidth, int imageHeight);

    /**
     * @brief 获取图像缩放倍数
     * get scale ratio
     */
    float getRatio();

    /**
     * @brief 手动分发检测结果
     * dispatch algorithm result manually
     * @param clz 检测结果类型
     *            type of result
     * @param o 检测结果
     *          result
     * @param frameCounts 无用
     *                    no use
     * @param <T> 结果类型
     *           type of result
     *
     */
    <T> void dispatchResult(Class<T> clz, Object o, int frameCounts);

    /**
     * @brief 是否返回原始纹理
     * whether return original texture
     * @details 当设置 setEffectOn false 跳过 SDK 处理时，
     * 默认返回输入纹理，如果设置了 onOriginalTexture 为 true，会将输入纹理拷贝一份返回
     * when we set setEffectOn to false to skip SDK process,
     * input texture will be returned default,
     * if set setIsDrawOnOriginalTexture to true, SDK will copy input texture
     * to a new texture and return
     * @param onOriginalTexture 是否返回原始纹理
     */
    void setIsDrawOnOriginalTexture(boolean onOriginalTexture);

    /**
     * @brief 恢复状态
     * recover state
     * @details 当调用 destroy 和 init 之后，SDK 实例会重新创建，之前设置的特效会消失，
     * 此时可用此函数，将之前设置的特效恢复
     * after called destroy and init, SDK instance will recreate, and the effects we set
     * will be removed, we can use it to recover effect
     */
    void recoverStatus();

    /**
     * @brief 是否开启并行渲染
     * whether use pipeline
     * @details 特效 SDK 内部工作分为两部分，算法检测和特效渲染，当开启并行渲染之后，
     * 算法检测和特效渲染将在不同线程执行，以充分利用多线程进行加速，
     * 但会导致渲染效果延迟一帧
     * there are two parts of effect SDK, algorithm detector and effect render,
     * when usePipeline is true, they will work in different thread to accelerate,
     * with one frame delay
     * @param usePipeline 是否开启并行渲染
     *                    whether use pipeline
     */
    boolean setPipeline(boolean usePipeline);

    /**
     * @brief 相机切换回调
     * called when camera changed
     */
    void onCameraChanged();

    abstract class ResultCallback<T> {
        protected abstract void doResult(T t, int framecount);

        // 使用反射得到T的真实类型
        // Use reflection to get the real type of T
        public Class<T> getRealGenericType() {
            ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
            Type[] types = pt.getActualTypeArguments();
            if (types.length > 0) {
                return (Class<T>) pt.getActualTypeArguments()[0];
            }
            return null;
        }
    }
}
