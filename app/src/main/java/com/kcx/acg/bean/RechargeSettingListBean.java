package com.kcx.acg.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jb on 2018/9/26.
 */
public class RechargeSettingListBean implements Serializable {


    /**
     * errorCode : 200
     * errorMsg : 请求成功
     * returnData : {"firstRechargeIco":"http://192.168.3.30:8060/SystemConfigIco/RechargeIco/bg_chongzhi@2x.png","isFirstRechargeActivity":false,"list":[{"aCoin":5000,"createTime":"2018-09-25 20:05:04","icon":"http://192.168.3.30:8060/SystemConfigIco/RechargeIco/2ximage_goldcoin1.png","isRecommend":false,"money":"50","rechargeSettingID":1,"rewardACoin":0,"rewardLotteryCount":5},{"aCoin":18800,"createTime":"2018-09-25 20:05:40","icon":"http://192.168.3.30:8060/SystemConfigIco/RechargeIco/2ximage_goldcoin2.png","isRecommend":true,"money":"188","rechargeSettingID":2,"rewardACoin":0,"rewardLotteryCount":28},{"aCoin":48800,"createTime":"2018-09-25 20:06:15","icon":"http://192.168.3.30:8060/SystemConfigIco/RechargeIco/2ximage_goldcoin3.png","isRecommend":false,"money":"488","rechargeSettingID":3,"rewardACoin":0,"rewardLotteryCount":78},{"aCoin":99800,"createTime":"2018-09-25 20:07:11","icon":"http://192.168.3.30:8060/SystemConfigIco/RechargeIco/2ximage_goldcoin4.png","isRecommend":false,"money":"998","rechargeSettingID":4,"rewardACoin":0,"rewardLotteryCount":188}]}
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
         * firstRechargeIco : http://192.168.3.30:8060/SystemConfigIco/RechargeIco/bg_chongzhi@2x.png
         * isFirstRechargeActivity : false
         * list : [{"aCoin":5000,"createTime":"2018-09-25 20:05:04","icon":"http://192.168.3.30:8060/SystemConfigIco/RechargeIco/2ximage_goldcoin1.png","isRecommend":false,"money":"50","rechargeSettingID":1,"rewardACoin":0,"rewardLotteryCount":5},{"aCoin":18800,"createTime":"2018-09-25 20:05:40","icon":"http://192.168.3.30:8060/SystemConfigIco/RechargeIco/2ximage_goldcoin2.png","isRecommend":true,"money":"188","rechargeSettingID":2,"rewardACoin":0,"rewardLotteryCount":28},{"aCoin":48800,"createTime":"2018-09-25 20:06:15","icon":"http://192.168.3.30:8060/SystemConfigIco/RechargeIco/2ximage_goldcoin3.png","isRecommend":false,"money":"488","rechargeSettingID":3,"rewardACoin":0,"rewardLotteryCount":78},{"aCoin":99800,"createTime":"2018-09-25 20:07:11","icon":"http://192.168.3.30:8060/SystemConfigIco/RechargeIco/2ximage_goldcoin4.png","isRecommend":false,"money":"998","rechargeSettingID":4,"rewardACoin":0,"rewardLotteryCount":188}]
         */

        private String firstRechargeIco;
        private boolean isFirstRechargeActivity;
        private List<ListBean> list;

        public String getFirstRechargeIco() {
            return firstRechargeIco;
        }

        public void setFirstRechargeIco(String firstRechargeIco) {
            this.firstRechargeIco = firstRechargeIco;
        }

        public boolean isIsFirstRechargeActivity() {
            return isFirstRechargeActivity;
        }

        public void setIsFirstRechargeActivity(boolean isFirstRechargeActivity) {
            this.isFirstRechargeActivity = isFirstRechargeActivity;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * aCoin : 5000
             * createTime : 2018-09-25 20:05:04
             * icon : http://192.168.3.30:8060/SystemConfigIco/RechargeIco/2ximage_goldcoin1.png
             * isRecommend : false
             * money : 50
             * rechargeSettingID : 1
             * rewardACoin : 0
             * rewardLotteryCount : 5
             */

            private int aCoin;
            private String createTime;
            private String icon;
            private boolean isRecommend;
            private boolean isDefault = false;
            private String money;
            private int rechargeSettingID;
            private int rewardACoin;
            private int rewardLotteryCount;

            public boolean isDefault() {
                return isDefault;
            }

            public void setDefault(boolean aDefault) {
                isDefault = aDefault;
            }


            public int getACoin() {
                return aCoin;
            }

            public void setACoin(int aCoin) {
                this.aCoin = aCoin;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public boolean isIsRecommend() {
                return isRecommend;
            }

            public void setIsRecommend(boolean isRecommend) {
                this.isRecommend = isRecommend;
            }

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }

            public int getRechargeSettingID() {
                return rechargeSettingID;
            }

            public void setRechargeSettingID(int rechargeSettingID) {
                this.rechargeSettingID = rechargeSettingID;
            }

            public int getRewardACoin() {
                return rewardACoin;
            }

            public void setRewardACoin(int rewardACoin) {
                this.rewardACoin = rewardACoin;
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
