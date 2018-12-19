package com.kcx.acg.views.activity;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.kcx.acg.R;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.utils.ImageUtil;
import com.kcx.acg.utils.QRCodeService;
import com.kcx.acg.utils.ToastUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 福利活动
 * Created by jb on 2018/9/6.
 */
public class WelfareActivity extends BaseActivity implements CancelAdapt {
    private QRCodeService qrCodeService;
    private TextView tv_title, tv_invitationCode;
    private Button  btn_downloadQR, btn_share;
    private LinearLayout ll_back;
    private ImageView iv_QRcode;
    private Bitmap bitmapQR;
    private String mSaveMessage;
    private String QRPath = "http://www.baidu.com";

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 0) {
                Bundle bundle = msg.getData();
                String s = bundle.getString("saveMessage");
                ToastUtil.showShort(WelfareActivity.this, s);
            }
        }
    };


    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_welfare, null);
    }

    @Override
    public void initView() {
        super.initView();
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("推广赚现金");
        ll_back = findViewById(R.id.ll_back);
        btn_downloadQR = findViewById(R.id.btn_downloadQR);
        iv_QRcode = findViewById(R.id.iv_QRcode);
        btn_share = findViewById(R.id.btn_share);
        tv_invitationCode = findViewById(R.id.tv_invitationCode);
        //长按复制文本到剪切板
        tv_invitationCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 从API11开始android推荐使用android.content.ClipboardManager
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(tv_invitationCode.getText());
                ToastUtil.showShort(WelfareActivity.this, "邀请码已复制");
                return true;
            }
        });
    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);
        btn_downloadQR.setOnClickListener(this);
        btn_share.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        qrCodeService = new QRCodeService();
        try {
            bitmapQR = qrCodeService.cretaeBitmap(QRPath, null);
            iv_QRcode.setImageBitmap(bitmapQR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back
                    :
                finish();
                break;
            case R.id.btn_share: //分享
                ToastUtil.showShort(this, "分享");
                break;
            case R.id.btn_downloadQR: //保存二维码到本地相册
                new Thread(saveFileRunnable).start();
                break;
        }
    }

    private Runnable saveFileRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                ImageUtil.saveBitmap2SysPhoto(WelfareActivity.this, bitmapQR, "QR_acg.jpg");
                mSaveMessage = "二维码已储存至相册";
            } catch (IOException e) {
                mSaveMessage = "二维码储存至相册失败";
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //handler 发送数据
            Message message = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("saveMessage", mSaveMessage);
            message.setData(bundle);
            message.arg1 = 0;
            handler.sendMessage(message);
            //            handler.sendMessage(handler.obtainMessage());
        }

    };


}
