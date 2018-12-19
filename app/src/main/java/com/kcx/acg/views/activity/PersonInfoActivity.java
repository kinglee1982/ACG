package com.kcx.acg.views.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kcx.acg.R;
import com.kcx.acg.api.UpUserPhotoApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.bean.UpUserPhotoBean;
import com.kcx.acg.bean.UserInfoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.impl.SubmitOnClickListenner;
import com.kcx.acg.permission.PermissionListener;
import com.kcx.acg.permission.PermissionUtil;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.ImageUtil;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.utils.MessageDialog;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.utils.StringUtil;
import com.kcx.acg.views.view.CustomToast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 编辑个人信息
 * Created by jb on 2018/9/4.
 */
public class PersonInfoActivity extends BaseActivity implements CancelAdapt {
    public static final int SELECT_PHOTO = 10;
    public static final int REQUESTCODE_PHOTO_ALBUM = 3;
    private LinearLayout ll_back;
    private TextView tv_title, tv_nickname, tv_phone,tv_areaCode;
    private RelativeLayout rl_touxiang, rl_nickname;
    private String nickname, phone, photo;
    private String imagePath;
    private ImageView iv_touxiang;
    private RequestOptions options;
    private UserInfoBean userInfoBean;
    private PermissionUtil permissionUtil;
    private List<String> imageList = null;
    private List<String> mSelectedImageList = null;

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_compile_person_info, null);
    }

    @Override
    public void initView() {
        super.initView();
        userInfoBean = AppUtil.getGson().fromJson(SPUtil.getString(PersonInfoActivity.this, Constants.USER_INFO, ""), UserInfoBean.class);
        ll_back = findViewById(R.id.ll_back);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.personInfo_title);

        rl_touxiang = findViewById(R.id.rl_touxiang);
        rl_nickname = findViewById(R.id.rl_nickname);
        tv_nickname = findViewById(R.id.tv_nickname);
        tv_phone = findViewById(R.id.tv_phone);
        tv_areaCode = findViewById(R.id.tv_areaCode);

        iv_touxiang = findViewById(R.id.iv_touxiang);

        permissionUtil = new PermissionUtil(PersonInfoActivity.this);
    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);
        rl_nickname.setOnClickListener(this);
        rl_touxiang.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        if (imageList == null) {
            imageList = new ArrayList<>();
        }
        if (mSelectedImageList == null) {
            mSelectedImageList = new ArrayList<>();
        }


        options = new RequestOptions()
                .placeholder(R.mipmap.placehold_head)  //预加载图片
                .error(R.mipmap.placehold_head);
        if (userInfoBean !=null){
            tv_areaCode.setText("+"+userInfoBean.getReturnData().getAreaCode());
            phone = userInfoBean.getReturnData().getPhone();
            nickname = userInfoBean.getReturnData().getUserName();
            photo = userInfoBean.getReturnData().getPhoto();
        }

        tv_phone.setText(this.phone);
        tv_nickname.setText(nickname);
        Glide.with(this).load(photo).apply(options).into(iv_touxiang);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                setResultData();
                break;
            case R.id.rl_touxiang: //头像
                //                selectPhoto();
                permissionUtil.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
                    @Override
                    public void onGranted() {
                        imageList.clear();
                        mSelectedImageList.clear();
                        Intent intent = new Intent(PersonInfoActivity.this, SampleCameraActivity.class);
                        startActivityForResult(intent, REQUESTCODE_PHOTO_ALBUM);
                    }

                    @Override
                    public void onDenied(List<String> deniedPermission) {

                    }

                    @Override
                    public void onShouldShowRationale(List<String> deniedPermission) {
                        openSettingActivity();
                    }
                });


                break;
            case R.id.rl_nickname: //昵称
                Intent i = new Intent(PersonInfoActivity.this, AlertNicknameActivity.class);
                i.putExtra("nickname", nickname);
                startActivityForResult(i, 10086);
                break;
        }
    }

    private void setResultData() {
        Intent intent = new Intent();
        intent.putExtra("nickname", nickname);
        intent.putExtra("imagePath", imagePath);
        setResult(101, intent);
        finish();
    }

    /**
     * 从相册中获取图片
     */
    private void selectPhoto() {
        new PermissionUtil(PersonInfoActivity.this).requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
            @Override
            public void onGranted() {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_PHOTO);
            }

            @Override
            public void onDenied(List<String> deniedPermission) {

            }

            @Override
            public void onShouldShowRationale(List<String> deniedPermission) {
                openSettingActivity();
            }
        });
    }

    private void openSettingActivity() {
        MessageDialog.init()
                .setTitle(getString(R.string.personInfo_hint1))
                .setMsg(getString(R.string.personInfo_hint2))
                .setSubmitStr(getString(R.string.personInfo_hint3))
                .setExitStr(getString(R.string.personInfo_hint4))
                .setSubmitOnClickListenner(new SubmitOnClickListenner() {
                    @Override
                    public void onSubmitOnClick() {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                }).show(getSupportFragmentManager());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (resultCode) {
                case 10010:  //修改昵称返回
                    nickname = data.getStringExtra("strData");
                    tv_nickname.setText(nickname);
                    break;
                case RESULT_OK: //相册选择返回
                    if (requestCode == SELECT_PHOTO) {
                        Bitmap bitmap = null;
                        //判断手机系统版本号
                        if (Build.VERSION.SDK_INT > 19) {
                            //4.4及以上系统使用这个方法处理图片
                            imagePath = ImageUtil.handleImgeOnKitKat(this, data);
                            if (imagePath == null) { //适配meizu MX5
                                imagePath = ImageUtil.handleImageForMeizu(this, data);
                            }

                        } else {
                            imagePath = ImageUtil.handleImageBeforeKitKat(this, data);
                        }
                        LogUtil.e("相册返回", "imagePath=" + imagePath);
                        //imagePath=/storage/emulated/0/Pictures/IMG_20180416_142541.jpg
                        //   imagePath=/storage/emulated/0/相机/IMG_20180427_155315.jpg

                        boolean compress = StringUtil.isCompress(imagePath, 512);
                        LogUtil.e("compress", "compress=" + compress);
                        //图片压缩
                        if (compress) {
                            bitmap = ImageUtil.compressPixel(imagePath, 5);
                        } else {
                            bitmap = ImageUtil.compressPixel(imagePath, 1);
                        }

                        UpUserPhoto(bitmap);
                    }
                    break;

                case 10086: //头像裁剪返回
                    byte[] b = data.getByteArrayExtra("bitmap");
                    Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                    LogUtil.e("bitmap.size","=="+ImageUtil.getBitmapSize(bitmap));
                    //上传头像到服务器
                    UpUserPhoto(bitmap);
                    break;
            }
        }
    }

    /**
     * 上传头像
     *
     * @param bitmap
     */
    private void UpUserPhoto(final Bitmap bitmap) {
        UpUserPhotoApi upUserPhotoApi = new UpUserPhotoApi(this, new HttpOnNextListener<UpUserPhotoBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(UpUserPhotoBean upUserPhotoBean) {
                CustomToast.showToast(upUserPhotoBean.getErrorMsg());
                if (upUserPhotoBean.getErrorCode() == 200) {
                    //上传成功
                    Glide.with(PersonInfoActivity.this).load(upUserPhotoBean.getReturnData().getPhotoRoute()).apply(options).into(iv_touxiang);
                }else if (upUserPhotoBean.getErrorCode() == 402){
                    //token失效
//                    startDDMActivity(LoginActivity.class,true);
                }

                return null;
            }
        });
        String imgPath = ImageUtil.saveBitmap(PersonInfoActivity.this, bitmap,"touxiang");
        LogUtil.e("imgPath",imgPath);
        File file = new File(imgPath);
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part imageBodyPart = MultipartBody.Part.createFormData("imgfile", file.getName(), imageBody);
        upUserPhotoApi.setPart(imageBodyPart);
        HttpManager.getInstance().doHttpDeal(PersonInfoActivity.this, upUserPhotoApi);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResultData();
            return true;
        }
        return false;
    }


}
