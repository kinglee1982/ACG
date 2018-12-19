package com.kcx.acg.bean;

import java.io.Serializable;

/**
 * Created by jb on 2018/9/21.
 */
public class CommonBean implements Serializable {


    /**
     * errorCode : 200
     * errorMsg : 关注成功
     * returnData : null
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
