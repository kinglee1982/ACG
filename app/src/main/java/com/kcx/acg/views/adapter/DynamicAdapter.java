package com.kcx.acg.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.kcx.acg.R;
import com.kcx.acg.bean.UsersDynamicListBean;
import com.kcx.acg.utils.FormatCurrentData;
import com.kcx.acg.views.activity.UserHomeActivity;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.GlideRoundTransform;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by jb on 2018/2/6.
 */

public class DynamicAdapter extends RecyclerView.Adapter<DynamicAdapter.ViewHolder> {

    private final Context context;
    private List<UsersDynamicListBean.ReturnDataBean.ListBean> list;
    public OnItemClickListener onItemClickListener;
    private RequestOptions options, requestOptions;

    public DynamicAdapter(Context context, List<UsersDynamicListBean.ReturnDataBean.ListBean> list) {
        this.context = context;
        this.list = list;
        options = new RequestOptions()
                .placeholder(R.drawable.home_img_holder)  //预加载图片
                .error(R.drawable.home_img_holder)  //加载失败图片
                .priority(Priority.HIGH)
                .transform(new GlideRoundTransform(context, 5));

        requestOptions = new RequestOptions()
                .placeholder(R.mipmap.placehold_head)  //预加载图片
                .error(R.mipmap.placehold_head);  //加载失败图片
    }


    //设置回调监听
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void updata(List<UsersDynamicListBean.ReturnDataBean.ListBean> list) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dynamic, parent, false);
        //实例化viewHolder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (1== list.get(position).getAuthor().getUserIdentify()){
            holder.iv_v.setVisibility(View.VISIBLE);
        }else {
            holder.iv_v.setVisibility(View.INVISIBLE);
        }
        holder.tv_nickname.setText(list.get(position).getAuthor().getUserName());
        holder.tv_content.setText(list.get(position).getTitle());
            String s = FormatCurrentData.getTimeRange(list.get(position).getCreateTime());
            holder.tv_time.setText(s);
//        if (list.get(position).isUser()) {
            holder.iv_touxiang.setVisibility(View.VISIBLE);
            holder.iv_touxiang_square.setVisibility(View.INVISIBLE);
            Glide.with(context).load(list.get(position).getAuthor().getPhoto()).apply(requestOptions).into(holder.iv_touxiang);
//        }else {
//            holder.iv_touxiang.setVisibility(View.INVISIBLE);
//            holder.iv_touxiang_square.setVisibility(View.VISIBLE);
//            Glide.with(context).load(list.get(position).getAuthor().getPhoto()).apply(options).into(holder.iv_touxiang_square);
//        }
        Glide.with(context).load(list.get(position).getCoverPicUrl()).apply(options).into(holder.iv_content);
        holder.tv_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goUserHome(list.get(position).getAuthor().getMemberID());
            }
        });

        holder.iv_touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goUserHome(list.get(position).getAuthor().getMemberID());

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });

    }

    private void goUserHome(int memberID) {
        Intent intent = new Intent(context, UserHomeActivity.class);
        intent.putExtra("memberID",memberID);
        context.startActivity(intent);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nickname, tv_content, tv_time;
        ImageView iv_touxiang, iv_content, iv_touxiang_square,iv_v;

        public ViewHolder(View view) {
            super(view);
            tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            iv_touxiang = (ImageView) view.findViewById(R.id.iv_touxiang);
            iv_touxiang_square = (ImageView) view.findViewById(R.id.iv_touxiang_square);
            iv_content = (ImageView) view.findViewById(R.id.iv_content);
            iv_v =  view.findViewById(R.id.iv_v);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(int position);

    }
}
