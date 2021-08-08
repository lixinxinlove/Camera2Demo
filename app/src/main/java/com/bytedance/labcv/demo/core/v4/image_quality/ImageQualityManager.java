package com.bytedance.labcv.demo.core.v4.image_quality;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.bytedance.labcv.demo.R;
import com.bytedance.labcv.demo.core.v4.ResourceHelper;
import com.bytedance.labcv.demo.utils.AppUtils;
import com.bytedance.labcv.demo.utils.timer_record.LogTimerRecord;
import com.bytedance.labcv.effectsdk.BytedEffectConstants;
import com.bytedance.labcv.effectsdk.BefVideoSRInfo;
import com.bytedance.labcv.effectsdk.VideoSR;
import com.bytedance.labcv.effectsdk.NightScene;


public class ImageQualityManager implements ImageQualityInterface {

    public static class ImageQualityResult{
        private  int texture;
        private int height;
        private int width;

        public ImageQualityResult(){
            texture = -1;
            height = 0;
            width = 0;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public void setTexture(int texture) {
            this.texture = texture;
        }

        public int getTexture() {
            return texture;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }
    };

    protected  VideoSR mVideoSRTask;
    protected  NightScene mNightSceneTask;

    public boolean mEnableVideoSr = false;
    public boolean mEnableNightScene = false;
    private String mRWPermissionDir;
    private BytedEffectConstants.ImageQualityType mLastSelectType;
    protected boolean mPause = false; // pause means do nothing
    protected Context mContext;
    protected ResourceHelper mResourceProvider;


    public ImageQualityManager (Context context, ResourceHelper provider){
        mContext = context;
        mResourceProvider = provider;
        mLastSelectType = BytedEffectConstants.ImageQualityType.IMAGE_QUALITY_TYPE_NONE;
    }
    public int init(String dir){
        mRWPermissionDir = dir;
        return 0;
    }

    public int destory(){
        if(mNightSceneTask != null){
            mNightSceneTask.release();
            mNightSceneTask = null;
        }

        if(mVideoSRTask != null){
            mVideoSRTask.release();
            mVideoSRTask = null;
        }

        return BytedEffectConstants.BytedResultCode.BEF_RESULT_SUC;
    }

    public void selectImageQuality(BytedEffectConstants.ImageQualityType type){
        setImageQuality(mLastSelectType, false);
        mLastSelectType = type;



        // 判断是否支持视频超分
        if (type == BytedEffectConstants.ImageQualityType.IMAGE_QUALITY_TYPE_VIDEO_SR) {

            if (AppUtils.isPixelSeriesDevices()) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, R.string.video_sr_pixel_not_support, Toast.LENGTH_SHORT).show();
                    }
                });
                return ;
            }

            if (!AppUtils.isSupportVideoSR(mContext)) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, R.string.video_sr_not_support, Toast.LENGTH_SHORT).show();
                    }
                });

                return ;
            }


        }

        setImageQuality(type, true);
    }

    private boolean setImageQuality(BytedEffectConstants.ImageQualityType type, boolean open)
    {
        // 打开或者关闭夜景增强
        if (type == BytedEffectConstants.ImageQualityType.IMAGE_QUALITY_TYPE_NIGHT_SCENE){
            mEnableNightScene = open;

            if (open){
                if (mNightSceneTask == null){
                    mNightSceneTask = new NightScene();
                    int ret = mNightSceneTask.init(mResourceProvider.getLicensePath());

                    if (ret != BytedEffectConstants.BytedResultCode.BEF_RESULT_SUC){
                        mNightSceneTask.release();
                        mNightSceneTask = null;
                    }
                }
            }else {
                if(mNightSceneTask != null){
                    mNightSceneTask.release();
                    mNightSceneTask = null;
                }
            }
        }
        // 打开或者关闭视频超分
        else if (type == BytedEffectConstants.ImageQualityType.IMAGE_QUALITY_TYPE_VIDEO_SR){
            mEnableVideoSr = open;
            if (open){
                if (mVideoSRTask == null){
                    mVideoSRTask = new VideoSR();
                    int ret = mVideoSRTask.init(mRWPermissionDir, mResourceProvider.getLicensePath());

                    if (ret != BytedEffectConstants.BytedResultCode.BEF_RESULT_SUC){
                        mVideoSRTask.release();
                        mVideoSRTask = null;
                    }
                }
            } else {
                if(mVideoSRTask != null){
                    mVideoSRTask.release();
                    mVideoSRTask = null;
                }
            }
        }

        return true;
    }

    public int processTexture(int srcTextureId,  BytedEffectConstants.TextureFormat textureFormat,
                       int srcTextureWidth, int srcTextureHeight, ImageQualityResult result)
    {
        // If pause, just return src result
        if (mPause){
            result.texture = srcTextureId;
            result.width = srcTextureWidth;
            result.height = srcTextureHeight;
            return 0;
        }

        if (mEnableVideoSr){
            if (mVideoSRTask != null){

                // This step to judge if the resolution larger than 720p, if true, we release the task, and disable it
                {
                    if ((srcTextureWidth * srcTextureHeight) > 1280 * 720){
                        mEnableVideoSr = false;
                        mVideoSRTask.release();
                        mVideoSRTask = null;

                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, R.string.video_sr_resolution_not_support, Toast.LENGTH_SHORT).show();
                            }
                        });
                        return 0;
                    }
                }

                LogTimerRecord.RECORD("video_sr");
                BefVideoSRInfo videoSrResult = mVideoSRTask.process(srcTextureId, srcTextureWidth, srcTextureHeight);
                if (videoSrResult == null){
                    return BytedEffectConstants.BytedResultCode.BEF_RESULT_FAIL;
                }
                LogTimerRecord.STOP("video_sr");

                result.height  = srcTextureHeight * 2;
                result.width   = srcTextureWidth * 2;
                result.texture = videoSrResult.getDestTextureId();

                return BytedEffectConstants.BytedResultCode.BEF_RESULT_SUC;
            }

        }else if (mEnableNightScene){
            if (mNightSceneTask != null){
                Integer destTextureId = new Integer(0);
                LogTimerRecord.RECORD("night_scene");
                int ret = mNightSceneTask.process(srcTextureId, destTextureId, srcTextureWidth, srcTextureHeight);
                if (ret != BytedEffectConstants.BytedResultCode.BEF_RESULT_SUC){
                    return ret;
                }
                LogTimerRecord.STOP("night_scene");
                result.height  = srcTextureHeight;
                result.width   = srcTextureWidth;
                result.texture = destTextureId.intValue();
                return ret;
            }
        }
        return BytedEffectConstants.BytedResultCode.BEF_RESULT_SUC;
    }

    public void recoverStatus(){
        if(mEnableNightScene){
            mNightSceneTask = new NightScene();
            mNightSceneTask.init(mResourceProvider.getLicensePath());
        }

        if (mEnableVideoSr){
            mVideoSRTask = new VideoSR();
            mVideoSRTask.init(mRWPermissionDir, mResourceProvider.getLicensePath());
        }
    }

    public void setPause(boolean pause) {
        this.mPause = pause;
    }
}
