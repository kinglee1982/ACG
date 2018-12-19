package com.kcx.acg.bean;

/**
 */

public class BuyProductPopTipBean {

    /**
     * errorCode : 200
     * errorMsg :
     * returnData : {"isShared":false,"totalACoin":16529,"accountStatus":1,"aCoinPrice":100,"level":3,"userLevel":1,"todayViewTimes":0,"sourceType":2,"discountRate":0.18,"discountDays":30,"disCountProductPrice":100,"isDisCount":false}
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
         * isShared : false
         * totalACoin : 16529
         * accountStatus : 1
         * aCoinPrice : 100
         * level : 3
         * userLevel : 1
         * todayViewTimes : 0
         * sourceType : 2
         * discountRate : 0.18
         * discountDays : 30
         * disCountProductPrice : 100
         * isDisCount : false
         */

        private boolean isShared;
        private int totalACoin;
        private int accountStatus;
        private int aCoinPrice;
        private int level;
        private int userLevel;
        private int todayViewTimes;
        private int sourceType;
        private double discountRate;
        private int discountDays;
        private int disCountProductPrice;
        private boolean isDisCount;

        public boolean isIsShared() {
            return isShared;
        }

        public void setIsShared(boolean isShared) {
            this.isShared = isShared;
        }

        public int getTotalACoin() {
            return totalACoin;
        }

        public void setTotalACoin(int totalACoin) {
            this.totalACoin = totalACoin;
        }

        public int getAccountStatus() {
            return accountStatus;
        }

        public void setAccountStatus(int accountStatus) {
            this.accountStatus = accountStatus;
        }

        public int getACoinPrice() {
            return aCoinPrice;
        }

        public void setACoinPrice(int aCoinPrice) {
            this.aCoinPrice = aCoinPrice;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getUserLevel() {
            return userLevel;
        }

        public void setUserLevel(int userLevel) {
            this.userLevel = userLevel;
        }

        public int getTodayViewTimes() {
            return todayViewTimes;
        }

        public void setTodayViewTimes(int todayViewTimes) {
            this.todayViewTimes = todayViewTimes;
        }

        public int getSourceType() {
            return sourceType;
        }

        public void setSourceType(int sourceType) {
            this.sourceType = sourceType;
        }

        public double getDiscountRate() {
            return discountRate;
        }

        public void setDiscountRate(double discountRate) {
            this.discountRate = discountRate;
        }

        public int getDiscountDays() {
            return discountDays;
        }

        public void setDiscountDays(int discountDays) {
            this.discountDays = discountDays;
        }

        public int getDisCountProductPrice() {
            return disCountProductPrice;
        }

        public void setDisCountProductPrice(int disCountProductPrice) {
            this.disCountProductPrice = disCountProductPrice;
        }

        public boolean isIsDisCount() {
            return isDisCount;
        }

        public void setIsDisCount(boolean isDisCount) {
            this.isDisCount = isDisCount;
        }
    }
}
