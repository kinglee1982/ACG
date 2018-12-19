package com.kcx.acg.views.activity;

/**
 * Created by jb.
 * 照片选择界面
 */

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.utils.ImageFloder;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.views.adapter.MyAdapter;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.ListImageDirPopupWindow;
import com.kcx.acg.views.view.PhotoChoseDialog;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;

public class SampleCameraActivity extends BaseActivity implements CancelAdapt {
    int totalCount = 0;
//    private ProgressDialog mProgressDialog;
    /**
     * 存储文件夹中的图片数量
     */
    private int mPicsSize;
    /**
     * 图片数量最多的文件夹
     */
    private File mImgDir;
    /**
     * 所有的图片
     */
    private List<String> mImgs;
    private GridView mGirdView;
    private MyAdapter mAdapter;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<String>();
    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();
    private RelativeLayout mBottomLy;
    private TextView mChooseDir;
    private TextView mImageCount;
    private int mScreenHeight;
    private ListImageDirPopupWindow mListImageDirPopupWindow;
    private TextView tv_samplecamera_confirm;
    private TextView tv_return;
    private RelativeLayout rl_exit;
    private TextView head_title;
    private ArrayList<String> mSelectedImageList = new ArrayList<String>();

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
//            mProgressDialog.dismiss();
            // 为View绑定数据
            data2View();

        }
    };

    /**
     * 为View绑定数据
     */
    private void data2View() {
        if (mImgDir == null) {
            CustomToast.showToast( getString(R.string.headPortraits_noImagve));
            return;
        }
        mImgs = Arrays.asList(mImgDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg")) {
                    return true;
                } else {
                    return false;
                }
            }
        }));
        //        mImgs = Arrays.asList(mImgDir.list());
        LogUtil.e("mImgs", mImgs.size() + "");
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        mAdapter = new MyAdapter(SampleCameraActivity.this, mImgs,
                R.layout.view_cameragrid_item, mImgDir.getAbsolutePath());
        mAdapter.setIamgeNull();
        mGirdView.setAdapter(mAdapter);
        mImageCount.setText(totalCount +getString(R.string.headPortraits_one));

        mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String imgPath) {
                Intent intent = new Intent(SampleCameraActivity.this, CropActivity.class);
                intent.putExtra("imgPath",imgPath);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_sample_camera, null);
    }
    @Override
    public void setTatusBar() {
        super.setTatusBar();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    @Override
    public void initView() {
        super.initView();
        rl_exit = findViewById(R.id.rl_exit);
        mGirdView = (GridView) findViewById(R.id.id_gridView);
        mChooseDir = (TextView) findViewById(R.id.id_choose_dir);
        mImageCount = (TextView) findViewById(R.id.id_total_count);
        mBottomLy = (RelativeLayout) findViewById(R.id.bottom_ly);
        rl_exit.setOnClickListener(this);
        mBottomLy.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        getImages();
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            CustomToast.showToast( getString(R.string.headPortraits_idCardStr));
            return;
        }
        // 显示进度条
//        mProgressDialog = ProgressDialog.show(this, null, getString(R.string.headPortraits_progressStr));
        new Thread(new Runnable() {
            @Override
            public void run() {
                String firstImage = null;
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = SampleCameraActivity.this.getContentResolver();
                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);
                if (mCursor == null) {
                    return;
                }
                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    LogUtil.e("图片路径", path + "                             ///");
                    // 拿到第一张图片的路径
                    if (firstImage == null)
                        firstImage = path;
                    // 获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;
                    String dirPath = parentFile.getAbsolutePath();
                    LogUtil.e("dirPath", "dirPath=" + dirPath);

                    ImageFloder imageFloder = null;
                    // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        // 初始化imageFloder
                        imageFloder = new ImageFloder();
                        imageFloder.setDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                    }
                    int picSize = 0;
                    try {
                        picSize = parentFile.list(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String filename) {
                                LogUtil.e("filename", "filename=" + filename);
                                if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg")) {
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        }).length;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    LogUtil.e("picSize", "picSize=" + picSize);
                    totalCount += picSize;
                    imageFloder.setCount(picSize);
                    mImageFloders.add(imageFloder);
                    if (picSize > mPicsSize) {
                        mPicsSize = picSize;
                        mImgDir = parentFile;
                    }

                }
                mCursor.close();
                // 扫描完成，辅助的HashSet也就可以释放内存了
                mDirPaths = null;
                // 通知Handler扫描图片完成
                mHandler.sendEmptyMessage(0x110);

            }
        }).start();
    }


    public void updata(ImageFloder floder) {
        mImgDir = new File(floder.getDir());
        mImgs = Arrays.asList(mImgDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg") || filename.endsWith(".png")
                        || filename.endsWith(".jpeg"))
                    return true;
                return false;
            }
        }));
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        mAdapter = new MyAdapter(getApplicationContext(), mImgs,
                R.layout.view_cameragrid_item, mImgDir.getAbsolutePath());
        mGirdView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mImageCount.setText(floder.getCount() + "张");
        mChooseDir.setText(floder.getName());
        mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String imgPath) {
                Intent intent = new Intent(SampleCameraActivity.this, CropActivity.class);
                intent.putExtra("imgPath",imgPath);
                startActivityForResult(intent,0);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_exit:
                finish();
                break;

            case R.id.bottom_ly:  //选择相册
                new PhotoChoseDialog(SampleCameraActivity.this, R.layout.view_cameralist_dir, 0.75, mImageFloders, new PhotoChoseDialog.BottonDialogListener() {
                    @Override
                    public void selected(ImageFloder floder) {
                        updata(floder);
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10086){ //头像裁剪返回
            byte[] b = data.getByteArrayExtra("bitmap");
            Intent intent = new Intent();
            intent.putExtra("bitmap", b);
            setResult(10086,intent);
            finish();
        }
    }
}
