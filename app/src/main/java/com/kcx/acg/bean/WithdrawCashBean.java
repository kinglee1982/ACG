package com.kcx.acg.bean;

import java.io.Serializable;

/**
 * Created by jb on 2018/9/20.
 */
public class WithdrawCashBean implements Serializable {
    /**
     * errorCode : 404
     * errorMsg : 余额不足￥100.00 无法提现
     * returnData : {"totalIncome":0}
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
         * totalIncome : 0
         */

        private int totalIncome;

        public int getTotalIncome() {
            return totalIncome;
        }

        public void setTotalIncome(int totalIncome) {
            this.totalIncome = totalIncome;
        }
    }
}
