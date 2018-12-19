package com.kcx.acg.bean;

import java.io.Serializable;

/**
 * Created by jb on 2018/9/12.
 */
public class ValidateIdentifyCodeBean implements Serializable {


    /**
     * errorCode : 200
     * errorMsg :
     * returnData : c58ae83e-f83b-4de8-97c5-646a7998dc7a
     */

    private int errorCode;
    private String errorMsg;
    private String returnData;

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

    public String getReturnData() {
        return returnData;
    }

    public void setReturnData(String returnData) {
        this.returnData = returnData;
    }
}
