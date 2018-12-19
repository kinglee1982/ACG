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
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.kcx.acg.R;
import com.kcx.acg.api.AddMemberTagApi;
import com.kcx.acg.api.ProductToDayListApi;
import com.kcx.acg.api.TagApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.base.BusEvent;
import com.kcx.acg.bean.CommonBean;
import com.kcx.acg.bean.ProductToDayListBean;
import com.kcx.acg.bean.TagBean;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.manager.ShareManager;
import com.kcx.acg.manager.WorkManager;
import com.kcx.acg.utils.BottomDialogUtil;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.views.adapter.UserHomeAdapter;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.FullyGridLayoutManager;
import com.kcx.acg.views.view.GlideRoundTransform;
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
 * 兴趣专题
 * Created by jb on 2018/9/25.
 */
public class InterestProjectActivity extends BaseActivity implements CancelAdapt {
    private RequestOptions options;
    private ImageView iv_touxiang,iv_back,iv_share;
    private TextView tv_nickname, tv_content, tv_title;
    private Button btn_attention,btn_empty;
    private RelativeLayout rl_share;
    private MyStateButton myStateButton;
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;
    private ObservableScrollView mScrollView;
    private RelativeLayout rl_bg, rl_head,rl_back;
    private LoadingErrorView mLoadingErrorView;
    private LinearLayout ll_empty;

    private UserHomeAdapter userHomeAdapter;
    private int tagID;
    private int pageIndex = 1;
    private int pageSize = 10;
    private List<ProductToDayListBean.ReturnDataBean.ListBean> productToDayList;
    private boolean isAttentioned;
    private String attentionStr, photoUrl;


    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_interest_project, null);
    }

    //    @Override
    //    public void setTatusBar() {
    //        super.setTatusBar();
    //        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    //    }

    @Override
    public void initView() {
        super.initView();
        iv_back = findViewById(R.id.iv_back);
        iv_share = findViewById(R.id.iv_share);
        rl_share = findViewById(R.id.rl_share);
        iv_touxiang = findViewById(R.id.iv_touxiang);
        tv_nickname = findViewById(R.id.tv_nickname);
        tv_content = findViewById(R.id.tv_content);
        tv_title = findViewById(R.id.tv_title);
        myStateButton = findViewById(R.id.myStateButton);
        btn_attention = findViewById(R.id.btn_attention);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRefreshLayout = findViewById(R.id.mRefreshLayout);
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        mScrollView = findViewById(R.id.mScrollView);
        rl_bg = findViewById(R.id.rl_bg);
        rl_head = findViewById(R.id.rl_head);
        ll_empty = findViewById(R.id.ll_empty);
        btn_empty = findViewById(R.id.btn_empty);
        btn_empty.setVisibility(View.GONE);
        mLoadingErrorView = findViewById(R.id.loadingErrorView);
        rl_back = findViewById(R.id.rl_back);
    }

    @Override
    public void setListener() {
        super.setListener();
        rl_back.setOnClickListener(this);
        rl_share.setOnClickListener(this);
        iv_touxiang.setOnClickListener(this);

        //加载更多
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex=pageIndex+1;
                getProductToDayList(tagID, pageIndex, pageSize);
                refreshLayout.finishLoadMore();
            }
        });

        mLoadingErrorView.setOnReloadListener(new LoadingErrorView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                getTag(tagID);
                productToDayList.clear();
                getProductToDayList(tagID, 1, pageSize);

            }
        });


        //滑动事件回调监听（一次滑动的过程一般会连续触发多次）
        mScrollView.setOnScrollListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScroll(int oldy, int dy, boolean isUp) {
                LogUtil.e("onScroll", "isUp=" + isUp + "+y=" + dy);
                if (!isUp && dy <= 300) { //渐变效果
                    //                    rl_head.setBackgroundResource(R.color.white);
                    //                    rl_bg.setBackgroundResource(R.color.white);
                    //                    titleAlphaChange(dy, 300);

                } else if (!isUp && dy > 300) {//手指往上滑
                    rl_bg.setBackgroundResource(R.color.white);
                    rl_head.setBackgroundResource(R.color.white);
                    iv_back.setBackgroundResource(R.mipmap.icon_back);
                    iv_share.setBackgroundResource(R.mipmap.icon_share_gray);
                    tv_title.setVisibility(View.VISIBLE);


                } else if (isUp && dy < 50) {//返回顶部，手指往下滑
                    rl_bg.setBackgroundResource(R.mipmap.interest_bg);
                    rl_head.setBackgroundResource(R.color.transparent);
                    iv_back.setBackgroundResource(R.mipmap.icon_back_white);
                    iv_share.setBackgroundResource(R.mipmap.icon_share_white);
                    tv_title.setVisibility(View.INVISIBLE);

                } else if (isUp && dy >= 50 && dy < 350) {//返回顶部，手指往下滑
                    //                    titleAlphaChange(dy, 350);
                }
            }
        });

        myStateButton.setOnInnerClickeListener(new MyStateButton.ButtonClickListener() {
            @Override
            public void innerClick() {
                attentionStr = btn_attention.getText().toString().trim();
                if (isAttentioned) {
                    new BottomDialogUtil(InterestProjectActivity.this,
                            R.layout.dialog_bottom,
                            getString(R.string.attentionAndFans_hint1),
                            getString(R.string.attentionAndFans_hint2),
                            getString(R.string.attentionAndFans_hint3),
                            new BottomDialogUtil.BottonDialogListener() {
                                @Override
                                public void onItemListener() {
                                    myStateButton.setOnInnerStart();
                                    addMemberTag(tagID, 0);
                                }
                            });
                } else {
                    myStateButton.setOnInnerStart();
                    addMemberTag(tagID, 1);
                }
            }
        });

    }


    private void titleAlphaChange(int dy, int headerHeight) {//设置标题栏透明度变化
        float percent = (float) Math.abs(dy) / Math.abs(headerHeight);
        //如果是设置背景透明度，则传入的参数是int类型，取值范围0-255
        //如果是设置控件透明度，传入的参数是float类型，取值范围0.0-1.0
        //这里我们是设置背景透明度就好，因为设置控件透明度的话，返回ICON等也会变成透明的。
        //alpha 值越小越透明
        int alpha = (int) (percent * 255);
        LogUtil.e("alpha", alpha + "///");
        rl_head.getBackground().setAlpha(alpha);//设置控件背景的透明度，传入int类型的参数（范围0~255）
        rl_bg.getBackground().setAlpha(alpha);//设置控件背景的透明度，传入int类型的参数（范围0~255）

    }

    @Override
    public void initData() {
        super.initData();
        options = new RequestOptions()
                .placeholder(R.drawable.img_holder_5dp)  //预加载图片
                .error(R.drawable.img_holder_5dp)  //加载失败图片
                .priority(Priority.HIGH)
                .transform(new GlideRoundTransform(InterestProjectActivity.this, 5));

        productToDayList = new ArrayList<ProductToDayListBean.ReturnDataBean.ListBean>();
        tagID = getIntent().getIntExtra("tagID", 0);
        initRecyclerView(productToDayList);
        getTag(tagID);
        getProductToDayList(tagID, pageIndex, pageSize);

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

            case R.id.rl_share:
                ShareManager.getInstances().share(2,tagID, InterestProjectActivity.this);
                break;

        }
    }


    /***
     * 初始列表
     */
    private void initRecyclerView(final List<ProductToDayListBean.ReturnDataBean.ListBean> listData) {
        FullyGridLayoutManager mgr = new FullyGridLayoutManager(InterestProjectActivity.this, 2);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mgr);
        userHomeAdapter = new UserHomeAdapter(InterestProjectActivity.this, listData);
        // 设置adapter
        mRecyclerView.setAdapter(userHomeAdapter);

        userHomeAdapter.setOnItemClickListener(new UserHomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                WorkManager.getInstances().goToWhere(InterestProjectActivity.this, listData.get(position).getCanViewType(), listData.get(position).getProductID());
            }
        });
    }

    private void getTag(int tagID) {
        TagApi tagApi = new TagApi(this, new HttpOnNextListener<TagBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(TagBean tagBean) {
                mLoadingErrorView.setVisibility(View.GONE);
                if (tagBean.getErrorCode() == 200) {
                    photoUrl = tagBean.getReturnData().getTagPhoto();
                    isAttentioned = tagBean.getReturnData().isIsAttentioned();
                    if (isAttentioned) {
                        btn_attention.setText(R.string.beAttention);
                        btn_attention.setTextColor(getResources().getColor(R.color.white));
                        btn_attention.setBackgroundResource(R.drawable.shape_be_attention_bg);
                    } else {
                        btn_attention.setText(R.string.addAttention);
                        btn_attention.setTextColor(getResources().getColor(R.color.pink_hint));
                        btn_attention.setBackgroundResource(R.drawable.shape_white_bg2);
                    }
                    tv_title.setText(tagBean.getReturnData().getName());
                    tv_nickname.setText(tagBean.getReturnData().getName());
                    tv_content.setText(tagBean.getReturnData().getTagDesc());
                    Glide.with(InterestProjectActivity.this).load(photoUrl).apply(options).into(iv_touxiang);
                } else {
                    CustomToast.showToast(tagBean.getErrorMsg());
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
        tagApi.setTagID(tagID);
        HttpManager.getInstance().doHttpDeal(InterestProjectActivity.this, tagApi);
    }

    /**
     * 获取兴趣列表接口
     *
     * @param tagID
     * @param pageIndex
     * @param pageSize
     */
    private void getProductToDayList(int tagID, final int pageIndex, int pageSize) {
        ProductToDayListApi productToDayListApi = new ProductToDayListApi(this, new HttpOnNextListener<ProductToDayListBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(ProductToDayListBean productToDayListBean) {
                if (productToDayListBean.getErrorCode() == 200) {
                    productToDayList.addAll(productToDayListBean.getReturnData().getList());
                    if (productToDayList.size() == 0){
                        ll_empty.setVisibility(View.VISIBLE);
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
        productToDayListApi.setTagId(tagID);
        productToDayListApi.setPageIndex(pageIndex);
        productToDayListApi.setPageSize(pageSize);
        HttpManager.getInstance().doHttpDeal(InterestProjectActivity.this, productToDayListApi);

    }

    /**
     * 添加兴趣/关注
     * tagID : 标签ID; type:(0:取消关注；1：关注)
     *
     * @param tagID
     * @param type
     */
    private void addMemberTag(int tagID, final int type) {

        AddMemberTagApi addMemberTagApi = new AddMemberTagApi(this, new HttpOnNextListener<CommonBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(CommonBean commonBean) {
                if (commonBean.getErrorCode() == 200) {
                    EventBus.getDefault().post(new BusEvent(BusEvent.INTEREST_ATTENTION, true));
                    CustomToast.showToast(commonBean.getErrorMsg());
                    if (type == 0) {
                        isAttentioned = false;
                        btn_attention.setText(R.string.addAttention);
                        btn_attention.setTextColor(getResources().getColor(R.color.pink_hint));
                        btn_attention.setBackgroundResource(R.drawable.shape_white_bg_3dp);
                    } else {
                        isAttentioned = true;
                        btn_attention.setText(R.string.beAttention);
                        btn_attention.setTextColor(getResources().getColor(R.color.white));
                        btn_attention.setBackgroundResource(R.drawable.shape_be_attention_bg);
                    }

                } else if (commonBean.getErrorCode() == 402) {
//                    startDDMActivity(LoginActivity.class, false);
//                    btn_attention.setText(attentionStr);
                } else {
                    btn_attention.setText(attentionStr);
                }
                myStateButton.setOnInnerResult();
                return null;
            }

            @Override
            public RetryWhenNetworkException.Wrapper onError(Throwable e) {
                super.onError(e);
                myStateButton.setOnInnerResult();
                btn_attention.setText(attentionStr);
                return null;
            }
        });
        addMemberTagApi.setTagID(tagID);
        addMemberTagApi.setType(type);
        HttpManager.getInstance().doHttpDeal(InterestProjectActivity.this, addMemberTagApi);

    }


}
