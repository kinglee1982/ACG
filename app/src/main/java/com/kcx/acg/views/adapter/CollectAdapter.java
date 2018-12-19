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
import com.bumptech.glide.request.RequestOptions;
import com.kcx.acg.R;
import com.kcx.acg.bean.FavouriteProductListBean;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.CornerTransform;

import java.util.List;

/**
 * Created by jb .
 */

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.ViewHolder> {

    private List<FavouriteProductListBean.ReturnDataBean.ListBean> list;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private RequestOptions options;
    private CornerTransform transformation;

    public CollectAdapter(Context context, List<FavouriteProductListBean.ReturnDataBean.ListBean> list) {
        this.context = context;
        this.list = list;

        transformation = new CornerTransform(context, AppUtil.dip2px(context, 5));
        //只是绘制左上角和右上角圆角
        transformation.setExceptCorner(false, false, true, true);
        options = new RequestOptions()
                .skipMemoryCache(true)
                .placeholder(R.drawable.home_img_holder)
                .error(R.drawable.home_img_holder)
                .transform(transformation);

    }

    public void upData(List<FavouriteProductListBean.ReturnDataBean.ListBean> list) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collect, parent, false);
        //实例化viewHolder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CollectAdapter.ViewHolder holder, final int position) {
        holder.tv_content.setText(list.get(position).getTitle());
        Glide.with(context).load(list.get(position).getCoverPicUrl()).apply(options).into(holder.mImageView);
        if (list.get(position).isIsUnShelved()){
            holder.rl_isUnShelved.setVisibility(View.VISIBLE);
        }else {
            holder.rl_isUnShelved.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position,list.get(position).getCanViewType(), list.get(position).getProductID());
                }
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView tv_content;
        RelativeLayout rl_isUnShelved;

        public ViewHolder(View view) {
            super(view);
            mImageView = view.findViewById(R.id.mImageView);
            tv_content = view.findViewById(R.id.tv_content);
            rl_isUnShelved = view.findViewById(R.id.rl_isUnShelved);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position,int canViewType, int productID);
    }
}
