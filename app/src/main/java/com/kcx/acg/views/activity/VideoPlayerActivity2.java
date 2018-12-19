package com.kcx.acg.views.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GestureDetectorCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.kcx.acg.R;
import com.kcx.acg.api.GetAdvertisementInfoApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.base.SysApplication;
import com.kcx.acg.bean.GetAdvertisementInfoBean;
import com.kcx.acg.bean.GetProductInfoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.views.callback.PlayerGestureListener;
import com.kcx.acg.views.view.CustomMediaController;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.MyMediaController;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by  o on 2018/9/7.
 */

public class VideoPlayerActivity2 extends BaseActivity implements MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener {

    public static final String KEY_VIDEO_ITEM = "key_video_item";
    public static final String KEY_VIDEO_POSITION = "key_video_position";
    public static final String KEY_VIDEO_LIST = "key_video_list";

    private View rootView;
    private VideoView videoView;

    private LinearLayout loadingLayout;
    private LinearLayout errorPortLayout;
    private LinearLayout errorLandLayout;
    private TextView reLoadPortTv;
    private TextView reLoadLandTv;

    private RelativeLayout mRelatviLayout;


    private TextView currentTimeTv;
    private TextView totalTimeTv;
    private TextView toastTimeTv;
    private LinearLayout controlLayout;
    private boolean showControl = false;
    private int videoPos;
    private List<GetProductInfoBean.ReturnDataBean.DetailListBean> videoList;
    private GetProductInfoBean.ReturnDataBean.DetailListBean item;
    private MyMediaController mCustomMediaController;
    private Uri uri;

    @Override
    public View setInitView() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_video_player2, null);
        videoView = rootView.findViewById(R.id.video_player_video_view);
        loadingLayout = rootView.findViewById(R.id.video_player_loading_layout);

        currentTimeTv = rootView.findViewById(R.id.video_player_current_time_tv);
        errorPortLayout = rootView.findViewById(R.id.video_player_error_port_layout);
        errorLandLayout = rootView.findViewById(R.id.video_player_error_land_layout);
        totalTimeTv = rootView.findViewById(R.id.video_player_total_time_tv);
        toastTimeTv = rootView.findViewById(R.id.video_player_toast_tv);
        controlLayout = rootView.findViewById(R.id.video_player_control_layout);
        reLoadPortTv = rootView.findViewById(R.id.video_player_error_port_tv);
        reLoadLandTv = rootView.findViewById(R.id.video_player_error_land_tv);
        mRelatviLayout = rootView.findViewById(R.id.rela);
        rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(networkReceiver, filter);

        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //获得当前窗体对象
        Window window = this.getWindow();
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
        //必须写这个，初始化加载库文件
        Vitamio.isInitialized(this);
        //设置视频解码监听
        return rootView;
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            rootView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            setFullScreen();
//        } else {
//            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    600);
//            mRelatviLayout.setLayoutParams(layoutParams);
//            RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
//                    RelativeLayout.LayoutParams.MATCH_PARENT,
//                    RelativeLayout.LayoutParams.MATCH_PARENT);
//            videoView.setLayoutParams(layoutParams1);
//        }
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏
//            mIsFullScreen = true;
            //去掉系统通知栏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            hideViews(true);
            //调整mFlVideoGroup布局参数
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout
                    .LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            mRelatviLayout.setGravity(Gravity.CENTER);
            mRelatviLayout.setLayoutParams(params);
            //原视频大小
//            public static final int VIDEO_LAYOUT_ORIGIN = 0;
            //最优选择，由于比例问题还是会离屏幕边缘有一点间距，所以最好把父View的背景设置为黑色会好一点
//            public static final int VIDEO_LAYOUT_SCALE = 1;
            //拉伸，可能导致变形
//            public static final int VIDEO_LAYOUT_STRETCH = 2;
            //会放大可能超出屏幕
//            public static final int VIDEO_LAYOUT_ZOOM = 3;
            //效果还是竖屏大小（字面意思是填充父View）
//            public static final int VIDEO_LAYOUT_FIT_PARENT = 4;
            videoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
        } else {
//            mIsFullScreen = false;
            /*清除flag,恢复显示系统状态栏*/
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            hideViews(false);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout
                    .LayoutParams.MATCH_PARENT,
                    FrameLayout
                            .LayoutParams.MATCH_PARENT);
            mRelatviLayout.setGravity(Gravity.CENTER);
            mRelatviLayout.setLayoutParams(params);

            RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            videoView.setLayoutParams(layoutParams1);
            videoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
        }
    }

    private void setFullScreen() {

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mRelatviLayout.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        videoView.setLayoutParams(layoutParams1);
    }

    @Override
    public void initData() {

        item = (GetProductInfoBean.ReturnDataBean.DetailListBean) getIntent().getExtras().getSerializable(KEY_VIDEO_ITEM);
        videoPos = getIntent().getIntExtra(KEY_VIDEO_POSITION, 0);
        videoList = (List<GetProductInfoBean.ReturnDataBean.DetailListBean>) getIntent().getExtras().getSerializable(KEY_VIDEO_LIST);
        mCustomMediaController = new MyMediaController(this, videoView, this, videoList.size(), videoPos);
//        String path = "http://vfx.mtime.cn/Video/2018/11/13/mp4/181113225325404977.mp4";
        String path = videoList.get(videoPos).getUrl();
        uri = Uri.parse(path);
        String cache = "cache:/sdcard/DICM/" + path.substring(path.lastIndexOf("/") + 1, path.length()) + ":" + uri;
        videoView.setVideoURI(uri);//设置视频播放地址
        mCustomMediaController.show(5);
        videoView.setMediaController(mCustomMediaController);

        videoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);//高画质
        videoView.requestFocus();
        videoView.setBufferSize(512 * 1024);
    }

    @Override
    public void setListener() {
        reLoadPortTv.setOnClickListener(this);
        reLoadLandTv.setOnClickListener(this);
        videoView.setOnClickListener(this);
        videoView.setOnInfoListener(this);
        videoView.setOnBufferingUpdateListener(this);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setPlaybackSpeed(1.0f);
            }
        });
        mCustomMediaController.setOnReLoadListener(new MyMediaController.OnReLoadListener() {
            @Override
            public void onReload() {
//                videoView.stopPlayback();
                loadingLayout.setVisibility(View.VISIBLE);
                videoView.setVideoURI(uri);//设置视频播放地址
                videoView.start();
            }

            @Override
            public void onNext() {
//                loadingLayout.setVisibility(View.VISIBLE);

                if (videoPos == videoList.size() - 1) {
                    mCustomMediaController.nextTv.setText(getString(R.string.video_player_first_msg));
                    videoView.setVideoPath(videoList.get(0).getUrl());
                } else {
                    videoPos = videoPos + 1;
                    videoView.setVideoPath(videoList.get(videoPos).getUrl());
                    if (videoPos == videoList.size() - 1) {
                        mCustomMediaController.nextTv.setText(getString(R.string.video_player_first_msg));
                    }
                }
                videoView.start();
            }
        });

        videoView.setOnCloseListener(new MediaPlayer.OnCloseListener() {
            @Override
            public void onClose() {
                finish();
            }
        });

    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                mCustomMediaController.show();
                if (videoView.isPlaying()) {
                    videoView.pause();
                    loadingLayout.setVisibility(View.VISIBLE);
//                    downloadRateView.setText("");
//                    loadRateView.setText("");
//                    downloadRateView.setVisibility(View.VISIBLE);
//                    loadRateView.setVisibility(View.VISIBLE);
                }
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                videoView.start();
                mCustomMediaController.isEnd = false;
                loadingLayout.setVisibility(View.GONE);
//                downloadRateView.setVisibility(View.GONE);
//                loadRateView.setVisibility(View.GONE);
                break;
            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
//                downloadRateView.setText("" + extra + "kb/s" + "  ");
                LogUtils.d("" + extra + "kb/s" + "  ");
                break;
        }
        return true;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
//        loadRateView.setText(percent + "%");
//        LogUtils.d(percent + "%");
        loadingLayout.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_player_error_port_tv:
                break;
            case R.id.video_player_error_land_tv:
                break;


        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.resume();
    }

    private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            NetworkInfo.State wifiState = null;
            NetworkInfo.State mobileState = null;
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
            if (wifiState != null && mobileState != null
                    && NetworkInfo.State.CONNECTED != wifiState
                    && NetworkInfo.State.CONNECTED == mobileState) {
                // 手机网络连接成功
                CustomToast.showToast(getString(R.string.play_video_3G_msg));
            } else if (wifiState != null && mobileState != null
                    && NetworkInfo.State.CONNECTED != wifiState
                    && NetworkInfo.State.CONNECTED != mobileState) {
                // 手机没有任何的网络
            } else if (wifiState != null && NetworkInfo.State.CONNECTED == wifiState) {
                // 无线网络连接成功
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkReceiver);
    }


}