package com.kcx.acg.views.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.LogUtils;
import com.google.common.collect.Lists;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kcx.acg.R;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.base.BaseFragment;
import com.kcx.acg.base.BusEvent;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.manager.AccountManager;
import com.kcx.acg.utils.FormatCurrentData;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.views.fragment.FindFragment;
import com.kcx.acg.views.fragment.HomeFragment;
import com.kcx.acg.views.fragment.MineFragment;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.RegisterSuccessDialog;
import com.monke.immerselayout.ImmerseLinearLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;


public class MainActivity extends BaseActivity implements CancelAdapt {

    private View rootView;
    private ArrayList<? extends BaseFragment<MainActivity>> fragments;
    private int mFrragmentIndex = 0;
    private boolean isInSwitch;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    //    public NoteRadioGroup mRbgHomeFragment;
    private ImmerseLinearLayout immerseLinearLayout;
    private String accessToken;

    private ImageView starIv;
    private ImageView cardIv;
    private ImageView mineIv;
    private LinearLayout guideIv;
    //    private LinearLayout container;
    private boolean starChecked = false;
    private boolean cardChecked = true;
    private boolean mineChecked = false;

    private FindFragment findFragment;
    private HomeFragment homeFragment;
    private MineFragment meFragment;
    public FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "nicoacg android");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "MainActivity");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "text");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
    }

    @Override
    public View setInitView() {
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
//        mRbgHomeFragment = rootView.findViewById(R.id.rbg_home_fragment);
        immerseLinearLayout = rootView.findViewById(R.id.main_immerse_frame_layout);
//        container = rootView.findViewById(R.id.container);

        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
//        lp.setMargins(0, result,0,100);
//        container.setLayoutParams(lp);
//        container.setPadding(0, result,0,100);

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();

        starIv = rootView.findViewById(R.id.star_iv);
        cardIv = rootView.findViewById(R.id.card_iv);
        mineIv = rootView.findViewById(R.id.mine_iv);
        guideIv = rootView.findViewById(R.id.main_guide_iv);

        return rootView;
    }

    @Override
    public void initView() {
        super.initView();


        boolean showGuide = (boolean) SPUtil.get(this, "showGuide", true);
        if (showGuide) {
            guideIv.setVisibility(View.VISIBLE);
        }

        findFragment = new FindFragment();
        homeFragment = new HomeFragment();
        meFragment = new MineFragment();
        fragments = Lists.newArrayList(findFragment, homeFragment, meFragment);
//        mRbgHomeFragment.check(R.id.rb_home);
        mFragmentTransaction.replace(R.id.container, homeFragment).commit();
    }

    @Override
    public void initData() {
        super.initData();
        playAnimIn(cardIv);
    }

    @Override
    public void setListener() {
        super.setListener();
        starIv.setOnClickListener(this);
        cardIv.setOnClickListener(this);
        mineIv.setOnClickListener(this);
        guideIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guideIv.setVisibility(View.GONE);
                SPUtil.put(MainActivity.this, "showGuide", false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.star_iv:
                mFrragmentIndex = 0;
                immerseLinearLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.pink_ff8));
                if (!starChecked) {
                    playAnimIn((ImageView) view);
                    EventBus.getDefault().post(new BusEvent(BusEvent.CLICK_STAR_TAB, true));
                }
                if (cardChecked) {
                    playAnimOut(cardIv);
                }
                if (mineChecked) {
                    playAnimOut(mineIv);
                }
                starChecked = true;
                cardChecked = false;
                mineChecked = false;
                break;
            case R.id.card_iv:
                mFrragmentIndex = 1;
                immerseLinearLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.home_bg_color));
                if (!cardChecked) {
                    playAnimIn((ImageView) view);
                }
                if (starChecked) {
                    playAnimOut(starIv);
                }
                if (mineChecked) {
                    playAnimOut(mineIv);
                }
                starChecked = false;
                cardChecked = true;
                mineChecked = false;
                break;
            case R.id.mine_iv:
                mFrragmentIndex = 2;
                accessToken = SPUtil.getString(MainActivity.this, Constants.ACCESS_TOKEN, "");
                if (TextUtils.isEmpty(accessToken)) {
                    changeStatusBar(false);
                } else {
                    changeStatusBar(true);
                }
                if (!mineChecked) {
                    playAnimIn((ImageView) view);
                    EventBus.getDefault().post(new BusEvent(BusEvent.CLICK_MINE_TAB, true));
                }
                if (starChecked) {
                    playAnimOut(starIv);
                }
                if (cardChecked) {
                    playAnimOut(cardIv);
                }
                starChecked = false;
                cardChecked = false;
                mineChecked = true;
                break;
            default:
                break;
        }
        switchFrag(mFrragmentIndex);
    }

    public void playAnimIn(ImageView imageView) {
        AnimationDrawable anim = new AnimationDrawable();
        String name = "";
        if (imageView.getId() == R.id.star_iv) {
            name = "star";
        }
        if (imageView.getId() == R.id.card_iv) {
            name = "card";
        }
        if (imageView.getId() == R.id.mine_iv) {
            name = "me";
        }
        for (int i = 1; i <= 16; i++) {
            int id = getResources().getIdentifier(name + i, "mipmap", getPackageName());
            Drawable drawable = getResources().getDrawable(id);
            anim.addFrame(drawable, 10);
        }
        anim.setOneShot(true);
        imageView.setImageDrawable(anim);
        anim.start();
    }

    public void playAnimOut(ImageView imageView) {
        AnimationDrawable anim = new AnimationDrawable();
        String name = "";
        if (imageView.getId() == R.id.star_iv) {
            name = "star";
        }
        if (imageView.getId() == R.id.card_iv) {
            name = "card";
        }
        if (imageView.getId() == R.id.mine_iv) {
            name = "me";
        }
        for (int i = 16; i >= 1; i--) {
            int id = getResources().getIdentifier(name + i, "mipmap", getPackageName());
            Drawable drawable = getResources().getDrawable(id);
            anim.addFrame(drawable, 10);
        }
        anim.setOneShot(true);
        imageView.setImageDrawable(anim);
        anim.start();
    }

    @Override
    public void onEventMainThread(BusEvent event) {
        super.onEventMainThread(event);
    }

    public synchronized void switchFrag(final int index) {
        mFrragmentIndex = index;
        if (isInSwitch)
            return;
        isInSwitch = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                mFragmentManager = getSupportFragmentManager();
                Fragment fragmentByTag = mFragmentManager.findFragmentByTag(mFrragmentIndex + "");
                if (fragmentByTag == null) {
                    fragmentByTag = fragments.get(mFrragmentIndex);
                }

                FragmentTransaction transaction = mFragmentManager.beginTransaction();

                if (!fragmentByTag.isAdded()) {
                    transaction.add(R.id.container, fragmentByTag, mFrragmentIndex + "");
                    for (int i = 0; i < fragments.size(); i++) {
                        if (i == mFrragmentIndex) {
                            transaction.show(fragments.get(i));
                        } else {
                            transaction.hide(fragments.get(i));
                        }
                    }
                } else {
                    for (int i = 0; i < fragments.size(); i++) {
                        if (i == mFrragmentIndex) {
                            transaction.show(fragments.get(i));
                        } else {
                            transaction.hide(fragments.get(i));
                        }
                    }
                }
                transaction.commit();
                isInSwitch = false;
            }
        }, 0);
    }

    /**
     * 改变个人中心的状态栏
     *
     * @param isLogin
     */
    public void changeStatusBar(boolean isLogin) {
        if (isLogin) {
            immerseLinearLayout.setBackgroundResource(R.mipmap.mine_bg);
        } else {
            immerseLinearLayout.setBackgroundResource(R.color.pink_hint);
        }
    }


    public void changeFindStatusBar() {
        immerseLinearLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.pink_ff8));
    }

    //切换到发现界面
    public void setFragment() {
        //        mFrragmentIndex = 0;
        //        immerseLinearLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.pink_ff8));
        //        if (!starChecked) {
        //            playAnimIn((ImageView) starIv);
        //        }
        //        if (cardChecked) {
        //            playAnimOut(cardIv);
        //        }
        //        if (mineChecked) {
        //            playAnimOut(mineIv);
        //        }
        //        starChecked = true;
        //        cardChecked = false;
        //        mineChecked = false;
        onClick(starIv);
    }

    //切换到发现界面
    public void setFragment(int card) {
        onClick(cardIv);
    }


    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                CustomToast.showToast(getString(R.string.back_toast));
                mExitTime = System.currentTimeMillis();
            } else {
                //                android.os.Process.killProcess(android.os.Process.myPid());  //获取PID
                //                System.exit(0);
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
