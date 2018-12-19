package com.kcx.acg.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferType;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.google.common.collect.Lists;
import com.kcx.acg.R;
import com.kcx.acg.aws.AWSUtil;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.base.BusEvent;
import com.kcx.acg.bean.GetProductByIDBean;
import com.kcx.acg.bean.PictureBean;
import com.kcx.acg.bean.PictureUploadBean;
import com.kcx.acg.bean.VideoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.manager.AccountManager;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.views.adapter.PictureWorkAdapter2;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.FullyGridLayoutManager;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.Serializable;
import java.security.spec.EllipticCurve;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片作品
 * Created by zjb on 2018/11/22.
 */
public class PictureWorkActivity extends BaseActivity {
    private static final int REQUEST_CODE = 0x00000011;
    private static final String TAG = PictureWorkActivity.class.getSimpleName();

    private LinearLayout ll_back;
    private TextView tv_title;
    private RecyclerView mRecyclerView;
    private PictureWorkAdapter2 pictureWorkAdapter2;
    private DelegateAdapter delegateAdapter;
    private Button btn_add, btn_complete;
    private PictureUploadBean pictureUploadBean;

    private TransferUtility transferUtility;
    private List<GetProductByIDBean.ReturnDataBean.PictureListBean> pictureList;
    private List<PictureBean> pictureTempList;
    private List<TransferObserver> observers, observerList;
    private int completedNumber = 0;
    private int productID;
    private List<String> pathList;


    @Override
    public void onEventMainThread(BusEvent event) {
        super.onEventMainThread(event);
        if (event.getType() == BusEvent.PICTURE_DEL) {
            delItem(event.getIntParam());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_picture_work, null);
    }

    @Override
    public void initView() {
        super.initView();
        ll_back = findViewById(R.id.ll_back);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.contribute_pictureWork);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        btn_add = findViewById(R.id.btn_add);
        btn_complete = findViewById(R.id.btn_complete);

        setRecycledViewPool(mRecyclerView);
        mRecyclerView.setLayoutManager(layoutManager);
        initRecyclerView();
    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_complete.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        observers = Lists.newArrayList();
        observerList = Lists.newArrayList();
        pictureTempList = Lists.newArrayList();
        pathList = Lists.newArrayList();

        productID = getIntent().getIntExtra("productID", 0);
        pictureList = (List<GetProductByIDBean.ReturnDataBean.PictureListBean>) getIntent().getSerializableExtra("pictureList");
        if (pictureList != null && productID != 0) {
            for (int i = 0; i < pictureList.size(); i++) {
                PictureBean pictureBean = new PictureBean();
                pictureBean.setProductDetailID(pictureList.get(i).getProductDetailID());
                pictureBean.setS3Key(pictureList.get(i).getS3Key());
                pictureBean.setUrl(pictureList.get(i).getUrl());
                pictureTempList.add(pictureBean);
            }
        }

        transferUtility = AWSUtil.getTransferUtility(this);
        List<TransferObserver> observerList = transferUtility.getTransfersWithType(TransferType.UPLOAD);
        for (int i = 0; i < observerList.size(); i++) {
            if (observerList.get(i).getAbsoluteFilePath().contains(".jpg")
                    || observerList.get(i).getAbsoluteFilePath().contains(".jpeg")
                    || observerList.get(i).getAbsoluteFilePath().contains(".png")
                    || observerList.get(i).getAbsoluteFilePath().contains(".bmp")
                    && !SPUtil.getString(PictureWorkActivity.this, Constants.COVER_IMAGE_ID, "").equals(String.valueOf(observerList.get(i).getId()))) {
                observers.add(observerList.get(i));

                PictureBean pictureBean = new PictureBean();
                pictureBean.setTransferObserver(observerList.get(i));
                pictureBean.setProductDetailID(0);
                pictureTempList.add(pictureBean);
            }
        }

        pictureWorkAdapter2.update(pictureTempList);
        refreshBtnState();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                exit();
                break;
            case R.id.btn_complete:
                exit();
                break;
            case R.id.btn_add:
                PictureSelector.create(PictureWorkActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .theme(R.style.picture_style)
                        .minSelectNum(1)
                        .maxSelectNum(50 - pictureTempList.size())
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
        }
    }

    private void exit() {
        EventBus.getDefault().post(new BusEvent(BusEvent.UPLOAD_PICTURE_STATE, pictureTempList));
        finish();
    }

    private void initRecyclerView() {
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(3);
        gridLayoutHelper.setAutoExpand(false);
        gridLayoutHelper.setMargin(5, 5, 5, 5);
        FullyGridLayoutManager mgr = new FullyGridLayoutManager(PictureWorkActivity.this, 3);
        // 设置布局管理器
        pictureWorkAdapter2 = new PictureWorkAdapter2(this, gridLayoutHelper, pictureTempList);
        delegateAdapter = new DelegateAdapter(layoutManager);
        delegateAdapter.addAdapter(pictureWorkAdapter2);
        mRecyclerView.setAdapter(delegateAdapter);
        //        pictureWorkAdapter = new PictureWorkAdapter(PictureWorkActivity.this, observers);
        //        mRecyclerView.setAdapter(pictureWorkAdapter);
        pictureWorkAdapter2.setOnItemClickListener(new PictureWorkAdapter2.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (pictureTempList.get(position).getTransferObserver() != null) {
                    if (Constants.UploadState.FAILED.equals(pictureTempList.get(position).getTransferObserver().getState() + "")) {
                        transferUtility.resume(pictureTempList.get(position).getTransferObserver().getId());
                        pictureTempList.get(position).getTransferObserver().setTransferListener(new UploadListener());
                    } else {
                        goPictureViewer(position);
                    }
                } else {
                    goPictureViewer(position);
                }
            }

            @Override
            public void onDelClick(int position) {
                delItem(position);

            }

            @Override
            public void onAddPic(int position) {
                ImageSelector.builder()
                        .useCamera(false) // 设置是否使用拍照
                        .setSingle(false)  //设置是否单选
                        .setViewImage(true) //是否点击放大图片查看,，默认为true
                        .setMaxSelectCount(50 - pictureTempList.size()) // 图片的最大选择数量，小于等于0时，不限数量。
                        .start(PictureWorkActivity.this, REQUEST_CODE); // 打开相册
            }
        });

    }

    private void delItem(int position) {
        if (pictureTempList.get(position).getTransferObserver() != null) {
            transferUtility.deleteTransferRecord(pictureTempList.get(position).getTransferObserver().getId());
        }
        pictureTempList.remove(position);
        pictureWorkAdapter2.notifyDataSetChanged();
        delegateAdapter.notifyDataSetChanged();
        refreshBtnState();
    }

    private void goPictureViewer(int position) {
        pathList.clear();
        for (int i = 0; i < pictureTempList.size(); i++) {
            if (pictureTempList.get(i).getTransferObserver() != null) {
                pathList.add(pictureTempList.get(i).getTransferObserver().getAbsoluteFilePath());
            } else {
                pathList.add(pictureTempList.get(i).getUrl());
            }
        }

        Intent intent = new Intent(PictureWorkActivity.this, PictureViewer2Activity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("pathList", (Serializable) pathList);
        bundle.putInt(PictureViewer2Activity.KEY_PIC_POSITION, position);
        intent.putExtras(bundle);
        startDDMActivity(intent, true);
    }

    private void refreshBtnState() {
        observers.clear();
        observerList.clear();
        observerList = transferUtility.getTransfersWithType(TransferType.UPLOAD);
        for (int i = 0; i < observerList.size(); i++) {
            if (observerList.get(i).getAbsoluteFilePath().contains(".jpg")
                    || observerList.get(i).getAbsoluteFilePath().contains(".jpeg")
                    || observerList.get(i).getAbsoluteFilePath().contains(".png")
                    || observerList.get(i).getAbsoluteFilePath().contains(".bmp")
                    && !SPUtil.getString(PictureWorkActivity.this, Constants.COVER_IMAGE_ID, "").equals(String.valueOf(observerList.get(i).getId()))) {
                observers.add(observerList.get(i));
            }
        }
        completedNumber = pictureTempList.size() - observers.size();
        if (pictureTempList.size() == 0) {
            btn_complete.setText(getString(R.string.upload_picture_complete));
        } else {
            for (int i = 0; i < pictureTempList.size(); i++) {
                if (pictureTempList.get(i).getTransferObserver() != null) {
                    if (Constants.UploadState.COMPLETED.equals(pictureTempList.get(i).getTransferObserver().getState() + "")) {
                        completedNumber++;
                    }
                }
            }
            btn_complete.setText(getString(R.string.upload_picture_complete) + "(" + completedNumber + "/" + pictureTempList.size() + ")");
        }
    }

    private void beginUpload(String filePath) {
        if (filePath == null) {
            CustomToast.showToast("文件为空！");
            return;
        }
        String key = AWSUtil.getKey("images/macgn/", ".jpg");
        File file = new File(filePath);
        TransferObserver observer = transferUtility.upload(AccountManager.getInstances().getAmazonS3Config().getBucketName(), key, file);
        observer.setTransferListener(new UploadListener());
        PictureBean pictureBean = new PictureBean();
        pictureBean.setProductDetailID(0);
        pictureBean.setTransferObserver(observer);
        pictureTempList.add(pictureBean);
        observers.add(observer);

        btn_complete.setText(getString(R.string.upload_picture_complete) + "(" + completedNumber + "/" + pictureTempList.size() + ")");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 图片选择结果回调（图片作品）
        if (requestCode == REQUEST_CODE && data != null) {
            ArrayList<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
            for (int i = 0; i < images.size(); i++) {
                LogUtil.e("图片路径" + i + "=", images.get(i));
                beginUpload(images.get(i));
            }
        }
    }


    class UploadListener implements TransferListener {
        @Override
        public void onError(int id, Exception e) {
            LogUtil.e(TAG, "Error during upload: " + id + e.toString());
            pictureWorkAdapter2.notifyDataSetChanged();
            delegateAdapter.notifyDataSetChanged();
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            LogUtil.e(TAG, String.format("onProgressChanged: %d, total: %d, current: %d", id, bytesTotal, bytesCurrent));
            //            pictureWorkAdapter.update(observers);
            pictureWorkAdapter2.notifyDataSetChanged();
            delegateAdapter.notifyDataSetChanged();

        }

        @Override
        public void onStateChanged(int id, TransferState newState) {
            LogUtil.e(TAG, "onStateChanged:id= " + id + ",newState= " + newState);
            //            pictureWorkAdapter.update(observers);
            //            pictureWorkAdapter.update(observers);
            refreshBtnState();
            pictureWorkAdapter2.notifyDataSetChanged();
            delegateAdapter.notifyDataSetChanged();
            refreshBtnState();
            EventBus.getDefault().post(new BusEvent(BusEvent.UPLOAD_PICTURE_STATE, pictureTempList));
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
