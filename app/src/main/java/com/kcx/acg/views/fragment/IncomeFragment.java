package com.kcx.acg.views.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.kcx.acg.R;
import com.kcx.acg.api.GetUserIncomeDetailNewApi;
import com.kcx.acg.api.GetUserIncomeSummaryNewApi;
import com.kcx.acg.base.BaseFragment;
import com.kcx.acg.base.BusEvent;
import com.kcx.acg.bean.UserIncomeDetailBean;
import com.kcx.acg.bean.UserIncomeSummaryBean;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.views.activity.IncomeDetailActivity;
import com.kcx.acg.views.adapter.IncomeDetailAdapter;
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
 * Created by zjb on 2018/11/14.
 */
public class IncomeFragment extends BaseFragment implements CancelAdapt {
    private static AppCompatActivity mContext;
    private LoadingErrorView mLoadingErrorView;
    private TextView tv_weekIncome, tv_monthIncome, tv_totalIncome;
    private RecyclerView mRecyclerView;
    private LinearLayout ll_hint;
    private FullyLinearLayoutManager mLayoutManager;
    private IncomeDetailAdapter incomeDetailAdapter;
    private List<UserIncomeDetailBean.ReturnDataBean.ListBean> list;
    private SmartRefreshLayout mRefreshLayout;
    private int pageIndex = 1;
    private int pageSize = 15;
    //1:总收益；2：推广收益；3：作品收益
    private int incomeSummaryType = 1;
    //1:总收益；2：推广收益；3：作品收益; 4:支出
    private int incomeDetailType = 1;


    @Override
    public void onEventMainThread(BusEvent event) {
        super.onEventMainThread(event);
        if (event.getType() == BusEvent.REFRESH_INCOME){
           incomeSummaryType= event.getIntParam();
           incomeDetailType= event.getIntParam();
           mRefreshLayout.autoRefresh();
        }
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_income, container, false);
    }

    @Override
    public void initUI(View view) {
        super.initUI(view);
        mContext = (IncomeDetailActivity) getActivity();
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        ll_hint = view.findViewById(R.id.ll_hint);
        tv_weekIncome = view.findViewById(R.id.tv_weekIncome);
        tv_monthIncome = view.findViewById(R.id.tv_monthIncome);
        tv_totalIncome = view.findViewById(R.id.tv_totalIncome);
        mLoadingErrorView = view.findViewById(R.id.loadingErrorView);

        mRefreshLayout = view.findViewById(R.id.mRefreshLayout);
//        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadMoreWhenContentNotFull(false);

        initRecyclerView(list);
    }

    @Override
    public void initListener() {
        super.initListener();
        //下拉刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageIndex=1;
                if (list != null) {
                    list.clear();
                }
                getUserIncomeSummaryNewApi(incomeSummaryType);
                getUserIncomeDetail(incomeDetailType,1,pageSize);
                refreshLayout.finishRefresh();
            }
        });


        //加载更多
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex = pageIndex + 1;
                getUserIncomeDetail(incomeDetailType, pageIndex, pageSize);
                refreshLayout.finishLoadMore();
            }
        });
        //重新加载
        mLoadingErrorView.setOnReloadListener(new LoadingErrorView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                list.clear();
                getUserIncomeSummaryNewApi(incomeSummaryType);
                getUserIncomeDetail(incomeDetailType, 1, pageSize);
            }
        });
    }

    @Override
    public void initData() {
        list = new ArrayList<UserIncomeDetailBean.ReturnDataBean.ListBean>();

        getUserIncomeSummaryNewApi(incomeSummaryType);
        getUserIncomeDetail(incomeDetailType, 1, pageSize);
    }

    private void refreshView(UserIncomeSummaryBean userIncomeSummaryBean) {
        tv_weekIncome.setText("￥"+userIncomeSummaryBean.getReturnData().getWeekIncome());
        tv_monthIncome.setText("￥"+userIncomeSummaryBean.getReturnData().getMonthIncome());
        tv_totalIncome.setText("￥"+userIncomeSummaryBean.getReturnData().getTotalIncome());
    }

    private void initRecyclerView(List<UserIncomeDetailBean.ReturnDataBean.ListBean> list) {
        mLayoutManager = new FullyLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        incomeDetailAdapter = new IncomeDetailAdapter(getActivity(), list);
        // 设置adapter
        mRecyclerView.setAdapter(incomeDetailAdapter);
        incomeDetailAdapter.setOnItemClickListener(new IncomeDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }

    public void getUserIncomeSummaryNewApi(int type) {
        GetUserIncomeSummaryNewApi getUserIncomeSummaryNewApi = new GetUserIncomeSummaryNewApi((RxAppCompatActivity) getActivity(), new HttpOnNextListener<UserIncomeSummaryBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(UserIncomeSummaryBean userIncomeSummaryBean) {
                if (userIncomeSummaryBean.getErrorCode() == 200) {
                    refreshView(userIncomeSummaryBean);
                }
                return null;
            }
        });
        getUserIncomeSummaryNewApi.setType(type);
        HttpManager.getInstance().doHttpDeal(getActivity(), getUserIncomeSummaryNewApi);

    }

    /**
     * 获取用户收益明细信息接口
     */
    public void getUserIncomeDetail(int incomeDetailType, int pageIndex, int pageSize) {
        GetUserIncomeDetailNewApi userIncomeDetailApi = new GetUserIncomeDetailNewApi((RxAppCompatActivity) getActivity(), new HttpOnNextListener<UserIncomeDetailBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(UserIncomeDetailBean userIncomeDetailBean) {
                if (userIncomeDetailBean.getErrorCode() == 200) {
                    ll_hint.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    if (userIncomeDetailBean.getReturnData() != null) {
                        list.addAll(userIncomeDetailBean.getReturnData().getList());
                        LogUtil.e("收益明细列表list", list.size() + "///");
                        incomeDetailAdapter.upData(list);
                        if (list.size() >= userIncomeDetailBean.getReturnData().getTotal()) {
                            mRefreshLayout.finishLoadMoreWithNoMoreData();

                        } else if (list.size() == 0) {
                            ll_hint.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                        }
                    } else {
                        ll_hint.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                    }


                } else {
                    CustomToast.showToast(userIncomeDetailBean.getErrorMsg());
                }
                return null;
            }
        });
        userIncomeDetailApi.setType(incomeDetailType);
        userIncomeDetailApi.setPageIndex(pageIndex);
        userIncomeDetailApi.setPageSize(pageSize);
        HttpManager.getInstance().doHttpDeal(getActivity(), userIncomeDetailApi);
    }


    /**
     * 刷新状态
     * @param type
     */
    public static void refreshStatu(int type){
//        getUserIncomeSummaryNewApi(type);
//        getUserIncomeDetail(type, 1, pageSize);
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public static void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        mContext.getWindow().setAttributes(lp);
    }


}
