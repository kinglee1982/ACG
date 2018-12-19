package com.kcx.acg.views.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.AgentWebView;
import com.kcx.acg.R;
import com.kcx.acg.base.BaseActivity;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 */

public class AdvertisActivity extends BaseActivity implements CancelAdapt {

    public static final String KEY_ADVERTIS_URL = "key_advertis_url";

    private View rootView;
    private AgentWebView agentWebView;
    private AgentWeb agentWeb;
    private String advertisUrl;

    @Override
    public View setInitView() {
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_advertis, null);
        return rootView;
    }

    @Override
    public void initView() {
        advertisUrl = getIntent().getStringExtra(KEY_ADVERTIS_URL);
        agentWeb = AgentWeb.with(this)//传入Activity
                .setAgentWebParent((LinearLayout) rootView, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams

                .useDefaultIndicator(R.color.pink_ff8)
                .createAgentWeb()//
                .ready()
                .go(advertisUrl);
    }

    @Override
    public void onClick(View view) {

    }
}
