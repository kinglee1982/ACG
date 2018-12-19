package com.kcx.acg.views.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kcx.acg.R;
import com.kcx.acg.api.AttentionMemberApi;
import com.kcx.acg.api.MemberEntityApi;
import com.kcx.acg.api.ProductToMenberListApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.base.BusEvent;
import com.kcx.acg.bean.AttentionMemberBean;
import com.kcx.acg.bean.MemberEntityBean;
import com.kcx.acg.bean.ProductToDayListBean;
import com.kcx.acg.bean.UserInfoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.manager.AccountManager;
import com.kcx.acg.manager.ShareManager;
import com.kcx.acg.manager.WorkManager;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.BottomDialogUtil;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.utils.StringUtil;
import com.kcx.acg.views.adapter.UserHomeAdapter;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.FullyGridLayoutManager;
import com.kcx.acg.views.view.LoadingErrorView;
import com.kcx.acg.views.view.MyStateButton;
import com.kcx.acg.views.view.ObservableScrollView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 用户主页
 * Created by jb on 2018/9/7.
 */
public class UserHomeActivity extends BaseActivity implements CancelAdapt {

    private LoadingErrorView mLoadingErrorView;
    private LinearLayout ll_empty;
    private TextView tv_empty_hint;
    private Button btn_empty;

    private MyStateButton myStateButton, myStateButton1;
    private Button btn_attention1, btn_attention;
    private RelativeLayout rl_head, rl_info,rl_back,rl_share;
    private RecyclerView mRecyclerView;
    private ImageView iv_touxiang1, iv_touxiang,iv_back,iv_share,iv_v,iv_v1;
    private TextView tv_title, tv_nickname, tv_fansNum;
    private SmartRefreshLayout mRefreshLayout;

    private ObservableScrollView scrollView;
    private UserHomeAdapter userHomeAdapter;
    private float title_height, head_height;
    private List<ProductToDayListBean.ReturnDataBean.ListBean> productToDayList;
    private UserInfoBean userInfoBean;
    private int memberID;
    private int pageIndex = 1;
    private int pageSize = 10;
    private boolean isAttentioned;
    private String attentionStr, attentionStr1, photoUrl;
    private RequestOptions options;
    private int userIdentify=0;
    //    @Override
    //    public void setTatusBar() {
    //        super.setTatusBar();
    //        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    //    }

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_user_home, null);
    }

    @Override
    public void initView() {
        super.initView();
        mLoadingErrorView = findViewById(R.id.loadingErrorView);
        iv_back = findViewById(R.id.iv_back);
        rl_back = findViewById(R.id.rl_back);
        iv_share = findViewById(R.id.iv_share);
        rl_share = findViewById(R.id.rl_share);

        ll_empty = findViewById(R.id.ll_empty);
        tv_empty_hint = findViewById(R.id.tv_empty_hint);
        tv_empty_hint.setText(R.string.empty_userHome_hint);
        btn_empty = findViewById(R.id.btn_empty);
        btn_empty.setVisibility(View.INVISIBLE);
        iv_v1 = findViewById(R.id.iv_v1);
        iv_v = findViewById(R.id.iv_v);


        myStateButton = findViewById(R.id.myStateButton);
        myStateButton1 = findViewById(R.id.myStateButton1);
        btn_attention = findViewById(R.id.btn_attention);
        btn_attention1 = findViewById(R.id.btn_attention1);
        iv_touxiang1 = findViewById(R.id.iv_touxiang1);
        tv_title = findViewById(R.id.tv_title);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        scrollView = findViewById(R.id.scrollView);
        rl_head = findViewById(R.id.rl_head);
        rl_info = findViewById(R.id.rl_info);
        iv_touxiang = findViewById(R.id.iv_touxiang);
        tv_nickname = findViewById(R.id.tv_nickname);
        tv_fansNum = findViewById(R.id.tv_fansNum);
        mRefreshLayout = findViewById(R.id.mRefreshLayout);
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadMoreWhenContentNotFull(false);

        options = new RequestOptions()
                .placeholder(R.mipmap.placehold_head)  //预加载图片
                .error(R.mipmap.placehold_head);
    }

    @Override
    public void setListener() {
        super.setListener();
        rl_back.setOnClickListener(this);
        rl_share.setOnClickListener(this);
        iv_touxiang.setOnClickListener(this);
        iv_touxiang1.setOnClickListener(this);

        //加载更多
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex = pageIndex + 1;
                getProductToMenberList(memberID, pageIndex++, pageSize);
                refreshLayout.finishLoadMore();
            }
        });

        //重新加载
        mLoadingErrorView.setOnReloadListener(new LoadingErrorView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                getMemberEntity(memberID);
                productToDayList.clear();
                getProductToMenberList(memberID, 1, pageSize);
            }
        });

        //滑动事件回调监听（一次滑动的过程一般会连续触发多次）
        scrollView.setOnScrollListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScroll(int oldy, int dy, boolean isUp) {
                float move_distance = head_height - title_height;
                LogUtil.e("onScroll", "isUp=" + isUp + "+y=" + dy + ";  move_distance=" + move_distance);
                if (!isUp && dy <= 480) { //渐变效果
                    //                    rl_info.setBackgroundResource(R.color.white);
                    //                    rl_head.setBackgroundResource(R.color.white);
                    //                    TitleAlphaChange(dy, 400);

                } else if (!isUp && dy > 480) {//手指往上滑
                    rl_info.setBackgroundResource(R.color.white);
                    rl_head.setBackgroundResource(R.color.white);

                    if (1== userIdentify){
                        iv_v1.setVisibility(View.VISIBLE);
                    }else {
                        iv_v1.setVisibility(View.INVISIBLE);
                    }
                    rl_share.setVisibility(View.GONE);
                    iv_touxiang1.setVisibility(View.VISIBLE);
                    myStateButton1.setVisibility(View.VISIBLE);
                    iv_back.setBackgroundResource(R.mipmap.icon_back);
                    tv_title.setVisibility(View.VISIBLE);
                    if (userInfoBean != null) {
                        if (memberID==userInfoBean.getReturnData().getMemberID()) {  //个人主页
                            myStateButton1.setVisibility(View.GONE);
                        } else {
                            myStateButton1.setVisibility(View.VISIBLE);
                        }
                    }
                } else if (isUp && dy <= 480) {//返回顶部
                    rl_info.setBackgroundResource(R.mipmap.bg_user_home);
                    rl_head.setBackgroundResource(R.color.transparent);

                    rl_share.setVisibility(View.VISIBLE);
                    iv_touxiang1.setVisibility(View.INVISIBLE);
                    iv_v1.setVisibility(View.INVISIBLE);
                    myStateButton1.setVisibility(View.GONE);
                    iv_back.setBackgroundResource(R.mipmap.icon_back_white);
                    tv_title.setVisibility(View.INVISIBLE);

                }
            }
        });

        myStateButton.setOnInnerClickeListener(new MyStateButton.ButtonClickListener() {
            @Override
            public void innerClick() {
                boolean isLogin = AccountManager.getInstances().isLogin(UserHomeActivity.this, true);
                if (!isLogin) {
                    return;
                }
                attentionStr = btn_attention.getText().toString().trim();
                if (isAttentioned) {
                    new BottomDialogUtil(UserHomeActivity.this,
                            R.layout.dialog_bottom,
                            getString(R.string.attentionAndFans_hint1),
                            getString(R.string.attentionAndFans_hint2),
                            getString(R.string.attentionAndFans_hint3),
                            new BottomDialogUtil.BottonDialogListener() {
                                @Override
                                public void onItemListener() {
                                    myStateButton.setOnInnerStart();
                                    AttentionMemberApi(memberID, 0, 0);
                                }
                            });
                } else {
                    myStateButton.setOnInnerStart();
                    AttentionMemberApi(memberID, 1, 0);
                }
            }
        });

        myStateButton1.setOnInnerClickeListener(new MyStateButton.ButtonClickListener() {
            @Override
            public void innerClick() {
                attentionStr1 = btn_attention1.getText().toString().trim();
                if (isAttentioned) {
                    new BottomDialogUtil(UserHomeActivity.this,
                            R.layout.dialog_bottom,
                            getString(R.string.attentionAndFans_hint1),
                            getString(R.string.attentionAndFans_hint2),
                            getString(R.string.attentionAndFans_hint3),
                            new BottomDialogUtil.BottonDialogListener() {
                                @Override
                                public void onItemListener() {
                                    myStateButton1.setOnInnerStart();
                                    AttentionMemberApi(memberID, 0, 1);
                                }
                            });
                } else {
                    myStateButton1.setOnInnerStart();
                    AttentionMemberApi(memberID, 1, 1);
                }
            }
        });

    }


    @Override
    public void initData() {
        super.initData();
        userInfoBean = AppUtil.getGson().fromJson(SPUtil.getString(UserHomeActivity.this, Constants.USER_INFO, ""), UserInfoBean.class);
        memberID = getIntent().getIntExtra("memberID", 0);
        if (userInfoBean != null) {
            if (memberID==userInfoBean.getReturnData().getMemberID()) {  //个人主页
                btn_attention.setVisibility(View.GONE);
                btn_attention1.setVisibility(View.GONE);
            }
        }
        productToDayList = new ArrayList<ProductToDayListBean.ReturnDataBean.ListBean>();

        initRecyclerView(productToDayList);
        getProductToMenberList(memberID, pageIndex, pageSize);
        getMemberEntity(memberID);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.iv_touxiang:  //查看头像
                Intent i1 = new Intent(this, LookHeadPortraitsActivity.class);
                i1.putExtra("photoUrl", photoUrl);
                startActivity(i1);
                break;
            case R.id.iv_touxiang1:
                break;
            case R.id.rl_share:  //分享
                ShareManager.getInstances().share(3, memberID, UserHomeActivity.this);
                break;
        }
    }

    /***
     * 初始列表
     */
    private void initRecyclerView(final List<ProductToDayListBean.ReturnDataBean.ListBean> listData) {
        FullyGridLayoutManager mgr = new FullyGridLayoutManager(UserHomeActivity.this, 2);
        //        mRecyclerView.setNestedScrollingEnabled(false);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mgr);
        userHomeAdapter = new UserHomeAdapter(UserHomeActivity.this, listData);
        // 设置adapter
        mRecyclerView.setAdapter(userHomeAdapter);

        userHomeAdapter.setOnItemClickListener(new UserHomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                WorkManager.getInstances().goToWhere(UserHomeActivity.this, listData.get(position).getCanViewType(), listData.get(position).getProductID());
            }
        });
    }

    /**
     * 获取用户信息
     *
     * @param memberID
     */
    private void getMemberEntity(int memberID) {
        MemberEntityApi memberEntityApi = new MemberEntityApi(this, new HttpOnNextListener<MemberEntityBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(MemberEntityBean memberEntityBean) {
                mLoadingErrorView.setVisibility(View.GONE);
                if (memberEntityBean.getErrorCode() == 200) {
                     userIdentify = memberEntityBean.getReturnData().getUserIdentify();
                    if (1== userIdentify){
                        iv_v.setVisibility(View.VISIBLE);
//                        iv_v1.setVisibility(View.VISIBLE);
                    }else {
                        iv_v.setVisibility(View.INVISIBLE);
                        iv_v1.setVisibility(View.INVISIBLE);
                    }

                    photoUrl = memberEntityBean.getReturnData().getPhoto();
                    Glide.with(UserHomeActivity.this).load(photoUrl).apply(options).into(iv_touxiang);
                    Glide.with(UserHomeActivity.this).load(photoUrl).apply(options).into(iv_touxiang1);
                    tv_nickname.setText(memberEntityBean.getReturnData().getUserName());
                    tv_title.setText(memberEntityBean.getReturnData().getUserName());
                    tv_fansNum.setText(StringUtil.toDecimalFormat(memberEntityBean.getReturnData().getFansCount()) + " " + getString(R.string.attentionAndFans_fans));
                    isAttentioned = memberEntityBean.getReturnData().isIsAttentioned();
                    if (isAttentioned) {
                        btn_attention.setBackgroundResource(R.drawable.shape_be_attention_bg);
                        btn_attention.setText(R.string.beAttention);
                        btn_attention1.setBackgroundResource(R.drawable.shape_be_attention_bg);
                        btn_attention1.setText(R.string.beAttention);
                    } else {
                        btn_attention.setBackgroundResource(R.drawable.shape_pink_bg_3dp);
                        btn_attention.setText(R.string.addAttention);
                        btn_attention1.setBackgroundResource(R.drawable.shape_pink_bg_3dp);
                        btn_attention1.setText(R.string.addAttention);
                    }
                } else {
                    CustomToast.showToast(memberEntityBean.getErrorMsg());
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
        memberEntityApi.setMemberID(memberID);
        HttpManager.getInstance().doHttpDeal(UserHomeActivity.this, memberEntityApi);

    }

    /**
     * 获取作品列表
     *
     * @param memberID
     * @param pageIndex
     * @param pageSize
     */
    private void getProductToMenberList(int memberID, int pageIndex, int pageSize) {
        ProductToMenberListApi productToMenberListApi = new ProductToMenberListApi(this, new HttpOnNextListener<ProductToDayListBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(ProductToDayListBean productToDayListBean) {
                if (productToDayListBean.getErrorCode() == 200) {
                    productToDayList.addAll(productToDayListBean.getReturnData().getList());
                    if (productToDayList.size() == 0) {  //空数据
                        mRecyclerView.setVisibility(View.GONE);
                        ll_empty.setVisibility(View.VISIBLE);
                        mRefreshLayout.setEnableLoadMore(false);
                        return null;
                    } else {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        ll_empty.setVisibility(View.GONE);
                        mRefreshLayout.setEnableLoadMore(true);
                    }

                    userHomeAdapter.update(productToDayList);
                    if (productToDayList.size() >= productToDayListBean.getReturnData().getTotal() && productToDayList.size() != 0) {
                        mRefreshLayout.finishLoadMoreWithNoMoreData();
                    }

                } else {
                    CustomToast.showToast(productToDayListBean.getErrorMsg());
                }

                return null;
            }
        });
        productToMenberListApi.setMenberId(memberID);
        productToMenberListApi.setPageIndex(pageIndex);
        productToMenberListApi.setPageSize(pageSize);
        HttpManager.getInstance().doHttpDeal(UserHomeActivity.this, productToMenberListApi);

    }

    /**
     * @param attentionedMemberID 被关注用户ID;
     *                            type                0:取消关注；1：关注
     *                            level               0 主页按钮   1  标题栏按钮
     */
    private void AttentionMemberApi(int attentionedMemberID, final int type, final int level) {

        AttentionMemberApi attentionMemberApi = new AttentionMemberApi(UserHomeActivity.this, new HttpOnNextListener<AttentionMemberBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(AttentionMemberBean attentionMemberBean) {
                CustomToast.showToast(attentionMemberBean.getErrorMsg());
                if (attentionMemberBean.getErrorCode() == 200) {
                    EventBus.getDefault().post(new BusEvent(BusEvent.ATTENTION_MEMBER, type == 1 ? true : false));
                    EventBus.getDefault().post(new BusEvent(BusEvent.ATTENTION, true));
                    if (type == 0) {
                        btn_attention.setBackgroundResource(R.drawable.shape_attention_pink_bg);
                        btn_attention.setText(R.string.addAttention);
                        btn_attention1.setBackgroundResource(R.drawable.shape_attention_pink_bg);
                        btn_attention1.setText(R.string.addAttention);
                        isAttentioned = false;
                    } else {
                        btn_attention.setBackgroundResource(R.drawable.shape_be_attention_bg);
                        btn_attention.setText(R.string.beAttention);
                        btn_attention1.setBackgroundResource(R.drawable.shape_be_attention_bg);
                        btn_attention1.setText(R.string.beAttention);
                        isAttentioned = true;
                    }
                } else {
                    if (level == 0) {
                        btn_attention.setText(attentionStr);
                    } else {
                        btn_attention1.setText(attentionStr1);
                    }
                }

                if (level == 0) {
                    myStateButton.setOnInnerResult();
                } else {
                    myStateButton1.setOnInnerResult();
                }
                return null;
            }

            @Override
            public RetryWhenNetworkException.Wrapper onError(Throwable e) {
                super.onError(e);
                if (level == 0) {
                    myStateButton.setOnInnerResult();
                    btn_attention.setText(attentionStr);
                } else {
                    myStateButton1.setOnInnerResult();
                    btn_attention1.setText(attentionStr1);
                }
                return null;
            }
        });
        attentionMemberApi.setMemberID(attentionedMemberID);
        attentionMemberApi.setType(type);
        HttpManager.getInstance().doHttpDeal(UserHomeActivity.this, attentionMemberApi);

    }


    private void TitleAlphaChange(int dy, float mHeaderHeight_px) {//设置标题栏透明度变化
        float percent = (float) Math.abs(dy) / Math.abs(mHeaderHeight_px);
        //如果是设置背景透明度，则传入的参数是int类型，取值范围0-255
        //如果是设置控件透明度，传入的参数是float类型，取值范围0.0-1.0
        //这里我们是设置背景透明度就好，因为设置控件透明度的话，返回ICON等也会变成透明的。
        //alpha 值越小越透明
        int alpha = (int) (percent * 255);
        rl_head.getBackground().setAlpha(alpha);//设置控件背景的透明度，传入int类型的参数（范围0~255）
        rl_info.getBackground().setAlpha(alpha);//设置控件背景的透明度，传入int类型的参数（范围0~255）

    }
}
