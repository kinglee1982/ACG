package com.kcx.acg.net;



import com.kcx.acg.conf.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017-11-29.
 */

public class RetrofitFactory {
    private static RetrofitFactory mRetrofitFactory;
    private RequestInterface requestInterface;

    private RetrofitFactory(){
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(InterceptorUtil.HeaderInterceptor())
                .addInterceptor(InterceptorUtil.LogInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_UPLOAD)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        requestInterface = retrofit.create(RequestInterface.class);
    }

    public static RetrofitFactory getInstance(){
        if(mRetrofitFactory == null){
            synchronized (RetrofitFactory.class){
                mRetrofitFactory = new RetrofitFactory();
            }
        }
        return mRetrofitFactory;
    }

    public RequestInterface API(){
        return requestInterface;
    }
}
