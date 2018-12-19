package com.kcx.acg.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.apkfuns.logutils.LogUtils;
import com.blankj.utilcode.util.BarUtils;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kcx.acg.R;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.manager.ActivityController;
import com.kcx.acg.manager.ActivityManagers;
import com.kcx.acg.manager.DialogManager;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.views.activity.LoginActivity;
import com.kcx.acg.views.activity.MainActivity;
import com.kcx.acg.views.activity.VerifyPrivacyPWActivity;
import com.kcx.acg.views.activity.VipActivity;
import com.kcx.acg.views.view.RegisterSuccessDialog;
import com.tmall.wireless.tangram.TangramBuilder;
import com.tmall.wireless.tangram.TangramEngine;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.autosize.internal.CancelAdapt;


/**
 * Created by Longer on 2016/10/26.
 */
public abstract class BaseActivity extends RxAppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000; //需要自己定义标志
    DisplayMetrics dm;
    public int mWidthPixels;
    public int mHeightPixels;
    //    private SweetAlertDialog mSweetAlertDialog;
    private View mMenuRoot;
    private SweetAlertDialog mSweetAlertDialog;

    private TangramBuilder.InnerBuilder innerBuilder;
    private TangramEngine engine;
    public VirtualLayoutManager layoutManager;
    public RecyclerView.RecycledViewPool viewPool;
    private boolean havePrivacyPw;

    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                field.setAccessible(true);
                field.setInt(getWindow().getDecorView(), Color.TRANSPARENT);  //改为透明
            } catch (Exception e) {
            }
        }
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT);
        BarUtils.setStatusBarAlpha(this, 1);
        setTatusBar();

        innerBuilder = TangramBuilder.newInnerBuilder(this);
        engine = innerBuilder.build();
        layoutManager = new VirtualLayoutManager(this);
        viewPool = new RecyclerView.RecycledViewPool();
        viewPool.setMaxRecycledViews(0, 20);
        View view = setInitView();
        setContentView(view);

        ActivityManagers.getInstance().onCreate(this); //把activity加入到活动管理器中
        ActivityController.addActivity(this);

        //Firebase


        //获取屏幕的宽高的像素
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        SysApplication.mWidthPixels = dm.widthPixels;
        SysApplication.mHeightPixels = dm.heightPixels;

        initView();
        initData();
        setListener();
    }

    public void setTatusBar() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    public void bindView(RecyclerView recyclerView) {
        engine.bindView(recyclerView);
    }

    public void setRecycledViewPool(RecyclerView recyclerView) {
        recyclerView.setRecycledViewPool(viewPool);
    }

    public void initView() {

    }

    public void setListener() {

    }

    public void initData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    /**
     * 返回当前activity(需要注意的是这里要是要返回子类的实例)
     **/
    public BaseActivity myActivity() {
        return this;
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        ActivityManagers.getInstance().onResume(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        //隐私密码
        SysApplication app = SysApplication.getInstance();
        if (app.isLocked) {
            app.isLocked = false;
            Intent intent = new Intent(this, VerifyPrivacyPWActivity.class);
            startActivity(intent);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * @param et 隐藏软键盘
     * @nama yl
     */
    public void Hidekeyboard(EditText et) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();// isOpen若返回true，则表示输入法打开
        if (isOpen) {
            ((InputMethodManager) et.getContext().getSystemService(
                    INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void showSoftInput(EditText editText) {
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    public void hideSoftInput() {
        InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (m.isActive()) {
            m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * @nama yl
     */
    public void showInputMethod() {
        //自动弹出键盘
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        //强制隐藏Android输入法窗口
        // inputManager.hideSoftInputFromWindow(edit.getWindowToken(),0);
    }


    public abstract View setInitView();


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    protected View getAnchorView() {
        return null;
    }

    PopupWindow mPopupWindow;

    protected void switchMenu() {
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(this);
            //mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mPopupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            mPopupWindow.setContentView(mMenuRoot);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mMenuRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchMenu();
                }
            });

        }
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        } else {
            mPopupWindow.showAsDropDown(getAnchorView());
        }
    }

    public void showLoading(String titleText, int type) {
        mSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mSweetAlertDialog.setTitleText(titleText);
        mSweetAlertDialog.setCancelable(true);
        mSweetAlertDialog.show();
    }

    public void showLoading() {
        mSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mSweetAlertDialog.setCancelable(true);
        mSweetAlertDialog.show();
    }

    public void showLoading(String titleText) {
        SweetAlertDialog mSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        mSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mSweetAlertDialog.setTitleText(titleText);
        mSweetAlertDialog.setCancelable(true);
        mSweetAlertDialog.show();
    }

    public void closeLoading() {
        if (mSweetAlertDialog != null && mSweetAlertDialog.isShowing()) {
            mSweetAlertDialog.dismissWithAnimation();
        }
    }

    public void setIsShowContentView(boolean isShow) {
        mIsShowContenttext = isShow;
    }

    private boolean mIsShowContenttext = false;

    public void disableShowInput(final EditText editText) {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            editText.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {//TODO: handle exception
            }
            try {
                method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {//TODO: handle exception
            }
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                editText.setSelection(editText.length());
            }
        });
    }

    public void setEditText(EditText editText, int position, String text) {
        if (position == 9) {
            if (!TextUtils.isEmpty(editText.getText()))
                editText.setText(editText.getText().subSequence(0, editText.getText().length() - 1));
        } else {
            editText.setText(editText.getText() + text);
        }
    }

    public void setEditText(EditText editText, int position, String text, int type) {
        if (position == 9) {
            if (!TextUtils.isEmpty(editText.getText()))
                editText.setText(editText.getText().subSequence(0, editText.getText().length() - 1));
        } else {
            editText.setText(editText.getText() + text);
        }
    }

    public String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    public String getCurrentTime(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    public String getCurrentTime(String format, int type) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);// HH:mm:ss
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        if (type == 1)
            c.add(Calendar.MONTH, -1);
        if (type == 2)
            c.add(Calendar.MONTH, +1);
        if (type == 3)
            c.add(Calendar.DAY_OF_MONTH, -1);
        if (type == 4)
            c.add(Calendar.DAY_OF_MONTH, +1);
        Date m = c.getTime();
        return simpleDateFormat.format(m);
    }

    public String getCurrentTime(String specifiedDay, String format, int type) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);// HH:mm:ss
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        c.setTime(date);
        if (type == 1)
            c.add(Calendar.MONTH, -1);
        if (type == 2)
            c.add(Calendar.MONTH, +1);
        if (type == 3)
            c.add(Calendar.DAY_OF_MONTH, -1);
        if (type == 4)
            c.add(Calendar.DAY_OF_MONTH, +1);
        Date m = c.getTime();
        return simpleDateFormat.format(m);
    }

    public String getMonthTime(String specifiedDay, String format, int type) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);// HH:mm:ss
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        c.setTime(date);
        if (type == 1)
            c.add(Calendar.MONTH, -1);
        if (type == 2)
            c.add(Calendar.MONTH, +1);
        if (type == 3)
            c.add(Calendar.DAY_OF_MONTH, -1);
        if (type == 4)
            c.add(Calendar.DAY_OF_MONTH, +1);
        Date m = c.getTime();
        return simpleDateFormat.format(m);
    }

    public void scrollTo(final ListView listView, final int position) {
        listView.post(new Runnable() {
            @Override
            public void run() {
                listView.smoothScrollToPosition(position);
            }
        });
    }

    public boolean mShouldScroll;
    //记录目标项位置
    public int mToPosition;

    public void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));

        if (position < firstItem) {
            // 如果跳转位置在第一个可见位置之前，就smoothScrollToPosition可以直接跳转
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 跳转位置在第一个可见项之后，最后一个可见项之前
            // smoothScrollToPosition根本不会动，此时调用smoothScrollBy来滑动到指定位置
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 如果要跳转的位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
    }

    @Subscribe
    public void onEventMainThread(BusEvent event) {
        LogUtils.d("main", this.getClass().getSimpleName());
        if (event.getType() == BusEvent.GO_HOME_CODE || event.getType() == BusEvent.LOGIN_SUCCESS) {
            if (!(this instanceof MainActivity)) {
//                finish();
            }
        }
        if (event.getType() == BusEvent.REGISTER_SUCCESS) {
            //注册成功
//            if ((boolean) SPUtil.get(this, Constants.IS_REGISTER, false)) {
//                SPUtil.put(this, Constants.IS_REGISTER, false);
            boolean isTop = isActivityTop(this.getClass(), this);
            if (isTop) {
                DialogManager.getInstances().isAcceptActivityCouponApi(this);
                new RegisterSuccessDialog(this, R.layout.dialog_register_success, null);
            }
        }
        if (event.getType() == BusEvent.LOGIN_SUCCESS) {
            boolean isTop = isActivityTop(this.getClass(), this);
            if (isTop)
                DialogManager.getInstances().isAcceptActivityCouponApi(this);
        }

    }

    public boolean isActivityTop(Class cls, Context context) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(cls.getName());
    }

    public void toActivity(Class target) {
        this.startActivity(new Intent(this, target));
    }

    public void toActivityByBoolean(Class target, String key, boolean value) {
        Intent intent = new Intent(this, target);
        intent.putExtra(key, value);
        this.startActivity(intent);
    }

    public String getStringByCoin(int coin) {
        NumberFormat format = new DecimalFormat(",###");
        return format.format(coin);
    }

    public String getStringByCoin2(int coin) {
        NumberFormat format = new DecimalFormat(",###.00");
        return format.format(coin);
    }

    /**
     * 跳转Activity的方法,传入我们需要的参数即可
     */
    public <T> void startDDMActivity(Class<T> activity, boolean isAinmain) {
        Intent intent = new Intent(myActivity(), activity);
        startActivity(intent);
        //是否需要开启动画(目前只有这种x轴平移动画,后续可以添加):
        if (isAinmain) {
            this.overridePendingTransition(R.anim.activity_translate_x_in, R.anim.activity_translate_x_out);
        }
    }

    /**
     * 跳转Activity的方法,传入我们需要的参数即可
     */
    public <T> void startDDMActivity(Intent intent, boolean isAinmain) {
        startActivity(intent);
        //是否需要开启动画(目前只有这种x轴平移动画,后续可以添加):
        if (isAinmain) {
            this.overridePendingTransition(R.anim.activity_translate_x_in, R.anim.activity_translate_x_out);
        }
    }

    /**
     * 跳转Activity的方法,传入我们需要的参数即可
     */
    public <T> void startDDMActivityForResult(Intent intent, int requestCode, boolean isAinmain) {
        startActivityForResult(intent, requestCode);
        //是否需要开启动画(目前只有这种x轴平移动画,后续可以添加):
        if (isAinmain) {
            this.overridePendingTransition(R.anim.activity_translate_x_in, R.anim.activity_translate_x_out);
        }
    }

    protected void share() {

    }

    protected void goLogin() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public <T> ObservableTransformer<T, T> setThread() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


}
