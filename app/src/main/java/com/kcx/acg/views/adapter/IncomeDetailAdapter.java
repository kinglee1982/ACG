package com.kcx.acg.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.bean.UserIncomeDetailBean;

import java.util.List;

/**
 * 收益明细
 * Created by jb on 2018/2/6.
 */

public class IncomeDetailAdapter extends RecyclerView.Adapter<IncomeDetailAdapter.ViewHolder> {

    private Context context;
    private List<UserIncomeDetailBean.ReturnDataBean.ListBean> list;
    public OnItemClickListener onItemClickListener;

    public IncomeDetailAdapter(Context context,List<UserIncomeDetailBean.ReturnDataBean.ListBean> list) {
        this.context = context;
        this.list = list;
    }

    //设置回调监听
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void upData(List<UserIncomeDetailBean.ReturnDataBean.ListBean> list){
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //实例化展示的view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_income_detail, parent, false);
        //实例化viewHolder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_title.setText(list.get(position).getTypeName());
        holder.tv_content.setText(list.get(position).getCreateTime());
        if (list.get(position).isIncome()){
            holder.tv_money.setText("+"+ list.get(position).getIncome());
            holder.tv_money.setTextColor(context.getResources().getColor(R.color.pink_hint));
        }else{
            holder.tv_money.setText("-"+list.get(position).getIncome());
            holder.tv_money.setTextColor(context.getResources().getColor(R.color.black_333));
        }


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
        TextView tv_title, tv_money, tv_content;

        public ViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_money = (TextView) view.findViewById(R.id.tv_money);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
