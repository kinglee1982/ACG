package com.kcx.acg.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kcx.acg.R;
import com.kcx.acg.bean.GetPopularityInfoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.views.activity.RankActivity;
import com.kcx.acg.views.activity.UserHomeActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class Top3Adapter extends DelegateAdapter.Adapter {

    private Context context;
    private LayoutHelper layoutHelper;
    private List<GetPopularityInfoBean.ReturnDataBean.ListBean> list;

    public Top3Adapter(Context context, LayoutHelper layoutHelper, List<GetPopularityInfoBean.ReturnDataBean.ListBean> list) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.list = list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Top3Holder(LayoutInflater.from(context).inflate(R.layout.rank_top3_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (list.size() == 0) {
            return;
        }
        Top3Holder top3Holder = (Top3Holder) holder;
        RequestOptions mRequestOptions = RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.NONE);
        mRequestOptions.placeholder(R.mipmap.placehold_head);
        mRequestOptions.error(R.mipmap.placehold_head);
        Glide.with(context).load(list.get(0).getPhoto()).apply(mRequestOptions).into(top3Holder.top1HeadIv);
        Glide.with(context).load(list.get(1).getPhoto()).apply(mRequestOptions).into(top3Holder.top2HeadIv);
        Glide.with(context).load(list.get(2).getPhoto()).apply(mRequestOptions).into(top3Holder.top3HeadIv);

        top3Holder.top1NameTv.setText(list.get(0).getUserName());
        top3Holder.top2NameTv.setText(list.get(1).getUserName());
        top3Holder.top3NameTv.setText(list.get(2).getUserName());

        top3Holder.top1PopularityTv.setText(getStringByCoin(list.get(0).getPopularityCount()));
        top3Holder.top2PopularityTv.setText(getStringByCoin(list.get(1).getPopularityCount()));
        top3Holder.top3PopularityTv.setText(getStringByCoin(list.get(2).getPopularityCount()));
        if(list.get(0).getUserIdentify() ==  Constants.Author.ID_F){
            top3Holder.top1VipIv.setVisibility(View.VISIBLE);
        }else {
            top3Holder.top1VipIv.setVisibility(View.GONE);
        }
        if(list.get(1).getUserIdentify() ==  Constants.Author.ID_F){
            top3Holder.top2VipIv.setVisibility(View.VISIBLE);
        }else {
            top3Holder.top2VipIv.setVisibility(View.GONE);
        }
        if(list.get(2).getUserIdentify() ==  Constants.Author.ID_F){
            top3Holder.top3VipIv.setVisibility(View.VISIBLE);
        }else {
            top3Holder.top3VipIv.setVisibility(View.GONE);
        }
        top3Holder.top1HeadIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserHomeActivity.class);
                intent.putExtra("memberID", list.get(0).getMemberID());
                ((RankActivity)context).startDDMActivity(intent, true);
            }
        });
        top3Holder.top2HeadIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserHomeActivity.class);
                intent.putExtra("memberID", list.get(1).getMemberID());
                ((RankActivity)context).startDDMActivity(intent, true);
            }
        });
        top3Holder.top3HeadIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserHomeActivity.class);
                intent.putExtra("memberID", list.get(2).getMemberID());
                ((RankActivity)context).startDDMActivity(intent, true);
            }
        });
    }

    public String getStringByCoin(int coin) {
        NumberFormat format = new DecimalFormat(",###");
        return format.format(coin);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    class Top3Holder extends RecyclerView.ViewHolder {

        public ImageView top1HeadIv;
        public ImageView top2HeadIv;
        public ImageView top3HeadIv;

        public ImageView top1VipIv;
        public ImageView top2VipIv;
        public ImageView top3VipIv;

        public TextView top1NameTv;
        public TextView top2NameTv;
        public TextView top3NameTv;
        public TextView top1PopularityTv;
        public TextView top2PopularityTv;
        public TextView top3PopularityTv;

        public Top3Holder(View itemView) {
            super(itemView);
            top1HeadIv = itemView.findViewById(R.id.rank_top1_head_iv);
            top2HeadIv = itemView.findViewById(R.id.rank_top2_head_iv);
            top3HeadIv = itemView.findViewById(R.id.rank_top3_head_iv);

            top1VipIv = itemView.findViewById(R.id.rank_top1_vip_iv);
            top2VipIv = itemView.findViewById(R.id.rank_top2_vip_iv);
            top3VipIv = itemView.findViewById(R.id.rank_top3_vip_iv);

            top1NameTv = itemView.findViewById(R.id.rank_top1_name_tv);
            top2NameTv = itemView.findViewById(R.id.rank_top2_name_tv);
            top3NameTv = itemView.findViewById(R.id.rank_top3_name_tv);

            top1PopularityTv = itemView.findViewById(R.id.rank_top1_popularity_tv);
            top2PopularityTv = itemView.findViewById(R.id.rank_top2_popularity_tv);
            top3PopularityTv = itemView.findViewById(R.id.rank_top3_popularity_tv);


        }
    }

}
