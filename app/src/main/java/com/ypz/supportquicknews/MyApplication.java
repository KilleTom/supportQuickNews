package com.ypz.supportquicknews;

import android.app.Application;
import android.util.Log;

import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.tencent.bugly.crashreport.CrashReport;
import com.ypz.supportquicknews.preservationData.PreservationDataSP;
import com.ypz.supportquicknews.rxUtil.MyBmobUtil;

/**
 * Created by kingadmin on 2018/2/7.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            CrashReport.initCrashReport(getApplicationContext(), "a2d52d13fd", true);
        } catch (Exception e) {
            Log.i("ypz",e.getMessage());
        }
        SophixManager.getInstance().setContext(this)
                .setAppVersion("1.0")
                .setAesKey(null)
                .setEnableDebug(true)
                .setPatchLoadStatusStub((i, i1, s, i2) -> {
                    switch (i1) {
                        case PatchStatus.CODE_LOAD_SUCCESS:
                            Log.i("ypzSophixManager", "加载成功");
                            PreservationDataSP.init(this);
                            break;
                        case PatchStatus.CODE_LOAD_RELAUNCH:
                            Log.i("ypzSophixManager", "app需要重启");
                            PreservationDataSP.init(this);
                            break;
                    }
                    Log.i("ypz", i1 + "");
                    Log.i("ypz", s + "");
                }).initialize();
       SophixManager.getInstance().queryAndLoadNewPatch();
        try {
            PreservationDataSP.init(this);
        } catch (Exception e) {
            Log.i("ypz",e.getMessage());
        }

        try {
            MyBmobUtil.initMyBmobUtil(this);
        } catch (Exception e) {
            Log.i("ypzBmob",e.getMessage()+"Bmob异常");
        }
    }
}
