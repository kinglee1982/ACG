package com.kcx.acg.views.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.common.collect.Lists;
import com.kcx.acg.R;
import com.kcx.acg.api.AddCommentApi;
import com.kcx.acg.api.AddReplyApi;
import com.kcx.acg.api.AttentionMemberApi;
import com.kcx.acg.api.CommentLikeApi;
import com.kcx.acg.api.FavouriteProductApi;
import com.kcx.acg.api.GetAdvertisementInfoApi;
import com.kcx.acg.api.GetProductCommentsApi;
import com.kcx.acg.api.GetProductInfoApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.base.BusEvent;
import com.kcx.acg.base.SysApplication;
import com.kcx.acg.bean.AddCommentBean;
import com.kcx.acg.bean.AddReplyBean;
import com.kcx.acg.bean.AttentionMemberBean;
import com.kcx.acg.bean.CommentLikeBean;
import com.kcx.acg.bean.FavouriteProductBean;
import com.kcx.acg.bean.GetAdvertisementInfoBean;
import com.kcx.acg.bean.GetProductInfoBean;
import com.kcx.acg.bean.ProductCommentsBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.manager.AccountManager;
import com.kcx.acg.manager.ShareManager;
import com.kcx.acg.views.adapter.CommentAdapter;
import com.kcx.acg.views.adapter.DetailsAdapter;
import com.kcx.acg.views.adapter.PreviewContentAdapter;
import com.kcx.acg.views.view.BottomDialog;
import com.kcx.acg.views.view.BottomDialog2;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.LoadingErrorView;
import com.kcx.acg.views.view.MyStateButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeoutException;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 */

public class WorkDetailsActivity extends BaseActivity implements CancelAdapt {

    public static final String KEY_SHOW_VIP_TOAST = "key_show_vip_toast";
    public static final String KEY_SHOW_FOLLOW = "key_show_follow";

    private View rootView;
    private TextView favoritedTv;
    private TextView replyTv;
    private TextView forwardTv;
    private ImageView backIv2;
    private RecyclerView contentRv;
    private SmartRefreshLayout refreshLayout;
    private BottomDialog bottomDialog;
    private LinearLayout userLayout;
    private ImageView ceilingHeadIv;
    private ImageView ceilingVipIv;

    private TextView ceilingNameTv;
    private Button ceilingFollowBtn;
    private MyStateButton ceilingFollowStateBtn;
    private LoadingErrorView loadingErrorView;
    private ImageView backIv3;
    private ImageView headIv2;
    private ImageView followVipIv;
    private TextView nameTv2;
    private TextView followTv;
    private LinearLayout followLayout;

    private boolean isFavorited = false;
    private int productID;
    private List<GetProductInfoBean.ReturnDataBean.DetailListBean> videoList;
    private List<GetProductInfoBean.ReturnDataBean.DetailListBean> picList;
    private List<ProductCommentsBean.ReturnDataBean.ListBean> commentList;

    private GetProductInfoBean.ReturnDataBean productInfoBean;
    private GetAdvertisementInfoBean.ReturnDataBean advertisBean;
    private ProductCommentsBean.ReturnDataBean productCommentsBean;
    private DetailsAdapter detailsHeadAdapter, picAdapter, videoAdapter, labelAdapter, commentHeadAdapter, advertisAdapter;
    private CommentAdapter commentAdapter;
    private PreviewContentAdapter previewAdapter;
    private DelegateAdapter delegateAdapter;
    private LinearLayoutHelper linearLayoutHelper;
    private int currentPage = 1;
    private static final int SIZE_PAGE = 10;
    private boolean isAttentionedAuthor;
    private int memberID;

    @Override
    public View setInitView() {
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_works_details, null);
        backIv2 = rootView.findViewById(R.id.work_details_back);
        favoritedTv = rootView.findViewById(R.id.work_details_favorited_tv);
        replyTv = rootView.findViewById(R.id.work_details_reply_tv);
        forwardTv = rootView.findViewById(R.id.work_details_forward_tv);
        contentRv = rootView.findViewById(R.id.work_details_content_rv);
        userLayout = rootView.findViewById(R.id.work_details_user_layout);
        ceilingHeadIv = rootView.findViewById(R.id.work_details_ceiling_head_iv);
        ceilingNameTv = rootView.findViewById(R.id.work_details_ceiling_name_tv);
        ceilingFollowBtn = rootView.findViewById(R.id.work_details_ceiling_follow_btn);
        ceilingFollowStateBtn = rootView.findViewById(R.id.work_details_ceiling_follow_state_btn);
        loadingErrorView = rootView.findViewById(R.id.work_details_loading_error_view);
        backIv3 = rootView.findViewById(R.id.work_details_back_iv3);
        headIv2 = rootView.findViewById(R.id.work_details_head_iv2);
        nameTv2 = rootView.findViewById(R.id.work_details_name_tv2);
        followTv = rootView.findViewById(R.id.work_details_follow_tv);
        followLayout = rootView.findViewById(R.id.work_details_follow_layout);
        ceilingVipIv = rootView.findViewById(R.id.work_details_ceiling_vip_iv);
        followVipIv = rootView.findViewById(R.id.work_details_follow_vip_iv);

        refreshLayout = rootView.findViewById(R.id.work_details_smart_refresh_layout);
        refreshLayout.setHeaderHeight(SysApplication.mHeightPixels);
        refreshLayout.setDragRate(0.5f);
        refreshLayout.setReboundDuration(100);
        refreshLayout.setEnableOverScrollDrag(true);
        contentRv.setRecycledViewPool(viewPool);
        contentRv.setLayoutManager(layoutManager);
        return rootView;
    }

    @Override
    public void initData() {
        String toast = getIntent().getStringExtra(KEY_SHOW_VIP_TOAST);
        if (toast != null) {
            if (!toast.isEmpty()) {
                CustomToast.showToast(toast);
            }
        }
        boolean showFollow = getIntent().getBooleanExtra(KEY_SHOW_FOLLOW, false);
        if (showFollow) {
            followLayout.setVisibility(View.VISIBLE);
        }
        productID = getIntent().getIntExtra(PreviewActivity.KEY_PRODUCT_ID, 0);
        getProductInfo(productID);
        getProductComments(productID, currentPage, SIZE_PAGE, false);
        getAdvertisementInfo(Constants.KEY_ADVERTIS_WORK_DETILS);


        SingleLayoutHelper singleLayoutHelper = new SingleLayoutHelper();
        detailsHeadAdapter = new DetailsAdapter(this, singleLayoutHelper, productInfoBean, R.layout.work_details_head_layout);

        labelAdapter = new DetailsAdapter(this, singleLayoutHelper, productInfoBean, R.layout.work_details_label_layout);
        commentHeadAdapter = new DetailsAdapter(this, singleLayoutHelper, productCommentsBean, R.layout.preview_comment_head_layout);
        advertisAdapter = new DetailsAdapter(this, singleLayoutHelper, advertisBean, R.layout.advertising_layout);

        linearLayoutHelper = new LinearLayoutHelper();
        picAdapter = new DetailsAdapter(this, linearLayoutHelper, picList, R.layout.work_details_pic_item_layout);
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(4);
        gridLayoutHelper.setVGap(6);// 控制子元素之间的垂直间距
        gridLayoutHelper.setHGap(6);// 控制子元素之间的水平间距
        gridLayoutHelper.setPadding(10, 0, 10, 0);
        gridLayoutHelper.setBgColor(ContextCompat.getColor(this, R.color.white));
        gridLayoutHelper.setAutoExpand(false);//是否自动填充空白区域
        previewAdapter = new PreviewContentAdapter(gridLayoutHelper, this, productInfoBean, PreviewContentAdapter.FROM_DETAILS);

        videoList = Lists.newArrayList();
        picList = Lists.newArrayList();
        linearLayoutHelper = new LinearLayoutHelper();
        videoAdapter = new DetailsAdapter(this, linearLayoutHelper, R.layout.work_details_video_item_layout, videoList);

        commentList = Lists.newArrayList();
        linearLayoutHelper = new LinearLayoutHelper();
        commentAdapter = new CommentAdapter(WorkDetailsActivity.this, commentList, linearLayoutHelper, true, 0, 0);

        delegateAdapter = new DelegateAdapter(layoutManager);
        delegateAdapter.addAdapter(detailsHeadAdapter);
        delegateAdapter.addAdapter(picAdapter);
        delegateAdapter.addAdapter(previewAdapter);
        delegateAdapter.addAdapter(videoAdapter);
        delegateAdapter.addAdapter(labelAdapter);
        delegateAdapter.addAdapter(advertisAdapter);
        delegateAdapter.addAdapter(commentHeadAdapter);
        delegateAdapter.addAdapter(commentAdapter);
        contentRv.setAdapter(delegateAdapter);
    }

    @Override
    public void setListener() {
        ceilingFollowStateBtn.setOnInnerClickeListener(new MyStateButton.ButtonClickListener() {
            @Override
            public void innerClick() {
                ceilingFollowStateBtn.setOnInnerStart();
                if (!isAttentionedAuthor) {
                    attentionMember(memberID, isAttentionedAuthor ? 0 : 1);
                } else {
                    BottomDialog2.Builder builder = new BottomDialog2.Builder(WorkDetailsActivity.this);
                    builder.setLayoutId(R.layout.dialog_unfollow_layout).show();
                    builder.setOnConfirmListener(new BottomDialog2.Builder.OnConfirmListener() {
                        @Override
                        public void onConfirm() {
                            attentionMember(memberID, isAttentionedAuthor ? 0 : 1);
                        }
                    });
                }
            }
        });
        backIv2.setOnClickListener(this);
        backIv3.setOnClickListener(this);
        followTv.setOnClickListener(this);
        followLayout.setOnClickListener(this);
        favoritedTv.setOnClickListener(this);
        replyTv.setOnClickListener(this);
        forwardTv.setOnClickListener(this);
        ceilingHeadIv.setOnClickListener(this);
        detailsHeadAdapter.setOnFollowListener(onFollowListener);
        commentAdapter.setOnItemClickListener(onItemClickListener);
        videoAdapter.setOnItemClickListener(videoOnItemClickListener);
        previewAdapter.setOnItemClickListener(picOnItemClickListener);
        picAdapter.setOnItemClickListener(picItemListener);
        commentHeadAdapter.setOnTextClickListener(onTextClickListener);
        advertisAdapter.setOnAdvertisListener(onAdvertisListener);
        loadingErrorView.setOnReloadListener(new LoadingErrorView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                getProductInfo(productID);
                getProductComments(productID, currentPage, SIZE_PAGE, false);
                getAdvertisementInfo(Constants.KEY_ADVERTIS_WORK_DETILS);
            }
        });
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getProductComments(productID, currentPage, SIZE_PAGE, true);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh();
//                getProductInfo(productID);
//                getProductComments(productID, currentPage, SIZE_PAGE, false);
//                getAdvertisementInfo(Constants.KEY_ADVERTIS_WORK_DETILS);
            }
        });
        final int[] iResult = {0};

        contentRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //找到即将移出屏幕Item的position,position是移出屏幕item的数量
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int position = linearLayoutManager.findFirstVisibleItemPosition();
//根据position找到这个Item
                View firstVisiableChildView = linearLayoutManager.findViewByPosition(position);
//获取Item的高
                int itemHeight = firstVisiableChildView.getHeight();
//算出该Item还未移出屏幕的高度
                int itemTop = firstVisiableChildView.getTop();
//position移出屏幕的数量*高度得出移动的距离
                int iposition = position * itemHeight;
//减去该Item还未移出屏幕的部分可得出滑动的距离
                iResult[0] = iposition - itemTop;
                if (iResult[0] < 230) {
                    userLayout.setVisibility(View.INVISIBLE);
                }
            }
        });
        refreshLayout.setOnMultiPurposeListener(new OnMultiPurposeListener() {
            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                userLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onHeaderReleased(RefreshHeader header, int headerHeight, int maxDragHeight) {

            }

            @Override
            public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int maxDragHeight) {

            }

            @Override
            public void onHeaderFinish(RefreshHeader header, boolean success) {

            }

            @Override
            public void onFooterMoving(RefreshFooter footer, boolean isDragging, float percent, int offset, int footerHeight, int maxDragHeight) {

            }

            @Override
            public void onFooterReleased(RefreshFooter footer, int footerHeight, int maxDragHeight) {

            }

            @Override
            public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int maxDragHeight) {

            }

            @Override
            public void onFooterFinish(RefreshFooter footer, boolean success) {

            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {

            }
        });
    }

    public void getProductInfo(final int productID) {
        GetProductInfoApi getProductInfoApi = new GetProductInfoApi(this);
        getProductInfoApi.setProductID(productID);
        getProductInfoApi.setListener(new HttpOnNextListener<GetProductInfoBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onError(Throwable e) {
                super.onError(e);
                if (e instanceof TimeoutException) {

                }
                loadingErrorView.onError(e);
                return null;
            }

            @Override
            public RetryWhenNetworkException.Wrapper onNext(GetProductInfoBean getProductInfoBean) {
                refreshLayout.finishRefresh();
                productInfoBean = getProductInfoBean.getReturnData();
                detailsHeadAdapter.setProductInfoBean(productInfoBean);

                switch (productInfoBean.getAuthor().getUserIdentify()) {
                    case Constants.Author.ID_F:
                        ceilingVipIv.setVisibility(View.VISIBLE);
                        followVipIv.setVisibility(View.VISIBLE);
                        break;
                    case Constants.Author.UNKNOWN:
                    default:
                        ceilingVipIv.setVisibility(View.GONE);
                        followVipIv.setVisibility(View.GONE);
                        break;
                }
                for (GetProductInfoBean.ReturnDataBean.DetailListBean bean :
                        productInfoBean.getDetailList()) {
                    if (bean.getType() == 2) {
                        videoList.add(bean);
                    }
                    if(bean.getType() == 1){
                        picList.add(bean);
                    }
                }
                detailsHeadAdapter.notifyDataSetChanged();
                videoAdapter.notifyDataSetChanged();
                labelAdapter.setProductInfoBean(productInfoBean);
                labelAdapter.notifyDataSetChanged();
                if (productInfoBean.getDetailList().size() > 20) {
                    previewAdapter.setProductInfoBean(productInfoBean);
                    previewAdapter.notifyDataSetChanged();
                } else {
                    picAdapter.setProductInfoBeanList(picList);
                    picAdapter.notifyDataSetChanged();
                }
                isFavorited = productInfoBean.isIsFavourited();
                Drawable drawable;
                if (isFavorited) {
                    drawable = getResources().getDrawable(R.drawable.like_checked_icon);
                    favoritedTv.setText(getString(R.string.favorited_btn_msg));
                } else {
                    drawable = getResources().getDrawable(R.drawable.like_unchecked_icon);
                    favoritedTv.setText(getString(R.string.favorit_btn_msg));
                }
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                favoritedTv.setCompoundDrawables(drawable, null, null, null);
                isAttentionedAuthor = productInfoBean.isIsAttentionedAuthor();
                if (productInfoBean.isIsAttentionedAuthor()) {
                    ceilingFollowBtn.setBackgroundResource(R.drawable.shape_gray_ccc_bg);
                    ceilingFollowBtn.setText(getString(R.string.browse_followed_msg));
                } else {
                    ceilingFollowBtn.setBackgroundResource(R.drawable.shape_pink_bg);
                    ceilingFollowBtn.setText(getString(R.string.follow_string));
                }
                if (productInfoBean.getAuthor() != null) {
                    followTv.setEnabled(true);
                    memberID = productInfoBean.getAuthor().getMemberID();
                    ceilingNameTv.setText(productInfoBean.getAuthor().getUserName());
                    nameTv2.setText(productInfoBean.getAuthor().getUserName());
                    RequestOptions mRequestOptions = RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.NONE);
                    mRequestOptions.placeholder(R.mipmap.placehold_head);
                    mRequestOptions.error(R.mipmap.placehold_head);
                    Glide.with(WorkDetailsActivity.this).load(productInfoBean.getAuthor().getPhoto()).apply(mRequestOptions).into(ceilingHeadIv);
                    Glide.with(WorkDetailsActivity.this).load(productInfoBean.getAuthor().getPhoto()).apply(mRequestOptions).into(headIv2);
                }

                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(this, getProductInfoApi);
    }

    public void getAdvertisementInfo(int locationID) {
        GetAdvertisementInfoApi getAdvertisementInfoApi = new GetAdvertisementInfoApi(this);
        getAdvertisementInfoApi.setLocationID(locationID);
        getAdvertisementInfoApi.setListener(new HttpOnNextListener<GetAdvertisementInfoBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(GetAdvertisementInfoBean getAdvertisementInfoBean) {
                if (getAdvertisementInfoBean.getReturnData() != null)
                    if (getAdvertisementInfoBean.getErrorCode() == 200) {
                        advertisBean = getAdvertisementInfoBean.getReturnData();
                        advertisAdapter.setAdvertisBean(advertisBean);
                        advertisAdapter.notifyDataSetChanged();
                    }
                return null;
            }

        });

        HttpManager.getInstance().doHttpDeal(this, getAdvertisementInfoApi);
    }

    public void getProductComments(int productID, final int pageIndex, int pageSize, final boolean isScrollRv) {
        GetProductCommentsApi getProductCommentsApi = new GetProductCommentsApi(this);
        getProductCommentsApi.setProductID(productID);
        getProductCommentsApi.setPageIndex(pageIndex);
        getProductCommentsApi.setPageSize(pageSize);
        getProductCommentsApi.setListener(new HttpOnNextListener<ProductCommentsBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(ProductCommentsBean commentsBean) {
                productCommentsBean = commentsBean.getReturnData();
                commentHeadAdapter.setProductCommentsBean(productCommentsBean);
                commentHeadAdapter.notifyDataSetChanged();
                if (pageIndex == 1) {
                    commentList.clear();
                }
                commentList.addAll(productCommentsBean.getList());
                commentAdapter.notifyDataSetChanged();
                if (productCommentsBean.getList().size() >= productCommentsBean.getTotal()) {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    refreshLayout.finishLoadMore();
                    currentPage++;
                }
                if (isScrollRv) {
                    contentRv.smoothScrollToPosition(contentRv.getAdapter().getItemCount());
                }
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(this, getProductCommentsApi);
    }

    public void addComment(final int productID, String content) {
        AddCommentApi addCommentApi = new AddCommentApi(this);
        addCommentApi.setProductID(productID);
        addCommentApi.setContent(content);
        addCommentApi.setListener(new HttpOnNextListener<AddCommentBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(AddCommentBean addCommentBean) {
                if (addCommentBean.isReturnData()) {
                    getProductComments(productID, 1, SIZE_PAGE, true);
                } else {
                    CustomToast.showToast(addCommentBean.getErrorMsg());
                }
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(this, addCommentApi);
    }

    public void commentLike(int commentID, final int type, final int position) {
        CommentLikeApi commentLikeApi = new CommentLikeApi(this);
        commentLikeApi.setCommentID(commentID);
        commentLikeApi.setType(type);
        commentLikeApi.setListener(new HttpOnNextListener<CommentLikeBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(CommentLikeBean commentLikeBean) {
                if (commentLikeBean.getErrorCode() == 200) {
                    commentList.get(position).setLikeCount(commentLikeBean.getReturnData().getLikeCount());
                    commentList.get(position).setIsLike(type == 1 ? true : false);
                    commentAdapter.notifyDataSetChanged();
                }
                return null;
            }

        });

        HttpManager.getInstance().doHttpDeal(this, commentLikeApi);
    }

    public void favouriteProduct(int productID, int type) {
        FavouriteProductApi favouriteProductApi = new FavouriteProductApi(this);
        favouriteProductApi.setProductID(productID);
        favouriteProductApi.setType(type);
        favouriteProductApi.setListener(new HttpOnNextListener<FavouriteProductBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(FavouriteProductBean favouriteProductBean) {
                if (favouriteProductBean.getErrorCode() == 200) {
                    EventBus.getDefault().post(new BusEvent(BusEvent.COLLECT, true));
                    Drawable drawable;
                    isFavorited = !isFavorited;
                    if (isFavorited) {
                        drawable = getResources().getDrawable(R.drawable.like_checked_icon);
                        favoritedTv.setText(getString(R.string.favorited_btn_msg));
                    } else {
                        drawable = getResources().getDrawable(R.drawable.like_unchecked_icon);
                        favoritedTv.setText(getString(R.string.favorit_btn_msg));
                    }
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    favoritedTv.setCompoundDrawables(drawable, null, null, null);
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
            ceilingFollowStateBtn.setOnInnerResult();
            detailsHeadAdapter.setOnInnerUnFinish(type);
            return;
        }
        AttentionMemberApi attentionMemberApi = new AttentionMemberApi(this);
        attentionMemberApi.setMemberID(memberID);
        attentionMemberApi.setType(type);
        attentionMemberApi.setListener(new HttpOnNextListener<AttentionMemberBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(AttentionMemberBean attentionMemberBean) {
                if (attentionMemberBean.getErrorCode() == 200) {
                    followLayout.setVisibility(View.GONE);
                    EventBus.getDefault().post(new BusEvent(BusEvent.ATTENTION_MEMBER, type == 1 ? true : false));
                    isAttentionedAuthor = type == 1 ? true : false;
                    detailsHeadAdapter.setIsAttentionedAuthor(type == 1 ? true : false);
                    detailsHeadAdapter.notifyDataSetChanged();
                    if (type == 1) {
                        ceilingFollowBtn.setBackgroundResource(R.drawable.shape_gray_ccc_bg);
                        ceilingFollowBtn.setText(getString(R.string.browse_followed_msg));
                    } else {
                        ceilingFollowBtn.setBackgroundResource(R.drawable.shape_pink_bg);
                        ceilingFollowBtn.setText(getString(R.string.follow_string));
                    }
                }

                CustomToast.showToast(attentionMemberBean.getErrorMsg());
                ceilingFollowStateBtn.setOnInnerResult();
                return null;
            }
        });

        HttpManager.getInstance().doHttpDeal(this, attentionMemberApi);
    }

    public void addReply(int commentID, String content) {
        AddReplyApi addReplyApi = new AddReplyApi(this);
        addReplyApi.setCommentID(commentID);
        addReplyApi.setContent(content);
        addReplyApi.setListener(new HttpOnNextListener<AddReplyBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(AddReplyBean addReplyBean) {
                if (addReplyBean.isReturnData()) {
                    getProductComments(productID, 1, SIZE_PAGE, true);

                } else {
                    CustomToast.showToast(addReplyBean.getErrorMsg());
                }
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(this, addReplyApi);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.work_details_favorited_tv:
                if (!AccountManager.getInstances().isLogin(this, true)) {
                    return;
                }
                favouriteProduct(productID, isFavorited ? 0 : 1);
                break;
            case R.id.work_details_reply_tv:
                LogUtils.d("-- " + contentRv.getAdapter().getItemCount());
//                contentRv.smoothScrollToPosition(contentRv.getAdapter().getItemCount() - commentList.size());
                showInputDialog(true, 0, "");
                break;
            case R.id.work_details_forward_tv:
//                BottomDialog2.Builder builder = new BottomDialog2.Builder(this);
//                builder.showShare().show();
//                builder.setOnItemClickListener(new BottomDialog2.Builder.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        CustomToast.showToast( position + "");
//                    }
//                });

                ShareManager.getInstances().share(1, productID, WorkDetailsActivity.this);
                break;
            case R.id.work_details_back:
            case R.id.work_details_back_iv3:
                finish();
                break;
            case R.id.work_details_ceiling_head_iv:
                Intent intent = new Intent(this, UserHomeActivity.class);
                intent.putExtra("memberID", memberID);
                startDDMActivity(intent, true);
                break;
            case R.id.work_details_follow_tv:
                attentionMember(memberID, isAttentionedAuthor ? 0 : 1);
                break;
            case R.id.work_details_follow_layout:
                break;
        }
    }

    public void showInputDialog(final boolean commentOrReply, final int commentId, String commentUserName) {
        boolean isLogin = AccountManager.getInstances().isLogin(WorkDetailsActivity.this, true);
        if (!isLogin) {
            return;
        }
        bottomDialog = new BottomDialog(WorkDetailsActivity.this, R.style.ActionSheetDialogStyle);
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
                    addComment(productID, content);
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

    private DetailsAdapter.OnTextClickListener onTextClickListener = new DetailsAdapter.OnTextClickListener() {
        @Override
        public void onClick(View view) {
            showInputDialog(true, 0, "");
        }
    };
    private CommentAdapter.OnItemClickListener onItemClickListener = new CommentAdapter.OnItemClickListener() {
        @Override
        public void onReplyClick(View view, int position) {
            Intent intent = new Intent(WorkDetailsActivity.this, ReplyActivitry.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ReplyActivitry.KEY_COMMENT, commentList.get(position));
            intent.putExtras(bundle);
            startDDMActivity(intent, true);
        }

        @Override
        public void onLikeClick(View view, int position, int commentID, boolean isChecked) {
            boolean isLogin = AccountManager.getInstances().isLogin(WorkDetailsActivity.this, true);
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

    private DetailsAdapter.OnItemClickListener picItemListener = new DetailsAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent(WorkDetailsActivity.this, PictureViewerActivity.class);
            intent.putExtra(PreviewActivity.KEY_PRODUCT_ID, productID);
            intent.putExtra(PictureViewerActivity.KEY_PIC_POSITION, position);
            startDDMActivity(intent, true);
        }
    };

    private PreviewContentAdapter.OnItemClickListener picOnItemClickListener = new PreviewContentAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent(WorkDetailsActivity.this, PictureViewerActivity.class);
            intent.putExtra(PreviewActivity.KEY_PRODUCT_ID, productID);
            intent.putExtra(PictureViewerActivity.KEY_PIC_POSITION, position);
            startDDMActivity(intent, true);
        }
    };

    private DetailsAdapter.OnItemClickListener videoOnItemClickListener = new DetailsAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent(WorkDetailsActivity.this, VideoPlayerActivity2.class);
            GetProductInfoBean.ReturnDataBean.DetailListBean item = videoList.get(position);
            Bundle bundle = new Bundle();
            bundle.putSerializable(VideoPlayerActivity.KEY_VIDEO_ITEM, item);
            bundle.putSerializable(VideoPlayerActivity.KEY_VIDEO_LIST, (Serializable) videoList);
            intent.putExtra(VideoPlayerActivity.KEY_VIDEO_POSITION, position);
            intent.putExtras(bundle);
            startDDMActivity(intent, true);
        }
    };

    private DetailsAdapter.OnFollowListener onFollowListener = new DetailsAdapter.OnFollowListener() {

        /**
         * isAttentionedAuthor表示当前用户的关注状态 true表示用户已关注改作者,false则取反,所以这里传相反的值
         * @param view
         * @param memberID
         * @param isAttentionedAuthor
         */
        @Override
        public void onFollow(View view, final int memberID, final boolean isAttentionedAuthor) {
            if (!isAttentionedAuthor) {
                attentionMember(memberID, isAttentionedAuthor ? 0 : 1);
            } else {
                BottomDialog2.Builder builder = new BottomDialog2.Builder(WorkDetailsActivity.this);
                builder.setLayoutId(R.layout.dialog_unfollow_layout).show();
                builder.setOnConfirmListener(new BottomDialog2.Builder.OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        attentionMember(memberID, isAttentionedAuthor ? 0 : 1);
                    }
                });
            }
        }

        @Override
        public void onBack() {
            finish();
        }
    };

    private DetailsAdapter.OnAdvertisListener onAdvertisListener = new DetailsAdapter.OnAdvertisListener() {
        @Override
        public void onAdvertis(View view, String url) {
            Intent intent = new Intent(WorkDetailsActivity.this, AdvertisActivity.class);
            intent.putExtra(AdvertisActivity.KEY_ADVERTIS_URL, url);
            startDDMActivity(intent, true);
        }
    };
}
