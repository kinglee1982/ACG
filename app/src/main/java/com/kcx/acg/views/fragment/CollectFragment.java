package com.kcx.acg.views.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kcx.acg.R;
import com.kcx.acg.base.BaseFragment;
import com.kcx.acg.views.activity.MainActivity;

/**
 * 收藏
 * Created by jb on 2018/8/30.
 */

public class CollectFragment extends BaseFragment<MainActivity> {
    private View rootView;


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_collect,container,false);
        return rootView;
    }

    @Override
    public void initData() {

    }
}
