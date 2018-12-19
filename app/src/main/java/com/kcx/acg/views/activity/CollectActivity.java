package com.kcx.acg.views.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.api.FavouriteProductListApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.base.BusEvent;
import com.kcx.acg.bean.FavouriteProductListBean;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.manager.WorkManager;
import com.kcx.acg.views.adapter.CollectAdapter;
import com.kcx.acg.views.view.LoadingErrorView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 收藏
 * Created by jb on 2018/9/20.
 */
public class CollectActivity extends BaseActivity implements CancelAdapt {
    private LoadingErrorView mLoadingErrorView;
    private LinearLayout ll_empty,ll_back;
    private TextView tv_title, tv_empty_hint;
    private Button  btn_empty;
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;
    private CollectAdapter collectAdapter;
    private int pageSize = 10;//每次请求条数，默认10
    private int pageIndex = 1;
    private List<FavouriteProductListBean.ReturnDataBean.ListBean> list;


    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_collect, null);
    }

    @Override
    public void initView() {
        super.initView();
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.collect);
        ll_back = findViewById(R.id.ll_back);
        //空数据
        ll_empty = findViewById(R.id.ll_empty);
        ll_empty.setBackgroundResource(R.color.gray_f5);
        btn_empty = findViewById(R.id.btn_empty);
        tv_empty_hint = findViewById(R.id.tv_empty_hint);
        tv_empty_hint.setText(R.string.empty_data_collect);
        mLoadingErrorView = findViewById(R.id.loadingErrorView);

        //有数据
        mRecyclerView = findViewById(R.id.rv);
        mRefreshLayout = findViewById(R.id.mRefreshLayout);
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        initRecyclerView(list);
    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);
        btn_empty.setOnClickListener(this);

        //加载更多
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex = pageIndex+1;
                getFavouriteProductList(pageIndex, pageSize);
                refreshLayout.finishLoadMore();
            }
        });

        mLoadingErrorView.setOnReloadListener(new LoadingErrorView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                getFavouriteProductList(1, pageSize);
            }
        });

    }


    @Override
    public void initData() {
        super.initData();
        list = new ArrayList<FavouriteProductListBean.ReturnDataBean.ListBean>();
        getFavouriteProductList(1, pageSize);

    }

    @Override
    public void onEventMainThread(BusEvent event) {
        super.onEventMainThread(event);
        if (event.getType() == BusEvent.COLLECT) {
            list.clear();
            getFavouriteProductList(1,pageSize);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;

            case R.id.btn_empty:  //去逛逛
                setResult(10086);
                finish();
                break;
        }
    }

    /***
     * 初始列表
     */
    private void initRecyclerView(final List<FavouriteProductListBean.ReturnDataBean.ListBean> listData) {
        GridLayoutManager fullyGridLayoutManager = new GridLayoutManager(CollectActivity.this, 2);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(fullyGridLayoutManager);
        collectAdapter = new CollectAdapter(CollectActivity.this, listData);
        // 设置adapter
        mRecyclerView.setAdapter(collectAdapter);

        collectAdapter.setOnItemClickListener(new CollectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position,int canViewType, int productID) {
                WorkManager.getInstances().goToWhere(CollectActivity.this, canViewType, productID);
            }
        });
    }


    /**
     * 获取收藏列表
     *
     * @param pageIndex
     * @param pageSize
     */
    public void getFavouriteProductList(int pageIndex, int pageSize) {
        FavouriteProductListApi favouriteProductListApi = new FavouriteProductListApi(this, new HttpOnNextListener<FavouriteProductListBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(FavouriteProductListBean favouriteProductListBean) {
                mLoadingErrorView.setVisibility(View.GONE);
                if (favouriteProductListBean.getErrorCode() == 200) {
                    list.addAll(favouriteProductListBean.getReturnData().getList());
                    if (list.size() == 0) {
                        //空数据情况
                        ll_empty.setVisibility(View.VISIBLE);
                        mRefreshLayout.setVisibility(View.GONE);
                        return null;
                    } else {
                        ll_empty.setVisibility(View.GONE);
                        mRefreshLayout.setVisibility(View.VISIBLE);
                    }

                    collectAdapter.upData(CollectActivity.this.list);
                    if (list.size() >= favouriteProductListBean.getReturnData().getTotal() && list.size() != 0) {
                        mRefreshLayout.finishLoadMoreWithNoMoreData();
                    }
                } else {
                    favouriteProductListBean.getErrorMsg();
                }
                return null;
            }

            @Override
            public RetryWhenNetworkException.Wrapper onError(Throwable e) {
                super.onError(e);
                mRefreshLayout.setVisibility(View.GONE);
                mLoadingErrorView.setVisibility(View.VISIBLE);
                mLoadingErrorView.onError(e);
                return null;
            }
        });
        favouriteProductListApi.setPageIndex(pageIndex);
        favouriteProductListApi.setPageSize(pageSize);
        HttpManager.getInstance().doHttpDeal(CollectActivity.this, favouriteProductListApi);
    }
}
