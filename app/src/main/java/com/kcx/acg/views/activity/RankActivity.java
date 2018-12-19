package com.kcx.acg.views.activity;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.google.common.collect.Lists;
import com.kcx.acg.R;
import com.kcx.acg.api.GetPopularityInfoApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.bean.GetPopularityInfoBean;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.views.adapter.Top3Adapter;
import com.kcx.acg.views.adapter.TopOtherAdapter;
import com.kcx.acg.views.view.RegisterSuccessDialog;
import com.kcx.acg.views.view.TitleBarView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;

public class RankActivity extends BaseActivity implements CancelAdapt {

    private View rootView;
    private TitleBarView titleBarView;
    private ImageView backIv;
    private ImageView rightIv;
    private TextView titleTv;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private int pageIndex = 1;
    private final int PAGE_SIZE = 20;
    private List<GetPopularityInfoBean.ReturnDataBean.ListBean> top3List, otherList;
    private Top3Adapter top3Adapter;
    private TopOtherAdapter topOtherAdapter;

    @Override
    public View setInitView() {
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_rank_layout, null);
        titleBarView = rootView.findViewById(R.id.rank_title_bar);
        backIv = titleBarView.getIv_in_title_back();
        rightIv = titleBarView.getIv_in_title_right();
        rightIv.setImageResource(R.mipmap.icon_wenhao);
        titleTv = titleBarView.getTv_in_title();
        titleTv.setTextColor(ContextCompat.getColor(this, R.color.white));
        refreshLayout = rootView.findViewById(R.id.rank_refresh_layout);
        refreshLayout.setEnableRefresh(false);
        recyclerView = rootView.findViewById(R.id.rank_rv);
        recyclerView.setRecycledViewPool(viewPool);
        recyclerView.setLayoutManager(layoutManager);
        return rootView;
    }

    @Override
    public void initData() {
        top3List = Lists.newArrayList();
        otherList = Lists.newArrayList();
        getPopularityInfo(pageIndex, PAGE_SIZE);

        SingleLayoutHelper singleLayoutHelper = new SingleLayoutHelper();
        top3Adapter = new Top3Adapter(this, singleLayoutHelper, top3List);

        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();

        topOtherAdapter = new TopOtherAdapter(this, linearLayoutHelper, otherList);
        DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager);
        delegateAdapter.addAdapter(top3Adapter);
        delegateAdapter.addAdapter(topOtherAdapter);
        recyclerView.setAdapter(delegateAdapter);
    }

    public void getPopularityInfo(final int index, int pageSize) {
        GetPopularityInfoApi getPopularityInfoApi = new GetPopularityInfoApi(this);
        getPopularityInfoApi.setPageIndex(index);
        getPopularityInfoApi.setPageSize(pageSize);
        getPopularityInfoApi.setListener(new HttpOnNextListener<GetPopularityInfoBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(GetPopularityInfoBean getPopularityInfoBean) {
                refreshLayout.finishLoadMore();
                List<GetPopularityInfoBean.ReturnDataBean.ListBean> tempList = getPopularityInfoBean.getReturnData().getList();
                for (int i = 0; i < tempList.size(); i++) {
                    if (i < 3) {
                        top3List.add(tempList.get(i));
                    } else {
                        otherList.add(tempList.get(i));
                    }
                }
                top3Adapter.notifyDataSetChanged();
                topOtherAdapter.notifyDataSetChanged();
                if (top3List.size() + otherList.size() >= getPopularityInfoBean.getReturnData().getTotal()) {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    pageIndex++;
                }
                return null;
            }

        });

        HttpManager.getInstance().doHttpDeal(this, getPopularityInfoApi);
    }

    @Override
    public void setListener() {
        backIv.setOnClickListener(this);
        rightIv.setOnClickListener(this);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getPopularityInfo(pageIndex, PAGE_SIZE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_in_title_back:
                finish();
                break;
            case R.id.iv_in_title_right:
                new RegisterSuccessDialog(this,R.layout.dialog_ranking_layout, null);
                break;
        }
    }
}
