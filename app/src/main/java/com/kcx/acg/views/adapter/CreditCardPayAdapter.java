package com.kcx.acg.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.bean.CreditCardPayBean;
import com.kcx.acg.bean.MessageBean;
import com.kcx.acg.utils.ToastUtil;

import java.util.List;

/**
 * 消息通知
 * Created by jb on 2018/2/6.
 */

public class CreditCardPayAdapter extends RecyclerView.Adapter<CreditCardPayAdapter.ViewHolder> {

    private View view;
    private final Context context;
    private List<CreditCardPayBean> list;
    public OnItemClickListener onItemClickListener;
    private boolean unBindCard;
    private int viewType = 0;

    public CreditCardPayAdapter(Context context, List<CreditCardPayBean> list, boolean unBindCard) {
        this.context = context;
        this.list = list;
        this.unBindCard = unBindCard;
    }

    //设置回调监听
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    //刷新布局
    public void update(boolean unBindCard,int viewType) {
        this.unBindCard = unBindCard;
        this.viewType = viewType;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //实例化展示的view
        if (viewType == 0) {
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_credit_card, parent, false);
        } else {
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_credit_card_del, parent, false);

        }
        //实例化viewHolder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv_name.setText(list.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (unBindCard) {
                    onItemClickListener.onDelItemClick(position);
                } else {
                    onItemClickListener.onItemClick(holder.itemView, position);
                }

            }
        });


    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        ImageView iv_del;
        RelativeLayout rl_item_creditCard;

        public ViewHolder(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            iv_del = (ImageView) view.findViewById(R.id.iv_del);
            rl_item_creditCard = (RelativeLayout) view.findViewById(R.id.rl_item_creditCard);


        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);

        void onDelItemClick(int position);


    }
}
