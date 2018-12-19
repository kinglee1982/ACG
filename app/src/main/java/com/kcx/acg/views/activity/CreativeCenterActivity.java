package com.kcx.acg.views.activity;

import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.blankj.utilcode.util.LogUtils;
import com.google.common.collect.Lists;
import com.kcx.acg.R;
import com.kcx.acg.api.DelProductByIDApi;
import com.kcx.acg.api.GetAdvertisementInfoApi;
import com.kcx.acg.api.GetProductListApi;
import com.kcx.acg.api.SearchHotTagApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.bean.DelProductByIDBean;
import com.kcx.acg.bean.GetAdvertisementInfoBean;
import com.kcx.acg.bean.GetProductListBean;
import com.kcx.acg.bean.HotTagBean;
import com.kcx.acg.bean.UserInfoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.manager.AccountManager;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.views.adapter.CCHeadAdapter;
import com.kcx.acg.views.adapter.CCItemAdapter;
import com.kcx.acg.views.adapter.CCTitleAdapter;
import com.kcx.acg.views.view.BottomDialog2;
import com.kcx.acg.views.view.CreativeDialog;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.HomeDialog;
import com.kcx.acg.views.view.LoadingErrorView;
import com.kcx.acg.views.view.TitleBarView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;
import retrofit2.http.HEAD;

public class CreativeCenterActivity extends BaseActivity implements CancelAdapt {

    private static final int STATUS_ALLS = -1;
    public static final int STATUS_REVIEW = 0;
    public static final int STATUS_PASSED = 1;
    public static final int STATUS_UNPASS = 2;

    private static final int PAGE_SIZE = 10;
    private static int pageIndex = 1;

    private View rootView;
    private TitleBarView titleBarView;
    private ImageView backIv;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ImageView floatBtn;
    private LoadingErrorView loadingErrorView;
    private BottomDialog2.Builder ceritficationDialog, deleteDialog;
    private CCItemAdapter itemAdapter;
    private CCHeadAdapter headAdapter;
    private DelegateAdapter delegateAdapter;
    private List<GetProductListBean.ReturnDataBean.ListBean> productList;
    private int auditStatus = STATUS_ALLS;
    private UserInfoBean userInfoBean;

    @Override
    public View setInitView() {
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_creative_center, null);
        titleBarView = rootView.findViewById(R.id.creative_center_titlebar);
        backIv = titleBarView.getIv_in_title_back();
        floatBtn = rootView.findViewById(R.id.creative_center_float_btn);
        refreshLayout = rootView.findViewById(R.id.creative_refresh_layout);
        recyclerView = rootView.findViewById(R.id.creative_recycle_view);
        setRecycledViewPool(recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        refreshLayout.setEnableRefresh(false);
        return rootView;
    }

    @Override
    public void initView() {
        userInfoBean = AppUtil.getGson().fromJson(SPUtil.getString(CreativeCenterActivity.this, Constants.USER_INFO, ""), UserInfoBean.class);

        productList = Lists.newArrayList();
        SingleLayoutHelper singleLayoutHelper = new SingleLayoutHelper();
        CCTitleAdapter titleAdapter = new CCTitleAdapter(this, singleLayoutHelper);

        StickyLayoutHelper stickyLayoutHelper = new StickyLayoutHelper(true);
        headAdapter = new CCHeadAdapter(this, stickyLayoutHelper);

        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        itemAdapter = new CCItemAdapter(this, linearLayoutHelper, productList);

        delegateAdapter = new DelegateAdapter(layoutManager);
        delegateAdapter.addAdapter(titleAdapter);
        delegateAdapter.addAdapter(headAdapter);
        delegateAdapter.addAdapter(itemAdapter);
        recyclerView.setAdapter(delegateAdapter);
    }

    @Override
    public void initData() {
        pageIndex = 1;
        getProductList(STATUS_ALLS, pageIndex, PAGE_SIZE);
        //         = (boolean) SPUtil.get(this, Constants.KEY_SHOW_CALL_FOR_PAPERS_DIALOG, true);
        if (Constants.isShowDialog) {
            getAdvertisementInfo(Constants.KEY_ADVERTIS_DIALOG_CALL_FOR_PAPERS);
        }

        getHotTag();
    }

    @Override
    public void setListener() {
        backIv.setOnClickListener(this);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getProductList(auditStatus, pageIndex, PAGE_SIZE);
            }
        });

        itemAdapter.setOnDeleteListener(new CCItemAdapter.OnDeleteListener() {
            @Override
            public void onDelete(final int position) {
                deleteDialog = new BottomDialog2.Builder(CreativeCenterActivity.this);
                deleteDialog.setLayoutId(R.layout.dialog_creative_center_delete_layout).show();
                deleteDialog.setOnConfirmListener(new BottomDialog2.Builder.OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        delProductByIDApi(position);
                    }
                });
            }
        });
        headAdapter.setOnCheckedChangeListener(new CCHeadAdapter.OnCheckedChangeListener() {
            @Override
            public void onCheckedChange(int checkedId) {
                switch (checkedId) {
                    case R.id.creative_center_head_alls_rb:
                        auditStatus = STATUS_ALLS;
                        break;
                    case R.id.creative_center_head_review_rb:
                        auditStatus = STATUS_REVIEW;
                        break;
                    case R.id.creative_center_head_passed_rb:
                        auditStatus = STATUS_PASSED;
                        break;
                    case R.id.creative_center_head_unpass_rb:
                        auditStatus = STATUS_UNPASS;
                        break;
                    case R.id.creative_center_head_draft_rb:
                        break;
                }
                //                productList.clear();
                headAdapter.setEnable(false);
                getProductList(auditStatus, 1, PAGE_SIZE);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    animOut(floatBtn);
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    animIn(floatBtn);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        //投稿
        floatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoBean.ReturnDataBean userInfo = AccountManager.getInstances().getUserInfo(CreativeCenterActivity.this).getReturnData();
                if (userInfo.getAuthenticateStatus() != Constants.UserInfo.STATUS_USER_AUTHENTICATE_PASSED) {
                    ceritficationDialog = new BottomDialog2.Builder(CreativeCenterActivity.this);
                    ceritficationDialog.setLayoutId(R.layout.dialog_certification_layout).setCancelable(false);
                    ceritficationDialog.show();
                    ceritficationDialog.setOnConfirmListener(new BottomDialog2.Builder.OnConfirmListener() {
                        @Override
                        public void onConfirm() {
                            startDDMActivity(OriginalPlanActivity.class, true);
                        }
                    });
                    ceritficationDialog.setOnCancelListener(new BottomDialog2.Builder.OnCancelListener() {
                        @Override
                        public void onCancel() {
                            finish();
                        }
                    });
                } else {
                    int accountStatus = userInfoBean.getReturnData().getAccountStatus();
                    if (accountStatus == 5 || accountStatus == 7 || accountStatus == 8 || accountStatus == 9) {
                        CustomToast.showToast(getString(R.string.toast_fengjin));
                    } else {
                        startDDMActivity(ContributeActivity.class, true);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_in_title_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        Constants.isShowDialog = false;
        super.onDestroy();
    }

    public void animIn(View view) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f);
        anim.setDuration(500);// 动画持续时间
        anim.start();
    }

    public void animOut(View view) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.0f);
        anim.setDuration(500);// 动画持续时间
        anim.start();
    }

    public void getProductList(int auditStatus, final int index, int pageSize) {
        GetProductListApi getProductListApi = new GetProductListApi(this);
        getProductListApi.setAuditStatus(auditStatus);
        getProductListApi.setPageIndex(index);
        getProductListApi.setPageSize(pageSize);
        getProductListApi.setListener(new HttpOnNextListener<GetProductListBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(GetProductListBean getProductListBean) {
                if (getProductListBean.getErrorCode() == 200) {
                    if (index == 1) {
                        productList.clear();
                    }
                    headAdapter.setEnable(true);
                    productList.addAll(getProductListBean.getReturnData().getList());
                    itemAdapter.notifyDataSetChanged();
                    delegateAdapter.notifyDataSetChanged();
                    if (productList.size() >= getProductListBean.getReturnData().getTotal()) {
                        refreshLayout.finishLoadMoreWithNoMoreData();
                    } else {
                        refreshLayout.finishLoadMore();
                        pageIndex++;
                    }

                } else {
                    CustomToast.showToast(getProductListBean.getErrorMsg());
                }
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(this, getProductListApi);
    }

    public void delProductByIDApi(final int position) {
        final DelProductByIDApi delProductByIDApi = new DelProductByIDApi(this);
        delProductByIDApi.setProductID(productList.get(position).getId());
        delProductByIDApi.setListener(new HttpOnNextListener<DelProductByIDBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(DelProductByIDBean delProductByIDBean) {
                if (delProductByIDBean.getErrorCode() == 200) {
                    productList.remove(position);
                    itemAdapter.notifyDataSetChanged();
                    deleteDialog.dismiss();
                } else {
                    CustomToast.showToast(delProductByIDBean.getErrorMsg());
                }
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(this, delProductByIDApi);
    }


    public void getAdvertisementInfo(int locationID) {
        GetAdvertisementInfoApi getAdvertisementInfoApi = new GetAdvertisementInfoApi(this);
        getAdvertisementInfoApi.setLocationID(locationID);
        getAdvertisementInfoApi.setListener(new HttpOnNextListener<GetAdvertisementInfoBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(final GetAdvertisementInfoBean getAdvertisementInfoBean) {
                if (getAdvertisementInfoBean.getReturnData() != null)

                    if (getAdvertisementInfoBean.getErrorCode() == 200) {
                        HomeDialog.Builder builder = new HomeDialog.Builder(CreativeCenterActivity.this);
//                        builder.setLayoutId(R.layout.dialog_creative_layout).setWebUrl("http://192.168.1.123/common/tougaoTip.html").builder().show();
                        builder.setLayoutId(R.layout.dialog_creative_layout).setWebUrl(getAdvertisementInfoBean.getReturnData().getUrl()).builder().show();
                    }

                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(this, getAdvertisementInfoApi);
    }


    private void getHotTag() {
        SearchHotTagApi searchHotTagApi = new SearchHotTagApi(this);
        searchHotTagApi.setPageIndex(1);
        searchHotTagApi.setPageSize(100);
        searchHotTagApi.setListener(new HttpOnNextListener<HotTagBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(HotTagBean hotTagBean) {
                if (hotTagBean.getErrorCode() == 200) {
                    SPUtil.saveObject(CreativeCenterActivity.this, Constants.HOT_TAG, hotTagBean.getReturnData().getList());
                }
                return null;
            }
        });
        HttpManager.getInstance().doHttpDeal(this, searchHotTagApi);
    }


}
