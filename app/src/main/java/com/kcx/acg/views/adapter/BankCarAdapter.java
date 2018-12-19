package com.kcx.acg.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kcx.acg.R;
import com.kcx.acg.bean.BankCardBean;
import com.kcx.acg.bean.MessageBean;
import com.kcx.acg.bean.SelectMemberBankBean;

import java.util.List;

/**
 * 银行卡
 * Created by jb on 2018/2/6.
 */

public class BankCarAdapter extends RecyclerView.Adapter<BankCarAdapter.ViewHolder> {

    private final Context context;
    private List<SelectMemberBankBean.ReturnDataBean> list;
    public OnItemClickListener onItemClickListener;

    public BankCarAdapter(Context context, List<SelectMemberBankBean.ReturnDataBean> list) {
        this.context = context;
        this.list = list;
    }

    public void upData(List<SelectMemberBankBean.ReturnDataBean> list) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bankcard, parent, false);
        //实例化viewHolder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv_bank.setText(list.get(position).getBankName());
        Glide.with(context).load(list.get(position).getLogo()).into(holder.iv);

        holder.tv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onDelItemClick(holder.itemView, position);
                }
            }
        });

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
        TextView tv_bank, tv_del;
        ImageView iv;

        public ViewHolder(View view) {
            super(view);
            iv = (ImageView) view.findViewById(R.id.iv);
            tv_bank = (TextView) view.findViewById(R.id.tv_bank);
            tv_del = (TextView) view.findViewById(R.id.tv_del);
            tv_del.setText("【" + context.getString(R.string.income_del) + "】");
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onDelItemClick(View view, int position);
    }
}
