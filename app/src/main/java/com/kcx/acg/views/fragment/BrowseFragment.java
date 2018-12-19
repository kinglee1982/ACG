package com.kcx.acg.views.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.transition.AutoTransition;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.ColumnLayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.google.common.collect.Lists;
import com.kcx.acg.R;
import com.kcx.acg.api.GetAdvertisementInfoApi;
import com.kcx.acg.api.GetRecommendApi;
import com.kcx.acg.api.SearchHotTagApi;
import com.kcx.acg.base.BusEvent;
import com.kcx.acg.bean.GetAdvertisementInfoBean;
import com.kcx.acg.bean.GetRecommendBean;
import com.kcx.acg.bean.HotTagBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.views.activity.AdvertisActivity;
import com.kcx.acg.views.activity.InterestProjectActivity;
import com.kcx.acg.views.activity.MainActivity;
import com.kcx.acg.views.activity.SearchActivity;
import com.kcx.acg.views.activity.UserHomeActivity;
import com.kcx.acg.views.adapter.RecommendAdapter;
import com.kcx.acg.views.adapter.TitleAdapter;
import com.kcx.acg.views.view.LoadingErrorView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.tmall.wireless.tangram.TangramBuilder;
import com.tmall.wireless.tangram.TangramEngine;

import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 */

public class BrowseFragment extends LazyFragment<MainActivity> implements CancelAdapt {
    private View rootView;
    private TangramBuilder.InnerBuilder innerBuilder;
    private TangramEngine engine;
    private RecyclerView mRecyclerView;
    private VirtualLayoutManager layoutManager;
    private SmartRefreshLayout refreshLayout;
    private LoadingErrorView loadingErrorView;
    private RecommendAdapter browseAdapter;
    private RecommendAdapter hotAdapter;
    private TitleAdapter bannerAdapter, searchAdapter;
    private List<GetRecommendBean.ReturnDataBean> reCommendList;
    private List<HotTagBean.ReturnDataBean.ListBean> hotTagList;
    private GetAdvertisementInfoBean.ReturnDataBean advertisBean;
    private int currentPage = 1;
    private int SIZE_PAGE = 100;


    @Override
    protected void loadData() {
        refreshLayout.autoRefresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        getRecommend();
//        ((MainActivity)getActivity()).changeFindStatusBar();
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.browse_fragment_layout, null);
        mRecyclerView = rootView.findViewById(R.id.browse_fragment_rv);
        loadingErrorView = rootView.findViewById(R.id.browse_fragment_loading_error_view);


//        refreshLayout.setHeaderHeight(50);
//        refreshLayout.setHeaderMaxDragRate(2);
//        refreshLayout.setHeaderTriggerRate(1);
//        refreshLayout.setEnableOverScrollBounce(true);

        innerBuilder = TangramBuilder.newInnerBuilder(mContext);
        engine = innerBuilder.build();
        engine.bindView(mRecyclerView);

        layoutManager = new VirtualLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        mRecyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 10);
        refreshLayout = rootView.findViewById(R.id.browse_refresh_layout);
        refreshLayout.setEnableLoadMore(false);
//        new TestPresenter(this);
//        presenter.start();
//        presenter.test();

        return rootView;
    }

    @Override
    public void initListener() {
        bannerAdapter.setOnAdvertisListener(onAdvertisListener);
        browseAdapter.setOnFollowListener(onFollowListener);
        hotAdapter.setOnHobbyListener(onHobbyListener);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getRecommend();
                searchHotTag("", currentPage, SIZE_PAGE);
                getAdvertisementInfo(Constants.KEY_ADVERTIS_FIND);
            }
        });
        loadingErrorView.setOnReloadListener(new LoadingErrorView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                getRecommend();
                searchHotTag("", currentPage, SIZE_PAGE);
                getAdvertisementInfo(Constants.KEY_ADVERTIS_FIND);
            }
        });
    }

    @Override
    public void initData() {
        SingleLayoutHelper singleLayoutHelper = new SingleLayoutHelper();

        ColumnLayoutHelper columnLayoutHelper = new ColumnLayoutHelper();
        columnLayoutHelper.setItemCount(2);// 设置布局里Item个数
        columnLayoutHelper.setPadding(30, 0, 30, 30);// 设置LayoutHelper的子元素相对LayoutHelper边缘的距离
        columnLayoutHelper.setBgColor(Color.WHITE);// 设置背景颜色
//        columnLayoutHelper.setAspectRatio(10);// 设置设置布局内每行布局的宽与高的比
        columnLayoutHelper.setWeights(new float[]{50, 50});

        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(3);
        gridLayoutHelper.setItemCount(4);// 设置布局里Item个数
        gridLayoutHelper.setPadding(45, 0, 45, 0);// 设置LayoutHelper的子元素相对LayoutHelper边缘的距离
//        gridLayoutHelper.setMargin(20, 20, 20, 20);// 设置LayoutHelper边缘相对父控件（即RecyclerView）的距离
        gridLayoutHelper.setBgColor(Color.WHITE);// 设置背景颜色
//        gridLayoutHelper.setAspectRatio(5);// 设置设置布局内每行布局的宽与高的比

//        gridLayoutHelper.setWeights(new float[]{30, 30, 30});//设置每行中 每个网格宽度 占 每行总宽度 的比例
        gridLayoutHelper.setVGap(20);// 控制子元素之间的垂直间距
        gridLayoutHelper.setHGap(20);// 控制子元素之间的水平间距
        gridLayoutHelper.setAutoExpand(false);//是否自动填充空白区域
        gridLayoutHelper.setSpanCount(4);// 设置每行多少个网格

        reCommendList = Lists.newArrayList();
        searchAdapter = new TitleAdapter(mContext, singleLayoutHelper, R.layout.browse_search_layout);
        searchAdapter.setOnSearchClickListener(onSearchClickListener);
        browseAdapter = new RecommendAdapter(mContext, columnLayoutHelper, R.layout.browse_recommend_layout, reCommendList);
        singleLayoutHelper = new SingleLayoutHelper();
        singleLayoutHelper.setMargin(0, 26, 0, 0);
        singleLayoutHelper.setBgColor(Color.WHITE);
        TitleAdapter titleAdapter = new TitleAdapter(mContext, singleLayoutHelper, R.layout.browse_title_layout, getString(R.string.browse_hot_msg));
        singleLayoutHelper = new SingleLayoutHelper();
        singleLayoutHelper.setMargin(0, 0, 0, 0);
        bannerAdapter = new TitleAdapter(mContext, singleLayoutHelper, R.layout.browse_banner_layout, advertisBean);

        hotTagList = Lists.newArrayList();
        gridLayoutHelper.setPaddingBottom(40);
        hotAdapter = new RecommendAdapter(mContext, gridLayoutHelper, R.layout.browse_hot_item_layout, hotTagList, 0);
        DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager);
        List<DelegateAdapter.Adapter> adapters = Lists.newArrayList();
        adapters.add(searchAdapter);
        adapters.add(browseAdapter);
        adapters.add(titleAdapter);
        adapters.add(hotAdapter);
        adapters.add(bannerAdapter);
        delegateAdapter.addAdapters(adapters);
        mRecyclerView.setAdapter(delegateAdapter);
    }

    @Override
    public void onEventMainThread(BusEvent event) {
        super.onEventMainThread(event);
        if(event.getType() == BusEvent.ATTENTION){
            getRecommend();
        }
        if(event.getType() == BusEvent.CLICK_STAR_TAB){
            getRecommend();
        }
    }

    public void getRecommend() {
        GetRecommendApi getRecommendApi = new GetRecommendApi(mContext);
        getRecommendApi.setListener(new HttpOnNextListener<GetRecommendBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(GetRecommendBean getRecommendBean) {
                refreshLayout.finishRefresh();
                reCommendList.clear();
                reCommendList.addAll(getRecommendBean.getReturnData());
                if(reCommendList.size() == 0){
                    searchAdapter.setRecommendVisibility(View.GONE);
                }else {
                    searchAdapter.setRecommendVisibility(View.VISIBLE);
                }
                browseAdapter.setCheckedMap(reCommendList);
                browseAdapter.notifyDataSetChanged();
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(mContext, getRecommendApi);
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
                hotAdapter.notifyDataSetChanged();

                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(mContext, searchHotTagApi);
    }

    public void getAdvertisementInfo(int locationID) {
        GetAdvertisementInfoApi getAdvertisementInfoApi = new GetAdvertisementInfoApi(mContext);
        getAdvertisementInfoApi.setLocationID(locationID);
        getAdvertisementInfoApi.setListener(new HttpOnNextListener<GetAdvertisementInfoBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(GetAdvertisementInfoBean getAdvertisementInfoBean) {
                if (getAdvertisementInfoBean.getReturnData() != null)
                    if (getAdvertisementInfoBean.getErrorCode() == 200) {
                    advertisBean = getAdvertisementInfoBean.getReturnData();
                    bannerAdapter.setAdvertisBean(advertisBean);
                    bannerAdapter.notifyDataSetChanged();
                }
                return null;
            }

        });

        HttpManager.getInstance().doHttpDeal(mContext, getAdvertisementInfoApi);
    }

    private TitleAdapter.OnSearchClickListener onSearchClickListener = new TitleAdapter.OnSearchClickListener() {
        @Override
        public void onSearchClick(View view) {
            if (android.os.Build.VERSION.SDK_INT > 20) {
                startActivity(new Intent(mContext, SearchActivity.class), ActivityOptions.makeSceneTransitionAnimation(mContext, view, "search").toBundle());
            } else {
                startActivity(new Intent(mContext, SearchActivity.class));
            }
        }
    };

    private TitleAdapter.OnAdvertisListener onAdvertisListener = new TitleAdapter.OnAdvertisListener() {
        @Override
        public void onAdvertis(View view, String url) {
            Intent intent = new Intent(mContext, AdvertisActivity.class);
            intent.putExtra(AdvertisActivity.KEY_ADVERTIS_URL, url);
            mContext.startDDMActivity(intent, true);
        }

    };

    private RecommendAdapter.OnFollowListener onFollowListener = new RecommendAdapter.OnFollowListener() {

        @Override
        public void onItemClick(View view, int memberID, int position) {
            Intent intent = new Intent(mContext, UserHomeActivity.class);
            intent.putExtra("memberID", memberID);
            mContext.startDDMActivity(intent, true);
        }

    };

    private RecommendAdapter.OnHobbyListener onHobbyListener = new RecommendAdapter.OnHobbyListener() {
        @Override
        public void onHobby(View view, int position) {
            Intent intent = new Intent(mContext, InterestProjectActivity.class);
            intent.putExtra("tagID", hotTagList.get(position).getTagID());
            mContext.startDDMActivity(intent, true);
        }
    };

//    @Override
//    public void setPresenter(TestContract.Presenter presenter) {
//        this.presenter = presenter;
//    }
//
//    @Override
//    public void showLoading() {
//
//    }
//
//    @Override
//    public void closeLoading() {
//
//    }
}
