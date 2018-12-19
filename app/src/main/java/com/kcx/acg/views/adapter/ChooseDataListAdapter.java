package com.kcx.acg.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.bean.InternationalAreaInfoListBean;

import java.util.List;

/**
 * 国家选择
 * Created by jb on 2018/2/6.
 */

public class ChooseDataListAdapter extends RecyclerView.Adapter<ChooseDataListAdapter.ViewHolder> {

    private  Context context;
    private String areaCode;
    private List<InternationalAreaInfoListBean.ReturnDataBean> list;
    public OnItemClickListener onItemClickListener;

    public ChooseDataListAdapter(Context context, List<InternationalAreaInfoListBean.ReturnDataBean> list, String areaCode) {
        this.context = context;
        this.list = list;
        this.areaCode = areaCode;
    }

    //设置回调监听
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void update(List<InternationalAreaInfoListBean.ReturnDataBean> list, String areaCode) {
        this.list = list;
        this.areaCode = areaCode;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //实例化展示的view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_country, parent, false);
        //实例化viewHolder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv_country.setText(list.get(position).getAreaName());
        holder.tv_areaCode.setText("+"+list.get(position).getAreaCode());

            if ((list.get(position).getAreaCode()).equals(areaCode)){
                holder.tv_areaCode.setTextColor(context.getResources().getColor(R.color.pink_hint));
                holder.tv_country.setTextColor(context.getResources().getColor(R.color.pink_hint));
                holder.iv.setVisibility(View.VISIBLE);
            }else {
                holder.tv_areaCode.setTextColor(context.getResources().getColor(R.color.black_333));
                holder.tv_country.setTextColor(context.getResources().getColor(R.color.black_333));
                holder.iv.setVisibility(View.INVISIBLE);
            }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(list.get(position).getAreaCode());
                }
            }
        });

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_country, tv_areaCode;
        ImageView iv;

        public ViewHolder(View view) {
            super(view);
            tv_country = (TextView) view.findViewById(R.id.tv_country);
            tv_areaCode = (TextView) view.findViewById(R.id.tv_areaCode);
            iv =  view.findViewById(R.id.iv);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(String areaCode);


    }
}
