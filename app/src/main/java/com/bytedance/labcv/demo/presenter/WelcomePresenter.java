package com.bytedance.labcv.demo.presenter;

import android.content.Context;
import android.content.pm.PackageManager;

import com.bytedance.labcv.demo.ui.DemoApplication;
import com.bytedance.labcv.demo.ui.ResourceHelper;
import com.bytedance.labcv.demo.presenter.contract.WelcomeContract;
import com.bytedance.labcv.demo.task.UnzipTask;
import com.bytedance.labcv.demo.utils.UserData;

/**
 * Created by QunZhang on 2019-07-20 17:30
 */
public class WelcomePresenter extends WelcomeContract.Presenter implements UnzipTask.IUnzipViewCallback {
    private UserData mUserData;

    public WelcomePresenter() {
        mUserData = UserData.getInstance(DemoApplication.context());
    }

    @Override
    public void startTask() {
        UnzipTask mTask = new UnzipTask(this);
        mTask.execute(UnzipTask.DIR);
    }

    @Override
    public int getVersionCode() {
        Context context = DemoApplication.context();
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public String getVersionName() {
        Context context = DemoApplication.context();
        try {
            return "v " + context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public boolean resourceReady() {
        return mUserData.isResourceReady() && mUserData.getVersion() == getVersionCode();
    }

    @Override
    public Context getContext() {
        return DemoApplication.context();
    }

    @Override
    public void onStartTask() {
        if (isAvailable()) {
            getView().onStartTask();
        }
    }

    @Override
    public void onEndTask(boolean result) {
        if (getView() == null) return;
        if (result) {
            mUserData.setResourceReady(true);
            mUserData.setVersion(getVersionCode());
        }
        if (isAvailable()) {
            getView().onEndTask(result);
        }
    }
}
