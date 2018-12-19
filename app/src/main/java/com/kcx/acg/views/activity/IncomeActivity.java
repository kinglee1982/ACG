package com.kcx.acg.views.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.kcx.acg.R;
import com.kcx.acg.api.GetAdvertisementInfoApi;
import com.kcx.acg.api.UserIncomeInfoApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.bean.GetAdvertisementInfoBean;
import com.kcx.acg.bean.UserIncomeInfoBean;
import com.kcx.acg.bean.UserInfoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.manager.H5LinkUrlManager;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.ButtonUtils;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.views.view.LoadingErrorView;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 收益
 * Created by jb on 2018/9/17.
 */
public class IncomeActivity extends BaseActivity implements CancelAdapt {
    private UserInfoBean userInfoBean;
    private TextView tv_title, tv_right, tv_whoEarings, tv_totalIncome, tv_newIncome, tv_freeze;
    private Button btn_withdrawal;
    private LinearLayout ll_generalize, ll_back;
    private LoadingErrorView mLoadingErrorView;
    private UserIncomeInfoBean.ReturnDataBean returnData = null;
    private int accountStatus;  //账号状态
    private ImageView advertisIv;
    private ImageView wenhaoIv;

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_earnings, null);
    }

    @Override
    public void initView() {
        super.initView();
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.income_title);
        tv_right = findViewById(R.id.tv_right);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(R.string.income_detail);

        ll_back = findViewById(R.id.ll_back);
        tv_whoEarings = findViewById(R.id.tv_whoEarings);
        btn_withdrawal = findViewById(R.id.btn_withdrawal);
        ll_generalize = findViewById(R.id.ll_generalize);

        tv_totalIncome = findViewById(R.id.tv_totalIncome);
        tv_newIncome = findViewById(R.id.tv_newIncome);
        tv_freeze = findViewById(R.id.tv_freeze);
        mLoadingErrorView = findViewById(R.id.loadingErrorView);
        advertisIv = findViewById(R.id.earnings_advertis_iv);
        wenhaoIv = findViewById(R.id.earnings_wenhao_iv);
    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);
        btn_withdrawal.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        tv_whoEarings.setOnClickListener(this);
        ll_generalize.setOnClickListener(this);
        wenhaoIv.setOnClickListener(this);

        mLoadingErrorView.setOnReloadListener(new LoadingErrorView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                getUserIncomeInfo();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        userInfoBean = AppUtil.getGson().fromJson(SPUtil.getString(this, Constants.USER_INFO, ""), UserInfoBean.class);
        getAdvertisementInfo(Constants.KEY_ADVERTIS_INCOME);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserIncomeInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_right: //明细
                if (!ButtonUtils.isFastDoubleClick(R.id.tv_right)) {
                    startDDMActivity(IncomeDetailActivity.class, true);
                }
                break;
            case R.id.btn_withdrawal:  //收益提现
                if (returnData != null) {
                    Intent i = new Intent(IncomeActivity.this, IncomeWithdrawalActivity.class);
                    i.putExtra("totalIncome", returnData.getTotalIncome());
                    startActivity(i);
                }

                break;
            case R.id.tv_whoEarings:  //什么是收益
                if (!ButtonUtils.isFastDoubleClick(R.id.tv_whoEarings)) {
//                    Intent intent = new Intent(this, WebViewActivity.class);
//                    intent.putExtra("classType", 4);
//                    intent.putExtra("url", returnData.getH5LinkIncomeDescription());
//                    startActivity(intent);
                    H5LinkUrlManager.getInstances().getH5LinkUrl(IncomeActivity.this,4);
                }
                break;
            case R.id.ll_generalize:  //推广
                if (!ButtonUtils.isFastDoubleClick(R.id.ll_generalize)) {
                    Intent i1 = new Intent(this, WebViewActivity.class);
                    i1.putExtra("url", userInfoBean.getReturnData().getH5Link_Welfare());
                    i1.putExtra("classType", 2);
                    startActivity(i1);
                }
                break;
            case R.id.earnings_wenhao_iv:
                if (!ButtonUtils.isFastDoubleClick(R.id.tv_whoEarings)) {
                    Intent intent = new Intent(this, WebViewActivity.class);
                    intent.putExtra("classType", 4);
                    intent.putExtra("url", returnData.getH5LinkIncomeDescription());
                    startActivity(intent);
                }
                break;
        }
    }

    /**
     * 获取收益信息接口
     */
    public void getUserIncomeInfo() {
        UserIncomeInfoApi userIncomeInfoApi = new UserIncomeInfoApi(this, new HttpOnNextListener<UserIncomeInfoBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(UserIncomeInfoBean userIncomeInfoBean) {
                mLoadingErrorView.setVisibility(View.GONE);
                if (userIncomeInfoBean.getErrorCode() == 200) {
                    returnData = userIncomeInfoBean.getReturnData();
                    accountStatus = returnData.getAccountStatus();
                    tv_totalIncome.setText("¥" + returnData.getTotalIncome());
                    if (userInfoBean.getReturnData().isIsHaveNewIncome()) {//新收益
                        tv_newIncome.setText(getString(R.string.income_new) + " ¥" + returnData.getNewIncome());
                        tv_newIncome.setVisibility(View.VISIBLE);
                    } else {
                        tv_newIncome.setVisibility(View.INVISIBLE);
                    }

                    if (accountStatus == 1) { //正常状态
                        tv_totalIncome.setTextColor(getResources().getColor(R.color.black_333));
                        tv_freeze.setVisibility(View.INVISIBLE);
                        btn_withdrawal.setText(R.string.income_btn);
                        btn_withdrawal.setTextColor(getResources().getColor(R.color.white));
                        btn_withdrawal.setBackgroundResource(R.drawable.shape_pink_bg_5dp);
                        btn_withdrawal.setEnabled(true);

                    } else if (accountStatus == 5 || accountStatus == 7 || accountStatus == 8 || accountStatus == 9) {//封禁状态
                        tv_totalIncome.setTextColor(getResources().getColor(R.color.black_ccc));
                        tv_freeze.setVisibility(View.VISIBLE);
                        btn_withdrawal.setText(R.string.income_btn2);
                        btn_withdrawal.setTextColor(getResources().getColor(R.color.black_999));
                        btn_withdrawal.setBackgroundResource(R.drawable.shape_gary_bg_5dp);
                        btn_withdrawal.setEnabled(false);
                    }
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
        HttpManager.getInstance().doHttpDeal(IncomeActivity.this, userIncomeInfoApi);
    }

    public void getAdvertisementInfo(int locationID) {
        GetAdvertisementInfoApi getAdvertisementInfoApi = new GetAdvertisementInfoApi(this);
        getAdvertisementInfoApi.setLocationID(locationID);
        getAdvertisementInfoApi.setListener(new HttpOnNextListener<GetAdvertisementInfoBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(final GetAdvertisementInfoBean getAdvertisementInfoBean) {
                if (getAdvertisementInfoBean.getErrorCode() == 200) {
                    if (getAdvertisementInfoBean.getReturnData() != null) {
                        MultiTransformation multi = new MultiTransformation(
                                new CenterCrop(),
                                new RoundedCornersTransformation(5, 0, RoundedCornersTransformation.CornerType.ALL));
                        RequestOptions options = new RequestOptions().transform(multi);
                        options.placeholder(R.drawable.advertis_holder_bg);
                        options.error(R.drawable.advertis_holder_bg);
                        Glide.with(IncomeActivity.this).load(getAdvertisementInfoBean.getReturnData().getImageUrl()).apply(options).into(advertisIv);
                        advertisIv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(IncomeActivity.this, WebViewActivity.class);
                                intent.putExtra("url", getAdvertisementInfoBean.getReturnData().getTargetUrl());
                                startDDMActivity(intent, true);
                            }
                        });
                    }
                }

                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(this, getAdvertisementInfoApi);
    }
}
