package com.kcx.acg.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jb on 2018/9/21.
 */
public class UserAttentionListBean implements Serializable {


    /**
     * errorCode : 200
     * errorMsg :
     * returnData : {"list":[{"attentionedMemberID":3,"photo":"http://192.168.3.30:8888/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07?type=1","userLevel":2,"userName":"who we are?"},{"attentionedMemberID":14,"photo":"http://192.168.3.30:8888/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07?type=1","userLevel":1,"userName":"阿姨"}],"total":38}
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
         * list : [{"attentionedMemberID":3,"photo":"http://192.168.3.30:8888/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07?type=1","userLevel":2,"userName":"who we are?"},{"attentionedMemberID":14,"photo":"http://192.168.3.30:8888/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07?type=1","userLevel":1,"userName":"阿姨"}]
         * total : 38
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
             * attentionedMemberID : 3
             * photo : http://192.168.3.30:8888/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07?type=1
             * userLevel : 2
             * userName : who we are?
             */

            private int memberID;
            private String photo;
            private int userLevel;
            private int userIdentify;
            private String userName;
            private boolean isAttentioned;

            public int getUserIdentify() {
                return userIdentify;
            }

            public void setUserIdentify(int userIdentify) {
                this.userIdentify = userIdentify;
            }


            public int getMemberID() {
                return memberID;
            }

            public void setMemberID(int memberID) {
                this.memberID = memberID;
            }

            public boolean isAttentioned() {
                return isAttentioned;
            }

            public void setAttentioned(boolean attentioned) {
                isAttentioned = attentioned;
            }


            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
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
        }
    }
}
