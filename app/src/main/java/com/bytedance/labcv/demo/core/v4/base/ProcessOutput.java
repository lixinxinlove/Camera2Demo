package com.bytedance.labcv.demo.core.v4.base;

import com.bytedance.labcv.demo.core.v4.algorithm.task.ConcentrationTask;
import com.bytedance.labcv.demo.core.v4.base.task.BufferCaptureTask;
import com.bytedance.labcv.effectsdk.BefAnimojiInfo;
import com.bytedance.labcv.effectsdk.BefC1Info;
import com.bytedance.labcv.effectsdk.BefC2Info;
import com.bytedance.labcv.effectsdk.BefCarDetectInfo;
import com.bytedance.labcv.effectsdk.BefDistanceInfo;
import com.bytedance.labcv.effectsdk.BefDynamicActionInfo;
import com.bytedance.labcv.effectsdk.BefFaceInfo;
import com.bytedance.labcv.effectsdk.BefGazeEstimationInfo;
import com.bytedance.labcv.effectsdk.BefHandInfo;
import com.bytedance.labcv.effectsdk.BefHeadSegInfo;
import com.bytedance.labcv.effectsdk.BefLightclsInfo;
import com.bytedance.labcv.effectsdk.BefPetFaceInfo;
import com.bytedance.labcv.effectsdk.BefSkeletonInfo;
import com.bytedance.labcv.effectsdk.BefSkyInfo;
import com.bytedance.labcv.effectsdk.BefStudentIdOcrInfo;
import com.bytedance.labcv.effectsdk.BefVideoClsInfo;
import com.bytedance.labcv.effectsdk.HairParser;
import com.bytedance.labcv.effectsdk.PortraitMatting;
import com.bytedance.labcv.effectsdk.SkySegment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QunZhang on 2020/7/14 20:55
 */
public class ProcessOutput {
    public int texture = -1;

    public BefFaceInfo faceInfo;
    public BefHandInfo handInfo;
    public BefSkeletonInfo skeletonInfo;
    public BefHeadSegInfo headSegInfo;
    public BefDistanceInfo distanceInfo;
    public BefPetFaceInfo petFaceInfo;
    public HairParser.HairMask hairMask;
    public PortraitMatting.MattingMask portraitMatting;
    public BefLightclsInfo lightclsInfo;
    public BefDynamicActionInfo dynamicActionInfo;
    public BufferCaptureTask.BufferCaptureTaskResult bufferCaptureTaskResult;
    public BefC1Info c1Info;
    public BefC2Info c2Info;
    public BefVideoClsInfo videoClsInfo;
    public ConcentrationTask.BefConcentrationInfo concentrationInfo;
    public BefGazeEstimationInfo gazeEstimationInfo;
    public BefCarDetectInfo carDetectInfo;
    public BefStudentIdOcrInfo studentIdOcrInfo;
    public BefSkyInfo skyInfo;
    public BefAnimojiInfo animojiInfo;

    public List<Object> availableResults() {
        List<Object> results = new ArrayList<>();
        if (faceInfo != null) {
            results.add(faceInfo);
        }
        if (handInfo != null) {
            results.add(handInfo);
        }
        if (skeletonInfo != null) {
            results.add(skeletonInfo);
        }
        if (headSegInfo != null) {
            results.add(headSegInfo);
        }
        if (distanceInfo != null) {
            results.add(distanceInfo);
        }
        if (petFaceInfo != null) {
            results.add(petFaceInfo);
        }
        if (hairMask != null) {
            results.add(hairMask);
        }
        if (portraitMatting != null) {
            results.add(portraitMatting);
        }
        if (lightclsInfo != null) {
            results.add(lightclsInfo);
        }
        if (dynamicActionInfo != null) {
            results.add(dynamicActionInfo);
        }
        if (bufferCaptureTaskResult != null) {
            results.add(bufferCaptureTaskResult);
        }
        if (c1Info != null) {
            results.add(c1Info);
        }
        if (c2Info != null) {
            results.add(c2Info);
        }
        if (videoClsInfo != null) {
            results.add(videoClsInfo);
        }
        if (concentrationInfo != null) {
            results.add(concentrationInfo);
        }
        if (gazeEstimationInfo != null) {
            results.add(gazeEstimationInfo);
        }

        if (carDetectInfo != null) {
            results.add(carDetectInfo);
        }

        if (studentIdOcrInfo != null) {
            results.add(studentIdOcrInfo);
        }

        if (skyInfo != null) {
            results.add(skyInfo);
        }

        if (animojiInfo != null) {
            results.add(animojiInfo);
        }

        return results;
    }
}
