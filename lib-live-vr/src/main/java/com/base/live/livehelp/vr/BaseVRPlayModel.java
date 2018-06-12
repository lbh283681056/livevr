package com.base.live.livehelp.vr;

import android.app.Activity;
import android.net.Uri;
import android.os.Message;
import android.view.Surface;
import android.view.View;

import com.asha.vrlib.MD360Director;
import com.asha.vrlib.MD360DirectorFactory;
import com.asha.vrlib.MDVRLibrary;
import com.asha.vrlib.model.BarrelDistortionConfig;
import com.asha.vrlib.model.MDPinchConfig;
import com.base.live.model.BaseLiveModel;

import tv.danmaku.ijk.media.player.IMediaPlayer;


/**
 * vr操作基类
 * Created by linbinghuang on 2018/4/22.
 */

public class BaseVRPlayModel  extends BaseLiveModel implements ILiveVRHelp {
    protected MediaPlayerWrapper mMediaPlayerWrapper = new MediaPlayerWrapper();
    protected MDVRLibrary mVRLibrary;
    protected void handMsg(int what, String msg) {
        if (mCallBackHandler != null) {
            Message message = mCallBackHandler.obtainMessage();
            message.what = what;
            message.obj = msg;
            mCallBackHandler.sendMessage(message);
        }
    }
    @Override
    public void onResume() {
        mVRLibrary.onResume(mActivity);
        mMediaPlayerWrapper.resume();
    }

    @Override
    public void onPause() {
        mVRLibrary.onPause(mActivity);
        mMediaPlayerWrapper.pause();
    }
    @Override
    public void onCreate(View playView) {
        mVRLibrary = createVRLibrary(mActivity, playView.getId());
        mMediaPlayerWrapper.init();
        mMediaPlayerWrapper.setPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                if (mVRLibrary != null) {
                    mVRLibrary.notifyPlayerChanged();
                    handMsg(1, "Prepared");
                }
            }
        });

        mMediaPlayerWrapper.getPlayer().setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                handMsg(2, "onError");
                return true;
            }
        });

        mMediaPlayerWrapper.getPlayer().setOnVideoSizeChangedListener(new IMediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
                mVRLibrary.onTextureResize(width, height);
                handMsg(3, "onError");
            }
        });
    }
    @Override
    public void onDestroy() {
        try {
            mVRLibrary.onDestroy();
            if (mMediaPlayerWrapper != null) {
                mMediaPlayerWrapper.destroy();
            }
            super.onDestroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void start(String url) {
        mMediaPlayerWrapper.openRemoteFile(Uri.parse(url).toString());
        mMediaPlayerWrapper.prepare();
    }
    @Override
    public void stop() {
        if (mMediaPlayerWrapper != null) {
            mMediaPlayerWrapper.destroy();
        }
    }
    @Override
    public void reStar(String url) {
        mMediaPlayerWrapper = new MediaPlayerWrapper();
        mMediaPlayerWrapper.init();
        mMediaPlayerWrapper.setPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                if (mVRLibrary != null) {
                    mVRLibrary.notifyPlayerChanged();
                    handMsg(1, "Prepared");

                }
            }
        });

        mMediaPlayerWrapper.getPlayer().setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                handMsg(2, "onError");
                return true;
            }
        });

        mMediaPlayerWrapper.getPlayer().setOnVideoSizeChangedListener(new IMediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
                mVRLibrary.onTextureResize(width, height);
                handMsg(3, "onError");
            }
        });


    }


    @Override
    public void onConfigurationChanged(Activity activity) {
        mVRLibrary.onOrientationChanged(activity);
    }
    @Override
    public void switchDisplayMode(Activity mActivity, int i) {
        mVRLibrary.switchDisplayMode(mActivity, i);
    }
    @Override
    public void switchInteractiveMode(Activity mActivity, int i) {
        mVRLibrary.switchInteractiveMode(mActivity, i);
    }

    protected MDVRLibrary createVRLibrary(Activity activity, int playId) {
        int interactiveMode = MDVRLibrary.INTERACTIVE_MODE_MOTION_WITH_TOUCH;
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
                        return MD360Director.builder().setPitch(90).build();
                    }
                })
                .projectionFactory(new CustomProjectionFactory())
                .barrelDistortionConfig(new BarrelDistortionConfig().setDefaultEnabled(false).setScale(0.95f))
                .build(playId);
    }


    @Override
    public void capturePicture(String path) {

    }

    @Override
    public void setAudioEnable(boolean enable) {

    }

    @Override
    public void setVideoEnable(boolean enable) {

    }

}
