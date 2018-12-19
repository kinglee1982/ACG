package com.kcx.acg.utils;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.kcx.acg.R;
import com.kcx.acg.impl.EixtSubmitOnClickListenner;
import com.kcx.acg.impl.SubmitOnClickListenner;

/**
 * jb
 */

public class MessageDialog extends DialogFragment {
    private static final String CANCEL = "out_cancel";
    private static final String USEANIM = "useAnim";
    private static final String ANIM = "anim";
    private static final String DIM = "dim";
    private static final String TITLE = "title";
    private static final String MSG = "msg";
    private static final String SUBMITSTR = "submitstr";
    private static final String EXITSTR = "exitStr";
    private static final String SUB_LISTENNER = "sub_listenner";
    private static final String EXIT_LISTENNER = "exit_listenner";

    private float dimAmount = 0.5f;//灰度深浅
    private boolean outCancel = false;//是否点击外部取消
    private boolean useAnim = true;//是否使用动画
    private String title;//标题
    private String msg;//消息
    private String submitStr;//确认按钮
    private String exitStr;//取消按钮
    @StyleRes
    private int animStyle;
    @LayoutRes
    protected int layoutId;
    /**
     * 确认按钮点击事件
     */
    private SubmitOnClickListenner submitOnClickListenner;
    /**
     * 取消按钮点击事件
     */
    private EixtSubmitOnClickListenner eixtSubmitOnClickListenner;
    private int color;

    public static MessageDialog init() {
        return new MessageDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog);
        layoutId = R.layout.dialog_message;
        //恢复保存的数据
        if (savedInstanceState != null) {
            outCancel = savedInstanceState.getBoolean(CANCEL);
            useAnim = savedInstanceState.getBoolean(USEANIM);
            animStyle = savedInstanceState.getInt(ANIM);
            dimAmount = savedInstanceState.getFloat(DIM);
            title = savedInstanceState.getString(TITLE);
            msg = savedInstanceState.getString(MSG);
            submitStr = savedInstanceState.getString(SUBMITSTR);
            exitStr = savedInstanceState.getString(EXITSTR);
            exitStr = savedInstanceState.getString(EXITSTR);
            submitOnClickListenner = savedInstanceState.getParcelable(SUB_LISTENNER);
            eixtSubmitOnClickListenner = savedInstanceState.getParcelable(EXIT_LISTENNER);
        }
    }

    /**
     * 屏幕旋转等导致DialogFragment销毁后重建时保存数据
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TITLE, title);
        outState.putString(MSG, msg);
        outState.putString(SUBMITSTR, submitStr);
        outState.putString(EXITSTR, exitStr);
        outState.putBoolean(CANCEL, outCancel);
        outState.putBoolean(USEANIM, useAnim);
        outState.putInt(ANIM, animStyle);
        outState.putFloat(DIM, dimAmount);
        outState.putParcelable(SUB_LISTENNER, submitOnClickListenner);
        outState.putParcelable(EXIT_LISTENNER, eixtSubmitOnClickListenner);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initParams();
    }

    private void initParams() {
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            //调节灰色背景透明度[0-1]，默认0.5f
            lp.dimAmount = dimAmount;
            if (useAnim) {
                if (animStyle == 0) {
                    animStyle = R.style.DefaultAnimation;
                }
                //设置dialog进入、退出的动画
                window.setWindowAnimations(animStyle);
            }
            window.setAttributes(lp);
        }

        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        setCancelable(outCancel);
    }


    private void initView(View view) {
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_message = (TextView) view.findViewById(R.id.tv_message);
        Button submit = (Button) view.findViewById(R.id.submit);
        Button exit = (Button) view.findViewById(R.id.exit);
        LinearLayout ll = view.findViewById(R.id.ll);
        TextView tv_line = view.findViewById(R.id.tv_line);

        if (TextUtils.isEmpty(title)){
            tv_title.setVisibility(View.GONE);
        }else{
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setText(title);
        }
        tv_message.setText(msg);
        if (!"".equals(null == submitStr || "null".equals(submitStr.trim()) ? "" : submitStr.trim())) {
            submit.setText(submitStr);
            submit.setTextColor(getResources().getColor(R.color.red));
        } else {
            submit.setVisibility(View.GONE);
            tv_line.setVisibility(View.GONE);

        }

        if ("".equals(null == exitStr || "null".equals(exitStr.trim()) ? "" : exitStr.trim())) {
            exit.setVisibility(View.GONE);
            tv_line.setVisibility(View.GONE);
        } else {
            exit.setText(exitStr);
        }


        if ("".equals(submitStr) && "".equals(exitStr)) {
            ll.setVisibility(View.GONE);

        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (submitOnClickListenner != null) {
                    submitOnClickListenner.onSubmitOnClick();
                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (eixtSubmitOnClickListenner != null) {
                    eixtSubmitOnClickListenner.onEixtSubmitOnClick();
                }
            }
        });

    }

    /**
     * 设置标题，默认显示"提 示"
     *
     * @param title
     * @return
     */
    public MessageDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 设置对话框内容
     *
     * @param msg 对话框内容
     * @return
     */
    public MessageDialog setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    /**
     * 设置确认按钮显示的文字
     *
     * @param submitStr 确认按钮文字
     * @return
     */
    public MessageDialog setSubmitStr(String submitStr) {
        this.submitStr = submitStr;
        return this;
    }

    /**
     * 设置确认按钮显示的文字颜色
     *
     * @param color 确认按钮文字颜色
     * @return
     */
    public MessageDialog setSubmitStrColor(int color) {
        this.color = color;
        return this;
    }

    /**
     * 设置取消按钮显示的文字
     *
     * @param exitStr 取消按钮文字
     * @return
     */
    public MessageDialog setExitStr(String exitStr) {
        this.exitStr = exitStr;
        return this;
    }

    /**
     * 设置是否可以点击外部关闭对话框，默认可以
     *
     * @param outCancel
     * @return
     */
    public MessageDialog outCancel(boolean outCancel) {
        this.outCancel = outCancel;
        return this;
    }

    /**
     * 设置是否可以使用动画，默认使用
     *
     * @param useAnim
     * @return
     */
    public MessageDialog useAnim(boolean useAnim) {
        this.useAnim = useAnim;
        return this;
    }

    /**
     * 设置背景灰度变化，默认0.5
     *
     * @param dimAmount
     * @return
     */
    public MessageDialog setDimAmount(float dimAmount) {
        this.dimAmount = dimAmount;
        return this;
    }

    /**
     * 设置动画效果
     *
     * @param animStyle
     * @return
     */
    public MessageDialog setAnimStyle(@StyleRes int animStyle) {
        this.animStyle = animStyle;
        return this;
    }

    /**
     * 设置确认按钮点击事件
     *
     * @param submitOnClickListenner
     * @return
     */
    public MessageDialog setSubmitOnClickListenner(SubmitOnClickListenner submitOnClickListenner) {
        this.submitOnClickListenner = submitOnClickListenner;
        return this;
    }

    /**
     * 设置取消按钮点击事件
     *
     * @param eixtSubmitOnClickListenner
     * @return
     */
    public MessageDialog setEixtSubmitOnClickListenner(EixtSubmitOnClickListenner eixtSubmitOnClickListenner) {
        this.eixtSubmitOnClickListenner = eixtSubmitOnClickListenner;
        return this;
    }

    /**
     * 显示
     *
     * @param manager
     * @return
     */
    public MessageDialog show(FragmentManager manager) {
        super.show(manager, String.valueOf(System.currentTimeMillis()));
        return this;
    }
}
