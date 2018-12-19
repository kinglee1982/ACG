package com.kcx.acg.views.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.google.common.collect.Lists;
import com.kcx.acg.R;
import com.kcx.acg.api.SearchHotTagApi;
import com.kcx.acg.bean.HotTagBean;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.views.activity.InterestProjectActivity;
import com.kcx.acg.views.activity.SearchActivity;
import com.kcx.acg.views.activity.SearchResultActivity;
import com.kcx.acg.views.adapter.HobbyAdapter;
import com.kcx.acg.views.view.LoadingErrorView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 */

public class HobbyFragment extends LazyFragment<SearchResultActivity>  implements CancelAdapt {

    private View rootView;
    private RecyclerView hobbyRv;
    private SmartRefreshLayout refreshLayout;
    private LoadingErrorView loadingErrorView;
    private LinearLayout emptyLayout;
    private String searchKey;
    private int currentPage = 1;
    private static final int SIZE_PAGE = 10;
    private List<HotTagBean.ReturnDataBean.ListBean> hotTagList;
    private HobbyAdapter hobbyAdapter;

    @Override
    protected void loadData() {
        searchKey = (String) getArguments().get(SearchActivity.SEARCH_STRING);
        searchHotTag(searchKey, currentPage, SIZE_PAGE);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_hobby, null);
        hobbyRv = rootView.findViewById(R.id.search_result_hobby_rv);
        refreshLayout = rootView.findViewById(R.id.search_result_hobby_refresh_layout);
        emptyLayout = rootView.findViewById(R.id.search_result_hobby_null_layout);
        loadingErrorView = rootView.findViewById(R.id.search_result_hobby_loading_error_view);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.autoRefresh();
        refreshLayout.setEnableLoadMoreWhenContentNotFull(false);

        hobbyRv.setLayoutManager(layoutManager);
        hobbyRv.setRecycledViewPool(viewPool);
        bindView(hobbyRv);
        return rootView;
    }

    @Override
    public void initListener() {
        hobbyAdapter.setOnItemClickListener(onItemClickListener);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                searchHotTag(searchKey, currentPage, SIZE_PAGE);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                searchHotTag(searchKey, currentPage, SIZE_PAGE);
            }
        });
        loadingErrorView.setOnReloadListener(new LoadingErrorView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                searchHotTag(searchKey, currentPage, SIZE_PAGE);
            }
        });
    }

    @Override
    public void initData() {
        hotTagList = Lists.newArrayList();
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        linearLayoutHelper.setBgColor(Color.WHITE);
        hobbyAdapter = new HobbyAdapter(mContext, linearLayoutHelper, hotTagList);

        DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager);
        delegateAdapter.addAdapter(hobbyAdapter);
        hobbyRv.setAdapter(delegateAdapter);
    }


    public void searchHotTag(String searchKey, final int pageIndex, int pageSize) {
        SearchHotTagApi searchHotTagApi = new SearchHotTagApi(mContext);
        searchHotTagApi.setSearchKey(searchKey);
        searchHotTagApi.setPageIndex(pageIndex);
        searchHotTagApi.setPageSize(pageSize);
        searchHotTagApi.setListener(new HttpOnNextListener<HotTagBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onError(Throwable e) {
                super.onError(e);
                loadingErrorView.onError(e);
                return null;
            }

            @Override
            public RetryWhenNetworkException.Wrapper onNext(HotTagBean hotTagBean) {
                refreshLayout.finishRefresh();
                if(pageIndex == 1){
                    hotTagList.clear();
                }
                hotTagList.addAll(hotTagBean.getReturnData().getList());
                hobbyAdapter.notifyDataSetChanged();
                if(hotTagList.size() > 0){
                    emptyLayout.setVisibility(View.GONE);
                }else {
                    emptyLayout.setVisibility(View.VISIBLE);
                }
                if (hotTagList.size() >= hotTagBean.getReturnData().getTotal()) {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    currentPage++;
                    refreshLayout.finishLoadMore();
                }
                return null;
            }
        });
        HttpManager.getInstance().doHttpDeal(mContext, searchHotTagApi);
    }

    private HobbyAdapter.OnItemClickListener onItemClickListener = new HobbyAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent(mContext, InterestProjectActivity.class);
            intent.putExtra("tagID", hotTagList.get(position).getTagID());
            mContext.startDDMActivity(intent, true);
        }
    };
}
