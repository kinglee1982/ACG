package com.kcx.acg.views.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.common.collect.Lists;
import com.kcx.acg.R;
import com.kcx.acg.base.BaseFragment;
import com.kcx.acg.bean.TabEntity;
import com.kcx.acg.views.activity.MainActivity;
import com.kcx.acg.views.adapter.TabFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 */

public class FindFragment extends BaseFragment<MainActivity> implements CancelAdapt {
    private static final Integer[] STRING_TABS = {R.string.browse_string, R.string.watch_string};

    private View rootView;
    private CommonTabLayout tabLayout;
    private FragmentManager fragmentManager;
    private List<BaseFragment> fragments;
    private ViewPager mViewPager;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.find_fragment, container, false);

        tabLayout = rootView.findViewById(R.id.find_fragment_tablayout);
        mViewPager = rootView.findViewById(R.id.find_fragment_viewpager);
        mViewPager.setOffscreenPageLimit(2);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void initData() {
        ArrayList<CustomTabEntity> tabEntities = Lists.newArrayList();
        for (int i = 0; i < STRING_TABS.length; i++) {
            tabEntities.add(new TabEntity(getString(STRING_TABS[i]), 0, 0));
        }
        fragmentManager = getChildFragmentManager();
        Fragment browseFragment = new BrowseFragment();
        Fragment watchFragment = new WatchFragment();
        fragments = Lists.newArrayList((BaseFragment) browseFragment, (BaseFragment) watchFragment);
        TabFragmentPagerAdapter adapter = new TabFragmentPagerAdapter(getChildFragmentManager(), (ArrayList<BaseFragment>) fragments);
        mViewPager.setAdapter(adapter);
        tabLayout.setTabData(tabEntities);

    }

    @Override
    public void initListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
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
    public void onClick(View v) {

    }

    /**
     * 切换指定fragment
     * @param i
     */
    public void changeFragment(int i) {
        mViewPager.setCurrentItem(i, true);
        tabLayout.setCurrentTab(i);
    }


}
