package com.bytedance.labcv.demo.presenter;

import android.annotation.SuppressLint;

import com.bytedance.labcv.demo.R;
import com.bytedance.labcv.demo.base.IView;
import com.bytedance.labcv.demo.model.EffectButtonItem;
import com.bytedance.labcv.demo.presenter.contract.EffectContract;
import com.bytedance.labcv.demo.presenter.contract.ItemGetContract;
import com.bytedance.labcv.demo.ui.BaseEffectActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_RESHAPE_SMILE_FOLDS;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_BEAUTY_RESHAPE_WHITEN_TEETH;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_CLOSE;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_FILTER;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_MAKEUP;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_MAKEUP_BLUSHER;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_MAKEUP_EYEBROW;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_MAKEUP_EYESHADOW;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_MAKEUP_FACIAL;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_MAKEUP_LIP;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_MAKEUP_PUPIL;
import static com.bytedance.labcv.demo.presenter.contract.ItemGetContract.TYPE_STYLE_MAKEUP;

/**
 * Created by QunZhang on 2019-07-22 13:57
 */
public class EffectPresenter extends EffectContract.Presenter {
    private final ItemGetContract.Presenter mItemGet;

    public EffectPresenter() {
        mItemGet = new ItemGetPresenter();
    }

    @Override
    public void attachView(IView view) {
        super.attachView(view);
        mItemGet.attachView(view);
    }

    @Override
    public String[][] generateComposerNodesAndTags(Set<EffectButtonItem> selectNodes) {
        List<EffectButtonItem> items = new ArrayList<>();
        Set<String> set = new HashSet<>();
        for (EffectButtonItem item : selectNodes) {
            if (item.getNode() != null && !set.contains(item.getNode().getPath())) {
                set.add(item.getNode().getPath());
                items.add(item);
            }
        }

        String[] nodes = new String[items.size()];
        String[] tags = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            nodes[i] = items.get(i).getNode().getPath();
            tags[i] = items.get(i).getNode().getTag();
        }

        return new String[][] {nodes, tags};
    }

    @Override
    public List<EffectButtonItem> getDefaultItems() {
        EffectButtonItem beautyFace = mItemGet.getItem(TYPE_BEAUTY_FACE);
        EffectButtonItem beautyReshape = mItemGet.getItem(TYPE_BEAUTY_RESHAPE);
        EffectButtonItem beautyFaceSelect = beautyFace.getSelectChild();
        EffectButtonItem beautyReshapeSelect = beautyReshape.getSelectChild();

        resetAll();

        beautyFace.setSelectChild(beautyFaceSelect);
        beautyReshape.setSelectChild(beautyReshapeSelect);
        List<EffectButtonItem> items = new ArrayList<>();
        for (EffectButtonItem item : beautyFace.getChildren()) {
            if (isDefaultEffect(item.getId())) {
                items.add(item);
            }
        }
        for (EffectButtonItem item : beautyReshape.getChildren()) {
            if (isDefaultEffect(item.getId())) {
                items.add(item);
            }
        }

        for (EffectButtonItem item : items) {
            float[] defaultIntensity = getDefaultValue(item.getId());
            if (defaultIntensity.length > 0 && item.getIntensityArray() != null) {
                for (int i = 0; i < defaultIntensity.length && i < item.getIntensityArray().length; i++) {
                    item.getIntensityArray()[i] = defaultIntensity[i];
                }
            }
        }

        return items;
    }

    @Override
    public float[] getDefaultValue(int type) {
        Object intensity = getDefaultMap().get(type);
        if (intensity instanceof Float) {
            return new float[] {(Float) intensity};
        } else if (intensity instanceof float[]) {
            return (float[])intensity;
        }
        return new float[0];
    }

    @Override
    public EffectButtonItem getItem(int type) {
        return mItemGet.getItem(type);
    }

    @Override
    public List<TabItem> getTabItems() {
        return Arrays.asList(
                new TabItem(TYPE_BEAUTY_FACE, R.string.tab_face_beautification),
                new TabItem(TYPE_BEAUTY_RESHAPE, R.string.tab_face_beauty_reshape),
                new TabItem(TYPE_BEAUTY_BODY, R.string.tab_face_beauty_body),
                new TabItem(TYPE_MAKEUP, R.string.tab_face_makeup),
                new TabItem(TYPE_STYLE_MAKEUP, R.string.tab_style_makeup),
                new TabItem(TYPE_FILTER, R.string.tab_filter)
        );
    }

    private Map<Integer, Object> getDefaultMap() {
        BaseEffectActivity.EffectType type = BaseEffectActivity.EffectType.CAMERA;
        if (getView() != null && getView().getEffectType() != null) {
            type = getView().getEffectType();
        }
        switch (type) {
            case VIDEO:
                return DEFAULT_LIVE_VALUE;
            case CAMERA:
                return DEFAULT_CAMERA_VALUE;
        }
        return DEFAULT_CAMERA_VALUE;
    }

    private void resetAll() {
        for (EffectButtonItem item : mItemGet.allItems()) {
            resetItem(item);
        }
    }

    private void resetItem(EffectButtonItem item) {
        if (item.getChildren() != null) {
            item.setSelectChild(null);
            for (EffectButtonItem child : item.getChildren()) {
                resetItem(child);
            }
        }

        item.resetIntensity();
    }

    private boolean isDefaultEffect(int id) {
        return id <= TYPE_BEAUTY_RESHAPE_MOUTH_MOVE && id > TYPE_CLOSE;
    }

    private static final Map<Integer, Object> DEFAULT_CAMERA_VALUE;
    private static final Map<Integer, Object> DEFAULT_LIVE_VALUE;
    static {
        @SuppressLint("UseSparseArrays") Map<Integer, Object> cameraMap = new HashMap<>();
        @SuppressLint("UseSparseArrays") Map<Integer, Object> liveMap = new HashMap<>();
        // 美颜
        // beauty face
        cameraMap.put(TYPE_BEAUTY_FACE_SMOOTH, 0.8F);
        cameraMap.put(TYPE_BEAUTY_FACE_WHITEN, 0.3F);
        cameraMap.put(TYPE_BEAUTY_FACE_SHARPEN, 0.32F);
        cameraMap.put(TYPE_BEAUTY_RESHAPE_BRIGHTEN_EYE, 0.0F);
        cameraMap.put(TYPE_BEAUTY_RESHAPE_REMOVE_POUCH, 0.0F);
        cameraMap.put(TYPE_BEAUTY_RESHAPE_SMILE_FOLDS, 0.0F);
        cameraMap.put(TYPE_BEAUTY_RESHAPE_WHITEN_TEETH, 0.0F);
        // 美型
        // beaury reshape
        cameraMap.put(TYPE_BEAUTY_RESHAPE_FACE_OVERALL, 0.5F);
        cameraMap.put(TYPE_BEAUTY_RESHAPE_FACE_SMALL, 0.0F);
        cameraMap.put(TYPE_BEAUTY_RESHAPE_FACE_CUT, 0.0F);
        cameraMap.put(TYPE_BEAUTY_RESHAPE_EYE, 0.3F);
        cameraMap.put(TYPE_BEAUTY_RESHAPE_EYE_ROTATE, 0.0F);
        cameraMap.put(TYPE_BEAUTY_RESHAPE_CHEEK, 0.0F);
        cameraMap.put(TYPE_BEAUTY_RESHAPE_JAW, 0.0F);
        cameraMap.put(TYPE_BEAUTY_RESHAPE_NOSE_LEAN, 0.0F);
        cameraMap.put(TYPE_BEAUTY_RESHAPE_NOSE_LONG, 0.25F);
        cameraMap.put(TYPE_BEAUTY_RESHAPE_CHIN, 0.0F);
        cameraMap.put(TYPE_BEAUTY_RESHAPE_FOREHEAD, 0.0F);
        cameraMap.put(TYPE_BEAUTY_RESHAPE_MOUTH_ZOOM, 0.0F);
        cameraMap.put(TYPE_BEAUTY_RESHAPE_MOUTH_SMILE, 0.0F);
        cameraMap.put(TYPE_BEAUTY_RESHAPE_EYE_SPACING, 0.0F);
        cameraMap.put(TYPE_BEAUTY_RESHAPE_EYE_MOVE, 0.0F);
        cameraMap.put(TYPE_BEAUTY_RESHAPE_MOUTH_MOVE, 0.0F);
        // 美体
        cameraMap.put(TYPE_BEAUTY_BODY_THIN, 0.0f);
        cameraMap.put(TYPE_BEAUTY_BODY_LONG_LEG, 0.0f);
        cameraMap.put(TYPE_BEAUTY_BODY_SLIM_LEG, 0.0f);
        cameraMap.put(TYPE_BEAUTY_BODY_SHRINK_HEAD, 0.0f);
        cameraMap.put(TYPE_BEAUTY_BODY_SLIM_WAIST, 0.0f);
        cameraMap.put(TYPE_BEAUTY_BODY_ENLARGE_BREAST, 0.0f);
        cameraMap.put(TYPE_BEAUTY_BODY_ENHANCE_HIP, 0.5f);
        cameraMap.put(TYPE_BEAUTY_BODY_ENHANCE_NECK, 0.0f);
        cameraMap.put(TYPE_BEAUTY_BODY_SLIM_ARM, 0.0f);

        // 美妆
        cameraMap.put(TYPE_MAKEUP_LIP, 0.5F);
//        cameraMap.put(TYPE_MAKEUP_HAIR, 0.5F);
        cameraMap.put(TYPE_MAKEUP_BLUSHER, 0.2F);
        cameraMap.put(TYPE_MAKEUP_FACIAL, 0.35F);
        cameraMap.put(TYPE_MAKEUP_EYEBROW, 0.35F);
        cameraMap.put(TYPE_MAKEUP_EYESHADOW, 0.35F);
        cameraMap.put(TYPE_MAKEUP_PUPIL, 0.4F);

        // 风格妆
        cameraMap.put(TYPE_STYLE_MAKEUP, new float[] {0.8f, 0.3f});

        // 滤镜
        // filter
        cameraMap.put(TYPE_FILTER, 0.8F);
        DEFAULT_CAMERA_VALUE = Collections.unmodifiableMap(cameraMap);

        // 美颜
        // beauty face
        liveMap.put(TYPE_BEAUTY_FACE_SMOOTH, 0.5F);
        liveMap.put(TYPE_BEAUTY_FACE_WHITEN, 0.35F);
        liveMap.put(TYPE_BEAUTY_FACE_SHARPEN, 0.3F);
        liveMap.put(TYPE_BEAUTY_RESHAPE_BRIGHTEN_EYE, 0.5F);
        liveMap.put(TYPE_BEAUTY_RESHAPE_REMOVE_POUCH, 0.5F);
        liveMap.put(TYPE_BEAUTY_RESHAPE_SMILE_FOLDS, 0.35F);
        liveMap.put(TYPE_BEAUTY_RESHAPE_WHITEN_TEETH, 0.35F);
        // 美型
        // beaury reshape
        liveMap.put(TYPE_BEAUTY_RESHAPE_FACE_OVERALL, 0.35F);
        liveMap.put(TYPE_BEAUTY_RESHAPE_FACE_SMALL, 0.0F);
        liveMap.put(TYPE_BEAUTY_RESHAPE_FACE_CUT, 0.0F);
        liveMap.put(TYPE_BEAUTY_RESHAPE_EYE, 0.35F);
        liveMap.put(TYPE_BEAUTY_RESHAPE_EYE_ROTATE, 0.0F);
        liveMap.put(TYPE_BEAUTY_RESHAPE_CHEEK, 0.2F);
        liveMap.put(TYPE_BEAUTY_RESHAPE_JAW, 0.4F);
        liveMap.put(TYPE_BEAUTY_RESHAPE_NOSE_LEAN, 0.2F);
        liveMap.put(TYPE_BEAUTY_RESHAPE_NOSE_LONG, 0.0F);
        liveMap.put(TYPE_BEAUTY_RESHAPE_CHIN, 0.0F);
        liveMap.put(TYPE_BEAUTY_RESHAPE_FOREHEAD, 0.0F);
        liveMap.put(TYPE_BEAUTY_RESHAPE_MOUTH_ZOOM, 0.15F);
        liveMap.put(TYPE_BEAUTY_RESHAPE_MOUTH_SMILE, 0.0F);
        liveMap.put(TYPE_BEAUTY_RESHAPE_EYE_SPACING, 0.15F);
        liveMap.put(TYPE_BEAUTY_RESHAPE_EYE_MOVE, 0.0F);
        liveMap.put(TYPE_BEAUTY_RESHAPE_MOUTH_MOVE, 0.0F);
        liveMap.put(TYPE_BEAUTY_RESHAPE_EYE_PLUMP, 0.35F);
        // 美体
        liveMap.put(TYPE_BEAUTY_BODY_THIN, 0.0f);
        liveMap.put(TYPE_BEAUTY_BODY_LONG_LEG, 0.0f);
        liveMap.put(TYPE_BEAUTY_BODY_SLIM_LEG, 0.0f);
        liveMap.put(TYPE_BEAUTY_BODY_SHRINK_HEAD, 0.0f);
        liveMap.put(TYPE_BEAUTY_BODY_SLIM_WAIST, 0.0f);
        liveMap.put(TYPE_BEAUTY_BODY_ENLARGE_BREAST, 0.0f);
        liveMap.put(TYPE_BEAUTY_BODY_ENHANCE_HIP, 0.5f);
        liveMap.put(TYPE_BEAUTY_BODY_ENHANCE_NECK, 0.0f);
        liveMap.put(TYPE_BEAUTY_BODY_SLIM_ARM, 0.0f);

        // 美妆
        liveMap.put(TYPE_MAKEUP_LIP, 0.5F);
//        liveMap.put(TYPE_MAKEUP_HAIR, 0.5F);
        liveMap.put(TYPE_MAKEUP_BLUSHER, 0.2F);
        liveMap.put(TYPE_MAKEUP_FACIAL, 0.35F);
        liveMap.put(TYPE_MAKEUP_EYEBROW, 0.35F);
        liveMap.put(TYPE_MAKEUP_EYESHADOW, 0.35F);
        liveMap.put(TYPE_MAKEUP_PUPIL, 0.4F);

        // 风格妆
        liveMap.put(TYPE_STYLE_MAKEUP, new float[] {0.8f, 0.3f});

        // 滤镜
        // filter
        liveMap.put(TYPE_FILTER, 0.8F);
        DEFAULT_LIVE_VALUE = Collections.unmodifiableMap(liveMap);
    }
}
