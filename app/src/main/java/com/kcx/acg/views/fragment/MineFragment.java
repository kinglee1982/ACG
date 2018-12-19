package com.kcx.acg.views.fragment;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.kcx.acg.R;
import com.kcx.acg.api.GetAdvertisementInfoApi;
import com.kcx.acg.api.GetUserInfoApi;
import com.kcx.acg.api.IsAcceptActivityCouponApi;
import com.kcx.acg.api.UserSignInApi;
import com.kcx.acg.base.BaseFragment;
import com.kcx.acg.base.BusEvent;
import com.kcx.acg.bean.CommonBean;
import com.kcx.acg.bean.GetAdvertisementInfoBean;
import com.kcx.acg.bean.IsAcceptActivityCouponBean;
import com.kcx.acg.bean.UserInfoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.manager.AccountManager;
import com.kcx.acg.manager.DialogManager;
import com.kcx.acg.manager.H5LinkUrlManager;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.ButtonUtils;
import com.kcx.acg.utils.DateUtil;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.utils.StringUtil;
import com.kcx.acg.utils.ToastUtil;
import com.kcx.acg.views.activity.AccountActivity;
import com.kcx.acg.views.activity.AdvertisActivity;
import com.kcx.acg.views.activity.AttentionAndFansActivity;
import com.kcx.acg.views.activity.CollectActivity;
import com.kcx.acg.views.activity.ContributeActivity;
import com.kcx.acg.views.activity.CreativeCenterActivity;
import com.kcx.acg.views.activity.HistoryActivity;
import com.kcx.acg.views.activity.IncomeActivity;
import com.kcx.acg.views.activity.LoginActivity;
import com.kcx.acg.views.activity.LookHeadPortraitsActivity;
import com.kcx.acg.views.activity.MainActivity;
import com.kcx.acg.views.activity.MessageActivity;
import com.kcx.acg.views.activity.OriginalPlanActivity;
import com.kcx.acg.views.activity.PersonInfoActivity;
import com.kcx.acg.views.activity.SettingActivity;
import com.kcx.acg.views.activity.VipActivity;
import com.kcx.acg.views.activity.WebViewActivity;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.HomeDialog;
import com.kcx.acg.views.view.RegisterSuccessDialog;
import com.kcx.acg.views.view.SignDialog;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.jessyan.autosize.internal.CancelAdapt;
import retrofit2.http.HEAD;

/**
 * 我的
 * Created by jb on 2018/8/30.
 */

public class MineFragment extends BaseFragment<MainActivity> implements CancelAdapt {
    private View view = null;
    private MainActivity mActivity;
    private String accessToken;
    private UserInfoBean mUserInfoBean;

    private RelativeLayout rl_news;
    //未登录状态
    private Button btn_login_mine;
    private RelativeLayout rl_unLogin;

    //已登录状态
    private TextView tv_attention;
    private RelativeLayout rl_beLogin;
    private RelativeLayout rl_signIn, rl_luck, rl_welfare;
    private RelativeLayout rl_vip, rl_account, rl_earnings, rl_creativeCenter, rl_attentionAndFans, rl_watchHistory, rl_originalPlan, rl_FMotherPlan, rl_set, rl_collect, rl_compile, rl_tuiguang;
    private ImageView iv_touxiang_belogin, iv_isSign, iv_abi_icon, iv_signIn, iv_message, iv_v;
    private TextView tv_nickname, tv_earnings, tv_accoutStatus_hint, tv_vip_hint, tv_remainLuckDrawCount, tv_accout, tv_attentionAndFans, tv_signIn, tv_porterPlan;

    private String nickname, imagePath;
    private UserInfoBean.ReturnDataBean returnData;
    private RequestOptions options;
    private boolean flag = true;
    private boolean dialogFlag = true;


    @Override
    public void onEventMainThread(BusEvent event) {
        super.onEventMainThread(event);
        if (event.getType() == BusEvent.ATTENTION_MEMBER || event.getType() == BusEvent.SHARE) {
            if (view != null)
                refreshUserInfo();
        }
        if (event.getType() == BusEvent.LOGIN_SUCCESS) {
            boolean isTop = isActivityTop(mContext.getClass(), mContext);
            if (isTop)
                isAcceptActivityCouponApi(mContext);
        }
        if (event.getType() == BusEvent.LOGIN_OUT) {
            flag = false;
        }
        if (event.getType() == BusEvent.CLICK_MINE_TAB && dialogFlag) {
            isAcceptActivityCouponApi(mContext);
        }
    }

    public boolean isActivityTop(Class cls, Context context) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(cls.getName());
    }


    @Override
    public void onResume() {
        super.onResume();
        accessToken = SPUtil.getString(getActivity(), Constants.ACCESS_TOKEN, "");
        if (TextUtils.isEmpty(accessToken)) {  //未登录
            mActivity.changeStatusBar(false);
            rl_unLogin.setVisibility(View.VISIBLE);
            rl_beLogin.setVisibility(View.GONE);
        } else {  //已登录
            mActivity.changeStatusBar(true);
            rl_unLogin.setVisibility(View.GONE);
            rl_beLogin.setVisibility(View.VISIBLE);
            refreshUserInfo();
        }
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        boolean isLogin = AccountManager.getInstances().isLogin(mContext, false);
        if (isLogin && dialogFlag)
            isAcceptActivityCouponApi(mContext);
        return view;
    }

    public void showDialog(String url, String targetUrl) {
        HomeDialog.Builder builder = new HomeDialog.Builder(mContext);
        builder.setLayoutId(R.layout.dialog_spread_layout).setAdvUrl(url, targetUrl).builder().show();
    }

    @Override
    public void initUI(View view) {
        super.initUI(view);
        mActivity = (MainActivity) getActivity();
        rl_news = view.findViewById(R.id.rl_news);
        //未登录
        btn_login_mine = view.findViewById(R.id.btn_login_mine);
        rl_unLogin = view.findViewById(R.id.rl_unLogin);
        //已登录
        rl_beLogin = view.findViewById(R.id.rl_beLogin);
        rl_compile = view.findViewById(R.id.rl_compile);
        rl_signIn = view.findViewById(R.id.rl_signIn);
        rl_luck = view.findViewById(R.id.rl_luck);
        rl_welfare = view.findViewById(R.id.rl_welfare);
        rl_vip = view.findViewById(R.id.rl_vip);
        rl_account = view.findViewById(R.id.rl_account);
        rl_earnings = view.findViewById(R.id.rl_earnings);
        rl_creativeCenter = view.findViewById(R.id.rl_creativeCenter);
        rl_attentionAndFans = view.findViewById(R.id.rl_attentionAndFans);
        rl_watchHistory = view.findViewById(R.id.rl_watchHistory);
        rl_originalPlan = view.findViewById(R.id.rl_originalPlan);
        rl_FMotherPlan = view.findViewById(R.id.rl_FMotherPlan);
        rl_set = view.findViewById(R.id.rl_set);
        rl_collect = view.findViewById(R.id.rl_collect);
        iv_touxiang_belogin = view.findViewById(R.id.iv_touxiang_belogin);
        tv_nickname = view.findViewById(R.id.tv_nickname);
        tv_earnings = view.findViewById(R.id.tv_earnings);
        tv_accoutStatus_hint = view.findViewById(R.id.tv_accoutStatus_hint);
        tv_vip_hint = view.findViewById(R.id.tv_vip_hint);
        iv_isSign = view.findViewById(R.id.iv_isSign);
        tv_remainLuckDrawCount = view.findViewById(R.id.tv_remainLuckDrawCount);
        tv_accout = view.findViewById(R.id.tv_accout);
        iv_abi_icon = view.findViewById(R.id.iv_abi_icon);
        tv_attentionAndFans = view.findViewById(R.id.tv_attentionAndFans);
        tv_attention = view.findViewById(R.id.tv_attention);
        tv_attention.setText(getString(R.string.attentionAndFans_attention) + " & " + getString(R.string.attentionAndFans_fans));
        tv_signIn = view.findViewById(R.id.tv_signIn);
        iv_signIn = view.findViewById(R.id.iv_signIn);
        iv_message = view.findViewById(R.id.iv_message);
        iv_v = view.findViewById(R.id.iv_v);
        tv_porterPlan = view.findViewById(R.id.tv_porterPlan);
        rl_tuiguang = view.findViewById(R.id.rl_tuiguang);

        options = new RequestOptions()
                .placeholder(R.mipmap.placehold_head)  //预加载图片
                .error(R.mipmap.placehold_head);

    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        super.initListener();
        rl_news.setOnClickListener(this);
        //未登录
        btn_login_mine.setOnClickListener(this);
        //已登录
        rl_compile.setOnClickListener(this);
        rl_signIn.setOnClickListener(this);
        rl_luck.setOnClickListener(this);
        rl_welfare.setOnClickListener(this);
        rl_vip.setOnClickListener(this);
        rl_account.setOnClickListener(this);
        rl_earnings.setOnClickListener(this);
        rl_creativeCenter.setOnClickListener(this);
        rl_attentionAndFans.setOnClickListener(this);
        rl_watchHistory.setOnClickListener(this);
        rl_originalPlan.setOnClickListener(this);
        rl_FMotherPlan.setOnClickListener(this);
        rl_set.setOnClickListener(this);
        rl_collect.setOnClickListener(this);
        iv_touxiang_belogin.setOnClickListener(this);
        rl_tuiguang.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_touxiang_belogin://查看头像
                Intent i1 = new Intent(getActivity(), LookHeadPortraitsActivity.class);
                i1.putExtra("photoUrl", mUserInfoBean.getReturnData().getPhoto());
                startActivity(i1);
                break;
            case R.id.rl_news://消息
                startActivity(new Intent(getActivity(), MessageActivity.class));
                break;
            case R.id.rl_compile://编辑个人信息
                Intent i = new Intent(getActivity(), PersonInfoActivity.class);
                startActivity(i);
                break;
            case R.id.btn_login_mine: //登陆
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.rl_signIn: //每日签到
                if (returnData.isIsSign()) {
                    CustomToast.showToast(getString(R.string.mine_signIn_hint));
                } else {
                    if (!ButtonUtils.isFastDoubleClick(R.id.rl_signIn)) {
                        userSignIn();
                    }
                }
                break;
            case R.id.rl_luck: //幸运抽奖
                if (!ButtonUtils.isFastDoubleClick(R.id.rl_luck)) {
                    goLottery();
                }
                break;
            case R.id.rl_welfare: //赚现金
                if (!ButtonUtils.isFastDoubleClick(R.id.rl_welfare)) {
                    H5LinkUrlManager.getInstances().getH5LinkUrl(getActivity(), 2);
//                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
//                    intent.putExtra("url", mUserInfoBean.getReturnData().getH5Link_Welfare());
//                    startActivity(intent);
                }
                break;
            case R.id.rl_vip: //vip
                startActivity(new Intent(getActivity(), VipActivity.class));
                break;
            case R.id.rl_account: //账户
                startActivity(new Intent(getActivity(), AccountActivity.class));
                break;
            case R.id.rl_earnings: //收益
                startActivity(new Intent(getActivity(), IncomeActivity.class));
                break;
            case R.id.rl_creativeCenter: //创作中心
                startActivity(new Intent(getActivity(), CreativeCenterActivity.class));
                break;
            case R.id.rl_attentionAndFans: //关注与粉丝
                Intent intent2 = new Intent(getActivity(), AttentionAndFansActivity.class);
                startActivityForResult(intent2, 100);
                break;
            case R.id.rl_watchHistory: //浏览历史
                startActivity(new Intent(getActivity(), HistoryActivity.class));
                break;
            case R.id.rl_originalPlan: //原创计划
                startActivity(new Intent(getActivity(), OriginalPlanActivity.class));
                break;
            case R.id.rl_FMotherPlan: //F娘计划
                ToastUtil.showShort(getActivity(), "F娘");
                break;
            case R.id.rl_set: //设置
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.rl_collect: //收藏
                Intent intent1 = new Intent(getActivity(), CollectActivity.class);
                startActivityForResult(intent1, 10010);
                break;

            case R.id.rl_tuiguang: //推广活动
                H5LinkUrlManager.getInstances().getH5LinkUrl(getActivity(), 8);
                break;
        }
    }

    //跳转到抽奖界面
    private void goLottery() {
        H5LinkUrlManager.getInstances().getH5LinkUrl(getActivity(), 1);
//        Intent intent = new Intent(getActivity(), WebViewActivity.class);
//        intent.putExtra("url", mUserInfoBean.getReturnData().getH5Link_Lottery());
//        intent.putExtra("classType", 1);
//        startActivity(intent);
    }

    /**
     * 用户签到接口
     */
    private void userSignIn() {
        UserSignInApi userSignInApi = new UserSignInApi((RxAppCompatActivity) getActivity(), new HttpOnNextListener<CommonBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(CommonBean commonBean) {
                if (commonBean.getErrorCode() == 200) {
                    refreshUserInfo();//刷新用户数据
                    new SignDialog(getActivity(), R.layout.dialog_sign_in, true, mUserInfoBean, new SignDialog.OnClickListener() {
                        @Override
                        public void onItemListener() {
                            goLottery();
                        }
                    });
                } else {
                    CustomToast.showToast(commonBean.getErrorMsg());
                }
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(getActivity(), userSignInApi);
    }

    /**
     * 获取个人中心接口
     */
    private void refreshUserInfo() {
        GetUserInfoApi getUserInfoApi = new GetUserInfoApi(new HttpOnNextListener<UserInfoBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(UserInfoBean userInfoBean) {
                mUserInfoBean = userInfoBean;
                if (mUserInfoBean.getErrorCode() == 200) {
                    SPUtil.putString(getActivity(), Constants.USER_INFO, AppUtil.getGson().toJson(userInfoBean));
                    refreshView(userInfoBean);
                }
                return null;
            }

            @Override
            public RetryWhenNetworkException.Wrapper onError(Throwable e) {
                super.onError(e);
                String userInfo = SPUtil.getString(getActivity(), Constants.USER_INFO, "");
                if (!TextUtils.isEmpty(userInfo)) {
                    mUserInfoBean = AppUtil.getGson().fromJson(userInfo, UserInfoBean.class);
                }
                refreshView(mUserInfoBean);
                return null;
            }
        }, (RxAppCompatActivity) getActivity());
        HttpManager httpManager = HttpManager.getInstance();
        httpManager.doHttpDeal(getActivity(), getUserInfoApi);
    }

    /**
     * 渲染界面
     *
     * @param userInfoBean
     */
    private void refreshView(UserInfoBean userInfoBean) {
        returnData = userInfoBean.getReturnData();
        if (returnData.getMessageCount() > 0) {
            iv_message.setVisibility(View.VISIBLE);
        } else {
            iv_message.setVisibility(View.INVISIBLE);
        }

        Glide.with(getActivity()).load(returnData.getPhoto()).apply(options).into(iv_touxiang_belogin);
        // UserIdentify：身份标识（0:未标识，1：F娘，2：搬运工）
        if (returnData.getUserIdentify() == 1) {
            iv_v.setVisibility(View.VISIBLE);
        } else {
            iv_v.setVisibility(View.INVISIBLE);
        }
        tv_nickname.setText(returnData.getUserName());
        int accountStatus = returnData.getAccountStatus();
        if (accountStatus == 5 || accountStatus == 7 || accountStatus == 8 || accountStatus == 9) { //账号封禁
            tv_accoutStatus_hint.setVisibility(View.VISIBLE);
            tv_accoutStatus_hint.setText(returnData.getAlertText());
        } else {
            tv_accoutStatus_hint.setVisibility(View.INVISIBLE);
        }

        //是否签到
        if (returnData.isIsSign()) {
            iv_isSign.setVisibility(View.INVISIBLE);
            tv_signIn.setText(R.string.mine_beSignIn);
            tv_signIn.setTextColor(getActivity().getResources().getColor(R.color.black_999));
            iv_signIn.setBackgroundResource(R.mipmap.icon_signin);
        } else {
            iv_isSign.setVisibility(View.VISIBLE);
            tv_signIn.setText(R.string.mine_signIn);
            tv_signIn.setTextColor(getActivity().getResources().getColor(R.color.black_333));
            iv_signIn.setBackgroundResource(R.mipmap.icon_unsignin);
        }

        //剩余抽奖次数
        if (returnData.getRemainLuckDrawCount() == 0) {
            tv_remainLuckDrawCount.setVisibility(View.INVISIBLE);
        } else {
            tv_remainLuckDrawCount.setVisibility(View.VISIBLE);
            tv_remainLuckDrawCount.setText("x" + returnData.getRemainLuckDrawCount());
        }

        //vip
        int userLevel = returnData.getUserLevel();
        if (userLevel == 1 || userLevel == 3) { //普通会员
            tv_vip_hint.setText(R.string.mine_vip_status_unOpen);
        } else if (userLevel == 2) { //vip
            tv_vip_hint.setText(DateUtil.timet(returnData.getVipExpirationTime()) + getActivity().getString(R.string.mine_vip_status_open));
        }

        //账户
        if (returnData.isIsHaveCharged()) { //是否充值过
            iv_abi_icon.setVisibility(View.VISIBLE);
            tv_accout.setText(StringUtil.toDecimalFormat(returnData.getTotalACoin()));
        } else {
            iv_abi_icon.setVisibility(View.INVISIBLE);
            tv_accout.setText(R.string.mine_account_frist);
        }

        //收益
        if (returnData.isIsHaveNewIncome()) {
            tv_earnings.setVisibility(View.VISIBLE);
        } else {
            tv_earnings.setVisibility(View.INVISIBLE);
        }

        //关注与粉丝
        tv_attentionAndFans.setText(returnData.getAttentionCount() + " & " + returnData.getFansCount());

        switch (returnData.getAuthenticateStatus()) {
            case Constants.UserInfo.STATUS_USER_AUTHENTICATE_UN_SUB:
                tv_porterPlan.setText(getString(R.string.mine_unsub_msg));
                break;
            case Constants.UserInfo.STATUS_USER_AUTHENTICATE_REVIEW:
                tv_porterPlan.setText(getString(R.string.mine_review_msg));
                break;
            case Constants.UserInfo.STATUS_USER_AUTHENTICATE_PASSED:
                tv_porterPlan.setText(getString(R.string.mine_passed_msg));
                break;
            case Constants.UserInfo.STATUS_USER_AUTHENTICATE_UNPASS:
                tv_porterPlan.setText(getString(R.string.mine_un_pass_msg));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 10086:  //收藏界面返回
                ((MainActivity) getActivity()).setFragment();
                break;
            case 10010:  //关注与粉丝界面返回
                ((MainActivity) getActivity()).setFragment();
                break;
        }
    }

    public void getAdvertisementInfo(int locationID) {
        GetAdvertisementInfoApi getAdvertisementInfoApi = new GetAdvertisementInfoApi(mContext);
        getAdvertisementInfoApi.setLocationID(locationID);
        getAdvertisementInfoApi.setListener(new HttpOnNextListener<GetAdvertisementInfoBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(final GetAdvertisementInfoBean getAdvertisementInfoBean) {
                if (getAdvertisementInfoBean.getReturnData() != null)
                    if (getAdvertisementInfoBean.getErrorCode() == 200) {
                        boolean isLogin = AccountManager.getInstances().isLogin(mContext, false);
                        if (isLogin) {
                            showDialog(getAdvertisementInfoBean.getReturnData().getImageUrl(), getAdvertisementInfoBean.getReturnData().getTargetUrl());
                        }
                    }
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(mContext, getAdvertisementInfoApi);
    }

    public void isAcceptActivityCouponApi(final Context context) {
        dialogFlag = false;
        IsAcceptActivityCouponApi isAcceptActivityCouponApi = new IsAcceptActivityCouponApi((RxAppCompatActivity) context);
        isAcceptActivityCouponApi.setListener(new HttpOnNextListener<IsAcceptActivityCouponBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(IsAcceptActivityCouponBean isAcceptActivityCouponBean) {
                if (isAcceptActivityCouponBean.getErrorCode() == 200) {
                    if (isAcceptActivityCouponBean.getReturnData().isIsAccepted()) {
                        getAdvertisementInfo(Constants.KEY_ADVERTIS_DIALOG_SHARE);
                    }
                }
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(context, isAcceptActivityCouponApi);
    }

}
