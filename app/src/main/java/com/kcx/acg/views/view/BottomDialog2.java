package com.kcx.acg.views.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.media.Image;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.kcx.acg.R;
import com.kcx.acg.manager.ShareManager;
import com.kcx.acg.views.activity.LoginActivity;
import com.kcx.acg.views.activity.PictureViewerActivity;
import com.kcx.acg.views.activity.PreviewActivity;
import com.kcx.acg.views.activity.VipActivity;
import com.kcx.acg.views.activity.WorkDetailsActivity;

import java.util.List;
import java.util.Map;

public class BottomDialog2 extends Dialog {

    public static final int SHOW_BUY_WITH_ACOIN = 1;
    public static final int SHOW_INSUFFICIENT_ACOIN = 2;
    public static final int SHOW_FREEZE_ACOIN = 3;
    public static final int SHOW_VIP_BUY_WITH_ACOIN = 4;
    public static final int SHOW_VIP_INSUFFICIENT_ACOIN = 5;
    public static final int SHOW_VIP_FREEZE_ACOIN = 6;

    public static final int SHOW_NOT_LOGIN_ACTION = 7;
    public static final int SHOW_OPEN_VIP_ACTION = 8;

    public BottomDialog2(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public BottomDialog2(@NonNull Context context) {
        super(context);
    }

    public static class Builder implements View.OnClickListener {

        private BottomDialog2 bottomDialog;
        private View view;
        private Context context;

        //buy works dialog
        private TextView titleTv;
        private TextView waringTv;
        private TextView hintTv;
        private LinearLayout buyOfCoinLayout;
        private LinearLayout buyOfCoin2Layout;
        private LinearLayout buyOfCoin3Layout;
        private LinearLayout openVipLayout;
        private LinearLayout openVip2Layout;
        private TextView openVip2Tv;
        private LinearLayout countDownLayout;
        private Button insufficientCoinBtn;
        private Button canNotBuyBtn;
        private ImageView dismissIv;

        private TextView buyACoinTv;
        private TextView buyACoinTv3;
        private TextView buyACoinTv4;

        //clear history dialog
        private Button clearConfirmBtn;
        private Button clearCancelBtn;

        //未登录弹窗
        private TextView notLoginWaringTv;
        private TextView notLoginBtn1;
        private TextView notLoginBtn2;
        private int notLoginShowWhat = 0;

        //分享
        private static final int[] SHARE_ICONS = {R.mipmap.icon_wechat, R.mipmap.icon_friend, R.mipmap.icon_qq, R.mipmap.icon_qzone, R.mipmap.icon_weibo, R.mipmap.icon_line,
                R.mipmap.icon_twwiter, R.mipmap.icon_facebook, R.mipmap.icon_messenger, R.mipmap.icon_whatsapp, R.mipmap.icon_link, R.mipmap.icon_more};
        private static final int[] SHARE_TITLES = {R.string.share_wechat_msg, R.string.share_friend_msg, R.string.share_qq_msg, R.string.share_qzone_msg, R.string.share_weibo_msg,
                R.string.share_line_msg, R.string.share_twwiter_msg, R.string.share_facebook_msg, R.string.share_messenger_msg, R.string.share_whatsapp_msg, R.string.share_link_msg,
                R.string.share_more_msg};

        private LinearLayout shareLayout;
        private TextView shareCancelTv;

        //清空浏览历史
        private TextView confirmTv;
        private TextView cancelTv;


        public Builder(Context context) {
            this.context = context;
            bottomDialog = new BottomDialog2(context, R.style.ActionSheetDialogStyle);
        }

        public BottomDialog2 setLayoutId(int layoutId) {
            view = LayoutInflater.from(context).inflate(layoutId, null);
            bottomDialog.setContentView(view);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
            view.setLayoutParams(layoutParams);
            bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
            bottomDialog.setCanceledOnTouchOutside(true);
            switch (layoutId) {
                case R.layout.dialog_buy_works_layout:
                    titleTv = view.findViewById(R.id.dialog_buy_title_tv);
                    waringTv = view.findViewById(R.id.dialog_buy_waring_msg_tv);
                    hintTv = view.findViewById(R.id.dialog_buy_hint_tv);
                    buyACoinTv = view.findViewById(R.id.dialog_buy_coin_tv);
                    buyACoinTv3 = view.findViewById(R.id.dialog_buy_coin_tv3);
                    buyACoinTv4 = view.findViewById(R.id.dialog_buy_coin_tv4);
                    buyOfCoinLayout = view.findViewById(R.id.dialog_buy_coin_layout);
                    buyOfCoin2Layout = view.findViewById(R.id.dialog_buy_coin2_layout);
                    buyOfCoin3Layout = view.findViewById(R.id.dialog_buy_coin_layout3);
                    openVipLayout = view.findViewById(R.id.dialog_buy_open_vip_layout);
                    openVip2Layout = view.findViewById(R.id.dialog_buy_open_vip2_layout);
                    openVip2Tv = view.findViewById(R.id.dialog_buy_open_vip2_tv);
                    countDownLayout = view.findViewById(R.id.dialog_buy_count_down_layout);
                    insufficientCoinBtn = view.findViewById(R.id.dialog_buy_insufficient_coin_btn);
                    canNotBuyBtn = view.findViewById(R.id.dialog_buy_can_not_btn);
                    dismissIv = view.findViewById(R.id.dialog_buy_works_dismiss_iv);

                    buyOfCoinLayout.setOnClickListener(this);
                    buyOfCoin2Layout.setOnClickListener(this);
                    buyOfCoin3Layout.setOnClickListener(this);
                    openVipLayout.setOnClickListener(this);
                    openVip2Layout.setOnClickListener(this);
                    countDownLayout.setOnClickListener(this);
                    insufficientCoinBtn.setOnClickListener(this);
                    canNotBuyBtn.setOnClickListener(this);
                    hintTv.setOnClickListener(this);
                    dismissIv.setOnClickListener(this);
                    break;
                case R.layout.dialog_clear_history:
                    clearConfirmBtn = view.findViewById(R.id.dialog_clear_history_confirm_btn);
                    clearCancelBtn = view.findViewById(R.id.dialog_clear_history_cancel_btn);
                    clearConfirmBtn.setOnClickListener(this);
                    clearCancelBtn.setOnClickListener(this);
                    break;
                case R.layout.dialog_unfollow_layout:
                    clearConfirmBtn = view.findViewById(R.id.dialog_unfollow_btn);
                    clearCancelBtn = view.findViewById(R.id.dialog_unfollow_cancel_btn);
                    clearConfirmBtn.setOnClickListener(this);
                    clearCancelBtn.setOnClickListener(this);
                    break;
                case R.layout.dialog_not_login_layout:
                    notLoginWaringTv = view.findViewById(R.id.not_login_waring_tv);
                    notLoginBtn1 = view.findViewById(R.id.not_login_btn_1);
                    notLoginBtn2 = view.findViewById(R.id.not_login_btn_2);
                    notLoginBtn1.setOnClickListener(this);
                    notLoginBtn2.setOnClickListener(this);
                    break;
                case R.layout.dialog_del_history_layout:
                    confirmTv = view.findViewById(R.id.del_history_confirm_tv);
                    cancelTv = view.findViewById(R.id.del_history_cancel_tv);
                    confirmTv.setOnClickListener(this);
                    cancelTv.setOnClickListener(this);
                    break;
                case R.layout.dialog_certification_layout:
                    confirmTv = view.findViewById(R.id.dialog_certification_confirm_tv);
                    cancelTv = view.findViewById(R.id.dialog_certification_cancel_tv);
                    confirmTv.setOnClickListener(this);
                    cancelTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onCancelListener.onCancel();
                        }
                    });
                    break;
                case R.layout.dialog_creative_center_delete_layout:
                    confirmTv = view.findViewById(R.id.dialog_creative_center_delete_confirm_tv);
                    cancelTv = view.findViewById(R.id.dialog_creative_center_delete_cancel_tv);
                    confirmTv.setOnClickListener(this);
                    cancelTv.setOnClickListener(this);
                    break;
            }

            return bottomDialog;
        }

        public BottomDialog2 showShare() {
            view = LayoutInflater.from(context).inflate(R.layout.dialog_share_layout, null);
            bottomDialog.setContentView(view);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
            view.setLayoutParams(layoutParams);
            bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
            bottomDialog.setCanceledOnTouchOutside(true);
            shareLayout = view.findViewById(R.id.dialog_share_layout);
            shareCancelTv = view.findViewById(R.id.dialog_share_cancel_tv);
            for (int i = 0; i < SHARE_ICONS.length; i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.share_item_layout, null);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(50, 0, 50, 0);
                view.setLayoutParams(lp);
                ImageView icon = view.findViewById(R.id.share_item_img_iv);
                TextView title = view.findViewById(R.id.share_item_title_tv);
                icon.setImageResource(SHARE_ICONS[i]);
                title.setText(SHARE_TITLES[i]);
                final int finalI = i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onItemClick(view, finalI);
                    }
                });
                shareLayout.addView(view);
            }

            shareCancelTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            return bottomDialog;
        }


        //转载作品的处理
        public BottomDialog2 showWhat(int what, boolean isShared, boolean isIsHaveCharged, String... btnTxt) {
            titleTv.setText(context.getString(R.string.buy_work_dialog_title_msg));
            switch (what) {
                case SHOW_BUY_WITH_ACOIN:
                    if (!isShared) {
                        //暂时隐藏
                        hintTv.setVisibility(View.VISIBLE);
                    }
                    titleTv.setVisibility(View.VISIBLE);
                    buyOfCoinLayout.setVisibility(View.VISIBLE);
                    buyACoinTv.setText(btnTxt[0]);
                    openVipLayout.setVisibility(View.VISIBLE);
                    waringTv.setVisibility(View.VISIBLE);
                    waringTv.setText(btnTxt[1]);
                    break;
                case SHOW_INSUFFICIENT_ACOIN:
                    titleTv.setVisibility(View.VISIBLE);
                    insufficientCoinBtn.setVisibility(View.VISIBLE);
                    waringTv.setVisibility(View.VISIBLE);
                    waringTv.setText(btnTxt[0]);
                    openVipLayout.setVisibility(View.VISIBLE);
                    if (!isShared) {
                        //暂时隐藏
                        hintTv.setVisibility(View.VISIBLE);
                    }
                    if (isIsHaveCharged) {
                        insufficientCoinBtn.setText(context.getString(R.string.dialog_freeze_coin_unused_msg));
                    } else {
                        insufficientCoinBtn.setText(context.getString(R.string.dialog_freeze_coin_unused_msg2));
                    }
                    break;
                case SHOW_FREEZE_ACOIN:
                    titleTv.setVisibility(View.VISIBLE);
                    waringTv.setVisibility(View.VISIBLE);
                    waringTv.setText(btnTxt[0]);
                    canNotBuyBtn.setVisibility(View.VISIBLE);
                    openVipLayout.setVisibility(View.VISIBLE);
                    break;
                case SHOW_VIP_BUY_WITH_ACOIN:
                    if (!isShared) {
                        //暂时隐藏
                        hintTv.setVisibility(View.VISIBLE);
                    }
                    titleTv.setVisibility(View.VISIBLE);
                    titleTv.setText(context.getString(R.string.dialog_freeze_coin_title_msg));
                    buyOfCoinLayout.setVisibility(View.VISIBLE);
                    buyACoinTv.setText(btnTxt[0]);
                    waringTv.setVisibility(View.VISIBLE);
                    waringTv.setText(btnTxt[1]);
                    break;
                case SHOW_VIP_INSUFFICIENT_ACOIN:
                    titleTv.setVisibility(View.VISIBLE);
                    titleTv.setText(context.getString(R.string.dialog_freeze_coin_title_msg));
                    insufficientCoinBtn.setVisibility(View.VISIBLE);
                    waringTv.setVisibility(View.VISIBLE);
                    waringTv.setText(btnTxt[0]);
                    if (!isShared) {
                        //暂时隐藏
                        hintTv.setVisibility(View.VISIBLE);
                    }
                    if (isIsHaveCharged) {
                        insufficientCoinBtn.setText(context.getString(R.string.dialog_freeze_coin_unused_msg));
                    } else {
                        insufficientCoinBtn.setText(context.getString(R.string.dialog_freeze_coin_unused_msg2));
                    }
                    break;
                case SHOW_VIP_FREEZE_ACOIN:
                    titleTv.setVisibility(View.VISIBLE);
                    titleTv.setText(context.getString(R.string.dialog_freeze_coin_title_msg));
                    waringTv.setVisibility(View.VISIBLE);
                    waringTv.setText(btnTxt[0]);
                    canNotBuyBtn.setVisibility(View.VISIBLE);
                    break;
            }
            return bottomDialog;
        }

        //原创作品的处理
        public BottomDialog2 showWhat(int what, boolean isShared, boolean isIsHaveCharged, int sourceType, boolean isDisCount, String... btnTxt) {
            titleTv.setText(context.getString(R.string.buy_the_original_works));
            switch (what) {
                case SHOW_BUY_WITH_ACOIN:
                    if (!isShared) {
                        //暂时隐藏
                        hintTv.setVisibility(View.VISIBLE);
                    }
                    titleTv.setVisibility(View.VISIBLE);
                    buyOfCoinLayout.setVisibility(View.VISIBLE);
                    buyACoinTv.setText(btnTxt[0]);
                    openVip2Tv.setText(btnTxt[2]);
                    waringTv.setVisibility(View.VISIBLE);
                    waringTv.setText(btnTxt[1]);
                    if (isDisCount) {
                        openVip2Layout.setVisibility(View.VISIBLE);
                    } else {
                        openVip2Layout.setVisibility(View.GONE);
                    }
                    break;
                case SHOW_INSUFFICIENT_ACOIN:
                    titleTv.setVisibility(View.VISIBLE);
                    insufficientCoinBtn.setVisibility(View.VISIBLE);
                    openVip2Tv.setText(btnTxt[1]);
                    waringTv.setVisibility(View.VISIBLE);
                    waringTv.setText(btnTxt[0]);
                    openVip2Layout.setVisibility(View.VISIBLE);
                    if (!isShared) {
                        //暂时隐藏
                        hintTv.setVisibility(View.VISIBLE);
                    }
                    if (isIsHaveCharged) {
                        insufficientCoinBtn.setText(context.getString(R.string.dialog_freeze_coin_unused_msg));
                    } else {
                        insufficientCoinBtn.setText(context.getString(R.string.dialog_freeze_coin_unused_msg2));
                    }
                    if (isDisCount) {
                        openVip2Layout.setVisibility(View.VISIBLE);
                    } else {
                        openVip2Layout.setVisibility(View.GONE);
                    }
                    break;
                case SHOW_FREEZE_ACOIN:
                    titleTv.setVisibility(View.VISIBLE);
                    waringTv.setVisibility(View.VISIBLE);
                    waringTv.setText(btnTxt[0]);
                    canNotBuyBtn.setVisibility(View.VISIBLE);
                    break;
                case SHOW_VIP_BUY_WITH_ACOIN:
                    if (!isShared) {
                        //暂时隐藏
                        hintTv.setVisibility(View.VISIBLE);
                    }
                    titleTv.setVisibility(View.VISIBLE);
                    titleTv.setText(context.getString(R.string.buy_the_original_works));
                    waringTv.setVisibility(View.VISIBLE);
                    waringTv.setText(btnTxt[1]);
                    if (isDisCount) {
                        buyOfCoinLayout.setVisibility(View.GONE);
                        buyOfCoin3Layout.setVisibility(View.VISIBLE);
                        buyACoinTv4.setText(btnTxt[2]);
                        buyACoinTv3.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        buyACoinTv3.getPaint().setAntiAlias(true);
                        buyACoinTv3.setText(btnTxt[3]);
                    } else {
                        buyOfCoinLayout.setVisibility(View.VISIBLE);
                        buyOfCoin3Layout.setVisibility(View.GONE);
                        buyACoinTv.setText(btnTxt[0]);
                    }
                    break;
                case SHOW_VIP_INSUFFICIENT_ACOIN:
                    titleTv.setVisibility(View.VISIBLE);
                    titleTv.setText(context.getString(R.string.buy_the_original_works));
                    insufficientCoinBtn.setVisibility(View.VISIBLE);
                    waringTv.setVisibility(View.VISIBLE);
                    waringTv.setText(btnTxt[0]);
                    if (!isShared) {
                        //暂时隐藏
                        hintTv.setVisibility(View.VISIBLE);
                    }
                    if (isIsHaveCharged) {
                        insufficientCoinBtn.setText(context.getString(R.string.dialog_freeze_coin_unused_msg));
                    } else {
                        insufficientCoinBtn.setText(context.getString(R.string.dialog_freeze_coin_unused_msg2));
                    }
                    break;
                case SHOW_VIP_FREEZE_ACOIN:
                    titleTv.setVisibility(View.VISIBLE);
                    titleTv.setText(context.getString(R.string.buy_the_original_works));
                    waringTv.setVisibility(View.VISIBLE);
                    waringTv.setText(btnTxt[0]);
                    canNotBuyBtn.setVisibility(View.VISIBLE);
                    break;
            }
            return bottomDialog;
        }

        public BottomDialog2 showWhat(int what, String... btnTxt) {
            notLoginShowWhat = what;
            switch (what) {
                case SHOW_NOT_LOGIN_ACTION:
                    notLoginWaringTv.setText(btnTxt[0]);
                    notLoginBtn1.setBackgroundResource(R.drawable.shape_pink_bg2);
                    notLoginBtn1.setText(btnTxt[1]);
                    notLoginBtn2.setText(btnTxt[2]);
                    break;
                case SHOW_OPEN_VIP_ACTION:
                    notLoginWaringTv.setText(btnTxt[0]);
                    notLoginBtn1.setBackgroundResource(R.drawable.shape_blue_bg2);
                    notLoginBtn1.setText(btnTxt[1]);
                    notLoginBtn2.setText(btnTxt[2]);
                    break;
            }
            return bottomDialog;
        }

        public BottomDialog2 create(List<Map<String, String>> list, int checkedPos) {
            bottomDialog.setContentView(view);
            bottomDialog.setCancelable(true);
            bottomDialog.setCanceledOnTouchOutside(true);
            return bottomDialog;
        }


        public void dismiss() {
            bottomDialog.dismiss();
        }

        public void show() {
            bottomDialog.show();
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.dialog_buy_coin_layout:
                    onBtnClickListener.onBuyWorkClick(view);
                    break;
                case R.id.dialog_buy_coin2_layout:
                    break;
                case R.id.dialog_buy_open_vip_layout:
                    onBtnClickListener.onVIPClick(view);
                    break;
                case R.id.dialog_buy_open_vip2_layout:
                    break;
                case R.id.dialog_buy_count_down_layout:
                    break;
                case R.id.dialog_buy_insufficient_coin_btn:
                    onBtnClickListener.onChargeClick(view);
                    break;
                case R.id.dialog_buy_can_not_btn:
                    onBtnClickListener.onCantBuyClick(view);
                    break;
                case R.id.dialog_buy_hint_tv:
                    onBtnClickListener.onShareClick(view);
                    break;
                case R.id.dialog_buy_works_dismiss_iv:
                    bottomDialog.dismiss();
                    break;
                case R.id.dialog_clear_history_confirm_btn:
                case R.id.dialog_unfollow_btn:
                    onConfirmListener.onConfirm();
                    bottomDialog.dismiss();
                    break;
                case R.id.dialog_clear_history_cancel_btn:
                case R.id.dialog_unfollow_cancel_btn:
                    bottomDialog.dismiss();
                    break;
                case R.id.not_login_btn_1:
                    onNotLoginClickListener.onBtnClick(view, notLoginShowWhat);
                    break;
                case R.id.not_login_btn_2:
                    dismiss();
                    break;
                case R.id.del_history_confirm_tv:
                case R.id.dialog_certification_confirm_tv:
                case R.id.dialog_creative_center_delete_confirm_tv:
                    onConfirmListener.onConfirm();
                    break;
                case R.id.del_history_cancel_tv:
                case R.id.dialog_certification_cancel_tv:
                case R.id.dialog_creative_center_delete_cancel_tv:
                    bottomDialog.dismiss();
                    break;
            }
        }

        private OnConfirmListener onConfirmListener;


        private OnCancelListener onCancelListener;
        private OnBtnClickListener onBtnClickListener;
        private OnItemClickListener onItemClickListener;
        private OnNotLoginClickListener onNotLoginClickListener;

        public void setOnNotLoginClickListener(OnNotLoginClickListener onNotLoginClickListener) {
            this.onNotLoginClickListener = onNotLoginClickListener;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
            this.onConfirmListener = onConfirmListener;
        }

        public interface OnConfirmListener {
            void onConfirm();
        }

        public void setOnCancelListener(OnCancelListener onCancelListener) {
            this.onCancelListener = onCancelListener;
        }

        public interface OnCancelListener {
            void onCancel();
        }

        public void setOnBtnClickListener(OnBtnClickListener onBtnClickListener) {
            this.onBtnClickListener = onBtnClickListener;
        }

        public interface OnBtnClickListener {
            void onBuyWorkClick(View view);

            void onVIPClick(View view);

            void onVIP2Click(View view);

            void onChargeClick(View view);

            void onCallMeClick(View view);

            void onCantBuyClick(View view);

            void onShareClick(View view);
        }

        public interface OnItemClickListener {
            void onItemClick(View view, int position);
        }

        public interface OnNotLoginClickListener {
            void onBtnClick(View view, int showWhat);
        }

    }
}
