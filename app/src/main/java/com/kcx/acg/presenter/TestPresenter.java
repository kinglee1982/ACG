package com.kcx.acg.presenter;

import com.apkfuns.logutils.LogUtils;
import com.kcx.acg.base.BaseEntity;
import com.kcx.acg.contract.TestContract;
import com.kcx.acg.net.RetrofitFactory;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 */

public class TestPresenter implements TestContract.Presenter {
    private TestContract.View testView;

    public TestPresenter(TestContract.View view) {
        testView = checkNotNull(view, "userView  不能为空");
        testView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public <T> ObservableTransformer<T, T> setThread() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    @Override
    public void test() {
        RetrofitFactory.getInstance().API()
                .testConnection()
                .compose(this.<BaseEntity>setThread())
                .subscribe(new Observer<BaseEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseEntity baseEntity) {
                        LogUtils.d("-----------------------------" + baseEntity.getMsg());
                        testView.closeLoading();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
