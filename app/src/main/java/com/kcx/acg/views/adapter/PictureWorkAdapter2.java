package com.kcx.acg.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.kcx.acg.R;
import com.kcx.acg.bean.PictureBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.utils.LogUtil;
import java.util.List;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by jb .
 */

public class PictureWorkAdapter2 extends DelegateAdapter.Adapter {

    private static final int TYPE_ADD = 0x1001;
    private static final int TYPE_ITEM = 0x1002;
    private List<PictureBean> list;


    private Context context;
    private OnItemClickListener onItemClickListener;
    private LayoutHelper layoutHelper;
    private TransferObserver observer;

    public PictureWorkAdapter2(Context context, LayoutHelper layoutHelper, List<PictureBean> list) {
        this.list = list;
        this.context = context;
        this.layoutHelper = layoutHelper;
    }

    //设置回调监听
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void update(List<PictureBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list == null ? 1 : list.size() == 50 ? 50 : list.size() + 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_picture_work2, parent, false));
    }

  /*  @Override
    public int getItemViewType(int position) {
        if (position == observers.size() - 1) {
            return TYPE_ADD;
        } else {
            return TYPE_ITEM;
        }
    }*/

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        MultiTransformation multi = new MultiTransformation(
                new CenterCrop(),
                new RoundedCornersTransformation(15, 0, RoundedCornersTransformation.CornerType.ALL));
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.yulan_picture_upload)  //预加载图片
                .error(R.mipmap.yulan_picture_upload)
                .transform(multi);

        if (list.size() == 0 || position == list.size()) {
            if (position == 50) {
                holder.itemView.setVisibility(View.GONE);
            } else {
                Glide.with(context).load(R.mipmap.pic_add).apply(options).into(viewHolder.iv_upload);
                viewHolder.tv_uploadHint.setVisibility(View.INVISIBLE);
                viewHolder.ib_del.setVisibility(View.INVISIBLE);
            }
        } else {
            observer = list.get(position).getTransferObserver();
            viewHolder.ib_del.setVisibility(View.VISIBLE);
            if (observer == null) {
                Glide.with(context).load(list.get(position).getUrl()).apply(options).into(viewHolder.iv_upload);
                viewHolder.tv_uploadHint.setVisibility(View.INVISIBLE);
            } else {
                Glide.with(context).load(observer.getAbsoluteFilePath()).apply(options).into(viewHolder.iv_upload);
                LogUtil.e("position=" + position + "///state =" + observer.getState() + "///");
                if (Constants.UploadState.WAITING.equals(observer.getState() + "")) {
                    viewHolder.tv_uploadHint.setText(R.string.wait_upload);
                    viewHolder.tv_uploadHint.setVisibility(View.VISIBLE);
                } else if (Constants.UploadState.IN_PROGRESS.equals(observer.getState() + "")) {
                    int progress = (int) ((double) observer.getBytesTransferred() * 100 / observer.getBytesTotal());
                    viewHolder.tv_uploadHint.setText(progress + "%");
                    viewHolder.tv_uploadHint.setVisibility(View.VISIBLE);
                } else if (Constants.UploadState.FAILED.equals(observer.getState() + "")) {
                    viewHolder.tv_uploadHint.setText(R.string.upload_failed);
                    viewHolder.tv_uploadHint.setVisibility(View.VISIBLE);
                } else if (Constants.UploadState.COMPLETED.equals(observer.getState() + "")) {
                    viewHolder.tv_uploadHint.setVisibility(View.INVISIBLE);
                }
            }


        }

        viewHolder.ib_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onDelClick(position);
                }
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    if (position == list.size()) {
                        onItemClickListener.onAddPic(position);
                    } else {
                        onItemClickListener.onItemClick(position);
                    }
                }
            }
        });
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_upload;
        TextView tv_uploadHint;
        ImageButton ib_del;
        //        RelativeLayout rl_uploadHint;

        public ViewHolder(View view) {
            super(view);
            iv_upload = view.findViewById(R.id.iv_upload);
            tv_uploadHint = view.findViewById(R.id.tv_uploadHint);
            ib_del = view.findViewById(R.id.ib_del);
            //            rl_uploadHint = view.findViewById(R.id.rl_uploadHint);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onDelClick(int position);

        void onAddPic(int position);
    }
}
