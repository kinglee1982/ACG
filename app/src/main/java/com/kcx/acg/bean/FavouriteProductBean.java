package com.kcx.acg.bean;

/**
 */

public class FavouriteProductBean {

    /**
     * errorCode : 200
     * errorMsg : 收藏成功
     * returnData : null
     */

    private int errorCode;
    private String errorMsg;
    private Object returnData;

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

    public Object getReturnData() {
        return returnData;
    }

    public void setReturnData(Object returnData) {
        this.returnData = returnData;
    }
}
