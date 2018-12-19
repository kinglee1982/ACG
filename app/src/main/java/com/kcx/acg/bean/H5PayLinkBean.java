package com.kcx.acg.bean;

import java.io.Serializable;

/**
 * Created by jb on 2018/10/23.
 */
public class H5PayLinkBean implements Serializable {
    /**
     * errorCode : 200
     * errorMsg : 请求成功
     * returnData : {"payLink":"http://192.168.1.139:8899/Home/H5Pay?orderNo=1054987041701359616"}
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
         * payLink : http://192.168.1.139:8899/Home/H5Pay?orderNo=1054987041701359616
         */

        private String payLink;

        public String getPayLink() {
            return payLink;
        }

        public void setPayLink(String payLink) {
            this.payLink = payLink;
        }
    }
}
