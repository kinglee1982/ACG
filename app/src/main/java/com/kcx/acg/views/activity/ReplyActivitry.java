package com.kcx.acg.views.activity;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.google.common.collect.Lists;
import com.kcx.acg.R;
import com.kcx.acg.api.AddReplyApi;
import com.kcx.acg.api.CommentLikeApi;
import com.kcx.acg.api.GetCommentReplysApi;
import com.kcx.acg.api.ReplyLikeApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.base.BusEvent;
import com.kcx.acg.bean.CommentLikeBean;
import com.kcx.acg.bean.GetCommentReplysBean;
import com.kcx.acg.bean.ProductCommentsBean;
import com.kcx.acg.bean.AddReplyBean;
import com.kcx.acg.bean.ReplyLikeBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.manager.AccountManager;
import com.kcx.acg.views.adapter.CommentAdapter;
import com.kcx.acg.views.adapter.TitleAdapter;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.LoadingErrorView;
import com.kcx.acg.views.view.TitleBarView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 */

public class ReplyActivitry extends BaseActivity implements CancelAdapt {
    public static final String KEY_COMMENT_ID = "key_comment_id";
    public static final String KEY_COMMENT = "key_comment";

    private View rootView;
    private TitleBarView titleBarView;
    private RecyclerView contentRV;
    private ImageView backIv;
    private TextView titleTv;
    private EditText contentEt;
    private TextView commitTv;
    private SmartRefreshLayout refreshLayout;
    private LoadingErrorView loadingErrorView;
    private ProductCommentsBean.ReturnDataBean.ListBean commentBean;
    private static final int SIZE_PAGE = 10;
    private int currentPage = 1;
    private List<GetCommentReplysBean.ReturnDataBean.ListBean> replyList;
    private CommentAdapter commentAdapter2;
    private CommentAdapter commentAdapter;
    private DelegateAdapter delegateAdapter;

    @Override
    public View setInitView() {
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_reply, null);
        titleBarView = rootView.findViewById(R.id.reply_title_bar_view);
        backIv = titleBarView.getIv_in_title_back();
        backIv.setImageResource(R.mipmap.icon_back);
        titleTv = titleBarView.getTv_in_title();
        loadingErrorView = rootView.findViewById(R.id.reply_loading_error_view);
        contentEt = rootView.findViewById(R.id.comment_dialog_et);
        commitTv = rootView.findViewById(R.id.dialog_add_comment_tv);
        contentRV = rootView.findViewById(R.id.reply_rv);
        refreshLayout = rootView.findViewById(R.id.reply_smart_refresh_layout);
        refreshLayout.setPrimaryColorsId(R.color.white, R.color.gray_999);
        refreshLayout.autoRefresh();
//        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setEnableFooterFollowWhenLoadFinished(false);
        refreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        contentRV.setRecycledViewPool(viewPool);
        contentRV.setLayoutManager(layoutManager);
        bindView(contentRV);
        return rootView;
    }

    @Override
    public void setListener() {
        backIv.setOnClickListener(this);
        commitTv.setOnClickListener(this);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getCommentReplys(commentBean.getCommentID(), currentPage, SIZE_PAGE);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getCommentReplys(commentBean.getCommentID(), 1, SIZE_PAGE);
            }
        });
        loadingErrorView.setOnReloadListener(new LoadingErrorView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                getCommentReplys(commentBean.getCommentID(), 1, SIZE_PAGE);
            }
        });
    }

    @Override
    public void initData() {
        commentBean = (ProductCommentsBean.ReturnDataBean.ListBean) getIntent().getExtras().getSerializable(KEY_COMMENT);

        int accountStatus = AccountManager.getInstances().getUserInfo(this).getReturnData().getAccountStatus();
        if (accountStatus == Constants.UserInfo.AccountStatus.SUSPEND ||
                accountStatus == Constants.UserInfo.AccountStatus.DISABLE_SPEECH ||
                accountStatus == Constants.UserInfo.AccountStatus.ACCOUNT_BANNED_AND_DISABLE_SPEECH) {
            contentEt.setHint(getString(R.string.account_disable_1));
            contentEt.setEnabled(false);
            commitTv.setEnabled(false);
        } else {
            contentEt.setHint(getString(R.string.comment_reply_hint_msg, commentBean.getCommentUser().getUserName()));
        }

        titleTv.setText(getString(R.string.comment_reply_count_msg, commentBean.getReplyCount()));
        SingleLayoutHelper singleLayoutHelper = new SingleLayoutHelper();
        singleLayoutHelper.setItemCount(1);
        singleLayoutHelper.setBgColor(Color.WHITE);
        commentAdapter = new CommentAdapter(this, commentBean, singleLayoutHelper, false, 1);
        commentAdapter.setOnItemClickListener(onHeadItemClickListener);
        TitleAdapter titleAdapter = new TitleAdapter(this, singleLayoutHelper, R.layout.reply_title_layout, "");

        replyList = Lists.newArrayList();
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        linearLayoutHelper.setItemCount(1);
        linearLayoutHelper.setBgColor(R.color.white);
        commentAdapter2 = new CommentAdapter(this, replyList, linearLayoutHelper, false, 2);
        commentAdapter2.setOnItemClickListener(onReplyItemClickListener);

        delegateAdapter = new DelegateAdapter(layoutManager);
        delegateAdapter.addAdapter(commentAdapter);
        delegateAdapter.addAdapter(titleAdapter);
        delegateAdapter.addAdapter(commentAdapter2);
        contentRV.setAdapter(delegateAdapter);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_in_title_back:
                finish();
                break;
            case R.id.dialog_add_comment_tv:
                addReply(commentBean.getCommentID(), contentEt.getText().toString());
                break;
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
                    contentEt.setText("");
                    Hidekeyboard(contentEt);
                    getCommentReplys(commentBean.getCommentID(), currentPage, SIZE_PAGE);
                    EventBus.getDefault().post(new BusEvent(BusEvent.REPLY_SUCCESS, true));
                } else {
                    CustomToast.showToast(addReplyBean.getErrorMsg());
                }
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(this, addReplyApi);

    }

    public void getCommentReplys(int commentID, final int pageIndex, int pageSize) {
        GetCommentReplysApi getCommentReplysApi = new GetCommentReplysApi(this);
        getCommentReplysApi.setCommentID(commentID);
        getCommentReplysApi.setPageIndex(pageIndex);
        getCommentReplysApi.setPageSize(pageSize);
        getCommentReplysApi.setListener(new HttpOnNextListener<GetCommentReplysBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onError(Throwable e) {
                super.onError(e);
                loadingErrorView.onError(e);
                return null;
            }

            @Override
            public RetryWhenNetworkException.Wrapper onNext(GetCommentReplysBean getCommentReplysBean) {
                refreshLayout.finishRefresh();
                if (getCommentReplysBean.getReturnData() != null) {
                    titleTv.setText(getString(R.string.comment_reply_count_msg, getCommentReplysBean.getReturnData().getTotal()));
                    if (pageIndex == 1) {
                        replyList.clear();
                    }
                    replyList.addAll(getCommentReplysBean.getReturnData().getList());
                    commentAdapter2.notifyDataSetChanged();
                    if (replyList.size() >= getCommentReplysBean.getReturnData().getTotal()) {
                        refreshLayout.finishLoadMoreWithNoMoreData();
                    } else {
                        refreshLayout.finishLoadMore();
                        currentPage++;
                    }
                } else {
                    showSoftInput(contentEt);
                }

                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(this, getCommentReplysApi);
    }

    public void replyLike(int replyID, final int type, final int position) {
        ReplyLikeApi replyLikeApi = new ReplyLikeApi(this);
        replyLikeApi.setReplyID(replyID);
        replyLikeApi.setType(type);
        replyLikeApi.setListener(new HttpOnNextListener<ReplyLikeBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(ReplyLikeBean replyLikeBean) {
                replyList.get(position).setIsLike(type == 1 ? true : false);
                replyList.get(position).setLikeCount(replyLikeBean.getReturnData().getLikeCount());
                commentAdapter2.notifyDataSetChanged();
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(this, replyLikeApi);
    }

    public void commentLike(int commentID, final int type, final int position) {
        CommentLikeApi commentLikeApi = new CommentLikeApi(this);
        commentLikeApi.setCommentID(commentID);
        commentLikeApi.setType(type);
        commentLikeApi.setListener(new HttpOnNextListener<CommentLikeBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(CommentLikeBean commentLikeBean) {
                commentBean.setLikeCount(commentLikeBean.getReturnData().getLikeCount());
                commentBean.setIsLike(type == 1 ? true : false);
                commentAdapter.notifyDataSetChanged();
                return null;
            }

        });

        HttpManager.getInstance().doHttpDeal(this, commentLikeApi);
    }

    private CommentAdapter.OnItemClickListener onHeadItemClickListener = new CommentAdapter.OnItemClickListener() {
        @Override
        public void onReplyClick(View view, int position) {
        }

        @Override
        public void onLikeClick(View view, int position, int id, boolean isChecked) {
            commentLike(commentBean.getCommentID(), isChecked ? 1 : 0, position);
        }

        @Override
        public void onTextClick(View view, int position) {

        }

    };
    private CommentAdapter.OnItemClickListener onReplyItemClickListener = new CommentAdapter.OnItemClickListener() {
        @Override
        public void onReplyClick(View view, int position) {
        }

        @Override
        public void onLikeClick(View view, int position, int id, boolean isChecked) {
            replyLike(id, isChecked ? 1 : 0, position);
        }

        @Override
        public void onTextClick(View view, int position) {

        }

    };
}
