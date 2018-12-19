package com.kcx.acg.bean;

import java.io.Serializable;

/**
 * Created by jb on 2018/9/12.
 */
public class RegisterUserBean implements Serializable {


    /**
     * errorCode : 200
     * errorMsg : 注册成功
     * returnData : {"accessToken":"a520842f4231949827dc700650d49bcb","regAwardCoinNum":500}
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
         * accessToken : a520842f4231949827dc700650d49bcb
         * regAwardCoinNum : 500
         */

        private String accessToken;
        private int regAwardCoinNum;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public int getRegAwardCoinNum() {
            return regAwardCoinNum;
        }

        public void setRegAwardCoinNum(int regAwardCoinNum) {
            this.regAwardCoinNum = regAwardCoinNum;
        }
    }
}
