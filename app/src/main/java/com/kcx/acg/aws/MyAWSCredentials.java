package com.kcx.acg.aws;

import com.amazonaws.auth.AWSCredentials;
import com.kcx.acg.bean.AmazonS3ConfigBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.manager.AccountManager;

/**
 * Created by nergal on 2017/5/12.
 */

public class MyAWSCredentials implements AWSCredentials {
    @Override
    public String getAWSAccessKeyId() {
        return AccountManager.getInstances().getAmazonS3Config().getAccessKey();
    }

    @Override
    public String getAWSSecretKey() {
        return AccountManager.getInstances().getAmazonS3Config().getSecretKey();
    }
}
