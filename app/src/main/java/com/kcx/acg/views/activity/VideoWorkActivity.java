package com.kcx.acg.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferType;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.google.common.collect.Lists;
import com.kcx.acg.R;
import com.kcx.acg.aws.AWSUtil;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.base.BusEvent;
import com.kcx.acg.bean.GetProductByIDBean;
import com.kcx.acg.bean.GetProductInfoBean;
import com.kcx.acg.bean.LocalMediaVideoBean;
import com.kcx.acg.bean.VideoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.manager.AccountManager;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.views.adapter.VideoWorkAdapter;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.SwipeItemLayout;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.Serializable;
import java.util.List;


/**
 * Created by zjb on 2018/11/24.
 * 视频作品
 */
public class VideoWorkActivity extends BaseActivity {
    private static final String TAG = VideoWorkActivity.class.getSimpleName();
    private LinearLayout ll_back;
    private TextView tv_title;
    private RecyclerView mRecyclerView;
    private Button btn_add, btn_complete;

    private VideoWorkAdapter videoWorkAdapter;
    private TransferUtility transferUtility;
    private int completedNumber;
    private List<TransferObserver> observerList, observers;
    private List<VideoBean> videoTempList;
    private List<GetProductByIDBean.ReturnDataBean.VideoListBean> videoList;
    private List<LocalMediaVideoBean> localMediaVideolist;
    private List<GetProductInfoBean.ReturnDataBean.DetailListBean> playerList;

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_video_work, null);
    }

    @Override
    public void initView() {
        super.initView();
        ll_back = findViewById(R.id.ll_back);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.contribute_videoWork);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        btn_add = findViewById(R.id.btn_add);
        btn_complete = findViewById(R.id.btn_complete);

        transferUtility = AWSUtil.getTransferUtility(this);
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
        playerList = Lists.newArrayList();
        observers = Lists.newArrayList();
        videoTempList = Lists.newArrayList();
        localMediaVideolist = (List<LocalMediaVideoBean>) SPUtil.readObject(VideoWorkActivity.this, Constants.LOCAL_MEDIA_VIDEO);
        if (localMediaVideolist == null) {
            localMediaVideolist = Lists.newArrayList();
        }
        videoList = (List<GetProductByIDBean.ReturnDataBean.VideoListBean>) getIntent().getSerializableExtra("videoList");
        if (videoList != null) {
            for (int i = 0; i < videoList.size(); i++) {
                VideoBean videoBean = new VideoBean();
                videoBean.setProductDetailID(videoList.get(i).getProductDetailID());
                videoBean.setProductID(videoList.get(i).getProductID());
                videoBean.setS3Key(videoList.get(i).getS3Key());
                videoBean.setVideoDuration(videoList.get(i).getVideoDuration());
                videoBean.setUrl(videoList.get(i).getUrl());
                videoBean.setName(videoList.get(i).getName());
                videoBean.setSize((long) videoList.get(i).getSize());
                videoTempList.add(videoBean);

                LocalMediaVideoBean localMediaVideoBean = new LocalMediaVideoBean();
                localMediaVideoBean.setDuration(videoList.get(i).getVideoDuration());
                localMediaVideolist.add(localMediaVideoBean);
            }
        }
        observerList = transferUtility.getTransfersWithType(TransferType.UPLOAD);
        for (int i = 0; i < observerList.size(); i++) {
            if (observerList.get(i).getAbsoluteFilePath().contains(".mp4")) {
                observers.add(observerList.get(i));
                VideoBean videoBean = new VideoBean();
                videoBean.setTransferObserver(observerList.get(i));
                videoBean.setProductDetailID(0);
                videoBean.setSize(observerList.get(i).getBytesTotal());
                for (int j = 0; j < observers.size(); j++) {
                    videoBean.setVideoDuration(localMediaVideolist.get(j).getDuration());
                }
                String path = observerList.get(i).getAbsoluteFilePath();
                videoBean.setName(new File(path).getName());
                videoTempList.add(videoBean);
            }
        }

        videoWorkAdapter.update(videoTempList);
        refreshAddBtnState();
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
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofVideo())
                        .theme(R.style.picture_style)
                        .isCamera(false)
                        .previewEggs(true)
                        .minSelectNum(1)
                        .maxSelectNum(5 - videoTempList.size())
                        .videoMinSecond(5)
                        .videoMaxSecond(1800)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));
        videoWorkAdapter = new VideoWorkAdapter(this, videoTempList);
        // 设置adapter
        mRecyclerView.setAdapter(videoWorkAdapter);

        videoWorkAdapter.setOnItemClickListener(new VideoWorkAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                TransferObserver transferObserver = videoTempList.get(position).getTransferObserver();
                LogUtil.e("onItemClick", AppUtil.getGson().toJson(transferObserver));
                if (transferObserver != null) {
                    if (Constants.UploadState.FAILED.equals(transferObserver.getState() + "")
                            || Constants.UploadState.PAUSED.equals(transferObserver.getState() + "")) {
                        if (videoTempList.get(position).isNullFile()) {
                            CustomToast.showToast(getString(R.string.video_isDel));
                        } else {
                            transferUtility.resume(transferObserver.getId());
                            transferObserver.setTransferListener(new UploadListener());
                        }
                    } else if (Constants.UploadState.IN_PROGRESS.equals(transferObserver.getState() + "")) {
                        transferUtility.pause(observers.get(position).getId());
                    } else if (Constants.UploadState.COMPLETED.equals(transferObserver.getState() + "")) {
                        goPlayer(transferObserver.getAbsoluteFilePath().toString());
                    }
                } else {
                    goPlayer(videoTempList.get(position).getUrl());
                }
            }

            @Override
            public void onDelClick(int position) {
                if (videoTempList.get(position).getTransferObserver() != null) {
                    transferUtility.deleteTransferRecord(observers.get(position).getId());
                }
                videoTempList.remove(position);
                localMediaVideolist.remove(position);
                //                videoWorkAdapter.notifyItemRemoved(position);
                videoWorkAdapter.notifyDataSetChanged();
                refreshAddBtnState();
                refreshBtnState();
                EventBus.getDefault().post(new BusEvent(BusEvent.UPLOAD_VIDEO_STATE, videoTempList));
            }
        });
    }

    private void goPlayer(String path) {
        playerList.clear();
        GetProductInfoBean.ReturnDataBean.DetailListBean detailListBean = new GetProductInfoBean.ReturnDataBean.DetailListBean();
        detailListBean.setUrl(path);
        playerList.add(detailListBean);
        Intent intent = new Intent(VideoWorkActivity.this, VideoPlayerActivity2.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(VideoPlayerActivity.KEY_VIDEO_LIST, (Serializable) playerList);
        intent.putExtra(VideoPlayerActivity.KEY_VIDEO_POSITION, 0);
        intent.putExtras(bundle);
        startDDMActivity(intent, true);
    }


    /**
     * 开始上传文件
     *
     * @param filePath
     */
    private void beginUpload(String filePath, Long duration) {
        if (filePath == null) {
            CustomToast.showToast("文件为空！");
            return;
        }
        String key = AWSUtil.getKey("images/macgn/", ".mp4");
        File file = new File(filePath);

        TransferObserver observer = transferUtility.upload(AccountManager.getInstances().getAmazonS3Config().getBucketName(), key, file);
        observers.add(observer);
        observer.setTransferListener(new UploadListener());
        VideoBean videoBean = new VideoBean();
        videoBean.setTransferObserver(observer);
        videoBean.setProductDetailID(0);
        videoBean.setVideoDuration(duration);
        String path = observer.getAbsoluteFilePath();
        videoBean.setName(new File(path).getName());
        videoBean.setSize(observer.getBytesTotal());
        videoTempList.add(videoBean);
        refreshAddBtnState();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 视频选择结果回调
                    List<LocalMedia> localMedia = PictureSelector.obtainMultipleResult(data);
                    for (int i = 0; i < localMedia.size(); i++) {
                        LocalMediaVideoBean localMediaVideoBean = new LocalMediaVideoBean();
                        localMediaVideoBean.setDuration(localMedia.get(i).getDuration());
                        localMediaVideolist.add(localMediaVideoBean);
                        beginUpload(localMedia.get(i).getPath(), localMedia.get(i).getDuration());
                    }
                    break;
            }
        }
    }

    class UploadListener implements TransferListener {
        @Override
        public void onError(int id, Exception e) {
            LogUtil.e(TAG, "视频上传错误: " + e.toString());
            if ("java.lang.IllegalArgumentException: The specified file doesn't exist".equals(e.toString())) {
                for (int i = 0; i < videoTempList.size(); i++) {
                    if (videoTempList.get(i).getTransferObserver() != null) {
                        if (videoTempList.get(i).getTransferObserver().getId() == id) {
                            videoTempList.get(i).setNullFile(true);
                        }
                    }
                }
            }
            videoWorkAdapter.update(videoTempList);
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            LogUtil.e(TAG, String.format("onProgressChanged: %d, total: %d, current: %d", id, bytesTotal, bytesCurrent));
            videoWorkAdapter.update(videoTempList);
        }

        @Override
        public void onStateChanged(int id, TransferState newState) {
            LogUtil.e(TAG, "onStateChanged:id= " + id + ",newState= " + newState + ",observers.get(0).getState()=" + observers.get(0).getState());
            videoWorkAdapter.update(videoTempList);
            refreshBtnState();
            EventBus.getDefault().post(new BusEvent(BusEvent.UPLOAD_VIDEO_STATE, videoTempList));
        }
    }

    private void refreshAddBtnState() {
        if (videoTempList.size() >= 5) {
            btn_add.setVisibility(View.INVISIBLE);
        } else {
            btn_add.setVisibility(View.VISIBLE);
        }
    }

    private void refreshBtnState() {
        observers.clear();
        observerList.clear();
        observerList = transferUtility.getTransfersWithType(TransferType.UPLOAD);
        for (int i = 0; i < observerList.size(); i++) {
            if (observerList.get(i).getAbsoluteFilePath().contains(".mp4")) {
                observers.add(observerList.get(i));
            }
        }
        completedNumber = videoTempList.size() - observers.size();
        if (videoTempList.size() == 0) {
            btn_complete.setText(getString(R.string.upload_picture_complete));
        } else {
            for (int i = 0; i < videoTempList.size(); i++) {
                if (videoTempList.get(i).getTransferObserver() != null) {
                    if (Constants.UploadState.COMPLETED.equals(videoTempList.get(i).getTransferObserver().getState() + "")) {
                        completedNumber++;
                    }

                }
            }
            btn_complete.setText(getString(R.string.upload_picture_complete) + "(" + completedNumber + "/" + videoTempList.size() + ")");
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

    private void exit() {
        SPUtil.saveObject(VideoWorkActivity.this, Constants.LOCAL_MEDIA_VIDEO, localMediaVideolist);
        EventBus.getDefault().post(new BusEvent(BusEvent.UPLOAD_VIDEO_STATE, videoTempList));
        finish();
    }

}
