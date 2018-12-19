package com.kcx.acg.bean;

import java.util.List;

public class GetProductListBean {
    /**
     * errorCode : 200
     * errorMsg :
     * returnData : {"list":[{"id":6565,"title":"_3_舰队少女_美少女战士","sourceType":1,"level":3,"coverPicUrl":"https://img-s.nicoacg2.com/images/cover/201810/6565_bdd842efea6b42db89920275e7416d6c.png","naCode":"","auditState":1,"createTime":"2018-10-09 04:52:01","auditTime":"2018-10-09 04:52:01","description":"","authorID":2167,"aCoinPrice":100,"isUnShelved":true,"updateTime":"2018-10-11 11:08:31","upShelfTime":null,"isNeedPay":true,"viewTimes":0,"favoriteTimes":0,"commentTimes":0,"sharedTimes":0,"buyTimes":0,"productIncomes":"0","auditFailureReason":""},{"id":6564,"title":"_3_舰队少女_红色高跟鞋","sourceType":1,"level":3,"coverPicUrl":"https://img-s.nicoacg2.com/images/cover/201810/6564_ddb7a3fc71e542db9b48ed7d5571bfa6.png","naCode":"","auditState":1,"createTime":"2018-10-09 04:52:00","auditTime":"2018-10-09 04:52:00","description":"","authorID":2167,"aCoinPrice":200,"isUnShelved":true,"updateTime":"2018-10-11 11:10:16","upShelfTime":null,"isNeedPay":true,"viewTimes":0,"favoriteTimes":0,"commentTimes":0,"sharedTimes":0,"buyTimes":0,"productIncomes":"0","auditFailureReason":""}],"total":35}
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
         * list : [{"id":6565,"title":"_3_舰队少女_美少女战士","sourceType":1,"level":3,"coverPicUrl":"https://img-s.nicoacg2.com/images/cover/201810/6565_bdd842efea6b42db89920275e7416d6c.png","naCode":"","auditState":1,"createTime":"2018-10-09 04:52:01","auditTime":"2018-10-09 04:52:01","description":"","authorID":2167,"aCoinPrice":100,"isUnShelved":true,"updateTime":"2018-10-11 11:08:31","upShelfTime":null,"isNeedPay":true,"viewTimes":0,"favoriteTimes":0,"commentTimes":0,"sharedTimes":0,"buyTimes":0,"productIncomes":"0","auditFailureReason":""},{"id":6564,"title":"_3_舰队少女_红色高跟鞋","sourceType":1,"level":3,"coverPicUrl":"https://img-s.nicoacg2.com/images/cover/201810/6564_ddb7a3fc71e542db9b48ed7d5571bfa6.png","naCode":"","auditState":1,"createTime":"2018-10-09 04:52:00","auditTime":"2018-10-09 04:52:00","description":"","authorID":2167,"aCoinPrice":200,"isUnShelved":true,"updateTime":"2018-10-11 11:10:16","upShelfTime":null,"isNeedPay":true,"viewTimes":0,"favoriteTimes":0,"commentTimes":0,"sharedTimes":0,"buyTimes":0,"productIncomes":"0","auditFailureReason":""}]
         * total : 35
         */

        private int total;
        private List<ListBean> list;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 6565
             * title : _3_舰队少女_美少女战士
             * sourceType : 1
             * level : 3
             * coverPicUrl : https://img-s.nicoacg2.com/images/cover/201810/6565_bdd842efea6b42db89920275e7416d6c.png
             * naCode :
             * auditState : 1
             * createTime : 2018-10-09 04:52:01
             * auditTime : 2018-10-09 04:52:01
             * description :
             * authorID : 2167
             * aCoinPrice : 100
             * isUnShelved : true
             * updateTime : 2018-10-11 11:08:31
             * upShelfTime : null
             * isNeedPay : true
             * viewTimes : 0
             * favoriteTimes : 0
             * commentTimes : 0
             * sharedTimes : 0
             * buyTimes : 0
             * productIncomes : 0
             * auditFailureReason :
             */

            private int id;
            private String title;
            private int sourceType;
            private int level;
            private String coverPicUrl;
            private String naCode;
            private int auditState;
            private String createTime;
            private String auditTime;
            private String description;
            private int authorID;
            private int aCoinPrice;
            private boolean isUnShelved;
            private String updateTime;
            private Object upShelfTime;
            private boolean isNeedPay;
            private int viewTimes;
            private int favoriteTimes;
            private int commentTimes;
            private int sharedTimes;
            private int buyTimes;
            private String productIncomes;
            private String auditFailureReason;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getSourceType() {
                return sourceType;
            }

            public void setSourceType(int sourceType) {
                this.sourceType = sourceType;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public String getCoverPicUrl() {
                return coverPicUrl;
            }

            public void setCoverPicUrl(String coverPicUrl) {
                this.coverPicUrl = coverPicUrl;
            }

            public String getNaCode() {
                return naCode;
            }

            public void setNaCode(String naCode) {
                this.naCode = naCode;
            }

            public int getAuditState() {
                return auditState;
            }

            public void setAuditState(int auditState) {
                this.auditState = auditState;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getAuditTime() {
                return auditTime;
            }

            public void setAuditTime(String auditTime) {
                this.auditTime = auditTime;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public int getAuthorID() {
                return authorID;
            }

            public void setAuthorID(int authorID) {
                this.authorID = authorID;
            }

            public int getACoinPrice() {
                return aCoinPrice;
            }

            public void setACoinPrice(int aCoinPrice) {
                this.aCoinPrice = aCoinPrice;
            }

            public boolean isIsUnShelved() {
                return isUnShelved;
            }

            public void setIsUnShelved(boolean isUnShelved) {
                this.isUnShelved = isUnShelved;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public Object getUpShelfTime() {
                return upShelfTime;
            }

            public void setUpShelfTime(Object upShelfTime) {
                this.upShelfTime = upShelfTime;
            }

            public boolean isIsNeedPay() {
                return isNeedPay;
            }

            public void setIsNeedPay(boolean isNeedPay) {
                this.isNeedPay = isNeedPay;
            }

            public int getViewTimes() {
                return viewTimes;
            }

            public void setViewTimes(int viewTimes) {
                this.viewTimes = viewTimes;
            }

            public int getFavoriteTimes() {
                return favoriteTimes;
            }

            public void setFavoriteTimes(int favoriteTimes) {
                this.favoriteTimes = favoriteTimes;
            }

            public int getCommentTimes() {
                return commentTimes;
            }

            public void setCommentTimes(int commentTimes) {
                this.commentTimes = commentTimes;
            }

            public int getSharedTimes() {
                return sharedTimes;
            }

            public void setSharedTimes(int sharedTimes) {
                this.sharedTimes = sharedTimes;
            }

            public int getBuyTimes() {
                return buyTimes;
            }

            public void setBuyTimes(int buyTimes) {
                this.buyTimes = buyTimes;
            }

            public String getProductIncomes() {
                return productIncomes;
            }

            public void setProductIncomes(String productIncomes) {
                this.productIncomes = productIncomes;
            }

            public String getAuditFailureReason() {
                return auditFailureReason;
            }

            public void setAuditFailureReason(String auditFailureReason) {
                this.auditFailureReason = auditFailureReason;
            }
        }
    }
}
