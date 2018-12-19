package com.kcx.acg.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.kcx.acg.R;
import com.kcx.acg.bean.GetProductListBean;
import com.kcx.acg.utils.FormatCurrentData;
import com.kcx.acg.views.activity.ContributeActivity;
import com.kcx.acg.views.activity.CreativeCenterActivity;
import com.kcx.acg.views.activity.OriginalPlanActivity;
import com.kcx.acg.views.activity.PreviewActivity;
import com.kcx.acg.views.activity.WorkDetailsActivity;
import com.kcx.acg.views.view.BottomDialog2;
import com.kcx.acg.views.view.CustomToast;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.kcx.acg.views.activity.CreativeCenterActivity.STATUS_PASSED;
import static com.kcx.acg.views.activity.CreativeCenterActivity.STATUS_REVIEW;
import static com.kcx.acg.views.activity.CreativeCenterActivity.STATUS_UNPASS;

public class CCItemAdapter extends DelegateAdapter.Adapter {

    private Context context;
    private LayoutHelper layoutHelper;
    private List<GetProductListBean.ReturnDataBean.ListBean> list;
    private ItemHolder itemHolder;

    public CCItemAdapter(Context context, LayoutHelper layoutHelper, List<GetProductListBean.ReturnDataBean.ListBean> list) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.list = list;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.creative_center_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        itemHolder = (ItemHolder) holder;
        if (list.size() == 0) {
            itemHolder.emptyLayout.setVisibility(View.VISIBLE);
            return;
        }
        itemHolder.emptyLayout.setVisibility(View.GONE);

        final GetProductListBean.ReturnDataBean.ListBean item = list.get(position);
        itemHolder.titleTv.setText(item.getTitle());
        MultiTransformation multi = new MultiTransformation(
                new CenterCrop(),
                new RoundedCornersTransformation(8, 0, RoundedCornersTransformation.CornerType.ALL));
        RequestOptions advOptions = new RequestOptions();
        advOptions.placeholder(R.drawable.home_img_holder);
        advOptions.error(R.drawable.home_img_holder);
        advOptions.skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.ALL);
        advOptions.transform(multi);
        Glide.with(context).load(item.getCoverPicUrl()).apply(advOptions).into(itemHolder.productImgIv);

        switch (item.getAuditState()) {
            case STATUS_REVIEW:
                itemHolder.statusTv.setVisibility(View.VISIBLE);
                itemHolder.statusTv.setText(context.getString(R.string.creative_center_head_review_msg));
                itemHolder.statusTv.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_blue_bottom_left_top_right_4dp));
                itemHolder.editParentLayout.setVisibility(View.VISIBLE);
                itemHolder.incomeLayout.setVisibility(View.INVISIBLE);
                itemHolder.waringTv.setVisibility(View.GONE);
                itemHolder.dataLayout.setVisibility(View.GONE);
                itemHolder.timeTv.setText(FormatCurrentData.getTimeRange(item.getCreateTime()));
                break;
            case STATUS_UNPASS:
                itemHolder.incomeLayout.setVisibility(View.INVISIBLE);
                itemHolder.dataLayout.setVisibility(View.GONE);
                itemHolder.statusTv.setVisibility(View.VISIBLE);
                itemHolder.statusTv.setText(context.getString(R.string.creative_center_head_unpass_msg));
                itemHolder.statusTv.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_pink_bottom_left_top_right_4dp));
                itemHolder.editParentLayout.setVisibility(View.VISIBLE);
                itemHolder.waringTv.setText(item.getAuditFailureReason());
                itemHolder.waringTv.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(item.getAuditTime())) {
                    itemHolder.timeTv.setVisibility(View.VISIBLE);
                    itemHolder.timeTv.setText(FormatCurrentData.getTimeRange(item.getAuditTime()));
                }else {
                    itemHolder.timeTv.setVisibility(View.GONE);
                }
                break;
            case STATUS_PASSED:
                itemHolder.waringTv.setVisibility(View.GONE);
                itemHolder.editParentLayout.setVisibility(View.GONE);
                itemHolder.dataLayout.setVisibility(View.VISIBLE);
                itemHolder.statusTv.setVisibility(View.GONE);
                if (item.isIsNeedPay()) {
                    itemHolder.incomeLayout.setVisibility(View.VISIBLE);
                    itemHolder.buyCountTv.setText(((CreativeCenterActivity) context).getStringByCoin(item.getBuyTimes()));
                    itemHolder.incomeTv.setText(item.getProductIncomes());
                } else {
                    itemHolder.incomeLayout.setVisibility(View.INVISIBLE);
                }
                itemHolder.watchTv.setText(((CreativeCenterActivity) context).getStringByCoin(item.getViewTimes()));
                itemHolder.favTv.setText(((CreativeCenterActivity) context).getStringByCoin(item.getFavoriteTimes()));
                itemHolder.commentTv.setText(((CreativeCenterActivity) context).getStringByCoin(item.getCommentTimes()));
                itemHolder.shareTv.setText(((CreativeCenterActivity) context).getStringByCoin(item.getSharedTimes()));
                if (item.isIsUnShelved()) {
                    itemHolder.removedTv.setVisibility(View.VISIBLE);
                } else {
                    itemHolder.removedTv.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(item.getAuditTime())) {
                    itemHolder.timeTv.setVisibility(View.VISIBLE);
                    itemHolder.timeTv.setText(FormatCurrentData.getTimeRange(item.getAuditTime()));
                }else {
                    itemHolder.timeTv.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }



        itemHolder.editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ContributeActivity.class);
                intent.putExtra("productID", item.getId());
                ((CreativeCenterActivity) context).startDDMActivity(intent, true);
            }
        });

        itemHolder.deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteListener.onDelete(position);
            }
        });

        itemHolder.productImgIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getAuditState() == STATUS_PASSED) {
                    if (item.isIsUnShelved()) {
                        CustomToast.showToast(context.getString(R.string.home_work_removed_msg));
                    } else {
                        Intent intent = new Intent(context, WorkDetailsActivity.class);
                        intent.putExtra(PreviewActivity.KEY_PRODUCT_ID, item.getId());
                        ((CreativeCenterActivity) context).startDDMActivity(intent, true);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size() == 0 ? 1 : list.size();
    }

    public void showEmpty() {
        itemHolder.emptyLayout.setVisibility(View.VISIBLE);
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        public ImageView productImgIv;
        public TextView statusTv;
        public TextView removedTv;
        public TextView titleTv;
        public TextView timeTv;
        public LinearLayout editParentLayout;
        public LinearLayout editLayout;
        public LinearLayout deleteLayout;
        public TextView waringTv;
        public TextView buyCountTv;
        public TextView incomeTv;
        public LinearLayout incomeLayout;
        public TextView watchTv;
        public TextView favTv;
        public TextView commentTv;
        public TextView shareTv;
        public LinearLayout dataLayout;
        public LinearLayout emptyLayout;


        public ItemHolder(View itemView) {
            super(itemView);
            productImgIv = itemView.findViewById(R.id.creative_center_item_product_img_iv);
            statusTv = itemView.findViewById(R.id.creative_center_item_status_tv);
            removedTv = itemView.findViewById(R.id.creative_center_item_removed_tv);
            titleTv = itemView.findViewById(R.id.creative_center_item_title_tv);
            timeTv = itemView.findViewById(R.id.creative_center_item_time_tv);
            editParentLayout = itemView.findViewById(R.id.creative_center_item_edit_parent_layout);
            editLayout = itemView.findViewById(R.id.creative_center_item_edit_layout);
            deleteLayout = itemView.findViewById(R.id.creative_center_item_delete_layout);
            waringTv = itemView.findViewById(R.id.creative_center_item_waring_tv);
            buyCountTv = itemView.findViewById(R.id.creative_center_item_buy_count_tv);
            incomeTv = itemView.findViewById(R.id.creative_center_item_income_tv);
            incomeLayout = itemView.findViewById(R.id.creative_center_item_count_layout);
            watchTv = itemView.findViewById(R.id.creative_center_item_watch_tv);
            favTv = itemView.findViewById(R.id.creative_center_item_fav_tv);
            commentTv = itemView.findViewById(R.id.creative_center_item_comment_tv);
            shareTv = itemView.findViewById(R.id.creative_center_item_share_tv);
            dataLayout = itemView.findViewById(R.id.creative_center_item_data_layout);
            emptyLayout = itemView.findViewById(R.id.creative_center_item_empty_layout);

        }
    }

    private OnDeleteListener onDeleteListener;

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    public interface OnDeleteListener {
        void onDelete(int productId);
    }
}
