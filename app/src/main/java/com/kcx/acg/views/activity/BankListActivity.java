package com.kcx.acg.views.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.api.GetBankListApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.bean.BankListBean;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.views.adapter.BankListAdapter;
import com.kcx.acg.views.view.LoadingErrorView;

import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 选择银行列表
 * Created by jb on 2018/9/12.
 */
public class BankListActivity extends BaseActivity implements CancelAdapt {
    private LoadingErrorView mLoadingErrorView;
    private TextView tv_title;
    private LinearLayout ll_back;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private List<BankListBean.ReturnDataBean> bankList = null;
    private BankListAdapter bankListAdapter;
    private String bankStr;

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_bank_list, null);
    }

    @Override
    public void initView() {
        super.initView();
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.income_selectBank2);
        ll_back = findViewById(R.id.ll_back);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mLoadingErrorView = findViewById(R.id.loadingErrorView);

    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);
        mLoadingErrorView.setOnReloadListener(new LoadingErrorView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                getBankList();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        bankStr = getIntent().getStringExtra("bank");
        initRecyclerView(bankList, bankStr);
        getBankList();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }

    private void initRecyclerView(List<BankListBean.ReturnDataBean> dataList, String bankStr) {
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        bankListAdapter = new BankListAdapter(BankListActivity.this, dataList, bankStr);
        // 设置adapter
        mRecyclerView.setAdapter(bankListAdapter);

        bankListAdapter.setOnItemClickListener(new BankListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String bankName) {
                Intent intent = new Intent();
                intent.putExtra("bankName", bankName);
                intent.putExtra("bankID", bankList.get(position).getBankID());
                setResult(10010, intent);
                finish();

            }
        });
    }

    /**
     * 获取所有银行列表
     */
    public void getBankList() {
        GetBankListApi getBankListApi = new GetBankListApi(this, new HttpOnNextListener<BankListBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(BankListBean bankListBean) {
                mLoadingErrorView.setVisibility(View.GONE);
                if (bankListBean.getErrorCode() == 200) {
                    bankList = bankListBean.getReturnData();
                    bankListAdapter.updata(bankList, bankStr);
                }
                return null;
            }

            @Override
            public RetryWhenNetworkException.Wrapper onError(Throwable e) {
                super.onError(e);
                mLoadingErrorView.setVisibility(View.VISIBLE);
                mLoadingErrorView.onError(e);
                return null;
            }
        });
        HttpManager.getInstance().doHttpDeal(BankListActivity.this, getBankListApi);
    }

}
