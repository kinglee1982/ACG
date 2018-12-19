package com.kcx.acg.views.adapter;

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
import com.kcx.acg.bean.UsersFavouriteTagListBean;
import com.kcx.acg.views.view.GlideRoundTransform;

import java.util.List;

/**
 * Created by jb on 2018/2/6.
 */

public class MyInterestAdapter extends RecyclerView.Adapter<MyInterestAdapter.ViewHolder> {

    private final Context context;
    private List<UsersFavouriteTagListBean.ReturnDataBean> list;
    public OnItemClickListener onItemClickListener;
    private RequestOptions options;

    public MyInterestAdapter(Context context,List<UsersFavouriteTagListBean.ReturnDataBean> list) {
        this.list = list;
        this.context = context;
        options = new RequestOptions()
                .placeholder(R.drawable.img_holder_5dp)  //预加载图片
                .error(R.drawable.img_holder_5dp)  //加载失败图片
                .transform(new GlideRoundTransform(context,5));

    }


    //设置回调监听
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void  updata(List<UsersFavouriteTagListBean.ReturnDataBean> list){
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_interest, parent, false);
        //实例化viewHolder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv.setText(list.get(position).getName());

        Glide.with(context).load(list.get(position).getTagPhoto()).apply(options).into(holder.iv);

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
        TextView tv;
        ImageView iv;
        public ViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv);
            iv = (ImageView) view.findViewById(R.id.iv_interest);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
