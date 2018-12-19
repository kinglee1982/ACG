package com.kcx.acg.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zjb on 2018/11/14.
 */
public class ReadNoticeBean implements Serializable {


    /**
     * errorCode : 200
     * errorMsg :
     * returnData : {"list":[{"noticeTypeName":"官方消息","id":1,"memberID":0,"noticeType":1,"noticeMessage":"欢迎来到NicoACG，请愉快的玩耍吧！如果喜欢我们的产品，别忘了推荐给你的小伙伴们哦~如果你是作者，投稿请戳 →<a href=\"http://www.baidu.com/\">投稿说明<\/a>","noticeTime":"2017-06-12 00:00:00","noticeRead":true}],"total":1}
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
         * list : [{"noticeTypeName":"官方消息","id":1,"memberID":0,"noticeType":1,"noticeMessage":"欢迎来到NicoACG，请愉快的玩耍吧！如果喜欢我们的产品，别忘了推荐给你的小伙伴们哦~如果你是作者，投稿请戳 →<a href=\"http://www.baidu.com/\">投稿说明<\/a>","noticeTime":"2017-06-12 00:00:00","noticeRead":true}]
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
             * noticeTypeName : 官方消息
             * id : 1
             * memberID : 0
             * noticeType : 1
             * noticeMessage : 欢迎来到NicoACG，请愉快的玩耍吧！如果喜欢我们的产品，别忘了推荐给你的小伙伴们哦~如果你是作者，投稿请戳 →<a href="http://www.baidu.com/">投稿说明</a>
             * noticeTime : 2017-06-12 00:00:00
             * noticeRead : true
             */

            private String noticeTypeName;
            private int id;
            private int memberID;
            private int noticeType;
            private String noticeMessage;
            private String noticeTime;
            private boolean noticeRead;

            public String getNoticeTypeName() {
                return noticeTypeName;
            }

            public void setNoticeTypeName(String noticeTypeName) {
                this.noticeTypeName = noticeTypeName;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getMemberID() {
                return memberID;
            }

            public void setMemberID(int memberID) {
                this.memberID = memberID;
            }

            public int getNoticeType() {
                return noticeType;
            }

            public void setNoticeType(int noticeType) {
                this.noticeType = noticeType;
            }

            public String getNoticeMessage() {
                return noticeMessage;
            }

            public void setNoticeMessage(String noticeMessage) {
                this.noticeMessage = noticeMessage;
            }

            public String getNoticeTime() {
                return noticeTime;
            }

            public void setNoticeTime(String noticeTime) {
                this.noticeTime = noticeTime;
            }

            public boolean isNoticeRead() {
                return noticeRead;
            }

            public void setNoticeRead(boolean noticeRead) {
                this.noticeRead = noticeRead;
            }
        }
    }
}
