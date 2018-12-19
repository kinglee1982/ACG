package com.kcx.acg.bean;

import java.io.Serializable;

/**
 * Created by jb on 2018/9/25.
 */
public class TagBean implements Serializable {


    /**
     * errorCode : 200
     * errorMsg :
     * returnData : {"tagID":782,"name":"多人","tagPhoto":"","tagDesc":"","isAttentioned":false}
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
         * tagID : 782
         * name : 多人
         * tagPhoto :
         * tagDesc :
         * isAttentioned : false
         */

        private int tagID;
        private String name;
        private String tagPhoto;
        private String tagDesc;
        private boolean isAttentioned;

        public int getTagID() {
            return tagID;
        }

        public void setTagID(int tagID) {
            this.tagID = tagID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTagPhoto() {
            return tagPhoto;
        }

        public void setTagPhoto(String tagPhoto) {
            this.tagPhoto = tagPhoto;
        }

        public String getTagDesc() {
            return tagDesc;
        }

        public void setTagDesc(String tagDesc) {
            this.tagDesc = tagDesc;
        }

        public boolean isIsAttentioned() {
            return isAttentioned;
        }

        public void setIsAttentioned(boolean isAttentioned) {
            this.isAttentioned = isAttentioned;
        }
    }
}
