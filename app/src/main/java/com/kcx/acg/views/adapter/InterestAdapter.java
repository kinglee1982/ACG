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
import com.kcx.acg.bean.FavouriteTagListBean;
import com.kcx.acg.bean.MessageBean;
import com.kcx.acg.bean.UsersDynamicListBean;
import com.kcx.acg.bean.UsersFavouriteTagListBean;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.views.view.GlideRoundTransform;

import java.util.List;

/**
 * Created by jb on 2018/2/6.
 */

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.ViewHolder> {

    private final Context context;
    private List<FavouriteTagListBean.ReturnDataBean.ListBean> list;
    public OnItemClickListener onItemClickListener;
    private RequestOptions options;

    public InterestAdapter(Context context, List<FavouriteTagListBean.ReturnDataBean.ListBean> list) {
        this.context = context;
        this.list = list;

        options = new RequestOptions()
                .placeholder(R.drawable.home_img_holder)  //预加载图片
                .error(R.drawable.home_img_holder)  //加载失败图片
                .transform(new GlideRoundTransform(context,5));
    }


    public void updata(List<FavouriteTagListBean.ReturnDataBean.ListBean> list){
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_interest, parent, false);
        //实例化viewHolder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv_userName.setText(list.get(position).getName());
        Glide.with(context).load(list.get(position).getTagPhoto()).apply(options).into(holder.iv_touxiang);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(holder.itemView, position,list.get(position).getTagID());
                }
            }
        });


    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_userName;
        private ImageView iv_touxiang;

        public ViewHolder(View view) {
            super(view);
            tv_userName =  view.findViewById(R.id.tv_userName);
            iv_touxiang =  view.findViewById(R.id.iv_touxiang);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position,int tagID);
    }
}
