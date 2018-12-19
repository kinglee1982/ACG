package com.kcx.acg.bean;

import java.util.List;

public class GetMyBrowseHistoryBean {
    /**
     * errorCode : 200
     * errorMsg :
     * returnData : {"list":[{"id":14,"productID":5271,"deviceID":"84668229289a9123","memberID":46,"createTime":"2018-11-23 09:42:25","productTitle":"1_美遊_サファイア"},{"id":13,"productID":5274,"deviceID":"84668229289a9123","memberID":46,"createTime":"2018-11-23 09:42:23","productTitle":"1_迟菓-SunnyChih"},{"id":12,"productID":5278,"deviceID":"84668229289a9123","memberID":46,"createTime":"2018-11-23 09:42:19","productTitle":"1_鱼丸"}],"total":3}
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
         * list : [{"id":14,"productID":5271,"deviceID":"84668229289a9123","memberID":46,"createTime":"2018-11-23 09:42:25","productTitle":"1_美遊_サファイア"},{"id":13,"productID":5274,"deviceID":"84668229289a9123","memberID":46,"createTime":"2018-11-23 09:42:23","productTitle":"1_迟菓-SunnyChih"},{"id":12,"productID":5278,"deviceID":"84668229289a9123","memberID":46,"createTime":"2018-11-23 09:42:19","productTitle":"1_鱼丸"}]
         * total : 3
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
             * id : 14
             * productID : 5271
             * deviceID : 84668229289a9123
             * memberID : 46
             * createTime : 2018-11-23 09:42:25
             * productTitle : 1_美遊_サファイア
             */

            private int id;
            private int productID;
            private String deviceID;
            private int memberID;
            private String createTime;
            private String productTitle;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getProductID() {
                return productID;
            }

            public void setProductID(int productID) {
                this.productID = productID;
            }

            public String getDeviceID() {
                return deviceID;
            }

            public void setDeviceID(String deviceID) {
                this.deviceID = deviceID;
            }

            public int getMemberID() {
                return memberID;
            }

            public void setMemberID(int memberID) {
                this.memberID = memberID;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getProductTitle() {
                return productTitle;
            }

            public void setProductTitle(String productTitle) {
                this.productTitle = productTitle;
            }
        }
    }
}
