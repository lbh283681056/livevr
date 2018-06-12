package com.base.live.livehelp.vr;


import android.app.Activity;

/**
 * vr接口
 * Created by linbinghuang on 2017/7/31.
 */
public interface ILiveVRHelp {
    /**
     * 切换模式
     *
     * 属性 mActivity
     * 属性 i
     */
    void switchInteractiveMode(Activity mActivity, int i);
    /**
     * 切换模式
     *
     * 属性 mActivity
     * 属性 i
     */
    void switchDisplayMode(Activity mActivity, int i);
    /**
     * 转屏幕调用
     * 属性 activity
     */
    void onConfigurationChanged(Activity activity);


    /**
     * vr的生命周期
     */
     void onResume() ;

    /**
     * vr的生命周期
     */
     void onPause() ;

}
