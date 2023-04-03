package com.trump.appinum;


import com.zhenxi.Superappium.PageManager;
import com.zhenxi.Superappium.utils.CLogUtils;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class MainHook implements IXposedHookLoadPackage {


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {

        if (lpparam.processName.equals("com.example.xposedappium")) {
            CLogUtils.e("发现匹配的App");
            //设置页面Handler 两秒后开始执行
            PageManager.setTaskDuration(2000);
            //添加需要处理的Activity
            AddHandleActivity();
        }

    }

    private void AddHandleActivity() {
//        PageManager.addHandler("com.example.xposedappium.ui.login.LoginActivity",
//                new LoginAcHandler());
//        PageManager.addHandler("com.example.xposedappium.ui.login.SecondActivity",
//                new SecondAcHandler());
//        CLogUtils.e("AddHandleActivity 注册完毕 ");
    }
}
