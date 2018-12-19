package com.kcx.acg.views.callback;

import android.media.MediaPlayer;

public interface IMPlayListener {

    void onStart(IMPlayer player);
    void onPause(IMPlayer player);
    void onResume(IMPlayer player);
    void onComplete(IMPlayer player);
    void onBufferingUpdate(MediaPlayer mp, int percent);
    void onPrepared(MediaPlayer mp) ;
}
