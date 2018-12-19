package com.kcx.acg.bean;

import java.util.List;

/**
 */

public class GetSearchRelatedUsersBean {

    /**
     * errorCode : 200
     * errorMsg :
     * returnData : {"list":[{"memberID":57,"nickName":"","userName":"Test002","photo":"","isAttentioned":false},{"memberID":56,"nickName":"","userName":"Test001","photo":"","isAttentioned":false},{"memberID":45,"nickName":"","userName":"17759210405","photo":"http://192.168.3.30:8888/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07?type=1","isAttentioned":false},{"memberID":41,"nickName":"","userName":"1000","photo":"http://192.168.3.30:8888/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07?type=1","isAttentioned":false}],"total":4}
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
         * list : [{"memberID":57,"nickName":"","userName":"Test002","photo":"","isAttentioned":false},{"memberID":56,"nickName":"","userName":"Test001","photo":"","isAttentioned":false},{"memberID":45,"nickName":"","userName":"17759210405","photo":"http://192.168.3.30:8888/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07?type=1","isAttentioned":false},{"memberID":41,"nickName":"","userName":"1000","photo":"http://192.168.3.30:8888/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07?type=1","isAttentioned":false}]
         * total : 4
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
             * memberID : 57
             * nickName :
             * userName : Test002
             * photo :
             * isAttentioned : false
             */

            private int memberID;
            private String nickName;
            private String userName;
            private String photo;
            private boolean isAttentioned;
            public int getUserIdentify() {
                return userIdentify;
            }

            public void setUserIdentify(int userIdentify) {
                this.userIdentify = userIdentify;
            }

            private int userIdentify;
            public int getMemberID() {
                return memberID;
            }

            public void setMemberID(int memberID) {
                this.memberID = memberID;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
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

            public boolean isIsAttentioned() {
                return isAttentioned;
            }

            public void setIsAttentioned(boolean isAttentioned) {
                this.isAttentioned = isAttentioned;
            }
        }
    }
}
