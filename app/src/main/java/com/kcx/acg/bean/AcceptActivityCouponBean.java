package com.kcx.acg.bean;

public class AcceptActivityCouponBean {
    /**
     * errorCode : 200
     * errorMsg :
     * returnData : {"amount":"40.1","incomeAmount":27.9,"name":"VIP现金抵用券","expirationDate":"2018-12-31 00:00:00"}
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
         * amount : 40.1
         * incomeAmount : 27.9
         * name : VIP现金抵用券
         * expirationDate : 2018-12-31 00:00:00
         */

        private String amount;
        private double incomeAmount;
        private String name;
        private String expirationDate;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public double getIncomeAmount() {
            return incomeAmount;
        }

        public void setIncomeAmount(double incomeAmount) {
            this.incomeAmount = incomeAmount;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getExpirationDate() {
            return expirationDate;
        }

        public void setExpirationDate(String expirationDate) {
            this.expirationDate = expirationDate;
        }
    }
}
