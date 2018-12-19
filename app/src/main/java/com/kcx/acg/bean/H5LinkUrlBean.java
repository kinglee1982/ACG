package com.kcx.acg.bean;

import java.io.Serializable;

/**
 * Created by jb on 2018/10/22.
 */
public class H5LinkUrlBean implements Serializable {


    /**
     * errorCode : 200
     * errorMsg : 请求成功
     * returnData : {"h5Link":"http://192.168.1.193:8080/common/agreement.html#android"}
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
         * h5Link : http://192.168.1.193:8080/common/agreement.html#android
         */

        private String h5Link;

        public String getH5Link() {
            return h5Link;
        }

        public void setH5Link(String h5Link) {
            this.h5Link = h5Link;
        }
    }
}
