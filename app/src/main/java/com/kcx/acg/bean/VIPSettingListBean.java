package com.kcx.acg.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jb on 2018/9/26.
 */
public class VIPSettingListBean implements Serializable {


    /**
     * errorCode : 200
     * errorMsg : 请求成功
     * returnData : {"list":[{"isDefault":true,"isDiscount":false,"money":"138","moneyPerMonth":"46","month":3,"packageID":1,"packageName":"3个月","rewardLotteryCount":18},{"isDefault":false,"isDiscount":true,"money":"448","moneyPerMonth":"37.33","month":12,"packageID":2,"packageName":"12个月","rewardLotteryCount":88},{"isDefault":false,"isDiscount":false,"money":"50","moneyPerMonth":"50","month":1,"packageID":3,"packageName":"3个月","rewardLotteryCount":5}]}
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
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * isDefault : true
             * isDiscount : false
             * money : 138
             * moneyPerMonth : 46
             * month : 3
             * packageID : 1
             * packageName : 3个月
             * rewardLotteryCount : 18
             */

            private boolean isDefault;
            private boolean isDiscount;
            private String money;
            private String moneyPerMonth;
            private int month;
            private int packageID;
            private String packageName;
            private int rewardLotteryCount;
            private boolean enbale;

            public boolean isEnbale() {
                return enbale;
            }

            public void setEnbale(boolean enbale) {
                this.enbale = enbale;
            }

            public boolean isIsDefault() {
                return isDefault;
            }

            public void setIsDefault(boolean isDefault) {
                this.isDefault = isDefault;
            }

            public boolean isIsDiscount() {
                return isDiscount;
            }

            public void setIsDiscount(boolean isDiscount) {
                this.isDiscount = isDiscount;
            }

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }

            public String getMoneyPerMonth() {
                return moneyPerMonth;
            }

            public void setMoneyPerMonth(String moneyPerMonth) {
                this.moneyPerMonth = moneyPerMonth;
            }

            public int getMonth() {
                return month;
            }

            public void setMonth(int month) {
                this.month = month;
            }

            public int getPackageID() {
                return packageID;
            }

            public void setPackageID(int packageID) {
                this.packageID = packageID;
            }

            public String getPackageName() {
                return packageName;
            }

            public void setPackageName(String packageName) {
                this.packageName = packageName;
            }

            public int getRewardLotteryCount() {
                return rewardLotteryCount;
            }

            public void setRewardLotteryCount(int rewardLotteryCount) {
                this.rewardLotteryCount = rewardLotteryCount;
            }
        }
    }
}
