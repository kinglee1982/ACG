package com.kcx.acg.views.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.api.GetAppUpgradeInfoApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.base.SysApplication;
import com.kcx.acg.bean.UpgradeInfoBean;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.manager.UpdateManager;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.ButtonUtils;
import com.kcx.acg.utils.CommonUtils;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.HomeDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.meituan.android.walle.WalleChannelReader;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * Created by zjb on 2018/11/12.
 */
public class AboutActivity extends BaseActivity implements CancelAdapt {

    int REQUEST_EXTERNAL_STORAGE = 1;
    String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private LinearLayout ll_back;
    private TextView tv_title, tv_versionMsg;
    private RelativeLayout rl_versionUpdate;
    private HomeDialog.Builder permissionBuilder;
    private UpgradeInfoBean.ReturnDataBean returnData;

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_about, null);
    }

    @Override
    public void initView() {
        super.initView();
        ll_back = findViewById(R.id.ll_back);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.setting_about) + AppUtil.getAppName(AboutActivity.this));
        tv_versionMsg = findViewById(R.id.tv_versionMsg);
        tv_versionMsg.setText(AppUtil.getAppName(AboutActivity.this) + CommonUtils.getVersionName(SysApplication.getContext()));
        rl_versionUpdate = findViewById(R.id.rl_versionUpdate);
        permissionBuilder = new HomeDialog.Builder(this);
        permissionBuilder.setLayoutId(R.layout.dialog_premission_layout);
    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);
        rl_versionUpdate.setOnClickListener(this);
        permissionBuilder.setOnClickListener(new HomeDialog.Builder.OnClickListener() {
            @Override
            public void onConfrim() {
                ActivityCompat.requestPermissions(
                        AboutActivity.this,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                );
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;

            case R.id.rl_versionUpdate: //检查版本更新
                if (!ButtonUtils.isFastDoubleClick(R.id.rl_versionUpdate)) {
                    getAppUpgradeInfoApi();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                UpdateManager updateManager = new UpdateManager();
                updateManager.showDialog(AboutActivity.this, returnData, null);
            } else {
                permissionBuilder.builder().show();
            }
        }
    }

    public void getAppUpgradeInfoApi() {
        GetAppUpgradeInfoApi getAppUpgradeInfoApi = new GetAppUpgradeInfoApi(AboutActivity.this);
        getAppUpgradeInfoApi.setListener(new HttpOnNextListener<UpgradeInfoBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(UpgradeInfoBean upgradeInfoBean) {
                if (upgradeInfoBean.getErrorCode() == 200) {
                    String versionName = CommonUtils.getVersionName(AboutActivity.this);
                    LogUtil.e("versionName", versionName);
                    returnData = upgradeInfoBean.getReturnData();
                    int compareTo = returnData.getAppVersion().compareTo(versionName);
                    LogUtil.e("compareTo", compareTo + "///");
                    if (!TextUtils.isEmpty(returnData.getAppVersion()) && !TextUtils.isEmpty(versionName) && !TextUtils.isEmpty(returnData.getDownLoadUrl())) {
                        if (returnData.getAppVersion().compareTo(versionName) > 0) {
                            int permission = ActivityCompat.checkSelfPermission(AboutActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if (permission != PackageManager.PERMISSION_GRANTED) {
                                permissionBuilder.builder().show();
                            } else {
                                UpdateManager updateManager = new UpdateManager();
                                updateManager.showDialog(AboutActivity.this, returnData, null);
                            }
                        } else {
                            CustomToast.showToast(getString(R.string.setting_about_versionUpdate_hint));
                        }
                    }
                }

                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(AboutActivity.this, getAppUpgradeInfoApi);
    }


}
