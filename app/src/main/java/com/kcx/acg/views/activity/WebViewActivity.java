package com.kcx.acg.views.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.kcx.acg.R;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.bean.UserInfoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.impl.HttpDownOnNextListener;
import com.kcx.acg.manager.ShareManager;
import com.kcx.acg.permission.PermissionListener;
import com.kcx.acg.permission.PermissionUtil;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.DialogMaker;
import com.kcx.acg.utils.ImageUtil;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.utils.QRCodeService;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.LoadingErrorView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import download.DownInfo;
import download.HttpDownManager;
import me.jessyan.autosize.internal.CancelAdapt;

/**
 * Created by jb on 2018/9/28.
 */
public class WebViewActivity extends BaseActivity implements CancelAdapt {
    public static String URL = "url";
    private Dialog dialog = null;
    private QRCodeService qrCodeService;
    private String url;
    private int classType;
    private BridgeWebView mBridgeWebView;
    private LinearLayout ll_back;
    private TextView tv_title;
    private RelativeLayout rl_head;
    private LoadingErrorView mLoadingErrorView;
    private String mSaveMessage;
    private Bitmap bitmapQR;
    private UserInfoBean userInfoBean;
    private boolean isError = false;
    private File outputFile;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 0) {
                Bundle bundle = msg.getData();
                String s = bundle.getString("saveMessage");
                CustomToast.showToast(s);
            }
        }
    };

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_webview, null);
    }

    @Override
    public void initView() {
        super.initView();
        userInfoBean = AppUtil.getGson().fromJson(SPUtil.getString(WebViewActivity.this, Constants.USER_INFO, ""), UserInfoBean.class);
        url = getIntent().getStringExtra("url");
        classType = getIntent().getIntExtra("classType", 0);
        rl_head = findViewById(R.id.rl_head);
        mBridgeWebView = findViewById(R.id.mWebView);
        ll_back = findViewById(R.id.ll_back);
        tv_title = findViewById(R.id.tv_title);
        mLoadingErrorView = findViewById(R.id.loadingErrorView);

        configWebView();
        mBridgeWebView.loadUrl(this.url);
//        mBridgeWebView.loadUrl("http://192.168.1.123/ACG-H5/zhuanxianjin/index.html?memberid=2178&type=android");
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
        //去提现
        mBridgeWebView.registerHandler(Constants.Hybrid.GO_WITHDRAW, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                LogUtil.e("goWithdraw", data);
                startDDMActivity(IncomeActivity.class, true);
            }

        });

        //返回
        mBridgeWebView.registerHandler(Constants.Hybrid.BACK_VC, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                LogUtil.e("backVC", data);
                finish();
            }
        });
        //保存图片
        mBridgeWebView.registerHandler(Constants.Hybrid.SAVE_QCODE_IMG, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                LogUtil.e("保存图片", data);
                saveImage(data);
            }
        });


        mBridgeWebView.registerHandler(Constants.Hybrid.SAVE_QCODE, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                LogUtil.e("保存二维码", data);
                                saveQCodeImg(data);
            }
        });

        //分享
        mBridgeWebView.registerHandler(Constants.Hybrid.SHARE, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                LogUtil.e("SHARE", data);
                ShareManager.getInstances().share(4, userInfoBean.getReturnData().getMemberID(), WebViewActivity.this);
            }
        });

        //幸运抽奖
        mBridgeWebView.registerHandler(Constants.Hybrid.IS_LOTTERY, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                LogUtil.e("IS_LOTTERY", data);
            }
        });
    }

    /**
     * 保存推广图片
     *
     * @param data
     */
    private void saveImage(final String data) {
        new PermissionUtil(WebViewActivity.this).requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
            @Override
            public void onGranted() {
                outputFile = new File(Environment.getExternalStorageDirectory(), "acg_tuiguang");
                DownInfo apkApi = new DownInfo();
                apkApi.setUrl(data);
                apkApi.setSavePath(outputFile.getAbsolutePath());
                apkApi.setUpdateProgress(true);
                apkApi.setListener(httpProgressOnNextListener);
                HttpDownManager httpDownManager = HttpDownManager.getInstance();
                httpDownManager.startDown(apkApi);
            }

            @Override
            public void onDenied(List<String> deniedPermission) {

            }

            @Override
            public void onShouldShowRationale(List<String> deniedPermission) {

            }
        });
    }

    /**
     * 保存二维码到本地相册
     *
     * @param imgPath
     */
    private void saveQCodeImg(final String imgPath) {
        new PermissionUtil(WebViewActivity.this).requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
            @Override
            public void onGranted() {
                try {
                    qrCodeService = new QRCodeService();
                    bitmapQR = qrCodeService.cretaeBitmap(imgPath, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new Thread(saveFileRunnable).start();
            }

            @Override
            public void onDenied(List<String> deniedPermission) {

            }

            @Override
            public void onShouldShowRationale(List<String> deniedPermission) {

            }
        });
    }


    @Override
    public void onClick(View v) {

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
            settings.setBlockNetworkImage(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //允许混合（http，https）
                settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//                settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
            }
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
            LogUtil.e("onReceivedError","////");
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
//                        if (dialog == null) {dialog = DialogMaker.showCommenWaitDialog(WebViewActivity.this, "", false);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            LogUtil.e("onPageFinished", url);
            String title = view.getTitle();
            if (isError) {
                mLoadingErrorView.setVisibility(View.VISIBLE);
            } else {
                mLoadingErrorView.setVisibility(View.GONE);
                tv_title.setText(title);
            }
            isError = false;
        }
    }

    //    public boolean parseScheme(String url) {
    //
    //        if (url.contains("platformapi/startapp")){
    //            myHandler.removeCallbacks(runable);
    //            return true;
    //        } else if(url.contains("web-other")){
    //            myHandler.postDelayed(runable, 10000);
    //            return false;
    //        }else {
    //            return false;
    //        }
    //    }


    private Runnable saveFileRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                ImageUtil.saveBitmap2SysPhoto(WebViewActivity.this, bitmapQR, "acg_QR.jpg");
                mSaveMessage = "二维码已储存至相册";
            } catch (IOException e) {
                mSaveMessage = "二维码储存至相册失败";
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //handler 发送数据
            Message message = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("saveMessage", mSaveMessage);
            message.setData(bundle);
            message.arg1 = 0;
            handler.sendMessage(message);
        }
    };


    //点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mBridgeWebView.canGoBack()) {
            mBridgeWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /*下载回调*/
    HttpDownOnNextListener<DownInfo> httpProgressOnNextListener = new HttpDownOnNextListener<DownInfo>() {
        @Override
        public void onNext(DownInfo baseDownEntity) {
            mBridgeWebView.callHandler(Constants.Hybrid.SAVE_QCODE_IMG, "0", new CallBackFunction() {
                @Override
                public void onCallBack(String data) {
                    LogUtil.e("保存成功回传返回",data+"///");
                }
            });

            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            LogUtil.e("提示：下载完成/文件地址->" + baseDownEntity.getSavePath());
            CustomToast.showToast("保存成功");
            // 其次把文件插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(getContentResolver(), outputFile.getAbsolutePath(), "acg_tuiguang", null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(outputFile.getAbsolutePath())));


        }

        @Override
        public void onStart() {
            LogUtil.e("开始下载->");
            if (dialog == null) {
                dialog = DialogMaker.showCommenWaitDialog(WebViewActivity.this, "", false);
            }
        }

        @Override
        public void onComplete() {
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            LogUtil.e("下载异常", e.getMessage());

        }

        @Override
        public void onPuase() {
            super.onPuase();

        }

        @Override
        public void onStop() {
            super.onStop();
        }

        @Override
        public void updateProgress(long readLength, long countLength) {
            int progress = (int) ((double) readLength * 100 / countLength);
            LogUtil.e("下载进度", progress + "%");

        }
    };

}
