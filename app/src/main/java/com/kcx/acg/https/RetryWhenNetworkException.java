package com.kcx.acg.https;

import android.content.Context;
import android.util.Log;

import com.kcx.acg.api.GetUserInfoApi;
import com.kcx.acg.api.ProductToMenberListApi;
import com.kcx.acg.base.SysApplication;
import com.kcx.acg.bean.DynamicHostBean;
import com.kcx.acg.bean.UserInfoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.utils.AESUtil;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.Base64;
import com.kcx.acg.utils.CommonUtils;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.utils.OkHttpManger;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.utils.SystemUtil;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

import static com.just.agentweb.AgentWebUIControllerImplBase.build;

/**
 * retry条件
 * Created by WZG on 2016/10/17.
 */
public class RetryWhenNetworkException implements Func1<Observable<? extends Throwable>, Observable<?>> {
    private Context context;
    //    retry次数
    private int count = 3;
    //    延迟
    private long delay = 3000;
    //    叠加延迟
    private long increaseDelay = 3000;

    public RetryWhenNetworkException() {

    }

    public RetryWhenNetworkException(int count, long delay) {
        this.count = count;
        this.delay = delay;
    }

    public RetryWhenNetworkException(Context context, int count, long delay, long increaseDelay) {
        this.context = context;
        this.count = count;
        this.delay = delay;
        this.increaseDelay = increaseDelay;
    }

    @Override
    public Observable<?> call(Observable<? extends Throwable> observable) {
        return observable
                .zipWith(Observable.range(1, count + 1), new Func2<Throwable, Integer, Wrapper>() {
                    @Override
                    public Wrapper call(final Throwable throwable, final Integer integer) {
                        if (AppUtil.isNetworkAvailable(context) && integer == count) {//检测网络
                            try {
                                long currentTimeMillis = System.currentTimeMillis();
                                String invalidApi = SPUtil.getString(context, Constants.DYNAMIC_HOST, Constants.RELEASE_URL);
                                invalidApi = AESUtil.Encrypt(invalidApi, Constants.AES_SECRET_KEY);
                                invalidApi = Base64.encode(invalidApi.getBytes("UTF-8"));
                                //AES加密（加密模式：ECB ，填充：pkcs7padding，数据块：128位，输出：base64 ,字符集：utf8）
                                String encryptString = 1 + SystemUtil.getSystemVersion() + currentTimeMillis;
                                String encryptData = AESUtil.Encrypt(encryptString, Constants.AES_SECRET_KEY);
                                String encryptStr = Base64.encode(encryptData.getBytes("UTF-8"));
                                Response response = OkHttpManger.getInstance().getSync(Constants.DYNAMIC_URL + "?" + "Timestamp=" + currentTimeMillis + "&SystemVersion=" + SystemUtil.getSystemVersion() + "&DeviceType=" + "1" + "&EncryptString=" + encryptStr+"&invalidApi="+invalidApi);
                                LogUtil.e("GetDomain_Url",Constants.DYNAMIC_URL + "?" + "Timestamp=" + currentTimeMillis + "&SystemVersion=" + SystemUtil.getSystemVersion() + "&DeviceType=" + "1" + "&EncryptString=" + encryptStr+"&invalidApi="+invalidApi);
                                if (response.isSuccessful()) {
                                    String responseStr = response.body().string();
                                    LogUtil.e("GetDomain",responseStr);
                                    OkHttpManger.getInstance().okHttpHandler.sendEmptyMessage(1);
                                    DynamicHostBean dynamicHostBean = AppUtil.getGson().fromJson(responseStr, DynamicHostBean.class);
                                    SPUtil.putString(context, Constants.DYNAMIC_HOST, dynamicHostBean.getReturnData().getUrl());
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                return new Wrapper(throwable, integer);
                            }
                        } else {
                            return new Wrapper(throwable, integer);
                        }
                    }
                }).flatMap(new Func1<Wrapper, Observable<?>>() {
                    @Override
                    public Observable<?> call(Wrapper wrapper) {
                        if ((wrapper.throwable instanceof ConnectException
                                || wrapper.throwable instanceof SocketTimeoutException
                                || wrapper.throwable instanceof TimeoutException)
                                && wrapper.index < count + 1) { //如果超出重试次数也抛出错误，否则默认是会进入onCompleted
                            return Observable.timer(delay + (wrapper.index - 1) * increaseDelay, TimeUnit.MILLISECONDS);

                        }
                        return Observable.error(wrapper.throwable);
                    }
                });
    }

    public class Wrapper {
        private int index;
        private Throwable throwable;

        public Wrapper(Throwable throwable, int index) {
            this.index = index;
            this.throwable = throwable;
        }
    }

    private String createJson() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Timestamp", System.currentTimeMillis());
        jsonObject.put("SystemVersion", SystemUtil.getSystemVersion());
        jsonObject.put("DeviceType", 1);

        String encryptString = System.currentTimeMillis() + SystemUtil.getSystemVersion() + 1;
        //AES加密（加密模式：ECB ，填充：pkcs7padding，数据块：128位，输出：base64 ,字符集：utf8）
        encryptString = AESUtil.Encrypt(encryptString, Constants.AES_SECRET_KEY);
        encryptString = Base64.encode(encryptString.getBytes("UTF-8"));

        jsonObject.put("EncryptString", encryptString);
        return jsonObject.toString();
    }


}
