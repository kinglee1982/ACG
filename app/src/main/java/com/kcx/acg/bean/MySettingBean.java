package com.kcx.acg.bean;

import java.io.Serializable;

public class MySettingBean implements Serializable {
    /**
     * errorCode : 200
     * errorMsg :
     * returnData : {"isPopularity":true}
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

    public static class ReturnDataBean implements Serializable{
        /**
         * isPopularity : true
         */

        private boolean isPopularity;

        public boolean isIsPopularity() {
            return isPopularity;
        }

        public void setIsPopularity(boolean isPopularity) {
            this.isPopularity = isPopularity;
        }
    }
}
