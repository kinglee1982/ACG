package com.kcx.acg.views.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;
import com.kcx.acg.base.BaseFragment;
import java.util.ArrayList;
import java.util.List;

/**
 */

public class TabFragmentPagerAdapter<T> extends FragmentStatePagerAdapter {

    private FragmentManager fragmentManager;
    private List<BaseFragment<T>> fragments;

    public TabFragmentPagerAdapter(FragmentManager fm, ArrayList<BaseFragment<T>> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {

    }
}
