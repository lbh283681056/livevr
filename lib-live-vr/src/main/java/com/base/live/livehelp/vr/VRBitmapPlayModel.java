package com.base.live.livehelp.vr;


import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;

import com.asha.vrlib.MDVRLibrary;
import com.asha.vrlib.model.MDRay;
import com.asha.vrlib.plugins.IMDHotspot;
import com.asha.vrlib.texture.MD360BitmapTexture;


/**
 * Created by linbinghuang on 2017/5/17.
 */

public class VRBitmapPlayModel extends BaseVRPlayModel  {
    private IVrBitmapCallBack iVrBitmapCallBack;
    private Uri uri;
    protected MDVRLibrary createVRLibrary(Activity activity, int playid) {
        final Activity a = activity;
        return MDVRLibrary.with(activity)
                .displayMode(MDVRLibrary.DISPLAY_MODE_NORMAL)
                .interactiveMode(MDVRLibrary.INTERACTIVE_MODE_MOTION_WITH_TOUCH)

                .asBitmap(new MDVRLibrary.IBitmapProvider() {
                    @Override
                    public void onProvideBitmap(final MD360BitmapTexture.Callback callback) {
                        if(iVrBitmapCallBack!=null) {
                            iVrBitmapCallBack.loadImage(a, uri, callback);
                        }
                    }
                })
                .listenTouchPick(new MDVRLibrary.ITouchPickListener() {
                    @Override
                    public void onHotspotHit(IMDHotspot hitHotspot, MDRay ray) {
                    }
                })
                .pinchEnabled(true)
                .projectionFactory(new CustomProjectionFactory())
                .build(playid);
    }

    /**
     * 设置回调
     * 属性 iVrBitmapCallBack
     */
    public void setVrBitmapCallBack(IVrBitmapCallBack iVrBitmapCallBack) {
        this.iVrBitmapCallBack = iVrBitmapCallBack;
    }

    /**
     * 设置资源
     * 属性 resource
     * 属性 callback1
     */
    public void setResource(Bitmap resource,MD360BitmapTexture.Callback callback1){
        mVRLibrary.onTextureResize(resource.getWidth(), resource.getHeight());
        if (callback1 != null) {
            callback1.texture(resource);
        }
    }
    @Override
    public void start(String url) {
        uri = Uri.parse(url);
        mVRLibrary.notifyPlayerChanged();
    }

    @Override
    public void stop() {

    }


    @Override
    public void reStar(String url) {

    }

    @Override
    public void switchInteractiveMode(Activity mActivity, int i) {

    }

    @Override
    public void switchDisplayMode(Activity mActivity, int i) {

    }



}
