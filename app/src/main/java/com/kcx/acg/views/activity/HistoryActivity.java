package com.kcx.acg.views.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kcx.acg.R;
import com.kcx.acg.api.DelMyBrowseHistoryApi;
import com.kcx.acg.api.GetMyBrowseHistoryApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.bean.DelMyBrowseHistoryBean;
import com.kcx.acg.bean.GetMyBrowseHistoryBean;
import com.kcx.acg.bean.HistoryBean;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.utils.FormatCurrentData;
import com.kcx.acg.views.adapter.HistoryAdapter;
import com.kcx.acg.views.view.BottomDialog2;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.LoadingErrorView;
import com.kcx.acg.views.view.TitleBarView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import me.jessyan.autosize.internal.CancelAdapt;

public class HistoryActivity extends BaseActivity implements CancelAdapt {

    private  int pageIndex = 1;
    private static final int PAGE_SIZE = 30;

    private View rootView;
    private TitleBarView titleBarView;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ImageView backIv;
    private TextView rightTv;
    private LinearLayout emptyLayout;
    private LoadingErrorView loadingErrorView;

    private List<HistoryBean> dataList;
    private HistoryAdapter historyAdapter;
    private BottomDialog2.Builder builder;

    @Override
    public View setInitView() {
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_history_layout, null);
        titleBarView = rootView.findViewById(R.id.history_title_bar);
        backIv = titleBarView.getIv_in_title_back();
        rightTv = titleBarView.getTv_in_title_right();
        rightTv.setText(getString(R.string.search_clear_msg));

        emptyLayout = rootView.findViewById(R.id.history_empty_layout);
        loadingErrorView = rootView.findViewById(R.id.history_loading_error_view);
        refreshLayout = rootView.findViewById(R.id.history_refresh_layout);
        recyclerView = rootView.findViewById(R.id.history_recycle_view);
        setRecycledViewPool(recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadMoreWhenContentNotFull(true);
        return rootView;
    }

    @Override
    public void initView() {
        dataList = Lists.newArrayList();
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        historyAdapter = new HistoryAdapter(this, linearLayoutHelper, dataList);
        DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager);
        delegateAdapter.addAdapter(historyAdapter);
        recyclerView.setAdapter(delegateAdapter);
    }

    @Override
    public void initData() {
        getMyBrowseHistory(pageIndex, PAGE_SIZE);
    }

    @Override
    public void setListener() {
        backIv.setOnClickListener(this);
        rightTv.setOnClickListener(this);
        loadingErrorView.setOnReloadListener(new LoadingErrorView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                getMyBrowseHistory(pageIndex, PAGE_SIZE);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getMyBrowseHistory(pageIndex, PAGE_SIZE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_in_title_back:
                finish();
                break;
            case R.id.tv_in_title_right:
                builder = new BottomDialog2.Builder(this);
                        builder.setLayoutId(R.layout.dialog_del_history_layout).show();
                        builder.setOnConfirmListener(new BottomDialog2.Builder.OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                delMyBrowseHistoryApi();
                            }
                        });
                break;
        }
    }

    public void delMyBrowseHistoryApi(){
        DelMyBrowseHistoryApi delMyBrowseHistoryApi = new DelMyBrowseHistoryApi(this);
        delMyBrowseHistoryApi.setListener(new HttpOnNextListener<DelMyBrowseHistoryBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(DelMyBrowseHistoryBean delMyBrowseHistoryBean) {
                if(delMyBrowseHistoryBean.getErrorCode() == 200){
                    builder.dismiss();
                    getMyBrowseHistory(1, PAGE_SIZE);
                }else {
                    CustomToast.showToast(delMyBrowseHistoryBean.getErrorMsg());
                }
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(this, delMyBrowseHistoryApi);
    }

    public void getMyBrowseHistory(final int index, int pageSize){
        GetMyBrowseHistoryApi getMyBrowseHistoryApi = new GetMyBrowseHistoryApi(this);
        getMyBrowseHistoryApi.setPageIndex(index);
        getMyBrowseHistoryApi.setPageSize(pageSize);
        getMyBrowseHistoryApi.setListener(new HttpOnNextListener<GetMyBrowseHistoryBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onError(Throwable e) {
                loadingErrorView.onError(e);
                return super.onError(e);
            }

            @Override
            public RetryWhenNetworkException.Wrapper onNext(GetMyBrowseHistoryBean getMyBrowseHistoryBean) {
                if(getMyBrowseHistoryBean.getErrorCode() == 200){
                    if(getMyBrowseHistoryBean.getReturnData().getTotal() == 0){
                        emptyLayout.setVisibility(View.VISIBLE);
                        rightTv.setVisibility(View.GONE);
                    }
                    dataList.addAll(getDataList(getMyBrowseHistoryBean.getReturnData().getList()));
                    historyAdapter.notifyDataSetChanged();

                    if(dataList.size() >= getMyBrowseHistoryBean.getReturnData().getTotal()){
                        refreshLayout.finishLoadMoreWithNoMoreData();
                    }else {
                        refreshLayout.finishLoadMore();
                        pageIndex++;
                    }
                }else {
                    refreshLayout.finishLoadMore();
                }
                return null;

            }

        });
        HttpManager.getInstance().doHttpDeal(this, getMyBrowseHistoryApi);
    }

    public List<HistoryBean> getDataList(List<GetMyBrowseHistoryBean.ReturnDataBean.ListBean> list){
        HistoryBean bean;
        String tempDate = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<HistoryBean> dataList = Lists.newArrayList();
        for (int i = 0; i < list.size(); i++) {
            try {
                bean = new HistoryBean();
                String date = format.format(format.parse(list.get(i).getCreateTime()));
                if (date.compareTo(tempDate) == 0) {
                    bean.setFlag(false);
                } else {
                    bean.setFlag(true);
                }
                tempDate = date;
                bean.setBean(list.get(i));
                dataList.add(bean);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return dataList;
    }
}
