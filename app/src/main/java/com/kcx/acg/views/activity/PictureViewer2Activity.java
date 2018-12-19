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
import com.kcx.acg.base.BusEvent;
import com.kcx.acg.bean.GetProductInfoBean;
import com.kcx.acg.bean.PictureBean;
import com.kcx.acg.bean.UserInfoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.manager.AccountManager;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.views.adapter.Image2Adapter;
import com.kcx.acg.views.adapter.ImageAdapter;
import com.kcx.acg.views.adapter.PicturePager2Adapter;
import com.kcx.acg.views.adapter.PicturePagerAdapter;
import com.kcx.acg.views.view.BottomDialog2;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.EcoGallery;
import com.kcx.acg.views.view.EcoGalleryAdapterView;
import com.monke.immerselayout.ImmerseFrameLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;

import static com.kcx.acg.views.view.BottomDialog2.SHOW_NOT_LOGIN_ACTION;
import static com.kcx.acg.views.view.BottomDialog2.SHOW_OPEN_VIP_ACTION;

/**
 */

public class PictureViewer2Activity extends BaseActivity implements CancelAdapt {

    public static final String KEY_PIC_POSITION = "key_pic_position";

    private View rootView;
    private ViewPager picViewPager;
    private EcoGallery ecoGallery;
    private ImageView backIv;
    private LinearLayout ecoLayout;
    private ImmerseFrameLayout mainLayout;
    private TextView countTv, tv_del;
    private TextView currnetPosTv;
    private PicturePager2Adapter pagerAdapter;
    private Image2Adapter smallAdapter;
    private boolean mVisible = true;
    private List<String> pathList;
    private int position;

    @Override
    public View setInitView() {
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_picture_viewer2, null);
        picViewPager = rootView.findViewById(R.id.pic_viewer_view_pager);
        ecoGallery = rootView.findViewById(R.id.pic_viewer_esc_gallery);
        backIv = rootView.findViewById(R.id.pic_viewer_back_iv);
        ecoLayout = rootView.findViewById(R.id.pic_eco_gallery_layout);
        mainLayout = rootView.findViewById(R.id.pic_main_layout);
        countTv = rootView.findViewById(R.id.pic_viewer_count_tv);
        currnetPosTv = rootView.findViewById(R.id.pic_viewer_current_pos_tv);
        tv_del = rootView.findViewById(R.id.tv_del);

        return rootView;
    }

    @Override
    public void setListener() {
        backIv.setOnClickListener(this);
        tv_del.setOnClickListener(this);
        pagerAdapter.setOnItemClickListener(new PicturePager2Adapter.OnItemClickListener() {
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
        pathList = (List<String>) getIntent().getSerializableExtra("pathList");
        position = getIntent().getIntExtra(KEY_PIC_POSITION, 0);

        countTv.setText(pathList.size() + "");
        currnetPosTv.setText((position + 1) + "");

        initAdapter(position);

    }

    private void initAdapter(int position) {
        pagerAdapter = new PicturePager2Adapter(this, pathList);
        smallAdapter = new Image2Adapter(this, pathList);
        picViewPager.setAdapter(pagerAdapter);
        ecoGallery.setAdapter(smallAdapter);
        ecoGallery.setUnselectedAlpha(1f);
        picViewPager.setCurrentItem(position, true);
        ecoGallery.setSelection(position, true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pic_viewer_back_iv:
                finish();
                break;
            case R.id.tv_del:
                EventBus.getDefault().post(new BusEvent(BusEvent.PICTURE_DEL, picViewPager.getCurrentItem()));
                pathList.remove(picViewPager.getCurrentItem());
                if (pathList == null && pathList.size() == 0) {
                    countTv.setText(0 + "");
                } else {
                    countTv.setText(pathList.size() + "");
                }
                initAdapter(picViewPager.getCurrentItem()-1);
                CustomToast.showToast(getString(R.string.picture_del_hint));
                break;
        }
    }
}
