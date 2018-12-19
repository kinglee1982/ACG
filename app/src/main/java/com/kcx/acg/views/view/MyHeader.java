package com.kcx.acg.views.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.apkfuns.logutils.LogUtils;
import com.kcx.acg.R;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

/**
 * Created by  o on 2018/10/16.
 */

public class MyHeader extends LinearLayout implements RefreshHeader {

    private View view;
    private Context context;
    private ImageView refreshIv;
    private int duration = 700;

    public MyHeader(Context context) {
        this(context, null);
    }

    public MyHeader(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyHeader, defStyleAttr, 0);
        duration = a.getInteger(R.styleable.MyHeader_duration, 700);
        a.recycle();
        init();
    }

    public void init() {
        view = LayoutInflater.from(context).inflate(R.layout.my_header_layout, null);
        refreshIv = view.findViewById(R.id.my_head_refresh_iv);

        refreshIv.setImageResource(R.mipmap.refresh7);
        view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        addView(view);
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        refreshIv.setImageResource(R.drawable.refreshing);
        AnimationDrawable animationDrawable = (AnimationDrawable) refreshIv.getDrawable();
        animationDrawable.start();
    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        refreshIv.setImageResource(R.drawable.refresh_finish);
        AnimationDrawable animationDrawable = (AnimationDrawable) refreshIv.getDrawable();
        animationDrawable.start();
        return duration;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        AnimationDrawable animationDrawable = null;
        switch (newState) {
            case None:
            case PullDownToRefresh:
                refreshIv.setImageResource(R.mipmap.refresh7);
                if(animationDrawable != null)
                animationDrawable.stop();
                break;
            case Refreshing:
                refreshIv.setImageResource(R.drawable.refreshing);
                animationDrawable = (AnimationDrawable) refreshIv.getDrawable();
                animationDrawable.start();
                break;
            case ReleaseToRefresh:
//                refreshIv.setImageResource(R.drawable.refresh_finish);
//                animationDrawable = (AnimationDrawable) refreshIv.getDrawable();
//                animationDrawable.start();
                break;
        }
    }
}
