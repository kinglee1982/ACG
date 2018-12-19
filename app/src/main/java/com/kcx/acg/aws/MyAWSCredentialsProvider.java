package com.kcx.acg.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;

/**
 * Created by nergal on 2017/5/12.
 */

public class MyAWSCredentialsProvider implements AWSCredentialsProvider {
    @Override
    public AWSCredentials getCredentials() {
        return new MyAWSCredentials();
    }

    @Override
    public void refresh() {

    }
}

