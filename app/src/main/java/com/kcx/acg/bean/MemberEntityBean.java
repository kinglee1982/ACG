package com.kcx.acg.bean;

import java.io.Serializable;

/**
 * Created by jb on 2018/9/26.
 */
public class MemberEntityBean implements Serializable {


    /**
     * errorCode : 200
     * errorMsg :
     * returnData : {"fansCount":0,"isAttentioned":false,"memberId":8,"photo":"http://192.168.3.30:8888/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07?type=1","userName":"LRY_A_B_C"}
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
         * fansCount : 0
         * isAttentioned : false
         * memberId : 8
         * photo : http://192.168.3.30:8888/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07?type=1
         * userName : LRY_A_B_C
         */

        private int fansCount;
        private boolean isAttentioned;
        private int memberId;
        private int userIdentify;
        private String photo;
        private String userName;

        public int getUserIdentify() {
            return userIdentify;
        }

        public void setUserIdentify(int userIdentify) {
            this.userIdentify = userIdentify;
        }


        public int getFansCount() {
            return fansCount;
        }

        public void setFansCount(int fansCount) {
            this.fansCount = fansCount;
        }

        public boolean isIsAttentioned() {
            return isAttentioned;
        }

        public void setIsAttentioned(boolean isAttentioned) {
            this.isAttentioned = isAttentioned;
        }

        public int getMemberId() {
            return memberId;
        }

        public void setMemberId(int memberId) {
            this.memberId = memberId;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
