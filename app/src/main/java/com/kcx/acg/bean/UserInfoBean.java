package com.kcx.acg.bean;

/**
 * Created by jb on 2018/9/14.
 */
public class UserInfoBean {

    /**
     * errorCode : 200
     * errorMsg : 请求成功
     * returnData : {"memberID":2167,"totalACoin":646,"totalIncome":"0.00","userLevel":1,"userName":"SBXQ","photo":"https://img-s.nicoacg2.com/images/default/default.png","areaCode":"86","phone":"123456078","vipExpirationTime":"","alertText":"","promotionLink":"http://www.baidu.com?inviteCode=99FOI","userIdentify":0,"accountStatus":1,"isHaveNewIncome":false,"attentionCount":1,"fansCount":0,"isSign":true,"isHaveCharged":true,"remainLuckDrawCount":2,"inviteCode":"99FOI","h5Link_Lottery":"http://www.nicoacg1.com/lottery/app/2445d50ae6e3b5b0afa9f7922ad46b78","h5Link_Welfare":"http://www.nicoacg1.com/promotion/app/99FOI","h5Link_AcoinDescription":"http://www.nicoacg1.com/common/explain.html#ios@acoin","messageCount":0,"authenticateStatus":0}
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
         UserLevel：1，普通会员；2，Vip；3，Vip已过期
          UserIdentify：身份标识（0:未标识，1：F娘，2：搬运工）
         TotalACoin：总A币数量
         TotalIncome:总收益 |
         IsHaveNewIncome:是否有新收益
         AttentionCount：关注数
         FansCount：粉丝数
         IsSign:是否签到
         RemainLuckDrawCount：剩余抽奖次数
         IsHaveCharged:是否充值过
         AlertText： 提示文本
         vipExpirationTime：VIP过期时间
         AccountStatus：账号状态（1：正常，2：封停，3：A币冻结，4：账号禁言，5：账号封禁，6：A币冻结+账号禁言,7：A币冻结+账号封禁，8:账号禁言+账号封禁，9：A币冻结+账号禁言+账号封禁）
         h5Link_AcoinDescription：A币说明H5链接
         h5Link_Lottery：抽奖H5链接
         h5Link_Welfare：福利H5链接
         AuthenticateStatus：认证状态（0:未提交；1：待审核；2：审核通过；3：审核失败)
         */

        private int memberID;
        private int totalACoin;
        private String totalIncome;
        private int userLevel;
        private String userName;
        private String photo;
        private String areaCode;
        private String phone;
        private String vipExpirationTime;
        private String alertText;
        private String promotionLink;
        private int userIdentify;
        private int accountStatus;
        private boolean isHaveNewIncome;
        private int attentionCount;
        private int fansCount;
        private boolean isSign;
        private boolean isHaveCharged;
        private int remainLuckDrawCount;
        private String inviteCode;
        private String h5Link_Lottery;
        private String h5Link_Welfare;
        private String h5Link_AcoinDescription;
        private int messageCount;
        private int authenticateStatus;

        public int getMemberID() {
            return memberID;
        }

        public void setMemberID(int memberID) {
            this.memberID = memberID;
        }

        public int getTotalACoin() {
            return totalACoin;
        }

        public void setTotalACoin(int totalACoin) {
            this.totalACoin = totalACoin;
        }

        public String getTotalIncome() {
            return totalIncome;
        }

        public void setTotalIncome(String totalIncome) {
            this.totalIncome = totalIncome;
        }

        public int getUserLevel() {
            return userLevel;
        }

        public void setUserLevel(int userLevel) {
            this.userLevel = userLevel;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getVipExpirationTime() {
            return vipExpirationTime;
        }

        public void setVipExpirationTime(String vipExpirationTime) {
            this.vipExpirationTime = vipExpirationTime;
        }

        public String getAlertText() {
            return alertText;
        }

        public void setAlertText(String alertText) {
            this.alertText = alertText;
        }

        public String getPromotionLink() {
            return promotionLink;
        }

        public void setPromotionLink(String promotionLink) {
            this.promotionLink = promotionLink;
        }

        public int getUserIdentify() {
            return userIdentify;
        }

        public void setUserIdentify(int userIdentify) {
            this.userIdentify = userIdentify;
        }

        public int getAccountStatus() {
            return accountStatus;
        }

        public void setAccountStatus(int accountStatus) {
            this.accountStatus = accountStatus;
        }

        public boolean isIsHaveNewIncome() {
            return isHaveNewIncome;
        }

        public void setIsHaveNewIncome(boolean isHaveNewIncome) {
            this.isHaveNewIncome = isHaveNewIncome;
        }

        public int getAttentionCount() {
            return attentionCount;
        }

        public void setAttentionCount(int attentionCount) {
            this.attentionCount = attentionCount;
        }

        public int getFansCount() {
            return fansCount;
        }

        public void setFansCount(int fansCount) {
            this.fansCount = fansCount;
        }

        public boolean isIsSign() {
            return isSign;
        }

        public void setIsSign(boolean isSign) {
            this.isSign = isSign;
        }

        public boolean isIsHaveCharged() {
            return isHaveCharged;
        }

        public void setIsHaveCharged(boolean isHaveCharged) {
            this.isHaveCharged = isHaveCharged;
        }

        public int getRemainLuckDrawCount() {
            return remainLuckDrawCount;
        }

        public void setRemainLuckDrawCount(int remainLuckDrawCount) {
            this.remainLuckDrawCount = remainLuckDrawCount;
        }

        public String getInviteCode() {
            return inviteCode;
        }

        public void setInviteCode(String inviteCode) {
            this.inviteCode = inviteCode;
        }

        public String getH5Link_Lottery() {
            return h5Link_Lottery;
        }

        public void setH5Link_Lottery(String h5Link_Lottery) {
            this.h5Link_Lottery = h5Link_Lottery;
        }

        public String getH5Link_Welfare() {
            return h5Link_Welfare;
        }

        public void setH5Link_Welfare(String h5Link_Welfare) {
            this.h5Link_Welfare = h5Link_Welfare;
        }

        public String getH5Link_AcoinDescription() {
            return h5Link_AcoinDescription;
        }

        public void setH5Link_AcoinDescription(String h5Link_AcoinDescription) {
            this.h5Link_AcoinDescription = h5Link_AcoinDescription;
        }

        public int getMessageCount() {
            return messageCount;
        }

        public void setMessageCount(int messageCount) {
            this.messageCount = messageCount;
        }

        public int getAuthenticateStatus() {
            return authenticateStatus;
        }

        public void setAuthenticateStatus(int authenticateStatus) {
            this.authenticateStatus = authenticateStatus;
        }
    }
}
