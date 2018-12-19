package com.kcx.acg.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.kcx.acg.R;
import com.kcx.acg.bean.GetProductInfoBean;
import com.kcx.acg.bean.PictureBean;
import com.kcx.acg.utils.LogUtil;

import java.util.List;


/**
 */

public class Image2Adapter extends BaseAdapter {
    private List<String> pathList;
    private Context context;


    public Image2Adapter(Context context, List<String> pathList) {
        this.pathList = pathList;
        this.context = context;
    }



    public int getCount() {
        return pathList == null ? 0 : pathList.size();
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
        Glide.with(context).load(pathList.get(position))
                .apply(options)
                .into(holder.imageView);
        return convertView;
    }

    class ViewHolder {
        private ImageView imageView;
    }
}
