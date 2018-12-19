package com.kcx.acg.bean;

import java.io.Serializable;

/**
 * Created by jb on 2018/9/20.
 */
public class UserIncomeInfoBean implements Serializable {


    /**
     * errorCode : 200
     * errorMsg : 请求成功
     * returnData : {"totalIncome":"9038","newIncome":"0","accountStatus":1}
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
         * totalIncome : 9038
         * newIncome : 0
         * accountStatus : 1
         */

        private String totalIncome;
        private String newIncome;
        private String h5LinkIncomeDescription;
        private int accountStatus;

        public String getH5LinkIncomeDescription() {
            return h5LinkIncomeDescription;
        }

        public void setH5LinkIncomeDescription(String h5LinkIncomeDescription) {
            this.h5LinkIncomeDescription = h5LinkIncomeDescription;
        }


        public String getTotalIncome() {
            return totalIncome;
        }

        public void setTotalIncome(String totalIncome) {
            this.totalIncome = totalIncome;
        }

        public String getNewIncome() {
            return newIncome;
        }

        public void setNewIncome(String newIncome) {
            this.newIncome = newIncome;
        }

        public int getAccountStatus() {
            return accountStatus;
        }

        public void setAccountStatus(int accountStatus) {
            this.accountStatus = accountStatus;
        }
    }
}
