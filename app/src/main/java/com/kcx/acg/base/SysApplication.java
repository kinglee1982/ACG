package com.kcx.acg.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.widget.ImageView;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.views.view.MyClassicsFooter;
import com.kcx.acg.views.view.MyHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tmall.wireless.tangram.TangramBuilder;
import com.tmall.wireless.tangram.util.IInnerImageSetter;
import com.zxy.tiny.Tiny;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import okhttp3.OkHttpClient;


/**
 * Created by Longer on 2016/10/26.
 */
public class SysApplication extends MultiDexApplication {
    public static String DEFAULT_LANGUAGE = "zh_CN";
    public static Context mContext;
    public static Handler mHandler;
    public static long mMainThreadId;
    public static SysApplication instance;
    public static String machineCode;
    public static int mWidthPixels;
    public static int mHeightPixels;
    public static int uIn;
    private volatile static SysApplication INSTANCE;
    public static boolean isLocked = false;
    private boolean havePrivacyPw;
    private String accessToken;
    private int mFinalCount;
    private Activity app_activity = null;
    //device_token
    public static String mDeviceToken = "";

    public static Context getContext() {
        return mContext;
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public static long getMainThreadId() {
        return mMainThreadId;
    }

    public static String uuid = "1234567890";

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setHeaderTriggerRate(1);
                return new MyHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new MyClassicsFooter(context);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCreate() {
        instance = this;

        // 1.上下文
        mContext = getApplicationContext();

        // 2.主线程的Handler
        mHandler = new Handler();

        // 3.得到主线程的id
        mMainThreadId = android.os.Process.myTid();

        //Fresco初始化
        //        Fresco.initialize(this);
        //AndroidUtilCode初始化
        Utils.init(this);
        //Tangram初始化
        TangramBuilder.init(this, new IInnerImageSetter() {
            @Override
            public <IMAGE extends ImageView> void doLoadImageUrl(@NonNull IMAGE view,
                                                                 @Nullable String url) {
                //假设你使用 Picasso 加载图片                		Picasso.with(context).load(url).into(view);
                Glide.with(mContext).load(url).into(view);
            }
        }, ImageView.class);
        Tiny.getInstance().init(this);
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                ex.printStackTrace();
            }
        });
        super.onCreate();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        initGlobeActivity();

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                //是否开启隐私密码
                havePrivacyPw = (boolean) SPUtil.get(activity, Constants.HAVE_PRIVACY_PW, false);
                accessToken = SPUtil.getString(activity, Constants.ACCESS_TOKEN, "");
                mFinalCount++;
                //如果mFinalCount ==1，说明是从后台到前台
                if (mFinalCount == 1){
                    //说明从后台回到了前台
                    if (havePrivacyPw && !TextUtils.isEmpty(accessToken)) {
                        isLocked = true;//是否开启隐私密码。是
                    } else {
                        isLocked = false;
                    }
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                mFinalCount--;
                //如果mFinalCount ==0，说明是前台到后台
                if (mFinalCount == 0){
                    //说明从前台回到了后台
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }
    private void initGlobeActivity() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }

            /** Unused implementation **/
            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                app_activity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }
        });
    }

    public Activity getCurrentActivity() {
        return app_activity;
    }

    //获取单例
    public static SysApplication getInstance() {
        return instance;
    }


    public void removeActivity(Activity activity) {

        for (Activity ac : mList) {
            if (ac.equals(activity)) {
                mList.remove(ac);
                break;
            }
        }
    }

    private List<Activity> mList = new LinkedList<Activity>();


    private String getChanl() {
        String channel = "";
        try {
            channel = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData.getString("UMENG_CHANNEL");
            //LogUtils.e("这是渠道号:" + channel);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channel;
    }

    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.connectTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
