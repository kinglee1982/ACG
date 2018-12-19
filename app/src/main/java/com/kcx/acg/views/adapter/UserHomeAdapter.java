package com.kcx.acg.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.kcx.acg.R;
import com.kcx.acg.bean.ProductToDayListBean;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.CornerTransform;
import com.kcx.acg.views.view.GlideRoundTransform;

import java.util.List;

/**
 * Created by jb .
 */

public class UserHomeAdapter extends RecyclerView.Adapter<UserHomeAdapter.ViewHolder> {

    private List<ProductToDayListBean.ReturnDataBean.ListBean> list;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private RequestOptions options;
    private  CornerTransform transformation;

    public UserHomeAdapter(Context context, List<ProductToDayListBean.ReturnDataBean.ListBean> list) {
        this.context = context;
        this.list = list;

        transformation = new CornerTransform(context,AppUtil.dip2px(context, 5));
        //只是绘制左上角和右上角圆角
        transformation.setExceptCorner(false, false, true, true);
        options = new RequestOptions()
                .skipMemoryCache(true)
                .placeholder(R.drawable.home_img_holder)
                .error(R.drawable.home_img_holder)
                .transform(transformation);

    }

    //设置回调监听
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void update(List<ProductToDayListBean.ReturnDataBean.ListBean> list){
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_home, parent, false);
        //实例化viewHolder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserHomeAdapter.ViewHolder holder, final int position) {
        holder.tv_content.setText(list.get(position).getTitle());
        Glide.with(context).load(list.get(position).getCoverPicUrl()).apply(options).into(holder.mImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });

    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        TextView tv_content;

        public ViewHolder(View view) {
            super(view);
            mImageView = view.findViewById(R.id.mImageView);
            tv_content = view.findViewById(R.id.tv_content);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
