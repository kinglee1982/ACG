package com.kcx.acg.views.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.kcx.acg.R;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

/**
 */

public class MyClassicsFooter extends LinearLayout implements RefreshFooter {

    private Context context;
    private View view;
    private TextView textView;
    private LottieAnimationView loadingLAV;
    protected boolean mNoMoreData = false;
    private int bgColor;


    public MyClassicsFooter(Context context) {
        this(context, null);
    }

    public MyClassicsFooter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyClassicsFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyClassicsFooter, defStyleAttr, 0);
        bgColor = a.getColor(R.styleable.MyClassicsFooter_bg_color, ContextCompat.getColor(context, R.color.transparent));
        view = LayoutInflater.from(context).inflate(R.layout.load_more_layout, null);
        textView = view.findViewById(R.id.load_more_tv);
        loadingLAV = view.findViewById(R.id.load_more_lav);
        view.setBackgroundColor(bgColor);
        view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(view);
        a.recycle();
    }

    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        if (mNoMoreData != noMoreData) {
            mNoMoreData = noMoreData;
            if (noMoreData) {
                loadingLAV.setVisibility(GONE);
                textView.setText(context.getString(R.string.foot_no_more_msg));
            } else {
                textView.setText(context.getString(R.string.foot_refresh_pulling_msg));
            }
        }

        return true;
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
        view.setBackgroundColor(colors[0]);
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

    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        if (!mNoMoreData) {
            textView.setText(success ? context.getString(R.string.foot_refresh_finish_msg) : context.getString(R.string.foot_refresh_failed_msg));
        }
        return 500;
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
        if (!mNoMoreData) {
            switch (newState) {
                case None:
                case ReleaseToLoad:
                    textView.setText(context.getString(R.string.foot_refresh_release_msg));
                    break;
                case Loading:
                case LoadReleased:
                    textView.setText(context.getString(R.string.foot_refresh_loading_msg));
                    break;
                case PullUpToLoad:
                    textView.setText(context.getString(R.string.foot_refresh_pulling_msg));
                    break;
                case Refreshing:
                    textView.setText(context.getString(R.string.foot_refresh_refreshing_msg));
                    break;
            }
        }
    }
}
