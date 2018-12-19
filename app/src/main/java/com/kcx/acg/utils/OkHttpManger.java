package com.kcx.acg.utils;

import android.os.Handler;
import android.os.Looper;

import com.kcx.acg.base.SysApplication;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * OkHttp 工具类，
 * get的同步异步请求
 * post的json字符串同步异步上传
 * post的键值对同步异步上传
 * post文件异步上传，回调结果以及进度
 * 异步下载文件，回调结果以及进度
 * <p>
 * Created by Seeker on 2016/6/24.
 */
public final class OkHttpManger {

    private static final String TAG = "OkHttpManger";

    private static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");

    public Handler okHttpHandler;
    private OkHttpClient mOkHttpClient;

    private OkHttpManger() {
        this.mOkHttpClient = SysApplication.getInstance().getUnsafeOkHttpClient();
        this.okHttpHandler = new Handler(Looper.getMainLooper());
    }

    public static final OkHttpManger getInstance() {
        return SingleFactory.manger;
    }

    private static final class SingleFactory {
        private static final OkHttpManger manger = new OkHttpManger();
    }

    /**
     * 同步get请求
     *
     * @param url 地址
     * @return Response 返回数据
     */
    public Response getSync(final String url) throws IOException {
        final Request request = new Request.Builder().url(url).build();
        final Call call = mOkHttpClient.newCall(request);
        return call.execute();
    }

    /**
     * 同步post请求,键值对
     *
     * @param url    地址
     * @param params 参数
     *               Request.Builder().url(url).post(builder.build()).build();
     */
    public Response postSync(String url, Param... params) throws IOException {
        final Request request = buildPostRequst(url, params);
        final Call call = mOkHttpClient.newCall(request);
        return call.execute();
    }

    /**
     * 同步post请求,键值对
     *
     * @param url    地址
     * @param params 参数
     */
    public Response postSync(String url, Map<String, String> params) throws IOException {
        final Request request = buildPostRequst(url, params);
        final Call call = mOkHttpClient.newCall(request);
        return call.execute();
    }

    /**
     * post同步请求，提交Json数据
     *
     * @param url  地址
     * @param json json格式的字符串
     * @return Response
     */
    public Response postSyncJson(String url, String json) throws IOException {
        final RequestBody requestBody = RequestBody.create(JSON_TYPE, json);
        final Request request = new Request.Builder().url(url).post(requestBody).build();
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 同步基于post的文件上传
     *
     * @param url     地址
     * @param file    提交的文件
     * @param fileKey 提交的文件key
     * @return Response
     */
    public Response uploadSync(String url, File file, String fileKey) throws IOException {
        return uploadSync(url, new File[]{file}, new String[]{fileKey}, new Param[0]);
    }

    /**
     * 同步基于post的文件上传
     *
     * @param url      地址
     * @param files    提交的文件数组
     * @param fileKeys 提交的文件数组key
     * @param params   提交的键值对
     * @return Response
     */
    public Response uploadSync(String url, File[] files, String[] fileKeys, Param[] params) throws IOException {
        final RequestBody requestBody = buildMultipartFormRequestBody(files, fileKeys, params);
        final Request request = new Request.Builder().url(url).post(requestBody).build();
        return mOkHttpClient.newCall(request).execute();
    }


    interface MyCallback {
        void onSuccess(String result);

        void onFailture();
    }

    public void postAsyncJsonn(String url, String json, MyCallback mCallback) throws IOException {
        final RequestBody requestBody = RequestBody.create(JSON_TYPE, json);
        final Request request = new Request.Builder().url(url).post(requestBody).build();
        deliveryResult(mOkHttpClient.newCall(request), mCallback);
    }

    private void deliveryResult(final Call call, final MyCallback mCallback) {
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                okHttpHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCallback != null) {
                            mCallback.onFailture();
                        }
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responseStr = response.body().string();

                okHttpHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCallback != null) {
                            mCallback.onSuccess(responseStr);
                        }
                    }
                });

            }
        });
    }

    /**
     * 生成request
     *
     * @param url
     * @param params
     * @return
     */
    private Request buildPostRequst(String url, Param... params) {
        if (params == null) {
            params = new Param[0];
        }
        FormBody.Builder builder = new FormBody.Builder();
        for (Param param : params) {
            builder.add(param.key, param.value);
        }
        return new Request.Builder().url(url).post(builder.build()).build();
    }

    /**
     * 生成request
     *
     * @param url
     * @param params
     * @return
     */
    private Request buildPostRequst(String url, Map<String, String> params) {
        Request request = null;
        if (params == null) {
            params = new HashMap<>();
        }
        if (params != null) {
            Set<Map.Entry<String, String>> entries = params.entrySet();
            FormBody.Builder builder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : entries) {
                builder.add(entry.getKey(), entry.getValue());
            }
            request = new Request.Builder().url(url).post(builder.build()).build();
        }
        return request;
    }

    /**
     * 生成post提交时的分块request
     *
     * @param files
     * @param fileKeys
     * @param params
     * @return
     */
    private RequestBody buildMultipartFormRequestBody(File[] files, String[] fileKeys, Param[] params) {
        if (params == null) {
            params = new Param[0];
        }
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""),
                    RequestBody.create(null, param.value));
        }
        if (files == null) {
            files = new File[0];
        }
        if (fileKeys == null) {
            fileKeys = new String[0];
        }

        if (fileKeys.length != files.length) {
            throw new ArrayStoreException("fileKeys.length != files.length");
        }
        RequestBody fileBody = null;
        int length = files.length;
        for (int i = 0; i < length; i++) {
            File file = files[i];
            String fileName = file.getName();
            fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
            //TODO 根据文件名设置contentType
            builder.addPart(Headers.of("Content-Disposition",
                    "form-data; name=\"" + fileKeys[i] + "\"; fileName=\"" + fileName + "\""),
                    fileBody);
        }
        return builder.build();
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = fileNameMap.getContentTypeFor(path);
        if (type == null) {
            type = "application/octet-stream";
        }
        return type;
    }

    /**
     * 获取文件名
     *
     * @param path
     */
    private String getFileName(String path) {
        int lastSeparaorIndex = path.lastIndexOf("/");
        return (lastSeparaorIndex < 0) ? path : path.substring(lastSeparaorIndex + 1, path.length());
    }


    public static final class Param {
        private String key;
        private String value;

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}