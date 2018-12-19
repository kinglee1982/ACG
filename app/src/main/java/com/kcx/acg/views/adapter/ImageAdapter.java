package com.kcx.acg.views.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.common.collect.Lists;
import com.kcx.acg.R;
import com.kcx.acg.bean.GetProductInfoBean;

import java.util.List;


/**
 */

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private List<GetProductInfoBean.ReturnDataBean.DetailListBean> productList;


    public ImageAdapter(Context context, List<GetProductInfoBean.ReturnDataBean.DetailListBean> productList) {
        this.productList = productList;
        this.context = context;
    }

    public int getCount() {
        return productList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.picture_small_item_layout, null);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.pic_small_iv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置图片圆角角度
        //通过RequestOptions扩展功能
        RequestOptions options = new RequestOptions()
                .dontAnimate()
                .centerCrop()
                .transform(new RoundedCorners(6));

        Glide.with(context).load(productList.get(position).getUrl())
                .apply(options)
                .into(holder.imageView);
        return convertView;
    }

    class ViewHolder {
        private ImageView imageView;
    }
}
