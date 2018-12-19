package com.kcx.acg.views.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kcx.acg.R;
import com.kcx.acg.base.BaseActivity;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 查看头像
 * Created by jb on 2018/9/30.
 */
public class LookHeadPortraitsActivity extends BaseActivity implements CancelAdapt {
    private ImageButton ib_back;
    private ImageView iv_headPhoto;
    private String photoUrl;
    private RequestOptions options;


    @Override
    public void setTatusBar() {
        super.setTatusBar();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_look_head_portraits, null);
    }

    @Override
    public void initView() {
        super.initView();
        ib_back = findViewById(R.id.ib_back);
        iv_headPhoto = findViewById(R.id.iv_headPhoto);
    }

    @Override
    public void setListener() {
        super.setListener();
        ib_back.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        options = new RequestOptions()
                .placeholder(R.mipmap.placehold_head)  //预加载图片
                .error(R.mipmap.placehold_head);

        photoUrl = getIntent().getStringExtra("photoUrl");
        Glide.with(this).load(photoUrl).apply(options).into(iv_headPhoto);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
        }

    }
}
