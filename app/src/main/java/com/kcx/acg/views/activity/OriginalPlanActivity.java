package com.kcx.acg.views.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.google.common.collect.Lists;
import com.kcx.acg.R;
import com.kcx.acg.api.SaveMemberAuthenticateUrlApi;
import com.kcx.acg.aws.AWSUtil;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.base.SysApplication;
import com.kcx.acg.bean.AmazonS3ConfigBean;
import com.kcx.acg.bean.SaveMemberAuthenticateUrlBean;
import com.kcx.acg.bean.UserInfoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.manager.AccountManager;
import com.kcx.acg.manager.DialogManager;
import com.kcx.acg.manager.UpdateManager;
import com.kcx.acg.utils.FileSizeUtil;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.utils.NetWorkSpeedUtils;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.HomeDialog;
import com.kcx.acg.views.view.OriginalDialog;
import com.kcx.acg.views.view.TitleBarView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.vincent.videocompressor.VideoCompress;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import me.jessyan.autosize.internal.CancelAdapt;

public class OriginalPlanActivity extends BaseActivity implements CancelAdapt {
    private static final int REQUEST_CODE_CHOOSE = 0x1101;
    private View rootView;
    private LinearLayout uploadLayout;
    private TitleBarView titleBarView;
    private ImageView backIv;
    private ImageView uploadStatusIv;
    private TextView uploadStatusTv;
    private List<LocalMedia> selectList;
    private OriginalDialog dialog;
    private OriginalDialog.Builder builder;
    private UserInfoBean userInfo;
    private TransferUtility transferUtility;
    private long l2;
    NetWorkSpeedUtils speedUtils;
    TransferObserver observer;
    private int id;
    private HomeDialog.Builder permissionBuilder;

    @Override
    public View setInitView() {
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_original_plan, null);
        titleBarView = rootView.findViewById(R.id.original_plan_titlebar);
        backIv = titleBarView.getIv_in_title_back();
        uploadLayout = rootView.findViewById(R.id.original_upload_layout);
        uploadStatusIv = rootView.findViewById(R.id.original_upload_iv);
        uploadStatusTv = rootView.findViewById(R.id.original_upload_tv);

        transferUtility = AWSUtil.getTransferUtility(this);
        permissionBuilder = new HomeDialog.Builder(this);
        permissionBuilder.setLayoutId(R.layout.dialog_premission_layout);
        return rootView;
    }

    @Override
    public void initData() {
        selectList = Lists.newArrayList();
        userInfo = AccountManager.getInstances().getUserInfo(this);
        switch (userInfo.getReturnData().getAuthenticateStatus()) {
            case Constants.UserInfo.STATUS_USER_AUTHENTICATE_UN_SUB:
                uploadStatusIv.setImageResource(R.mipmap.yc_icon_cloud);
                uploadStatusTv.setText(R.string.original_btn_msg1);
                uploadLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_pink_bg_5dp));
                break;
            case Constants.UserInfo.STATUS_USER_AUTHENTICATE_REVIEW:
                uploadLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_blue_bg_5dp));
                uploadStatusIv.setImageResource(R.mipmap.yc_icon_dendai);
                uploadStatusTv.setText(R.string.original_btn_msg2);
                break;
            case Constants.UserInfo.STATUS_USER_AUTHENTICATE_PASSED:
                uploadLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_blue_bg_5dp));
                uploadStatusIv.setImageResource(R.mipmap.yc_icon_yirenzheng);
                uploadStatusTv.setText(R.string.original_btn_msg3);
                break;
            case Constants.UserInfo.STATUS_USER_AUTHENTICATE_UNPASS:
                uploadLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_pink_bg_5dp));
                uploadStatusIv.setImageResource(R.mipmap.yc_icon_renzheng_failed);
                uploadStatusTv.setText(R.string.original_btn_msg4);
                break;
        }
    }

    @Override
    public void setListener() {
        uploadLayout.setOnClickListener(this);
        backIv.setOnClickListener(this);
        permissionBuilder.setOnClickListener(new HomeDialog.Builder.OnClickListener() {
            @Override
            public void onConfrim() {
                ActivityCompat.requestPermissions(
                        OriginalPlanActivity.this,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                );
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.original_upload_layout:

                int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    permissionBuilder.builder().show();
                } else {
                    if (userInfo.getReturnData().getAuthenticateStatus() == Constants.UserInfo.STATUS_USER_AUTHENTICATE_PASSED) {
                        startDDMActivity(CreativeCenterActivity.class, true);
                    } else {
                        PictureSelector.create(this)
                                .openGallery(PictureMimeType.ofVideo())
                                .theme(R.style.picture_style)
                                .previewEggs(true)
                                .minSelectNum(1)
                                .isCamera(false)
                                .maxSelectNum(1)
                                .videoMinSecond(5)
                                .enableCrop(true)
//                            .videoMaxSecond(60 * 5)
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                    }
                }

                break;
            case R.id.iv_in_title_back:
                finish();
                break;
        }
    }

    int REQUEST_EXTERNAL_STORAGE = 1;
    String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户同意，执行操作
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofVideo())
                        .theme(R.style.picture_style)
                        .previewEggs(true)
                        .minSelectNum(1)
                        .isCamera(false)
                        .maxSelectNum(1)
                        .videoMinSecond(5)
                        .enableCrop(true)
//                            .videoMaxSecond(60 * 5)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            } else {
                permissionBuilder.builder().show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    selectList.clear();
                    selectList.addAll(PictureSelector.obtainMultipleResult(data));

                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    final LocalMedia media = selectList.get(0);
//                    if (media.getDuration() < (5 * 1000) || media.getDuration() > (60 * 1000)) {
//                        CustomToast.showToast(getString(R.string.original_plan_video_err_msg));
//                        return;
//                    }
//                    upload(media.getPath());
                    speedUtils = new NetWorkSpeedUtils(OriginalPlanActivity.this, handler);

                    double fileSizeKb = FileSizeUtil.getFileOrFilesSize(selectList.get(0).getPath(), FileSizeUtil.SIZETYPE_KB);
                    builder = new OriginalDialog.Builder(this);
                    dialog = builder.create(media);
                    dialog.show();
                    compressVideo(media.getPath());
                    builder.setOnRetryListener(new OriginalDialog.Builder.OnRetryListener() {
                        @Override
                        public void onRetry() {
//                            upload(media.getPath());
                            transferUtility.resume(observer.getId());
                            observer.setTransferListener(uploadListener);
                        }
                    });
                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            transferUtility.cancel(observer.getId());
                            transferUtility.deleteTransferRecord(observer.getId());
                            observer.cleanTransferListener();
                        }
                    });
                    WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                    lp.width = SysApplication.mWidthPixels; //设置宽度
                    dialog.getWindow().setAttributes(lp);
                    break;
            }
        }
    }

    private Locale getLocale() {
        Configuration config = getResources().getConfiguration();
        Locale sysLocale = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = getSystemLocale(config);
        } else {
            sysLocale = getSystemLocaleLegacy(config);
        }

        return sysLocale;
    }

    @SuppressWarnings("deprecation")
    public static Locale getSystemLocaleLegacy(Configuration config) {
        return config.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSystemLocale(Configuration config) {
        return config.getLocales().get(0);
    }


    public void compressVideo(String path) {

        final String destPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/ACG/" + new SimpleDateFormat("yyyyMMdd_HHmmss", getLocale()).format(new Date()) + ".mp4";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/ACG");
        file.mkdirs();
        VideoCompress.compressVideoMedium(path, destPath, new VideoCompress.CompressListener() {
            @Override
            public void onStart() {
                LogUtils.d("onStart");
                builder.setTitle(getString(R.string.original_compressing_msg));
            }

            @Override
            public void onSuccess() {

                LogUtils.d(destPath);
                upload(destPath);
            }

            @Override
            public void onFail() {
                LogUtils.d("fail");
            }

            @Override
            public void onProgress(float percent) {
//                tv_progress.setText(String.valueOf(percent) + "%");
                builder.setProgress((int) percent);
            }
        });
    }

    public void upload(final String filePath) {
        final File file = new File(filePath);
        observer = transferUtility.upload(AccountManager.getInstances().getAmazonS3Config().getBucketName(), createKey(), file);
        observer.setTransferListener(uploadListener);
    }

    private TransferListener uploadListener = new TransferListener() {
        @Override
        public void onStateChanged(int i, TransferState transferState) {
            LogUtils.d(transferState);
            speedUtils.stopTime();
            if (transferState == TransferState.COMPLETED) {
                LogUtils.d(observer.getKey());
//                    CustomToast.showToast("上传成功");
                getUrl(observer);
                builder.setSuccess();
                uploadStatusIv.setImageResource(R.mipmap.yc_icon_dendai);
                uploadStatusTv.setText(R.string.original_btn_msg2);
                transferUtility.deleteTransferRecord(observer.getId());
            } else if (transferState == TransferState.FAILED || transferState == TransferState.UNKNOWN) {
                LogUtils.d(transferState.toString());
                id = i;
                speedUtils.stopTime();
                builder.setError();
                transferUtility.pause(i);
                transferUtility.deleteTransferRecord(observer.getId());
            } else if (transferState == TransferState.IN_PROGRESS) {
                builder.setUploading();
            }
        }

        @Override
        public void onProgressChanged(int i, long l, long l1) {
            int progress = (int) (l * 100 / l1);
            builder.setProgress(progress);
            l2 = l1 - l;
            int temp = speedUtils.getNetSpeed();
            if (temp > 1024) {
                int secs = (int) (l2 / temp);
                builder.setTime(secs);
            }
        }

        @Override
        public void onError(int i, Exception e) {
            LogUtil.e(e.toString());
            id = i;
            speedUtils.stopTime();
            builder.setError();
            transferUtility.pause(i);

        }
    };

    public void uploadResume(int id) {
        transferUtility.resume(id);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int i = msg.arg1;
            int secs = (int) (l2 / i);
            builder.setTime(secs);
            LogUtils.d("secs " + secs);
        }
    };

    public void getUrl(TransferObserver observer) {
        AmazonS3 s3 = AWSUtil.getS3Client(OriginalPlanActivity.this);
        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(AccountManager.getInstances().getAmazonS3Config().getBucketName(), observer.getKey());
        URL url = s3.generatePresignedUrl(urlRequest);
        saveMemberAuthenticateUrl(url.toString());
        LogUtils.d(url);
    }

    public String createKey() {
        String time = getCurrentTime("yyyyMMdd");
        String url = "videos/Auth/" + time + "/" + UUID.randomUUID().toString() + ".mp4";
        return url;
    }

    public void saveMemberAuthenticateUrl(String authenticateVideoUrl) {
        SaveMemberAuthenticateUrlApi saveMemberAuthenticateUrlApi = new SaveMemberAuthenticateUrlApi(this);
        saveMemberAuthenticateUrlApi.setAuthenticateVideoUrl(authenticateVideoUrl);
        saveMemberAuthenticateUrlApi.setListener(new HttpOnNextListener<SaveMemberAuthenticateUrlBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(SaveMemberAuthenticateUrlBean saveMemberAuthenticateUrlBean) {
                if (saveMemberAuthenticateUrlBean.getErrorCode() == 200) {
                    AccountManager.getInstances().refreshUserInfo(OriginalPlanActivity.this);
                }
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(this, saveMemberAuthenticateUrlApi);
    }
}
