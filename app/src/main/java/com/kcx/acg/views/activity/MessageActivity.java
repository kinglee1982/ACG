package com.kcx.acg.views.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.kcx.acg.R;
import com.kcx.acg.api.GetReadNoticeApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.bean.MessageBean;
import com.kcx.acg.bean.ReadNoticeBean;
import com.kcx.acg.bean.UserInfoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.utils.ToastUtil;
import com.kcx.acg.views.adapter.MessageAdapter;
import com.kcx.acg.views.view.LoadingErrorView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 消息通知界面
 * Created by jb on 2018/9/3.
 */
public class MessageActivity extends BaseActivity implements CancelAdapt {
    private TextView tv_title;
    private RecyclerView mRecyclerView;
    private LinearLayout ll_back;
    private LinearLayoutManager mLayoutManager;
    private MessageAdapter messageAdapter;
    private List<ReadNoticeBean.ReturnDataBean.ListBean> list;
    private SmartRefreshLayout mRefreshLayout;
    private LoadingErrorView mLoadingErrorView;

    private int isRead;
    private int pageIndex = 1;
    private int pageSize = 10;

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_message, null);
    }

    @Override
    public void initView() {
        super.initView();
        ll_back = findViewById(R.id.ll_back);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.message_title);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRefreshLayout = findViewById(R.id.mRefreshLayout);
        mRefreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        mLoadingErrorView = findViewById(R.id.loadingErrorView);
        list = Lists.newArrayList();
        initRecyclerView(list);
    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);


        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageIndex=1;
                list.clear();
                getReadNoticeApi(1, pageIndex, pageSize);
                mRefreshLayout.finishRefresh();
            }
        });

        //加载更多
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex = pageIndex+1;
                getReadNoticeApi(2, pageIndex, pageSize);
                refreshLayout.finishLoadMore();
            }
        });

        mLoadingErrorView.setOnReloadListener(new LoadingErrorView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                getReadNoticeApi(2, 1, pageSize);
            }
        });


    }

    @Override
    public void initData() {
        super.initData();
        getReadNoticeApi(2, pageIndex, pageSize);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }

    private void initRecyclerView(final List<ReadNoticeBean.ReturnDataBean.ListBean> list) {
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        messageAdapter = new MessageAdapter(MessageActivity.this,list);
        // 设置adapter
        mRecyclerView.setAdapter(messageAdapter);

        //        messageAdapter.setOnItemClickListener(new MessageAdapter.OnItemClickListener() {
        //            @Override
        //            public void onItemClick(View view, int position) {
        //                ToastUtil.showShort(MessageActivity.this, position + "");
        //            }
        //        });
    }

    /**
     * @param isRead
     * @param pageIndex
     * @param pageSize
     */
    private void getReadNoticeApi(int isRead, int pageIndex, int pageSize) {

        GetReadNoticeApi getReadNoticeApi = new GetReadNoticeApi(this, new HttpOnNextListener<ReadNoticeBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(ReadNoticeBean readNoticeBean) {
                if (readNoticeBean.getErrorCode() == 200) {
                    list.addAll(readNoticeBean.getReturnData().getList());
                    messageAdapter.update(list);
                    if(list.size() >= readNoticeBean.getReturnData().getTotal()){
                        mRefreshLayout.finishLoadMoreWithNoMoreData();
                    }else {
                        mRefreshLayout.finishLoadMore();
                    }
                }
                return null;
            }
        });
        getReadNoticeApi.setIsRead(isRead);
        getReadNoticeApi.setPageIndex(pageIndex);
        getReadNoticeApi.setPageSize(pageSize);
        HttpManager.getInstance().doHttpDeal(this, getReadNoticeApi);
    }

}
