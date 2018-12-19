package com.kcx.acg.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.bean.UserInfoBean;


/**
 * Created by jb on 2018/2/11.
 */

public class BottomDialogUtil_Pay {
    private String money;
    private final UserInfoBean userInfoBean;
    private Context context;
    private int layout;
    private double pHeight;
    private Boolean cancelable = false;
    private BottonDialogListener mlistener = null;
    private Dialog bottomDialog;

    private RelativeLayout rl_alipay, rl_wechatpay, rl_creditcard, rl_balance;
    private ImageButton ib_close;
    private TextView tv_paymentAmount, tv_balance, tv_freezeAccount;
    private ImageView iv_jiantou_right, iv_balance;
    private boolean isChecked;

    public interface BottonDialogListener {
        void onItemListener(int i);
    }

    //余额 充值vip和a币
    public BottomDialogUtil_Pay(Context context,boolean isChecked, int layout, UserInfoBean userInfoBean, String money, BottonDialogListener listener) {
        this.context = context;
        this.layout = layout;
        this.mlistener = listener;
        this.userInfoBean = userInfoBean;
        this.money = money;
        this.isChecked = isChecked;
        initView();
    }

    private void initView() {
        bottomDialog = new Dialog(context, R.style.dialog);
        View contentView = LayoutInflater.from(context).inflate(layout, null);
        bottomDialog.setContentView(contentView);
        setData(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);

        //        if (type != 3 && type != 4) {
        //            WindowManager wm = (WindowManager) context
        //                    .getSystemService(Context.WINDOW_SERVICE); //为获取屏幕宽、高
        //            android.view.WindowManager.LayoutParams p = bottomDialog.getWindow().getAttributes();  //获取对话框当前的参数值
        //            p.height = (int) (wm.getDefaultDisplay().getHeight() * pHeight);   //高度设置为屏幕的0.75
        //            p.width = (int) (wm.getDefaultDisplay().getWidth() * 1);    //宽度设置为屏幕的1
        //            bottomDialog.getWindow().setAttributes(p);     //设置生效
        //        }

        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.setCancelable(cancelable);
        bottomDialog.setCanceledOnTouchOutside(cancelable);
        bottomDialog.show();
    }

    private void setData(View contentView) {
        tv_paymentAmount = contentView.findViewById(R.id.tv_paymentAmount);
        tv_paymentAmount.setText("¥" + money);
        tv_balance = contentView.findViewById(R.id.tv_balance);
        //        tv_balance.setText("余额（剩余:¥" + userInfoBean.getReturnData().getTotalIncome() + ")");
        tv_balance.setText("余额（剩余:¥" + StringUtil.toDecimalFormat(userInfoBean.getReturnData().getTotalIncome()) + ")");
        tv_freezeAccount = contentView.findViewById(R.id.tv_freezeAccount);  //冻结状态
        ib_close = contentView.findViewById(R.id.ib_close);
        rl_alipay = contentView.findViewById(R.id.rl_alipay);
        rl_wechatpay = contentView.findViewById(R.id.rl_wechatpay);
        rl_creditcard = contentView.findViewById(R.id.rl_creditcard);
        rl_balance = contentView.findViewById(R.id.rl_balance);
        iv_jiantou_right = contentView.findViewById(R.id.iv_jiantou_right);
        iv_balance = contentView.findViewById(R.id.iv_balance);

        if (isChecked){
            rl_balance.setVisibility(View.GONE);
        }else {
            rl_balance.setVisibility(View.VISIBLE);
        }
        ib_close.setOnClickListener(new clickListener());
        rl_alipay.setOnClickListener(new clickListener());
        rl_wechatpay.setOnClickListener(new clickListener());
        rl_creditcard.setOnClickListener(new clickListener());
        rl_balance.setOnClickListener(new clickListener());


        //余额不足
        if (Double.parseDouble(userInfoBean.getReturnData().getTotalIncome()) < Double.parseDouble(money)) {
            tv_balance.setText(context.getString(R.string.bottomDialogUtil_Pay_less) + "（" + context.getString(R.string.bottomDialogUtil_Pay_residue) + ":¥" + StringUtil.toDecimalFormat(userInfoBean.getReturnData().getTotalIncome()) + "）");
            tv_balance.setTextColor(context.getResources().getColor(R.color.black_999));
            iv_balance.setBackgroundResource(R.mipmap.icon_balance2);
            rl_balance.setEnabled(false);
        }
        int accountStatus = userInfoBean.getReturnData().getAccountStatus();
        LogUtil.e("accountStatus","///"+accountStatus);
        if (accountStatus == 5 || accountStatus == 7 || accountStatus == 8 || accountStatus == 9) { //封禁
            tv_freezeAccount.setText(R.string.bottomDialogUtil_Pay_banned);
            tv_freezeAccount.setVisibility(View.VISIBLE);
            iv_jiantou_right.setVisibility(View.INVISIBLE);
            tv_balance.setText("余额");
            tv_balance.setTextColor(context.getResources().getColor(R.color.black_999));
            iv_balance.setBackgroundResource(R.mipmap.icon_balance2);
            rl_balance.setEnabled(false);
        }
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ib_close:
                    bottomDialog.cancel();
                    break;
                case R.id.rl_alipay:  //支付宝
                    bottomDialog.cancel();
                    if (mlistener != null) {
                        mlistener.onItemListener(0);
                    }
                    break;
                case R.id.rl_wechatpay: //微信
                    bottomDialog.cancel();
                    if (mlistener != null) {
                        mlistener.onItemListener(1);
                    }
                    break;
                case R.id.rl_creditcard: //信用卡
                    bottomDialog.cancel();
                    if (mlistener != null) {
                        mlistener.onItemListener(2);
                    }
                    break;
                case R.id.rl_balance: //余额
                    bottomDialog.cancel();
                    if (mlistener != null) {
                        mlistener.onItemListener(3);
                    }
                    break;
            }
        }
    }
}
