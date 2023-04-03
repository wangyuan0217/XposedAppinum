package com.example.xposeddemo.handlesAcs;

import android.app.Activity;

import com.example.xposeddemo.utils.CLogUtils;
import com.zhenxi.Superappium.PageManager;
import com.zhenxi.Superappium.SwipeUtils;
import com.zhenxi.Superappium.ViewImage;

/**
 * @author Zhenxi on 2020-07-03
 */
public class SecondAcHandler implements PageManager.ActivityFocusHandler {


    @Override
    public boolean handleActivity(Activity activity, ViewImage root) {
        //点击确定按钮 可以根据ID查找或者其他属性查找
        //boolean isSucess = root.clickByXpath("//android.widget.Button[@id='android:id/button1']");
        boolean isSucess = root.clickByXpath("//android.widget.Button[@text='确定']");
        CLogUtils.e("点击按钮完毕 " + isSucess);

        SwipeUtils.simulateScroll(200, 200, 500, 500);

        return true;
    }
}
