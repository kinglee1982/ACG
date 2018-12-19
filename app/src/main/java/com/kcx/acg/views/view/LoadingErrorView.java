package com.kcx.acg.views.view;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kcx.acg.R;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

/**
 */

public class LoadingErrorView extends LinearLayout implements View.OnClickListener {

    private View view;
    private Context context;
    private TextView reloadTv;
    private ImageView errorIv;

    public LoadingErrorView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public LoadingErrorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public LoadingErrorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void init() {
        view = LayoutInflater.from(context).inflate(R.layout.loading_error_layout, null);
        reloadTv = view.findViewById(R.id.loading_error_reload_tv);
        errorIv = view.findViewById(R.id.loading_error_iv);
        reloadTv.setOnClickListener(this);
        view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(view);
    }

    public void onError(Throwable e){
        setVisibility(VISIBLE);
        if(e instanceof TimeoutException || e instanceof SocketTimeoutException){
            errorIv.setImageResource(R.mipmap.image_failed);
        }else if(e instanceof ConnectException || e instanceof NetworkErrorException){
            errorIv.setImageResource(R.mipmap.image_networkout);
        }else {
            errorIv.setImageResource(R.mipmap.image_failed);
        }
    }

    public void setTimeOut() {
        setVisibility(VISIBLE);
        errorIv.setImageResource(R.mipmap.image_failed);
    }

    public void setNetworkError() {
        setVisibility(VISIBLE);
        errorIv.setImageResource(R.mipmap.image_networkout);
    }

    public void setServerError() {
        setVisibility(VISIBLE);
        errorIv.setImageResource(R.mipmap.image_failed);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loading_error_reload_tv:
                if (onReloadListener != null)
                    onReloadListener.onReload(this);
                setVisibility(GONE);
                break;
        }
    }

    private OnReloadListener onReloadListener;

    public void setOnReloadListener(OnReloadListener onReloadListener) {
        this.onReloadListener = onReloadListener;
    }

    public interface OnReloadListener {
        void onReload(View view);
    }
}
