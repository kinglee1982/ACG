package com.kcx.acg.views.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.kcx.acg.R;
import com.kcx.acg.bean.GetAdvertisementInfoBean;
import com.kcx.acg.bean.ProductCommentsBean;
import com.kcx.acg.bean.PreviewBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.manager.AccountManager;
import com.kcx.acg.manager.SettingManager;
import com.kcx.acg.views.activity.MainActivity;
import com.kcx.acg.views.activity.RankActivity;
import com.kcx.acg.views.activity.UserHomeActivity;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.MyStateButton;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 */

public class TitleAdapter extends DelegateAdapter.Adapter {

    private Context context;
    private LayoutHelper layoutHelper;
    private int layoutId;
    private String titleMsg;
    private String userIdMsg;

    public void setLabels(List<PreviewBean.ReturnDataBean.TagListBean> labels) {
        this.labels = labels;
    }

    private List<PreviewBean.ReturnDataBean.TagListBean> labels;

    public void setReturnDataBean(PreviewBean.ReturnDataBean returnDataBean) {
        this.returnDataBean = returnDataBean;
    }

    public void setReturnDataBeanOfProductComments(ProductCommentsBean.ReturnDataBean returnDataBeanOfProductComments) {
        this.returnDataBeanOfProductComments = returnDataBeanOfProductComments;
    }

    private PreviewBean.ReturnDataBean returnDataBean;
    private ProductCommentsBean.ReturnDataBean returnDataBeanOfProductComments;

    public void setAdvertisBean(GetAdvertisementInfoBean.ReturnDataBean advertisBean) {
        this.advertisBean = advertisBean;
    }

    private GetAdvertisementInfoBean.ReturnDataBean advertisBean;
    private boolean isAttentionedAuthor;
    private TitleHolder titleHolder;

    public TitleAdapter(Context context, LayoutHelper layoutHelper, int layoutId) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.layoutId = layoutId;
    }

    public TitleAdapter(Context context, LayoutHelper layoutHelper, int layoutId, String titleMsg) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.layoutId = layoutId;
        this.titleMsg = titleMsg;
    }

    public TitleAdapter(Context context, LayoutHelper layoutHelper, int layoutId, PreviewBean.ReturnDataBean returnDataBean) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.layoutId = layoutId;
        this.returnDataBean = returnDataBean;
    }

    public TitleAdapter(Context context, LayoutHelper layoutHelper, int layoutId, ProductCommentsBean.ReturnDataBean returnDataBeanOfProductComments) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.layoutId = layoutId;
        this.titleMsg = titleMsg;
        this.returnDataBeanOfProductComments = returnDataBeanOfProductComments;
    }

    public TitleAdapter(Context context, LayoutHelper layoutHelper, int layoutId, GetAdvertisementInfoBean.ReturnDataBean advertisBean) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.layoutId = layoutId;
        this.titleMsg = titleMsg;
        this.advertisBean = advertisBean;
    }

    public TitleAdapter(Context context, LayoutHelper layoutHelper, int layoutId, String titleMsg, String userIdMsg, List<PreviewBean.ReturnDataBean.TagListBean> labels) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.layoutId = layoutId;
        this.titleMsg = titleMsg;
        this.userIdMsg = userIdMsg;
        this.labels = labels;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new TitleHolder(LayoutInflater.from(context).inflate(R.layout.browse_title_layout, parent, false));
        return new TitleHolder(LayoutInflater.from(context).inflate(layoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        titleHolder = (TitleHolder) holder;
        switch (layoutId) {
            case R.layout.browse_search_layout:
                titleHolder.rankTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, RankActivity.class);
                        context.startActivity(intent);
                    }
                });
                break;
            case R.layout.browse_title_layout:
                if (titleHolder.textView != null)
                    titleHolder.textView.setText(titleMsg);
                break;
            case R.layout.preview_title_layout:
                if (returnDataBean != null) {
                    titleHolder.textView.setText(returnDataBean.getProduct().getTitle());
                    titleHolder.previewUserIdTv.setText(returnDataBean.getProduct().getNaCode());
                    titleHolder.previewUserIdTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ClipboardManager myClipboard;
                            myClipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                            ClipData myClip;
                            myClip = ClipData.newPlainText("text", userIdMsg);
                            myClipboard.setPrimaryClip(myClip);
                            CustomToast.showToast(context.getString(R.string.copy_na));
                        }
                    });
                    titleHolder.previewLabelLayout.removeAllViews();
                    if (returnDataBean.getProduct().getSourceType() == 2) {
                        TextView textView = new TextView(context);
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(0, 0, 60, 0);
                        textView.setLayoutParams(layoutParams);
                        textView.setText(context.getString(R.string.original_works_msg));
//                        if (i >= 1)
//                            textView.setTextColor(ContextCompat.getColor(context, R.color.blue_6bb));
//                        else
                        textView.setTextColor(ContextCompat.getColor(context, R.color.pink_ff8));
                        titleHolder.previewLabelLayout.addView(textView);
                    }
                    if (returnDataBean.getTagList() != null) {
                        for (int i = 0; i < returnDataBean.getTagList().size(); i++) {
                            TextView textView = new TextView(context);
                            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(0, 0, 60, 0);
                            textView.setLayoutParams(layoutParams);
                            textView.setText(labels.get(i).getTagName());
//                        if (i >= 1)
//                            textView.setTextColor(ContextCompat.getColor(context, R.color.blue_6bb));
//                        else
                            textView.setTextColor(ContextCompat.getColor(context, R.color.blue_6bb));
                            titleHolder.previewLabelLayout.addView(textView);
                        }
                    }
                    titleHolder.textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            onTitleClickListener.onTitleClick(view);
                        }
                    });
                }

                break;
            case R.layout.browse_banner_layout:
                if (advertisBean == null) {
                    titleHolder.setVisibility(false);
                    return;
                }
                titleHolder.setVisibility(true);

                MultiTransformation multi = new MultiTransformation(
                        new CenterCrop(),
                        new RoundedCornersTransformation(5, 0, RoundedCornersTransformation.CornerType.ALL));
                RequestOptions options = new RequestOptions().transform(multi);
                options.placeholder(R.drawable.advertis_holder_bg);
                options.error(R.drawable.advertis_holder_bg);
                Glide.with(context).load(advertisBean.getImageUrl()).apply(options).into(titleHolder.bannerIv);
                titleHolder.bannerIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onAdvertisListener.onAdvertis(view, advertisBean.getTargetUrl());
                    }
                });
                break;
            case R.layout.preview_head_bottom_layout:
                RequestOptions mRequestOptions = RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.NONE);
                mRequestOptions.placeholder(R.mipmap.placehold_head);
                mRequestOptions.error(R.mipmap.placehold_head);
                if (returnDataBean != null) {
                    if (returnDataBean.getAuthor() != null) {
                        titleHolder.userLayout.setVisibility(View.VISIBLE);
                        Glide.with(context).load(returnDataBean.getAuthor().getPhoto()).apply(mRequestOptions).into(titleHolder.headIv);
                        titleHolder.nameTv.setText(returnDataBean.getAuthor().getUserName());
                        switch (returnDataBean.getAuthor().getUserIdentify()) {
                            case Constants.Author.ID_F:
                                titleHolder.vipIv.setVisibility(View.VISIBLE);
                                break;
                            case Constants.Author.UNKNOWN:
                            default:
                                titleHolder.vipIv.setVisibility(View.GONE);
                                break;
                        }
                    } else {
                        titleHolder.userLayout.setVisibility(View.GONE);
                    }
                    if (returnDataBean.getVideoCount() == 0) {
                        titleHolder.countTv.setText(context.getString(R.string.home_preview_works_count_msg2, returnDataBean.getPicCount()));
                    } else {
                        titleHolder.countTv.setText(context.getString(R.string.home_preview_works_count_msg, returnDataBean.getPicCount(), returnDataBean.getVideoCount()));
                    }
                    isAttentionedAuthor = returnDataBean.isIsAttentionedAuthor();

                    if (isAttentionedAuthor) {
                        titleHolder.followBtn.setBackgroundResource(R.drawable.shape_gray_ccc_bg);
                        titleHolder.followBtn.setText(context.getString(R.string.browse_followed_msg));
                    } else {
                        titleHolder.followBtn.setBackgroundResource(R.drawable.shape_pink_bg);
                        titleHolder.followBtn.setText(context.getString(R.string.follow_string));
                    }

                    titleHolder.followStateBtn.setOnInnerClickeListener(new MyStateButton.ButtonClickListener() {
                        @Override
                        public void innerClick() {
                            titleHolder.followStateBtn.setOnInnerStart();
                            onFollowListener.onFollow(titleHolder.followStateBtn, returnDataBean.getAuthor().getMemberID(), isAttentionedAuthor);
                        }
                    });
                    titleHolder.nameTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, UserHomeActivity.class);
                            intent.putExtra("memberID", returnDataBean.getAuthor().getMemberID());
                            context.startActivity(intent);
                        }
                    });
                    titleHolder.headIv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, UserHomeActivity.class);
                            intent.putExtra("memberID", returnDataBean.getAuthor().getMemberID());
                            context.startActivity(intent);
                        }
                    });
                }

                break;
            case R.layout.preview_comment_head_layout:
                if (returnDataBeanOfProductComments != null) {
                    titleHolder.commentCountTv.setText(context.getString(R.string.comment_count_msg, returnDataBeanOfProductComments.getTotal()));
                }
                break;
        }
    }

    public void setOnInnerUnFinish(int type) {
        if (type == 1) {
            titleHolder.followStateBtn.setOnInnerUnFinish(context.getString(R.string.follow_string));
        } else {
            titleHolder.followStateBtn.setOnInnerFinish(context.getString(R.string.browse_followed_msg));
        }
    }

    public void setIsAttentionedAuthor(boolean isAttentionedAuthor) {
        this.isAttentionedAuthor = isAttentionedAuthor;
        if (isAttentionedAuthor) {
            titleHolder.followBtn.setBackgroundResource(R.drawable.shape_gray_ccc_bg);
            titleHolder.followBtn.setText(context.getString(R.string.browse_followed_msg));
        } else {
            titleHolder.followBtn.setBackgroundResource(R.drawable.shape_pink_bg);
            titleHolder.followBtn.setText(context.getString(R.string.follow_string));
        }
        titleHolder.followStateBtn.setOnInnerResult();
    }

    public void setVisibility(boolean visibility) {
        titleHolder.setVisibility(visibility);
    }

    public void setLineVisibility(int visibility) {
        titleHolder.lineIv.setVisibility(visibility);
    }

    public void setRecommendVisibility(int visibility) {
        titleHolder.recommendLayout.setVisibility(visibility);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class TitleHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private TextView searchTv;
        private TextView previewUserIdTv;
        private LinearLayout previewLabelLayout;
        private LinearLayout searchLayout;
        private LinearLayout recommendLayout;
        private ImageView bannerIv;
        private TextView rankTv;
        private ImageView rankIv;

        //预览用户布局
        private ImageView headIv;
        private ImageView vipIv;
        private TextView countTv;
        private TextView nameTv;
        private Button followBtn;
        private MyStateButton followStateBtn;
        private LinearLayout userLayout;

        private ImageView lineIv;
        //评论头布局
        private TextView commentCountTv;

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

        public TitleHolder(View itemView) {
            super(itemView);
            switch (layoutId) {
                case R.layout.browse_search_layout:
                    searchTv = itemView.findViewById(R.id.browse_search_edittext);
                    searchLayout = itemView.findViewById(R.id.browse_search_layout);
                    recommendLayout = itemView.findViewById(R.id.browse_recommend_layout);
                    searchLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onSearchClickListener.onSearchClick(view);
                        }
                    });
                    rankIv = itemView.findViewById(R.id.browse_search_rank_iv);
                    rankTv = itemView.findViewById(R.id.browse_search_rank_tv);
                    boolean isPopularity = SettingManager.getInstances().getSettingBean().getReturnData().isIsPopularity();
                    if (isPopularity) {
                        rankTv.setVisibility(View.VISIBLE);
                        rankIv.setVisibility(View.VISIBLE);
                    } else {
                        rankIv.setVisibility(View.GONE);
                        rankTv.setVisibility(View.GONE);
                    }
                    break;
                case R.layout.browse_title_layout:
                    textView = itemView.findViewById(R.id.title_tv);
                    lineIv = itemView.findViewById(R.id.title_line_iv);
                    break;
                case R.layout.preview_title_layout:
                    textView = itemView.findViewById(R.id.preview_title_tv);
                    previewUserIdTv = itemView.findViewById(R.id.preview_user_id_tv);
                    previewLabelLayout = itemView.findViewById(R.id.preview_label_layout);

                    break;
                case R.layout.preview_comment_head_layout:
                    commentCountTv = itemView.findViewById(R.id.preview_comment_head_title_tv);
                    textView = itemView.findViewById(R.id.preview_comment_head_reply_tv);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onTextClickListener.onClick(view);
                        }
                    });
                    break;
                case R.layout.browse_banner_layout:
                    bannerIv = itemView.findViewById(R.id.browse_banner_iv);
                    break;
                case R.layout.preview_head_bottom_layout:
                    headIv = itemView.findViewById(R.id.preview_head_bottom_head_iv);
                    vipIv = itemView.findViewById(R.id.preview_head_bottom_vip_iv);
                    countTv = itemView.findViewById(R.id.preview_head_bottom_work_count_tv);
                    followBtn = itemView.findViewById(R.id.preview_head_bottom_follow_btn);
                    followStateBtn = itemView.findViewById(R.id.preview_head_bottom_follow_state_btn);
                    nameTv = itemView.findViewById(R.id.preview_head_bottom_name_tv);
                    userLayout = itemView.findViewById(R.id.preview_head_bottom_user_layout);
                    break;
            }

        }
    }

    private OnTextClickListener onTextClickListener;
    private OnSearchClickListener onSearchClickListener;
    private OnFollowListener onFollowListener;
    private OnAdvertisListener onAdvertisListener;

    public void setOnTitleClickListener(OnTitleClickListener onTitleClickListener) {
        this.onTitleClickListener = onTitleClickListener;
    }

    private OnTitleClickListener onTitleClickListener;

    public void setOnAdvertisListener(OnAdvertisListener onAdvertisListener) {
        this.onAdvertisListener = onAdvertisListener;
    }

    public void setOnTextClickListener(OnTextClickListener onTextClickListener) {
        this.onTextClickListener = onTextClickListener;
    }

    public interface OnTextClickListener {
        void onClick(View view);
    }

    public void setOnSearchClickListener(OnSearchClickListener onSearchClickListener) {
        this.onSearchClickListener = onSearchClickListener;
    }

    public interface OnSearchClickListener {
        void onSearchClick(View view);
    }

    public void setOnFollowListener(OnFollowListener onFollowListener) {
        this.onFollowListener = onFollowListener;
    }

    public interface OnFollowListener {
        void onFollow(View view, int memberID, boolean isAttentionedAuthor);
    }

    public interface OnAdvertisListener {
        void onAdvertis(View view, String url);
    }

    public interface OnTitleClickListener {
        void onTitleClick(View view);
    }
}
