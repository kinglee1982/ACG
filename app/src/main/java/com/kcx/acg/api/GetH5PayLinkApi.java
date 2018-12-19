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
 * Created by jb on 2018/10/23.
 */
public class GetH5PayLinkApi extends BaseApi {
    private int payType;
    private int goodsType;
    private int packageID;
    private int buyAmount;
    private int memberCouponID;

    public int getMemberCouponID() {
        return memberCouponID;
    }

    public void setMemberCouponID(int memberCouponID) {
        this.memberCouponID = memberCouponID;
    }

    public int getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(int buyAmount) {
        this.buyAmount = buyAmount;
    }

    public int getPackageID() {
        return packageID;
    }

    public void setPackageID(int packageID) {
        this.packageID = packageID;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }



    public GetH5PayLinkApi(RxAppCompatActivity rxAppCompatActivity, HttpOnNextListener listener) {
        super(rxAppCompatActivity, listener);
        setShowProgress(false);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpService httpService = retrofit.create(HttpService.class);
        return httpService.getH5PayLink(getPayType(),getGoodsType(),getPackageID(),getBuyAmount(),memberCouponID);
    }
}
