package com.kcx.acg.views.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.blankj.utilcode.util.LogUtils;
import com.google.common.collect.Lists;
import com.kcx.acg.R;
import com.kcx.acg.api.AcceptActivityCouponApi;
import com.kcx.acg.api.GetAppUpgradeInfoApi;
import com.kcx.acg.api.GetRecommendProductList;
import com.kcx.acg.api.GetUserInfoApi;
import com.kcx.acg.api.IsAcceptActivityCouponApi;
import com.kcx.acg.base.BaseFragment;
import com.kcx.acg.base.BusEvent;
import com.kcx.acg.base.SysApplication;
import com.kcx.acg.bean.AcceptActivityCouponBean;
import com.kcx.acg.bean.HomeBean;
import com.kcx.acg.bean.IsAcceptActivityCouponBean;
import com.kcx.acg.bean.UpgradeInfoBean;
import com.kcx.acg.bean.UserInfoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.manager.AccountManager;
import com.kcx.acg.manager.DialogManager;
import com.kcx.acg.manager.UpdateManager;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.CommonUtils;
import com.kcx.acg.utils.DateUtil;
import com.kcx.acg.utils.FormatCurrentData;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.views.activity.LoginActivity;
import com.kcx.acg.views.activity.MainActivity;
import com.kcx.acg.views.adapter.HomePagerAdapter;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.HomeDialog;
import com.kcx.acg.views.view.LoadingErrorView;
import com.kcx.acg.views.view.OrientedViewPager;
import com.kcx.acg.views.view.transformer.VerticalStackTransformer;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.List;

import io.vov.vitamio.utils.StringUtils;
import me.jessyan.autosize.internal.CancelAdapt;
import retrofit2.http.HEAD;

import static android.app.Activity.RESULT_CANCELED;
import static android.support.v4.app.ActivityCompat.requestPermissions;

/**
 */

public class HomeFragment extends BaseFragment<MainActivity> implements CancelAdapt {
    private static final int REQUEST_CODE_CHOOSE = 23;//定义请求码常量
    private View rootView;

    private LoadingErrorView loadingErrorView;
    private OrientedViewPager mOrientedViewPager;
    private SmartRefreshLayout refreshLayout;
    private HomePagerAdapter pagerAdapter;
    private List<HomeBean.ReturnDataBean.ListBean> listBeans;
    private int currentPage = 1;
    private int pageSize = 10;
    private int currentPos;
    private HomeDialog.Builder builder;
    //    private ImageView loadingIv;
//    private AnimationDrawable animationDrawable;

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home_fragment, container, false);
        mOrientedViewPager = rootView.findViewById(R.id.view_pager);
        loadingErrorView = rootView.findViewById(R.id.home_loading_error_view);
//        loadingIv = rootView.findViewById(R.id.loading_iv);
        refreshLayout = rootView.findViewById(R.id.home_refresh_layout);
        refreshLayout.setEnableLoadMore(false);
//        animationDrawable = (AnimationDrawable) loadingIv.getDrawable();
//        loadingIv.setImageResource(R.drawable.loading);
        builder = new HomeDialog.Builder(mContext);
        builder.setLayoutId(R.layout.dialog_premission_layout);
        listBeans = Lists.newArrayList();
        mOrientedViewPager.setOrientation(OrientedViewPager.Orientation.VERTICAL);
        //设置limit
        mOrientedViewPager.setOffscreenPageLimit(3);
        //设置transformer
        mOrientedViewPager.setPageTransformer(true, new VerticalStackTransformer(mContext, 10, 10));
        return rootView;
    }

    @Override
    public void initData() {
        getRecommendProductList(currentPage, pageSize, true);
        if (AccountManager.getInstances().getUserInfo(mContext) != null)
            AccountManager.getInstances().refreshUserInfo(mContext);
    }

    public void showLoadIv() {
//        loadingIv.setVisibility(View.GONE);
//        animationDrawable.start();
    }

    public void closeLoadIv() {
//        loadingIv.setVisibility(View.GONE);
//        animationDrawable.stop();
    }

    @Override
    public void initListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                CustomToast.showToast(mContext.getString(R.string.home_first_page_toast_msg));
                refreshLayout.finishRefresh();
            }
        });
        mOrientedViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    refreshLayout.setEnableRefresh(true);
                } else {
                    refreshLayout.setEnableRefresh(false);
                }
//                if ((position >= pageSize * (currentPage <= 2 ? currentPage : currentPage - 1) -1)) {
                if ((position >= pageSize * currentPage - 3)) {
                    currentPos = position;
                    ++currentPage;
                    getRecommendProductList(currentPage, pageSize, false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        loadingErrorView.setOnReloadListener(new LoadingErrorView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                getRecommendProductList(currentPage, pageSize, false);
            }
        });

        builder.setOnClickListener(new HomeDialog.Builder.OnClickListener() {
            @Override
            public void onConfrim() {
                requestPermissions(
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                );
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1003 && resultCode == RESULT_CANCELED) {
//            AccountManager.getInstances().clearUserInfo();
            getRecommendProductList(currentPage, pageSize, false);
        }
    }

    public void getRecommendProductList(final int pageIndex, int pageSize, final boolean checkVer) {
//        showLoadIv();
        GetRecommendProductList getRecommendProductList = new GetRecommendProductList(mContext);
        getRecommendProductList.setListener(new HttpOnNextListener<HomeBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(HomeBean homeBean) {
//                CustomToast.showToast("getRecommendProductList");
//                closeLoadIv();
                if (pageIndex == 1 && checkVer)
                    getAppUpgradeInfoApi();

//                if (homeBean.getErrorCode() == 402) {
//                    Intent intent = new Intent(mContext, LoginActivity.class);
//                    intent.putExtra(LoginActivity.KEY_GO_HOME, true);
//                    mContext.startDDMActivityForResult(intent, 1003, true);
//                    getActivity().finish();
//                } else {
                listBeans.addAll(homeBean.getReturnData().getList());
                if (pageIndex == 1) {
                    pagerAdapter = new HomePagerAdapter(listBeans, mContext);
                    mOrientedViewPager.setAdapter(pagerAdapter);
                } else {
                    pagerAdapter.notifyDataSetChanged();
                }
//                }
                return null;
            }

            @Override
            public RetryWhenNetworkException.Wrapper onError(Throwable e) {
                super.onError(e);
                loadingErrorView.onError(e);
                return null;
            }
        });
        getRecommendProductList.setDeviceID(AppUtil.getAndroidID(mContext));
        getRecommendProductList.setPageIndex(pageIndex);
        getRecommendProductList.setPageSize(pageSize);
        HttpManager.getInstance().doHttpDeal(mContext, getRecommendProductList);
    }


    private UpgradeInfoBean.ReturnDataBean version;
    int REQUEST_EXTERNAL_STORAGE = 1234;
    String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public void getAppUpgradeInfoApi() {
        GetAppUpgradeInfoApi getAppUpgradeInfoApi = new GetAppUpgradeInfoApi(mContext);
        getAppUpgradeInfoApi.setListener(new HttpOnNextListener<UpgradeInfoBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(UpgradeInfoBean upgradeInfoBean) {
                if (upgradeInfoBean.getErrorCode() == 200) {
                    String versionName = CommonUtils.getVersionName(mContext);
                    version = upgradeInfoBean.getReturnData();
                    if (!TextUtils.isEmpty(version.getAppVersion()) && !TextUtils.isEmpty(versionName) && !TextUtils.isEmpty(version.getDownLoadUrl())) {
                        if (version.getAppVersion().compareTo(versionName) > 0) {

                            int permission = ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if (permission != PackageManager.PERMISSION_GRANTED) {
                                builder.builder().show();
                            } else {
                                UpdateManager updateManager = new UpdateManager();
                                updateManager.showDialog(mContext, version, new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        DialogManager.getInstances().isAcceptActivityCouponApi(mContext);
                                    }
                                });
                            }
                        } else {
                            DialogManager.getInstances().isAcceptActivityCouponApi(mContext);
                        }
                    }
                }
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(mContext, getAppUpgradeInfoApi);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户同意，执行操作
                UpdateManager updateManager = new UpdateManager();
                updateManager.showDialog(mContext, version, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        DialogManager.getInstances().isAcceptActivityCouponApi(mContext);

                    }
                });
            } else {
                //用户不同意，向用户展示该权限作用
                builder.builder().show();
            }
        }

    }

    @Override
    public void onEventMainThread(BusEvent event) {
        super.onEventMainThread(event);
        if (event.getType() == BusEvent.LOGIN_SUCCESS || event.getType() == BusEvent.LOGIN_OUT) {
            listBeans.clear();
            currentPage = 1;
            getRecommendProductList(1, pageSize, false);
        }
    }

    public String getPesudoUniqueID() {
        String m_szDevIDShort = "35" + //we make this look like a valid IMEI
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 digits
        return m_szDevIDShort;
    }


}
