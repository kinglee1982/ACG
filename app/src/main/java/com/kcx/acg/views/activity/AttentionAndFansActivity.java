package com.kcx.acg.views.activity;

import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.common.collect.Lists;
import com.kcx.acg.R;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.base.BaseFragment;
import com.kcx.acg.bean.TabEntity;
import com.kcx.acg.views.adapter.TabFragmentPagerAdapter;
import com.kcx.acg.views.fragment.AttentionFragment;
import com.kcx.acg.views.fragment.FansFragment;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;


/**
 * 关注与粉丝
 * Created by jb on 2018/9/5.
 */
public class AttentionAndFansActivity extends BaseActivity implements CancelAdapt {

    private static final Integer[] STRING_TABS = {R.string.attentionAndFans_attention, R.string.attentionAndFans_fans};
    private CommonTabLayout mTabLayout;
    private LinearLayout ll_back;
    private List<BaseFragment> fragments;
    private ViewPager mViewPager;
    private FragmentManager fragmentManager;


    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_attention_and_fans, null);
    }

    @Override
    public void initView() {
        super.initView();
        ll_back = findViewById(R.id.ll_back);
        mTabLayout = findViewById(R.id.mTablayout);
        mViewPager = findViewById(R.id.mViewPager);
        //        mViewPager.setOffscreenPageLimit(2);//设置缓存的个数

    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position, true);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        ArrayList<CustomTabEntity> tabEntities = Lists.newArrayList();
        for (int i = 0; i < STRING_TABS.length; i++) {
            tabEntities.add(new TabEntity(getString(STRING_TABS[i]), 0, 0));
        }
        fragmentManager = getFragmentManager();
        Fragment attentionFragment = new AttentionFragment();
        Fragment fansFragment = new FansFragment();
        fragments = Lists.newArrayList((BaseFragment) attentionFragment, (BaseFragment) fansFragment);
        TabFragmentPagerAdapter adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), (ArrayList<BaseFragment>) fragments);
        mViewPager.setAdapter(adapter);
        mTabLayout.setTabData(tabEntities);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }

}
