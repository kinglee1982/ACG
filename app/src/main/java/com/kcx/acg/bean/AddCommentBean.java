package com.kcx.acg.bean;

/**
 */

public class AddCommentBean {

    /**
     * errorCode : 200
     * errorMsg : 添加评论信息成功
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
