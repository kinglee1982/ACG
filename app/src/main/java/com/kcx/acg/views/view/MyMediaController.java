package com.kcx.acg.views.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kcx.acg.R;
import com.kcx.acg.api.GetAdvertisementInfoApi;
import com.kcx.acg.bean.GetAdvertisementInfoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.views.activity.AdvertisActivity;
import com.kcx.acg.views.activity.VideoPlayerActivity2;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.utils.StringUtils;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by 鹤 on 2015/10/29.
 */
public class MyMediaController extends MediaController implements View.OnClickListener {

    private static final int HIDEFRAM = 0;
    private static final int SHOW_PROGRESS = 2;

    private GestureDetector mGestureDetector;
    private ImageView img_back;//返回键
    private ImageView img_Battery;//电池电量显示
    private TextView textViewTime;//时间提示
    private TextView textViewBattery;//文字显示电池
    private VideoView videoView;
    private Activity activity;
    private Context context;
    private int controllerWidth = 0;//设置mediaController高度为了使横屏时top显示在屏幕顶端

    private View mVolumeBrightnessLayout;
    private ImageView mOperationBg;
    private TextView mOperationTv;
    private AudioManager mAudioManager;

    private SeekBar seekBarProgress;
    private boolean progress_turn;
    private int progress;

    private boolean mDragging;
    private MediaPlayerControl player;
    //最大声音
    private int mMaxVolume;
    // 当前声音
    private int mVolume = -1;
    //当前亮度
    private float mBrightness = -1f;

    private ImageView pauseIv;
    private ImageView mIvScale;
    private ImageView backWindIv;
    private ImageView fastForwardIv;
    public TextView nextTv;
    private LinearLayout bannerNextLayout;
    private LinearLayout rePlayLayout;
    private LinearLayout bannerLayout;
    private ImageView advertisIv;
    private ImageView advertisCloseIv;
    private int resourceSize;
    private int videoPos;

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            long pos;
            switch (msg.what) {
                case HIDEFRAM:
                    mVolumeBrightnessLayout.setVisibility(View.GONE);
                    mOperationTv.setVisibility(View.GONE);
                    break;
            }
        }
    };


    //videoview 用于对视频进行控制的等，activity为了退出
    public MyMediaController(Context context, VideoView videoView, Activity activity, int resourceSize, int videoPos) {
        super(context);
        this.context = context;
        this.videoView = videoView;
        this.activity = activity;
        this.resourceSize = resourceSize;
        this.videoPos = videoPos;
        show();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        controllerWidth = wm.getDefaultDisplay().getWidth();
        mGestureDetector = new GestureDetector(context, new MyGestureListener());

    }

    @Override
    protected View makeControllerView() {
        getAdvertisementInfo(Constants.KEY_ADVERTIS_VIDEO);

        View v = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(getResources().getIdentifier("mymediacontroller", "layout", getContext().getPackageName()), this);
        v.setMinimumHeight(controllerWidth);
        //TOP
        img_back = v.findViewById(R.id.mediacontroller_top_back);
        seekBarProgress = (SeekBar) v.findViewById(R.id.media_controller_seekbar);
        pauseIv = (ImageView) v.findViewById(getResources().getIdentifier("video_player_pause_iv", "id", context.getPackageName()));
        backWindIv = (ImageView) v.findViewById(getResources().getIdentifier("video_player_back_wind_iv", "id", context.getPackageName()));
        fastForwardIv = (ImageView) v.findViewById(getResources().getIdentifier("video_player_fast_forward_iv", "id", context.getPackageName()));
        //缩放控件
        mIvScale = v.findViewById(getResources().getIdentifier("video_player_switch_iv", "id", context.getPackageName()));
        advertisIv = v.findViewById(getResources().getIdentifier("video_player_advertis_iv", "id", context.getPackageName()));
        advertisCloseIv = v.findViewById(getResources().getIdentifier("video_player_advertis_close_iv", "id", context.getPackageName()));
        bannerLayout = v.findViewById(getResources().getIdentifier("video_player_banner_layout", "id", context.getPackageName()));
        bannerNextLayout = v.findViewById(getResources().getIdentifier("video_player_next_layout", "id", context.getPackageName()));
        rePlayLayout = v.findViewById(getResources().getIdentifier("video_player_re_play_layout", "id", context.getPackageName()));
        nextTv = v.findViewById(getResources().getIdentifier("video_player_next_tv", "id", context.getPackageName()));

        mVolumeBrightnessLayout = (RelativeLayout) v.findViewById(R.id.operation_volume_brightness);
        mOperationBg = (ImageView) v.findViewById(R.id.operation_bg);
        mOperationTv = (TextView) v.findViewById(R.id.operation_tv);
        mOperationTv.setVisibility(View.GONE);
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        if (resourceSize == 1) {
            bannerNextLayout.setVisibility(View.GONE);
        } else {
            rePlayLayout.setVisibility(View.GONE);
        }
        if (videoPos == resourceSize - 1) {
            nextTv.setText(context.getString(R.string.video_player_first_msg));
        }
        img_back.setOnClickListener(this);
        pauseIv.setOnClickListener(this);
        mIvScale.setOnClickListener(this);
        backWindIv.setOnClickListener(this);
        fastForwardIv.setOnClickListener(this);
        advertisIv.setOnClickListener(this);
        advertisCloseIv.setOnClickListener(this);
        bannerNextLayout.setOnClickListener(this);
        rePlayLayout.setOnClickListener(this);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                bannerLayout.setVisibility(VISIBLE);
                isEnd = true;
//                seekBarProgress.setEnabled(false);
//                backWindIv.setEnabled(false);
//                fastForwardIv.setEnabled(false);
                pauseIv.setImageResource(R.mipmap.video_suspend);
            }
        });
        return v;
    }

    public boolean isEnd = false;

    public void setNextVisibility() {
        bannerNextLayout.setVisibility(GONE);
    }

    public void setRePlayVisibility() {
        rePlayLayout.setVisibility(GONE);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        System.out.println("MYApp-MyMediaController-dispatchKeyEvent");
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) return true;
        // 处理手势结束
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                endGesture();
                if (progress_turn) {
                    onFinishSeekBar();
                    progress_turn = false;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 手势结束
     */
    private void endGesture() {
        mVolume = -1;
        mBrightness = -1f;
        // 隐藏
        myHandler.removeMessages(HIDEFRAM);
        myHandler.sendEmptyMessageDelayed(HIDEFRAM, 1);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            //当收拾结束，并且是单击结束时，控制器隐藏/显示
            toggleMediaControlsVisiblity();
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            progress = getProgress();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float beginX = e1.getX();
            float endX = e2.getX();
            float beginY = e1.getY();
            float endY = e2.getY();

            Display disp = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            disp.getSize(size);
            int windowWidth = size.x;
            int windowHeight = size.y;
            if (Math.abs(endX - beginX) < Math.abs(beginY - endY)) {//上下滑动
                if (beginX > windowWidth * 3.0 / 4.0) {// 右边滑动 屏幕3/5
                    onVolumeSlide((beginY - endY) / windowHeight);
                } else if (beginX < windowWidth * 1.0 / 4.0) {// 左边滑动 屏幕2/5
                    onBrightnessSlide((beginY - endY) / windowHeight);
                }
            } else {
                onSeekTo((endX - beginX) / 2);
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        //双击暂停或开始
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            playOrPause();
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    /**
     * 滑动改变播放进度
     *
     * @param percent
     */
    private void onSeekTo(float percent) {
        //计算并显示 前进后退
        if (!progress_turn) {
            onStartSeekBar();
            progress_turn = true;
        }
        int change = (int) (percent);
        if (change > 0) {
//            mOperationBg.setImageResource(R.drawable.right);
        } else {
//            mOperationBg.setImageResource(R.drawable.left);
        }
        mOperationTv.setVisibility(View.VISIBLE);

        mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        if (progress + change > 0) {
            if ((progress + change < 1000))
                mOperationTv.setText(setSeekBarChange(progress + change) + "/" + StringUtils.generateTime(videoView.getDuration()));
            else
                mOperationTv.setText(setSeekBarChange(1000) + "/" + StringUtils.generateTime(videoView.getDuration()));
        } else {
            mOperationTv.setText(setSeekBarChange(0) + "/" + StringUtils.generateTime(videoView.getDuration()));

        }
        mPlayer.start();
        bannerLayout.setVisibility(GONE);
        pauseIv.setImageResource(R.mipmap.video_pause);
    }

    /**
     * 滑动改变声音大小
     *
     * @param percent
     */
    private void onVolumeSlide(float percent) {
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0)
                mVolume = 0;

            // 显示
//            mOperationBg.setImageResource(R.drawable.video_volumn_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
            mOperationTv.setVisibility(VISIBLE);
        }

        int index = (int) (percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;
        if (index >= 10) {
//            mOperationBg.setImageResource(R.drawable.volmn_100);
        } else if (index >= 5 && index < 10) {
//            mOperationBg.setImageResource(R.drawable.volmn_60);
        } else if (index > 0 && index < 5) {
//            mOperationBg.setImageResource(R.drawable.volmn_30);
        } else {
//            mOperationBg.setImageResource(R.drawable.volmn_no);
        }
        //DecimalFormat    df   = new DecimalFormat("######0.00");
        mOperationTv.setText((int) (((double) index / mMaxVolume) * 100) + "%");
        // 变更声音
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
    }

    /**
     * 滑动改变亮度
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        if (mBrightness < 0) {
            mBrightness = activity.getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;

            // 显示
            //mOperationBg.setImageResource(R.drawable.video_brightness_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
            mOperationTv.setVisibility(VISIBLE);

        }
        WindowManager.LayoutParams lpa = activity.getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        activity.getWindow().setAttributes(lpa);

        mOperationTv.setText((int) (lpa.screenBrightness * 100) + "%");
        if (lpa.screenBrightness * 100 >= 90) {
//            mOperationBg.setImageResource(R.drawable.light_100);
        } else if (lpa.screenBrightness * 100 >= 80 && lpa.screenBrightness * 100 < 90) {
//            mOperationBg.setImageResource(R.drawable.light_90);
        } else if (lpa.screenBrightness * 100 >= 70 && lpa.screenBrightness * 100 < 80) {
//            mOperationBg.setImageResource(R.drawable.light_80);
        } else if (lpa.screenBrightness * 100 >= 60 && lpa.screenBrightness * 100 < 70) {
//            mOperationBg.setImageResource(R.drawable.light_70);
        } else if (lpa.screenBrightness * 100 >= 50 && lpa.screenBrightness * 100 < 60) {
//            mOperationBg.setImageResource(R.drawable.light_60);
        } else if (lpa.screenBrightness * 100 >= 40 && lpa.screenBrightness * 100 < 50) {
//            mOperationBg.setImageResource(R.drawable.light_50);
        } else if (lpa.screenBrightness * 100 >= 30 && lpa.screenBrightness * 100 < 40) {
//            mOperationBg.setImageResource(R.drawable.light_40);
        } else if (lpa.screenBrightness * 100 >= 20 && lpa.screenBrightness * 100 < 20) {
//            mOperationBg.setImageResource(R.drawable.light_30);
        } else if (lpa.screenBrightness * 100 >= 10 && lpa.screenBrightness * 100 < 20) {
//            mOperationBg.setImageResource(R.drawable.light_20);
        }
    }


    public void setTime(String time) {
        if (textViewTime != null)
            textViewTime.setText(time);
    }

    //显示电量，
    public void setBattery(String stringBattery) {
        if (textViewTime != null && img_Battery != null) {
            textViewBattery.setText(stringBattery + "%");
            int battery = Integer.valueOf(stringBattery);
//            if(battery < 15)img_Battery.setImageDrawable(getResources().getDrawable(R.drawable.battery_15));
//            if(battery < 30 && battery >= 15)img_Battery.setImageDrawable(getResources().getDrawable(R.drawable.battery_15));
//            if(battery < 45 && battery >= 30)img_Battery.setImageDrawable(getResources().getDrawable(R.drawable.battery_30));
//            if(battery < 60 && battery >= 45)img_Battery.setImageDrawable(getResources().getDrawable(R.drawable.battery_45));
//            if(battery < 75 && battery >= 60)img_Battery.setImageDrawable(getResources().getDrawable(R.drawable.battery_60));
//            if(battery < 90 && battery >= 75)img_Battery.setImageDrawable(getResources().getDrawable(R.drawable.battery_75));
//            if(battery > 90 )img_Battery.setImageDrawable(getResources().getDrawable(R.drawable.battery_90));
        }
    }

    //隐藏/显示
    private void toggleMediaControlsVisiblity() {
        if (isShowing()) {
            hide();
        } else {
            show();
        }
    }

    //播放与暂停
    private void playOrPause() {
        bannerLayout.setVisibility(GONE);
        if (videoView != null) {
            if (videoView.isPlaying()) {
                videoView.pause();
                pauseIv.setImageResource(R.mipmap.icon_suspend);
            } else {
                videoView.start();
                pauseIv.setImageResource(R.mipmap.video_pause);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_player_fast_forward_iv:
                videoView.seekTo((videoView.getCurrentPosition() + 10000));
                mPlayer.start();
                bannerLayout.setVisibility(GONE);
                pauseIv.setImageResource(R.mipmap.video_pause);
                break;
            case R.id.video_player_back_wind_iv:
                videoView.seekTo((videoView.getCurrentPosition() - 5000));
                break;
            case R.id.video_player_pause_iv:
                if (isEnd) {
                    bannerLayout.setVisibility(View.GONE);
                    onReLoadListener.onReload();
                } else {
                    playOrPause();
                }
                break;
            case R.id.video_player_switch_iv:
                if (activity != null) {
                    switch (activity.getResources().getConfiguration().orientation) {
                        case Configuration.ORIENTATION_LANDSCAPE://横屏
                            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            break;
                        case Configuration.ORIENTATION_PORTRAIT://竖屏
                            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            break;
                    }
                }
                break;
            case R.id.mediacontroller_top_back:
                if (activity != null) {
                    activity.finish();
                }
                break;
            case R.id.video_player_advertis_close_iv:
                bannerLayout.setVisibility(View.GONE);
                return;
            case R.id.video_player_re_play_layout:
                bannerLayout.setVisibility(View.GONE);
                onReLoadListener.onReload();
                break;
            case R.id.video_player_advertis_iv:
                Intent intent = new Intent(context, AdvertisActivity.class);
                intent.putExtra(AdvertisActivity.KEY_ADVERTIS_URL, advertisUrl);
                context.startActivity(intent);
                return;
            case R.id.video_player_next_layout:
                bannerLayout.setVisibility(GONE);
                onReLoadListener.onNext();
                break;
        }
    }

    public void setOnReLoadListener(OnReLoadListener onReLoadListener) {
        this.onReLoadListener = onReLoadListener;
    }

    private OnReLoadListener onReLoadListener;

    public interface OnReLoadListener {
        void onReload();

        void onNext();
    }

    private String advertisUrl;

    public void getAdvertisementInfo(int locationID) {
        GetAdvertisementInfoApi getAdvertisementInfoApi = new GetAdvertisementInfoApi((RxAppCompatActivity) context);
        getAdvertisementInfoApi.setLocationID(locationID);
        getAdvertisementInfoApi.setListener(new HttpOnNextListener<GetAdvertisementInfoBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(GetAdvertisementInfoBean getAdvertisementInfoBean) {
                if (getAdvertisementInfoBean.getReturnData() != null)
                    if (getAdvertisementInfoBean.getErrorCode() == 200) {
                        advertisUrl = getAdvertisementInfoBean.getReturnData().getTargetUrl();
                        Glide.with((RxAppCompatActivity) context).load(getAdvertisementInfoBean.getReturnData().getImageUrl()).into(advertisIv);
                    }
                return null;
            }

        });

        HttpManager.getInstance().doHttpDeal(context, getAdvertisementInfoApi);
    }


}
