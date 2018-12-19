package com.kcx.acg.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kcx.acg.R;
import com.kcx.acg.bean.RechargeSettingListBean;
import com.kcx.acg.utils.StringUtil;

import java.util.List;

/**
 * vip配置列表适配器
 * Created by jb on 2018/2/6.
 */

public class ACoinAdapter extends RecyclerView.Adapter<ACoinAdapter.ViewHolder> {

    private final Context context;
    private List<RechargeSettingListBean.ReturnDataBean.ListBean> list;
    public OnItemClickListener onItemClickListener;
    private boolean isFirstRechargeActivity=false;

    public ACoinAdapter(Context context, List<RechargeSettingListBean.ReturnDataBean.ListBean> list) {
        this.context = context;
        this.list = list;
    }

    public void upData(List<RechargeSettingListBean.ReturnDataBean.ListBean> list,boolean isFirstRechargeActivity){
        this.list = list;
        this.isFirstRechargeActivity = isFirstRechargeActivity;
        notifyDataSetChanged();
    }

    //设置回调监听
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }




    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //实例化展示的view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_abi_chongzhi, parent, false);
        //实例化viewHolder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //推荐
        if (list.get(position).isIsRecommend()){
            holder.tv_recommend.setVisibility(View.VISIBLE);
        }else {
            holder.tv_recommend.setVisibility(View.INVISIBLE);
        }
        //选项
        if (list.get(position).isDefault()){
            holder.rl_bg.setBackgroundResource(R.drawable.shape_abi_bg_define);
            holder.iv_abi_isChecked.setVisibility(View.VISIBLE);
        }else {
            holder.rl_bg.setBackgroundResource(R.drawable.shape_abi_bg_undefine);
            holder.iv_abi_isChecked.setVisibility(View.INVISIBLE);
        }

        Glide.with(context).load(list.get(position).getIcon()).into(holder.iv_icon);
        holder.tv_aCoin.setText(StringUtil.toDecimalFormat(list.get(position).getACoin()));
        holder.tv_rewardACoin.setText("+"+StringUtil.toDecimalFormat(list.get(position).getRewardACoin())+context.getString(R.string.account_ACoin));
        if (isFirstRechargeActivity){
            holder.tv_rewardACoin.setVisibility(View.VISIBLE);
        }else {
            holder.tv_rewardACoin.setVisibility(View.GONE);
        }

        holder.tv_rewardLotteryCount.setText(context.getString(R.string.account_send)+list.get(position).getRewardLotteryCount()+context.getString(R.string.account_lattery));
        holder.tv_money.setText("¥"+list.get(position).getMoney());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(holder.itemView, position);
                }
            }
        });


    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_recommend,tv_aCoin,tv_rewardACoin,tv_rewardLotteryCount,tv_money;
        ImageView iv_icon,iv_abi_isChecked;
        RelativeLayout rl_bg;

        public ViewHolder(View view) {
            super(view);
            rl_bg = view.findViewById(R.id.rl_bg);
            tv_recommend =  view.findViewById(R.id.tv_recommend);
            iv_icon =  view.findViewById(R.id.iv_icon);
            tv_aCoin =  view.findViewById(R.id.tv_aCoin);
            tv_rewardACoin =  view.findViewById(R.id.tv_rewardACoin);
            tv_rewardLotteryCount =  view.findViewById(R.id.tv_rewardLotteryCount);
            tv_money =  view.findViewById(R.id.tv_money);
            iv_abi_isChecked =  view.findViewById(R.id.iv_abi_isChecked);

        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);

    }
}
