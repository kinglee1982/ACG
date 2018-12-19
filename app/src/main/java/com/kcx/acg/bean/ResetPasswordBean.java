package com.kcx.acg.bean;

import java.io.Serializable;

/**
 * Created by jb on 2018/9/14.
 */
public class ResetPasswordBean implements Serializable {


    /**
     * errorCode : 200
     * errorMsg : 修改成功
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
