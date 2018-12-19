package com.kcx.acg.bean;

import java.io.Serializable;

/**
 * Created by jb on 2018/9/20.
 */
public class AddBankBean implements Serializable {


    /**
     * errorCode : 202
     * errorMsg : 该银行卡位数有误
     */

    private int errorCode;
    private String errorMsg;

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
}
