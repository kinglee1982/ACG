package com.kcx.acg.views.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferType;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.donkingliang.labels.LabelsView;
import com.google.common.collect.Lists;
import com.kcx.acg.R;
import com.kcx.acg.api.GetProductByIDApi;
import com.kcx.acg.api.SaveProductApi;
import com.kcx.acg.api.SearchHotTagApi;
import com.kcx.acg.aws.AWSUtil;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.base.BusEvent;
import com.kcx.acg.bean.GetProductByIDBean;
import com.kcx.acg.bean.HotTagBean;
import com.kcx.acg.bean.LocalMediaVideoBean;
import com.kcx.acg.bean.PictureBean;
import com.kcx.acg.bean.SaveProductBean;
import com.kcx.acg.bean.SaveProductVO;
import com.kcx.acg.bean.VideoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.ClickListener;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.manager.AccountManager;
import com.kcx.acg.manager.H5LinkUrlManager;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.BottomDialogUtil;
import com.kcx.acg.utils.DialogMaker;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.utils.TextViewUtil;
import com.kcx.acg.views.adapter.LabelAdapter;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.GlideRoundTransform;
import com.kcx.acg.views.view.RegisterSuccessDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjb on 2018/11/22.
 * 投稿界面
 */
public class ContributeActivity extends BaseActivity implements ClickListener {
    private static final String TAG = ContributeActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 0x00000011;
    private RequestOptions options;
    private Dialog dialog = null;

    private TextView tv_pictureWork, tv_videoWork, tv_onUpload_picture, tv_onUpload_video;
    private TextView tv_title, tv_right, tv_hint, tv_describe, tv_uploadHint, tv_selectedLabel;
    private ImageView iv1, iv2, iv_cover;
    private RelativeLayout rl_pictureWork, rl_videoWork, rl_uploadHint;
    private LinearLayout ll_back;
    private LabelsView labelsView;
    private ImageView ib_question;
    private Button btn_apply;
    private RecyclerView rv_label;
    private EditText et_title, et_description, et_charge;
    private CheckBox cb_free, cb_charge;
    private TextView tv_desc_count;

    private TransferUtility transferUtility;
    private AmazonS3 s3;
    private GeneratePresignedUrlRequest urlRequest;
    private String bucketName;
    private ArrayList<String> labels;
    private List<String> str_list;
    private List<Integer> color_list;
    private List<HotTagBean.ReturnDataBean.ListBean> hotTagList;
    private List<TransferObserver> pictureObservers, videoObservers;
    private int completedPictureNumber, completedVideoNumber;
    private String coverPicUrl = "";  //封面URL
    private TransferObserver observer;
    private int selecLabelNumber = 0;
    private int productID = 0;  //作品ID （原创默认为0）
    private boolean isNeedPay; //是否需要付费（0：免费；1：付费）
    private boolean isPictureItemUploadState = true;
    private boolean isVideoItemUploadState = true;
    private SaveProductVO saveProductVO;
    private List<SaveProductVO.TagsBean> tagsList;
    private SaveProductVO.TagsBean tagsBean;
    private List<SaveProductVO.ProductDetailsBean> productDetailsList;
    private GetProductByIDBean.ReturnDataBean.VideoListBean videoListBean;
    private GetProductByIDBean.ReturnDataBean.PictureListBean pictureListBean;
    private List<LocalMediaVideoBean> localMediaVideoList;
    private List<GetProductByIDBean.ReturnDataBean.PictureListBean> pictureList;
    private List<GetProductByIDBean.ReturnDataBean.VideoListBean> videoList;
    private List<GetProductByIDBean.ReturnDataBean.ProductTagListBean> productTagList;
    private ProgressBar uploadPro;
    private ProgressBar videoUploadPro;
    private List<PictureBean> pictureTempList;
    private List<VideoBean> videoTempList;

    @Override
    public void onEventMainThread(BusEvent event) {
        super.onEventMainThread(event);
        if (event.getType() == BusEvent.UPLOAD_PICTURE_STATE) {
            pictureList.clear();
            pictureTempList = (List<PictureBean>) event.getParam();
            for (int i = 0; i < pictureTempList.size(); i++) {
                if (pictureTempList.get(i).getTransferObserver() == null) {
                    pictureListBean = new GetProductByIDBean.ReturnDataBean.PictureListBean();
                    pictureListBean.setProductDetailID(pictureTempList.get(i).getProductDetailID());
                    pictureListBean.setS3Key(pictureTempList.get(i).getS3Key());
                    pictureListBean.setUrl(pictureTempList.get(i).getUrl());
                    pictureList.add(pictureListBean);
                }
            }
            refreshPictureUploadState();
        } else if (event.getType() == BusEvent.UPLOAD_VIDEO_STATE) {
            videoList.clear();
            videoTempList = (List<VideoBean>) event.getParam();
            for (int i = 0; i < videoTempList.size(); i++) {
                if (videoTempList.get(i).getTransferObserver() == null) {
                    videoListBean = new GetProductByIDBean.ReturnDataBean.VideoListBean();
                    videoListBean.setName(videoTempList.get(i).getName());
                    videoListBean.setProductDetailID(videoTempList.get(i).getProductDetailID());
                    videoListBean.setS3Key(videoTempList.get(i).getS3Key());
                    videoListBean.setSize(videoTempList.get(i).getSize());
                    videoListBean.setVideoDuration(videoTempList.get(i).getVideoDuration());
                    videoListBean.setUrl(videoTempList.get(i).getUrl());
                    videoList.add(videoListBean);
                }
            }
            refreshVideoUploadState(videoTempList);
        }
    }


    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_contribute, null);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void initView() {
        super.initView();
        productID = getIntent().getIntExtra("productID", 0);
        if (productID != 0) {
            getProductByID(productID);
        }
        transferUtility = AWSUtil.getTransferUtility(this);
        s3 = AWSUtil.getS3Client(ContributeActivity.this);
        bucketName = AccountManager.getInstances().getAmazonS3Config().getBucketName();

        ll_back = findViewById(R.id.ll_back);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.contribute_title);
        tv_right = findViewById(R.id.tv_right);
        tv_right.setText(R.string.contribute_titleRight);
        tv_right.setTextColor(getResources().getColor(R.color.pink_hint));
        tv_right.setVisibility(View.GONE);
        tv_hint = findViewById(R.id.tv_hint);
        tv_describe = findViewById(R.id.tv_describe);
        tv_describe.setText(getString(R.string.contribute_textHint3) + "\n" + getString(R.string.contribute_textHint4));

        tv_pictureWork = findViewById(R.id.tv_pictureWork);
        tv_onUpload_picture = findViewById(R.id.tv_onUpload_picture);
        iv1 = findViewById(R.id.iv1);
        rl_pictureWork = findViewById(R.id.rl_pictureWork);
        tv_videoWork = findViewById(R.id.tv_videoWork);
        tv_onUpload_video = findViewById(R.id.tv_onUpload_video);
        iv2 = findViewById(R.id.iv2);
        rl_videoWork = findViewById(R.id.rl_videoWork);

        iv_cover = findViewById(R.id.iv_cover);
        et_title = findViewById(R.id.et_title);//标题
        et_description = findViewById(R.id.et_description); //描述
        tv_selectedLabel = findViewById(R.id.tv_selectedLabel); //已选标签
        tv_selectedLabel.setText(getString(R.string.certification_selected_label) + "(0/10)");
        labelsView = findViewById(R.id.labelsView);
        cb_free = findViewById(R.id.cb_free);
        cb_charge = findViewById(R.id.cb_charge);
        et_charge = findViewById(R.id.et_charge);

        ib_question = findViewById(R.id.ib_question);
        btn_apply = findViewById(R.id.btn_apply);
        rl_uploadHint = findViewById(R.id.rl_uploadHint);
        tv_uploadHint = findViewById(R.id.tv_uploadHint);
        rv_label = findViewById(R.id.mRecyclerView);
        tv_desc_count = findViewById(R.id.tv_desc_count);
        uploadPro = findViewById(R.id.pro_upload);
        videoUploadPro = findViewById(R.id.pro_video_upload);

        str_list = new ArrayList<String>();
        color_list = new ArrayList<Integer>();
        str_list.add(getResources().getString(R.string.contribute_textHint1));
        str_list.add(getResources().getString(R.string.contribute_textHint2));
        color_list.add(getResources().getColor(R.color.black_666));
        color_list.add(getResources().getColor(R.color.blue_6bb));

        TextViewUtil.setText(ContributeActivity.this, tv_hint, str_list, color_list, this);
    }


    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        rl_pictureWork.setOnClickListener(this);
        rl_videoWork.setOnClickListener(this);
        iv_cover.setOnClickListener(this);
        ib_question.setOnClickListener(this);
        btn_apply.setOnClickListener(this);

        et_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //标签的选中监听
        labelsView.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
            @Override
            public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                //label是被选中的标签，data是标签所对应的数据，isSelect是是否选中，position是标签的位置。
                int tagID = -1;
                int productTagID = 0;
                tagsBean = new SaveProductVO.TagsBean();
                for (int i = 0; i < hotTagList.size(); i++) {
                    if (hotTagList.get(i).getTagName().equals(data.toString())) {
                        tagID = hotTagList.get(i).getTagID();
                    }
                }

                if (productTagList != null) {
                    for (int i = 0; i < productTagList.size(); i++) {
                        if (productTagList.get(i).getTagName().equals(data.toString())) {
                            productTagID = productTagList.get(i).getProductTagID();
                        }
                    }
                }

                if (isSelect) {
                    selecLabelNumber++;
                    tagsBean.setTagID(tagID);
                    tagsBean.setProductTagID(productTagID);
                    tagsList.add(tagsBean);

                } else {
                    selecLabelNumber--;
                    for (int i = 0; i < tagsList.size(); i++) {
                        if (tagsList.get(i).getTagID() == tagID) {
                            tagsList.remove(i);
                        }
                    }
                }
                tv_selectedLabel.setText(getString(R.string.certification_selected_label) + "(" + selecLabelNumber + "/10)");
            }
        });

        cb_free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isNeedPay = false;
                    cb_charge.setChecked(false);
                    et_charge.setText("");
                    et_charge.setEnabled(false);
                }

            }
        });

        cb_charge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    et_charge.setEnabled(true);
                    isNeedPay = true;
                    cb_free.setChecked(false);
                }
            }
        });

        et_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tv_desc_count.setText(charSequence.length() + "/1000");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        labels = Lists.newArrayList();
        pictureObservers = Lists.newArrayList();
        videoObservers = Lists.newArrayList();
        tagsList = Lists.newArrayList();
        productDetailsList = Lists.newArrayList();
        pictureTempList = Lists.newArrayList();
        videoTempList = Lists.newArrayList();
        if (videoList == null) {
            videoList = Lists.newArrayList();
        }
        if (pictureList == null) {
            pictureList = Lists.newArrayList();
        }
        hotTagList = (List<HotTagBean.ReturnDataBean.ListBean>) SPUtil.readObject(ContributeActivity.this, Constants.HOT_TAG);
        for (int i = 0; i < hotTagList.size(); i++) {
            labels.add(hotTagList.get(i).getTagName());
        }
        labelsView.setLabels(labels);

        options = new RequestOptions()
                .placeholder(R.mipmap.pic_cover)  //预加载图片
                .error(R.mipmap.pic_cover)  //加载失败图片
                .priority(Priority.HIGH)
                .transform(new GlideRoundTransform(ContributeActivity.this, 5));
        //        initRecyclerView();
        SPUtil.putString(ContributeActivity.this, Constants.COVER_IMAGE_ID, "");
    }

    /**
     * 根据作品id获取作品信息
     *
     * @param productID
     */
    private void getProductByID(int productID) {
        GetProductByIDApi getProductByIDApi = new GetProductByIDApi(this);
        getProductByIDApi.setProductID(productID);
        getProductByIDApi.setListener(new HttpOnNextListener<GetProductByIDBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(GetProductByIDBean getProductByIDBean) {
                if (getProductByIDBean.getErrorCode() == 200) {
                    String getProductByIDStr = AppUtil.getGson().toJson(getProductByIDBean);
                    LogUtil.e("getProductByIDStr", getProductByIDStr);
                    setView(getProductByIDBean.getReturnData());
                } else if (getProductByIDBean.getErrorCode() == 402) {
                    //                    goLogin();
                } else {
                    CustomToast.showToast(getProductByIDBean.getErrorMsg());
                }

                return null;
            }
        });

        HttpManager.getInstance().doHttpDeal(this, getProductByIDApi);

    }

    private void setView(GetProductByIDBean.ReturnDataBean returnData) {
        pictureList = returnData.getPictureList();
        videoList = returnData.getVideoList();
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
            }
            completedVideoNumber = videoTempList.size();
        }
        if (pictureList != null && productID != 0) {
            for (int i = 0; i < pictureList.size(); i++) {
                PictureBean pictureBean = new PictureBean();
                pictureBean.setProductDetailID(pictureList.get(i).getProductDetailID());
                pictureBean.setS3Key(pictureList.get(i).getS3Key());
                pictureBean.setUrl(pictureList.get(i).getUrl());
                pictureTempList.add(pictureBean);
            }
            completedPictureNumber = pictureTempList.size();
        }

        productTagList = returnData.getProductTagList();
        List<Integer> productTagSelectList = Lists.newArrayList();

        if (pictureList != null && pictureList.size() != 0) {
            tv_pictureWork.setText(getString(R.string.contribute_pictureWork) + "(" + pictureList.size() + ")");
            tv_pictureWork.setTextColor(getResources().getColor(R.color.white));
            tv_onUpload_picture.setVisibility(View.INVISIBLE);
            iv1.setBackgroundResource(R.mipmap.upload_next_white);
            rl_pictureWork.setBackgroundResource(R.drawable.shape_pink_bg_5dp);
            uploadPro.setMax(pictureList.size());
            uploadPro.setProgress(pictureList.size());
        }

        if (videoList != null&&videoList.size()!=0) {
            tv_videoWork.setText(getString(R.string.contribute_videoWork) + "(" + videoList.size() + ")");
            tv_videoWork.setTextColor(getResources().getColor(R.color.white));
            tv_onUpload_video.setVisibility(View.INVISIBLE);
            iv2.setBackgroundResource(R.mipmap.upload_next_white);
            rl_videoWork.setBackgroundResource(R.drawable.shape_pink_bg_5dp);
            videoUploadPro.setMax(videoList.size());
            videoUploadPro.setProgress(videoList.size());
        }

        Glide.with(this).load(returnData.getCoverPicUrl()).into(iv_cover);
        coverPicUrl = returnData.getCoverPicUrl();
        et_title.setText(returnData.getTitle());
        et_description.setText(returnData.getDescription());

        for (int i = 0; i < productTagList.size(); i++) {
            for (int j = 0; j < labels.size(); j++) {
                if (productTagList.get(i).getTagName().equals(labels.get(j))) {
                    productTagSelectList.add(j);
                }
            }
        }
        labelsView.setSelects(productTagSelectList);

        if (returnData.isIsNeedPay()) {
            cb_charge.setChecked(true);
            et_charge.setEnabled(true);
            et_charge.setText(returnData.getACoinPrice() + "");
        } else {
            cb_free.setChecked(true);
        }

    }


    /**
     * 刷新图片上传状态
     */
    private void refreshPictureUploadState() {
        pictureObservers.clear();
        isPictureItemUploadState = true;

        List<TransferObserver> observerList = transferUtility.getTransfersWithType(TransferType.UPLOAD);
        for (int i = 0; i < observerList.size(); i++) {
            if (observerList.get(i).getAbsoluteFilePath().contains(".jpg")
                    || observerList.get(i).getAbsoluteFilePath().contains(".jpeg")
                    || observerList.get(i).getAbsoluteFilePath().contains(".png")
                    || observerList.get(i).getAbsoluteFilePath().contains(".bmp")
                    || observerList.get(i).getAbsoluteFilePath().contains(",gif")) {
                pictureObservers.add(observerList.get(i));
            }
        }
        completedPictureNumber = pictureTempList.size() - pictureObservers.size();
        uploadPro.setMax(pictureTempList.size());
        if (pictureTempList.size() == 0) {  //无任务
            tv_pictureWork.setText(R.string.contribute_pictureWork_not_added);
            tv_pictureWork.setTextColor(getResources().getColor(R.color.black_333));
            tv_onUpload_picture.setVisibility(View.INVISIBLE);
            iv1.setBackgroundResource(R.mipmap.upload_next_gray);
            rl_pictureWork.setBackgroundResource(R.drawable.shape_gray_eee_5dp_bg);

        } else {
            for (int i = 0; i < pictureObservers.size(); i++) {
                if (Constants.UploadState.COMPLETED.equals(pictureObservers.get(i).getState() + "")) {
                    completedPictureNumber++;
                } else if (Constants.UploadState.FAILED.equals(pictureObservers.get(i).getState() + "")) {
                    isPictureItemUploadState = false;
                }
            }
            uploadPro.setProgress(completedPictureNumber);

            if (completedPictureNumber == pictureTempList.size()) { //完成
                tv_pictureWork.setText(getString(R.string.contribute_pictureWork) + "(" + pictureTempList.size() + ")");
                tv_pictureWork.setTextColor(getResources().getColor(R.color.white));
                tv_onUpload_picture.setVisibility(View.INVISIBLE);
                iv1.setBackgroundResource(R.mipmap.upload_next_white);
                rl_pictureWork.setBackgroundResource(R.drawable.shape_pink_bg_5dp);
            } else {
                //上传中
                if (isPictureItemUploadState) {
                    tv_onUpload_picture.setText(R.string.upload_in_progress);
                } else {
                    tv_onUpload_picture.setText(R.string.upload_failed_hint);
                }
                tv_pictureWork.setText(getString(R.string.contribute_pictureWork) + "(" + completedPictureNumber + "/" + pictureTempList.size() + ")");
                tv_pictureWork.setTextColor(getResources().getColor(R.color.black_666));
                tv_onUpload_picture.setVisibility(View.VISIBLE);
                iv1.setBackgroundResource(R.mipmap.upload_next_gray);
                rl_pictureWork.setBackgroundResource(R.drawable.shape_gray_eee_5dp_bg);
            }
        }

    }


    /**
     * 刷新视频上传状态
     */
    private void refreshVideoUploadState(List<VideoBean> videoTempList) {
        videoObservers.clear();
        isVideoItemUploadState = true;

        List<TransferObserver> observerList = transferUtility.getTransfersWithType(TransferType.UPLOAD);
        for (int i = 0; i < observerList.size(); i++) {
            if (observerList.get(i).getAbsoluteFilePath().contains(".mp4")) {
                videoObservers.add(observerList.get(i));
            }
        }
        completedVideoNumber = videoTempList.size() - videoObservers.size();
        videoUploadPro.setMax(videoTempList.size());
        if (videoTempList.size() == 0) {
            //无任务
            tv_videoWork.setText(R.string.contribute_videoWork_not_added);
            tv_videoWork.setTextColor(getResources().getColor(R.color.black_333));
            tv_onUpload_video.setVisibility(View.INVISIBLE);
            iv2.setBackgroundResource(R.mipmap.upload_next_gray);
            rl_videoWork.setBackgroundResource(R.drawable.shape_gray_eee_5dp_bg);

        } else {
            for (int i = 0; i < videoTempList.size(); i++) {
                if (videoTempList.get(i).getTransferObserver() != null) {
                    if (Constants.UploadState.COMPLETED.equals(videoTempList.get(i).getTransferObserver().getState() + "")) {
                        completedVideoNumber++;
                    } else if (Constants.UploadState.FAILED.equals(videoTempList.get(i).getTransferObserver().getState() + "")) {
                        isVideoItemUploadState = false;
                    }
                }
                videoUploadPro.setProgress(completedVideoNumber);
                if (completedVideoNumber == videoTempList.size()) { //完成
                    tv_videoWork.setText(getString(R.string.contribute_videoWork) + "(" + videoTempList.size() + ")");
                    tv_videoWork.setTextColor(getResources().getColor(R.color.white));
                    tv_onUpload_video.setVisibility(View.INVISIBLE);
                    iv2.setBackgroundResource(R.mipmap.upload_next_white);
                    rl_videoWork.setBackgroundResource(R.drawable.shape_pink_bg_5dp);
                } else {
                    if (isVideoItemUploadState) {  //上传中
                        tv_onUpload_video.setText(R.string.upload_in_progress);
                    } else {//上传失败
                        tv_onUpload_video.setText(R.string.upload_failed_hint);
                    }
                    tv_videoWork.setText(getString(R.string.contribute_videoWork) + "(" + completedVideoNumber + "/" + videoTempList.size() + ")");
                    tv_videoWork.setTextColor(getResources().getColor(R.color.black_666));
                    tv_onUpload_video.setVisibility(View.VISIBLE);
                    iv2.setBackgroundResource(R.mipmap.upload_next_gray);
                    rl_videoWork.setBackgroundResource(R.drawable.shape_gray_eee_5dp_bg);
                }
            }
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                exit();
                break;
            case R.id.tv_right:
                CustomToast.showToast("存草稿");
                break;
            case R.id.rl_pictureWork:  //图片作品
                Intent pictureIntent = new Intent(ContributeActivity.this, PictureWorkActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("pictureList", (Serializable) pictureList);
                bundle.putInt("productID", productID);
                pictureIntent.putExtras(bundle);
                startActivity(pictureIntent);
                break;
            case R.id.rl_videoWork:  //视频作品
                Intent videoIntent = new Intent(ContributeActivity.this, VideoWorkActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("videoList", (Serializable) videoList);
                videoIntent.putExtras(b);
                startActivity(videoIntent);

                break;
            case R.id.iv_cover:  //设置封面
                if (observer == null) {
                    ImageSelector.builder()
                            .useCamera(false) // 设置是否使用拍照
                            .setCrop(true)  // 设置是否使用图片剪切功能。
                            .setSingle(true)  //设置是否单选
                            .setViewImage(true) //是否点击放大图片查看,，默认为true
                            .start(this, REQUEST_CODE); // 打开相册
                } else {
                    if (Constants.UploadState.FAILED.equals(observer.getState() + "")) {
                        transferUtility.resume(observer.getId());
                        observer.setTransferListener(new UploadListener());
                    } else {
                        ImageSelector.builder()
                                .useCamera(false) // 设置是否使用拍照
                                .setCrop(true)  // 设置是否使用图片剪切功能。
                                .setSingle(true)  //设置是否单选
                                .setViewImage(true) //是否点击放大图片查看,，默认为true
                                .start(this, REQUEST_CODE); // 打开相册
                    }
                }
                break;
            case R.id.ib_question:  //问题
                new RegisterSuccessDialog(ContributeActivity.this, R.layout.dialog_about_price, null);
                break;
            case R.id.btn_apply:  //提交申请
                submitApplications();
                break;
        }
    }


    @Override
    public void click(int position) {
        if (position == 1) {  //详情请戳
            H5LinkUrlManager.getInstances().getH5LinkUrl(ContributeActivity.this, 7);
        }
    }

    private void getHotTag() {
        SearchHotTagApi searchHotTagApi = new SearchHotTagApi(this);
        searchHotTagApi.setPageIndex(1);
        searchHotTagApi.setPageSize(100);
        searchHotTagApi.setListener(new HttpOnNextListener<HotTagBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(HotTagBean hotTagBean) {
                if (hotTagBean.getErrorCode() == 200) {
                    hotTagList = hotTagBean.getReturnData().getList();
                    SPUtil.saveObject(ContributeActivity.this, Constants.HOT_TAG, hotTagList);
                    for (int i = 0; i < hotTagList.size(); i++) {
                        labels.add(hotTagList.get(i).getTagName());
                    }
                    labelsView.setLabels(labels);

                } else {
                    hotTagList = (List<HotTagBean.ReturnDataBean.ListBean>) SPUtil.readObject(ContributeActivity.this, Constants.HOT_TAG);
                }
                return null;
            }

            @Override
            public RetryWhenNetworkException.Wrapper onError(Throwable e) {
                hotTagList = (List<HotTagBean.ReturnDataBean.ListBean>) SPUtil.readObject(ContributeActivity.this, Constants.HOT_TAG);
                return super.onError(e);
            }
        });
        HttpManager.getInstance().doHttpDeal(this, searchHotTagApi);
    }


    private void exit() {
        new BottomDialogUtil(ContributeActivity.this,
                R.layout.dialog_bottom,
                getString(R.string.dialog_certification_title),
                getString(R.string.dialog_certification_confrim),
                getString(R.string.dialog_certification_cancel),
                new BottomDialogUtil.BottonDialogListener() {
                    @Override
                    public void onItemListener() {
                        List<TransferObserver> observers = transferUtility.getTransfersWithType(TransferType.UPLOAD);
                        for (TransferObserver observer : observers) {
                            transferUtility.deleteTransferRecord(observer.getId());
                        }
                        SPUtil.saveObject(ContributeActivity.this, Constants.LOCAL_MEDIA_VIDEO, null);
                        finish();
                    }
                });
    }


    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        // 设置布局管理器
        rv_label.setLayoutManager(linearLayoutManager);
        LabelAdapter labelAdapter = new LabelAdapter(ContributeActivity.this);
        // 设置adapter
        rv_label.setAdapter(labelAdapter);

        labelAdapter.setOnItemClickListener(new LabelAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });

    }

    public void getUrl(TransferObserver observer) {
        urlRequest = new GeneratePresignedUrlRequest(AccountManager.getInstances().getAmazonS3Config().getBucketName(), observer.getKey());
        URL url = s3.generatePresignedUrl(urlRequest);
        coverPicUrl = url.toString();
        LogUtil.e("作品封面Url", url.toString());
        transferUtility.deleteTransferRecord(observer.getId());
        SPUtil.putString(ContributeActivity.this, Constants.COVER_IMAGE_ID, "");
    }


    public void upload(final String filePath) {
        String key = AWSUtil.getKey("images/cover/", ".jpg");
        transferUtility = AWSUtil.getTransferUtility(this);
        final File file = new File(filePath);
        observer = transferUtility.upload(AccountManager.getInstances().getAmazonS3Config().getBucketName(), key, file);
        observer.setTransferListener(new UploadListener());
        SPUtil.putString(ContributeActivity.this, Constants.COVER_IMAGE_ID, String.valueOf(observer.getId()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        coverPicUrl = "";
        // 图片选择结果回调（封面）
        if (requestCode == REQUEST_CODE && data != null) {
            ArrayList<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
            if (images.size() != 0) {
                LogUtil.e("图片单选返回", AppUtil.getGson().toJson(images.get(0)));
                Glide.with(ContributeActivity.this).load(images.get(0)).apply(options).into(iv_cover);
                upload(images.get(0));
            }
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

    class UploadListener implements TransferListener {
        @Override
        public void onError(int id, Exception e) {
            LogUtil.e(TAG, "Error during upload: " + id + e.toString());
            rl_uploadHint.setVisibility(View.GONE);
            tv_uploadHint.setText(R.string.upload_failed);
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            LogUtil.e(TAG, String.format("onProgressChanged: %d, total: %d, current: %d", id, bytesTotal, bytesCurrent));
            int progress = (int) ((double) bytesCurrent * 100 / bytesTotal);
            rl_uploadHint.setVisibility(View.VISIBLE);
            tv_uploadHint.setText(progress + "%");
        }

        @Override
        public void onStateChanged(int id, TransferState newState) {
            LogUtil.e(TAG, "onStateChanged: " + id + ", " + newState);
            if ("FAILED".equals(newState + "")) {
                rl_uploadHint.setVisibility(View.VISIBLE);
                tv_uploadHint.setText(R.string.upload_failed);
            } else if ("COMPLETED".equals(newState + "")) {
                rl_uploadHint.setVisibility(View.GONE);
                getUrl(observer);
            }
        }
    }

    /**
     * 提交申请 接口
     */
    private void submitApplications() {
        //========作品校验=====================
        if (pictureTempList.size() == 0 && videoTempList.size() == 0) { //没有作品
            CustomToast.showToast(getString(R.string.toast_commit_apply_hint1));
            return;
        } else if (pictureTempList.size() != 0 && videoTempList.size() != 0) { //有视频和图片
            if (completedPictureNumber != pictureTempList.size() || completedVideoNumber != videoTempList.size()) {
                CustomToast.showToast(getString(R.string.toast_commit_apply_hint3));
                return;
            } else if (pictureTempList.size() < 6) {
                CustomToast.showToast(getString(R.string.toast_commit_apply_hint2));
                return;
            }
        } else if (pictureTempList.size() != 0 && videoTempList.size() == 0) { //只有图片
            if (pictureTempList.size() < 6) { //图片小于6张
                CustomToast.showToast(getString(R.string.toast_commit_apply_hint2));
                return;
            } else {
                if (completedPictureNumber != pictureTempList.size()) {
                    CustomToast.showToast(getString(R.string.toast_commit_apply_hint3));
                    return;
                }
            }

        } else if (pictureTempList.size() == 0 && videoTempList.size() != 0) { //只有视频
            if (completedVideoNumber != videoTempList.size()) {
                CustomToast.showToast(getString(R.string.toast_commit_apply_hint3));
                return;
            }
        }

        //封面
        if (TextUtils.isEmpty(coverPicUrl)) {
            CustomToast.showToast(getString(R.string.toast_commit_apply_hint4));
            return;
        }

        //标题
        if (TextUtils.isEmpty(et_title.getText().toString().trim())) {
            CustomToast.showToast(getString(R.string.toast_commit_apply_hint5));
            return;
        }
        //标签
        if (selecLabelNumber == 0) {
            CustomToast.showToast(getString(R.string.toast_commit_apply_hint6));
            return;
        }
        //售价
        if (!cb_free.isChecked() && !cb_charge.isChecked()) {
            CustomToast.showToast(getString(R.string.toast_commit_apply_hint7));
            return;
        } else if (cb_charge.isChecked()) {
            if (TextUtils.isEmpty(et_charge.getText().toString().trim())) {
                CustomToast.showToast(getString(R.string.toast_commit_apply_hint7));
                return;
            } else if (Integer.parseInt(et_charge.getText().toString().trim()) < 100 || Integer.parseInt(et_charge.getText().toString().trim()) > 100000) {
                CustomToast.showToast(getString(R.string.toast_commit_apply_hint8));
                return;
            }
        }

        localMediaVideoList = (List<LocalMediaVideoBean>) SPUtil.readObject(ContributeActivity.this, Constants.LOCAL_MEDIA_VIDEO, null);
        new AsyncTaskBase().execute();

    }

    public class AsyncTaskBase extends AsyncTask<Object, String, String> {
        private String pictureOrVideoUrl = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (dialog == null) {
                dialog = DialogMaker.showCommenWaitDialog(ContributeActivity.this, "", false);
            }
        }

        @Override
        protected String doInBackground(Object... objects) {
            for (int i = 0; i < pictureTempList.size(); i++) {
                SaveProductVO.ProductDetailsBean productDetailsPictureBean = new SaveProductVO.ProductDetailsBean();
                if (pictureTempList.get(i).getTransferObserver() == null) {
                    urlRequest = new GeneratePresignedUrlRequest(bucketName, pictureTempList.get(i).getS3Key());
                } else {
                    urlRequest = new GeneratePresignedUrlRequest(bucketName, pictureTempList.get(i).getTransferObserver().getKey());
                }
                URL url = s3.generatePresignedUrl(urlRequest);
                pictureOrVideoUrl = url.toString();
                LogUtil.e("上传图片地址_" + i + "=", pictureOrVideoUrl);
                productDetailsPictureBean.setS3URL(pictureOrVideoUrl);
                productDetailsPictureBean.setProductDetailID(pictureTempList.get(i).getProductDetailID());
                productDetailsPictureBean.setProductID(productID);
                productDetailsPictureBean.setType(1);
                if (pictureTempList.get(i).getTransferObserver() == null) {
                    productDetailsPictureBean.setS3Key(pictureTempList.get(i).getS3Key());
                } else {
                    productDetailsPictureBean.setS3Key(pictureTempList.get(i).getTransferObserver().getKey());
                }
                productDetailsList.add(productDetailsPictureBean);
            }

            for (int i = 0; i < videoTempList.size(); i++) {
                SaveProductVO.ProductDetailsBean productDetailsVideoBean = new SaveProductVO.ProductDetailsBean();
                if (videoTempList.get(i).getTransferObserver() != null) {
                    urlRequest = new GeneratePresignedUrlRequest(bucketName, videoTempList.get(i).getTransferObserver().getKey());
                    productDetailsVideoBean.setS3Key(videoTempList.get(i).getTransferObserver().getKey());
                } else {
                    urlRequest = new GeneratePresignedUrlRequest(bucketName, videoTempList.get(i).getS3Key());
                    productDetailsVideoBean.setS3Key(videoTempList.get(i).getS3Key());
                }
                URL url = s3.generatePresignedUrl(urlRequest);
                pictureOrVideoUrl = url.toString();
                LogUtil.e("上传视频地址_" + i + "=", pictureOrVideoUrl);
                productDetailsVideoBean.setS3URL(pictureOrVideoUrl);
                productDetailsVideoBean.setProductDetailID(videoTempList.get(i).getProductDetailID());
                productDetailsVideoBean.setProductID(productID);
                productDetailsVideoBean.setType(2);
                productDetailsVideoBean.setVideoDuration(videoTempList.get(i).getVideoDuration());
                productDetailsVideoBean.setName(videoTempList.get(i).getName());
                productDetailsVideoBean.setSize(videoTempList.get(i).getSize());
                productDetailsList.add(productDetailsVideoBean);
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }

            //组装入参实体
            saveProductVO = new SaveProductVO();
            saveProductVO.setCoverPicUrl(coverPicUrl);
            saveProductVO.setTitle(et_title.getText().toString().trim());
            saveProductVO.setDescription(et_description.getText().toString().trim());
            if (cb_charge.isChecked()) {
                saveProductVO.setACoinPrice(Integer.parseInt(et_charge.getText().toString().trim()));
            }
            saveProductVO.setIsNeedPay(isNeedPay);
            saveProductVO.setTags(tagsList);
            saveProductVO.setProductDetails(productDetailsList);
            saveProductVO.setProductID(productID);
            String toJson = AppUtil.getGson().toJson(saveProductVO);
            LogUtil.e("投稿上传入参", toJson);

            saveProductApi(saveProductVO);
        }

    }

    /**
     * 提交作品接口
     *
     * @param saveProductVO
     */
    private void saveProductApi(SaveProductVO saveProductVO) {
        SaveProductApi saveProductApi = new SaveProductApi(this);
        saveProductApi.setSaveProductVO(saveProductVO);
        saveProductApi.setListener(new HttpOnNextListener<SaveProductBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(SaveProductBean saveProductBean) {
                if (saveProductBean.getErrorCode() == 200) {
                    startDDMActivity(SaveProducSuccessActivity.class, true);
                    List<TransferObserver> observers = transferUtility.getTransfersWithType(TransferType.UPLOAD);
                    for (TransferObserver observer : observers) {
                        transferUtility.deleteTransferRecord(observer.getId());
                    }
                    finish();
                } else {
                    CustomToast.showToast(saveProductBean.getErrorMsg());
                }
                return null;
            }
        });
        HttpManager.getInstance().doHttpDeal(this, saveProductApi);
    }
}
