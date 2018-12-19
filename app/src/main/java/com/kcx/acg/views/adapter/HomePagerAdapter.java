package com.kcx.acg.views.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
//import com.facebook.drawee.view.SimpleDraweeView;
import com.kcx.acg.R;
import com.kcx.acg.bean.HomeBean;
import com.kcx.acg.bean.UserInfoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.manager.AccountManager;
import com.kcx.acg.manager.WorkManager;
import com.kcx.acg.views.activity.MainActivity;
import com.kcx.acg.views.activity.PreviewActivity;
import com.kcx.acg.views.activity.VideoPlayerActivity2;
import com.kcx.acg.views.activity.WorkDetailsActivity;
import com.kcx.acg.views.view.CustomToast;
//import com.sunfusheng.GlideImageView;
//import com.sunfusheng.progress.OnProgressListener;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 */

public class HomePagerAdapter extends PagerAdapter {
    private View view;
    private Context context;
    private List<HomeBean.ReturnDataBean.ListBean> list;

    public HomePagerAdapter(ImageView[] imageViews, Context context) {
        this.context = context;
    }

    public HomePagerAdapter(List<HomeBean.ReturnDataBean.ListBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
//    private int mChildCount = 0;
//    @Override
//    public void notifyDataSetChanged() {
//        mChildCount = getCount();
//        super.notifyDataSetChanged();
//    }
//
//    @Override
//    public int getItemPosition(Object object) {
//        // 重写getItemPosition,保证每次获取时都强制重绘UI
//        if (mChildCount > 0) {
//            mChildCount--;
//            return POSITION_NONE;
//        }
//        return super.getItemPosition(object);
//    }

        @Override
    public Object instantiateItem(ViewGroup container, int position) {
        view = LayoutInflater.from(context).inflate(R.layout.home_item_layout, null);
        ImageView imageView = view.findViewById(R.id.home_item_iv);
        TextView textView = view.findViewById(R.id.home_item_tv);
        final LottieAnimationView lav = view.findViewById(R.id.home_item_lav);

        final HomeBean.ReturnDataBean.ListBean item = list.get(position);
        textView.setText(item.getTitle());

        RequestOptions options = new RequestOptions();
        options.error(R.drawable.home_img_holder);
        options.placeholder(R.drawable.home_img_holder);
        options.dontAnimate();

        Glide.with(context).load(item.getCoverPicUrl()).apply(options).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                lav.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                lav.setVisibility(View.GONE);
                return false;
            }
        }).into(imageView);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                context.startActivity(new Intent(context, VideoPlayerActivity2.class));
                WorkManager.getInstances().goToWhere(context, item.getCanViewType(), item.getId());
            }
        });
        container.addView(view);
        return view;

    }
}
