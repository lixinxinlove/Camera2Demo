package com.bytedance.labcv.demo.core.v4.algorithm;

import android.content.Context;

import com.bytedance.labcv.demo.core.v4.base.ResourceProvider;

import java.io.File;

/**
 * Created by QunZhang on 2020/7/30 14:25
 */
public class AlgorithmResourceHelper implements ResourceProvider,
        AlgorithmTask.AlgorithmResourceProvider {
    public static final String RESOURCE = "resource";
    public static final String FACE = "ttfacemodel/tt_face_v10.0.model";
    public static final String PETFACE = "ttpetface/tt_petface_v3.0.model";
    public static final String HAND_DETECT = "handmodel/tt_hand_det_v10.0.model";
    public static final String HAND_BOX = "handmodel/tt_hand_box_reg_v11.0.model";
    public static final String HAND_GESTURE = "handmodel/tt_hand_gesture_v10.0.model";
    public static final String HAND_KEY_POINT = "handmodel/tt_hand_kp_v6.0.model";
    public static final String HAND_SEGMENT = "handmodel/tt_hand_seg_v2.0.model";
    public static final String FACEEXTA = "ttfacemodel/tt_face_extra_v12.0.model";
    public static final String FACEATTRI = "ttfaceattrmodel/tt_face_attribute_v7.0.model";
    public static final String FACEVERIFY = "ttfaceverify/tt_faceverify_v6.0.model";
    public static final String SKELETON = "skeleton_model/tt_skeleton_v7.0.model";
    public static final String PORTRAITMATTING = "mattingmodel/tt_matting_v12.0.model";
    public static final String HEADSEGMENT = "headsegmodel/tt_headseg_v6.0.model";
    public static final String HAIRPARSING = "hairparser/tt_hair_v10.0.model";
    public static final String LIGHTCLS = "lightcls/tt_lightcls_v1.0.model";
    public static final String HUMANDIST = "humandistance/tt_humandist_v1.0.model";
    public static final String GENERAL_OBJECT_DETECT = "generalobjectmodel/tt_general_obj_detection_v1.0.model";
    public static final String GENERAL_OBJECT_CLS = "generalobjectmodel/tt_general_obj_detection_cls_v1.0.model";
    public static final String GENERAL_OBJECT_TRACK = "generalobjectmodel/tt_sample_v1.0.model";
    public static final String C1 = "c1/tt_c1_small_v7.0.model";
    public static final String C2 = "c2/tt_C2Cls_v5.0.model";
    public static final String VIDEO_CLS = "video_cls/tt_videoCls_v4.0.model";
    public static final String GAZE_ESTIMAION = "gazeestimation/tt_gaze_v3.0.model";

    public static final String CAR_DETECT = "cardamanagedetect/tt_car_damage_detect_v2.0.model";
    public static final String CAR_BRAND_DETECT = "cardamanagedetect/tt_car_landmarks_v3.0.model";
    public static final String CAR_BRAND_OCR = "cardamanagedetect/tt_car_plate_ocr_v2.0.model";
    public static final String CAR_TRACK = "cardamanagedetect/tt_car_track_v2.0.model";

    public static final String STUDENT_ID_OCR = "student_id_ocr/tt_student_id_ocr_v2.0.model";
    public static final String SKY_SEGMENT= "skysegmodel/tt_skyseg_v7.0.model";
    public static final String ANIMOJI_MODEL = "animoji/animoji_v5.0.model";

    public static final String LICENSE_NAME = "labcv_test_20210225_20210831_com.bytedance.labcv.demo_v4.0.2.4.licbag";

    private Context mContext;

    public AlgorithmResourceHelper(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public String getLicensePath() {
        return new File(new File(getResourcePath(), "LicenseBag.bundle"), LICENSE_NAME).getAbsolutePath();
    }

    private String getResourcePath() {
        return mContext.getExternalFilesDir("assets").getAbsolutePath() + File.separator + RESOURCE;
    }

    @Override
    public String getModelPath(String modelName) {
        return new File(new File(getResourcePath(), "ModelResource.bundle"), modelName).getAbsolutePath();
    }
}
