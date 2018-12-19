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

public class TopOtherAdapter extends DelegateAdapter.Adapter {

    private Context context;
    private LayoutHelper layoutHelper;
    private List<GetPopularityInfoBean.ReturnDataBean.ListBean> list;

    public TopOtherAdapter(Context context, LayoutHelper layoutHelper, List<GetPopularityInfoBean.ReturnDataBean.ListBean> list) {
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
        return new TopOtherHolder(LayoutInflater.from(context).inflate(R.layout.rank_other_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        TopOtherHolder topOtherHolder = (TopOtherHolder) holder;
        RequestOptions mRequestOptions = RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.NONE);
        mRequestOptions.placeholder(R.mipmap.placehold_head);
        mRequestOptions.error(R.mipmap.placehold_head);
        GetPopularityInfoBean.ReturnDataBean.ListBean item = list.get(position);
        Glide.with(context).load(item.getPhoto()).apply(mRequestOptions).into(topOtherHolder.headIv);

        topOtherHolder.nameTv.setText(item.getUserName());
        topOtherHolder.popularityTv.setText(getStringByCoin(item.getPopularityCount()));
        topOtherHolder.rankingTv.setText(position + 4 + "");
        switch (item.getUserIdentify()) {
            case Constants.Author.ID_F:
                topOtherHolder.vipIv.setVisibility(View.VISIBLE);
                break;
            case Constants.Author.UNKNOWN:
            default:
                topOtherHolder.vipIv.setVisibility(View.GONE);
                break;
        }
        topOtherHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserHomeActivity.class);
                intent.putExtra("memberID", list.get(position).getMemberID());
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
        return list.size();
    }

    class TopOtherHolder extends RecyclerView.ViewHolder {
        public ImageView headIv;
        public ImageView vipIv;
        public TextView nameTv;
        public TextView popularityTv;
        public TextView rankingTv;


        public TopOtherHolder(View itemView) {
            super(itemView);
            headIv = itemView.findViewById(R.id.rank_other_head_iv);
            vipIv = itemView.findViewById(R.id.rank_other_vip_iv);
            nameTv = itemView.findViewById(R.id.rank_other_name_tv);
            popularityTv = itemView.findViewById(R.id.rank_other_popularity_tv);
            rankingTv = itemView.findViewById(R.id.rank_other_ranking_tv);
        }
    }
}
