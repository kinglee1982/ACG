package com.kcx.acg.views.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kcx.acg.R;
import com.kcx.acg.api.GetAdvertisementInfoApi;
import com.kcx.acg.api.UsersDynamicListApi;
import com.kcx.acg.api.UsersTopFavouriteTagListApi;
import com.kcx.acg.base.BusEvent;
import com.kcx.acg.bean.GetAdvertisementInfoBean;
import com.kcx.acg.bean.UsersDynamicListBean;
import com.kcx.acg.bean.UsersFavouriteTagListBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.manager.WorkManager;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.views.activity.InterestProjectActivity;
import com.kcx.acg.views.activity.LoginActivity;
import com.kcx.acg.views.activity.MainActivity;
import com.kcx.acg.views.activity.MyInterestActivity;
import com.kcx.acg.views.activity.WebViewActivity;
import com.kcx.acg.views.adapter.DynamicAdapter;
import com.kcx.acg.views.adapter.MyInterestAdapter;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.FullyLinearLayoutManager;
import com.kcx.acg.views.view.LoadingErrorView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 关注
 * Created by jb on 2018/8/24.
 */

public class WatchFragment extends LazyFragment<MainActivity> implements CancelAdapt {
    private LoadingErrorView mLoadingErrorView;
    //空数据
    private LinearLayout ll_empty_attention;
    //未登录
    private RelativeLayout rl_unLogin;
    private Button btn_login;
    //已登录
    private LinearLayout ll_login;
    private RelativeLayout rl_myInterest;
    private RecyclerView rv_myInterest, rv_dynamic;
    private LinearLayout ll_Interest, ll_unInterest;//是否有关注数据
    private LinearLayout ll_empty_dynamic;//没有动态
    private SmartRefreshLayout mRefreshLayout;
    private Button btn_dynamic, btn_dynamic1;

    private String accessToken, targetUrl;
    private MyInterestAdapter myInterestAdapter;
    private DynamicAdapter dynamicAdapter;
    private List<UsersFavouriteTagListBean.ReturnDataBean> usersFavouriteTagList = null;
    private List<UsersDynamicListBean.ReturnDataBean.ListBean> usersDynamicList = null;
    private UsersDynamicListBean.ReturnDataBean.ListBean.AuthorBean authorBean;
    private UsersDynamicListBean.ReturnDataBean.ListBean advertisingBean;
    private int topCount = 5;
    private int pageIndex = 1;
    private int pageSize = 20;
    private boolean isUsersTopFavouriteTag = false;


    @Override
    public void onResume() {
        super.onResume();
        //        ((MainActivity)getActivity()).changeFindStatusBar();
        accessToken = SPUtil.getString(getActivity(), Constants.ACCESS_TOKEN, "");
        if (TextUtils.isEmpty(accessToken)) {
            rl_unLogin.setVisibility(View.VISIBLE);
            ll_login.setVisibility(View.GONE);
        } else {
            rl_unLogin.setVisibility(View.GONE);
            ll_login.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.watch_fragment_layout, container, false);
    }

    @Override
    protected void loadData() {
        if (!TextUtils.isEmpty(accessToken))
            mRefreshLayout.autoRefresh();
    }

    @Override
    public void onEventMainThread(BusEvent event) {
        super.onEventMainThread(event);
        if (event.getType() == BusEvent.LOGIN_SUCCESS || event.getType() == BusEvent.INTEREST_ATTENTION || event.getType() == BusEvent.ATTENTION_MEMBER) {
            mRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void initUI(View view) {
        super.initUI(view);
        mLoadingErrorView = view.findViewById(R.id.loadingErrorView);
        rl_unLogin = view.findViewById(R.id.rl_unLogin);
        ll_login = view.findViewById(R.id.ll_login);
        ll_empty_attention = view.findViewById(R.id.ll_empty_attention);
        btn_login = view.findViewById(R.id.btn_login);
        rl_myInterest = view.findViewById(R.id.rl_myInterest);
        rv_myInterest = view.findViewById(R.id.rv_myInterest);
        rv_dynamic = view.findViewById(R.id.rv_dynamic);
        ll_Interest = view.findViewById(R.id.ll_Interest);
        ll_unInterest = view.findViewById(R.id.ll_unInterest);
        ll_empty_dynamic = view.findViewById(R.id.ll_empty_dynamic);
        btn_dynamic = view.findViewById(R.id.btn_dynamic);
        btn_dynamic1 = view.findViewById(R.id.btn_dynamic1);
        mRefreshLayout = view.findViewById(R.id.mRefreshLayout);
        mRefreshLayout.setEnableLoadMoreWhenContentNotFull(false);

        usersDynamicList = new ArrayList<UsersDynamicListBean.ReturnDataBean.ListBean>();
        initMyInterest(usersFavouriteTagList);
        initDynamic(usersDynamicList);

    }


    @Override
    public void initListener() {
        super.initListener();
        btn_login.setOnClickListener(this);
        btn_dynamic.setOnClickListener(this);
        btn_dynamic1.setOnClickListener(this);
        ll_Interest.setOnClickListener(this);
        ll_unInterest.setOnClickListener(this);

        //加载更多
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex = pageIndex + 1;
                getUsersDynamicList(pageIndex, pageSize);
            }
        });

        //下拉刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (usersFavouriteTagList != null) {
                    usersFavouriteTagList.clear();
                }
                if (usersDynamicList != null) {
                    usersDynamicList.clear();
                }

                getUsersTopFavouriteTagList(topCount);
                refreshLayout.finishRefresh();
            }
        });

        mLoadingErrorView.setOnReloadListener(new LoadingErrorView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                if (usersFavouriteTagList != null)
                    usersFavouriteTagList.clear();
                if (usersDynamicList != null)
                    usersDynamicList.clear();

                getUsersTopFavouriteTagList(topCount);
            }
        });

    }


    @Override
    public void initData() {

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_login:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.btn_dynamic: //去逛逛（空数据    ）
                ((FindFragment) (WatchFragment.this.getParentFragment())).changeFragment(0);
                break;

            case R.id.btn_dynamic1: //去逛逛(无动态)
                ((FindFragment) (WatchFragment.this.getParentFragment())).changeFragment(0);
                break;

            case R.id.ll_Interest:  //我的兴趣  全部
                startActivity(new Intent(getActivity(), MyInterestActivity.class));
                break;

            case R.id.ll_unInterest:  //添加关注
                ((FindFragment) (WatchFragment.this.getParentFragment())).changeFragment(0);
                break;
        }
    }

    //初始化我的兴趣
    private void initMyInterest(List<UsersFavouriteTagListBean.ReturnDataBean> listData) {
        GridLayoutManager fullyGridLayoutManager = new GridLayoutManager(getActivity(), 5);
        rv_myInterest.setLayoutManager(fullyGridLayoutManager);
        myInterestAdapter = new MyInterestAdapter(getActivity(), listData);
        rv_myInterest.setAdapter(myInterestAdapter);

        myInterestAdapter.setOnItemClickListener(new MyInterestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), InterestProjectActivity.class);
                intent.putExtra("tagID", usersFavouriteTagList.get(position).getTagID());
                startActivity(intent);

            }
        });

    }

    //初始化动态列表
    private void initDynamic(final List<UsersDynamicListBean.ReturnDataBean.ListBean> usersDynamicList) {
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(getActivity());
        rv_dynamic.setLayoutManager(linearLayoutManager);
        dynamicAdapter = new DynamicAdapter(getActivity(), usersDynamicList);
        rv_dynamic.setAdapter(dynamicAdapter);
        dynamicAdapter.setOnItemClickListener(new DynamicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position == 4) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("url", targetUrl);
                    startActivity(intent);
                } else {
                    WorkManager.getInstances().goToWhere(mContext, usersDynamicList.get(position).getCanViewType(), usersDynamicList.get(position).getProductID());
                }
            }
        });
    }

    /**
     * 用户兴趣标签Top列表接口
     *
     * @param i
     */
    private void getUsersTopFavouriteTagList(int i) {
        UsersTopFavouriteTagListApi usersTopFavouriteTagListApi = new UsersTopFavouriteTagListApi((RxAppCompatActivity) getActivity(), new HttpOnNextListener<UsersFavouriteTagListBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(UsersFavouriteTagListBean usersFavouriteTagListBean) {
                ll_login.setVisibility(View.VISIBLE);
                mLoadingErrorView.setVisibility(View.GONE);
                if (usersFavouriteTagListBean.getErrorCode() == 402) {
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else if (usersFavouriteTagListBean.getErrorCode() == 200) {
                    getUsersDynamicList(1, pageSize);
                    usersFavouriteTagList = usersFavouriteTagListBean.getReturnData();
                    myInterestAdapter.updata(usersFavouriteTagList);
                    if (usersFavouriteTagList.size() == 0 || usersFavouriteTagList == null) {
                        isUsersTopFavouriteTag = false;
                        ll_unInterest.setVisibility(View.VISIBLE);
                        ll_Interest.setVisibility(View.GONE);
                        rv_myInterest.setVisibility(View.VISIBLE);
                        return null;
                    } else {
                        isUsersTopFavouriteTag = true;
                    }

                    if (usersFavouriteTagList.size() >= 5) {
                        ll_unInterest.setVisibility(View.GONE);
                        ll_Interest.setVisibility(View.VISIBLE);
                        rv_myInterest.setVisibility(View.VISIBLE);

                    } else if (usersFavouriteTagList.size() > 0 && usersFavouriteTagList.size() < 5) {
                        ll_unInterest.setVisibility(View.VISIBLE);
                        ll_Interest.setVisibility(View.GONE);
                        rv_myInterest.setVisibility(View.VISIBLE);
                    } else {
                        ll_Interest.setVisibility(View.GONE);
                        ll_unInterest.setVisibility(View.VISIBLE);
                        rv_myInterest.setVisibility(View.GONE);
                    }

                } else {
                    isUsersTopFavouriteTag = false;
                    CustomToast.showToast(usersFavouriteTagListBean.getErrorMsg());
                }
                return null;
            }

            @Override
            public RetryWhenNetworkException.Wrapper onError(Throwable e) {
                super.onError(e);
                mLoadingErrorView.setVisibility(View.VISIBLE);
                mLoadingErrorView.onError(e);

                ll_login.setVisibility(View.GONE);
                ll_empty_attention.setVisibility(View.GONE);
                ll_empty_dynamic.setVisibility(View.GONE);
                return null;
            }
        });
        usersTopFavouriteTagListApi.setTopCount(i);
        HttpManager.getInstance().doHttpDeal(getActivity(), usersTopFavouriteTagListApi);

    }

    /**
     * 获取动态列表接口
     *
     * @param pageIndex
     * @param pageSize
     */
    private void getUsersDynamicList(final int pageIndex, int pageSize) {
        UsersDynamicListApi usersDynamicListApi = new UsersDynamicListApi((RxAppCompatActivity) getActivity(), new HttpOnNextListener<UsersDynamicListBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(UsersDynamicListBean usersDynamicListBean) {
                mLoadingErrorView.setVisibility(View.GONE);
                ll_login.setVisibility(View.VISIBLE);

                if (usersDynamicListBean.getErrorCode() == 200) {
                    if (pageIndex == 1) {
                        GetAdvertisementInfo();
                    }

                    usersDynamicList.addAll(usersDynamicListBean.getReturnData().getList());
                    LogUtil.e("usersDynamicList.size()", usersDynamicList.size() + "///");
                    verifyEmpty();
                    if (usersDynamicList.size() != 0) {
                        ll_empty_dynamic.setVisibility(View.GONE);
                        rv_dynamic.setVisibility(View.VISIBLE);
                        dynamicAdapter.updata(usersDynamicList);

                        if (usersDynamicList.size() >= usersDynamicListBean.getReturnData().getTotal()) {
                            mRefreshLayout.finishLoadMoreWithNoMoreData();
                        } else {
                            mRefreshLayout.finishLoadMore();
                        }
                    } else {  //没有动态数据
                        ll_empty_dynamic.setVisibility(View.VISIBLE);
                        rv_dynamic.setVisibility(View.GONE);
                    }
                } else {
                    CustomToast.showToast(usersDynamicListBean.getErrorMsg());
                }
                return null;
            }

            @Override
            public RetryWhenNetworkException.Wrapper onError(Throwable e) {
                super.onError(e);
                mLoadingErrorView.setVisibility(View.VISIBLE);
                mLoadingErrorView.onError(e);

                ll_login.setVisibility(View.GONE);
                ll_empty_attention.setVisibility(View.GONE);
                ll_empty_dynamic.setVisibility(View.GONE);
                return null;
            }
        });
        usersDynamicListApi.setPageIndex(pageIndex);
        usersDynamicListApi.setPageSize(pageSize);
        HttpManager.getInstance().doHttpDeal(getActivity(), usersDynamicListApi);
    }


    /**
     * 广告位ID 1 - 作品预览页：评论区上方 ； 2 - 作品观看页：评论区上方； 3 - 关注：动态列表中； 4 - 发现：底部； 5 - 兴趣专栏：作品列表中； 6 - 抽奖：底部； 7 - 视频播放完成页；
     */
    private void GetAdvertisementInfo() {
        GetAdvertisementInfoApi getAdvertisementInfoApi = new GetAdvertisementInfoApi((RxAppCompatActivity) getActivity(), new HttpOnNextListener<GetAdvertisementInfoBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(GetAdvertisementInfoBean getAdvertisementInfoBean) {
                if (getAdvertisementInfoBean.getReturnData() != null) {
                    targetUrl = getAdvertisementInfoBean.getReturnData().getTargetUrl();
                    if (usersDynamicList.size() == 4) {
                        usersDynamicList.add(spellItemBean(getAdvertisementInfoBean));
                        dynamicAdapter.updata(usersDynamicList);
                    } else if (usersDynamicList.size() > 4) {
                        usersDynamicList.set(4, spellItemBean(getAdvertisementInfoBean));
                        dynamicAdapter.updata(usersDynamicList);
                    }
                }
                return null;
            }
        });
        getAdvertisementInfoApi.setLocationID(3);
        HttpManager.getInstance().doHttpDeal(getActivity(), getAdvertisementInfoApi);
    }


    /**
     * 广告bean封装
     *
     * @param getAdvertisementInfoBean
     * @return
     */
    private UsersDynamicListBean.ReturnDataBean.ListBean spellItemBean(GetAdvertisementInfoBean getAdvertisementInfoBean) {
        authorBean = new UsersDynamicListBean.ReturnDataBean.ListBean.AuthorBean();
        UsersDynamicListBean.ReturnDataBean.ListBean advertisingBean = new UsersDynamicListBean.ReturnDataBean.ListBean();
        authorBean.setUserName(getAdvertisementInfoBean.getReturnData().getLocationName());
        authorBean.setPhoto(getAdvertisementInfoBean.getReturnData().getAdvertiserLogoUrl());
        advertisingBean.setTitle(getAdvertisementInfoBean.getReturnData().getAdvertiserDescription());
        advertisingBean.setCreateTime(getAdvertisementInfoBean.getReturnData().getDescription());
        advertisingBean.setCoverPicUrl(getAdvertisementInfoBean.getReturnData().getImageUrl());
        advertisingBean.setAuthor(authorBean);
        return advertisingBean;
    }

    //是否空数据
    private void verifyEmpty() {
        if (isUsersTopFavouriteTag == false && usersDynamicList.size() == 0) {
            ll_login.setVisibility(View.GONE);
            ll_empty_dynamic.setVisibility(View.GONE);
            ll_empty_attention.setVisibility(View.VISIBLE);
            return;
        } else {
            ll_empty_attention.setVisibility(View.GONE);

        }
    }

}
