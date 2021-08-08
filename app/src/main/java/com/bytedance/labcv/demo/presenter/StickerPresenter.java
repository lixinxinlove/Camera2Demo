package com.bytedance.labcv.demo.presenter;

import android.content.Context;

import com.bytedance.labcv.demo.R;
import com.bytedance.labcv.demo.presenter.contract.StickerContract;
import com.bytedance.labcv.demo.model.StickerItem;
import com.bytedance.labcv.demo.model.GlobalData;
import com.bytedance.labcv.demo.ui.DemoApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bytedance.labcv.demo.presenter.contract.StickerContract.MASK;
import static com.bytedance.labcv.demo.presenter.contract.StickerContract.TYPE_ANIMOJI;
import static com.bytedance.labcv.demo.presenter.contract.StickerContract.TYPE_ARSCAN;
import static com.bytedance.labcv.demo.presenter.contract.StickerContract.TYPE_STICKER;
import static com.bytedance.labcv.demo.presenter.contract.StickerContract.TYPE_STICKER_2D;
import static com.bytedance.labcv.demo.presenter.contract.StickerContract.TYPE_STICKER_3D;
import static com.bytedance.labcv.demo.presenter.contract.StickerContract.TYPE_STICKER_ADVANCED;
import static com.bytedance.labcv.demo.presenter.contract.StickerContract.TYPE_STICKER_COMPLEX;
import static com.bytedance.labcv.demo.presenter.contract.StickerContract.TYPE_STICKER_STYLE_MAKEUP;

/**
 * Created by QunZhang on 2019-07-21 14:09
 */
public class StickerPresenter extends StickerContract.Presenter {
//    private List<StickerItem> mStickerComplexItems;
//    private List<StickerItem> mSticker2DItems;
//    private List<StickerItem> mSticker3DItems;
//    private List<StickerItem> mStickerAdvancedItems;
//    private List<StickerItem> mAnimojiItems;
//    private List<StickerItem> mARScanItems;
    private Map<Integer, List<StickerItem>> mSavedItems = new HashMap<>();

    @Override
    public List<TabItem> getTabItems() {
        return GlobalData.getInstance().useArScan() ?
                Arrays.asList(
                        new TabItem(TYPE_STICKER_2D, R.string.sticker_2d),
                        new TabItem(TYPE_STICKER_COMPLEX, R.string.sticker_complex),
                        new TabItem(TYPE_STICKER_3D, R.string.sticker_3d),
                        new TabItem(TYPE_STICKER_ADVANCED, R.string.sticker_advanced),
                        new TabItem(TYPE_STICKER_STYLE_MAKEUP, R.string.sticker_makeup),
                        new TabItem(TYPE_ARSCAN, R.string.tab_saoyisao)
                ) :
                Arrays.asList(
                        new TabItem(TYPE_STICKER_2D, R.string.sticker_2d),
                        new TabItem(TYPE_STICKER_COMPLEX, R.string.sticker_complex),
                        new TabItem(TYPE_STICKER_3D, R.string.sticker_3d),
                        new TabItem(TYPE_STICKER_ADVANCED, R.string.sticker_advanced),
                        new TabItem(TYPE_STICKER_STYLE_MAKEUP, R.string.sticker_makeup)
                );
    }

    @Override
    public List<StickerItem> getItems(int type) {
        List<StickerItem> items = mSavedItems.get(type);
        if (items != null) {
            return items;
        }
        switch (type) {
            case TYPE_STICKER_2D:
                items = getSticker2DItems();
                break;
            case TYPE_STICKER_3D:
                items = getSticker3DItems();
                break;
            case TYPE_STICKER_COMPLEX:
                items = getStickerComplexItems();
                break;
            case TYPE_STICKER_ADVANCED:
                items = getStickerAdvancedItems();
                break;
            case TYPE_ANIMOJI:
                items = getAnimojiItems();
                break;
            case TYPE_ARSCAN:
                items = getARScanItems();
                break;
            case TYPE_STICKER_STYLE_MAKEUP:
                items = getStyleMakeupItems();
                break;
                default:
                    items = Collections.emptyList();
        }
        mSavedItems.put(type, items);
        return items;
    }

    private List<StickerItem> getSticker2DItems() {
        List<StickerItem> mSticker2DItems = new ArrayList<>();
        Context context = DemoApplication.context();

        mSticker2DItems.add(new StickerItem(context.getString(R.string.filter_normal), R.drawable.clear, null));
        mSticker2DItems.add(new StickerItem(context.getString(R.string.sticker_landiaoxueying), R.drawable.icon_landiaoxueying, "stickers/landiaoxueying", context.getString(R.string.sticker_tip_landiaoxueying)));
        mSticker2DItems.add(new StickerItem(context.getString(R.string.sticker_weilandongrizhuang), R.drawable.icon_weilandongrizhuang, "stickers/weilandongrizhuang", context.getString(R.string.sticker_tip_weilandongrizhuang)));
        mSticker2DItems.add(new StickerItem(context.getString(R.string.sticker_tiaowuhuoji), R.drawable.icon_tiaowuhuoji, "stickers/tiaowuhuoji", context.getString(R.string.sticker_tip_tiaowuhuoji)));
        mSticker2DItems.add(new StickerItem(context.getString(R.string.sticker_lizishengdan), R.drawable.icon_lizishengdan, "stickers/lizishengdan", context.getString(R.string.sticker_tip_lizishengdan)));
        mSticker2DItems.add(new StickerItem(context.getString(R.string.sticker_heimaoyanjing), R.drawable.icon_heimaoyanjing, "stickers/heimaoyanjing"));
        mSticker2DItems.add(new StickerItem(context.getString(R.string.sticker_chitushaonv), R.drawable.icon_chitushaonv, "stickers/chitushaonv"));
        mSticker2DItems.add(new StickerItem(context.getString(R.string.sticker_huahua), R.drawable.icon_huahua, "stickers/huahua", context.getString(R.string.sticker_tip_huahua)));
        mSticker2DItems.add(new StickerItem(context.getString(R.string.sticker_zhaocaimao), R.drawable.icon_zhaocaimao, "stickers/zhaocaimao"));
        mSticker2DItems.add(new StickerItem(context.getString(R.string.sticker_wochaotian), R.drawable.icon_wochaotian, "stickers/wochaotian"));
        mSticker2DItems.add(new StickerItem(context.getString(R.string.sticker_xiatiandefeng), R.drawable.icon_xiatiandefeng, "stickers/xiatiandefeng", context.getString(R.string.sticker_tip_xiatiandefeng)));
        mSticker2DItems.add(new StickerItem(context.getString(R.string.sticker_shengrikuaile), R.drawable.icon_shengrikuaile, "stickers/shengrikuaile"));
        mSticker2DItems.add(new StickerItem(context.getString(R.string.sticker_zhutouzhuer), R.drawable.icon_zhutouzhuer, "stickers/zhutouzhuer"));
        mSticker2DItems.add(new StickerItem(context.getString(R.string.sticker_huanletuchiluobo), R.drawable.icon_huanletuchiluobo, "stickers/huanletuchiluobo"));
        return mSticker2DItems;
    }

    private List<StickerItem> getSticker3DItems() {
        List<StickerItem> mSticker3DItems = new ArrayList<>();
        Context context = DemoApplication.context();

        mSticker3DItems.add(new StickerItem(context.getString(R.string.filter_normal), R.drawable.clear, null));
        mSticker3DItems.add(new StickerItem(context.getString(R.string.sticker_zhuluojimaoxian), R.drawable.icon_zhuluojimaoxian, "stickers/zhuluojimaoxian", context.getString(R.string.sticker_tip_zhuluojimaoxian)));
        mSticker3DItems.add(new StickerItem(context.getString(R.string.sticker_nuannuandoupeng), R.drawable.icon_nuannuandoupeng, "stickers/nuannuandoupeng", context.getString(R.string.sticker_tip_nuannuandoupeng)));
        mSticker3DItems.add(new StickerItem(context.getString(R.string.sticker_haoqilongbao), R.drawable.icon_haoqilongbao, "stickers/haoqilongbao", context.getString(R.string.sticker_tip_haoqilongbao)));
        mSticker3DItems.add(new StickerItem(context.getString(R.string.sticker_konglongshiguangji), R.drawable.icon_konglongshiguangji, "stickers/konglongshiguangji", context.getString(R.string.sticker_tip_konglongshiguangji)));
        mSticker3DItems.add(new StickerItem(context.getString(R.string.sticker_konglongceshi), R.drawable.icon_konglongceshi, "stickers/konglongceshi", context.getString(R.string.sticker_tip_konglongceshi)));
        if (GlobalData.getInstance().use3D()) {
            mSticker3DItems.add(new StickerItem(context.getString(R.string.sticker_tryshoe01), R.drawable.icon_shoe1, "stickers/foot_try_shoe_01", context.getString(R.string.sticker_tip_tryshoe)));
            mSticker3DItems.add(new StickerItem(context.getString(R.string.sticker_tryshoe02), R.drawable.icon_shoe2, "stickers/foot_try_shoe_02", context.getString(R.string.sticker_tip_tryshoe)));
            mSticker3DItems.add(new StickerItem(context.getString(R.string.sticker_tryshoe03), R.drawable.icon_shoe3, "stickers/foot_try_shoe_03", context.getString(R.string.sticker_tip_tryshoe)));
        }
        return mSticker3DItems;
    }

    private List<StickerItem> getStickerAdvancedItems() {
        List<StickerItem> mStickerAdvancedItems = new ArrayList<>();
        Context context = DemoApplication.context();

        mStickerAdvancedItems.add(new StickerItem(context.getString(R.string.filter_normal), R.drawable.clear, null));
        mStickerAdvancedItems.add(new StickerItem(context.getString(R.string.sticker_shahua), R.drawable.icon_shahua, "stickers/shahua", context.getString(R.string.sticker_tip_shahua)));
        return mStickerAdvancedItems;
    }

    private List<StickerItem> getStickerComplexItems() {
        List<StickerItem> mStickerComplexItems = new ArrayList<>();
        Context context = DemoApplication.context();

        mStickerComplexItems.add(new StickerItem(context.getString(R.string.filter_normal), R.drawable.clear, null));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_maobing), R.drawable.icon_maobing, "stickers/maobing", context.getString(R.string.sticker_tip_snap_with_cats)));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_kongquegongzhu), R.drawable.icon_kongquegongzhu, "stickers/kongquegongzhu", context.getString(R.string.sticker_tip_kongquegongzhu)));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_eldermakup), R.drawable.icon_eldermakup, "stickers/eldermakup"));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_kidmakup), R.drawable.icon_kidmakup, "stickers/kidmakup"));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_zisemeihuo), R.drawable.icon_zisemeihuo, "stickers/zisemeihuo", context.getString(R.string.sticker_tip_zisemeihuo)));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_yanlidoushini), R.drawable.icon_yanlidoushini, "stickers/yanlidoushini", context.getString(R.string.sticker_tip_yanlidoushini)));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_xiaribingshuang), R.drawable.icon_xiaribingshuang, "stickers/xiaribignshuang", context.getString(R.string.sticker_tip_xiaribingshuang)));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_biaobaqixi), R.drawable.icon_biaobaiqixi, "stickers/biaobaiqixi", context.getString(R.string.sticker_tip_biaobaqixi)));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_cinamiheti), R.drawable.icon_cinamiheti, "stickers/cinamiheti", context.getString(R.string.sticker_tip_cinamiheti)));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_shuiliandong), R.drawable.icon_shuiliandong, "stickers/shuiliandong", context.getString(R.string.sticker_tip_shuiliandong)));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_mofabaoshi), R.drawable.icon_mofabaoshi, "stickers/mofabaoshi", context.getString(R.string.sticker_tip_mofabaoshi)));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_shangke), R.drawable.icon_shangke, "stickers/shangke", context.getString(R.string.sticker_tip_shangke)));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_baibianfaxing), R.drawable.icon_baibianfaxing, "stickers/baibianfaxing", context.getString(R.string.sticker_tip_baibianfaxing)));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_qianduoduo), R.drawable.icon_qianduoduo, "stickers/qianduoduo", context.getString(R.string.sticker_tip_qianduoduo)));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_meihaoxinqing), R.drawable.icon_meihaoxinqing, "stickers/meihaoxinqing", context.getString(R.string.sticker_tip_meihaoxinqing)));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_jiancedanshenyinyuan), R.drawable.icon_jiancedanshenyinyuan, "stickers/jiancedanshenyinyuan", context.getString(R.string.sticker_tip_jiancedanshenyinyuan)));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_shuihaimeigeqiutian), R.drawable.icon_shuihaimeigeqiutian, "stickers/shuihaimeigeqiutian", context.getString(R.string.sticker_tip_shuihaimeigeqiutian)));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_kejiganqueaixiong), R.drawable.icon_kejiganqueaixiong, "stickers/kejiganqueaixiong", context.getString(R.string.sticker_tip_kejiganqueaixiong)));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_mengguiyaotang), R.drawable.icon_mengguiyaotang, "stickers/mengguiyaotang", context.getString(R.string.sticker_tip_mengguiyaotang)));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_dianjita), R.drawable.icon_dianjita, "stickers/dianjita", context.getString(R.string.sticker_tip_dianjita)));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_xuyuanping), R.drawable.icon_xuyuanping, "stickers/xuyuanping"));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_katongnan), R.drawable.icon_katongnan, "stickers/katongnan"));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_katongnv), R.drawable.icon_katongnv, "stickers/katongnv"));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_jiamian), R.drawable.icon_jiamian, "stickers/jiamian", context.getString(R.string.sticker_tip_jiamian)));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_huanlongshu), R.drawable.icon_huanlongshu, "stickers/huanlongshu", context.getString(R.string.sticker_tip_huanlonghsu)));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_gongzhumianju), R.drawable.icon_gongzhumianju, "stickers/gongzhumianju"));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_shenshi), R.drawable.icon_shenshi, "stickers/shenshi"));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_luzhihuakuang), R.drawable.icon_luzhihuakuang, "stickers/luzhihuakuang", context.getString(R.string.sticker_tip_luzhihuakuang)));
//        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_duobibingdu), R.drawable.icon_duobibingdu, ResourceHelper.getGamePath(context, "duobibingdu")));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_qianshuiting), R.drawable.icon_qianshuiting, "game/qianshuiting", context.getString(R.string.sticker_tip_qianshuiting)));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_weixiaoyaotou), R.drawable.icon_weixiaoyaotou, "stickers/weixiaoyaotou"));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_zhangshangyouxiji), R.drawable.icon_zhangshangyouxiji, "stickers/zhangshangyouxiji", context.getString(R.string.sticker_tip_zhangshangyouxiji)));
        mStickerComplexItems.add(new StickerItem(context.getString(R.string.sticker_kuailexiaopingzi), R.drawable.icon_kuailexiaopingzi, "stickers/kuailexiaopingzi", context.getString(R.string.sticker_tip_kuailexiaopingzi)));
        return mStickerComplexItems;
    }

    private List<StickerItem> getAnimojiItems() {
        List<StickerItem> mAnimojiItems = new ArrayList<>();
        Context context = DemoApplication.context();
        mAnimojiItems.add(new StickerItem(context.getString(R.string.filter_normal), R.drawable.clear, null));
        mAnimojiItems.add(new StickerItem(context.getString(R.string.animoji_boy), R.drawable.icon_moji_boy, "animoji/animoji_boy"));
        mAnimojiItems.add(new StickerItem(context.getString(R.string.animoji_girl), R.drawable.icon_moji_girl, "animoji/animoji_girl"));
//        mAnimojiItems.add(new StickerItem(context.getString(R.string.avatar), R.drawable.icon_change_face, "animoji/Tob_avatar"));
        mAnimojiItems.add(new StickerItem(context.getString(R.string.animoji_caihongzhu), R.drawable.icon_caihongzhuxiaodi, "animoji/caihongzhu"));
        mAnimojiItems.add(new StickerItem(context.getString(R.string.animoji_fensebianbian), R.drawable.icon_fensebianbian, "animoji/fensebianbian"));
        mAnimojiItems.add(new StickerItem(context.getString(R.string.animoji_keaizhu), R.drawable.icon_keaizhu, "animoji/keaizhu"));
        mAnimojiItems.add(new StickerItem(context.getString(R.string.animoji_maoernvhai), R.drawable.icon_maoernvhai, "animoji/maoernvhai"));
        mAnimojiItems.add(new StickerItem(context.getString(R.string.animoji_bagequan), R.drawable.icon_bagequan, "animoji/bagequan"));
        mAnimojiItems.add(new StickerItem(context.getString(R.string.animoji_gewanggugu), R.drawable.icon_gewanggugu, "animoji/gewanggugu"));
        mAnimojiItems.add(new StickerItem(context.getString(R.string.animoji_duoshanbingqilin), R.drawable.icon_duoshanbingqilin, "animoji/duoshanbingqilin"));
        return mAnimojiItems;
    }

    private List<StickerItem> getARScanItems() {
        List<StickerItem> mARScanItems = new ArrayList<>();
        Context context = DemoApplication.context();
        mARScanItems.add(new StickerItem(context.getString(R.string.filter_normal), R.drawable.clear, null));

        mARScanItems.add(new StickerItem(context.getString(R.string.sticker_merry_christamas), R.drawable.icon_yanjlimdaidongxi, "stickers/merry_chrismas", context.getString(R.string.sticker_merry_christamas_tip)));
        return mARScanItems;
    }

    private List<StickerItem> getStyleMakeupItems() {
        List<StickerItem> items = new ArrayList<>();
        Context context = DemoApplication.context();

        items.add(new StickerItem(context.getString(R.string.filter_normal), R.drawable.clear, null));
        items.add(new StickerItem(context.getString(R.string.style_makeup_qise), R.drawable.icon_qise, "style_makeup/qise"));
        items.add(new StickerItem(context.getString(R.string.style_makeup_aidou), R.drawable.icon_aidou, "style_makeup/aidou"));
        items.add(new StickerItem(context.getString(R.string.style_makeup_youya), R.drawable.icon_youya, "style_makeup/youya"));
        items.add(new StickerItem(context.getString(R.string.style_makeup_cwei), R.drawable.icon_cwei, "style_makeup/cwei"));
        items.add(new StickerItem(context.getString(R.string.style_makeup_nuannan), R.drawable.icon_nuannan, "style_makeup/nuannan"));
        items.add(new StickerItem(context.getString(R.string.style_makeup_baixi), R.drawable.icon_baixi, "style_makeup/baixi"));
        items.add(new StickerItem(context.getString(R.string.style_makeup_wennuan), R.drawable.icon_wennuan, "style_makeup/wennuan"));
        items.add(new StickerItem(context.getString(R.string.style_makeup_shensui), R.drawable.icon_shensui, "style_makeup/shensui"));
        items.add(new StickerItem(context.getString(R.string.style_makeup_tianmei), R.drawable.icon_tianmei, "style_makeup/tianmei"));
        items.add(new StickerItem(context.getString(R.string.style_makeup_duanmei), R.drawable.icon_duanmei, "style_makeup/duanmei"));
        items.add(new StickerItem(context.getString(R.string.style_makeup_oumei), R.drawable.icon_oumei, "style_makeup/oumei"));
        items.add(new StickerItem(context.getString(R.string.style_makeup_zhigan), R.drawable.icon_zhigan, "style_makeup/zhigan"));
        items.add(new StickerItem(context.getString(R.string.style_makeup_hanxi), R.drawable.icon_hanxi, "style_makeup/hanxi"));
        items.add(new StickerItem(context.getString(R.string.style_makeup_yuanqi), R.drawable.icon_yuanqi, "style_makeup/yuanqi"));

        return items;
    }
}
