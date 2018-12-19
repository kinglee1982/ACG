package com.kcx.acg.views.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kcx.acg.R;
import com.kcx.acg.bean.BankListBean;

import java.util.List;

/**
 * Created by jb on 2018/2/6.
 */

public class BankListAdapter extends RecyclerView.Adapter<BankListAdapter.ViewHolder> {

    private final Context context;
    private List<BankListBean.ReturnDataBean> list;
    public OnItemClickListener onItemClickListener;
    private String bankStr;
    private RequestOptions options;

    @SuppressLint("CheckResult")
    public BankListAdapter(Context context, List<BankListBean.ReturnDataBean> list, String bankStr) {
        this.list = list;
        this.bankStr = bankStr;
        this.context = context;
        options = new RequestOptions()
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher);
    }

    //设置回调监听
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void  updata(List<BankListBean.ReturnDataBean> list,String bankStr){
        this.list = list;
        this.bankStr = bankStr;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //实例化展示的view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bank_list, parent, false);
        //实例化viewHolder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (list.get(position).getName().equals(bankStr)){
            holder.iv.setVisibility(View.VISIBLE);
            holder.tv_bankName.setTextColor(context.getResources().getColor(R.color.pink_hint));
        }else{
            holder.iv.setVisibility(View.INVISIBLE);
            holder.tv_bankName.setTextColor(context.getResources().getColor(R.color.black_333));
        }

        holder.tv_bankName.setText(list.get(position).getName());
        Glide.with(context).load(list.get(position).getLogo()).apply(options).into(holder.iv_icon);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(holder.itemView, position,list.get(position).getName());
                }
            }
        });

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_bankName;
        ImageView iv,iv_icon;

        public ViewHolder(View view) {
            super(view);
            tv_bankName = (TextView) view.findViewById(R.id.tv_bankName);
            iv = (ImageView) view.findViewById(R.id.iv);
            iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position,String bankName);

    }
}
