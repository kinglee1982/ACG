package com.kcx.acg.api;

import com.kcx.acg.base.BaseApi;
import com.kcx.acg.base.SysApplication;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpService;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.utils.SPUtil;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * 验证短信验证码和手机
 * Created by jb on 2018/9/11.
 */
public class ValidateIdentifyCodeApi extends BaseApi {

    private String code;
    private String phone;
    private String areaCode;

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public ValidateIdentifyCodeApi(HttpOnNextListener listener, RxAppCompatActivity rxAppCompatActivity) {
        super(listener, rxAppCompatActivity);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpService httpService = retrofit.create(HttpService.class);
        return httpService.validateIdentifyCode(getCode(),getPhone(),getAreaCode());
    }
}
