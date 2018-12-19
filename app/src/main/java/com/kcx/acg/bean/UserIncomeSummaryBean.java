package com.kcx.acg.bean;

import java.io.Serializable;

/**
 * Created by jb on 2018/9/28.
 */
public class UserIncomeSummaryBean implements Serializable {


    /**
     * errorCode : 200
     * errorMsg : 请求成功
     * returnData : {"weekIncome":"0","monthIncome":"0","totalIncome":"0"}
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
         * weekIncome : 0
         * monthIncome : 0
         * totalIncome : 0
         */

        private String weekIncome;
        private String monthIncome;
        private String totalIncome;

        public String getWeekIncome() {
            return weekIncome;
        }

        public void setWeekIncome(String weekIncome) {
            this.weekIncome = weekIncome;
        }

        public String getMonthIncome() {
            return monthIncome;
        }

        public void setMonthIncome(String monthIncome) {
            this.monthIncome = monthIncome;
        }

        public String getTotalIncome() {
            return totalIncome;
        }

        public void setTotalIncome(String totalIncome) {
            this.totalIncome = totalIncome;
        }
    }
}
