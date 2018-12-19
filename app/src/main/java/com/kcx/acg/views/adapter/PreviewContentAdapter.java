package com.kcx.acg.views.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kcx.acg.R;
import com.kcx.acg.base.SysApplication;
import com.kcx.acg.bean.GetProductInfoBean;
import com.kcx.acg.bean.PreviewBean;
import com.kcx.acg.utils.GlideBlurformation;

import java.util.HashMap;
import java.util.List;

//import jp.wasabeef.glide.transformations.BlurTransformation;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 */

public class PreviewContentAdapter extends DelegateAdapter.Adapter {

    public static final int FROM_PREVIEW = 0;
    public static final int FROM_DETAILS = 1;

    private Context context;

    public void setReturnDataBean(PreviewBean.ReturnDataBean returnDataBean) {
        this.returnDataBean = returnDataBean;
    }

    private PreviewBean.ReturnDataBean returnDataBean;
    private boolean isBlurImg;

    public void setBlurImg(boolean blurImg) {
        isBlurImg = blurImg;
    }

    public void setProductInfoBean(GetProductInfoBean.ReturnDataBean productInfoBean) {
        this.productInfoBean = productInfoBean;
    }

    private GetProductInfoBean.ReturnDataBean productInfoBean;
    private LayoutHelper layoutHelper;
    private int fromWhere;

    public PreviewContentAdapter(LayoutHelper layoutHelper, Context context, PreviewBean.ReturnDataBean returnDataBean, int fromWhere, boolean isBlurImg) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.fromWhere = fromWhere;
        this.returnDataBean = returnDataBean;
        this.isBlurImg = isBlurImg;
    }

    public PreviewContentAdapter(LayoutHelper layoutHelper, Context context, GetProductInfoBean.ReturnDataBean productInfoBean, int fromWhere) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.fromWhere = fromWhere;
        this.productInfoBean = productInfoBean;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.preview_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ImageView itemIV = viewHolder.itemIV;
        itemIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(view, position);
            }
        });
        ImageView coverIV = viewHolder.coverIv;
        switch (fromWhere) {
            case FROM_PREVIEW:
                if (returnDataBean != null) {
                    RequestOptions options1 = new RequestOptions()
                            .dontAnimate()
                            .centerCrop()
                            .skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .override(SysApplication.mWidthPixels / 3 - 30, SysApplication.mWidthPixels / 3 - 30)
                            .placeholder(R.drawable.img_holder)
                            .error(R.drawable.img_holder);
                    if (returnDataBean.getVideoCount() > 0) {
                        if (position == returnDataBean.getDetailList().size() - 1)
                            coverIV.setVisibility(View.VISIBLE);
                        else
                            coverIV.setVisibility(View.GONE);
                    } else {
                        coverIV.setVisibility(View.GONE);
                    }
                    itemIV.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, SysApplication.mWidthPixels / 3 - 30));
                    coverIV.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, SysApplication.mWidthPixels / 3 - 30));

                    if (isBlurImg) {
                        Glide.with(context).load(returnDataBean.getDetailList().get(position).getUrl())
                                .apply(options1)
                                .apply(RequestOptions.bitmapTransform(new BlurTransformation(30)))
                                .into(itemIV);
                    } else {
                        Glide.with(context).load(returnDataBean.getDetailList().get(position).getUrl())
                                .apply(options1)
                                .into(itemIV);
                    }
                }
                break;
            case FROM_DETAILS:
                coverIV.setVisibility(View.GONE);
                itemIV.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, SysApplication.mWidthPixels / 4));
                RequestOptions options2 = new RequestOptions()
                        .dontAnimate()
                        .centerCrop()
                        .override(SysApplication.mWidthPixels / 4, SysApplication.mWidthPixels / 4)
                        .placeholder(R.drawable.img_holder)
                        .error(R.drawable.img_holder);
                Glide.with(context).load(productInfoBean.getDetailList().get(position).getUrl())
                        .apply(options2)
                        .into(itemIV);
                break;
        }
    }

    @Override
    public int getItemCount() {
        switch (fromWhere) {
            case FROM_PREVIEW:
                return returnDataBean != null ? returnDataBean.getDetailList().size() : 0;
            case FROM_DETAILS:
                return productInfoBean != null ? productInfoBean.getDetailList().size() : 0;
            default:
                return 0;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemIV;
        private ImageView coverIv;

        public ViewHolder(View itemView) {
            super(itemView);
            itemIV = itemView.findViewById(R.id.preview_item_iv);
            coverIv = itemView.findViewById(R.id.preview_cover_iv);

        }
    }


    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
