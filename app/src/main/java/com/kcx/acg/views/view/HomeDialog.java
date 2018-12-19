package com.kcx.acg.views.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.just.agentweb.AgentWeb;
import com.kcx.acg.R;
import com.kcx.acg.base.SysApplication;
import com.kcx.acg.bean.AcceptActivityCouponBean;
import com.kcx.acg.bean.IsAcceptActivityCouponBean;
import com.kcx.acg.views.activity.AdvertisActivity;
import com.kcx.acg.views.activity.CreativeCenterActivity;
import com.kcx.acg.views.activity.IncomeActivity;
import com.kcx.acg.views.activity.MainActivity;
import com.kcx.acg.views.activity.WebViewActivity;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class HomeDialog extends Dialog {


    public HomeDialog(@NonNull Context context) {
        super(context);
    }

    public HomeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected HomeDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder implements View.OnClickListener {
        private Context context;
        private HomeDialog dialog;
        private View view;

        //红包
        private TextView RPConfirmTv;
        private TextView RPCancelTv;
        private TextView RPPriceTv;

        //优惠券
        private ImageView closeIv;
        private TextView couponPriceTv;
        private TextView couponTitleTv;
        private TextView incomePriceTv;
        private TextView incomeTitleTv;

        //推广
        private ImageView advIv;
        private TextView goTv;

        //创作中心
        private LinearLayout webLayout;
        private TextView okTv;

        //权限弹窗
        private TextView cancelTv;
        private TextView nextTv;

        public Builder(Context context) {
            this.context = context;
            dialog = new HomeDialog(context, R.style.ActionSheetDialogStyle);
        }

        public Builder setLayoutId(int layoutId) {
            view = LayoutInflater.from(context).inflate(layoutId, null);
            dialog.setContentView(view);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = (int) (SysApplication.mWidthPixels * 0.8); //设置宽度
            dialog.getWindow().setAttributes(lp);

            switch (layoutId) {
                case R.layout.dialog_red_packet_layout:
                    //红包
                    RPConfirmTv = view.findViewById(R.id.red_pocket_confirm_tv);
                    RPCancelTv = view.findViewById(R.id.red_pocket_cancel_tv);
                    RPPriceTv = view.findViewById(R.id.dialog_red_packet_price_tv);

                    RPConfirmTv.setOnClickListener(this);
                    RPCancelTv.setOnClickListener(this);
                    break;
                case R.layout.dialog_coupon_layout:
                    //优惠券
                    closeIv = view.findViewById(R.id.dialog_coupon_close_iv);
                    couponPriceTv = view.findViewById(R.id.coupon_price_tv);
                    couponTitleTv = view.findViewById(R.id.coupon_price_title_tv);
                    incomePriceTv = view.findViewById(R.id.income_price_tv);
                    incomeTitleTv = view.findViewById(R.id.income_title_tv);
                    closeIv.setOnClickListener(this);
                    break;
                case R.layout.dialog_spread_layout:
                    closeIv = view.findViewById(R.id.dialog_spread_close_iv);
                    advIv = view.findViewById(R.id.dialog_spread_adv_iv);
                    goTv = view.findViewById(R.id.dialog_spread_go_tv);
                    closeIv.setOnClickListener(this);
                    break;
                case R.layout.dialog_creative_layout:
                    webLayout = view.findViewById(R.id.dialog_creative_web_layout);
                    okTv = view.findViewById(R.id.dialog_creative_ok_tv);
                    lp.height = (int) (SysApplication.mHeightPixels * 0.8); //设置宽度
                    dialog.getWindow().setAttributes(lp);

                    okTv.setOnClickListener(this);
                    break;
                case R.layout.dialog_premission_layout:
                    cancelTv = view.findViewById(R.id.permission_cancel_tv);
                    nextTv = view.findViewById(R.id.permission_next_tv);

                    cancelTv.setOnClickListener(this);
                    nextTv.setOnClickListener(this);
                    break;
            }
            return this;
        }

        public Builder setWebUrl(String url) {
            AgentWeb.with((CreativeCenterActivity) context)//传入Activity
                    .setAgentWebParent((LinearLayout) webLayout, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams
                    .useDefaultIndicator(R.color.pink_ff8)
                    .createAgentWeb()//
                    .ready()
                    .go(url);
            return this;
        }

        public Builder setAdvUrl(String url, final String targetUrl) {
            MultiTransformation multi = new MultiTransformation(
                    new CenterCrop(),
                    new RoundedCornersTransformation(5, 0, RoundedCornersTransformation.CornerType.ALL));
            RequestOptions options = new RequestOptions().transform(multi);
            options.placeholder(R.drawable.img_holder);
            options.error(R.drawable.img_holder);
            Glide.with(context).load(url).apply(options).into(advIv);
            goTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
//                    Intent intent = new Intent(context, AdvertisActivity.class);
//                    intent.putExtra(AdvertisActivity.KEY_ADVERTIS_URL, targetUrl);
//                    ((MainActivity) context).startDDMActivity(intent, true);

                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra(WebViewActivity.URL, targetUrl + "#app");
                    ((MainActivity) context).startDDMActivity(intent, true);
                }
            });
            return this;
        }

        public Builder setCouponData(AcceptActivityCouponBean.ReturnDataBean couponBean) {
            couponPriceTv.setText(couponBean.getAmount());
            couponTitleTv.setText(couponBean.getName().split("\\(")[0]);
            incomePriceTv.setText(((int) couponBean.getIncomeAmount()) + "");
            return this;
        }

        public Builder setRedPacketData(IsAcceptActivityCouponBean.ReturnDataBean redPacketData) {
            RPPriceTv.setText(redPacketData.getCouponAmout());
            return this;
        }

        public HomeDialog builder() {
            return dialog;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.red_pocket_confirm_tv:
                case R.id.permission_next_tv:
                    dialog.cancel();
                    onClickListener.onConfrim();
                    break;
                case R.id.dialog_coupon_close_iv:
                case R.id.red_pocket_cancel_tv:
                case R.id.dialog_spread_close_iv:
                case R.id.dialog_creative_ok_tv:
                case R.id.permission_cancel_tv:
                    dialog.cancel();
                    break;
            }
        }


        private OnClickListener onClickListener;

        public void setOnClickListener(OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        public interface OnClickListener {
            void onConfrim();
        }
    }

}
