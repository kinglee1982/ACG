package com.kcx.acg.views.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.kcx.acg.R;
import com.kcx.acg.bean.GetProductInfoBean;


import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 */

public class PicturePagerAdapter extends PagerAdapter {
    private View view;
    private Context context;
    private ImageView[] imgViews;
    private List<GetProductInfoBean.ReturnDataBean.DetailListBean> productList;
    private boolean isHdvMode = false;

    public PicturePagerAdapter(ImageView[] imageViews, Context context) {
        this.imgViews = imageViews;
        this.context = context;
    }

    public PicturePagerAdapter(Context context, List<GetProductInfoBean.ReturnDataBean.DetailListBean> productList) {
        this.productList = productList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        ((ViewPager)container).removeView(imgViews[position % imgViews.length]);
        container.removeView((View) object);
    }

    public void setHdvMode(boolean isHdvMode){
        this.isHdvMode = isHdvMode;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        view = LayoutInflater.from(context).inflate(R.layout.picture_item_layout, null);
        final PhotoView photoView = view.findViewById(R.id.pic_item_photo_view);
        final LottieAnimationView lav = view.findViewById(R.id.pic_item_lav);
        GetProductInfoBean.ReturnDataBean.DetailListBean item = productList.get(position);

        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.home_img_holder);
        options.error(R.drawable.home_img_holder);
        options.dontAnimate();
        options.skipMemoryCache(true);
        options.priority(Priority.LOW);
        options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        String imgUrl;
        if(isHdvMode){
            imgUrl = item.getUrl().replace("_S.", "_L.");
        }else {
            imgUrl = item.getUrl().replace("_S.", "_M.");
        }
        Glide.with(context).load(imgUrl).apply(options).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                e.printStackTrace();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                lav.setVisibility(View.GONE);

                return false;
            }
        }).into(photoView);

        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {
                onItemClickListener.onItemClick(view);

            }

            @Override
            public void onOutsidePhotoTap() {

            }
        });

        container.addView(view);
        return view;

    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {

        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view);
    }
}
