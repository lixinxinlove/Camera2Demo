package com.bytedance.labcv.demo.presenter;

import com.bytedance.labcv.demo.R;
import com.bytedance.labcv.demo.model.ComposerNode;
import com.bytedance.labcv.demo.model.EffectButtonItem;
import com.bytedance.labcv.demo.presenter.contract.ItemGetContract;
import com.bytedance.labcv.demo.ui.BaseEffectActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.MASK;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.NODE_ALL_SLIM;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.NODE_BEAUTY_4ITEMS;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.NODE_BEAUTY_CAMERA;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.NODE_BEAUTY_LIVE;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.NODE_BEAUTY_SURGERY;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.NODE_RESHAPE_CAMERA;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.NODE_RESHAPE_LIVE;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.SUB_MASK;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_BODY;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_BODY_ENHANCE_HIP;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_BODY_ENHANCE_NECK;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_BODY_ENLARGE_BREAST;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_BODY_LONG_LEG;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_BODY_SHRINK_HEAD;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_BODY_SLIM_ARM;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_BODY_SLIM_LEG;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_BODY_SLIM_WAIST;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_BODY_THIN;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_FACE;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_FACE_SHARPEN;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_FACE_SMOOTH;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_FACE_WHITEN;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_RESHAPE;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_RESHAPE_BRIGHTEN_EYE;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_RESHAPE_CHEEK;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_RESHAPE_CHIN;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_RESHAPE_EYE;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_RESHAPE_EYE_MOVE;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_RESHAPE_EYE_PLUMP;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_RESHAPE_EYE_ROTATE;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_RESHAPE_EYE_SPACING;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_RESHAPE_FACE_CUT;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_RESHAPE_FACE_OVERALL;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_RESHAPE_FACE_SMALL;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_RESHAPE_FOREHEAD;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_RESHAPE_JAW;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_RESHAPE_MOUTH_MOVE;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_RESHAPE_MOUTH_SMILE;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_RESHAPE_MOUTH_ZOOM;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_RESHAPE_NOSE_LEAN;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_RESHAPE_NOSE_LONG;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_RESHAPE_REMOVE_POUCH;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_RESHAPE_SINGLE_TO_DOUBLE_EYELID;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_RESHAPE_SMILE_FOLDS;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_RESHAPE_WHITEN_TEETH;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_CLOSE;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_MAKEUP;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_MAKEUP_BLUSHER;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_MAKEUP_EYEBROW;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_MAKEUP_EYESHADOW;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_MAKEUP_FACIAL;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_MAKEUP_HAIR;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_MAKEUP_LIP;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_MAKEUP_PUPIL;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_STYLE_MAKEUP;

/**
 * Created by QunZhang on 2019-07-21 12:27
 */
public class ItemGetPresenter extends ItemGetContract.Presenter {
    private final Map<Integer, EffectButtonItem> mSavedItems = new HashMap<>();

    @Override
    public EffectButtonItem getItem(int type) {
        EffectButtonItem item = mSavedItems.get(type);
        if (item != null) {
            return item;
        }
        switch (type & MASK) {
            case TYPE_BEAUTY_FACE:
                item = getBeautyFaceItems();
                break;
            case TYPE_BEAUTY_RESHAPE:
                item = getBeautyReshapeItems();
                break;
            case TYPE_BEAUTY_BODY:
                item = getBeautyBodyItems();
                break;
            case TYPE_MAKEUP:
                item = getMakeupItems();
                break;
            case TYPE_STYLE_MAKEUP:
                item = getStyleMakeupItems();
                break;
        }
        if (item != null) {
            mSavedItems.put(type, item);
        }
        return item;
    }

    @Override
    public List<EffectButtonItem> allItems() {
        List<EffectButtonItem> items = new ArrayList<>();
        for (Map.Entry<Integer, EffectButtonItem> en : mSavedItems.entrySet()) {
            items.add(en.getValue());
        }
        return items;
    }

    private EffectButtonItem getBeautyFaceItems() {
        String beautyNode = beautyNode();
        return new EffectButtonItem(TYPE_BEAUTY_FACE,
                new EffectButtonItem[]{
                        new EffectButtonItem(TYPE_CLOSE, R.drawable.ic_none, R.string.close),
                        new EffectButtonItem(TYPE_BEAUTY_FACE_SMOOTH, R.drawable.ic_beauty_smooth, R.string.beauty_face_smooth, new ComposerNode(beautyNode, "smooth")),
                        new EffectButtonItem(TYPE_BEAUTY_FACE_WHITEN, R.drawable.ic_beauty_whiten, R.string.beauty_face_whiten, new ComposerNode(beautyNode, "whiten")),
                        new EffectButtonItem(TYPE_BEAUTY_FACE_SHARPEN, R.drawable.ic_beauty_sharpen, R.string.beauty_face_sharpen, new ComposerNode(beautyNode, "sharp"))
                });
    }

    private EffectButtonItem getBeautyReshapeItems() {
        String reshapeNode = reshapeNode();
        return new EffectButtonItem(TYPE_BEAUTY_RESHAPE,
                new EffectButtonItem[]{
                        new EffectButtonItem(TYPE_CLOSE, R.drawable.ic_none, R.string.close),
                        new EffectButtonItem(TYPE_BEAUTY_RESHAPE_FACE_OVERALL, R.drawable.ic_beauty_cheek_reshape, R.string.beauty_reshape_face_overall, new ComposerNode(reshapeNode, "Internal_Deform_Overall")),
                        new EffectButtonItem(TYPE_BEAUTY_RESHAPE_FACE_CUT, R.drawable.ic_beauty_reshape_face_cut, R.string.beauty_reshape_face_cut, new ComposerNode(reshapeNode, "Internal_Deform_CutFace")),
                        new EffectButtonItem(TYPE_BEAUTY_RESHAPE_FACE_SMALL, R.drawable.ic_beauty_reshape_face_small, R.string.beauty_reshape_face_small, new ComposerNode(reshapeNode, "Internal_Deform_Face")),
                        new EffectButtonItem(TYPE_BEAUTY_RESHAPE_EYE, R.drawable.ic_beauty_eye_reshape, R.string.beauty_reshape_eye, new ComposerNode(reshapeNode, "Internal_Deform_Eye")),
                        new EffectButtonItem(TYPE_BEAUTY_RESHAPE_EYE_ROTATE, R.drawable.ic_beauty_reshape_eye_rotate, R.string.beauty_reshape_eye_rotate, new ComposerNode(reshapeNode, "Internal_Deform_RotateEye")),
                        new EffectButtonItem(TYPE_BEAUTY_RESHAPE_CHEEK, R.drawable.ic_beauty_reshape_cheek, R.string.beauty_reshape_cheek, new ComposerNode(reshapeNode, "Internal_Deform_Zoom_Cheekbone")),
                        new EffectButtonItem(TYPE_BEAUTY_RESHAPE_JAW, R.drawable.ic_beauty_reshape_jaw, R.string.beauty_reshape_jaw, new ComposerNode(reshapeNode, "Internal_Deform_Zoom_Jawbone")),
                        new EffectButtonItem(TYPE_BEAUTY_RESHAPE_NOSE_LEAN, R.drawable.ic_beauty_reshape_nose_lean, R.string.beauty_reshape_nose_lean, new ComposerNode(reshapeNode, "Internal_Deform_Nose")),
                        new EffectButtonItem(TYPE_BEAUTY_RESHAPE_NOSE_LONG, R.drawable.ic_beauty_reshape_nose_long, R.string.beauty_reshape_nose_long, new ComposerNode(reshapeNode, "Internal_Deform_MovNose")),
                        new EffectButtonItem(TYPE_BEAUTY_RESHAPE_CHIN, R.drawable.ic_beauty_reshape_chin, R.string.beauty_reshape_chin, new ComposerNode(reshapeNode, "Internal_Deform_Chin")),
                        new EffectButtonItem(TYPE_BEAUTY_RESHAPE_FOREHEAD, R.drawable.ic_beauty_reshape_forehead, R.string.beauty_reshape_forehead, new ComposerNode(reshapeNode, "Internal_Deform_Forehead")),
                        new EffectButtonItem(TYPE_BEAUTY_RESHAPE_MOUTH_ZOOM, R.drawable.ic_beauty_reshape_mouth_zoom, R.string.beauty_reshape_mouth_zoom, new ComposerNode(reshapeNode, "Internal_Deform_ZoomMouth")),
                        new EffectButtonItem(TYPE_BEAUTY_RESHAPE_MOUTH_SMILE, R.drawable.ic_beauty_reshape_mouth_smile, R.string.beauty_reshape_mouth_smile, new ComposerNode(reshapeNode, "Internal_Deform_MouthCorner")),
                        new EffectButtonItem(TYPE_BEAUTY_RESHAPE_EYE_SPACING, R.drawable.ic_beauty_reshape_eye_rotate, R.string.beauty_reshape_eye_spacing, new ComposerNode(reshapeNode, "Internal_Eye_Spacing")),
                        new EffectButtonItem(TYPE_BEAUTY_RESHAPE_EYE_MOVE, R.drawable.ic_beauty_reshape_eye_rotate, R.string.beauty_reshape_eye_move, new ComposerNode(reshapeNode, "Internal_Deform_Eye_Move")),
                        new EffectButtonItem(TYPE_BEAUTY_RESHAPE_MOUTH_MOVE, R.drawable.ic_beauty_reshape_mouth_zoom, R.string.beauty_reshape_mouth_move, new ComposerNode(reshapeNode, "Internal_Deform_MovMouth")),
                        new EffectButtonItem(TYPE_BEAUTY_RESHAPE_BRIGHTEN_EYE, R.drawable.ic_beauty_smooth, R.string.beauty_face_brighten_eye, new ComposerNode(NODE_BEAUTY_4ITEMS, "BEF_BEAUTY_BRIGHTEN_EYE")),
                        new EffectButtonItem(TYPE_BEAUTY_RESHAPE_REMOVE_POUCH, R.drawable.ic_beauty_smooth, R.string.beauty_face_remove_pouch, new ComposerNode(NODE_BEAUTY_4ITEMS, "BEF_BEAUTY_REMOVE_POUCH")),
                        new EffectButtonItem(TYPE_BEAUTY_RESHAPE_SMILE_FOLDS, R.drawable.ic_beauty_smooth, R.string.beauty_face_smile_folds, new ComposerNode(NODE_BEAUTY_4ITEMS, "BEF_BEAUTY_SMILES_FOLDS")),
                        new EffectButtonItem(TYPE_BEAUTY_RESHAPE_WHITEN_TEETH, R.drawable.ic_beauty_smooth, R.string.beauty_face_whiten_teeth, new ComposerNode(NODE_BEAUTY_4ITEMS, "BEF_BEAUTY_WHITEN_TEETH")),
                        new EffectButtonItem(TYPE_BEAUTY_RESHAPE_SINGLE_TO_DOUBLE_EYELID, R.drawable.ic_beauty_smooth, R.string.beauty_face_eye_single_to_double_eyelid, new ComposerNode(NODE_BEAUTY_SURGERY, "BEF_BEAUTY_EYE_SINGLE_TO_DOUBLE")),
                        new EffectButtonItem(TYPE_BEAUTY_RESHAPE_EYE_PLUMP, R.drawable.ic_beauty_smooth, R.string.beauty_face_eye_plump, new ComposerNode(NODE_BEAUTY_SURGERY, "BEF_BEAUTY_EYE_PLUMP")),
                });
    }

    private EffectButtonItem getBeautyBodyItems() {
        return new EffectButtonItem(
                TYPE_BEAUTY_BODY,
                new EffectButtonItem[]{
                        new EffectButtonItem(TYPE_CLOSE, R.drawable.ic_none, R.string.close),
                        new EffectButtonItem(TYPE_BEAUTY_BODY_THIN, R.drawable.ic_beauty_body_thin, R.string.beauty_body_thin, new ComposerNode(NODE_ALL_SLIM, "BEF_BEAUTY_BODY_THIN")),
                        new EffectButtonItem(TYPE_BEAUTY_BODY_LONG_LEG, R.drawable.ic_beauty_body_long_leg, R.string.beauty_body_long_leg, new ComposerNode(NODE_ALL_SLIM, "BEF_BEAUTY_BODY_LONG_LEG")),
                        new EffectButtonItem(TYPE_BEAUTY_BODY_SHRINK_HEAD, R.drawable.ic_beauty_body_shrink_head, R.string.beauty_body_shrink_head, new ComposerNode(NODE_ALL_SLIM, "BEF_BEAUTY_BODY_SHRINK_HEAD")),
                        new EffectButtonItem(TYPE_BEAUTY_BODY_SLIM_LEG, R.drawable.ic_beauty_body_slim_leg, R.string.beauty_body_leg_slim, new ComposerNode(NODE_ALL_SLIM, "BEF_BEAUTY_BODY_SLIM_LEG")),
                        new EffectButtonItem(TYPE_BEAUTY_BODY_SLIM_WAIST, R.drawable.ic_beauty_body_thin, R.string.beauty_body_waist_slim, new ComposerNode(NODE_ALL_SLIM, "BEF_BEAUTY_BODY_SLIM_WAIST")),
                        new EffectButtonItem(TYPE_BEAUTY_BODY_ENLARGE_BREAST, R.drawable.ic_beauty_body_enlarge_breast, R.string.beauty_body_breast_enlarge, new ComposerNode(NODE_ALL_SLIM, "BEF_BEAUTY_BODY_ENLARGR_BREAST")),
                        new EffectButtonItem(TYPE_BEAUTY_BODY_ENHANCE_HIP, R.drawable.ic_beauty_body_enhance_hip, R.string.beauty_body_hip_enhance, new ComposerNode(NODE_ALL_SLIM, "BEF_BEAUTY_BODY_ENHANCE_HIP")),
                        new EffectButtonItem(TYPE_BEAUTY_BODY_ENHANCE_NECK, R.drawable.ic_beauty_body_enhance_neck, R.string.beauty_body_neck_enhance, new ComposerNode(NODE_ALL_SLIM, "BEF_BEAUTY_BODY_ENHANCE_NECK")),
                        new EffectButtonItem(TYPE_BEAUTY_BODY_SLIM_ARM, R.drawable.ic_beauty_body_slim_arm, R.string.beauty_body_arm_slim, new ComposerNode(NODE_ALL_SLIM, "BEF_BEAUTY_BODY_SLIM_ARM")),
                }
        );
    }

    private EffectButtonItem getMakeupItems() {
        return new EffectButtonItem(
                TYPE_MAKEUP,
                new EffectButtonItem[]{
                        new EffectButtonItem(TYPE_CLOSE, R.drawable.ic_none, R.string.close),
                        new EffectButtonItem(TYPE_MAKEUP_BLUSHER, R.drawable.ic_makeup_blusher, R.string.makeup_blusher, getMakeupOptionItems(TYPE_MAKEUP_BLUSHER), false),
                        new EffectButtonItem(TYPE_MAKEUP_LIP, R.drawable.ic_makeup_lip, R.string.makeup_lip, getMakeupOptionItems(TYPE_MAKEUP_LIP), false),
                        new EffectButtonItem(TYPE_MAKEUP_FACIAL, R.drawable.ic_makeup_facial, R.string.makeup_facial, getMakeupOptionItems(TYPE_MAKEUP_FACIAL), false),
                        new EffectButtonItem(TYPE_MAKEUP_PUPIL, R.drawable.ic_makeup_pupil, R.string.makeup_pupil, getMakeupOptionItems(TYPE_MAKEUP_PUPIL), false),
                        new EffectButtonItem(TYPE_MAKEUP_HAIR, R.drawable.ic_makeup_hair, R.string.makeup_hair, getMakeupOptionItems(TYPE_MAKEUP_HAIR), false),
                        new EffectButtonItem(TYPE_MAKEUP_EYESHADOW, R.drawable.ic_makeup_eye, R.string.makeup_eye, getMakeupOptionItems(TYPE_MAKEUP_EYESHADOW), false),
                        new EffectButtonItem(TYPE_MAKEUP_EYEBROW, R.drawable.ic_makeup_eyebrow, R.string.makeup_eyebrow, getMakeupOptionItems(TYPE_MAKEUP_EYEBROW), false),
                }
        );
    }

    private EffectButtonItem[] getMakeupOptionItems(int type) {
        switch (type & SUB_MASK) {
            case TYPE_MAKEUP_LIP:
                return new EffectButtonItem[]{
                        new EffectButtonItem(TYPE_CLOSE, R.drawable.ic_none, R.string.close),
                        new EffectButtonItem(TYPE_MAKEUP_LIP, R.drawable.ic_makeup_lip, R.string.lip_fuguhong, new ComposerNode("lip/fuguhong", "Internal_Makeup_Lips")),
                        new EffectButtonItem(TYPE_MAKEUP_LIP, R.drawable.ic_makeup_lip, R.string.lip_shaonvfen, new ComposerNode("lip/shaonvfen", "Internal_Makeup_Lips")),
                        new EffectButtonItem(TYPE_MAKEUP_LIP, R.drawable.ic_makeup_lip, R.string.lip_yuanqiju, new ComposerNode("lip/yuanqiju", "Internal_Makeup_Lips")),
                        new EffectButtonItem(TYPE_MAKEUP_LIP, R.drawable.ic_makeup_lip, R.string.lip_xiyouse, new ComposerNode("lip/xiyouse", "Internal_Makeup_Lips")),
                        new EffectButtonItem(TYPE_MAKEUP_LIP, R.drawable.ic_makeup_lip, R.string.lip_xiguahong, new ComposerNode("lip/xiguahong", "Internal_Makeup_Lips")),
                        new EffectButtonItem(TYPE_MAKEUP_LIP, R.drawable.ic_makeup_lip, R.string.lip_sironghong, new ComposerNode("lip/sironghong", "Internal_Makeup_Lips")),
                        new EffectButtonItem(TYPE_MAKEUP_LIP, R.drawable.ic_makeup_lip, R.string.lip_zangjuse, new ComposerNode("lip/zangjuse", "Internal_Makeup_Lips")),
                        new EffectButtonItem(TYPE_MAKEUP_LIP, R.drawable.ic_makeup_lip, R.string.lip_meizise, new ComposerNode("lip/meizise", "Internal_Makeup_Lips")),
                        new EffectButtonItem(TYPE_MAKEUP_LIP, R.drawable.ic_makeup_lip, R.string.lip_shanhuse, new ComposerNode("lip/shanhuse", "Internal_Makeup_Lips")),
                        new EffectButtonItem(TYPE_MAKEUP_LIP, R.drawable.ic_makeup_lip, R.string.lip_doushafen, new ComposerNode("lip/doushafen", "Internal_Makeup_Lips")),
                };
            case TYPE_MAKEUP_BLUSHER:
                return new EffectButtonItem[]{
                        new EffectButtonItem(TYPE_CLOSE, R.drawable.ic_none, R.string.close),
                        new EffectButtonItem(TYPE_MAKEUP_BLUSHER, R.drawable.ic_makeup_blusher, R.string.blusher_weixunfen, new ComposerNode("blush/weixun", "Internal_Makeup_Blusher")),
                        new EffectButtonItem(TYPE_MAKEUP_BLUSHER, R.drawable.ic_makeup_blusher, R.string.blusher_richang, new ComposerNode("blush/richang", "Internal_Makeup_Blusher")),
                        new EffectButtonItem(TYPE_MAKEUP_BLUSHER, R.drawable.ic_makeup_blusher, R.string.blusher_mitao, new ComposerNode("blush/mitao", "Internal_Makeup_Blusher")),
                        new EffectButtonItem(TYPE_MAKEUP_BLUSHER, R.drawable.ic_makeup_blusher, R.string.blusher_tiancheng, new ComposerNode("blush/tiancheng", "Internal_Makeup_Blusher")),
                        new EffectButtonItem(TYPE_MAKEUP_BLUSHER, R.drawable.ic_makeup_blusher, R.string.blusher_qiaopi, new ComposerNode("blush/qiaopi", "Internal_Makeup_Blusher")),
                        new EffectButtonItem(TYPE_MAKEUP_BLUSHER, R.drawable.ic_makeup_blusher, R.string.blusher_xinji, new ComposerNode("blush/xinji", "Internal_Makeup_Blusher")),
                        new EffectButtonItem(TYPE_MAKEUP_BLUSHER, R.drawable.ic_makeup_blusher, R.string.blusher_shaishang, new ComposerNode("blush/shaishang", "Internal_Makeup_Blusher")),
                };
            case TYPE_MAKEUP_PUPIL:
                return new EffectButtonItem[]{
                        new EffectButtonItem(TYPE_CLOSE, R.drawable.ic_none, R.string.close),
                        new EffectButtonItem(TYPE_MAKEUP_PUPIL, R.drawable.ic_makeup_pupil, R.string.pupil_hunxuezong, new ComposerNode("pupil/hunxuezong", "Internal_Makeup_Pupil")),
                        new EffectButtonItem(TYPE_MAKEUP_PUPIL, R.drawable.ic_makeup_pupil, R.string.pupil_kekezong, new ComposerNode("pupil/kekezong", "Internal_Makeup_Pupil")),
                        new EffectButtonItem(TYPE_MAKEUP_PUPIL, R.drawable.ic_makeup_pupil, R.string.pupil_mitaofen, new ComposerNode("pupil/mitaofen", "Internal_Makeup_Pupil")),
                        new EffectButtonItem(TYPE_MAKEUP_PUPIL, R.drawable.ic_makeup_pupil, R.string.pupil_shuiguanghei, new ComposerNode("pupil/shuiguanghei", "Internal_Makeup_Pupil")),
                        new EffectButtonItem(TYPE_MAKEUP_PUPIL, R.drawable.ic_makeup_pupil, R.string.pupil_xingkonglan, new ComposerNode("pupil/xingkonglan", "Internal_Makeup_Pupil")),
                        new EffectButtonItem(TYPE_MAKEUP_PUPIL, R.drawable.ic_makeup_pupil, R.string.pupil_chujianhui, new ComposerNode("pupil/chujianhui", "Internal_Makeup_Pupil")),
                };
            case TYPE_MAKEUP_HAIR:
                return new EffectButtonItem[]{
                        new EffectButtonItem(TYPE_CLOSE, R.drawable.ic_none, R.string.close),
                        new EffectButtonItem(TYPE_MAKEUP_HAIR, R.drawable.ic_makeup_hair, R.string.hair_anlan, new ComposerNode("hair/anlan")),
                        new EffectButtonItem(TYPE_MAKEUP_HAIR, R.drawable.ic_makeup_hair, R.string.hair_molv, new ComposerNode("hair/molv")),
                        new EffectButtonItem(TYPE_MAKEUP_HAIR, R.drawable.ic_makeup_hair, R.string.hair_shenzong, new ComposerNode("hair/shenzong")),
                };
            case TYPE_MAKEUP_EYESHADOW:
                return new EffectButtonItem[]{
                        new EffectButtonItem(TYPE_CLOSE, R.drawable.ic_none, R.string.close),
                        new EffectButtonItem(TYPE_MAKEUP_EYESHADOW, R.drawable.ic_makeup_eye, R.string.eye_dadizong, new ComposerNode("eyeshadow/dadizong", "Internal_Makeup_Eye")),
                        new EffectButtonItem(TYPE_MAKEUP_EYESHADOW, R.drawable.ic_makeup_eye, R.string.eye_wanxiahong, new ComposerNode("eyeshadow/wanxiahong", "Internal_Makeup_Eye")),
                        new EffectButtonItem(TYPE_MAKEUP_EYESHADOW, R.drawable.ic_makeup_eye, R.string.eye_shaonvfen, new ComposerNode("eyeshadow/shaonvfen", "Internal_Makeup_Eye")),
                        new EffectButtonItem(TYPE_MAKEUP_EYESHADOW, R.drawable.ic_makeup_eye, R.string.eye_qizhifen, new ComposerNode("eyeshadow/qizhifen", "Internal_Makeup_Eye")),
                        new EffectButtonItem(TYPE_MAKEUP_EYESHADOW, R.drawable.ic_makeup_eye, R.string.eye_meizihong, new ComposerNode("eyeshadow/meizihong", "Internal_Makeup_Eye")),
                        new EffectButtonItem(TYPE_MAKEUP_EYESHADOW, R.drawable.ic_makeup_eye, R.string.eye_jiaotangzong, new ComposerNode("eyeshadow/jiaotangzong", "Internal_Makeup_Eye")),
                        new EffectButtonItem(TYPE_MAKEUP_EYESHADOW, R.drawable.ic_makeup_eye, R.string.eye_yuanqiju, new ComposerNode("eyeshadow/yuanqiju", "Internal_Makeup_Eye")),
                        new EffectButtonItem(TYPE_MAKEUP_EYESHADOW, R.drawable.ic_makeup_eye, R.string.eye_naichase, new ComposerNode("eyeshadow/naichase", "Internal_Makeup_Eye")),
                };
            case TYPE_MAKEUP_EYEBROW:
                return new EffectButtonItem[]{
                        new EffectButtonItem(TYPE_CLOSE, R.drawable.ic_none, R.string.close),
                        new EffectButtonItem(TYPE_MAKEUP_EYEBROW, R.drawable.ic_makeup_eyebrow, R.string.eyebrow_BRO1, new ComposerNode("eyebrow/BR01", "Internal_Makeup_Brow")),
                        new EffectButtonItem(TYPE_MAKEUP_EYEBROW, R.drawable.ic_makeup_eyebrow, R.string.eyebrow_BKO1, new ComposerNode("eyebrow/BK01", "Internal_Makeup_Brow")),
                        new EffectButtonItem(TYPE_MAKEUP_EYEBROW, R.drawable.ic_makeup_eyebrow, R.string.eyebrow_BKO2, new ComposerNode("eyebrow/BK02", "Internal_Makeup_Brow")),
                        new EffectButtonItem(TYPE_MAKEUP_EYEBROW, R.drawable.ic_makeup_eyebrow, R.string.eyebrow_BKO3, new ComposerNode("eyebrow/BK03", "Internal_Makeup_Brow")),
                };
            case TYPE_MAKEUP_FACIAL:
                return new EffectButtonItem[]{
                        new EffectButtonItem(TYPE_CLOSE, R.drawable.ic_none,  R.string.close),
                        new EffectButtonItem(TYPE_MAKEUP_FACIAL, R.drawable.ic_makeup_facial,  R.string.facial_1,  new ComposerNode( "facial/xiurong01",  "Internal_Makeup_Facial")),
                        new EffectButtonItem(TYPE_MAKEUP_FACIAL, R.drawable.ic_makeup_facial,  R.string.facial_2,  new ComposerNode( "facial/xiurong02",  "Internal_Makeup_Facial")),
                        new EffectButtonItem(TYPE_MAKEUP_FACIAL, R.drawable.ic_makeup_facial,  R.string.facial_3,  new ComposerNode( "facial/xiurong03",  "Internal_Makeup_Facial")),
                        new EffectButtonItem(TYPE_MAKEUP_FACIAL, R.drawable.ic_makeup_facial,  R.string.facial_4,  new ComposerNode( "facial/xiurong04",  "Internal_Makeup_Facial")),
                };
        }
        return null;
    }

    private EffectButtonItem getStyleMakeupItems() {
        String tag = "{\"effectsdk_config\":\"{\\\"minV\\\":\\\"6.9.0\\\",\\\"FaceMakeupV2AMG\\\":true}\"}";
        return new EffectButtonItem(
                TYPE_STYLE_MAKEUP,
                new EffectButtonItem[]{
                        new EffectButtonItem(TYPE_CLOSE, R.drawable.ic_none, R.string.close),
                        new EffectButtonItem(TYPE_STYLE_MAKEUP, R.drawable.icon_qise, R.string.style_makeup_qise, new ComposerNode("style_makeup/qise", new String[]{"Filter_ALL", "Makeup_ALL"}, tag)),
                        new EffectButtonItem(TYPE_STYLE_MAKEUP, R.drawable.icon_aidou, R.string.style_makeup_aidou, new ComposerNode("style_makeup/aidou", new String[]{"Filter_ALL", "Makeup_ALL"}, tag)),
                        new EffectButtonItem(TYPE_STYLE_MAKEUP, R.drawable.icon_youya, R.string.style_makeup_youya, new ComposerNode("style_makeup/youya", new String[]{"Filter_ALL", "Makeup_ALL"}, tag)),
                        new EffectButtonItem(TYPE_STYLE_MAKEUP, R.drawable.icon_cwei, R.string.style_makeup_cwei, new ComposerNode("style_makeup/cwei", new String[]{"Filter_ALL", "Makeup_ALL"}, tag)),
                        new EffectButtonItem(TYPE_STYLE_MAKEUP, R.drawable.icon_nuannan, R.string.style_makeup_nuannan, new ComposerNode("style_makeup/nuannan", new String[]{"Filter_ALL", "Makeup_ALL"}, tag)),
                        new EffectButtonItem(TYPE_STYLE_MAKEUP, R.drawable.icon_baixi, R.string.style_makeup_baixi, new ComposerNode("style_makeup/baixi", new String[]{"Filter_ALL", "Makeup_ALL"}, tag)),
                        new EffectButtonItem(TYPE_STYLE_MAKEUP, R.drawable.icon_wennuan, R.string.style_makeup_wennuan, new ComposerNode("style_makeup/wennuan", new String[]{"Filter_ALL", "Makeup_ALL"}, tag)),
                        new EffectButtonItem(TYPE_STYLE_MAKEUP, R.drawable.icon_shensui, R.string.style_makeup_shensui, new ComposerNode("style_makeup/shensui", new String[]{"Filter_ALL", "Makeup_ALL"}, tag)),
                        new EffectButtonItem(TYPE_STYLE_MAKEUP, R.drawable.icon_tianmei, R.string.style_makeup_tianmei, new ComposerNode("style_makeup/tianmei", new String[]{"Filter_ALL", "Makeup_ALL"}, tag)),
                        new EffectButtonItem(TYPE_STYLE_MAKEUP, R.drawable.icon_duanmei, R.string.style_makeup_duanmei, new ComposerNode("style_makeup/duanmei", new String[]{"Filter_ALL", "Makeup_ALL"}, tag)),
                        new EffectButtonItem(TYPE_STYLE_MAKEUP, R.drawable.icon_oumei, R.string.style_makeup_oumei, new ComposerNode("style_makeup/oumei", new String[]{"Filter_ALL", "Makeup_ALL"}, tag)),
                        new EffectButtonItem(TYPE_STYLE_MAKEUP, R.drawable.icon_zhigan, R.string.style_makeup_zhigan, new ComposerNode("style_makeup/zhigan", new String[]{"Filter_ALL", "Makeup_ALL"}, tag)),
                        new EffectButtonItem(TYPE_STYLE_MAKEUP, R.drawable.icon_hanxi, R.string.style_makeup_hanxi, new ComposerNode("style_makeup/hanxi", new String[]{"Filter_ALL", "Makeup_ALL"}, tag)),
                        new EffectButtonItem(TYPE_STYLE_MAKEUP, R.drawable.icon_yuanqi, R.string.style_makeup_yuanqi, new ComposerNode("style_makeup/yuanqi", new String[]{"Filter_ALL", "Makeup_ALL"}, tag)),
                },
                false
        );
    }

    private String beautyNode() {
        if (getView().getEffectType() == null || getView().getEffectType() == BaseEffectActivity.EffectType.CAMERA) {
            return NODE_BEAUTY_CAMERA;
        } else {
            return NODE_BEAUTY_LIVE;
        }
    }

    private String reshapeNode() {
        if (getView().getEffectType() == null || getView().getEffectType() == BaseEffectActivity.EffectType.CAMERA) {
            return NODE_RESHAPE_CAMERA;
        } else {
            return NODE_RESHAPE_LIVE;
        }
    }
}
