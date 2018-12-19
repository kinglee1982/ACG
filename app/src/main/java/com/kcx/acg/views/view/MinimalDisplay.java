package com.kcx.acg.views.view;


import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.kcx.acg.views.callback.IMDisplay;
import com.kcx.acg.views.callback.IMPlayer;

/**
 * Description:
 */
public class MinimalDisplay implements IMDisplay {

    private SurfaceView surfaceView;

    public MinimalDisplay(SurfaceView surfaceView){
        this.surfaceView=surfaceView;
    }

    @Override
    public View getDisplayView() {
        return surfaceView;
    }

    @Override
    public SurfaceHolder getHolder() {
        return surfaceView.getHolder();
    }

    @Override
    public void onStart(IMPlayer player) {

    }

    @Override
    public void onPause(IMPlayer player) {

    }

    @Override
    public void onResume(IMPlayer player) {

    }

    @Override
    public void onComplete(IMPlayer player) {

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }
}