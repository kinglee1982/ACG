package com.kcx.acg.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kcx.acg.R;
import com.kcx.acg.base.SysApplication;
import com.kcx.acg.bean.PictureUploadBean;
import com.kcx.acg.bean.ProductToDayListBean;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.CornerTransform;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.views.view.CustomToast;

import java.util.List;

/**
 * Created by jb .
 */

public class PictureWorkAdapter extends RecyclerView.Adapter<PictureWorkAdapter.ViewHolder> {

    private Context context;
    private OnItemClickListener onItemClickListener;
    private List<TransferObserver> observers;
    private ViewHolder viewHolder;
    private RequestOptions options;

    public PictureWorkAdapter(Context context, List<TransferObserver> observers) {
        this.observers = observers;
        this.context = context;
        options = new RequestOptions()
                .placeholder(R.drawable.home_img_holder)  //预加载图片
                .error(R.drawable.home_img_holder);
        int width = (SysApplication.mWidthPixels - 30 - 30 - 20 - 20) / 3;
        options.override(width, width);
    }

    //设置回调监听
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void update(List<TransferObserver> observers) {
        this.observers = observers;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        //        return list == null ? 0 : list.size();
        return observers == null ? 1 : observers.size() == 50 ? 50 : observers.size() + 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //实例化展示的view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture_work, parent, false);
        //实例化viewHolder
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PictureWorkAdapter.ViewHolder holder, final int position) {
        if (observers.size() == 0 || position == observers.size()) {
            if (position == 50) {
                holder.itemView.setVisibility(View.GONE);
            } else {
                Glide.with(context).load(R.mipmap.pic_add).apply(options).into(holder.iv_upload);
                holder.rl_uploadHint.setVisibility(View.INVISIBLE);
                holder.ib_del.setVisibility(View.INVISIBLE);
                holder.iv_upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onAddPic(position);
                    }
                });
            }
        } else {
            if (position < observers.size()) {
                holder.ib_del.setVisibility(View.VISIBLE);
                Glide.with(context).load(observers.get(position).getAbsoluteFilePath()).apply(options).into(holder.iv_upload);
                LogUtil.e("position=" + position + "///state =" + observers.get(position).getState() + "///");
                if ("WAITING".equals(observers.get(position).getState() + "")) {
                    holder.tv_uploadHint.setText(R.string.wait_upload);
                    holder.rl_uploadHint.setVisibility(View.VISIBLE);
                } else if ("IN_PROGRESS".equals(observers.get(position).getState() + "")) {
                    int progress = (int) ((double) observers.get(position).getBytesTransferred() * 100 / observers.get(position).getBytesTotal());
                    holder.tv_uploadHint.setText(progress + "%");
                    holder.rl_uploadHint.setVisibility(View.VISIBLE);
                } else if ("FAILED".equals(observers.get(position).getState() + "")) {
                    holder.tv_uploadHint.setText(R.string.upload_failed);
                    holder.rl_uploadHint.setVisibility(View.VISIBLE);
                } else if ("COMPLETED".equals(observers.get(position).getState() + "")) {
                    holder.rl_uploadHint.setVisibility(View.INVISIBLE);
                }
                holder.ib_del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onDelClick(position);
                        }
                    }
                });

                holder.iv_upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(position);
                        }
                    }
                });
            }
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_upload;
        TextView tv_uploadHint;
        ImageButton ib_del;
        RelativeLayout rl_uploadHint;

        public ViewHolder(View view) {
            super(view);
            iv_upload = view.findViewById(R.id.iv_upload);
            tv_uploadHint = view.findViewById(R.id.tv_uploadHint);
            ib_del = view.findViewById(R.id.ib_del);
            rl_uploadHint = view.findViewById(R.id.rl_uploadHint);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onDelClick(int position);

        void onAddPic(int position);
    }
}
