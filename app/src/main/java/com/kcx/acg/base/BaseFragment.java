package com.kcx.acg.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.tmall.wireless.tangram.TangramBuilder;
import com.tmall.wireless.tangram.TangramEngine;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Longer on 2016/10/26.
 */
public abstract class BaseFragment<ACTIVITY_TYPE> extends Fragment implements View.OnClickListener {
    private final static String TAG = "BaseFragment";
    protected ACTIVITY_TYPE mContext;
    private SweetAlertDialog dialog;
    private TangramBuilder.InnerBuilder innerBuilder;
    private TangramEngine engine;
    public VirtualLayoutManager layoutManager;
    public RecyclerView.RecycledViewPool viewPool;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        init();// 可以接受别人传递过来的参数
        mContext = (ACTIVITY_TYPE) getActivity();
        innerBuilder = TangramBuilder.newInnerBuilder(getActivity());
        engine = innerBuilder.build();
        layoutManager = new VirtualLayoutManager(getActivity());
        viewPool = new RecyclerView.RecycledViewPool();
        viewPool.setMaxRecycledViews(0, 20);
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initView(inflater, container, savedInstanceState);
        initUI(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        initData();
        initListener();
        super.onActivityCreated(savedInstanceState);
    }

    public void bindView(RecyclerView recyclerView) {
        engine.bindView(recyclerView);
    }

    public void setRecycledViewPool(RecyclerView recyclerView) {
        recyclerView.setRecycledViewPool(viewPool);
    }

    /**
     * 初始化的时候,可以接收别人传递进来的参数
     */
    public void init() {

    }

    /**
     * 初始化视图
     */
    public void initUI(View view) {

    }

    /**
     * 初始化Fragment应有的视图 ,为抽象
     *
     * @return
     */
    public abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 初始化Fragment应有的数据,必须实现
     */
    public abstract void initData();

    /**
     * 初始化监听器
     */
    public void initListener() {

    }

    @Subscribe
    public void onEventMainThread(BusEvent event) {
        //    Log.d(TAG, "onEventMainThread收到了消息：" + event.getType());
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    protected void closeLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (this.isHidden()) return;
        BaseActivity activity = (BaseActivity) getActivity();
        //activity.closeLoading();
    }

    protected void showLoading() {
        if (dialog == null)
            dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#ffffff"));
        dialog.setCancelable(false);
        dialog.show();
        if (this.isHidden()) return;
        BaseActivity activity = (BaseActivity) getActivity();
        //activity.showLoading();
    }

    protected void showLoading(int time) {
       /* dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog.setCancelable(false);
        dialog.show();*/
        if (this.isHidden()) return;
        BaseActivity activity = (BaseActivity) getActivity();
        //activity.showLoading(time);
    }

    @Override
    public void onResume() {
        super.onResume();
        //LogUtils.d(this.getClass().getSimpleName() + "onResume");
    }

    @Override
    public void onPause() {
        //LogUtils.d(this.getClass().getSimpleName() + "onPause");
        super.onPause();
    }

    public <T> ObservableTransformer<T, T> setThread() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

}
