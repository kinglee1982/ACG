package com.kcx.acg.views.callback;

/**
 */

public interface UploadCallback {
    void onLoading(int position, long total, long progress, boolean hasFinish);
}
