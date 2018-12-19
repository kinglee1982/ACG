package com.kcx.acg.manager;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import com.apkfuns.logutils.LogUtils;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.kcx.acg.R;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.model.UploadFileRequestBody;
import com.kcx.acg.net.ThreadPoolProxyFactory;
import com.kcx.acg.views.callback.RetrofitCallback;
import com.kcx.acg.views.callback.UploadCallback;
import com.luck.picture.lib.entity.LocalMedia;
import com.vincent.videocompressor.VideoCompress;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileBatchCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 */

public class UploadManager {

    private UploadManager() {
    }

    public static UploadManager getInstances() {
        synchronized (UploadManager.class) {
            return new UploadManager();
        }
    }

    public static class Builder {

        private UploadManager uploadManager;
        private Context context;
        private boolean isCompress = false;
        private MediaType meidiaType = MediaType.VIDEO;
        private List<LocalMedia> mediaList;

        public enum MediaType {
            VIDEO,
            IMAGE
        }

        public Builder(Context context) {
            this.context = context;
        }

        public Builder upload(UploadListener uploadListener) {
            List<LocalMedia> mMediaList = checkNotNull(mediaList, "media list is null");
            if (isCompress) {
                if (meidiaType == MediaType.VIDEO) {
                    compressVideo(mMediaList, uploadListener);
                }
            }
            return this;
        }

        public Builder isCompress(boolean isCompress) {
            this.isCompress = isCompress;
            return this;
        }

        public Builder setMeidiaType(MediaType meidiaType) {
            this.meidiaType = meidiaType;
            return this;
        }

        public Builder setMediaList(List<LocalMedia> mediaList) {
            this.mediaList = mediaList;
            return this;
        }

        private Locale getLocale() {
            Configuration config = context.getResources().getConfiguration();
            Locale sysLocale = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                sysLocale = getSystemLocale(config);
            } else {
                sysLocale = getSystemLocaleLegacy(config);
            }

            return sysLocale;
        }

        @SuppressWarnings("deprecation")
        private static Locale getSystemLocaleLegacy(Configuration config) {
            return config.locale;
        }

        @TargetApi(Build.VERSION_CODES.N)
        private static Locale getSystemLocale(Configuration config) {
            return config.getLocales().get(0);
        }

        private void compressVideo(List<LocalMedia> mMediaList, final UploadListener uploadListener) {

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/ACG");
            file.mkdirs();
            for (int i = 0; i < mMediaList.size(); i++) {

                final String destPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/ACG/" + new SimpleDateFormat("yyyyMMdd_HHmmssSSS", getLocale()).format(new Date()) + i + ".mp4";
                final int finalI = i;
                String path = mMediaList.get(i).getPath();
                VideoCompress.compressVideoMedium(path, destPath, new VideoCompress.CompressListener() {
                    @Override
                    public void onStart() {
                        uploadListener.onStart(finalI);
                    }

                    @Override
                    public void onSuccess() {
                        uploadListener.onSuccess(finalI);
                        LogUtils.d(finalI + " onSuccess ");
                    }

                    @Override
                    public void onFail() {
                        uploadListener.onFail(finalI);
                    }

                    @Override
                    public void onProgress(float percent) {
                        uploadListener.onProgress(finalI, percent);
                        LogUtils.d(finalI + " - " + (int) percent);
                    }
                });
            }

        }

        public interface UploadListener {
            void onStart(int position);

            void onSuccess(int position);

            void onFail(int position);

            void onProgress(int position, float percent);
        }

    }

}
