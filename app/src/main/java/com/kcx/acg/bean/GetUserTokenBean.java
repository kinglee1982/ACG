package com.kcx.acg.bean;

import java.io.Serializable;

/**
 * Created by jb on 2018/9/12.
 */
public class GetUserTokenBean implements Serializable {


    /**
     * errorCode : 200
     * errorMsg : null
     * returnData : {"accessToken":"83c1031f3c82b5e972a5aebe18024f57"}
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
         * accessToken : 83c1031f3c82b5e972a5aebe18024f57
         */

        private String accessToken;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }
    }
}
