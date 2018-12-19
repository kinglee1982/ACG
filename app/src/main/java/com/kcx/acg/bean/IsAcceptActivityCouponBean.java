package com.kcx.acg.bean;

public class IsAcceptActivityCouponBean {

    /**
     * errorCode : 200
     * errorMsg :
     * returnData : {"isAccepted":true,"couponAmout":"68"}
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
         * isAccepted : true
         * couponAmout : 68
         */

        private boolean isAccepted;
        private String couponAmout;

        public boolean isIsAccepted() {
            return isAccepted;
        }

        public void setIsAccepted(boolean isAccepted) {
            this.isAccepted = isAccepted;
        }

        public String getCouponAmout() {
            return couponAmout;
        }

        public void setCouponAmout(String couponAmout) {
            this.couponAmout = couponAmout;
        }
    }
}
