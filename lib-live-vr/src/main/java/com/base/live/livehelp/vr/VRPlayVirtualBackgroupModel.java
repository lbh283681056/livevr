package com.base.live.livehelp.vr;

import android.app.Activity;
import android.net.Uri;
import android.os.Message;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import com.asha.vrlib.MD360Director;
import com.asha.vrlib.MD360DirectorFactory;
import com.asha.vrlib.MDVRLibrary;
import com.asha.vrlib.model.BarrelDistortionConfig;
import com.asha.vrlib.model.MDPinchConfig;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by linbinghuang on 2016/12/5.
 * vr视频帮助类
 */
public class VRPlayVirtualBackgroupModel extends BaseVRPlayModel  {

    protected MDVRLibrary createVRLibrary(Activity activity,int playId) {
        int interactiveMode = MDVRLibrary.INTERACTIVE_MODE_TOUCH;
        return MDVRLibrary.with(activity)
                .displayMode(MDVRLibrary.DISPLAY_MODE_NORMAL)
                .interactiveMode(interactiveMode)
                .asVideo(new MDVRLibrary.IOnSurfaceReadyCallback() {
                    @Override
                    public void onSurfaceReady(Surface surface) {
                        mMediaPlayerWrapper.setSurface(surface);
                        handMsg(3, "onError");
                    }
                })
                .ifNotSupport(new MDVRLibrary.INotSupportCallback() {
                    @Override
                    public void onNotSupport(int mode) {
                        String tip = mode == MDVRLibrary.INTERACTIVE_MODE_MOTION_WITH_TOUCH
                                ? "onNotSupport:MOTION" : "onNotSupport:" + String.valueOf(mode);
                    }
                })
                .pinchConfig(new MDPinchConfig().setMin(1.0f).setMax(8.0f).setDefaultValue(0.1f))
                .pinchEnabled(true)
                .directorFactory(new MD360DirectorFactory() {
                    @Override
                    public MD360Director createDirector(int index) {
                        MD360Director.sss = 0.42f;
                        MD360Director.sss = 0.65f;
                        return MD360Director.builder().setsNear(0.65f).setLookX(-500).build();
                    }
                })
                .projectionFactory(new CustomProjectionFactory())
                .barrelDistortionConfig(new BarrelDistortionConfig().setDefaultEnabled(false).setScale(0.95f))
                .build(playId);
    }
}
