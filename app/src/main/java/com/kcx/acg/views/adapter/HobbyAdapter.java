package com.kcx.acg.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.kcx.acg.R;
import com.kcx.acg.bean.HotTagBean;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 */

public class HobbyAdapter extends DelegateAdapter.Adapter {

    private Context context;
    private LayoutHelper layoutHelper;
    private List<HotTagBean.ReturnDataBean.ListBean> hotTagList;

    public HobbyAdapter(Context context, LayoutHelper layoutHelper, List<HotTagBean.ReturnDataBean.ListBean> hotTagList) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.hotTagList = hotTagList;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.hobby_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyHolder hobbyHolder = (MyHolder) holder;
        MultiTransformation multi = new MultiTransformation(
                new CenterCrop(),
                new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL));
        RequestOptions options = new RequestOptions();
        options.transform(multi);
        options.placeholder(R.drawable.img_holder_5dp);
        options.error(R.drawable.img_holder_5dp);
        HotTagBean.ReturnDataBean.ListBean item = hotTagList.get(position);
        Glide.with(context).load(item.getTagPhoto()).apply(options).into(hobbyHolder.imgIv);
        hobbyHolder.labelTv.setText(item.getTagName());
        hobbyHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, position);
            }
        });
        if(position == hotTagList.size() - 1){
            hobbyHolder.lineIv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return hotTagList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private ImageView imgIv;
        private ImageView lineIv;
        private TextView labelTv;

        public MyHolder(View itemView) {
            super(itemView);
            imgIv = itemView.findViewById(R.id.search_result_hobby_img_iv);
            lineIv = itemView.findViewById(R.id.search_result_hobby_line_iv);
            labelTv = itemView.findViewById(R.id.search_result_hobby_lab_tv);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
}
