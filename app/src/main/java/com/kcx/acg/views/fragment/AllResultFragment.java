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
import com.alibaba.android.vlayout.layout.ColumnLayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.google.common.collect.Lists;
import com.kcx.acg.R;
import com.kcx.acg.api.GetSearchRelatedProductApi;
import com.kcx.acg.api.GetSearchRelatedUsersApi;
import com.kcx.acg.api.SearchHotTagApi;
import com.kcx.acg.bean.GetSearchRelatedProductBean;
import com.kcx.acg.bean.GetSearchRelatedUsersBean;
import com.kcx.acg.bean.HotTagBean;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.manager.WorkManager;
import com.kcx.acg.views.activity.InterestProjectActivity;
import com.kcx.acg.views.activity.SearchActivity;
import com.kcx.acg.views.activity.SearchResultActivity;
import com.kcx.acg.views.activity.UserHomeActivity;
import com.kcx.acg.views.adapter.SearchAdapter;
import com.kcx.acg.views.adapter.TitleAdapter;
import com.kcx.acg.views.view.LoadingErrorView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 */

public class AllResultFragment extends LazyFragment<SearchResultActivity> implements CancelAdapt {

    private View rootView;
    private RecyclerView recyclerView;
    private SmartRefreshLayout refreshLayout;
    private LinearLayout emptyLayout;
    private LoadingErrorView loadingErrorView;

    private int pageIndexOfHobbyAndUsers = 1;
    private int pageIndexOfWorks = 1;
    private final int SIZE_PAGE_HOBBY_AND_USERS = 5;
    private final int SIZE_PAGE_WORKS = 10;
    private String searchString;

    private SearchAdapter hobbyAdapter, userAdapter, workAdapter;
    private TitleAdapter hobbyTitleAdapter, usersTitleAdapter, worksTitleAdapter;
    private List<HotTagBean.ReturnDataBean.ListBean> hotTagList;
    private List<GetSearchRelatedUsersBean.ReturnDataBean.ListBean> userList;
    private List<GetSearchRelatedProductBean.ReturnDataBean.ListBean> worksList;

    @Override
    protected void loadData() {
        searchString = (String) getArguments().get(SearchActivity.SEARCH_STRING);

        searchHotTag(searchString, pageIndexOfHobbyAndUsers, SIZE_PAGE_HOBBY_AND_USERS);
        getSearchRelatedUsers(searchString, pageIndexOfHobbyAndUsers, SIZE_PAGE_HOBBY_AND_USERS);
        getSearchRelatedProduct(searchString, pageIndexOfWorks, SIZE_PAGE_WORKS);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_all_result, null);
        recyclerView = rootView.findViewById(R.id.all_result_rv);
        recyclerView.setRecycledViewPool(viewPool);
        recyclerView.setLayoutManager(layoutManager);
        refreshLayout = rootView.findViewById(R.id.all_result_smart_refresh_layout);
        emptyLayout = rootView.findViewById(R.id.all_result_null_layout);
        loadingErrorView = rootView.findViewById(R.id.all_result_loading_error_view);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadMoreWhenContentNotFull(false);

        return rootView;
    }

    @Override
    public void initData() {
//        searchString = (String) getArguments().get(SearchActivity.SEARCH_STRING);

        SingleLayoutHelper singleLayoutHelper = new SingleLayoutHelper();
//        singleLayoutHelper.setBgColor(Color.WHITE);
//        singleLayoutHelper.setMargin(0, 0, 0 ,20);
        hobbyTitleAdapter = new TitleAdapter(mContext, singleLayoutHelper, R.layout.browse_title_layout, getString(R.string.search_result_with_hobby_msg));
        singleLayoutHelper = new SingleLayoutHelper();
//        singleLayoutHelper.setBgColor(Color.WHITE);
//        singleLayoutHelper.setMargin(0, 0, 0 ,18);
        usersTitleAdapter = new TitleAdapter(mContext, singleLayoutHelper, R.layout.browse_title_layout, getString(R.string.search_result_with_users_msg));
        singleLayoutHelper = new SingleLayoutHelper();
//        singleLayoutHelper.setBgColor(Color.WHITE);
//        singleLayoutHelper.setMargin(0, 2, 0 ,0);
        worksTitleAdapter = new TitleAdapter(mContext, singleLayoutHelper, R.layout.browse_title_layout, getString(R.string.search_result_with_works_msg));

        ColumnLayoutHelper columnLayoutHelper = new ColumnLayoutHelper();
        columnLayoutHelper.setBgColor(Color.WHITE);
        columnLayoutHelper.setPadding(30, 0, 30, 30);

        hotTagList = Lists.newArrayList();
        hobbyAdapter = new SearchAdapter(mContext, columnLayoutHelper, R.layout.search_hobby_item_layout, hotTagList);
        GridLayoutHelper columnLayoutHelper2 = new GridLayoutHelper(5);
        columnLayoutHelper2.setPadding(30, 0, 30, 0);
        columnLayoutHelper2.setBgColor(Color.WHITE);
        columnLayoutHelper2.setGap(30);
        columnLayoutHelper2.setAutoExpand(false);

        userList = Lists.newArrayList();
        userAdapter = new SearchAdapter(mContext, columnLayoutHelper2, R.layout.search_users_item_layout, userList, 0, 0);

        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(2);
        gridLayoutHelper.setGap(30);
        gridLayoutHelper.setVGap(30);
        gridLayoutHelper.setBgColor(Color.WHITE);
        gridLayoutHelper.setAutoExpand(false);
        gridLayoutHelper.setPadding(50, 0, 50, 30);
        worksList = Lists.newArrayList();
        workAdapter = new SearchAdapter(mContext, gridLayoutHelper, R.layout.search_works_item_layout, worksList, 0);


        DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager);
        delegateAdapter.addAdapter(hobbyTitleAdapter);
        delegateAdapter.addAdapter(hobbyAdapter);
        delegateAdapter.addAdapter(usersTitleAdapter);
        delegateAdapter.addAdapter(userAdapter);
        delegateAdapter.addAdapter(worksTitleAdapter);
        delegateAdapter.addAdapter(workAdapter);
        recyclerView.setAdapter(delegateAdapter);

    }

    @Override
    public void initListener() {
        hobbyAdapter.setOnHobbyListener(onHobbyListener);
        userAdapter.setOnUserListener(onUserListener);
        workAdapter.setOnWorkListener(onWorkListener);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getSearchRelatedProduct(searchString, pageIndexOfWorks, SIZE_PAGE_WORKS);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                searchHotTag(searchString, pageIndexOfHobbyAndUsers, SIZE_PAGE_HOBBY_AND_USERS);
//                getSearchRelatedUsers(searchString, pageIndexOfHobbyAndUsers, SIZE_PAGE_HOBBY_AND_USERS);
//                getSearchRelatedProduct(searchString, pageIndexOfWorks, SIZE_PAGE_WORKS);
            }
        });
        loadingErrorView.setOnReloadListener(new LoadingErrorView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                searchHotTag(searchString, pageIndexOfHobbyAndUsers, SIZE_PAGE_HOBBY_AND_USERS);
                getSearchRelatedUsers(searchString, pageIndexOfHobbyAndUsers, SIZE_PAGE_HOBBY_AND_USERS);
                getSearchRelatedProduct(searchString, pageIndexOfWorks, SIZE_PAGE_WORKS);
            }
        });
    }

    public void searchHotTag(String searchKey, int pageIndex, int pageSize) {
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
                hotTagList.clear();
                hotTagList.addAll(hotTagBean.getReturnData().getList());
                showEmptyLayout();
                if (hotTagList == null || hotTagList.size() == 0) {
                    hobbyTitleAdapter.setVisibility(false);
                    hobbyTitleAdapter.notifyDataSetChanged();
                }
                hobbyAdapter.notifyDataSetChanged();
                return null;
            }
        });
        HttpManager.getInstance().doHttpDeal(mContext, searchHotTagApi);
    }

    public void getSearchRelatedUsers(final String searchKey, int pageIndex, int pageSize) {
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
                userList.clear();
                userList.addAll(getSearchRelatedUsersBean.getReturnData().getList());
                showEmptyLayout();
                if (userList == null || userList.size() == 0) {
                    usersTitleAdapter.setVisibility(false);
                    usersTitleAdapter.notifyDataSetChanged();
                } else {
                    if (hotTagList != null && hotTagList.size() > 0) {
                        usersTitleAdapter.setLineVisibility(View.VISIBLE);
                    }
                }
                userAdapter.setKeyWord(searchKey);
                userAdapter.notifyDataSetChanged();

                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(mContext, getSearchRelatedUsersApi);
    }

    public void getSearchRelatedProduct(String searchKey, final int pageIndex, int pageSize) {
        GetSearchRelatedProductApi getSearchRelatedProductApi = new GetSearchRelatedProductApi(mContext);
        getSearchRelatedProductApi.setSearchKey(searchKey);
        getSearchRelatedProductApi.setPageIndex(pageIndex);
        getSearchRelatedProductApi.setPageSize(pageSize);
        getSearchRelatedProductApi.setListener(new HttpOnNextListener<GetSearchRelatedProductBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onError(Throwable e) {
                super.onError(e);
                loadingErrorView.onError(e);
                return null;
            }

            @Override
            public RetryWhenNetworkException.Wrapper onNext(GetSearchRelatedProductBean getSearchRelatedProductBean) {
                refreshLayout.finishRefresh();
                if(pageIndex == 1){
                    worksList.clear();
                }
                worksList.addAll(getSearchRelatedProductBean.getReturnData().getList());
                showEmptyLayout();
                if (worksList == null || worksList.size() == 0) {
                    worksTitleAdapter.setVisibility(false);
                    worksTitleAdapter.notifyDataSetChanged();
                } else {
                    if (hotTagList != null && hotTagList.size() > 0 || userList != null && userList.size() > 0) {
                        worksTitleAdapter.setLineVisibility(View.VISIBLE);
                    }
                }
                workAdapter.notifyDataSetChanged();
                if (worksList.size() >= getSearchRelatedProductBean.getReturnData().getTotal()) {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    pageIndexOfWorks++;
                    refreshLayout.finishLoadMore();
                }

                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(mContext, getSearchRelatedProductApi);
    }

    public void showEmptyLayout(){
        if(hotTagList.size() == 0 && userList.size() == 0 && worksList.size() == 0){
            emptyLayout.setVisibility(View.VISIBLE);
        }else {
            emptyLayout.setVisibility(View.GONE);
        }
    }

    private SearchAdapter.OnHobbyListener onHobbyListener = new SearchAdapter.OnHobbyListener() {
        @Override
        public void onHobby(View view, int position) {
            Intent intent = new Intent(mContext, InterestProjectActivity.class);
            intent.putExtra("tagID", hotTagList.get(position).getTagID());
            mContext.startDDMActivity(intent, true);
        }

        @Override
        public void onMore(View view, int position) {
            ((SearchResultActivity) getActivity()).viewPager.setCurrentItem(1);
        }
    };

    private SearchAdapter.OnUserListener onUserListener = new SearchAdapter.OnUserListener() {
        @Override
        public void onUser(View view, int position) {
            Intent intent = new Intent(mContext, UserHomeActivity.class);
            intent.putExtra("memberID", userList.get(position).getMemberID());
            mContext.startDDMActivity(intent, true);
        }

        @Override
        public void onMore(View view, int position) {
            ((SearchResultActivity) getActivity()).viewPager.setCurrentItem(2);
        }
    };

    private SearchAdapter.OnWorkListener onWorkListener = new SearchAdapter.OnWorkListener() {
        @Override
        public void onWork(View view, int position) {
            GetSearchRelatedProductBean.ReturnDataBean.ListBean item = worksList.get(position);
            WorkManager.getInstances().goToWhere(mContext, item.getCanViewType(), item.getProductID());
        }
    };
}
