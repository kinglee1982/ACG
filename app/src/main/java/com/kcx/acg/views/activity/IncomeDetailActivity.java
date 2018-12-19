package com.kcx.acg.views.activity;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.kcx.acg.R;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.base.BaseFragment;
import com.kcx.acg.base.BusEvent;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.utils.PopUtil;
import com.kcx.acg.views.adapter.TabFragmentPagerAdapter;
import com.kcx.acg.views.fragment.ExpendFragment;
import com.kcx.acg.views.fragment.IncomeFragment;
import com.kcx.acg.views.view.NoScrollViewPager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * Created by jb on 2018/9/17.
 * 收益明细
 */
public class IncomeDetailActivity extends BaseActivity {
    private TextView tv_title, tv_earnings, tv_expend;
    private LinearLayout ll_back, ll_head;
    private ImageView iv_sanjiao;
    private RelativeLayout rl_earnings, rl_expend;
    private View view_expend, view_earnings;
    private NoScrollViewPager mViewPager;
    private Fragment incomeFragment, expendFragment;

    private List<BaseFragment> fragments;
    private TabFragmentPagerAdapter adapter;
    private int flag = 1;

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_earnings_detail, null);
    }

    @Override
    public void initView() {
        super.initView();
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.income_detail_title);
        ll_back = findViewById(R.id.ll_back);
        rl_earnings = findViewById(R.id.rl_earnings);
        rl_expend = findViewById(R.id.rl_expend);
        tv_earnings = findViewById(R.id.tv_earnings);
        tv_expend = findViewById(R.id.tv_expend);
        view_earnings = findViewById(R.id.view_earnings);
        view_expend = findViewById(R.id.view_expend);
        mViewPager = findViewById(R.id.viewPager);
        iv_sanjiao = findViewById(R.id.iv_sanjiao);
        ll_head = findViewById(R.id.ll_head);

    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);
        rl_earnings.setOnClickListener(this);
        rl_expend.setOnClickListener(this);

    }

    @Override
    public void initData() {
        super.initData();
        incomeFragment = new IncomeFragment();
        expendFragment = new ExpendFragment();
        fragments = Lists.newArrayList((BaseFragment) incomeFragment, (BaseFragment) expendFragment);

        adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), (ArrayList<BaseFragment>) fragments);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);  //初始化显示第一个页
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.rl_earnings:  //收益
                if (0 == mViewPager.getCurrentItem()) {
                    new PopUtil(IncomeDetailActivity.this, R.layout.pop_income, rl_earnings, flag, new PopUtil.OnItemClickListener() {
                        @Override
                        public void onItemListener(int i) {
                            flag = i;
                            checkItem(i);
                        }
                    }).showPop();

                } else {
                    mViewPager.setCurrentItem(0);
                    tv_earnings.setTextColor(getResources().getColor(R.color.black_333));
                    tv_expend.setTextColor(getResources().getColor(R.color.black_999));
                    view_earnings.setVisibility(View.VISIBLE);
                    view_expend.setVisibility(View.INVISIBLE);
                    iv_sanjiao.setBackgroundResource(R.mipmap.icon_sanjiao_xuan);
                }

                break;
            case R.id.rl_expend:  //支出
                mViewPager.setCurrentItem(1);
                tv_earnings.setTextColor(getResources().getColor(R.color.black_999));
                tv_expend.setTextColor(getResources().getColor(R.color.black_333));
                view_earnings.setVisibility(View.INVISIBLE);
                view_expend.setVisibility(View.VISIBLE);
                iv_sanjiao.setBackgroundResource(R.mipmap.icon_sanjiao_buxuan);
                break;
        }
    }

    private void checkItem(int type) {
        if (1 == type) {
            tv_earnings.setText(R.string.incomeDetail_totalIncome);
        } else if (2 == type) {
            tv_earnings.setText(R.string.incomeDetail_generalizeIncome);
        } else if (3 == type) {
            tv_earnings.setText(R.string.incomeDetail_productionIncome);
        }
        EventBus.getDefault().post(new BusEvent(BusEvent.REFRESH_INCOME, type));
    }

}
