package com.kcx.acg.bean;

import java.util.List;

public class GetMyCouponListBean {

    /**
     * errorCode : 200
     * errorMsg :
     * returnData : {"list":[{"memberCouponID":8,"memberID":46,"orderNo":"","useTime":null,"isUseD":false,"receiveTime":"2018-11-28 16:47:57","coupon":{"amount":"40.1","startTime":"2018-11-27 00:00:00","expirationDate":"2018-12-31 00:00:00","name":"VIP现金抵用券"}}],"total":1}
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
         * list : [{"memberCouponID":8,"memberID":46,"orderNo":"","useTime":null,"isUseD":false,"receiveTime":"2018-11-28 16:47:57","coupon":{"amount":"40.1","startTime":"2018-11-27 00:00:00","expirationDate":"2018-12-31 00:00:00","name":"VIP现金抵用券"}}]
         * total : 1
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
             * memberCouponID : 8
             * memberID : 46
             * orderNo :
             * useTime : null
             * isUseD : false
             * receiveTime : 2018-11-28 16:47:57
             * coupon : {"amount":"40.1","startTime":"2018-11-27 00:00:00","expirationDate":"2018-12-31 00:00:00","name":"VIP现金抵用券"}
             */

            private int memberCouponID;
            private int memberID;
            private String orderNo;
            private Object useTime;
            private boolean isUseD;
            private String receiveTime;
            private CouponBean coupon;

            public int getMemberCouponID() {
                return memberCouponID;
            }

            public void setMemberCouponID(int memberCouponID) {
                this.memberCouponID = memberCouponID;
            }

            public int getMemberID() {
                return memberID;
            }

            public void setMemberID(int memberID) {
                this.memberID = memberID;
            }

            public String getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(String orderNo) {
                this.orderNo = orderNo;
            }

            public Object getUseTime() {
                return useTime;
            }

            public void setUseTime(Object useTime) {
                this.useTime = useTime;
            }

            public boolean isIsUseD() {
                return isUseD;
            }

            public void setIsUseD(boolean isUseD) {
                this.isUseD = isUseD;
            }

            public String getReceiveTime() {
                return receiveTime;
            }

            public void setReceiveTime(String receiveTime) {
                this.receiveTime = receiveTime;
            }

            public CouponBean getCoupon() {
                return coupon;
            }

            public void setCoupon(CouponBean coupon) {
                this.coupon = coupon;
            }

            public static class CouponBean {
                /**
                 * amount : 40.1
                 * startTime : 2018-11-27 00:00:00
                 * expirationDate : 2018-12-31 00:00:00
                 * name : VIP现金抵用券
                 */

                private String amount;
                private String startTime;
                private String expirationDate;
                private String name;

                public String getAmount() {
                    return amount;
                }

                public void setAmount(String amount) {
                    this.amount = amount;
                }

                public String getStartTime() {
                    return startTime;
                }

                public void setStartTime(String startTime) {
                    this.startTime = startTime;
                }

                public String getExpirationDate() {
                    return expirationDate;
                }

                public void setExpirationDate(String expirationDate) {
                    this.expirationDate = expirationDate;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
        }
    }
}
