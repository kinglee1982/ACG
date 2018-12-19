package com.kcx.acg.https;

import android.app.Dialog;
import android.content.Context;
import com.kcx.acg.R;
import com.kcx.acg.base.BaseApi;
import com.kcx.acg.base.SysApplication;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.CookieDbUtil;
import com.kcx.acg.utils.DialogMaker;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.views.view.CustomToast;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import java.lang.ref.SoftReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import rx.Observable;
import rx.Subscriber;

/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * Created by jb.
 */
public class ProgressSubscriber<T> extends Subscriber<T> {
    /*是否弹框*/
    private boolean showPorgress = true;
    /* 软引用回调接口*/
    private HttpOnNextListener mSubscriberOnNextListener;

    /*软引用反正内存泄露*/
    private SoftReference<RxAppCompatActivity> mActivity;
    /*请求数据*/
    private BaseApi api;
    private Dialog dialog_progress = null;
    //    private LottieLoadingDialog dialog;
    //    private LottieLoadingDialog.Builder dialogBuilder;

    /**
     * 构造
     *
     * @param api
     */
    public ProgressSubscriber(BaseApi api) {
        this.api = api;
        this.mSubscriberOnNextListener = api.getListener();
        this.mActivity = new SoftReference<>(api.getRxAppCompatActivity());
        setShowPorgress(api.isShowProgress());
        if (api.isShowProgress()) {
            initProgressDialog(api.isCancel());
            //            initLottieLoadingDialog(api.isCancel());
        }
    }

    /**
     * 初始化加载框
     */
    private void initProgressDialog(boolean cancel) {
        Context context = mActivity.get();
        if (!isShowPorgress())
            return;
        if (dialog_progress == null) {
            dialog_progress = DialogMaker.showCommenWaitDialog(context, "", false);
        }
    }

    public void initLottieLoadingDialog(boolean cancel) {
        //        dialogBuilder =new LottieLoadingDialog.Builder(mActivity.get());
        //        dialog = dialogBuilder.create();
        //        if (cancel) {
        //            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
        //                @Override
        //                public void onCancel(DialogInterface dialogInterface) {
        //                    if (mSubscriberOnNextListener.get() != null) {
        //                        mSubscriberOnNextListener.get().onCancel();
        //                    }
        //                    onCancelProgress();
        //                }
        //            });
        //        }
    }


    /**
     * 显示加载框
     */
    private void showProgressDialog() {
        if (!isShowPorgress())
            return;
        Context context = mActivity.get();
        if (dialog_progress == null || context == null)
            return;
        if (!dialog_progress.isShowing()) {
            dialog_progress.show();
        }
    }

    private void showLottieLoadingDialog() {
        //        if (!isShowPorgress())
        //            return;
        //        dialog.show();
    }

    private void dismissLottieLoadingDialog() {
        //        if (!isShowPorgress())
        //            return;
        //        dialog.dismiss();
    }

    /**
     * 隐藏
     */
    private void dismissProgressDialog() {
        if (!isShowPorgress())
            return;
        if (dialog_progress != null && dialog_progress.isShowing()) {
            dialog_progress.dismiss();
        }
    }


    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {

        showProgressDialog();
        //        showLottieLoadingDialog();
        /*缓存并且有网*/
        if (api.isCache() && AppUtil.isNetworkAvailable(SysApplication.getInstance())) {
            /*获取缓存数据*/
            CookieResulte cookieResulte = CookieDbUtil.getInstance().queryCookieBy(api.getUrl());
            if (cookieResulte != null) {
                long time = (System.currentTimeMillis() - cookieResulte.getTime()) / 1000;
                if (time < api.getCookieNetWorkTime()) {
                    if (mSubscriberOnNextListener != null)
                        if (mSubscriberOnNextListener != null) {
                            mSubscriberOnNextListener.onCacheNext(cookieResulte.getResulte());
                        }
                    onCompleted();
                    unsubscribe();
                }
            }
        }
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
        dismissProgressDialog();
        //        dismissLottieLoadingDialog();
    }


    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
        //        dismissLottieLoadingDialog();

        /*需要緩存并且本地有缓存才返回*/
        if (api.isCache()) {
            Observable.just(api.getUrl()).subscribe(new Subscriber<String>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    errorDo(e);
                }

                @Override
                public void onNext(String s) {
                    /*获取缓存数据*/
                    CookieResulte cookieResulte = CookieDbUtil.getInstance().queryCookieBy(s);
                    if (cookieResulte == null) {
                        throw new HttpTimeException("network error");
                    }
                    long time = (System.currentTimeMillis() - cookieResulte.getTime()) / 1000;
                    if (time < api.getCookieNoNetWorkTime()) {
                        if (mSubscriberOnNextListener != null)
                            if (mSubscriberOnNextListener != null) {
                                mSubscriberOnNextListener.onCacheNext(cookieResulte.getResulte());
                            }
                    } else {
                        CookieDbUtil.getInstance().deleteCookie(cookieResulte);
                        throw new HttpTimeException("network error");
                    }
                }
            });
        } else {
            errorDo(e);
        }
    }

    /*错误统一处理*/
    private void errorDo(Throwable e) {
        LogUtil.e("onError", "错误" + e.getMessage());
        Context context = mActivity.get();
        if (context == null)
            return;
        if (e instanceof SocketTimeoutException) {
            CustomToast.showToast(context.getString(R.string.load_error_msg));
        } else if (e instanceof ConnectException) {
            CustomToast.showToast(context.getString(R.string.net_error));
        } else {
            CustomToast.showToast(context.getString(R.string.net_error));
        }
        if (mSubscriberOnNextListener != null)
            if (mSubscriberOnNextListener != null) {
                mSubscriberOnNextListener.onError(e);
            }
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener != null)
            if (mSubscriberOnNextListener != null) {
                mSubscriberOnNextListener.onNext(t);
            }
    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }


    public boolean isShowPorgress() {
        return showPorgress;
    }

    /**
     * 是否需要弹框设置
     *
     * @param showPorgress
     */
    public void setShowPorgress(boolean showPorgress) {
        this.showPorgress = showPorgress;
    }
}