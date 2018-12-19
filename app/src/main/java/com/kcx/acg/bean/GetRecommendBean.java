package com.kcx.acg.bean;

import java.util.List;

/**
 */

public class GetRecommendBean {

    /**
     * errorCode : 200
     * errorMsg :
     * returnData : [{"memberID":5,"memberUserName":"LRY","memberPhoto":"http://192.168.3.30:8888/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07?type=1","recommendProductList":[{"productID":600,"coverPicUrl":"http://192.168.3.30:9001/cosplay_la/1105--42914c8f-8133-a24e-139a-4a666685e36d.jpg"},{"productID":601,"coverPicUrl":"http://192.168.3.30:9001/cosplay_la/1018--2ab9919e-0133-a251-1344-2c306284cdf6.jpg"},{"productID":602,"coverPicUrl":"http://192.168.3.30:9001/cosplay_la/1156--04db33e4-9133-a252-1461-5fc012edc159.jpg"}]}]
     * resCount : 1
     */

    private int errorCode;
    private String errorMsg;
    private int resCount;
    private List<ReturnDataBean> returnData;

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

    public int getResCount() {
        return resCount;
    }

    public void setResCount(int resCount) {
        this.resCount = resCount;
    }

    public List<ReturnDataBean> getReturnData() {
        return returnData;
    }

    public void setReturnData(List<ReturnDataBean> returnData) {
        this.returnData = returnData;
    }

    public static class ReturnDataBean {
        /**
         * memberID : 5
         * memberUserName : LRY
         * memberPhoto : http://192.168.3.30:8888/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07?type=1
         * recommendProductList : [{"productID":600,"coverPicUrl":"http://192.168.3.30:9001/cosplay_la/1105--42914c8f-8133-a24e-139a-4a666685e36d.jpg"},{"productID":601,"coverPicUrl":"http://192.168.3.30:9001/cosplay_la/1018--2ab9919e-0133-a251-1344-2c306284cdf6.jpg"},{"productID":602,"coverPicUrl":"http://192.168.3.30:9001/cosplay_la/1156--04db33e4-9133-a252-1461-5fc012edc159.jpg"}]
         */

        private int memberID;
        private String memberUserName;
        private String memberPhoto;

        public int getUserIdentify() {
            return userIdentify;
        }

        public void setUserIdentify(int userIdentify) {
            this.userIdentify = userIdentify;
        }

        private int userIdentify;
        private List<RecommendProductListBean> recommendProductList;

        public int getMemberID() {
            return memberID;
        }

        public void setMemberID(int memberID) {
            this.memberID = memberID;
        }

        public String getMemberUserName() {
            return memberUserName;
        }

        public void setMemberUserName(String memberUserName) {
            this.memberUserName = memberUserName;
        }

        public String getMemberPhoto() {
            return memberPhoto;
        }

        public void setMemberPhoto(String memberPhoto) {
            this.memberPhoto = memberPhoto;
        }

        public List<RecommendProductListBean> getRecommendProductList() {
            return recommendProductList;
        }

        public void setRecommendProductList(List<RecommendProductListBean> recommendProductList) {
            this.recommendProductList = recommendProductList;
        }

        public static class RecommendProductListBean {
            /**
             * productID : 600
             * coverPicUrl : http://192.168.3.30:9001/cosplay_la/1105--42914c8f-8133-a24e-139a-4a666685e36d.jpg
             */

            private int productID;
            private String coverPicUrl;

            public int getProductID() {
                return productID;
            }

            public void setProductID(int productID) {
                this.productID = productID;
            }

            public String getCoverPicUrl() {
                return coverPicUrl;
            }

            public void setCoverPicUrl(String coverPicUrl) {
                this.coverPicUrl = coverPicUrl;
            }
        }
    }
}
