package com.kcx.acg.bean;

import java.io.Serializable;

/**
 * Created by zjb on 2018/11/23.
 */
public class AmazonS3ConfigBean implements Serializable {
    /**
     * errorCode : 200
     * errorMsg : 请求成功
     * returnData : {"configName":"ap-northeast_S3Config_https","bucketName":"acg-my-s","accessKey":"ODdQVGtkQ0hXL2pHd1c4dE9mOEhQTGpjTk4xYzFQemNIMzJadzlqUHh2QT0=","secretKey":"T1F4U0FEeU1jQ2JKbm5FWDNsNXZKdUc0eXFmRlhyL0JuZ1ZIaEVOYlVrc0pBSE9qOXY5eUNrVHBoRjVIc0JrZQ==","endpoint":"APNortheast1","endpointURL":"https://s3-ap-northeast-1.amazonaws.com"}
     */

    private int errorCode;
    private String errorMsg;
    private ReturnDataBean returnData;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public ReturnDataBean getReturnData() {
        return returnData;
    }

    public void setReturnData(ReturnDataBean returnData) {
        this.returnData = returnData;
    }

    public static class ReturnDataBean implements Serializable {
        /**
         * configName : ap-northeast_S3Config_https
         * bucketName : acg-my-s
         * accessKey : ODdQVGtkQ0hXL2pHd1c4dE9mOEhQTGpjTk4xYzFQemNIMzJadzlqUHh2QT0=
         * secretKey : T1F4U0FEeU1jQ2JKbm5FWDNsNXZKdUc0eXFmRlhyL0JuZ1ZIaEVOYlVrc0pBSE9qOXY5eUNrVHBoRjVIc0JrZQ==
         * endpoint : APNortheast1
         * endpointURL : https://s3-ap-northeast-1.amazonaws.com
         */

        private String configName;
        private String bucketName;
        private String accessKey;
        private String secretKey;
        private String endpoint;
        private String endpointURL;

        public String getConfigName() {
            return configName;
        }

        public void setConfigName(String configName) {
            this.configName = configName;
        }

        public String getBucketName() {
            return bucketName;
        }

        public void setBucketName(String bucketName) {
            this.bucketName = bucketName;
        }

        public String getAccessKey() {
            return accessKey;
        }

        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getEndpointURL() {
            return endpointURL;
        }

        public void setEndpointURL(String endpointURL) {
            this.endpointURL = endpointURL;
        }
    }
}
