package com.kcx.acg.net;

/**
 */

public class ThreadPoolProxyFactory {
    static ThreadPoolProxy mNormalThreadPoolProxy;
    static ThreadPoolProxy mUploadThreadPoolProxy;

    /**
     * 得到普通线程池代理对象mNormalThreadPoolProxy
     */
    public static ThreadPoolProxy getNormalThreadPoolProxy() {
        if (mNormalThreadPoolProxy == null) {
            synchronized (ThreadPoolProxyFactory.class) {
                if (mNormalThreadPoolProxy == null) {
                    mNormalThreadPoolProxy = new ThreadPoolProxy(5, 5);
                }
            }
        }
        return mNormalThreadPoolProxy;
    }

    /**
     * 得到下载线程池代理对象mDownLoadThreadPoolProxy
     */
    public static ThreadPoolProxy getUploadThreadPoolProxy() {
        if (mUploadThreadPoolProxy == null) {
            synchronized (ThreadPoolProxyFactory.class) {
                if (mUploadThreadPoolProxy == null) {
                    mUploadThreadPoolProxy = new ThreadPoolProxy(3, 10);
                }
            }
        }
        return mUploadThreadPoolProxy;
    }


}
