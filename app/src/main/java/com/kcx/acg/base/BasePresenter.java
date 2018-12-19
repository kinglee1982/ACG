package com.kcx.acg.base;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 */

public interface BasePresenter {
    void start();
    <T> ObservableTransformer<T, T> setThread();
}
