package com.kcx.acg.views.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kcx.acg.R;
import com.kcx.acg.api.BalanceRechargeACoinApi;
import com.kcx.acg.api.RechargeSettingListApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.bean.CommonBean;
import com.kcx.acg.bean.RechargeSettingListBean;
import com.kcx.acg.bean.UserInfoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.manager.PayManager;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.BottomDialogUtil_Pay;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.utils.ToastUtil;
import com.kcx.acg.views.adapter.ACoinAdapter;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.FullyGridLayoutManager;
import com.kcx.acg.views.view.LoadingErrorView;

import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * A币充值
 * Created by jb on 2018/9/6.
 */
public class ToUpACoinActivity extends BaseActivity implements CancelAdapt {
    private TextView tv_title;
    private Button btn_toUpAbi;
    private RecyclerView mRecyclerView;
    private ImageView iv_chongzhi_bg;
    private LinearLayout ll_back;
    private LoadingErrorView mLoadingErrorView;

    private UserInfoBean userInfoBean;
    private ACoinAdapter aCoinAdapter;
    private List<RechargeSettingListBean.ReturnDataBean.ListBean> list;
    private int rechargeSettingID;
    private String money;
    private RequestOptions options;
    private boolean isFirstRechargeActivity;

    @Override
    protected void onResume() {
        super.onResume();
        userInfoBean = AppUtil.getGson().fromJson(SPUtil.getString(ToUpACoinActivity.this, Constants.USER_INFO, ""), UserInfoBean.class);

    }

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_toup_abi, null);
    }

    @Override
    public void initView() {
        super.initView();
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.account_title2);
        ll_back = findViewById(R.id.ll_back);
        btn_toUpAbi = findViewById(R.id.btn_toUpAbi);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        iv_chongzhi_bg = findViewById(R.id.iv_chongzhi_bg);
        mLoadingErrorView = findViewById(R.id.loadingErrorView);

        options = new RequestOptions()
                .placeholder(R.drawable.img_holder)  //预加载图片
                .error(R.drawable.img_holder);
    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);
        btn_toUpAbi.setOnClickListener(this);
        iv_chongzhi_bg.setOnClickListener(this);
        //重新加载
        mLoadingErrorView.setOnReloadListener(new LoadingErrorView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                getRechargeSettingList();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        initRecyclerView(list);
        getRechargeSettingList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.btn_toUpAbi:  //A币充值
                new BottomDialogUtil_Pay(ToUpACoinActivity.this, false,R.layout.dialog_bottom_vip, userInfoBean, money, new BottomDialogUtil_Pay.BottonDialogListener() {
                    @Override
                    public void onItemListener(int i) {
                        switch (i) {
                            case 0:  //支付宝
                                PayManager.getInstances().goPay(ToUpACoinActivity.this, 2, rechargeSettingID);
                                break;
                            case 1:  //微信
                                ToastUtil.showShort(ToUpACoinActivity.this, "微信");
                                break;
                            case 2:   //信用卡
                                Intent intent = new Intent(ToUpACoinActivity.this, CreditCardPayActivity.class);
                                startActivity(intent);
                                break;
                            case 3:  //余额
                                balanceRechargeACoin(rechargeSettingID);
                                break;
                        }
                    }
                });
                break;


        }
    }


    /***
     * 初始列表
     */
    private void initRecyclerView(List<RechargeSettingListBean.ReturnDataBean.ListBean> listData) {
        FullyGridLayoutManager mgr = new FullyGridLayoutManager(ToUpACoinActivity.this, 2);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mgr);
        aCoinAdapter = new ACoinAdapter(ToUpACoinActivity.this, listData);
        // 设置adapter
        mRecyclerView.setAdapter(aCoinAdapter);

        aCoinAdapter.setOnItemClickListener(new ACoinAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                money = list.get(position).getMoney();
                rechargeSettingID = list.get(position).getRechargeSettingID();
                for (int i = 0; i < list.size(); i++) {
                    if (i == position) {
                        list.get(i).setDefault(true);
                    } else {
                        list.get(i).setDefault(false);
                    }
                }
                aCoinAdapter.upData(list, isFirstRechargeActivity);
            }
        });
    }


    /**
     * 获取配置信息接口
     */
    private void getRechargeSettingList() {
        RechargeSettingListApi rechargeSettingListApi = new RechargeSettingListApi(this, new HttpOnNextListener<RechargeSettingListBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(RechargeSettingListBean rechargeSettingListBean) {
                mLoadingErrorView.setVisibility(View.GONE);
                if (rechargeSettingListBean.getErrorCode() == 200) {
                    list = rechargeSettingListBean.getReturnData().getList();
                    //如果推荐，设置默认
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isIsRecommend()) {
                            rechargeSettingID = list.get(i).getRechargeSettingID();
                            money = list.get(i).getMoney();
                            list.get(i).setDefault(true);
                        }
                    }
                    isFirstRechargeActivity = rechargeSettingListBean.getReturnData().isIsFirstRechargeActivity();
                    aCoinAdapter.upData(list, isFirstRechargeActivity);
                    if (isFirstRechargeActivity) {
                        //有活动
                        iv_chongzhi_bg.setVisibility(View.VISIBLE);
                        Glide.with(ToUpACoinActivity.this).load(rechargeSettingListBean.getReturnData().getFirstRechargeIco()).apply(options).into(iv_chongzhi_bg);
                    } else {
                        iv_chongzhi_bg.setVisibility(View.GONE);
                    }
                } else {
                    CustomToast.showToast(rechargeSettingListBean.getErrorMsg());
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
        HttpManager.getInstance().doHttpDeal(ToUpACoinActivity.this, rechargeSettingListApi);
    }

    /**
     * 充值A币接口
     *
     * @param rechargeSettingID
     */
    private void balanceRechargeACoin(int rechargeSettingID) {
        BalanceRechargeACoinApi balanceRechargeACoinApi = new BalanceRechargeACoinApi(this, new HttpOnNextListener<CommonBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(CommonBean commonBean) {
                Intent intent = new Intent(ToUpACoinActivity.this, RechargeStatusActivity.class);
                if (commonBean.getErrorCode() == 200) {
                    intent.putExtra("isSuccess", true);
                } else {
                    intent.putExtra("isSuccess", false);
                }
                intent.putExtra("msg", commonBean.getErrorMsg());
                startActivity(intent);

                return null;
            }
        });
        balanceRechargeACoinApi.setRechargeSettingID(rechargeSettingID);
        HttpManager.getInstance().doHttpDeal(ToUpACoinActivity.this, balanceRechargeACoinApi);
    }
}
