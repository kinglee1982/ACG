package com.kcx.acg.views.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.api.UsersFavouriteTagListApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.bean.FavouriteTagListBean;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.views.adapter.InterestAdapter;
import com.kcx.acg.views.view.CustomToast;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 我的兴趣
 * Created by jb on 2018/9/25.
 */
public class MyInterestActivity extends BaseActivity implements CancelAdapt {
    private TextView tv_title;
    private RecyclerView mRecyclerView;
    private LinearLayout ll_back;
    private SmartRefreshLayout mRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private InterestAdapter interestAdapter;
    private List<FavouriteTagListBean.ReturnDataBean.ListBean> list;
    private int pageIndex = 1;
    private int pageSize = 10;

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_my_interest, null);
    }

    @Override
    public void initView() {
        super.initView();
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.attention_myInterest);
        ll_back = findViewById(R.id.ll_back);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRefreshLayout = findViewById(R.id.mRefreshLayout);
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadMoreWhenContentNotFull(false);
    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);

        //加载更多
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex = pageIndex+1;
                getUsersFavouriteTagList(pageIndex, pageSize);
                refreshLayout.finishLoadMore(1000);
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        list = new ArrayList<FavouriteTagListBean.ReturnDataBean.ListBean>();
        initRecyclerView(list);

        getUsersFavouriteTagList(pageIndex, pageSize);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }

    private void initRecyclerView(List<FavouriteTagListBean.ReturnDataBean.ListBean> dataList) {
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        interestAdapter = new InterestAdapter(MyInterestActivity.this, dataList);
        // 设置adapter
        mRecyclerView.setAdapter(interestAdapter);

        interestAdapter.setOnItemClickListener(new InterestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position,int tagID) {
                Intent intent = new Intent(MyInterestActivity.this, InterestProjectActivity.class);
                intent.putExtra("tagID",tagID);
                startActivity(intent);
            }


        });
    }

    /**
     * @param pageIndex
     * @param pageSize
     */
    private void getUsersFavouriteTagList(int pageIndex, int pageSize) {
        UsersFavouriteTagListApi usersFavouriteTagListApi = new UsersFavouriteTagListApi(this, new HttpOnNextListener<FavouriteTagListBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(FavouriteTagListBean favouriteTagListBean) {
                if (favouriteTagListBean.getErrorCode() == 200) {
                    list.addAll(favouriteTagListBean.getReturnData().getList());
                    interestAdapter.updata(list);
                    if (list.size() >= favouriteTagListBean.getReturnData().getTotal()) {
                        mRefreshLayout.finishLoadMoreWithNoMoreData();
                    }


                } else {
                    CustomToast.showToast( favouriteTagListBean.getErrorMsg());
                }

                return null;
            }
        });
        usersFavouriteTagListApi.setPageIndex(pageIndex);
        usersFavouriteTagListApi.setPageSize(pageSize);
        HttpManager.getInstance().doHttpDeal(MyInterestActivity.this, usersFavouriteTagListApi);

    }

}
