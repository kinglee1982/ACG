package com.kcx.acg.views.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.donkingliang.labels.LabelsView;
import com.google.common.collect.Lists;
import com.kcx.acg.R;
import com.kcx.acg.bean.GetAdvertisementInfoBean;
import com.kcx.acg.bean.GetProductInfoBean;
import com.kcx.acg.bean.ProductCommentsBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.views.activity.InterestProjectActivity;
import com.kcx.acg.views.activity.UserHomeActivity;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.MyStateButton;
import com.kcx.acg.views.view.TitleBarView;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 */

public class DetailsAdapter extends DelegateAdapter.Adapter {

    private Context context;
    private LayoutHelper layoutHelper;
    private Integer layoutId;
    private List<GetProductInfoBean.ReturnDataBean.DetailListBean> videoList;
    private List<String> labelList;
    private GetProductInfoBean.ReturnDataBean productInfoBean;
    private List<GetProductInfoBean.ReturnDataBean.DetailListBean> productInfoBeans;
    private ProductCommentsBean.ReturnDataBean productCommentsBean;
    private GetAdvertisementInfoBean.ReturnDataBean advertisBean;
    private ViewHolder headHolder;
    private boolean isAttentionedAuthor;

    public DetailsAdapter(Context context, LayoutHelper layoutHelper, Integer layoutId) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.layoutId = layoutId;
    }

    public DetailsAdapter(Context context, LayoutHelper layoutHelper, GetProductInfoBean.ReturnDataBean productInfoBean, Integer layoutId) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.layoutId = layoutId;
        this.productInfoBean = productInfoBean;
    }

    public DetailsAdapter(Context context, LayoutHelper layoutHelper, List<GetProductInfoBean.ReturnDataBean.DetailListBean> productInfoBeans, Integer layoutId) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.layoutId = layoutId;
        this.productInfoBeans = productInfoBeans;
    }

    public DetailsAdapter(Context context, LayoutHelper layoutHelper, GetAdvertisementInfoBean.ReturnDataBean advertisBean, Integer layoutId) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.layoutId = layoutId;
        this.advertisBean = advertisBean;
    }

    public DetailsAdapter(Context context, LayoutHelper layoutHelper, ProductCommentsBean.ReturnDataBean productCommentsBean, Integer layoutId) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.layoutId = layoutId;
        this.productCommentsBean = productCommentsBean;
    }

    public DetailsAdapter(Context context, LayoutHelper layoutHelper, Integer layoutId, List<GetProductInfoBean.ReturnDataBean.DetailListBean> videoList) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.layoutId = layoutId;
        this.videoList = videoList;
    }

    public DetailsAdapter(Context context, LayoutHelper layoutHelper, Integer layoutId, List<String> labelList, int type) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.layoutId = layoutId;
        this.labelList = labelList;
    }

    public void setProductInfoBean(GetProductInfoBean.ReturnDataBean productInfoBean) {
        this.productInfoBean = productInfoBean;
    }
    public void setProductInfoBeanList(List<GetProductInfoBean.ReturnDataBean.DetailListBean> productInfoBeans) {
        this.productInfoBeans = productInfoBeans;
    }

    public void setProductCommentsBean(ProductCommentsBean.ReturnDataBean productCommentsBean) {
        this.productCommentsBean = productCommentsBean;
    }

    public void setAdvertisBean(GetAdvertisementInfoBean.ReturnDataBean advertisBean) {
        this.advertisBean = advertisBean;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (layoutId) {
            case R.layout.work_details_head_layout:
                return new ViewHolder(LayoutInflater.from(context).inflate(layoutId, parent, false));
            case R.layout.work_details_pic_item_layout:
                return new PicHolder(LayoutInflater.from(context).inflate(layoutId, parent, false));
            case R.layout.work_details_video_item_layout:
                return new VideoHolder(LayoutInflater.from(context).inflate(layoutId, parent, false));
            case R.layout.work_details_label_layout:
                return new LabelHolder(LayoutInflater.from(context).inflate(layoutId, parent, false));
            case R.layout.preview_comment_head_layout:
                return new CommentHeadHolder(LayoutInflater.from(context).inflate(layoutId, parent, false));
            case R.layout.advertising_layout:
                return new AdvertisHolder(LayoutInflater.from(context).inflate(layoutId, parent, false));
            default:
                return null;
        }

    }

    public void setOnInnerUnFinish(int type) {
        if (type == 1) {
            headHolder.followStateBtn.setOnInnerUnFinish(context.getString(R.string.follow_string));
        } else {
            headHolder.followStateBtn.setOnInnerFinish(context.getString(R.string.browse_followed_msg));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (layoutId) {
            case R.layout.work_details_head_layout:
                headHolder = (ViewHolder) holder;
                RequestOptions mRequestOptions = RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.NONE);
                mRequestOptions.placeholder(R.mipmap.placehold_head);
                mRequestOptions.error(R.mipmap.placehold_head);
                if (productInfoBean != null) {
                    if (productInfoBean.getAuthor() != null) {
                        headHolder.headLayout.setVisibility(View.VISIBLE);
                        Glide.with(context).load(productInfoBean.getAuthor().getPhoto()).apply(mRequestOptions).into(headHolder.headIv);
                        headHolder.nameTv.setText(productInfoBean.getAuthor().getUserName());
                    }
                    switch (productInfoBean.getAuthor().getUserIdentify()) {
                        case Constants.Author.ID_F:
                            headHolder.vipIv.setVisibility(View.VISIBLE);
                            break;
                        case Constants.Author.UNKNOWN:
                        default:
                            headHolder.vipIv.setVisibility(View.GONE);
                            break;
                    }
                    isAttentionedAuthor = productInfoBean.isIsAttentionedAuthor();
                    if (productInfoBean.isIsAttentionedAuthor()) {
                        headHolder.followBtn.setBackgroundResource(R.drawable.shape_gray_ccc_bg);
                        headHolder.followBtn.setText(context.getString(R.string.browse_followed_msg));
                    } else {
                        headHolder.followBtn.setBackgroundResource(R.drawable.shape_pink_bg);
                        headHolder.followBtn.setText(context.getString(R.string.follow_string));
                    }
                    headHolder.titleTv.setText(productInfoBean.getProduct().getTitle());
                    headHolder.userIdTv.setText(productInfoBean.getProduct().getNaCode());
                    if (productInfoBean.getProduct().getDescription().isEmpty()) {
                        headHolder.detailsTxtTv.setVisibility(View.GONE);
                    } else {
                        headHolder.detailsTxtTv.setVisibility(View.VISIBLE);
                        headHolder.detailsTxtTv.setText(productInfoBean.getProduct().getDescription());
                    }
                    headHolder.followStateBtn.setOnInnerClickeListener(new MyStateButton.ButtonClickListener() {
                        @Override
                        public void innerClick() {
                            headHolder.followStateBtn.setOnInnerStart();
                            onFollowListener.onFollow(headHolder.followStateBtn, productInfoBean.getAuthor().getMemberID(), isAttentionedAuthor);
                        }
                    });

                    headHolder.backIv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onFollowListener.onBack();
                        }
                    });

                    headHolder.userIdTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ClipboardManager myClipboard;
                            myClipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                            ClipData myClip;
                            myClip = ClipData.newPlainText("text", productInfoBean.getProduct().getNaCode());
                            myClipboard.setPrimaryClip(myClip);
                            CustomToast.showToast(context.getString(R.string.copy_na));
                        }
                    });

                    headHolder.nameTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, UserHomeActivity.class);
                            intent.putExtra("memberID", productInfoBean.getAuthor().getMemberID());
                            context.startActivity(intent);
                        }
                    });
                    headHolder.headIv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, UserHomeActivity.class);
                            intent.putExtra("memberID", productInfoBean.getAuthor().getMemberID());
                            context.startActivity(intent);
                        }
                    });
                }

                break;
            case R.layout.work_details_pic_item_layout:
                final PicHolder picHolder = (PicHolder) holder;
                GetProductInfoBean.ReturnDataBean.DetailListBean item = productInfoBeans.get(position);
                RequestOptions options = new RequestOptions();
                options.error(R.drawable.home_img_holder);
                options.placeholder(R.drawable.home_img_holder);
                options.dontAnimate();
                options.centerInside();
                options.skipMemoryCache(true);
                options.priority(Priority.LOW);
                options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                String imgUrl = item.getUrl().replace("_S.", "_M.");
                Glide.with(context).load(imgUrl).apply(options).into(picHolder.picIv);

//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        e.printStackTrace();
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        picHolder.picLav.setVisibility(View.GONE);
//                        return false;
//                    }
//                }).into(new SimpleTarget<Drawable>() {
//                    @Override
//                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
//                        picHolder.picIv.setAdjustViewBounds(true);
//                        picHolder.picIv.setImageDrawable(resource);
//
//                    }
//                });
                picHolder.picIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onItemClick(view, position);
                    }
                });
                break;
            case R.layout.work_details_video_item_layout:
                final VideoHolder videoHolder = (VideoHolder) holder;
                GetProductInfoBean.ReturnDataBean.DetailListBean videoItem = videoList.get(position);
                RequestOptions VideoOptions = new RequestOptions();
                VideoOptions.placeholder(R.drawable.home_img_holder);
                VideoOptions.error(R.drawable.home_img_holder);
                VideoOptions.dontAnimate();

                Glide.with(context).load(videoItem.getUrl()).apply(VideoOptions).into(videoHolder.videoIv);
//
//                        .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        e.printStackTrace();
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        videoHolder.picLav.setVisibility(View.GONE);
//                        return false;
//                    }
//                }).into(videoHolder.videoIv);
                videoHolder.videoIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onItemClick(view, position);
                    }
                });
                break;
            case R.layout.work_details_label_layout:
                LabelHolder labelHolder = (LabelHolder) holder;
                if (productInfoBean != null) {
                    labelList = Lists.newArrayList();
                    if (productInfoBean.getTagList() != null) {
                        for (GetProductInfoBean.ReturnDataBean.TagListBean tag :
                                productInfoBean.getTagList()) {
                            labelList.add(tag.getTagName());
                        }
                        labelHolder.label.setLabels(labelList);
                        labelHolder.label.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
                            @Override
                            public void onLabelClick(TextView label, Object data, int position) {
                                Intent intent = new Intent(context, InterestProjectActivity.class);
                                intent.putExtra("tagID", productInfoBean.getTagList().get(position).getTagID());
                                context.startActivity(intent);
                            }
                        });
                    }
                }
                break;
            case R.layout.preview_comment_head_layout:
                CommentHeadHolder commentHeadHolder = (CommentHeadHolder) holder;
                if (productCommentsBean != null) {
                    commentHeadHolder.titleTv.setText(context.getString(R.string.comment_count_msg, productCommentsBean.getTotal()));
                }
                break;
            case R.layout.advertising_layout:
                final AdvertisHolder advertisHolder = (AdvertisHolder) holder;
                if (advertisBean == null) {
                    advertisHolder.setVisibility(false);
                    return;
                }
                advertisHolder.setVisibility(true);
                MultiTransformation multi = new MultiTransformation(
                        new CenterCrop(),
                        new RoundedCornersTransformation(5, 0, RoundedCornersTransformation.CornerType.ALL));
                RequestOptions advOptions = new RequestOptions();
                advOptions.placeholder(R.drawable.advertis_holder_bg);
                advOptions.error(R.drawable.advertis_holder_bg);
                advOptions.centerCrop();
                advOptions.transform(multi);
                Glide.with(context).load(advertisBean.getImageUrl()).apply(advOptions).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        e.printStackTrace();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        advertisHolder.advertisLav.setVisibility(View.GONE);
                        return false;
                    }
                }).into(advertisHolder.advertisIv);
                advertisHolder.advertisIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onAdvertisListener.onAdvertis(view, advertisBean.getTargetUrl());
                    }
                });
                break;
        }
    }

    public void setIsAttentionedAuthor(boolean isAttentionedAuthor) {
        this.isAttentionedAuthor = isAttentionedAuthor;
        productInfoBean.setIsAttentionedAuthor(isAttentionedAuthor);
//        if (isAttentionedAuthor) {
//            headHolder.followBtn.setBackgroundResource(R.drawable.shape_gray_ccc_bg);
//            headHolder.followBtn.setText(context.getString(R.string.browse_followed_msg));
//        } else {
//            headHolder.followBtn.setBackgroundResource(R.drawable.shape_pink_bg);
//            headHolder.followBtn.setText(context.getString(R.string.follow_string));
//        }
        headHolder.followStateBtn.setOnInnerResult();
    }

    @Override
    public int getItemCount() {
        switch (layoutId) {
            case R.layout.preview_comment_head_layout:
            case R.layout.work_details_head_layout:
            case R.layout.work_details_label_layout:
            case R.layout.advertising_layout:
                return 1;
            case R.layout.work_details_pic_item_layout:
                return productInfoBeans != null ? productInfoBeans.size() : 0;
            case R.layout.work_details_video_item_layout:
                return videoList != null ? videoList.size() : 0;
            default:
                return 0;
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView headIv;
        private ImageView vipIv;
        private TextView nameTv;
        private Button followBtn;
        private MyStateButton followStateBtn;
        private TextView titleTv;
        private TextView userIdTv;
        private TextView detailsTxtTv;
        private ImageView backIv;
        private LinearLayout headLayout;
        private TitleBarView titleBarView;


        public ViewHolder(View itemView) {
            super(itemView);
            switch (layoutId) {
                case R.layout.work_details_head_layout:
                    titleBarView = itemView.findViewById(R.id.work_details_head_titlebar);
                    headLayout = itemView.findViewById(R.id.work_details_head_linelayout);
                    headIv = itemView.findViewById(R.id.work_details_head_iv);
                    vipIv = itemView.findViewById(R.id.work_details_head_vip_iv);
                    backIv = itemView.findViewById(R.id.work_details_head_back_iv);
                    nameTv = itemView.findViewById(R.id.work_details_name_tv);
                    followBtn = itemView.findViewById(R.id.work_details_follow_btn);
                    followStateBtn = itemView.findViewById(R.id.work_details_follow_state_btn);
                    titleTv = itemView.findViewById(R.id.work_details_title_tv);
                    userIdTv = itemView.findViewById(R.id.work_details_user_id_tv);
                    detailsTxtTv = itemView.findViewById(R.id.work_details_txt_tv);

                    backIv = titleBarView.getIv_in_title_back();
                    backIv.setImageResource(R.mipmap.icon_back);
                    backIv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onFollowListener.onBack();
                        }
                    });
                    break;
            }
        }
    }

    class PicHolder extends RecyclerView.ViewHolder {
        private ImageView picIv;
        private LottieAnimationView picLav;

        public PicHolder(View itemView) {
            super(itemView);
            picIv = itemView.findViewById(R.id.work_details_pic_iv);
            picLav = itemView.findViewById(R.id.work_details_pic_lav);
        }
    }

    class VideoHolder extends RecyclerView.ViewHolder {
        private ImageView videoIv;
        private LottieAnimationView picLav;

        public VideoHolder(View itemView) {
            super(itemView);
            videoIv = itemView.findViewById(R.id.work_details_video_item_iv);
            picLav = itemView.findViewById(R.id.work_details_video_item_lav);

        }
    }

    class LabelHolder extends RecyclerView.ViewHolder {
        private LabelsView label;

        public LabelHolder(View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.work_details_labels_view);
        }
    }

    class CommentHeadHolder extends RecyclerView.ViewHolder {
        private TextView titleTv;
        private TextView commentTv;

        public CommentHeadHolder(View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.preview_comment_head_title_tv);
            commentTv = itemView.findViewById(R.id.preview_comment_head_reply_tv);
            commentTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onTextClickListener.onClick(view);
                }
            });
        }
    }

    class AdvertisHolder extends RecyclerView.ViewHolder {
        private ImageView advertisIv;
        private LottieAnimationView advertisLav;

        public AdvertisHolder(View itemView) {
            super(itemView);
            advertisIv = itemView.findViewById(R.id.advertising_iv);
            advertisLav = itemView.findViewById(R.id.advertising_lav);
        }

        public void setVisibility(boolean isVisible) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if (isVisible) {
                param.height = FrameLayout.LayoutParams.WRAP_CONTENT;
                param.width = FrameLayout.LayoutParams.MATCH_PARENT;
                itemView.setVisibility(View.VISIBLE);
            } else {
                itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }
    }

    private OnTextClickListener onTextClickListener;
    private OnItemClickListener onItemClickListener;
    private OnFollowListener onFollowListener;
    private OnAdvertisListener onAdvertisListener;

    public void setOnAdvertisListener(OnAdvertisListener onAdvertisListener) {
        this.onAdvertisListener = onAdvertisListener;
    }

    public void setOnFollowListener(OnFollowListener onFollowListener) {
        this.onFollowListener = onFollowListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnTextClickListener(OnTextClickListener onTextClickListener) {
        this.onTextClickListener = onTextClickListener;
    }

    public interface OnTextClickListener {
        void onClick(View view);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnFollowListener {
        void onFollow(View view, int memberID, boolean isAttentionedAuthor);

        void onBack();
    }

    public interface OnAdvertisListener {
        void onAdvertis(View view, String url);
    }
}
