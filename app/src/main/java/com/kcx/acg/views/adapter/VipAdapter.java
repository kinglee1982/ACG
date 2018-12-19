package com.kcx.acg.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.bean.SelectMemberBankBean;
import com.kcx.acg.bean.VIPSettingListBean;
import com.kcx.acg.utils.LogUtil;

import java.util.List;

/**
 * vip配置列表适配器
 * Created by jb on 2018/2/6.
 */

public class VipAdapter extends RecyclerView.Adapter<VipAdapter.ViewHolder> {

    private final Context context;
    private List<VIPSettingListBean.ReturnDataBean.ListBean> list;
    public OnItemClickListener onItemClickListener;

    public VipAdapter(Context context, List<VIPSettingListBean.ReturnDataBean.ListBean> list) {
        this.context = context;
        this.list = list;
    }

    public void upData(List<VIPSettingListBean.ReturnDataBean.ListBean> list) {
        this.list = list;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vip, parent, false);
        //实例化viewHolder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_month.setText(list.get(position).getMonth() + context.getString(R.string.vip_month));
        holder.tv_money.setText(list.get(position).getMoney());
        holder.tv_moneyPerMonth.setText("¥" + list.get(position).getMoneyPerMonth() + "/" + context.getString(R.string.vip_month2));
        holder.tv_rewardLotteryCount.setText(context.getString(R.string.vip_send) + list.get(position).getRewardLotteryCount() + context.getString(R.string.vip_lottery));
        if (list.get(position).isIsDiscount()) {
            //是否优惠  --是
            holder.iv_isDiscount.setVisibility(View.VISIBLE);
        } else {
            holder.iv_isDiscount.setVisibility(View.INVISIBLE);
        }

        if (list.get(position).isIsDefault()) {
            // 是否默认   --是
            holder.rl_bg.setBackgroundResource(R.drawable.shape_vip_define_bg);
            holder.iv_isChecked.setVisibility(View.VISIBLE);

        } else {
            holder.rl_bg.setBackgroundResource(R.drawable.shape_vip_undefine_bg);
            holder.iv_isChecked.setVisibility(View.INVISIBLE);
        }

//        if (list.get(position).isIsDefault() && list.get(position).getMonth() == 1) {
//            holder.tv_money.setText(list.get(position).getMoney());
//            holder.tv_moneyPerMonth.setText("¥" + list.get(position).getMoneyPerMonth() + "/" + context.getString(R.string.vip_month2));
//        } else {
//            holder.tv_money.setText(list.get(position).getMoney());
//            holder.tv_moneyPerMonth.setText("¥" + list.get(position).getMoneyPerMonth() + "/" + context.getString(R.string.vip_month2));
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(holder.itemView, position);
                }
            }
        });
    }

    public void setPrice(int i, String amount) {
        list.get(i).setMoney(Float.parseFloat(list.get(i).getMoney()) - Float.parseFloat(amount) + "");
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_month, tv_money, tv_moneyPerMonth, tv_rewardLotteryCount;
        ImageView iv_isDiscount, iv_isChecked;
        RelativeLayout rl_bg;

        public ViewHolder(View view) {
            super(view);
            iv_isDiscount = (ImageView) view.findViewById(R.id.iv_isDiscount);
            iv_isChecked = (ImageView) view.findViewById(R.id.iv_isChecked);
            tv_month = (TextView) view.findViewById(R.id.tv_month);
            tv_money = (TextView) view.findViewById(R.id.tv_money);
            tv_moneyPerMonth = (TextView) view.findViewById(R.id.tv_moneyPerMonth);
            tv_rewardLotteryCount = (TextView) view.findViewById(R.id.tv_rewardLotteryCount);
            rl_bg = view.findViewById(R.id.rl_bg);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);

    }
}
