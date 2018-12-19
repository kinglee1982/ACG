package com.kcx.acg.views.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kcx.acg.R;
import com.kcx.acg.aws.AWSUtil;
import com.kcx.acg.bean.LocalMediaBean;
import com.kcx.acg.bean.LocalMediaVideoBean;
import com.kcx.acg.bean.ReadNoticeBean;
import com.kcx.acg.bean.VideoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.DateUtil;
import com.kcx.acg.utils.InterceptUtil;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.views.view.GlideRoundTransform;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import io.vov.vitamio.provider.MediaStore;

/**
 * Created by jb on 2018/2/6.
 */

public class VideoWorkAdapter extends RecyclerView.Adapter<VideoWorkAdapter.ViewHolder> {
    private RequestOptions options;
    private Context context;
    public OnItemClickListener onItemClickListener;
    private List<VideoBean> list;

    public VideoWorkAdapter(Context context, List<VideoBean> list) {
        this.context = context;
        this.list = list;

        options = new RequestOptions()
                .placeholder(R.drawable.img_holder_5dp)  //预加载图片
                .error(R.drawable.img_holder_5dp);  //加载失败图片

    }

    public void update(List<VideoBean> list) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_work, parent, false);
        //实例化viewHolder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Long videoDuration = list.get(position).getVideoDuration();
        holder.tv_time.setText(DateUtil.duration(list.get(position).getVideoDuration()));
        holder.tv_fileName.setText("#"+(position+1));
        if (list.get(position).getTransferObserver() == null) {
            Glide.with(context).load(list.get(position).getUrl()).apply(options).into(holder.iv);
//            holder.tv_fileName.setText(list.get(position).getName());
            holder.tv_bytesTotal.setText(AWSUtil.getBytesString(list.get(position).getSize()));
            holder.tv_uploadHint.setText(R.string.upload_completed);
            holder.tv_uploadHint.setTextColor(context.getResources().getColor(R.color.black_999));
            holder.progressBar.setVisibility(View.INVISIBLE);
        } else {
            LogUtil.e("transferObserver=" + position,list.get(position).getTransferObserver().getState()+"   ///");
            Glide.with(context).load(list.get(position).getTransferObserver().getAbsoluteFilePath()).apply(options).into(holder.iv);
            String path = list.get(position).getTransferObserver().getAbsoluteFilePath();
//            holder.tv_fileName.setText(new File(path).getName());
            holder.tv_bytesTotal.setText(AWSUtil.getBytesString(list.get(position).getTransferObserver().getBytesTotal()));

            holder.progressBar.setVisibility(View.VISIBLE);
            if (Constants.UploadState.WAITING.equals(list.get(position).getTransferObserver().getState() + "")) {
                holder.tv_uploadHint.setText(R.string.wait_upload);
                holder.tv_uploadHint.setTextColor(context.getResources().getColor(R.color.black_999));
            } else if (Constants.UploadState.IN_PROGRESS.equals(list.get(position).getTransferObserver().getState() + "")) {
                int progress = (int) ((double) list.get(position).getTransferObserver().getBytesTransferred() * 100 / list.get(position).getTransferObserver().getBytesTotal());
                holder.progressBar.setProgress(progress);
                holder.tv_uploadHint.setText(context.getString(R.string.upload_in_progress));
                holder.tv_uploadHint.setTextColor(context.getResources().getColor(R.color.black_999));
            } else if (Constants.UploadState.PAUSED.equals(list.get(position).getTransferObserver().getState() + "")) {
                holder.tv_uploadHint.setText(R.string.upload_pause);
                holder.tv_uploadHint.setTextColor(context.getResources().getColor(R.color.pink_hint));
            } else if (Constants.UploadState.FAILED.equals(list.get(position).getTransferObserver().getState() + "")) {
                holder.tv_uploadHint.setText(R.string.upload_video_failed);
                holder.tv_uploadHint.setTextColor(context.getResources().getColor(R.color.pink_hint));
            } else if (Constants.UploadState.COMPLETED.equals(list.get(position).getTransferObserver().getState() + "")) {
                holder.tv_uploadHint.setText(R.string.upload_completed);
                holder.tv_uploadHint.setTextColor(context.getResources().getColor(R.color.black_999));
                holder.progressBar.setVisibility(View.INVISIBLE);
            }

        }


        holder.rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });

        holder.btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onDelClick(position);
                }
            }
        });


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_fileName, tv_uploadHint, tv_bytesTotal, tv_time;
        ImageView iv;
        ProgressBar progressBar;
        Button btn_del;
        RelativeLayout rl_item;

        public ViewHolder(View view) {
            super(view);
            iv = view.findViewById(R.id.iv);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_fileName = (TextView) view.findViewById(R.id.tv_fileName);
            tv_uploadHint = (TextView) view.findViewById(R.id.tv_uploadHint);
            tv_bytesTotal = (TextView) view.findViewById(R.id.tv_bytesTotal);
            progressBar = view.findViewById(R.id.progressBar);
            btn_del = view.findViewById(R.id.btn_del);
            rl_item = view.findViewById(R.id.rl_item);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onDelClick(int position);
    }
}
