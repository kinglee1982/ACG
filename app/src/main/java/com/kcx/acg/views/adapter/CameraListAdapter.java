package com.kcx.acg.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.kcx.acg.R;
import com.kcx.acg.utils.ImageFloder;
import com.kcx.acg.utils.StringUtil;
import java.util.List;

/**
 * Created by jb on 2018/2/6.
 */

public class CameraListAdapter extends RecyclerView.Adapter<CameraListAdapter.ViewHolder> {

    private List<ImageFloder> mImageFloders;
    private Context mContext;
    private CameraListAdapter.OnItemClickListener onItemClickListener;

    public CameraListAdapter(Context context, List<ImageFloder> mImageFloders) {
        this.mContext = context;
        this.mImageFloders = mImageFloders;
    }

    //设置回调监听
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    @Override
    public int getItemCount() {
        return mImageFloders == null ? 0 : mImageFloders.size();
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //实例化展示的view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_cameralist_dir_item, parent, false);
        //实例化viewHolder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ImageFloder imageFloder = mImageFloders.get(position);
        holder.id_dir_item_name.setText(StringUtil.replaceNULL(imageFloder.getName()));
        holder.id_dir_item_count.setText(StringUtil.replaceNULL(imageFloder.getCount()+"张"));
        Glide.with(mContext).load(imageFloder.getFirstImagePath()).into(holder.id_dir_item_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null){
                    onItemClickListener.selected(imageFloder);
                }
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id_dir_item_name, id_dir_item_count;
        ImageView id_dir_item_image;

        public ViewHolder(View view) {
            super(view);
            id_dir_item_image = view.findViewById(R.id.id_dir_item_image);
            id_dir_item_name = view.findViewById(R.id.id_dir_item_name);
            id_dir_item_count = view.findViewById(R.id.id_dir_item_count);
        }
    }

    public interface OnItemClickListener {

        void selected(ImageFloder floder);

    }
}
