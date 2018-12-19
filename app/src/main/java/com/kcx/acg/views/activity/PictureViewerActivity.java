package com.kcx.acg.views.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.kcx.acg.R;
import com.kcx.acg.api.GetProductInfoApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.bean.GetProductInfoBean;
import com.kcx.acg.bean.UserInfoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.manager.AccountManager;
import com.kcx.acg.views.adapter.ImageAdapter;
import com.kcx.acg.views.adapter.PicturePagerAdapter;
import com.kcx.acg.views.view.BottomDialog2;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.EcoGallery;
import com.kcx.acg.views.view.EcoGalleryAdapterView;
import com.monke.immerselayout.ImmerseFrameLayout;

import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;

import static com.kcx.acg.views.view.BottomDialog2.SHOW_NOT_LOGIN_ACTION;
import static com.kcx.acg.views.view.BottomDialog2.SHOW_OPEN_VIP_ACTION;

/**
 */

public class PictureViewerActivity extends BaseActivity implements CancelAdapt {

    public static final String KEY_PIC_POSITION = "key_pic_position";

    private View rootView;
    private ViewPager picViewPager;
    private EcoGallery ecoGallery;
    private ImageView backIv;
    private CheckedTextView hdvCtv;
    private LinearLayout hdvLayout;
    private LinearLayout ecoLayout;
    private ImmerseFrameLayout mainLayout;
    private TextView countTv;
    private TextView currnetPosTv;
    private int productID;
    private List<GetProductInfoBean.ReturnDataBean.DetailListBean> productList;
    private PicturePagerAdapter pagerAdapter;
    private ImageAdapter smallAdapter;
    private int currentPos = 1;
    private boolean mVisible = true;

    @Override
    public View setInitView() {
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_picture_viewer, null);
        picViewPager = rootView.findViewById(R.id.pic_viewer_view_pager);
        ecoGallery = rootView.findViewById(R.id.pic_viewer_esc_gallery);
        backIv = rootView.findViewById(R.id.pic_viewer_back_iv);
        hdvCtv = rootView.findViewById(R.id.pic_viewer_hdv_ctv);
        hdvLayout = rootView.findViewById(R.id.pic_viewer_hdv_layout);
        ecoLayout = rootView.findViewById(R.id.pic_eco_gallery_layout);
        mainLayout = rootView.findViewById(R.id.pic_main_layout);
        countTv = rootView.findViewById(R.id.pic_viewer_count_tv);
        currnetPosTv = rootView.findViewById(R.id.pic_viewer_current_pos_tv);

        return rootView;
    }

    @Override
    public void setListener() {
        backIv.setOnClickListener(this);
        hdvLayout.setOnClickListener(this);
        pagerAdapter.setOnItemClickListener(new PicturePagerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                mVisible = !mVisible;
                if (mVisible) {
                    ecoLayout.setVisibility(View.VISIBLE);
                } else {
                    ecoLayout.setVisibility(View.GONE);
                }
                setVis(mVisible);
            }
        });
        picViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                ecoGallery.setSelection(position, true);
                ecoGallery.setSelected(true);
                currnetPosTv.setText(position + 1 + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ecoGallery.setOnItemSelectedListener(new EcoGalleryAdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(EcoGalleryAdapterView<?> parent, View view, int position, long id) {
                picViewPager.setCurrentItem(position, true);
                currnetPosTv.setText(position + 1 + "");
                view.setAlpha(1);
            }

            @Override
            public void onNothingSelected(EcoGalleryAdapterView<?> parent) {

            }
        });
    }

    public void setVis(boolean mVisible) {
        if (!mVisible) { //全屏
            rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        } else { //非全屏
            rootView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            );
        }
    }

    @Override
    public void initData() {
        productID = getIntent().getIntExtra(PreviewActivity.KEY_PRODUCT_ID, 0);
        currentPos = getIntent().getIntExtra(KEY_PIC_POSITION, 1);
        getProductInfo(productID);

        productList = Lists.newArrayList();
        pagerAdapter = new PicturePagerAdapter(this, productList);
        smallAdapter = new ImageAdapter(this, productList);

        picViewPager.setAdapter(pagerAdapter);
        ecoGallery.setAdapter(smallAdapter);
        ecoGallery.setUnselectedAlpha(1f);
    }

    public void getProductInfo(final int productID) {
        GetProductInfoApi getProductInfoApi = new GetProductInfoApi(this);
        getProductInfoApi.setProductID(productID);
        getProductInfoApi.setListener(new HttpOnNextListener<GetProductInfoBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(GetProductInfoBean getProductInfoBean) {
                if (getProductInfoBean.getErrorCode() == 200) {
                    productList.addAll(getProductInfoBean.getReturnData().getDetailList());
                    pagerAdapter.notifyDataSetChanged();
                    smallAdapter.notifyDataSetChanged();
                    countTv.setText(getProductInfoBean.getReturnData().getDetailList().size() + "");
                    picViewPager.setCurrentItem(currentPos, true);
                    ecoGallery.setSelection(currentPos, true);
                }
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(this, getProductInfoApi);
    }


    BottomDialog2.Builder dialog2;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pic_viewer_back_iv:
                finish();
                break;
            case R.id.pic_viewer_hdv_layout:
                boolean isLogin = AccountManager.getInstances().isLogin(this, false);
                dialog2 = new BottomDialog2.Builder(this);
                dialog2.setLayoutId(R.layout.dialog_not_login_layout);
                if (!isLogin) {
                    dialog2.showWhat(SHOW_NOT_LOGIN_ACTION, getString(R.string.not_login_waring_msg), getString(R.string.not_login_btn_msg1), getString(R.string.not_login_btn_msg2)).show();
                } else {
                    UserInfoBean.ReturnDataBean userInfo = AccountManager.getInstances().getUserInfo(this).getReturnData();
                    if (userInfo.getUserLevel() == 1) {
                        dialog2.showWhat(SHOW_OPEN_VIP_ACTION, getString(R.string.not_login_open_vip_waring_msg), getString(R.string.not_login_open_vip_btn_msg), getString(R.string.not_login_btn_msg2)).show();
                    } else {
                        if (!hdvCtv.isChecked()) {
                            CustomToast.showToast(getString(R.string.pic_hdv_toast_msg));
                            pagerAdapter.setHdvMode(true);
                            pagerAdapter.notifyDataSetChanged();
                        }
                        hdvCtv.setChecked(!hdvCtv.isChecked());
                    }
                }

                dialog2.setOnNotLoginClickListener(new BottomDialog2.Builder.OnNotLoginClickListener() {
                    @Override
                    public void onBtnClick(View view, int showWhat) {
                        Intent intent = new Intent();
                        intent.putExtra(Constants.KEY_IS_FINISH_CUR_ACTIVITY, true);
                        if (showWhat == SHOW_NOT_LOGIN_ACTION) {
                            intent.setClass(PictureViewerActivity.this, LoginActivity.class);
                        }
                        if (showWhat == SHOW_OPEN_VIP_ACTION) {
                            intent.setClass(PictureViewerActivity.this, VipActivity.class);
                        }
                        startDDMActivity(intent, true);
                        dialog2.dismiss();
                    }
                });

                break;
        }
    }
}
