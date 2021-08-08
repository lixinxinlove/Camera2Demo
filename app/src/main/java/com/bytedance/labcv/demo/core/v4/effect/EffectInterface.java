package com.bytedance.labcv.demo.core.v4.effect;

import com.bytedance.labcv.demo.core.v4.base.ResourceProvider;
import com.bytedance.labcv.demo.model.CaptureResult;
import com.bytedance.labcv.demo.model.ComposerNode;
import com.bytedance.labcv.effectsdk.BefFaceInfo;
import com.bytedance.labcv.effectsdk.BefHandInfo;
import com.bytedance.labcv.effectsdk.BefSkeletonInfo;
import com.bytedance.labcv.effectsdk.BytedEffectConstants;

/**
 * Created by QunZhang on 2020/7/30 21:08
 */
public interface EffectInterface {
    /**
     * @brief 销毁特效 SDK，确保在 gl 线程中执行
     * destroy SDK，must run in gl thread
     */
    int destroy();

    /**
     * @brief 初始化特效SDK，确保在 gl 线程中执行
     * initialize SDK, must run in gl thread
     */
    int init();

    /**
     * @brief 设置 surface 尺寸
     * reset surface size
     * @param width width
     * @param height height
     */
    void onSurfaceChanged(int width, int height);

    /**
     * @brief 设置特效监听回调接口
     * set listener
     * @param listener 监听函数
     *                 listener
     */
    void setOnEffectListener(OnEffectListener listener);

    /**
     * @brief 设置滤镜路径
     * set filter path
     * @details 相对 FilterResource.bundle/Filter 路径，为 null 时关闭滤镜
     *          relative path to FilterResource.bundle/Filter, close filter if null
     * @param path 相对路径
     *             relative path
     */
    boolean setFilter(String path);

    /**
     * @brief 设置滤镜强度
     * set filter intensity
     * @param intensity 滤镜强度，0-1
     *                  filter intensity, from 0 to 1
     */
    boolean updateFilterIntensity(float intensity);

    /**
     * @brief 设置组合特效
     * set composer effects
     * @details 设置 ComposeMakeup.bundle 下的所有功能，包含美颜、美形、美体、美妆等
     * set effects below ComposeMakeup.bundle, includes beauty, reshape, body and makeup ect.
     * @param nodes 特效素材相对 ComposeMakeup.bundle/ComposeMakeup 的路径
     *              effect resource relative path to ComposeMakeup.bundle/ComposeMakeup
     */
    boolean setComposeNodes(String[] nodes);

    boolean setComposeNodes(String[] nodes, String[] tags);

    /**
     * @brief 更新组合特效中某个功能的强度
     * update intensity of a feature in effect
     * @param node 特效素材对应的 ComposerNode，包含素材路径、功能 key 和功能强度
     *             the ComposerNode object, contains relative path, key of feature and intensity
     * @param update 是否更新节点，默认为 true
     *               whether update node, default is true
     * @deprecated use updateComposerNodeIntensity replace
     */
    boolean updateComposeNode(ComposerNode node, boolean update);

    boolean updateComposerNodeIntensity(String node, String key, float intensity);

    /**
     * @brief 设置贴纸路径
     * set sticker path
     * @details 贴纸素材的文件路径，相对 StickerResource.bundle 路径，为 null 时为关闭贴纸
     * relative path of sticker resource to StickerResource.bundle, close sticker if null
     * @param path 贴纸路径
     *             relative path of sticker
     */
    boolean setSticker(String path);

    /**
     * @brief 设置贴纸的绝对路径
     * set absolute path of sticker
     * @details 贴纸素材的文件路径，在 SD 卡上的绝对路径，为 null 是为关闭贴纸
     * absolute path of sticker in SD card, close sticker if null
     * @param absPath 贴纸路径
     *                sticker path
     */
    boolean setStickerAbs(String absPath);

    /**
     * @brief 获取 SDK 支持的功能，一般为测试用
     * get available features in SDK, just for test
     * @param features 功能数组，外部分配大小
     *                 features array, allocate in outer
     */
    boolean getAvailableFeatures(String[] features);

    /**
     * @brief 获取特效 SDK 中的人脸检测结果
     * get face result in effect SDK
     * @return 人脸检测结果 face result
     */
    BefFaceInfo getFaceDetectResult();

    /**
     * @brief 获取特效 SDK 中的手势检测结果
     * get hand result in effect SDK
     * @return 手势检测结果 hand result
     */
    BefHandInfo getHandDetectResult();

    /**
     * @brief 获取特效 SDK 中的人体检测结果
     * get skeleton result in effect SDK
     * @return 人体检测结果 skeleton result
     */
    BefSkeletonInfo getSkeletonDetectResult();

    /**
     * @brief 获取特效 SDK 中的人脸 mask 结果
     * get face mask result in effect SDK
     * @param type mask 类型，具体查看 BytedEffectConstants.FaceMaskType
     *             type of mask, see more in BytedEffectConstants.FaceMaskType
     * @return 人脸检测结果 face mask result
     */
    BefFaceInfo getFaceMaskResult(BytedEffectConstants.FaceMaskType type);

    /**
     * @brief 处理单击事件
     * process click event
     * @param x x，0-1
     * @param y y，0-1
     */
    boolean processTouchEvent(float x, float y);

    /**
     * @brief 处理触摸事件
     * process touch event, can receive multi-type event
     * @param eventCode 事件类型，不同的 eventCode 值对应着不同的事件
     *                  type of event
     * @param x x，0-1
     * @param y y，0-1
     * @param extra 如果是旋转、缩放等事件，extra 表示为旋转角度、缩放倍数等
     *              if event code is rotate or scale, extra presents rotate angle or scale time
     */
    boolean processTouchEvent(BytedEffectConstants.EventCode eventCode, float x, float y, float extra);

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
     * @brief 是否开启 3-buffer
     * whether use 3-buffer
     * @details 当开启并行渲染之后，由于算法和特效在不同线程执行，所以需要一些线程同步的工作。
     * 当不开启 3buffer 的时候，SDK 会将传进来的每一帧进行拷贝，
     * 当开启 3buffer 的时候，SDK 不会拷贝每一帧纹理，要求外部传进来的纹理是一个循环的队列，
     * 即连续的 3 帧纹理 ID 不能相同
     * when we use pipeline, we should do something for thread safe,
     * if we use 3buffer, SDK will copy every input frame,
     * otherwise, SDK will not copy it, but we should confirm every 3 continuous
     * texture ID not same
     * @param use3Buffer 是否开启 3buffer
     *                   whether use 3buffer
     */
    boolean set3Buffer(boolean use3Buffer);

    /**
     * @brief 相机切换回调
     * called when camera changed
     */
    void onCameraChanged();

    /**
     * @brief 设置相机位置
     * set camera position
     * @param isFront 是否为前置摄像头
     *                whether it is front camera
     */
    void setCameraPosition(boolean isFront);

    /**
     * @brief 设置是否执行特效渲染
     * set effect on
     * @param isOn 是否执行特效渲染
     *             whether open effect
     */
    void setEffectOn(boolean isOn);

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
     * @brief 截取当前帧
     * capture current frame
     */
    CaptureResult capture();

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
     * @brief 处理纹理
     * process texture
     * @details 此函数可用于处理 OES/2D 纹理，且会根据传入的相机角度将输入纹理旋转，
     * 再输入到 SDK 中，并会将输出纹理再根据相机角度旋转到跟输入纹理相同的角度
     * this method can receive OES/2D texture, and will rotate input texture
     * according to cameraRotation then send to SDK, and will rotate output texture
     * to a rotation the same with input texture
     * @param srcTextureId 纹理 ID
     *                     texture ID
     * @param srcTextureFormat 纹理类型 OES/2D
     *                         texture type, OES/2D
     * @param srcTextureWidth width
     * @param srcTextureHeight height
     * @param cameraRotation camera rotation
     * @param frontCamera whether it is front camera
     * @param sensorRotation phone rotation
     * @param timestamp timeStamp
     * @return 输出 2D 纹理 output texture
     */
    int processTexture(int srcTextureId, BytedEffectConstants.TextureFormat srcTextureFormat,
                   int srcTextureWidth, int srcTextureHeight, int cameraRotation,
                   boolean frontCamera, BytedEffectConstants.Rotation sensorRotation,
                   long timestamp);

    /**
     * @brief 处理纹理
     * process texture
     * @details 此函数只可用于处理 2D 纹理，且需要保证纹理是无旋转的
     * this method only receive 2D texture, and we should confirm texture is not rotated
     * @param srcTextureId 纹理 ID
     *                     texture ID
     * @param width width
     * @param height height
     * @return 输出 2D 纹理 output texture
     */
    int processImageTexture(int srcTextureId, int width, int height);

    /**
     * @brief 处理纹理
     * process texture
     * @details 此函数可用于处理 2D/OES 纹理，且会根据传入的 videoRotation 对纹理进行旋转，
     * 但与 processTexture 不同的是，此函数不会对输出纹理做旋转
     * this method can receive OES/2D texture, and will rotate input texture
     * according to videoRotation. different with processTexture, this method
     * will not rotate output texture
     * @param srcTextureId 纹理 ID
     *                     texture ID
     * @param srcTextureFormat 纹理类型 OES/2D
     *                         texture type, OES/2D
     * @param width width
     * @param height height
     * @param videoRotation 纹理旋转角度
     *                      rotation of texture
     * @return 输出 2D 纹理 output texture
     */
    int processVideoTexture(int srcTextureId, BytedEffectConstants.TextureFormat srcTextureFormat, int width, int height, int videoRotation);

    /**
     * @brief 绘制纹理到屏幕
     * draw texture to screen
     * @details 此函数会以铺满的方式绘制纹理，且会根据相机角度、翻转信息对纹理进行变换
     * this method will draw texture to screen in cover mode,
     * and will rotate texture according to cameraRotation, flipHorizontal and flipVertical
     * @param textureId 纹理 ID
     *                  texture ID
     * @param textureFormat 纹理类型，OES/2D
     *                      texture type, OES/2D
     * @param srcTextureWidth width
     * @param srcTextureHeight height
     * @param cameraRotation 相机角度
     *                       camera rotation
     * @param flipHorizontal 是否水平翻转
     *                       whether do horizontal flip
     * @param flipVertical 是否垂直翻转
     *                     whether do vertical flip
     */
    void drawFrame(int textureId, BytedEffectConstants.TextureFormat textureFormat,
                   int srcTextureWidth, int srcTextureHeight, int cameraRotation,
                   boolean flipHorizontal, boolean flipVertical);

    /**
     * @brief 绘制纹理到屏幕
     * draw texture to screen
     * @details 此函数会以居中的方式绘制纹理
     * this method will draw texture to screen in center fit mode
     * @param textureId 纹理 ID
     *                  texture ID
     * @param textureFormat 纹理类型，OES/2D
     *                      texture type, OES/2D
     * @param width width
     * @param height height
     */
    void drawFrameCenter(int textureId, BytedEffectConstants.TextureFormat textureFormat, int width, int height);

    interface OnEffectListener {
        void onEffectInitialized();
    }

    interface EffectResourceProvider extends ResourceProvider {
        String getModelPath();
        String getComposePath();
        String getFilterPath();
        String getFilterPath(String filter);
        String getStickerPath(String sticker);
    }
}
