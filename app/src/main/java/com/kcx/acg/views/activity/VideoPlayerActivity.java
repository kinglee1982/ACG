package com.kcx.acg.views.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GestureDetectorCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

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
import com.kcx.acg.views.view.CustomToast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by  o on 2018/9/7.
 */

public class VideoPlayerActivity extends BaseActivity implements PlayerGestureListener.VideoGestureListener {

    public static final String KEY_VIDEO_ITEM = "key_video_item";
    public static final String KEY_VIDEO_POSITION = "key_video_position";
    public static final String KEY_VIDEO_LIST = "key_video_list";

    private View rootView;
    private VideoView videoView;
    private String advertisUrl;
    private ImageView backIv;
    private ImageView backWindIv;
    private ImageView fastForwardIv;
    private ImageView pauseIv;
    private ImageView switchIv;
    private ImageView advertisIv;
    private ImageView advertisCloseIv;
    private LinearLayout bannerNextLayout;
    private LinearLayout rePlayLayout;
    private LinearLayout loadingLayout;
    private LinearLayout bannerLayout;
    private LinearLayout errorPortLayout;
    private LinearLayout errorLandLayout;
    private FrameLayout advertisLayout;
    private TextView reLoadPortTv;
    private TextView reLoadLandTv;
    private TextView nextTv;

    private TextView currentTimeTv;
    private SeekBar proSeekBar;
    private TextView totalTimeTv;
    private TextView toastTimeTv;
    private LinearLayout controlLayout;
    private boolean showControl = false;
    private boolean flag = true;
    private int videoPos;
    private List<GetProductInfoBean.ReturnDataBean.DetailListBean> videoList;
    private GetProductInfoBean.ReturnDataBean.DetailListBean item;

    private GestureDetectorCompat mDetector;
    private PlayerGestureListener playerGestureListener;

    @Override
    public View setInitView() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_video_player, null);
        videoView = rootView.findViewById(R.id.video_player_video_view);
        backIv = rootView.findViewById(R.id.video_player_back_iv);
        backWindIv = rootView.findViewById(R.id.video_player_back_wind_iv);
        pauseIv = rootView.findViewById(R.id.video_player_pause_iv);
        fastForwardIv = rootView.findViewById(R.id.video_player_fast_forward_iv);
        switchIv = rootView.findViewById(R.id.video_player_switch_iv);
        advertisIv = rootView.findViewById(R.id.video_player_advertis_iv);
        advertisCloseIv = rootView.findViewById(R.id.video_player_advertis_close_iv);
        loadingLayout = rootView.findViewById(R.id.video_player_loading_layout);
        bannerLayout = rootView.findViewById(R.id.video_player_banner_layout);
        bannerNextLayout = rootView.findViewById(R.id.video_player_next_layout);
        rePlayLayout = rootView.findViewById(R.id.video_player_re_play_layout);
        currentTimeTv = rootView.findViewById(R.id.video_player_current_time_tv);
        proSeekBar = rootView.findViewById(R.id.video_player_pro_seekbar);
        errorPortLayout = rootView.findViewById(R.id.video_player_error_port_layout);
        errorLandLayout = rootView.findViewById(R.id.video_player_error_land_layout);
        totalTimeTv = rootView.findViewById(R.id.video_player_total_time_tv);
        toastTimeTv = rootView.findViewById(R.id.video_player_toast_tv);
        controlLayout = rootView.findViewById(R.id.video_player_control_layout);
        reLoadPortTv = rootView.findViewById(R.id.video_player_error_port_tv);
        reLoadLandTv = rootView.findViewById(R.id.video_player_error_land_tv);
        nextTv = rootView.findViewById(R.id.video_player_next_tv);
        advertisLayout = rootView.findViewById(R.id.video_player_advertis_layout);

        rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        mDetector = new GestureDetectorCompat(this, playerGestureListener = new PlayerGestureListener(this, this));
        playerGestureListener.setVideoWH(SysApplication.mWidthPixels, SysApplication.mHeightPixels);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(networkReceiver, filter);
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
    public void initData() {
        getAdvertisementInfo(Constants.KEY_ADVERTIS_VIDEO);
        item = (GetProductInfoBean.ReturnDataBean.DetailListBean) getIntent().getExtras().getSerializable(KEY_VIDEO_ITEM);
        videoPos = getIntent().getIntExtra(KEY_VIDEO_POSITION, 0);
        videoList = (List<GetProductInfoBean.ReturnDataBean.DetailListBean>) getIntent().getExtras().getSerializable(KEY_VIDEO_LIST);
        if (videoList.size() == 1) {
            bannerNextLayout.setVisibility(View.GONE);
        } else {
            rePlayLayout.setVisibility(View.GONE);
        }
        if (videoPos == videoList.size() - 1) {
            nextTv.setText(getString(R.string.video_player_first_msg));
        }
//        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "ceshi.mp4";
//        path = "http://192.168.3.247:8080/uploads/video/ceshi2.mp4";
//        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ceshi2));

    }

    private Handler handler = new Handler();
    int time;
    int buffer;
    int currentPosition;
    int duration;
    private Runnable run = new Runnable() {

        public void run() {
            if (!flag) {
                return;
            }
            // 获得当前播放时间和当前视频的长度

            currentPosition = videoView.getCurrentPosition();
            duration = videoView.getDuration();
            time = ((currentPosition * 100) / duration);
            // 设置进度条的主要进度，表示当前的播放时间
            proSeekBar.setProgress(time);
            // 设置进度条的次要进度，表示视频的缓冲进度
            buffer = videoView.getBufferPercentage();
//            proSeekBar.setSecondaryProgress(buffer);
//            proSeekBar.setSecondaryProgress(percent);
            if (currentPosition / 1000 / 60 < 10) {
                if (currentPosition / 1000 % 60 < 10)
                    currentTimeTv.setText("0" + currentPosition / 1000 / 60 + ":0" + currentPosition / 1000 % 60);
                else
                    currentTimeTv.setText("0" + currentPosition / 1000 / 60 + ":" + currentPosition / 1000 % 60);
            } else {
                if (currentPosition / 1000 % 60 < 10)
                    currentTimeTv.setText(currentPosition / 1000 / 60 + ":0" + currentPosition / 1000 % 60);
                else
                    currentTimeTv.setText(currentPosition / 1000 / 60 + ":" + currentPosition / 1000 % 60);
            }
            toastTimeTv.setText(currentTimeTv.getText() + "/" + totalTimeTv.getText());
            proSeekBar.postDelayed(run, 1000);
        }
    };

    public void getAdvertisementInfo(int locationID) {
        GetAdvertisementInfoApi getAdvertisementInfoApi = new GetAdvertisementInfoApi(this);
        getAdvertisementInfoApi.setLocationID(locationID);
        getAdvertisementInfoApi.setListener(new HttpOnNextListener<GetAdvertisementInfoBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(GetAdvertisementInfoBean getAdvertisementInfoBean) {
                if (getAdvertisementInfoBean.getReturnData() != null)

                    if (getAdvertisementInfoBean.getErrorCode() == 200) {
                    if (getAdvertisementInfoBean != null) {
                        advertisUrl = getAdvertisementInfoBean.getReturnData().getTargetUrl();
                        Glide.with(VideoPlayerActivity.this).load(getAdvertisementInfoBean.getReturnData().getImageUrl()).into(advertisIv);
                    } else {
                        advertisLayout.setVisibility(View.GONE);
                    }
                }
                return null;
            }

        });

        HttpManager.getInstance().doHttpDeal(this, getAdvertisementInfoApi);
    }

    boolean isPause = true;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void setListener() {
        backIv.setOnClickListener(this);
        backWindIv.setOnClickListener(this);
        pauseIv.setOnClickListener(this);
        fastForwardIv.setOnClickListener(this);
        switchIv.setOnClickListener(this);
        bannerLayout.setOnClickListener(this);
        bannerNextLayout.setOnClickListener(this);
        advertisCloseIv.setOnClickListener(this);
        advertisIv.setOnClickListener(this);
        reLoadPortTv.setOnClickListener(this);
        reLoadLandTv.setOnClickListener(this);
        rePlayLayout.setOnClickListener(this);

        videoView.setOnClickListener(this);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mediaPlayer) {
                mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mp) {
                        // seekTo 方法完成时的回调
                        if (isPause) {
                            videoView.start();
                            isPause = false;
                            flag = true;
                        }
                    }
                });

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                        int duration = videoView.getDuration();
                        if (duration / 1000 / 60 < 10) {
                            if (duration / 1000 % 60 < 10)
                                totalTimeTv.setText("0" + duration / 1000 / 60 + ":0" + duration / 1000 % 60);
                            else
                                totalTimeTv.setText("0" + duration / 1000 / 60 + ":" + duration / 1000 % 60);
                        } else {
                            if (duration / 1000 % 60 < 10)
                                totalTimeTv.setText(duration / 1000 / 60 + ":0" + duration / 1000 % 60);
                            else
                                totalTimeTv.setText(duration / 1000 / 60 + ":" + duration / 1000 % 60);
                        }
                    }
                });
            }
        });

        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
                switch (i) {
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        //开始卡顿-----需要做一些处理(比如：显示加载动画，或者当前下载速度)
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        //卡顿结束   (隐藏加载动画，或者加载速度)
                        break;
                }
                return false;
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(isShowLoading){
                    bannerLayout.setVisibility(View.VISIBLE);
                }
                handler.removeCallbacks(run);
                pauseIv.setImageResource(R.mipmap.icon_suspend);
                proSeekBar.setProgress(0);
                currentTimeTv.setText("00:00");
                mTimer.cancel();
                flag = false;
                fastForwardIv.setEnabled(false);
                backWindIv.setEnabled(false);
            }
        });
        proSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    videoView.seekTo(videoView.getDuration() * seekBar.getProgress() / 100);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        videoView.setOnTouchListener(new View.OnTouchListener() {
            float x;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                LogUtils.d("----- " + motionEvent.getAction());
                if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    //如果在快进状态，则检测到这两个手势代表快进结束：因为GestureDetectorCompat不能检测手势抬起的缘故，所以要自己检测
                    //做一些操作：如播放器快进 seekTo()
                }
                return mDetector.onTouchEvent(motionEvent);
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                isShowLoading = false;
                if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    errorPortLayout.setVisibility(View.VISIBLE);
                } else {
                    errorLandLayout.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.setVideoPath(item.getUrl());
        videoView.start();
        handler.post(run);
        start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_player_back_iv:
                if (getResources().getConfiguration().orientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    switchIv.setImageResource(R.mipmap.video_fullscreen);
                } else {
                    finish();
                }
                break;
            case R.id.video_player_back_wind_iv:
                isPause = true;
                flag = false;
                videoView.pause();
                LogUtils.d("--- " + (videoView.getCurrentPosition() - 5000));
                videoView.seekTo((videoView.getCurrentPosition() - 5000));
                currentPosition = videoView.getCurrentPosition() - 5000;
                break;
            case R.id.video_player_fast_forward_iv:
                isPause = true;
                flag = false;
                videoView.pause();
                LogUtils.d("--- " + (videoView.getCurrentPosition() + 10000));
                videoView.seekTo((videoView.getCurrentPosition() + 10000));
                currentPosition = videoView.getCurrentPosition() + 10000;
                break;
            case R.id.video_player_pause_iv:
                if (videoView.isPlaying()) {
                    videoView.pause();
                    pauseIv.setImageResource(R.mipmap.icon_suspend);
                } else {
                    videoView.start();
                    pauseIv.setImageResource(R.mipmap.video_pause);
                    fastForwardIv.setEnabled(true);
                    backWindIv.setEnabled(true);
                    bannerLayout.setVisibility(View.GONE);
                    handler.post(run);
                    start();
                    flag = true;
                }
                break;
            case R.id.video_player_switch_iv:
                if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                    switchIv.setImageResource(R.mipmap.video_vertical);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                    switchIv.setImageResource(R.mipmap.video_fullscreen);
                }
                break;
            case R.id.video_player_video_view:

                break;
            case R.id.video_player_next_layout:
                if (videoPos == videoList.size() - 1) {
                    nextTv.setText(getString(R.string.video_player_first_msg));
                    videoView.setVideoPath(videoList.get(0).getUrl());
                } else {
                    videoPos = videoPos + 1;
                    videoView.setVideoPath(videoList.get(videoPos).getUrl());
                    if (videoPos == videoList.size() - 1) {
                        nextTv.setText(getString(R.string.video_player_first_msg));
                    }
                }
                videoView.start();
                handler.post(run);
                flag = true;
                bannerLayout.setVisibility(View.GONE);
                pauseIv.setImageResource(R.mipmap.video_pause);
                break;
            case R.id.video_player_advertis_iv:
                Intent intent = new Intent(VideoPlayerActivity.this, AdvertisActivity.class);
                intent.putExtra(AdvertisActivity.KEY_ADVERTIS_URL, advertisUrl);
                startDDMActivity(intent, true);
                return;
            case R.id.video_player_advertis_close_iv:
                bannerLayout.setVisibility(View.GONE);
                return;
            case R.id.video_player_error_port_tv:
                videoView.setVideoPath(item.getUrl());
                videoView.start();
                handler.post(run);
                start();
                break;
            case R.id.video_player_error_land_tv:
                videoView.setVideoPath(item.getUrl());
                videoView.start();
                handler.post(run);
                start();
                break;
            case R.id.video_player_re_play_layout:
                videoView.start();
                pauseIv.setImageResource(R.mipmap.video_pause);
                fastForwardIv.setEnabled(true);
                backWindIv.setEnabled(true);
                bannerLayout.setVisibility(View.GONE);
                handler.post(run);
                start();
                flag = true;
                break;
        }
    }

    //定义1s前播放位置
    private long lastPosition;
    private Timer mTimer;
    boolean isShowLoading = true;

    private void start() {
        mTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                final long dis = getVideoViewDelay();
                //如果正常播放，dis应该等于1000ms，如果小于1000ms,则有卡顿的嫌疑，小于500ms基本可以断定卡顿严重
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dis < 500 && isShowLoading) {
                            loadingLayout.setVisibility(View.VISIBLE);
                            //视频卡顿，-------做一些卡顿处理(比如：显示加载动画，或者当前下载速度)
                        } else {
                            loadingLayout.setVisibility(View.GONE);
                            //视频卡顿结束(判断加载动画是否显示，如果显示则隐藏)
                        }
                    }
                });

            }
        };

        //开启检测前，先获取一次进度(主要是为了给lastPosition赋值)
        getVideoViewDelay();
        //1s之后开启检测
        mTimer.schedule(task, 1000, 1000);
    }

    /**
     * [currentPosition 获取延时]
     *
     * @type {[type]}
     */
    private long getVideoViewDelay() {
        //获取当前播放位置
        long currentPosition = videoView.getCurrentPosition();
        //计算指定时间间隔实际播放的进度
        long dis = currentPosition - lastPosition;

        lastPosition = currentPosition;
        return dis;
    }


    @Override
    public void onGestureLeftTB(float ratio) {

    }

    @Override
    public void onGestureRightTB(float ratio) {

    }

    @Override
    public void onGestureUpdateVideoTime(int duration) {
        LogUtils.d(duration + "--------------");
        if (duration > 0) {
            proSeekBar.setProgress(time++);
        } else {
            proSeekBar.setProgress(time--);
        }
        videoView.seekTo(videoView.getDuration() * proSeekBar.getProgress() / 100);
        currentPosition = videoView.getDuration() * proSeekBar.getProgress() / 100;
    }

    @Override
    public void onGestureSingleClick() {
        if (showControl)
            controlLayout.setVisibility(View.VISIBLE);
        else
            controlLayout.setVisibility(View.GONE);
        showControl = !showControl;
    }

    @Override
    public void onGestureDoubleClick() {

    }

    @Override
    public void onGestureDown() {

    }

    @Override
    public void onLongPress() {
        toastTimeTv.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toastTimeTv.setVisibility(View.GONE);
            }
        }, 5000);
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
