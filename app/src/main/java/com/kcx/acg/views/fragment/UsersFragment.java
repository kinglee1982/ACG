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
import com.kcx.acg.api.GetSearchRelatedUsersApi;
import com.kcx.acg.bean.GetSearchRelatedUsersBean;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.views.activity.SearchActivity;
import com.kcx.acg.views.activity.SearchResultActivity;
import com.kcx.acg.views.activity.UserHomeActivity;
import com.kcx.acg.views.adapter.UsersAdapter;
import com.kcx.acg.views.view.LoadingErrorView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 */

public class UsersFragment extends LazyFragment<SearchResultActivity> implements CancelAdapt {

    private View rootView;
    private RecyclerView userRv;
    private SmartRefreshLayout refreshLayout;
    private LinearLayout emptyLayout;
    private LoadingErrorView loadingErrorView;

    private String searchKey;
    private int currentPage = 1;
    private static final int SIZE_PAGE = 10;
    private List<GetSearchRelatedUsersBean.ReturnDataBean.ListBean> userList;
    private UsersAdapter userAdapter;

    @Override
    protected void loadData() {
        searchKey = (String) getArguments().get(SearchActivity.SEARCH_STRING);
        getSearchRelatedUsers(searchKey,currentPage, SIZE_PAGE);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_users, null);
        userRv = rootView.findViewById(R.id.search_result_users_rv);
        refreshLayout = rootView.findViewById(R.id.search_result_users_refresh_layout);
        emptyLayout = rootView.findViewById(R.id.search_result_users_null_layout);
        loadingErrorView = rootView.findViewById(R.id.search_result_users_loading_error_view);
        refreshLayout.setEnableRefresh(false);

        refreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        userRv.setLayoutManager(layoutManager);
        userRv.setRecycledViewPool(viewPool);
        bindView(userRv);
        return rootView;
    }

    @Override
    public void initData() {
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        linearLayoutHelper.setBgColor(Color.WHITE);
        userList = Lists.newArrayList();
        userAdapter = new UsersAdapter(mContext, linearLayoutHelper, userList);

        DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager);
        delegateAdapter.addAdapter(userAdapter);
        userRv.setAdapter(delegateAdapter);
    }

    @Override
    public void initListener() {
        userAdapter.setOnFollowListener(onFollowListener);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getSearchRelatedUsers(searchKey,currentPage, SIZE_PAGE);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getSearchRelatedUsers(searchKey,currentPage, SIZE_PAGE);
            }
        });
        loadingErrorView.setOnReloadListener(new LoadingErrorView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                getSearchRelatedUsers(searchKey,currentPage, SIZE_PAGE);
            }
        });
    }

    public void getSearchRelatedUsers(String searchKey, final int pageIndex, int pageSize) {
        GetSearchRelatedUsersApi getSearchRelatedUsersApi = new GetSearchRelatedUsersApi(mContext);
        getSearchRelatedUsersApi.setSearchKey(searchKey);
        getSearchRelatedUsersApi.setPageIndex(pageIndex);
        getSearchRelatedUsersApi.setPageSize(pageSize);
        getSearchRelatedUsersApi.setListener(new HttpOnNextListener<GetSearchRelatedUsersBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onError(Throwable e) {
                super.onError(e);
                loadingErrorView.onError(e);
                return null;
            }

            @Override
            public RetryWhenNetworkException.Wrapper onNext(GetSearchRelatedUsersBean getSearchRelatedUsersBean) {
                refreshLayout.finishRefresh();
                if(pageIndex == 1){
                    userList.clear();
                }
                userList.addAll(getSearchRelatedUsersBean.getReturnData().getList());
                if(userList.size() > 0){
                    emptyLayout.setVisibility(View.GONE);
                }else {
                    emptyLayout.setVisibility(View.VISIBLE);
                }
                if(userList.size() >= getSearchRelatedUsersBean.getReturnData().getTotal()){
                    refreshLayout.finishLoadMoreWithNoMoreData();
                }else {
                    currentPage++;
                    refreshLayout.finishLoadMore();
                }
                userAdapter.notifyDataSetChanged();
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(mContext, getSearchRelatedUsersApi);
    }



    private UsersAdapter.OnFollowListener onFollowListener = new UsersAdapter.OnFollowListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent(mContext, UserHomeActivity.class);
            intent.putExtra("memberID", userList.get(position).getMemberID());
            mContext.startDDMActivity(intent, true);
        }
    };
}
