package com.kcx.acg.bean;

/**
 */

public class UpgradeInfoBean {

    /**
     * errorCode : 200
     * errorMsg : 请求成功
     * returnData : {"description":"描述\\n123","deviceType":"Android","downLoadUrl":"http://192.168.3.80:8899/Content/AppDownload/app-debug.apk","appVersion":"1.1","isEnforceUpdate":false}
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

    public static class ReturnDataBean {
        /**
         * description : 描述\n123
         * deviceType : Android
         * downLoadUrl : http://192.168.3.80:8899/Content/AppDownload/app-debug.apk
         * appVersion : 1.1
         * isEnforceUpdate : false
         */

        private String description;
        private String deviceType;
        private String downLoadUrl;
        private String appVersion;
        private boolean isEnforceUpdate;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getDownLoadUrl() {
            return downLoadUrl;
        }

        public void setDownLoadUrl(String downLoadUrl) {
            this.downLoadUrl = downLoadUrl;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public boolean isIsEnforceUpdate() {
            return isEnforceUpdate;
        }

        public void setIsEnforceUpdate(boolean isEnforceUpdate) {
            this.isEnforceUpdate = isEnforceUpdate;
        }
    }
}
