package com.kcx.acg.views.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.google.common.collect.Lists;
import com.kcx.acg.R;
import com.kcx.acg.api.AddCommentApi;
import com.kcx.acg.api.AddReplyApi;
import com.kcx.acg.api.AttentionMemberApi;
import com.kcx.acg.api.BuyProductPopTipApi;
import com.kcx.acg.api.BuyProductWithACoinApi;
import com.kcx.acg.api.CommentLikeApi;
import com.kcx.acg.api.FavouriteProductApi;
import com.kcx.acg.api.GetAdvertisementInfoApi;
import com.kcx.acg.api.GetPreviewProductInfoApi;
import com.kcx.acg.api.GetProductCommentsApi;
import com.kcx.acg.base.BusEvent;
import com.kcx.acg.bean.AddCommentBean;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.bean.AddReplyBean;
import com.kcx.acg.bean.AttentionMemberBean;
import com.kcx.acg.bean.BuyProductPopTipBean;
import com.kcx.acg.bean.BuyProductWithACoinBean;
import com.kcx.acg.bean.CommentLikeBean;
import com.kcx.acg.bean.FavouriteProductBean;
import com.kcx.acg.bean.GetAdvertisementInfoBean;
import com.kcx.acg.bean.ProductCommentsBean;
import com.kcx.acg.bean.PreviewBean;
import com.kcx.acg.bean.UserInfoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.manager.AccountManager;
import com.kcx.acg.manager.ShareManager;
import com.kcx.acg.views.adapter.CommentAdapter;
import com.kcx.acg.views.adapter.DetailsAdapter;
import com.kcx.acg.views.adapter.PreviewContentAdapter;
import com.kcx.acg.views.adapter.TitleAdapter;
import com.kcx.acg.views.view.BottomDialog;
import com.kcx.acg.views.view.BottomDialog2;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.LoadingErrorView;
import com.kcx.acg.views.view.TitleBarView;
import com.monke.immerselayout.ImmerseLinearLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 */

public class PreviewActivity extends BaseActivity implements CancelAdapt {

    public static final String KEY_PRODUCT_ID = "key_product_id";

    private View rootView;
    private TitleBarView titleBarView;
    private ImageView backIv;
    private ImageView likeIv;
    private ImageView jumpIv;
    private ImageView shareIv;
    private ImageView optionIv;
    private TextView optionBtn;
    private LinearLayout optionLayout;
    private ImmerseLinearLayout immerseLinearLayout;
    private EditText replyEt;
    private RecyclerView contentRv;
    private SmartRefreshLayout refreshLayout;
    private boolean liked = false;
    private boolean isLogin;
    private int productId;
    private BottomDialog bottomDialog;
    private BottomDialog2.Builder dialogBuilder;
    private LoadingErrorView loadingErrorView;

    private PreviewBean.ReturnDataBean bean;
    private GetAdvertisementInfoBean.ReturnDataBean advInfoBean;
    private ProductCommentsBean.ReturnDataBean productCommentsBean;

    private TitleAdapter titleAdapter, headBottomAdapter, commentHeadAdapter;
    private PreviewContentAdapter previewAdapter;
    private CommentAdapter commentAdapter;
    private DelegateAdapter delegateAdapter;
    private DetailsAdapter advertisAdapter;
    private SingleLayoutHelper singleLayoutHelper, commentHeadLayoutHelper;
    private GridLayoutHelper gridLayoutHelper;
    private LinearLayoutHelper linearLayoutHelper;
    private static final int SIZE_PAGE = 10;
    private int currentPage = 1;
    private List<ProductCommentsBean.ReturnDataBean.ListBean> commentList;
    private BuyProductPopTipBean.ReturnDataBean popTipBean;
    private boolean isFavorited = false;
    private boolean isShowBuyDialog = true;
    private boolean isBlurImg = true;
    private boolean isShare = false;

    @Override
    public View setInitView() {
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_preview, null);
        titleBarView = rootView.findViewById(R.id.preview_back_titlebar_view);
        backIv = titleBarView.getIv_in_title_back();
        contentRv = rootView.findViewById(R.id.preview_content_rv);
        likeIv = rootView.findViewById(R.id.preview_like_iv);
        jumpIv = rootView.findViewById(R.id.preview_jump_iv);
        shareIv = rootView.findViewById(R.id.preview_share_iv);
        optionBtn = rootView.findViewById(R.id.preview_option_btn);
        optionIv = rootView.findViewById(R.id.preview_option_iv);
        optionLayout = rootView.findViewById(R.id.preview_option_layout);
        loadingErrorView = rootView.findViewById(R.id.preview_loading_error);

        refreshLayout = rootView.findViewById(R.id.preview_smart_refresh_layout);
        refreshLayout.setEnableFooterFollowWhenLoadFinished(false);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setEnableAutoLoadMore(true);
//        refreshLayout.setPrimaryColorsId(R.color.white, R.color.gray_999);
//        refreshLayout.finishLoadMoreWithNoMoreData();
//        refreshLayout.autoRefresh();
        refreshLayout.setEnableRefresh(false);
//        refreshLayout.setEnableOverScrollDrag(true);

        contentRv.setHasFixedSize(true);
        contentRv.setNestedScrollingEnabled(false);

        contentRv.setRecycledViewPool(viewPool);
        contentRv.setLayoutManager(layoutManager);
        return rootView;
    }

    @Override
    public void setListener() {
        backIv.setOnClickListener(this);
        likeIv.setOnClickListener(this);
        jumpIv.setOnClickListener(this);
        shareIv.setOnClickListener(this);
        optionBtn.setOnClickListener(this);
        optionLayout.setOnClickListener(this);
        advertisAdapter.setOnAdvertisListener(onAdvertisListener);
        commentHeadAdapter.setOnTextClickListener(onTextClickListener);
        commentAdapter.setOnItemClickListener(onItemClickListener);
//        titleAdapter.setOnTitleClickListener(new TitleAdapter.OnTitleClickListener() {
//            @Override
//            public void onTitleClick(View view) {
//                if (!isLogin) {
//                    startDDMActivity(LoginActivity.class, true);
//                } else {
//                    if (isShowBuyDialog) {
//                        showDialog();
//                    }
//                }
//            }
//        });

        headBottomAdapter.setOnFollowListener(onFollowListener);
        previewAdapter.setOnItemClickListener(new PreviewContentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!isLogin) {
                    startDDMActivity(LoginActivity.class, true);
                } else {
                    if (isShowBuyDialog) {
                        showDialog();
                    } else {
                        Intent intent = new Intent(PreviewActivity.this, WorkDetailsActivity.class);
                        if (bean.getCanViewType() == 3) {
                            intent.putExtra(WorkDetailsActivity.KEY_SHOW_VIP_TOAST, getString(R.string.work_details_toast_msg));
                        }
                        intent.putExtra(KEY_PRODUCT_ID, productId);
                        startDDMActivity(intent, true);
                    }
                }
            }
        });
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getProductComments(productId, currentPage, SIZE_PAGE);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getPreviewProductInfo(productId);
                getAdvertisementInfo(Constants.KEY_ADVERTIS_PREVIEW);
                getProductComments(productId, 1, SIZE_PAGE);
            }
        });
        loadingErrorView.setOnReloadListener(new LoadingErrorView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                getPreviewProductInfo(productId);
            }
        });


    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        productId = intent.getIntExtra(KEY_PRODUCT_ID, -1);

        getPreviewProductInfo(productId);
        getAdvertisementInfo(Constants.KEY_ADVERTIS_PREVIEW);
        getProductComments(productId, 1, SIZE_PAGE);
        if (AccountManager.getInstances().getUserInfo(this) != null)
            buyProductPopTip(productId);

        linearLayoutHelper = new LinearLayoutHelper();

        singleLayoutHelper = new SingleLayoutHelper();
        singleLayoutHelper.setItemCount(1);
        singleLayoutHelper.setBgColor(Color.WHITE);
        singleLayoutHelper.setMarginBottom(20);

        commentHeadLayoutHelper = new SingleLayoutHelper();
        commentHeadLayoutHelper.setItemCount(1);
        commentHeadLayoutHelper.setBgColor(Color.WHITE);

        gridLayoutHelper = new GridLayoutHelper(3, 3);
        gridLayoutHelper.setVGap(15);// 控制子元素之间的垂直间距
        gridLayoutHelper.setHGap(15);// 控制子元素之间的水平间距
        gridLayoutHelper.setPadding(30, 0, 30, 0);
        gridLayoutHelper.setBgColor(ContextCompat.getColor(this, R.color.white));
        gridLayoutHelper.setAutoExpand(false);//是否自动填充空白区域
//        gridLayoutHelper.setWeights(new float[]{33, 33, 33});
//        gridLayoutHelper.setAspectRatio(3);

        commentList = Lists.newArrayList();
        titleAdapter = new TitleAdapter(PreviewActivity.this, singleLayoutHelper, R.layout.preview_title_layout, bean);
        previewAdapter = new PreviewContentAdapter(gridLayoutHelper, PreviewActivity.this, bean, PreviewContentAdapter.FROM_PREVIEW, isBlurImg);
        headBottomAdapter = new TitleAdapter(PreviewActivity.this, singleLayoutHelper, R.layout.preview_head_bottom_layout, bean);
        singleLayoutHelper = new SingleLayoutHelper();
        advertisAdapter = new DetailsAdapter(PreviewActivity.this, singleLayoutHelper, advInfoBean, R.layout.advertising_layout);
        commentHeadAdapter = new TitleAdapter(PreviewActivity.this, commentHeadLayoutHelper, R.layout.preview_comment_head_layout, productCommentsBean);
        commentAdapter = new CommentAdapter(PreviewActivity.this, commentList, linearLayoutHelper, true, 0, 0);

        delegateAdapter = new DelegateAdapter(layoutManager);
        List<DelegateAdapter.Adapter> adapterList = Lists.newArrayList(titleAdapter, previewAdapter, headBottomAdapter, advertisAdapter, commentHeadAdapter, commentAdapter);
        delegateAdapter.addAdapter(titleAdapter);
        delegateAdapter.addAdapter(previewAdapter);
        delegateAdapter.addAdapter(headBottomAdapter);
        delegateAdapter.addAdapter(advertisAdapter);
        delegateAdapter.addAdapter(commentHeadAdapter);
        delegateAdapter.addAdapter(commentAdapter);
        contentRv.setAdapter(delegateAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isLogin = AccountManager.getInstances().isLogin(this, false);
        if (isShare) {
            ShareManager.getInstances().shareSuccess(this);
        }
    }


//        if (!isLogin) {
//            optionIv.setVisibility(View.GONE);
//            optionBtn.setText(getString(R.string.preview_login_msg));
//            optionLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.pink_ff8));
//        } else {
//            if (bean != null && bean.getProduct().getACoinPrice() > 0) {
//                optionBtn.setText(getStringByCoin(bean.getProduct().getACoinPrice()));
//                optionIv.setVisibility(View.VISIBLE);
//                optionLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.pink_ff8));
//            } else {
//                optionIv.setVisibility(View.GONE);
//                optionLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_6bb));
//                optionBtn.setText(getString(R.string.preview_watch_msg));
//            }
//        }
//}

    public void getPreviewProductInfo(final int productId) {
        GetPreviewProductInfoApi getPreviewProductInfoApi = new GetPreviewProductInfoApi(this);
        getPreviewProductInfoApi.setProductId(productId);
        getPreviewProductInfoApi.setListener(new HttpOnNextListener<PreviewBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(PreviewBean previewBean) {
                refreshLayout.finishRefresh();
                optionLayout.setClickable(true);
                bean = previewBean.getReturnData();
                titleAdapter.setReturnDataBean(bean);
                titleAdapter.setLabels(bean.getTagList());
                titleAdapter.notifyDataSetChanged();

                previewAdapter.setReturnDataBean(bean);
                previewAdapter.notifyDataSetChanged();

                headBottomAdapter.setReturnDataBean(bean);
                headBottomAdapter.notifyDataSetChanged();
                isLogin = AccountManager.getInstances().isLogin(PreviewActivity.this, false);

                if (!isLogin) {
                    optionIv.setVisibility(View.GONE);
                    optionBtn.setText(getString(R.string.preview_login_msg));
                    optionLayout.setBackgroundColor(ContextCompat.getColor(PreviewActivity.this, R.color.pink_ff8));
                } else {
                    switch (bean.getCanViewType()) {
                        case Constants.Works.CANT_WATCH:
                            if (bean.getProduct().getACoinPrice() > 0) {
                                isShowBuyDialog = true;
                                optionBtn.setText(getStringByCoin(bean.getProduct().getACoinPrice()));
                                optionIv.setVisibility(View.VISIBLE);
                                optionLayout.setBackgroundResource(R.color.pink_ff8);
                            } else {
                                isShowBuyDialog = false;
                            }
                            break;
                        case Constants.Works.VIP_WATCH:
                        case Constants.Works.FREE_WATCH:
                        case Constants.Works.PAY_WATCH:
                        case Constants.Works.CANT_WATCH_OF_ORIGINAL:
                            isShowBuyDialog = false;
                            optionIv.setVisibility(View.GONE);
                            optionLayout.setBackgroundColor(ContextCompat.getColor(PreviewActivity.this, R.color.blue_6bb));
                            optionBtn.setText(getString(R.string.preview_watch_msg));
                            break;
                    }

                }

                isFavorited = bean.isIsFavourited();
                if (isFavorited) {
                    likeIv.setImageResource(R.drawable.like_checked_icon);
                } else {
                    likeIv.setImageResource(R.drawable.like_unchecked_icon);
                }
                return null;
            }

            @Override
            public RetryWhenNetworkException.Wrapper onError(Throwable e) {
                loadingErrorView.onError(e);
                return null;
            }
        });
        HttpManager.getInstance().doHttpDeal(this, getPreviewProductInfoApi);
    }

    public void addComment(final int productID, String content) {
        AddCommentApi addCommentApi = new AddCommentApi(this);
        addCommentApi.setProductID(productID);
        addCommentApi.setContent(content);
        addCommentApi.setListener(new HttpOnNextListener<AddCommentBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(AddCommentBean addCommentBean) {
                if (addCommentBean.isReturnData()) {
                    getProductComments(productID, 1, SIZE_PAGE);
                } else {
                    CustomToast.showToast(addCommentBean.getErrorMsg());
                }
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(this, addCommentApi);
    }

    public void getProductComments(int productID, final int pageIndex, int pageSize) {
        GetProductCommentsApi getProductCommentsApi = new GetProductCommentsApi(this);
        getProductCommentsApi.setProductID(productID);
        getProductCommentsApi.setPageIndex(pageIndex);
        getProductCommentsApi.setPageSize(pageSize);
        getProductCommentsApi.setListener(new HttpOnNextListener<ProductCommentsBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(ProductCommentsBean productBean) {
                optionBtn.setEnabled(true);
                if (pageIndex == 1) {
                    commentList.clear();
                }
                commentList.addAll(productBean.getReturnData().getList());
                commentAdapter.notifyDataSetChanged();
                productCommentsBean = productBean.getReturnData();
                commentHeadAdapter.setReturnDataBeanOfProductComments(productCommentsBean);
                commentHeadAdapter.notifyDataSetChanged();
                if (commentList.size() >= productBean.getReturnData().getTotal()) {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    refreshLayout.finishLoadMore();
                    currentPage++;
                }
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(this, getProductCommentsApi);
    }

    public void commentLike(int commentID, final int type, final int position) {
        CommentLikeApi commentLikeApi = new CommentLikeApi(this);
        commentLikeApi.setCommentID(commentID);
        commentLikeApi.setType(type);
        commentLikeApi.setListener(new HttpOnNextListener<CommentLikeBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(CommentLikeBean commentLikeBean) {
                commentList.get(position).setLikeCount(commentLikeBean.getReturnData().getLikeCount());
                commentList.get(position).setIsLike(type == 1 ? true : false);
                commentAdapter.notifyDataSetChanged();
                return null;
            }

        });

        HttpManager.getInstance().doHttpDeal(this, commentLikeApi);
    }

    public void buyProductPopTip(int productID) {
        BuyProductPopTipApi buyProductPopTipApi = new BuyProductPopTipApi(this);
        buyProductPopTipApi.setProductID(productID);
        buyProductPopTipApi.setListener(new HttpOnNextListener<BuyProductPopTipBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(BuyProductPopTipBean buyProductPopTipBean) {
                popTipBean = buyProductPopTipBean.getReturnData();
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(this, buyProductPopTipApi);
    }

    public void getAdvertisementInfo(int locationID) {
        GetAdvertisementInfoApi getAdvertisementInfoApi = new GetAdvertisementInfoApi(this);
        getAdvertisementInfoApi.setLocationID(locationID);
        getAdvertisementInfoApi.setListener(new HttpOnNextListener<GetAdvertisementInfoBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(GetAdvertisementInfoBean getAdvertisementInfoBean) {
                if (getAdvertisementInfoBean.getReturnData() != null)

                    if (getAdvertisementInfoBean.getErrorCode() == 200) {
                        advInfoBean = getAdvertisementInfoBean.getReturnData();
                        advertisAdapter.setAdvertisBean(advInfoBean);
                        advertisAdapter.notifyDataSetChanged();
                    }
                return null;
            }

        });

        HttpManager.getInstance().doHttpDeal(this, getAdvertisementInfoApi);
    }

    public void favouriteProduct(int productID, int type) {
        FavouriteProductApi favouriteProductApi = new FavouriteProductApi(this);
        favouriteProductApi.setProductID(productID);
        favouriteProductApi.setType(type);
        favouriteProductApi.setListener(new HttpOnNextListener<FavouriteProductBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(FavouriteProductBean favouriteProductBean) {
                if (favouriteProductBean.getErrorCode() == 200) {
                    isFavorited = !isFavorited;
                    if (isFavorited) {
                        likeIv.setImageResource(R.drawable.like_checked_icon);
                    } else {
                        likeIv.setImageResource(R.drawable.like_unchecked_icon);
                    }
                } else {
                }
                CustomToast.showToast(favouriteProductBean.getErrorMsg());
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(this, favouriteProductApi);
    }


    public void attentionMember(int memberID, final int type) {
        boolean isLogin = AccountManager.getInstances().isLogin(this, true);
        if (!isLogin) {
            headBottomAdapter.setOnInnerUnFinish(type);
            return;
        }

        AttentionMemberApi attentionMemberApi = new AttentionMemberApi(this);
        attentionMemberApi.setMemberID(memberID);
        attentionMemberApi.setType(type);
        attentionMemberApi.setListener(new HttpOnNextListener<AttentionMemberBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(AttentionMemberBean attentionMemberBean) {
                if (attentionMemberBean.getErrorCode() == 200) {
                    headBottomAdapter.setIsAttentionedAuthor(type == 1 ? true : false);
                    EventBus.getDefault().post(new BusEvent(BusEvent.ATTENTION_MEMBER, type == 1 ? true : false));

                }
                CustomToast.showToast(attentionMemberBean.getErrorMsg());
                return null;
            }
        });

        HttpManager.getInstance().doHttpDeal(this, attentionMemberApi);
    }

    public void buyProductWithACoin(int productID) {
        BuyProductWithACoinApi buyProductWithACoinApi = new BuyProductWithACoinApi(this);
        buyProductWithACoinApi.setProductID(productID);
        buyProductWithACoinApi.setListener(new HttpOnNextListener<BuyProductWithACoinBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(BuyProductWithACoinBean buyProductWithACoinBean) {
                if (buyProductWithACoinBean.getErrorCode() == 200) {
                    isBlurImg = false;
                    dialogBuilder.dismiss();
                    previewAdapter.setBlurImg(false);
                    previewAdapter.notifyDataSetChanged();
                    isShowBuyDialog = false;
                    optionBtn.setText(getString(R.string.preview_watch_msg));
                    optionIv.setVisibility(View.GONE);
                    optionLayout.setBackgroundResource(R.color.blue_6bb);
                }
                CustomToast.showToast(buyProductWithACoinBean.getErrorMsg());
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(this, buyProductWithACoinApi);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_in_title_back:
                finish();
                break;
            case R.id.preview_like_iv:
                if (!isLogin) {
                    startDDMActivity(LoginActivity.class, true);
                    return;
                }
                favouriteProduct(productId, isFavorited ? 0 : 1);
                break;
            case R.id.preview_jump_iv:
//                layoutManager.scrollToPositionWithOffset(8, 0);
                showInputDialog(true, 0, "");
                break;
            case R.id.preview_share_iv:
                ShareManager.getInstances().share(1, productId, PreviewActivity.this);
//                BottomDialog2.Builder builder = new BottomDialog2.Builder(this);
//                builder.showShare().show();
//                builder.setOnItemClickListener(new BottomDialog2.Builder.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//
//                    }
//                });
                break;
            case R.id.preview_option_layout:
            case R.id.preview_option_btn:
                if (!isLogin) {
                    startDDMActivity(LoginActivity.class, true);
                } else {
                    if (isShowBuyDialog) {
                        showDialog();
                    } else {
                        Intent intent = new Intent(PreviewActivity.this, WorkDetailsActivity.class);
                        if (bean.getCanViewType() == 3) {
                            intent.putExtra(WorkDetailsActivity.KEY_SHOW_VIP_TOAST, getString(R.string.work_details_toast_msg));
                        }
                        if (bean.getCanViewType() == Constants.Works.CANT_WATCH_OF_ORIGINAL) {
                            intent.putExtra(WorkDetailsActivity.KEY_SHOW_FOLLOW, true);
                        }
                        intent.putExtra(KEY_PRODUCT_ID, productId);
                        startDDMActivity(intent, true);
                    }
                }
                break;
        }
    }

    public void showDialog() {
        dialogBuilder = new BottomDialog2.Builder(this);
        dialogBuilder.setLayoutId(R.layout.dialog_buy_works_layout);
        UserInfoBean userInfo = AccountManager.getInstances().getUserInfo(this);
        int accountStatus = AccountManager.getInstances().getUserInfo(this).getReturnData().getAccountStatus();
        switch (userInfo.getReturnData().getUserLevel()) {
            case Constants.UserInfo.ORDINARY_MEMBER:
            case Constants.UserInfo.VIP_EXPIRED:
                if (accountStatus == Constants.UserInfo.AccountStatus.ACOIN_FREEZE ||
                        accountStatus == Constants.UserInfo.AccountStatus.ACOIN_FREEZE_AND_DISABLE_SPEECH ||
                        accountStatus == Constants.UserInfo.AccountStatus.ACCOUNT_BANNED_AND_ACOIN_FREEZE ||
                        accountStatus == Constants.UserInfo.AccountStatus.ACCOUNT_BANNED_AND_DISABLE_SPEECH_AND_ACOIN_FREEZE) {
                    if (popTipBean.getSourceType() == 1) {
                        dialogBuilder.showWhat(BottomDialog2.SHOW_FREEZE_ACOIN, popTipBean.isIsShared(), userInfo.getReturnData().isIsHaveCharged(), getString(R.string.dialog_freeze_coin_txt_msg)).show();
                    } else {
                        dialogBuilder.showWhat(BottomDialog2.SHOW_FREEZE_ACOIN, popTipBean.isIsShared(), userInfo.getReturnData().isIsHaveCharged(), popTipBean.getSourceType(), popTipBean.isIsDisCount(), getString(R.string.dialog_freeze_coin_txt_msg)).show();
                    }
                } else {
                    if (popTipBean.getSourceType() == 1) {
                        if (popTipBean.getTotalACoin() >= popTipBean.getDisCountProductPrice()) {
                            dialogBuilder.showWhat(BottomDialog2.SHOW_BUY_WITH_ACOIN,
                                    popTipBean.isIsShared(),
                                    userInfo.getReturnData().isIsHaveCharged(),
                                    getString(R.string.buy_of_coin_dialog_btn_msg,
                                            getStringByCoin(bean.getProduct().getACoinPrice())),
                                    getString(R.string.buy_of_coin_dialog_balance_msg, getStringByCoin(popTipBean.getTotalACoin()))).show();
                        } else if (popTipBean.getTotalACoin() < popTipBean.getDisCountProductPrice()) {
                            dialogBuilder.showWhat(BottomDialog2.SHOW_INSUFFICIENT_ACOIN,
                                    popTipBean.isIsShared(),
                                    userInfo.getReturnData().isIsHaveCharged(),
                                    getString(R.string.buy_of_coin_dialog_balance_msg, getStringByCoin(popTipBean.getTotalACoin()))).show();
                        }
                    } else {
                        if (popTipBean.getTotalACoin() >= popTipBean.getDisCountProductPrice()) {
                            dialogBuilder.showWhat(BottomDialog2.SHOW_BUY_WITH_ACOIN,
                                    popTipBean.isIsShared(),
                                    userInfo.getReturnData().isIsHaveCharged(),
                                    popTipBean.getSourceType(),
                                    popTipBean.isIsDisCount(),
                                    getString(R.string.buy_of_coin_dialog_btn_msg,
                                            getStringByCoin(bean.getProduct().getACoinPrice())),
                                    getString(R.string.buy_of_coin_dialog_balance_msg, getStringByCoin(popTipBean.getTotalACoin())),
                                    getString(R.string.open_vip_msg, popTipBean.getDiscountRate() * 10)).show();
                        } else if (popTipBean.getTotalACoin() < popTipBean.getDisCountProductPrice()) {
                            dialogBuilder.showWhat(BottomDialog2.SHOW_INSUFFICIENT_ACOIN,
                                    popTipBean.isIsShared(),
                                    userInfo.getReturnData().isIsHaveCharged(),
                                    popTipBean.getSourceType(),
                                    popTipBean.isIsDisCount(),
                                    getString(R.string.buy_of_coin_dialog_balance_msg, getStringByCoin(popTipBean.getTotalACoin())),
                                    getString(R.string.open_vip_msg, (popTipBean.getDiscountRate() * 10))).show();
                        }
                    }

                }
                break;
            case Constants.UserInfo.VIP_MEMBER:
                if (accountStatus == Constants.UserInfo.AccountStatus.ACOIN_FREEZE ||
                        accountStatus == Constants.UserInfo.AccountStatus.ACOIN_FREEZE_AND_DISABLE_SPEECH ||
                        accountStatus == Constants.UserInfo.AccountStatus.ACCOUNT_BANNED_AND_ACOIN_FREEZE ||
                        accountStatus == Constants.UserInfo.AccountStatus.ACCOUNT_BANNED_AND_DISABLE_SPEECH_AND_ACOIN_FREEZE) {
                    if (popTipBean.getSourceType() == 1) {
                        dialogBuilder.showWhat(BottomDialog2.SHOW_VIP_FREEZE_ACOIN, popTipBean.isIsShared(), userInfo.getReturnData().isIsHaveCharged(), getString(R.string.dialog_freeze_coin_txt_msg)).show();
                    } else {
                        dialogBuilder.showWhat(BottomDialog2.SHOW_VIP_FREEZE_ACOIN, popTipBean.isIsShared(), userInfo.getReturnData().isIsHaveCharged(), popTipBean.getSourceType(), popTipBean.isIsDisCount(), getString(R.string.dialog_freeze_coin_txt_msg)).show();
                    }
                } else {
                    if (popTipBean.getSourceType() == 1) {
                        if (popTipBean.getTotalACoin() >= popTipBean.getDisCountProductPrice()) {
                            dialogBuilder.showWhat(BottomDialog2.SHOW_VIP_BUY_WITH_ACOIN,
                                    popTipBean.isIsShared(),
                                    userInfo.getReturnData().isIsHaveCharged(),
                                    getString(R.string.buy_of_coin_dialog_btn_msg,
                                            getStringByCoin(bean.getProduct().getACoinPrice())),
                                    getString(R.string.buy_of_coin_dialog_balance_msg, getStringByCoin(popTipBean.getTotalACoin()))).show();
                        } else if (popTipBean.getTotalACoin() < popTipBean.getDisCountProductPrice()) {
                            dialogBuilder.showWhat(BottomDialog2.SHOW_VIP_INSUFFICIENT_ACOIN,
                                    popTipBean.isIsShared(),
                                    userInfo.getReturnData().isIsHaveCharged(),
                                    getString(R.string.buy_of_coin_dialog_balance_msg, getStringByCoin(popTipBean.getTotalACoin()))).show();
                        }
                    } else {
                        if (popTipBean.getTotalACoin() >= popTipBean.getDisCountProductPrice()) {
                            dialogBuilder.showWhat(BottomDialog2.SHOW_VIP_BUY_WITH_ACOIN,
                                    popTipBean.isIsShared(),
                                    userInfo.getReturnData().isIsHaveCharged(),
                                    popTipBean.getSourceType(),
                                    popTipBean.isIsDisCount(),
                                    getString(R.string.buy_of_coin_dialog_btn_msg,
                                            getStringByCoin(bean.getProduct().getACoinPrice())),
                                    getString(R.string.buy_of_coin_dialog_balance_msg, getStringByCoin(popTipBean.getTotalACoin())),
                                    getString(R.string.buy_of_coin_dialog_btn_msg2, getStringByCoin(popTipBean.getDisCountProductPrice())),
                                    getStringByCoin(popTipBean.getACoinPrice())).show();
                        } else if (popTipBean.getTotalACoin() < popTipBean.getDisCountProductPrice()) {
                            dialogBuilder.showWhat(BottomDialog2.SHOW_VIP_INSUFFICIENT_ACOIN,
                                    popTipBean.isIsShared(),
                                    userInfo.getReturnData().isIsHaveCharged(),
                                    popTipBean.getSourceType(),
                                    popTipBean.isIsDisCount(),
                                    getString(R.string.buy_of_coin_dialog_balance_msg, getStringByCoin(popTipBean.getTotalACoin()))).show();
                        }
                    }
                }
                break;
        }

        dialogBuilder.setOnBtnClickListener(new BottomDialog2.Builder.OnBtnClickListener() {
            @Override
            public void onBuyWorkClick(View view) {
                buyProductWithACoin(productId);
            }

            @Override
            public void onVIPClick(View view) {
                Intent intent = new Intent(PreviewActivity.this, VipActivity.class);
                startDDMActivity(intent, true);
            }

            @Override
            public void onVIP2Click(View view) {

            }

            @Override
            public void onChargeClick(View view) {
                Intent intent = new Intent(PreviewActivity.this, ToUpACoinActivity.class);
                startDDMActivity(intent, true);
            }

            @Override
            public void onCallMeClick(View view) {

            }

            @Override
            public void onCantBuyClick(View view) {

            }

            @Override
            public void onShareClick(View view) {
                isShare = true;
                ShareManager.getInstances().share(1, productId, PreviewActivity.this);
            }
        });
    }

    @Override
    public void onEventMainThread(BusEvent event) {
        super.onEventMainThread(event);
        if (event.getType() == BusEvent.LOGIN_SUCCESS) {
            buyProductPopTip(productId);
            getPreviewProductInfo(productId);
            getAdvertisementInfo(Constants.KEY_ADVERTIS_PREVIEW);
            getProductComments(productId, 1, SIZE_PAGE);
        }
        if (event.getType() == BusEvent.SHARE) {
            buyProductPopTip(productId);
            Intent intent = new Intent(this, WebViewActivity.class);
            String url = AccountManager.getInstances().getUserInfo(this).getReturnData().getH5Link_Lottery();
            intent.putExtra("classType", 1);
            intent.putExtra("url", url);
            startDDMActivity(intent, true);
            isShare = false;
        }
    }

    public void addReply(int commentID, String content) {
        AddReplyApi addReplyApi = new AddReplyApi(this);
        addReplyApi.setCommentID(commentID);
        addReplyApi.setContent(content);
        addReplyApi.setListener(new HttpOnNextListener<AddReplyBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(AddReplyBean addReplyBean) {
                if (addReplyBean.isReturnData()) {
                    getProductComments(productId, 1, SIZE_PAGE);
                } else {
                    CustomToast.showToast(addReplyBean.getErrorMsg());
                }
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(this, addReplyApi);

    }

    public void showInputDialog(final boolean commentOrReply, final int commentId, String commentUserName) {
        boolean isLogin = AccountManager.getInstances().isLogin(PreviewActivity.this, true);
        if (!isLogin) {
            return;
        }
        bottomDialog = new BottomDialog(PreviewActivity.this, R.style.ActionSheetDialogStyle);
        bottomDialog.show();
        if (!commentOrReply) {
            bottomDialog.setReplyHint(getString(R.string.comment_reply_hint_msg, commentUserName));
        }
        int accountStatus = AccountManager.getInstances().getUserInfo(this).getReturnData().getAccountStatus();
        if (accountStatus == Constants.UserInfo.AccountStatus.SUSPEND ||
                accountStatus == Constants.UserInfo.AccountStatus.DISABLE_SPEECH ||
                accountStatus == Constants.UserInfo.AccountStatus.ACOIN_FREEZE_AND_DISABLE_SPEECH ||
                accountStatus == Constants.UserInfo.AccountStatus.ACCOUNT_BANNED_AND_DISABLE_SPEECH_AND_ACOIN_FREEZE ||
                accountStatus == Constants.UserInfo.AccountStatus.ACCOUNT_BANNED_AND_DISABLE_SPEECH) {
            bottomDialog.setDisable();
        }
        bottomDialog.setCommitListener(new BottomDialog.OnCommitListener() {
            @Override
            public void onCommit(String content) {

                if (commentOrReply) {
                    addComment(productId, content);
                } else {
                    addReply(commentId, content);
                }
                bottomDialog.dismiss();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showInputMethod();
            }
        }, 100);
    }

    private CommentAdapter.OnItemClickListener onItemClickListener = new CommentAdapter.OnItemClickListener() {
        @Override
        public void onReplyClick(View view, int position) {

            Intent intent = new Intent(PreviewActivity.this, ReplyActivitry.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ReplyActivitry.KEY_COMMENT, commentList.get(position));
            intent.putExtras(bundle);
            startDDMActivity(intent, true);
        }

        @Override
        public void onLikeClick(View view, int position, int commentID, boolean isChecked) {
            boolean isLogin = AccountManager.getInstances().isLogin(PreviewActivity.this, true);
            if (!isLogin) {
                return;
            }
            commentLike(commentID, isChecked ? 1 : 0, position);
        }

        @Override
        public void onTextClick(View view, int position) {
            showInputDialog(false, commentList.get(position).getCommentID(), commentList.get(position).getCommentUser().getUserName());
        }
    };

    private TitleAdapter.OnTextClickListener onTextClickListener = new TitleAdapter.OnTextClickListener() {
        @Override
        public void onClick(View view) {
            showInputDialog(true, 0, "");
        }
    };

    private TitleAdapter.OnFollowListener onFollowListener = new TitleAdapter.OnFollowListener() {
        @Override
        public void onFollow(View view, final int memberID, final boolean isAttentionedAuthor) {
            if (!isAttentionedAuthor) {
                attentionMember(memberID, isAttentionedAuthor ? 0 : 1);
            } else {
                BottomDialog2.Builder builder = new BottomDialog2.Builder(PreviewActivity.this);
                builder.setLayoutId(R.layout.dialog_unfollow_layout).show();
                builder.setOnConfirmListener(new BottomDialog2.Builder.OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        attentionMember(memberID, isAttentionedAuthor ? 0 : 1);
                    }
                });
            }
        }

    };

    private DetailsAdapter.OnAdvertisListener onAdvertisListener = new DetailsAdapter.OnAdvertisListener() {
        @Override
        public void onAdvertis(View view, String url) {
            Intent intent = new Intent(PreviewActivity.this, AdvertisActivity.class);
            intent.putExtra(AdvertisActivity.KEY_ADVERTIS_URL, url);
            startDDMActivity(intent, true);
        }
    };
}
