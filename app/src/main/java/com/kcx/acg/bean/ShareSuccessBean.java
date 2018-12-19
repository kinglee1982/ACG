package com.kcx.acg.bean;

/**
 * Created by  o on 2018/10/26.
 */

public class ShareSuccessBean {
    /**
     * errorCode : 200
     * errorMsg : 请求成功
     * returnData : true
     */

    private int errorCode;
    private String errorMsg;
    private boolean returnData;

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

    public boolean isReturnData() {
        return returnData;
    }

    public void setReturnData(boolean returnData) {
        this.returnData = returnData;
    }
}
