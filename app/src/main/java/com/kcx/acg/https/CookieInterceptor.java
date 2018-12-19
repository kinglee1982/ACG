package com.kcx.acg.https;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.amazonaws.util.json.JsonUtils;
import com.apkfuns.logutils.LogUtils;
import com.kcx.acg.base.BusEvent;
import com.kcx.acg.base.SysApplication;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.CommonUtils;
import com.kcx.acg.utils.CookieDbUtil;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.utils.SystemUtil;
import com.kcx.acg.views.activity.LoginActivity;
import com.kcx.acg.views.activity.RegisterSetActivity;
import com.kcx.acg.views.activity.SettingActivity;
import com.meituan.android.walle.WalleChannelReader;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

import static com.amazonaws.util.StringUtils.UTF8;

/**
 * gson持久化截取保存数据
 * Created by WZG on 2016/10/20.
 */
public class CookieInterceptor implements Interceptor {
    private final Context context;
    private CookieDbUtil dbUtil;
    /*是否缓存标识*/
    private boolean cache;
    /*url*/
    private String url;
    private String mModel;
    private String mSystemVersion;
    private int mAppVersion;
    private String mAppVersionName;
    private String mChannelName;


    public CookieInterceptor(Context context, boolean cache, String url) {
        dbUtil = CookieDbUtil.getInstance();
        this.context = context;
        this.url = url;
        this.cache = cache;
        setUaCode();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        //配置request
        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();

        //acg/Android/设备型号/系统版本/软件版本号/渠道号
        String channel = WalleChannelReader.getChannel(context);
//        LogUtils.e("ACG Channel: " + (TextUtils.isEmpty(channel) ? "default" : channel));
//        String uaCode = "acg/Android/" + mModel + "/" + mSystemVersion + "/" + mAppVersionName + "/" + (TextUtils.isEmpty(channel) ? "default" : channel);
        LogUtils.e("ACG Channel: " + (TextUtils.isEmpty(channel) ? "android" : channel));
        String uaCode = "acg/Android/" + mModel + "/" + mSystemVersion + "/" + mAppVersionName + "/" + (TextUtils.isEmpty(channel) ? "android" : channel);
        requestBuilder.addHeader("UaCode", uaCode);
        String ipAddress = SystemUtil.getIPAddress(context);
        requestBuilder.addHeader("IPAddress", ipAddress);
        String deviceID = AppUtil.getAndroidID(context);
        requestBuilder.addHeader("DeviceID", deviceID);
        requestBuilder.addHeader("AccessToken", SPUtil.getString(context, Constants.ACCESS_TOKEN, ""));

        String requestUrl = request.url().toString();
        String baseUrl = SPUtil.getString(context, Constants.DYNAMIC_HOST, Constants.RELEASE_URL);
        if (!url.equals(baseUrl)) {
            requestUrl = requestUrl.replace(url, baseUrl);
        }

//        Uri parse = Uri.parse(url);
//        String host = parse.getHost();
//        LogUtil.e("host",host);
//        HttpUrl httpUrl = request.url().newBuilder().host(host).build();

        requestBuilder.url(requestUrl);
        Response.Builder responseBuilder = chain.proceed(requestBuilder.build()).newBuilder();
        Response response = responseBuilder.build();

        if (cache) {
            ResponseBody body = response.body();
            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();
            Charset charset = Charset.defaultCharset();
            MediaType contentType = body.contentType();

            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            String bodyString = buffer.clone().readString(charset);
            CookieResulte resulte = dbUtil.queryCookieBy(url);
            long time = System.currentTimeMillis();
            /*保存和更新本地数据*/
            if (resulte == null) {
                resulte = new CookieResulte(url, bodyString, time);
                dbUtil.saveCookie(resulte);
            } else {
                resulte.setResulte(bodyString);
                resulte.setTime(time);
                dbUtil.updateCookie(resulte);
            }
        }
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }

        String bodyString = buffer.clone().readString(charset);
        Map<String,String> map = JsonUtils.jsonToMap(bodyString);
        int errorCode = Integer.parseInt(map.get("errorCode"));
        if(errorCode == 0b110010010){
            SPUtil.putString(context, Constants.ACCESS_TOKEN, "");
            SPUtil.putString(context, Constants.USER_INFO, "");
            EventBus.getDefault().post(new BusEvent(BusEvent.LOGIN_OUT, true));
            context.startActivity(new Intent(context, LoginActivity.class));
        }

        return response;
    }

    public void setUaCode() {
        mModel = SystemUtil.getDeviceBrand() + " " + SystemUtil.getSystemModel();
        mSystemVersion = SystemUtil.getSystemVersion();
        mAppVersionName = CommonUtils.getVersionName(SysApplication.getContext());
    }
}
