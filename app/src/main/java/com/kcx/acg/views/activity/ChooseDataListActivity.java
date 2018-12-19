package com.kcx.acg.views.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.api.InternationalAreaInfoListApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.bean.InternationalAreaInfoListBean;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.views.adapter.ChooseDataListAdapter;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.LoadingErrorView;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 选择数据列表
 * Created by jb on 2018/9/12.
 */
public class ChooseDataListActivity extends BaseActivity implements CancelAdapt {
    private static final int MSG_CODE = 0;
    private TextView tv_title;
    private RecyclerView mRecyclerView;
    private LinearLayout ll_select,ll_back;
    private ImageView iv_select;
    private EditText et_select;
    private ImageButton ib_clean;
    private LoadingErrorView mLoadingErrorView;

    private LinearLayoutManager mLayoutManager;
    private ChooseDataListAdapter chooseDataListAdapter;
    private List<InternationalAreaInfoListBean.ReturnDataBean> list;
    private List<InternationalAreaInfoListBean.ReturnDataBean> tempList;
    private String areaCode;

    @SuppressLint("HandlerLeak")
    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CODE:
                    tempList.clear();
                    String content = (String) msg.obj;
                    for (int i = 0; i < list.size(); i++) {
                        String areaName = list.get(i).getAreaName();
                        if (list.get(i).getAreaName().contains(content)) {
                            tempList.add(list.get(i));
                        }
                    }
                    chooseDataListAdapter.update(tempList, areaCode);
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_choose_country, null);
    }

    @Override
    public void initView() {
        super.initView();
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.chooseDataList_title);
        ll_back = findViewById(R.id.ll_back);
        mRecyclerView = findViewById(R.id.mRecyclerView);

        ll_select = findViewById(R.id.ll_select);
        iv_select = findViewById(R.id.iv_select);
        et_select = findViewById(R.id.et_select);
        ib_clean = findViewById(R.id.ib_clean);
        mLoadingErrorView =findViewById(R.id.loadingErrorView);

    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);
        ll_select.setOnClickListener(this);
        ib_clean.setOnClickListener(this);

        et_select.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    ib_clean.setVisibility(View.GONE);
                    chooseDataListAdapter.update(list, areaCode);

                } else {
                    ib_clean.setVisibility(View.VISIBLE);
                    Message msg = new Message();
                    msg.what = MSG_CODE;
                    msg.obj = s.toString();
                    myHandler.sendMessageDelayed(msg, 0);

                }

            }
        });

        mLoadingErrorView.setOnReloadListener(new LoadingErrorView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                list.clear();
                getInternationalAreaInfoList();
            }
        });


    }

    @Override
    public void initData() {
        super.initData();
        areaCode = getIntent().getStringExtra("areaCode");
        list = new ArrayList<InternationalAreaInfoListBean.ReturnDataBean>();
        tempList = new ArrayList<InternationalAreaInfoListBean.ReturnDataBean>();
        initRecyclerView(list);
        getInternationalAreaInfoList();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ib_clean:
                et_select.setText("");
                break;
            case R.id.ll_select:
                ll_select.setVisibility(View.GONE);
                iv_select.setVisibility(View.VISIBLE);
                et_select.setVisibility(View.VISIBLE);
                AppUtil.showSoftInputFromWindow(et_select);
                break;
        }
    }

    private void initRecyclerView(final List<InternationalAreaInfoListBean.ReturnDataBean> dataList) {
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        chooseDataListAdapter = new ChooseDataListAdapter(ChooseDataListActivity.this, dataList, areaCode);
        // 设置adapter
        mRecyclerView.setAdapter(chooseDataListAdapter);

        chooseDataListAdapter.setOnItemClickListener(new ChooseDataListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String areaCode) {
                Intent intent = new Intent();
                intent.putExtra("areaCode", areaCode);
                setResult(10086, intent);
                finish();
            }
        });
    }

    //获取所有国际地区信息列表
    private void getInternationalAreaInfoList() {

        InternationalAreaInfoListApi internationalAreaInfoListApi = new InternationalAreaInfoListApi(this, new HttpOnNextListener<InternationalAreaInfoListBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(InternationalAreaInfoListBean internationalAreaInfoListBean) {
                mLoadingErrorView.setVisibility(View.GONE);
                if (internationalAreaInfoListBean.getErrorCode() == 200) {
                    list = internationalAreaInfoListBean.getReturnData();
                    chooseDataListAdapter.update(list, areaCode);
                } else {
                    CustomToast.showToast( internationalAreaInfoListBean.getErrorMsg());
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

        HttpManager.getInstance().doHttpDeal(ChooseDataListActivity.this, internationalAreaInfoListApi);

    }
}