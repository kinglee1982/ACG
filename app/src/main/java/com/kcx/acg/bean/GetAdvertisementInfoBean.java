package com.kcx.acg.bean;

/**
 */

public class GetAdvertisementInfoBean {


    /**
     * errorCode : 200
     * errorMsg : 请求成功
     * returnData : {"locationID":10,"imageUrl":"http://acg-my-s.s3.ap-northeast-1.amazonaws.com/images/ad/201810/1.png","url":"http://acg-my-s.s3.ap-northeast-1.amazonaws.com/images/ad/201810/1.png","locationName":"征稿活动弹框","targetUrl":"http://www.baidu.com","description":"","advertiserLogoUrl":"","advertiserDescription":"","urlType":1}
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
         * locationID : 10
         * imageUrl : http://acg-my-s.s3.ap-northeast-1.amazonaws.com/images/ad/201810/1.png
         * url : http://acg-my-s.s3.ap-northeast-1.amazonaws.com/images/ad/201810/1.png
         * locationName : 征稿活动弹框
         * targetUrl : http://www.baidu.com
         * description :
         * advertiserLogoUrl :
         * advertiserDescription :
         * urlType : 1
         */

        private int locationID;
        private String imageUrl;
        private String url;
        private String locationName;
        private String targetUrl;
        private String description;
        private String advertiserLogoUrl;
        private String advertiserDescription;
        private int urlType;

        public int getLocationID() {
            return locationID;
        }

        public void setLocationID(int locationID) {
            this.locationID = locationID;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        public String getTargetUrl() {
            return targetUrl;
        }

        public void setTargetUrl(String targetUrl) {
            this.targetUrl = targetUrl;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAdvertiserLogoUrl() {
            return advertiserLogoUrl;
        }

        public void setAdvertiserLogoUrl(String advertiserLogoUrl) {
            this.advertiserLogoUrl = advertiserLogoUrl;
        }

        public String getAdvertiserDescription() {
            return advertiserDescription;
        }

        public void setAdvertiserDescription(String advertiserDescription) {
            this.advertiserDescription = advertiserDescription;
        }

        public int getUrlType() {
            return urlType;
        }

        public void setUrlType(int urlType) {
            this.urlType = urlType;
        }
    }
}
