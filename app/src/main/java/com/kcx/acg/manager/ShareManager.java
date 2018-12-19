package com.kcx.acg.manager;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.kcx.acg.api.GetH5ShareLinkUrlApi;
import com.kcx.acg.api.ShareSuccessApi;
import com.kcx.acg.api.ShareSuccessV1_2Api;
import com.kcx.acg.base.BusEvent;
import com.kcx.acg.bean.GetH5ShareLinkUrlBean;
import com.kcx.acg.bean.ShareSuccessBean;
import com.kcx.acg.bean.ShareSuccessV1_2Bean;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * Created by  o on 2018/10/22.
 */

public class ShareManager {

    private Bitmap bitmap;
    private GetH5ShareLinkUrlBean H5ShareLinkUrlBean;
    private Context context;

    private ShareManager() {

    }

    public static ShareManager getInstances() {
        synchronized (ShareManager.class) {
            return new ShareManager();
        }
    }

    /**
     * 获取分享的链接
     *
     * @param type    1:分享作品 | 2:分享标签 | 3:分享用户 | 4:分享APP
     * @param shareID 根据分享的类型传不同的值, 作品传作品ID,标签传标签ID...
     * @param context
     */
    public void share(int type, int shareID, Context context) {
        this.context = context;
        getH5ShareLinkUrl(type, shareID, context);
        shareSuccessV1_2Api(type, shareID);
//        int REQUEST_EXTERNAL_STORAGE = 1;
//        String[] PERMISSIONS_STORAGE = {
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//        };
//        int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(
//                    (Activity) context,
//                    PERMISSIONS_STORAGE,
//                    REQUEST_EXTERNAL_STORAGE
//            );
//        } else {
//            getH5ShareLinkUrl(type, shareID, context);
//
//        }
    }

    public void shareSuccess(Context context){
        ShareSuccessApi shareSuccessApi = new ShareSuccessApi((RxAppCompatActivity) context);
        shareSuccessApi.setListener(new HttpOnNextListener<ShareSuccessBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(ShareSuccessBean shareSuccessBean) {
                if(shareSuccessBean.getErrorCode() == 200){
                    EventBus.getDefault().post(new BusEvent(BusEvent.SHARE, true));
                }
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(context, shareSuccessApi);
    }

    public void shareSuccessV1_2Api(int type, int shareID){
        ShareSuccessV1_2Api shareSuccessV1_2Api = new ShareSuccessV1_2Api((RxAppCompatActivity) context);
        shareSuccessV1_2Api.setType(type);
        shareSuccessV1_2Api.setShareID(shareID);
        shareSuccessV1_2Api.setListener(new HttpOnNextListener<ShareSuccessV1_2Bean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(ShareSuccessV1_2Bean shareSuccessV1_2Bean) {
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(context, shareSuccessV1_2Api);
    }

    private void getH5ShareLinkUrl(int type, int shareID, final Context context) {
        GetH5ShareLinkUrlApi getH5ShareLinkUrlApi = new GetH5ShareLinkUrlApi((RxAppCompatActivity) context);
        getH5ShareLinkUrlApi.setType(type);
        getH5ShareLinkUrlApi.setShareID(shareID);
        getH5ShareLinkUrlApi.setListener(new HttpOnNextListener<GetH5ShareLinkUrlBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(final GetH5ShareLinkUrlBean getH5ShareLinkUrlBean) {
                if (getH5ShareLinkUrlBean.getErrorCode() == 200) {
////                    new Task().execute(getH5ShareLinkUrlBean.getReturnData().getImgUrl());
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Bitmap bitmap = null;
//                            try {
//                                bitmap = BitmapFactory.decodeStream(new URL(getH5ShareLinkUrlBean.getReturnData().getImgUrl()).openStream());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            GetH5ShareLinkUrlBean.ReturnDataBean bean = getH5ShareLinkUrlBean.getReturnData();
//                            Uri u = saveBitmap(bitmap);
////                                shareImg(bean.getTitle(),bean.getTitle(),bean.getContent()+bean.getLink(),u);
//                            shareMsg(bean.getTitle(), bean.getTitle(), bean.getContent() + ";;;" + bean.getLink(), u);
////                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
////                    if (f != null && f.exists() && f.isFile()) {
////                    }+
////                                shareIntent.setType("image/*");
////                                shareIntent.putExtra(Intent.EXTRA_STREAM, u);
////
//////                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(getH5ShareLinkUrlBean.getReturnData().getImgUrl()));
//////                    shareIntent.setType("image/*");
////                                //当用户选择短信时使用sms_body取得文字
////                                shareIntent.putExtra("sms_body", getH5ShareLinkUrlBean.getReturnData().getContent() + getH5ShareLinkUrlBean.getReturnData().getLink());
////                                shareIntent.setType("text/plain");
////                                shareIntent.putExtra(Intent.EXTRA_TEXT, getH5ShareLinkUrlBean.getReturnData().getContent() + getH5ShareLinkUrlBean.getReturnData().getLink());
////                                startActivity(Intent.createChooser(shareIntent, getH5ShareLinkUrlBean.getReturnData().getTitle()));
//
//
//                        }
//                    }).start();
//
//                    H5ShareLinkUrlBean = getH5ShareLinkUrlBean;
//                    File f = new File(getH5ShareLinkUrlBean.getReturnData().getImgUrl());
//                    if (f != null && f.exists() && f.isFile()) {
//                    shareIntent.setType("image/*");
//                    Uri u = Uri.fromFile(f);
//                    shareIntent.putExtra(Intent.EXTRA_STREAM, u);
//                    }
//
//                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(getH5ShareLinkUrlBean.getReturnData().getImgUrl()));
//                    shareIntent.setType("image/*");

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.putExtra("sms_body", getH5ShareLinkUrlBean.getReturnData().getContent() + getH5ShareLinkUrlBean.getReturnData().getLink());
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, getH5ShareLinkUrlBean.getReturnData().getTitle()+"\n"+
                            getH5ShareLinkUrlBean.getReturnData().getContent() +"\n"+
                            getH5ShareLinkUrlBean.getReturnData().getLink());
                    startActivity(Intent.createChooser(shareIntent, getH5ShareLinkUrlBean.getReturnData().getTitle()));
                }
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(context, getH5ShareLinkUrlApi);
    }

    private void shareImg(String dlgTitle, String subject, String content,
                          Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        if (subject != null && !"".equals(subject)) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        if (content != null && !"".equals(content)) {
            intent.putExtra(Intent.EXTRA_TEXT, content);
        }

        // 设置弹出框标题
        if (dlgTitle != null && !"".equals(dlgTitle)) { // 自定义标题
            startActivity(Intent.createChooser(intent, dlgTitle));
        } else { // 系统默认标题
            startActivity(intent);
        }
    }

    /**
     * 将图片存到本地
     */
    private static Uri saveBitmap(Bitmap bm) {
        try {
            String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/renji/" + System.currentTimeMillis() + ".jpg";
            File f = new File(dir);
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Uri uri = Uri.fromFile(f);
            return uri;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从Assets中读取图片
     */
    private Bitmap getImageFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }


    /**
     * 获取网络图片
     *
     * @param imageurl 图片网络地址
     * @return Bitmap 返回位图
     */
    public Bitmap GetImageInputStream(String imageurl) {
        URL url;
        HttpURLConnection connection = null;
        Bitmap bitmap = null;
        try {
            url = new URL(imageurl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(6000); //超时设置
            connection.setDoInput(true);
            connection.setUseCaches(false); //设置不使用缓存
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0x123) {
                savaImage(bitmap, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
            }
        }
    };

    /**
     * 异步线程下载图片
     */
    class Task extends AsyncTask<String, Integer, Void> {

        protected Void doInBackground(String... params) {
            bitmap = GetImageInputStream((String) params[0]);
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Message message = new Message();
            message.what = 0x123;
            handler.sendMessage(message);
        }

    }

    /**
     * 保存位图到本地
     *
     * @param bitmap
     * @param path   本地路径
     * @return void
     */
    public void savaImage(Bitmap bitmap, File path) {
//        File file = new File(path);
        FileOutputStream fileOutputStream = null;
        //文件夹不存在，则创建它
        if (!path.exists()) {
            path.mkdir();
        }
        try {
            fileOutputStream = new FileOutputStream(path + "/" + System.currentTimeMillis() + ".png");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            File f = new File(path + "/" + System.currentTimeMillis() + ".png");
//                    if (f != null && f.exists() && f.isFile()) {
            shareIntent.setType("image/*");
            Uri u = Uri.fromFile(f);
            shareIntent.putExtra(Intent.EXTRA_STREAM, u);
//                    }
//                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(getH5ShareLinkUrlBean.getReturnData().getImgUrl()));
//                    shareIntent.setType("image/*");
            //当用户选择短信时使用sms_body取得文字
            shareIntent.putExtra("sms_body", H5ShareLinkUrlBean.getReturnData().getContent() + H5ShareLinkUrlBean.getReturnData().getLink());
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, H5ShareLinkUrlBean.getReturnData().getContent() + H5ShareLinkUrlBean.getReturnData().getLink());
            startActivity(Intent.createChooser(shareIntent, H5ShareLinkUrlBean.getReturnData().getTitle()));


            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
