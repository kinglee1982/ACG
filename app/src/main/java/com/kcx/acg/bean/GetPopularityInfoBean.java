package com.kcx.acg.bean;

import java.util.List;

public class GetPopularityInfoBean {


    /**
     * errorCode : 200
     * errorMsg :
     * returnData : {"list":[{"memberID":62,"userName":"花生","photo":"https://img-s.nicoacg2.com/images/avatar/201810/1343/fc7ba0cebd4843409fb4f2344cf25db4.jpg","popularityCount":1000,"userIdentify":0},{"memberID":47,"userName":"毛虾","photo":"https://img-s.nicoacg2.com/images/avatar/20181025/0020/17431361fca94184bfa424d9a224f19f.jpg","popularityCount":259,"userIdentify":0},{"memberID":2165,"userName":"图哦","photo":"https://img-s.nicoacg2.com/images/default/default.png","popularityCount":147,"userIdentify":0},{"memberID":2082,"userName":"ftt","photo":"https://img-s.nicoacg2.com/images/avatar/20181025/0020/31c46459a4f9423f9f4435a784bd0c45.png","popularityCount":91,"userIdentify":0},{"memberID":73,"userName":"小乔","photo":"https://img-s.nicoacg2.com/images/avatar/201810/1623/7ab9ff1d00904842adafb77848e087ce.jpg","popularityCount":85,"userIdentify":0},{"memberID":1113,"userName":"&巴黎","photo":"https://img-s.nicoacg2.com/images/avatar/20181025/0020/c5725e6bdde442fc8c4f7871ef1149da.jpeg","popularityCount":85,"userIdentify":1},{"memberID":69,"userName":"testsql","photo":"https://img-s.nicoacg2.com/images/avatar/201811/1618/2b919446f5a14854b3efd4981cd287e7.jpg","popularityCount":84,"userIdentify":1},{"memberID":50,"userName":"猛龙过江","photo":"https://img-s.nicoacg2.com/images/avatar/201810/1220/1ea7af5f9c4f4512af085981eb946164.jpg","popularityCount":71,"userIdentify":0},{"memberID":52,"userName":"Fatsam","photo":"https://img-s.nicoacg2.com/images/avatar/201811/1523/af686a30095c4c8cb2f7ce798a946503.jpg","popularityCount":68,"userIdentify":0},{"memberID":2168,"userName":"不","photo":"https://img-s.nicoacg2.com/images/default/default.png","popularityCount":63,"userIdentify":0},{"memberID":46,"userName":"要你命3000","photo":"https://img-s.nicoacg2.com/images/avatar/20181025/0020/173c9258614e49ae844d98422b4c13a3.jpg","popularityCount":51,"userIdentify":0},{"memberID":64,"userName":"zzz","photo":"https://img-s.nicoacg2.com/images/avatar/20181025/0020/1f38671252ed4c5f97f972778b664472.jpg","popularityCount":45,"userIdentify":0},{"memberID":2096,"userName":"余额","photo":"https://img-s.nicoacg2.com/images/avatar/20181025/0020/3e22e332b37f42fdb462470c9c7deece.png","popularityCount":42,"userIdentify":0},{"memberID":2121,"userName":"校车","photo":"https://img-s.nicoacg2.com/images/avatar/20181025/0020/4ca18518fdcc453991c882cc3ddc240b.jpg","popularityCount":35,"userIdentify":0},{"memberID":2162,"userName":"TestEnvironment","photo":"https://img-s.nicoacg2.com/images/avatar/201811/1004/ed4ac8f714de45c3b7e0968ad9372a2f.jpg","popularityCount":33,"userIdentify":0},{"memberID":2115,"userName":"斌","photo":"https://img-s.nicoacg2.com/images/avatar/20181025/0020/494f75082f8749058e50db2ab6745464.jpeg","popularityCount":28,"userIdentify":0},{"memberID":2069,"userName":"ww","photo":"https://img-s.nicoacg2.com/images/avatar/20181025/0020/2b2be388a709406dac07f7bf9ea7fa6f.jpeg","popularityCount":28,"userIdentify":0},{"memberID":2163,"userName":"Zero","photo":"https://img-s.nicoacg2.com/images/default/default.png","popularityCount":26,"userIdentify":0},{"memberID":2147,"userName":"312345670","photo":"https://img-s.nicoacg2.com/images/avatar/20181025/0020/5bcd0debdb4e4feea04a67b21f3dd8b5.jpeg","popularityCount":21,"userIdentify":0},{"memberID":2088,"userName":"托尼·史塔克の反应堆","photo":"https://img-s.nicoacg2.com/images/avatar/20181025/0020/37bc24af24984b3888a600d4febbad54.jpg","popularityCount":21,"userIdentify":0}],"total":35}
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
         * list : [{"memberID":62,"userName":"花生","photo":"https://img-s.nicoacg2.com/images/avatar/201810/1343/fc7ba0cebd4843409fb4f2344cf25db4.jpg","popularityCount":1000,"userIdentify":0},{"memberID":47,"userName":"毛虾","photo":"https://img-s.nicoacg2.com/images/avatar/20181025/0020/17431361fca94184bfa424d9a224f19f.jpg","popularityCount":259,"userIdentify":0},{"memberID":2165,"userName":"图哦","photo":"https://img-s.nicoacg2.com/images/default/default.png","popularityCount":147,"userIdentify":0},{"memberID":2082,"userName":"ftt","photo":"https://img-s.nicoacg2.com/images/avatar/20181025/0020/31c46459a4f9423f9f4435a784bd0c45.png","popularityCount":91,"userIdentify":0},{"memberID":73,"userName":"小乔","photo":"https://img-s.nicoacg2.com/images/avatar/201810/1623/7ab9ff1d00904842adafb77848e087ce.jpg","popularityCount":85,"userIdentify":0},{"memberID":1113,"userName":"&巴黎","photo":"https://img-s.nicoacg2.com/images/avatar/20181025/0020/c5725e6bdde442fc8c4f7871ef1149da.jpeg","popularityCount":85,"userIdentify":1},{"memberID":69,"userName":"testsql","photo":"https://img-s.nicoacg2.com/images/avatar/201811/1618/2b919446f5a14854b3efd4981cd287e7.jpg","popularityCount":84,"userIdentify":1},{"memberID":50,"userName":"猛龙过江","photo":"https://img-s.nicoacg2.com/images/avatar/201810/1220/1ea7af5f9c4f4512af085981eb946164.jpg","popularityCount":71,"userIdentify":0},{"memberID":52,"userName":"Fatsam","photo":"https://img-s.nicoacg2.com/images/avatar/201811/1523/af686a30095c4c8cb2f7ce798a946503.jpg","popularityCount":68,"userIdentify":0},{"memberID":2168,"userName":"不","photo":"https://img-s.nicoacg2.com/images/default/default.png","popularityCount":63,"userIdentify":0},{"memberID":46,"userName":"要你命3000","photo":"https://img-s.nicoacg2.com/images/avatar/20181025/0020/173c9258614e49ae844d98422b4c13a3.jpg","popularityCount":51,"userIdentify":0},{"memberID":64,"userName":"zzz","photo":"https://img-s.nicoacg2.com/images/avatar/20181025/0020/1f38671252ed4c5f97f972778b664472.jpg","popularityCount":45,"userIdentify":0},{"memberID":2096,"userName":"余额","photo":"https://img-s.nicoacg2.com/images/avatar/20181025/0020/3e22e332b37f42fdb462470c9c7deece.png","popularityCount":42,"userIdentify":0},{"memberID":2121,"userName":"校车","photo":"https://img-s.nicoacg2.com/images/avatar/20181025/0020/4ca18518fdcc453991c882cc3ddc240b.jpg","popularityCount":35,"userIdentify":0},{"memberID":2162,"userName":"TestEnvironment","photo":"https://img-s.nicoacg2.com/images/avatar/201811/1004/ed4ac8f714de45c3b7e0968ad9372a2f.jpg","popularityCount":33,"userIdentify":0},{"memberID":2115,"userName":"斌","photo":"https://img-s.nicoacg2.com/images/avatar/20181025/0020/494f75082f8749058e50db2ab6745464.jpeg","popularityCount":28,"userIdentify":0},{"memberID":2069,"userName":"ww","photo":"https://img-s.nicoacg2.com/images/avatar/20181025/0020/2b2be388a709406dac07f7bf9ea7fa6f.jpeg","popularityCount":28,"userIdentify":0},{"memberID":2163,"userName":"Zero","photo":"https://img-s.nicoacg2.com/images/default/default.png","popularityCount":26,"userIdentify":0},{"memberID":2147,"userName":"312345670","photo":"https://img-s.nicoacg2.com/images/avatar/20181025/0020/5bcd0debdb4e4feea04a67b21f3dd8b5.jpeg","popularityCount":21,"userIdentify":0},{"memberID":2088,"userName":"托尼·史塔克の反应堆","photo":"https://img-s.nicoacg2.com/images/avatar/20181025/0020/37bc24af24984b3888a600d4febbad54.jpg","popularityCount":21,"userIdentify":0}]
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
             * memberID : 62
             * userName : 花生
             * photo : https://img-s.nicoacg2.com/images/avatar/201810/1343/fc7ba0cebd4843409fb4f2344cf25db4.jpg
             * popularityCount : 1000
             * userIdentify : 0
             */

            private int memberID;
            private String userName;
            private String photo;
            private int popularityCount;
            private int userIdentify;

            public int getMemberID() {
                return memberID;
            }

            public void setMemberID(int memberID) {
                this.memberID = memberID;
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

            public int getPopularityCount() {
                return popularityCount;
            }

            public void setPopularityCount(int popularityCount) {
                this.popularityCount = popularityCount;
            }

            public int getUserIdentify() {
                return userIdentify;
            }

            public void setUserIdentify(int userIdentify) {
                this.userIdentify = userIdentify;
            }
        }
    }
}
