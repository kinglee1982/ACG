package com.kcx.acg.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.kcx.acg.R;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.utils.ToastUtil;
import com.kcx.acg.views.activity.CropActivity;
import com.kcx.acg.views.view.ViewHolder;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends CommonAdapter<String> {

    public OnItemClickListener onItemClickListener;
    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public static ArrayList<ImageView> SelectedImageViews = new ArrayList<ImageView>();

    /**
     * 文件夹路径
     */
    private String mDirPath;

    public MyAdapter(Context context, List<String> mDatas, int itemLayoutId,
                     String dirPath) {
        super(context, mDatas, itemLayoutId);
        this.mDirPath = dirPath;
    }

    //设置回调监听
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setIamgeNull() {
        for (ImageView imageView : SelectedImageViews) {
            imageView.setImageResource(R.mipmap.picture_unselected);
        }

    }

    @Override
    public void convert(ViewHolder helper, final String item) {
        // 设置图片
        helper.setImageByUrl(R.id.item_image, mDirPath + "/" + item);

        final ImageView mImageView = helper.getView(R.id.item_image);
        mImageView.setColorFilter(null);
        // 设置ImageView的点击事件
        mImageView.setOnClickListener(new OnClickListener() {
            // 选择，则将图片变暗，反之则反之
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(mDirPath + "/" + item);
                LogUtil.e("选择了图片",mDirPath + "/" + item+"///");
                }
            }
        });

    }




    public interface OnItemClickListener {
        void onItemClick(String imgPath);

    }

}
