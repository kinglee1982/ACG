package com.kcx.acg.views.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.utils.ImageFloder;
import com.kcx.acg.views.adapter.CameraListAdapter;

import java.util.List;


/**
 * Created by jb on 2018/2/11.
 */

public class PhotoChoseDialog {
    private  String title,hint,item1Str,item2Str;
    private Context context;
    private int layout;
    private double pHeight;
    private Boolean cancelable = true;
    private BottonDialogListener mlistener = null;
    private Dialog bottomDialog;
    private List<ImageFloder> mImageFloders;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private CameraListAdapter cameraListAdapter;



    public interface BottonDialogListener {
        void selected(ImageFloder floder);
    }



    public PhotoChoseDialog(Context context, int layout, double pHeight, List<ImageFloder> mImageFloders, BottonDialogListener mlistener) {
        this.context = context;
        this.layout = layout;
        this.pHeight = pHeight;
        this.mlistener = mlistener;
        this.mImageFloders = mImageFloders;
        initView();
    }


    private void initView() {
        bottomDialog = new Dialog(context, R.style.dialog);
        View contentView = LayoutInflater.from(context).inflate(layout, null);
        bottomDialog.setContentView(contentView);
        setData(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);

            WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE); //为获取屏幕宽、高
            android.view.WindowManager.LayoutParams p = bottomDialog.getWindow().getAttributes();  //获取对话框当前的参数值
            p.height = (int) (wm.getDefaultDisplay().getHeight() * pHeight);   //高度设置为屏幕的0.75
            p.width = (int) (wm.getDefaultDisplay().getWidth() * 1);    //宽度设置为屏幕的1
            bottomDialog.getWindow().setAttributes(p);     //设置生效

        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.setCancelable(cancelable);
        bottomDialog.setCanceledOnTouchOutside(cancelable);
        bottomDialog.show();

    }


    private void setData(View contentView) {
        mRecyclerView = contentView.findViewById(R.id.mCameraRecyclerView);
        initCamearListView();


    }

    private class clickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

//                case R.id.tv_item2:
//                    bottomDialog.cancel();
//                    break;


            }
        }
    }

    //初始化本地相册列表
    private void initCamearListView() {
        mLayoutManager = new LinearLayoutManager(((Activity) context), LinearLayoutManager.VERTICAL, false);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        cameraListAdapter = new CameraListAdapter(context, mImageFloders);
        // 设置adapter
        mRecyclerView.setAdapter(cameraListAdapter);
        cameraListAdapter.setOnItemClickListener(new CameraListAdapter.OnItemClickListener() {
            @Override
            public void selected(ImageFloder floder) {
                bottomDialog.cancel();
                if (mlistener != null) {
                    mlistener.selected(floder);
                }
            }
        });
    }

}
