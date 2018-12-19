package com.kcx.acg.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.kcx.acg.BuildConfig;


public class WebViewUtils {
    private Context context;
    public WebViewUtils() {
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void initWeb(Context context, WebView webview) {
        webview.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        WebSettings mWebSettings = webview.getSettings();
        // 支持缩放
        mWebSettings.setSupportZoom(true);

        // 缩放至屏幕大小
        mWebSettings.setLoadWithOverviewMode(true);
        // 将图片调整到适合webview大小
        mWebSettings.setUseWideViewPort(true);
        // 编码格式
        mWebSettings.setDefaultTextEncodingName("UTF-8");
        mWebSettings.setBuiltInZoomControls(true);

        // 支持自动加载图片，在页面加载完之前不加载图片
        if (Build.VERSION.SDK_INT >= 19) {
            // 对系统api在19以上的版本做了兼容，因为4.4以上系统在onpageFinished时再恢复图片加载时，如果存在多张图片
            // 引用的是相同的src时，会只有一个image标签得到加载。
            mWebSettings.setLoadsImagesAutomatically(true);
        } else {
            mWebSettings.setLoadsImagesAutomatically(false);
        }
        if (Build.VERSION.SDK_INT >= 11) {
            dealJavascriptLeak(webview);
        }
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setBlockNetworkImage(false);
        // 调用JS方法.安卓版本大于17,加上注解 @JavascriptInterface
        mWebSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 缓存数据
        saveData(context, mWebSettings);
        setHttpAndHttps(mWebSettings);
    }




    /**
     * HTML5数据存储
     */
    private void saveData(Context context, WebSettings mWebSettings) {
        // 有时候网页需要自己保存一些关键数据,Android WebView 需要自己设置
        // if (NetStatusUtil.isConnected(context)) {
        // mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//
        // 根据cache-control决定是否从网络上取数据。
        // } else {
        // mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//
        // 没网，则从本地获取，即离线加载
        // }

        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);// 没网，则从本地获取，即离线加载
        mWebSettings.setGeolocationEnabled(true);
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setDatabaseEnabled(true);
        mWebSettings.setAppCacheEnabled(true);
        mWebSettings.setGeolocationDatabasePath(context.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath());
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebSettings.setAppCachePath(context.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath());
        mWebSettings.setDatabasePath(context.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath());
    }

    // 5.0默认不支持http和https混合内容解决
    @SuppressLint("NewApi")
    private void setHttpAndHttps(WebSettings mWebSettings) {
        if (Build.VERSION.SDK_INT >= 21) {
            mWebSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    // 针对Android3.0以上系统版本移除问题接口
    private void dealJavascriptLeak(WebView webview) {
        webview.removeJavascriptInterface("searchBoxJavaBridge_");
        webview.removeJavascriptInterface("accessibility");
        webview.removeJavascriptInterface("accessibilityTraversal");
    }
}
