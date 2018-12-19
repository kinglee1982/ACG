package com.kcx.acg.views.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.kcx.acg.R;
import com.kcx.acg.api.GetUserInfoApi;
import com.kcx.acg.api.SetPayFailureReasonApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.bean.SetPayFailureReasonBean;
import com.kcx.acg.bean.UserInfoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.views.view.LoadingErrorView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * Created by jb on 2018/9/28.
 */
public class PayActivity extends BaseActivity implements CancelAdapt {
    private String url;
    private BridgeWebView mBridgeWebView;
    private LinearLayout ll_back;
    private TextView tv_title;
    private RelativeLayout rl_head;
    private LoadingErrorView mLoadingErrorView;
    private boolean isError = false;
    private String orderID;

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_pay, null);
    }

    @Override
    public void initView() {
        super.initView();
        url = getIntent().getStringExtra("url");
        String[] strings = url.split("\\?");
        String[] strings1 = strings[1].split("&");
        String[] strings2 = strings1[0].split("=");
        orderID = strings2[1];


        rl_head = findViewById(R.id.rl_head);
        mBridgeWebView = findViewById(R.id.mWebView);
        ll_back = findViewById(R.id.ll_back);
        tv_title = findViewById(R.id.tv_title);
        mLoadingErrorView = findViewById(R.id.loadingErrorView);

        configWebView();
        mBridgeWebView.loadUrl(this.url);
        mBridgeWebView.setWebViewClient(new MyWebViewClient(mBridgeWebView));
    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBridgeWebView.canGoBack()) {
                    mBridgeWebView.goBack();
                } else {
                    finish();
                }
            }
        });

        mLoadingErrorView.setOnReloadListener(new LoadingErrorView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                mBridgeWebView.reload();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();


        //返回
        mBridgeWebView.registerHandler(Constants.Hybrid.BACK_VC, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                LogUtil.e("backVC", data);
                finish();
            }
        });

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LogUtils.d("onBackPressed");
    }


    public void configWebView() {
        try {
            WebSettings settings = mBridgeWebView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setJavaScriptCanOpenWindowsAutomatically(true);
            settings.setDatabaseEnabled(true);
            settings.setBuiltInZoomControls(false);
            settings.setDomStorageEnabled(true);
            settings.setAppCacheEnabled(true);
            settings.setAllowFileAccess(true);
            //设置localStorage存储路径
            String localStorageDBPath = mBridgeWebView.getContext().getFilesDir().getAbsolutePath();
            settings.setDatabasePath(localStorageDBPath);
        } catch (Exception e) {
            LogUtil.e("configWebView error.", e.getMessage());
        }
    }

    /**
     * 自定义的WebViewClient
     */
    class MyWebViewClient extends BridgeWebViewClient {

        public MyWebViewClient(BridgeWebView webView) {
            super(webView);
        }


        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            isError = true;

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            LogUtil.e("onPageFinished", url);
            String title = view.getTitle();
            if (!TextUtils.isEmpty(title)) {
                tv_title.setText(title);
            }

            if (isError) {
                mLoadingErrorView.setVisibility(View.VISIBLE);
            } else {
                mLoadingErrorView.setVisibility(View.GONE);
            }
            isError = false;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtil.e("shouldOverrideUrlLoading-url", url);
            if (url.contains("alipays://platformapi")) {
                try {
                    Uri uri = Uri.parse(url);
                    Intent intent;
                    intent = Intent.parseUri(url,
                            Intent.URI_INTENT_SCHEME);
                    intent.addCategory("android.intent.category.BROWSABLE");
                    intent.setComponent(null);
                    startActivity(intent);

                } catch (Exception e) {
                }
            } else if (url.contains("QUEUE_MESSAGE")) {
                refreshUserInfo();
            } else {
                view.loadUrl(url);

            }
            return true;
        }
    }


    //点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mBridgeWebView.canGoBack()) {
            mBridgeWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        setPayFailureReason(orderID, getString(R.string.cancel_pay_msg));
        super.onDestroy();


    }

    public void setPayFailureReason(String orderID, String failureReason) {
        SetPayFailureReasonApi setPayFailureReasonApi = new SetPayFailureReasonApi(this);
        setPayFailureReasonApi.setOrderID(orderID);
        setPayFailureReasonApi.setFailureReason(failureReason);
        setPayFailureReasonApi.setListener(new HttpOnNextListener<SetPayFailureReasonBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(SetPayFailureReasonBean setPayFailureReasonBean) {
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(this, setPayFailureReasonApi);
    }

    public void refreshUserInfo() {
        GetUserInfoApi getUserInfoApi = new GetUserInfoApi(new HttpOnNextListener<UserInfoBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(UserInfoBean userInfoBean) {
                if (userInfoBean.getErrorCode() == 200) {
                    SPUtil.putString(PayActivity.this, Constants.USER_INFO, AppUtil.getGson().toJson(userInfoBean));
                    finish();
                }
                return null;
            }

            @Override
            public RetryWhenNetworkException.Wrapper onError(Throwable e) {
                super.onError(e);
                finish();
                return null;
            }
        }, (RxAppCompatActivity) this);
        HttpManager httpManager = HttpManager.getInstance();
        httpManager.doHttpDeal(PayActivity.this, getUserInfoApi);
    }


}
