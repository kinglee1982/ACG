package com.kcx.acg.views.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kcx.acg.R;
import com.kcx.acg.api.BalanceRechargeVipApi;
import com.kcx.acg.api.GetMyCouponListApi;
import com.kcx.acg.api.VipSettingListApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.bean.GetMyCouponListBean;
import com.kcx.acg.bean.CommonBean;
import com.kcx.acg.bean.GetProductListBean;
import com.kcx.acg.bean.UserInfoBean;
import com.kcx.acg.bean.VIPSettingListBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.manager.PayManager;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.BottomDialogUtil_Pay;
import com.kcx.acg.utils.DateUtil;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.utils.ToastUtil;
import com.kcx.acg.views.adapter.VipAdapter;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.FullyGridLayoutManager;
import com.kcx.acg.views.view.LoadingErrorView;
import com.tmall.wireless.tangram.TangramBuilder;
import com.tmall.wireless.tangram.TangramEngine;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * vip界面
 * Created by jb on 2018/9/6.
 */
public class VipActivity extends BaseActivity implements CancelAdapt {

    private static final int USE_TYPE_UNUSE = 0;
    private static final int USE_TYPE_USED = 1;
    private static final int USE_TYPE_ALLS = 2;
    private static final int PAGE_SIZE = 10;

    private LoadingErrorView mLoadingErrorView;
    private LinearLayout ll_content;
    private TextView tv_vip;
    private TangramBuilder.InnerBuilder innerBuilder;
    private TangramEngine engine;
    public VirtualLayoutManager layoutManager;
    private RelativeLayout rl_back;
    private Button btn_openVip;
    private RecyclerView mRecyclerView;
    private ImageView iv_touxiang, iv_v;

    private List<VIPSettingListBean.ReturnDataBean.ListBean> list;
    private VipAdapter vipAdapter;
    private UserInfoBean userInfoBean;
    private int userLevel;
    private int packageID;
    private String money;
    private String onMonthMoney;
    private RequestOptions options;
    private boolean isFinishActivity;

    //优惠券
    private CheckedTextView couponCtv;
    private ImageView sekuaiIv;
    private TextView currencyTv;
    private TextView priceTv;
    private TextView titleTv;
    private TextView timeTv;
    private LinearLayout couponLayout;
    private int currentPage = 1;
    private GetMyCouponListBean.ReturnDataBean.ListBean couponBean;

    @Override
    protected void onResume() {
        super.onResume();
        userInfoBean = AppUtil.getGson().fromJson(SPUtil.getString(VipActivity.this, Constants.USER_INFO, ""), UserInfoBean.class);
        refreshView(userInfoBean);
        getMyCouponList(USE_TYPE_UNUSE, currentPage, PAGE_SIZE);
    }

    //    @Override
    //    public void setTatusBar() {
    //        super.setTatusBar();
    //        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    //    }

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_vip, null);
    }

    @Override
    public void initView() {
        super.initView();
        isFinishActivity = getIntent().getBooleanExtra(Constants.KEY_IS_FINISH_CUR_ACTIVITY, false);
        tv_vip = findViewById(R.id.tv_vip);
        rl_back = findViewById(R.id.rl_back);
        iv_touxiang = findViewById(R.id.iv_touxiang);
        btn_openVip = findViewById(R.id.btn_openVip);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mLoadingErrorView = findViewById(R.id.loadingErrorView);
        iv_v = findViewById(R.id.iv_v);
        ll_content = findViewById(R.id.ll_content);
        sekuaiIv = findViewById(R.id.vip_coupon_sekuai_iv);
        currencyTv = findViewById(R.id.vip_coupon_currency_iv);
        priceTv = findViewById(R.id.vip_coupon_price_tv);
        titleTv = findViewById(R.id.vip_coupon_title_tv);
        timeTv = findViewById(R.id.vip_coupon_time_tv);
        couponLayout = findViewById(R.id.coupon_layout);

        couponCtv = findViewById(R.id.vip_coupon_ctv);
        couponCtv.setCheckMarkDrawable(R.drawable.selector_coupon);

        options = new RequestOptions()
                .placeholder(R.mipmap.placehold_head)  //预加载图片
                .error(R.mipmap.placehold_head);
    }

    @Override
    public void setListener() {
        super.setListener();
        rl_back.setOnClickListener(this);
        btn_openVip.setOnClickListener(this);
        mLoadingErrorView.setOnReloadListener(new LoadingErrorView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                GetVIPSettingList();
                getMyCouponList(USE_TYPE_UNUSE, currentPage, PAGE_SIZE);
            }
        });
        couponCtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                couponCtv.setChecked(!couponCtv.isChecked());
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getMonth() == 1) {
                        if (couponCtv.isChecked()) {
                            setAdapterChecked(i);
                            list.get(i).setMoney(String.format("%.0f", Float.parseFloat(list.get(i).getMoney()) - Float.parseFloat(couponBean.getCoupon().getAmount())));
                            list.get(i).setMoneyPerMonth(String.format("%.0f", Float.parseFloat(list.get(i).getMoneyPerMonth()) - Float.parseFloat(couponBean.getCoupon().getAmount())));
                            vipAdapter.notifyDataSetChanged();
                        } else {
                            list.get(i).setMoneyPerMonth(onMonthMoney);
                            list.get(i).setMoney(onMonthMoney);
                            vipAdapter.notifyDataSetChanged();
                        }
                        money = list.get(i).getMoney();
                    }
                }
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        list = new ArrayList<VIPSettingListBean.ReturnDataBean.ListBean>();
        initRecyclerView(list);
        GetVIPSettingList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.btn_openVip:  //开通vip
                if (userInfoBean == null) {
                    return;
                }
                if (TextUtils.isEmpty(money)) {
                    return;
                }
                new BottomDialogUtil_Pay(VipActivity.this, couponCtv.isChecked(), R.layout.dialog_bottom_vip, userInfoBean, money, new BottomDialogUtil_Pay.BottonDialogListener() {
                    @Override
                    public void onItemListener(int i) {
                        switch (i) {
                            case 0:  //支付宝
                                PayManager.getInstances().goPay(VipActivity.this, 1, packageID, couponCtv.isChecked() == true ? couponBean : null);
                                break;
                            case 1:  //微信
                                ToastUtil.showShort(VipActivity.this, "微信");
                                break;
                            case 2:   //信用卡
                                Intent intent = new Intent(VipActivity.this, CreditCardPayActivity.class);
                                startActivity(intent);
                                break;
                            case 3:  //余额
                                balanceRechargeVIP(packageID);
                                break;
                        }
                    }
                });
                break;
        }
    }

    /**
     * 刷新界面
     *
     * @param userInfoBean
     */
    private void refreshView(UserInfoBean userInfoBean) {
        Glide.with(VipActivity.this).load(userInfoBean.getReturnData().getPhoto()).apply(options).into(iv_touxiang);
        if (1 == userInfoBean.getReturnData().getUserIdentify()) {
            iv_v.setVisibility(View.VISIBLE);
        } else {
            iv_v.setVisibility(View.INVISIBLE);
        }
        userLevel = userInfoBean.getReturnData().getUserLevel();
        if (userLevel == 1 || userLevel == 3) { //普通会员
            tv_vip.setText(R.string.vip_unOpenVip);
            btn_openVip.setText(R.string.vip_openVip);
        } else if (userLevel == 2) {//vip
            long nowTime = System.currentTimeMillis();
            long vipExpirationTime = Long.parseLong(userInfoBean.getReturnData().getVipExpirationTime());
            tv_vip.setText(getString(R.string.also) + DateUtil.formatDuring(this, vipExpirationTime - nowTime / 1000) + getString(R.string.mine_vip_status_open));
            btn_openVip.setText(R.string.vip_beOpenVip);
        }
    }

    /***
     * 初始列表
     */
    int count = 0;

    private void initRecyclerView(final List<VIPSettingListBean.ReturnDataBean.ListBean> listData) {
        FullyGridLayoutManager mgr = new FullyGridLayoutManager(VipActivity.this, 3);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mgr);
        vipAdapter = new VipAdapter(VipActivity.this, listData);
        // 设置adapter
        mRecyclerView.setAdapter(vipAdapter);
        final int[] temp = {-1};
        vipAdapter.setOnItemClickListener(new VipAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                VIPSettingListBean.ReturnDataBean.ListBean item = list.get(position);
                if (couponBean != null) {
                    if (item.getMonth() == 1) {
                        couponCtv.setChecked(true);
                        disableCoupon(true);
                        if (count == 0) {
                            temp[0] = position;
                            list.get(position).setMoney(String.format("%.0f", Float.parseFloat(list.get(position).getMoney()) - Float.parseFloat(couponBean.getCoupon().getAmount())));
                            list.get(position).setMoneyPerMonth(String.format("%.0f", Float.parseFloat(list.get(position).getMoneyPerMonth()) - Float.parseFloat(couponBean.getCoupon().getAmount())));
                        }
                        count++;
                    } else {
                        if (temp[0] > 0) {
                            list.get(temp[0]).setMoney(onMonthMoney);
                            list.get(temp[0]).setMoneyPerMonth(onMonthMoney);
                        }
                        couponCtv.setChecked(false);
                        disableCoupon(false);
                        count = 0;
                    }
                }
                money = list.get(position).getMoney();
                packageID = list.get(position).getPackageID();
                setAdapterChecked(position);
            }
        });
    }

    public void setAdapterChecked(int position) {
        for (int i = 0; i < list.size(); i++) {
            if (i == position) {
                list.get(i).setIsDefault(true);
            } else {
                list.get(i).setIsDefault(false);
            }
        }
        vipAdapter.upData(list);
    }

    public void disableCoupon(boolean disable) {
        if (disable) {
            sekuaiIv.setImageResource(R.mipmap.sekuai);
            currencyTv.setTextColor(ContextCompat.getColor(this, R.color.pink_ff8));
            priceTv.setTextColor(ContextCompat.getColor(this, R.color.pink_ff8));
            titleTv.setTextColor(ContextCompat.getColor(this, R.color.gray_66));
            timeTv.setTextColor(ContextCompat.getColor(this, R.color.gray_999));
            couponCtv.setEnabled(true);
        } else {
            sekuaiIv.setImageResource(R.mipmap.sekuai_grey);
            currencyTv.setTextColor(ContextCompat.getColor(this, R.color.black_ccc));
            priceTv.setTextColor(ContextCompat.getColor(this, R.color.black_ccc));
            titleTv.setTextColor(ContextCompat.getColor(this, R.color.gray_999));
            timeTv.setTextColor(ContextCompat.getColor(this, R.color.gray_999));
            couponCtv.setEnabled(false);
        }
    }

    /**
     * 获取vip配置列表
     */
    private void GetVIPSettingList() {
        VipSettingListApi vipSettingListApi = new VipSettingListApi(this, new HttpOnNextListener<VIPSettingListBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(VIPSettingListBean vipSettingListBean) {
                mLoadingErrorView.setVisibility(View.GONE);
                ll_content.setVisibility(View.VISIBLE);
                if (vipSettingListBean.getErrorCode() == 200) {
                    list = vipSettingListBean.getReturnData().getList();
                    vipAdapter.upData(list);
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isIsDefault()) {
                            packageID = list.get(i).getPackageID();
                            money = list.get(i).getMoney();
                        }

                        if (list.get(i).getMonth() == 1) {
                            onMonthMoney = list.get(i).getMoney();
                        }
                        if (list.get(i).isIsDefault()) {
                            disableCoupon(true);
                        } else {
                            disableCoupon(false);
                        }
                    }
                } else {
                    CustomToast.showToast(vipSettingListBean.getErrorMsg());
                }
                return null;
            }

            @Override
            public RetryWhenNetworkException.Wrapper onError(Throwable e) {
                super.onError(e);
                ll_content.setVisibility(View.GONE);
                mLoadingErrorView.setVisibility(View.VISIBLE);
                mLoadingErrorView.onError(e);
                return null;
            }
        });
        HttpManager.getInstance().doHttpDeal(VipActivity.this, vipSettingListApi);
    }

    /**
     * 充值vip接口
     *
     * @param packageID
     */
    private void balanceRechargeVIP(int packageID) {
        BalanceRechargeVipApi balanceRechargeVipApi = new BalanceRechargeVipApi(this, new HttpOnNextListener<CommonBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(CommonBean commonBean) {
                Intent intent = new Intent(VipActivity.this, RechargeStatusActivity.class);
                if (commonBean.getErrorCode() == 200) {
                    intent.putExtra("isSuccess", true);
                } else {
                    intent.putExtra("isSuccess", false);
                }
                intent.putExtra("msg", commonBean.getErrorMsg());
                startActivity(intent);
                if (isFinishActivity && commonBean.getErrorCode() == 200) {
                    finish();
                }
                return null;
            }
        });
        balanceRechargeVipApi.setPackageID(packageID);
        HttpManager.getInstance().doHttpDeal(VipActivity.this, balanceRechargeVipApi);

    }

    public void getMyCouponList(int useType, int pageIndex, int pageSize) {
        GetMyCouponListApi getMyCouponListApi = new GetMyCouponListApi(this);
        getMyCouponListApi.setUseType(useType);
        getMyCouponListApi.setPageIndex(pageIndex);
        getMyCouponListApi.setPageSize(pageSize);
        getMyCouponListApi.setListener(new HttpOnNextListener<GetMyCouponListBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(GetMyCouponListBean getMyCouponListBean) {
                if (getMyCouponListBean.getErrorCode() == 200) {
                    List<GetMyCouponListBean.ReturnDataBean.ListBean> couponList = getMyCouponListBean.getReturnData().getList();
                    if (couponList.size() == 0) {
                        couponLayout.setVisibility(View.GONE);
                    } else {
                        couponLayout.setVisibility(View.VISIBLE);
                        couponBean = getMyCouponListBean.getReturnData().getList().get(0);
                        priceTv.setText(couponBean.getCoupon().getAmount());
                        titleTv.setText(couponBean.getCoupon().getName());
                        //                        timeTv.setText(getString(R.string.coupon_expiration_date_msg, couponBean.getCoupon().getExpirationDate()));
                        timeTv.setText(couponBean.getCoupon().getExpirationDate());
                    }
                }
                return null;
            }
        });
        HttpManager.getInstance().doHttpDeal(this, getMyCouponListApi);
    }

}
