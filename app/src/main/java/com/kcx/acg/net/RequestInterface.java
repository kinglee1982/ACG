package com.kcx.acg.net;

import com.kcx.acg.base.BaseEntity;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * Created by Administrator on 2017-11-27.
 */

public interface RequestInterface {
//    @GET("http://122.226.100.91/adyen/listRecurringDetails/uin/102802")
//    Observable<AdyenUserInfoBean> getUserInfo();
//
//    @HTTP(method = "GET", path = "adyen/listRecurringDetails/uin/{uId}", hasBody = false)
//    Call<AdyenUserInfoBean> getUserInfoByHttp(@Path("uId") int uId);
//
//    @POST("recurringPay")
//    Call<PayResultBaen> recurringPay(@Body PayBean payBean);
//
//    @POST("recurringPayByRxJava")
//    Observable<BaseEntity<PayResultBaen>> recurringPayByRxJava(@Body PayBean bean);
//
//    @GET("mobile/get")
//    Observable<PhoneBean<Result>> getPhoneAddress(@Query("phone") long phone, @Query("key") String key);

    /**
     * 测试连接服务器
     *
     * @return
     */
    @GET("testConnection")
    Observable<BaseEntity> testConnection();

    @Multipart
    @POST("upload")
    Observable<BaseEntity> upload(@PartMap Map<String, RequestBody> map);

}
