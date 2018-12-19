package com.kcx.acg.views.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kcx.acg.R;
import com.kcx.acg.bean.GetCommentReplysBean;
import com.kcx.acg.bean.ProductCommentsBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.manager.AccountManager;
import com.kcx.acg.utils.FormatCurrentData;
import com.kcx.acg.views.activity.PreviewActivity;
import com.kcx.acg.views.activity.ReplyActivitry;
import com.kcx.acg.views.activity.UserHomeActivity;
import com.kcx.acg.views.activity.WorkDetailsActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 */

public class CommentAdapter extends DelegateAdapter.Adapter {

    private Context context;
    private List<ProductCommentsBean.ReturnDataBean.ListBean> commentList;
    private List<String> list;
    private LayoutHelper layoutHelper;
    private boolean showReplyTv;
    private ProductCommentsBean.ReturnDataBean.ListBean commentBean;
    private List<GetCommentReplysBean.ReturnDataBean.ListBean> replyList;
    private int type;

    public CommentAdapter(Context context, ProductCommentsBean.ReturnDataBean.ListBean commentBean, LayoutHelper layoutHelper, boolean showReplyTv, int type) {
        this.context = context;
        this.commentBean = commentBean;
        this.layoutHelper = layoutHelper;
        this.showReplyTv = showReplyTv;
        this.type = type;
    }

    public CommentAdapter(Context context, List<ProductCommentsBean.ReturnDataBean.ListBean> commentList, LayoutHelper layoutHelper, boolean showReplyTv, int type, int where) {
        this.context = context;
        this.commentList = commentList;
        this.layoutHelper = layoutHelper;
        this.showReplyTv = showReplyTv;
        this.type = type;
    }

    public CommentAdapter(Context context, List<GetCommentReplysBean.ReturnDataBean.ListBean> replyList, LayoutHelper layoutHelper, boolean showReplyTv, int type) {
        this.context = context;
        this.replyList = replyList;
        this.layoutHelper = layoutHelper;
        this.showReplyTv = showReplyTv;
        this.type = type;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.preview_comment_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ProductCommentsBean.ReturnDataBean.ListBean item;
        switch (type) {
            case 0:
                if (commentList.size() > 0) {
                    viewHolder.noCommentLayout.setVisibility(View.GONE);
                    item = commentList.get(position);
                    bindViewWithComment(viewHolder, item, position);
                } else {
                    viewHolder.noCommentLayout.setVisibility(View.VISIBLE);
                }
                break;
            case 1:
                viewHolder.noCommentLayout.setVisibility(View.GONE);
                item = commentBean;
                bindViewWithComment(viewHolder, item, position);
                break;
            case 2:
                if (replyList.size() > 0) {
                    viewHolder.noCommentLayout.setVisibility(View.GONE);
                    GetCommentReplysBean.ReturnDataBean.ListBean replyItem = replyList.get(position);
                    bindViewWithReply(viewHolder, replyItem, position);
                } else {
//                    viewHolder.setVisibility(false);
                }
                break;
        }

    }

    public void bindViewWithComment(final ViewHolder viewHolder, final ProductCommentsBean.ReturnDataBean.ListBean item, final int position) {
        RequestOptions mRequestOptions = RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.NONE);
        mRequestOptions.placeholder(R.mipmap.placehold_head);
        mRequestOptions.error(R.mipmap.placehold_head);
        if (item != null) {
            Glide.with(context).load(item.getCommentUser().getPhoto()).apply(mRequestOptions).into(viewHolder.headIv);
        }
        viewHolder.nameTv.setText(item.getCommentUser().getUserName());
        viewHolder.commentTv.setText(item.getContent());
        if (item.getCommentUser().isAuthor()) {
            viewHolder.labelTv.setVisibility(View.VISIBLE);
            viewHolder.labelTv.setText(context.getString(R.string.author_msg));
            viewHolder.labelTv.setBackgroundResource(R.drawable.shape_label_bg);
            viewHolder.labelTv.setTextColor(ContextCompat.getColor(context, R.color.pink_ff8));
        } else if (item.getCommentUser().isIsPassenger()) {
            viewHolder.labelTv.setVisibility(View.GONE);
            viewHolder.labelTv.setText(context.getString(R.string.comment_passenger_msg));
            viewHolder.labelTv.setTextColor(ContextCompat.getColor(context, R.color.blue_6bb));
            viewHolder.labelTv.setBackgroundResource(R.drawable.shape_label_blue_bg);
        } else {
            viewHolder.labelTv.setVisibility(View.GONE);
        }
        switch (item.getCommentUser().getUserIdentify()) {
            case Constants.Author.ID_F:
                viewHolder.vipIv.setVisibility(View.VISIBLE);
                break;
            case Constants.Author.UNKNOWN:
            default:
                viewHolder.vipIv.setVisibility(View.GONE);
                break;
        }
        viewHolder.likeTv.setText(item.getLikeCount() == 0 ? context.getString(R.string.preview_like_msg) : item.getLikeCount() + "");
        viewHolder.dateTv.setText(FormatCurrentData.getCommentTime(item.getCommentTime()));

        if (item.getReplyCount() == 0) {
            viewHolder.replyTv.setText(context.getString(R.string.comment_no_reply_msg));
        } else {
            viewHolder.replyTv.setText(context.getString(R.string.comment_reply_count_msg, item.getReplyCount()));
        }
        viewHolder.likeCtv.setChecked(item.isIsLike());

        if (showReplyTv) {
            viewHolder.replyTv.setVisibility(View.VISIBLE);
            viewHolder.porintTv.setVisibility(View.VISIBLE);
        } else {
            viewHolder.replyTv.setVisibility(View.GONE);
            viewHolder.porintTv.setVisibility(View.GONE);
        }
        viewHolder.replyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        viewHolder.likeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!AccountManager.getInstances().isLogin(context, true)) {
                    return;
                }
                anim(viewHolder.likeTv);
                anim(viewHolder.likeCtv);
                viewHolder.likeCtv.setChecked(!viewHolder.likeCtv.isChecked());

                if (onItemClickListener != null)
                    onItemClickListener.onLikeClick(view, position, item.getCommentID(), viewHolder.likeCtv.isChecked());
            }
        });

        viewHolder.nameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserHomeActivity.class);
                intent.putExtra("memberID", item.getCommentUser().getMemberID());
                context.startActivity(intent);
            }
        });
        viewHolder.headIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserHomeActivity.class);
                intent.putExtra("memberID", item.getCommentUser().getMemberID());
                context.startActivity(intent);
            }
        });
        viewHolder.commentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null)
                    onItemClickListener.onTextClick(view, position);
            }
        });
        viewHolder.replyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!AccountManager.getInstances().isLogin(context, true)) {
                    return;
                }
                if (onItemClickListener != null && item.getReplyCount() == 0) {
                    onItemClickListener.onTextClick(view, position);
                } else {
                    onItemClickListener.onReplyClick(view, position);
                }
            }
        });
    }

    public void bindViewWithReply(final ViewHolder viewHolder, final GetCommentReplysBean.ReturnDataBean.ListBean item, final int position) {
        RequestOptions mRequestOptions = RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.NONE);
        mRequestOptions.placeholder(R.mipmap.placehold_head);
        mRequestOptions.error(R.mipmap.placehold_head);
        if (item != null) {
            Glide.with(context).load(item.getCommentUser().getPhoto()).apply(mRequestOptions).into(viewHolder.headIv);
        }
        viewHolder.nameTv.setText(item.getCommentUser().getUserName());
        viewHolder.commentTv.setText(item.getContent());
        if (item.getCommentUser().isAuthor()) {
            viewHolder.labelTv.setVisibility(View.VISIBLE);
            viewHolder.labelTv.setText(context.getString(R.string.author_msg));
            viewHolder.labelTv.setBackgroundResource(R.drawable.shape_label_bg);
            viewHolder.labelTv.setTextColor(ContextCompat.getColor(context, R.color.pink_ff8));
        } else if (item.getCommentUser().isIsPassenger()) {
            viewHolder.labelTv.setVisibility(View.GONE);
            viewHolder.labelTv.setText(context.getString(R.string.comment_passenger_msg));
            viewHolder.labelTv.setTextColor(ContextCompat.getColor(context, R.color.blue_6bb));
            viewHolder.labelTv.setBackgroundResource(R.drawable.shape_label_blue_bg);
        } else {
            viewHolder.labelTv.setVisibility(View.GONE);
        }
        switch (item.getCommentUser().getUserIdentify()) {
            case Constants.Author.ID_F:
                viewHolder.vipIv.setVisibility(View.VISIBLE);
                break;
            case Constants.Author.UNKNOWN:
            default:
                viewHolder.vipIv.setVisibility(View.GONE);
                break;
        }
        viewHolder.likeTv.setText(item.getLikeCount() == 0 ? context.getString(R.string.preview_like_msg) : item.getLikeCount() + "");

        viewHolder.dateTv.setText(FormatCurrentData.getCommentTime(item.getReplyTime()));

        if (showReplyTv) {
            viewHolder.replyTv.setVisibility(View.VISIBLE);
            viewHolder.porintTv.setVisibility(View.VISIBLE);
        } else {
            viewHolder.replyTv.setVisibility(View.GONE);
            viewHolder.porintTv.setVisibility(View.GONE);
        }
        viewHolder.replyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!AccountManager.getInstances().isLogin(context, true)) {
                    return;
                }
                onItemClickListener.onReplyClick(view, position);
            }
        });
        viewHolder.likeCtv.setChecked(item.isIsLike());
        viewHolder.likeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!AccountManager.getInstances().isLogin(context, true)) {
                    return;
                }
                anim(viewHolder.likeTv);
                anim(viewHolder.likeCtv);
                viewHolder.likeCtv.setChecked(!viewHolder.likeCtv.isChecked());
                if (onItemClickListener != null)
                    onItemClickListener.onLikeClick(view, position, item.getReplyID(), viewHolder.likeCtv.isChecked());
            }
        });

        viewHolder.nameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserHomeActivity.class);
                intent.putExtra("memberID", item.getCommentUser().getMemberID());
                context.startActivity(intent);
            }
        });
        viewHolder.headIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserHomeActivity.class);
                intent.putExtra("memberID", item.getCommentUser().getMemberID());
                ((ReplyActivitry) context).startDDMActivity(intent, true);
            }
        });
    }

    public void anim(View view) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.5f, 1f);
//沿y轴放大
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.5f, 1f);

        animatorSet.setDuration(200);
//        animatorSet.setInterpolator(new DecelerateInterpolator());  //设置插值器
        animatorSet.play(scaleXAnimator).with(scaleYAnimator);
        animatorSet.start();
    }

    @Override
    public int getItemCount() {
        switch (type) {
            case 0:
                return commentList.size() == 0 ? 1 : commentList.size();
            case 1:
                return 1;
            case 2:
                return replyList.size() == 0 ? 1 : replyList.size();
            default:
                return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView headIv;
        private ImageView vipIv;
        private CheckedTextView likeCtv;
        private TextView nameTv;
        private TextView labelTv;
        private TextView likeTv;
        private TextView commentTv;
        private TextView dateTv;
        private TextView porintTv;
        private TextView replyTv;
        private LinearLayout likeLayout;
        private LinearLayout noCommentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            headIv = itemView.findViewById(R.id.comment_item_head_iv);
            vipIv = itemView.findViewById(R.id.comment_item_vip_iv);
            likeCtv = itemView.findViewById(R.id.comment_item_like_ctv);
            nameTv = itemView.findViewById(R.id.comment_item_name_tv);
            labelTv = itemView.findViewById(R.id.comment_item_label_tv);
            likeTv = itemView.findViewById(R.id.comment_item_like_tv);
            commentTv = itemView.findViewById(R.id.comment_item_comment_tv);
            dateTv = itemView.findViewById(R.id.comment_item_date_tv);
            porintTv = itemView.findViewById(R.id.comment_item_porint_tv);
            replyTv = itemView.findViewById(R.id.comment_item_reply_tv);
            likeLayout = itemView.findViewById(R.id.comment_item_like_layout);
            noCommentLayout = itemView.findViewById(R.id.comment_nothing_layout);
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

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onReplyClick(View view, int position);

        void onLikeClick(View view, int position, int id, boolean isChecked);

        void onTextClick(View view, int position);
    }
}
