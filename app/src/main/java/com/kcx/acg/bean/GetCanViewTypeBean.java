package com.kcx.acg.bean;

/**
 */

public class GetCanViewTypeBean {
    /**
     * errorCode : 200
     * errorMsg : 请求成功
     * returnData : {"canViewType":1}
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
         * canViewType : 1
         */

        private int canViewType;

        public int getCanViewType() {
            return canViewType;
        }

        public void setCanViewType(int canViewType) {
            this.canViewType = canViewType;
        }
    }
}
