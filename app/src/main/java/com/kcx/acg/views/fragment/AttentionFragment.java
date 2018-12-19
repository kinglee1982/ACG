package com.kcx.acg.views.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.api.UserAttentionListApi;
import com.kcx.acg.base.BaseFragment;
import com.kcx.acg.base.BusEvent;
import com.kcx.acg.bean.UserAttentionListBean;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.views.activity.AttentionAndFansActivity;
import com.kcx.acg.views.activity.UserHomeActivity;
import com.kcx.acg.views.adapter.AttentionAdapter;
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
 * Created by jb on 2018/9/5.
 */
public class AttentionFragment extends BaseFragment<AttentionAndFansActivity> implements CancelAdapt {
    private LoadingErrorView mLoadingErrorView;
    private LinearLayout ll_empty;
    private Button btn_empty;

    private TextView tv_empty_hint;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;
    private AttentionAdapter attentionAdapter;
    private int pageIndex = 1;
    private int pageSize = 30;
    private List<UserAttentionListBean.ReturnDataBean.ListBean> list;


    public static AttentionFragment newInstance(String content) {
        AttentionFragment fragment = new AttentionFragment();
        return fragment;
    }

    @Override
    public void onEventMainThread(BusEvent event) {
        super.onEventMainThread(event);
        if (event.getType() == BusEvent.ATTENTION_MEMBER) {
                        list.clear();
                        getUserAttentionList(1, pageSize);
                    }
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_attention, container, false);
    }

    @Override
    public void initUI(View view) {
        super.initUI(view);
        //空数据
        ll_empty = view.findViewById(R.id.ll_empty);
        tv_empty_hint = view.findViewById(R.id.tv_empty_hint);
        tv_empty_hint.setText(R.string.empty_attention_hint);
        btn_empty = view.findViewById(R.id.btn_empty);

        mLoadingErrorView = view.findViewById(R.id.loadingErrorView);
        mRecyclerView = view.findViewById(R.id.rv);
        mRefreshLayout = view.findViewById(R.id.mRefreshLayout);
        mRefreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        initRecyclerView(list);

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                list.clear();
                getUserAttentionList(1, pageSize);
                mRefreshLayout.finishRefresh();
            }
        });

        //加载更多
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex = pageIndex+1;
                getUserAttentionList(pageIndex, pageSize);
                refreshLayout.finishLoadMore();
            }
        });

        //去逛逛
        btn_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setResult(10010);
                getActivity().finish();
            }
        });

        mLoadingErrorView.setOnReloadListener(new LoadingErrorView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                getUserAttentionList(1, pageSize);
            }
        });
    }

    @Override
    public void initData() {
        list = new ArrayList<UserAttentionListBean.ReturnDataBean.ListBean>();
        getUserAttentionList(pageIndex, pageSize);
    }

    private void initRecyclerView(final List<UserAttentionListBean.ReturnDataBean.ListBean> dataList) {
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        attentionAdapter = new AttentionAdapter(getActivity(), dataList);
        // 设置adapter
        mRecyclerView.setAdapter(attentionAdapter);

        attentionAdapter.setOnItemClickListener(new AttentionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int memberID) {
                Intent intent = new Intent(getActivity(), UserHomeActivity.class);
                intent.putExtra("memberID", memberID);
                startActivity(intent);
            }
        });
    }

    public void getUserAttentionList(int pageIndex, int pageSize) {

        UserAttentionListApi userAttentionListApi = new UserAttentionListApi((RxAppCompatActivity) getActivity(), new HttpOnNextListener<UserAttentionListBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(UserAttentionListBean userAttentionListBean) {
                mLoadingErrorView.setVisibility(View.GONE);
                if (userAttentionListBean.getErrorCode() == 200) {
                    list.addAll(userAttentionListBean.getReturnData().getList());
                    if (list.size() == 0) {
                        //空数据
                        ll_empty.setVisibility(View.VISIBLE);
                        mRefreshLayout.setVisibility(View.GONE);
                        return null;
                    } else {
                        ll_empty.setVisibility(View.GONE);
                        mRefreshLayout.setVisibility(View.VISIBLE);
                    }

                    attentionAdapter.updata(list);
                    if (list.size() >= userAttentionListBean.getReturnData().getTotal() && list.size() != 0) {
                        mRefreshLayout.finishLoadMoreWithNoMoreData();

                    }
                } else {
                    userAttentionListBean.getErrorMsg();
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
        userAttentionListApi.setPageIndex(pageIndex);
        userAttentionListApi.setPageSize(pageSize);
        HttpManager.getInstance().doHttpDeal(getActivity(), userAttentionListApi);

    }
}
