package com.kcx.acg.api;

import com.kcx.acg.base.BaseApi;
import com.kcx.acg.bean.SaveProductVO;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.HttpService;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by zjb on 2018/11/30.
 */
public class SaveProductApi extends BaseApi {
    private SaveProductVO saveProductVO;
    public SaveProductVO getSaveProductVO() {
        return saveProductVO;
    }

    public void setSaveProductVO(SaveProductVO saveProductVO) {
        this.saveProductVO = saveProductVO;
    }


    public SaveProductApi(RxAppCompatActivity rxAppCompatActivity) {
        super(rxAppCompatActivity);

    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        return retrofit.create(HttpService.class).saveProduct(getSaveProductVO());
    }
}
