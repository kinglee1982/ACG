package com.kcx.acg.views.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.kcx.acg.R;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.utils.WebViewUtils;

import me.jessyan.autosize.internal.CancelAdapt;


public class H5Activity extends BaseActivity implements CancelAdapt {
    private LinearLayout ll_back;
    private WebView mWebview;
    private WebViewUtils webViewUtils;
    private String url;
    private String title;
    private H5Activity.OnH5ResultListener onH5ResultListener;
    private int position;

    public interface OnH5ResultListener {
        void onResult(String phone);
    }

    //设置回调监听
    public void setOnH5ResultListener(OnH5ResultListener onH5ResultListener) {
        this.onH5ResultListener = onH5ResultListener;
    }

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_h5, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void initView() {
        super.initView();

        mWebview = findViewById(R.id.webView);
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        ll_back = findViewById(R.id.ll_back);

        webViewUtils = new WebViewUtils();
        webViewUtils.initWeb(H5Activity.this, mWebview);
        //设置本地调用对象及其接口
        mWebview.loadUrl(url);

        mWebview.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                LogUtil.e("onReceivedError", "h5加载失败");
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            }

            //设置结束加载函数
            @Override
            public void onPageFinished(WebView view, String url) {
            }
        });


        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    //点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebview.canGoBack()) {
            mWebview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mWebview != null) {
            mWebview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebview.clearHistory();

            ((ViewGroup) mWebview.getParent()).removeView(mWebview);
            mWebview.destroy();
            mWebview = null;
        }
        super.onDestroy();
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                break;
        }
    }

}
