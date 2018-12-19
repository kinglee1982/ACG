package com.kcx.acg.views.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.utils.ImageUtil;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.utils.StringUtil;
import com.kcx.acg.views.view.ClipImageLayout;
import com.kcx.acg.views.view.ClipZoomImageView;
import com.tmall.wireless.vaf.virtualview.view.nlayout.INativeLayout;

import java.io.ByteArrayOutputStream;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * Created by jb on 2018/10/8.
 * 图片裁剪
 */
public class CropActivity extends BaseActivity implements CancelAdapt {
    private ImageButton ib_back;
    private TextView tv_user;
    private ClipImageLayout clipImageLayout;
    private String imgPath;
    private Bitmap bitmap;

    @Override
    public void setTatusBar() {
        super.setTatusBar();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_crop, null);
    }

    @Override
    public void initView() {
        super.initView();
        imgPath = getIntent().getStringExtra("imgPath");
        bitmap = ImageUtil.path2Bitmap(imgPath);

        ib_back = findViewById(R.id.ib_back);
        tv_user = findViewById(R.id.tv_user);
        clipImageLayout = findViewById(R.id.clipImageLayout);
        clipImageLayout.init(CropActivity.this, this.bitmap);

    }

    @Override
    public void setListener() {
        super.setListener();
        ib_back.setOnClickListener(this);
        tv_user.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;

            case R.id.tv_user: //使用
                Bitmap bitmap = clipImageLayout.clip();
                //图片压缩,如果大于512kb
                bitmap = ImageUtil.compressImage(bitmap,500);
                byte[] datas = ImageUtil.bitmap2Bytes(bitmap);
                Intent intent = new Intent();
                intent.putExtra("bitmap", datas);
                setResult(10086, intent);
                bitmap.recycle();
                finish();
                break;
        }

    }
}
