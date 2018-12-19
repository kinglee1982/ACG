package com.kcx.acg.bean;

import java.util.List;

/**
 */

public class GetCommentReplysBean {


    /**
     * errorCode : 200
     * errorMsg : null
     * returnData : {"total":1,"list":[{"replyID":24,"content":"shsh","likeCount":0,"commentUser":{"memberID":46,"userName":"qubao","nickName":null,"photo":"http://192.168.3.30:8888/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07?type=1","isDriver":false,"isPassenger":false},"replyTime":"2018-09-18 15:10:47","isLike":false}]}
     */

    private int errorCode;
    private Object errorMsg;
    private ReturnDataBean returnData;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public Object getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(Object errorMsg) {
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
         * total : 1
         * list : [{"replyID":24,"content":"shsh","likeCount":0,"commentUser":{"memberID":46,"userName":"qubao","nickName":null,"photo":"http://192.168.3.30:8888/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07?type=1","isDriver":false,"isPassenger":false},"replyTime":"2018-09-18 15:10:47","isLike":false}]
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
             * replyID : 24
             * content : shsh
             * likeCount : 0
             * commentUser : {"memberID":46,"userName":"qubao","nickName":null,"photo":"http://192.168.3.30:8888/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07?type=1","isDriver":false,"isPassenger":false}
             * replyTime : 2018-09-18 15:10:47
             * isLike : false
             */

            private int replyID;
            private String content;
            private int likeCount;
            private CommentUserBean commentUser;
            private String replyTime;
            private boolean isLike;


            public int getReplyID() {
                return replyID;
            }

            public void setReplyID(int replyID) {
                this.replyID = replyID;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getLikeCount() {
                return likeCount;
            }

            public void setLikeCount(int likeCount) {
                this.likeCount = likeCount;
            }

            public CommentUserBean getCommentUser() {
                return commentUser;
            }

            public void setCommentUser(CommentUserBean commentUser) {
                this.commentUser = commentUser;
            }

            public String getReplyTime() {
                return replyTime;
            }

            public void setReplyTime(String replyTime) {
                this.replyTime = replyTime;
            }

            public boolean isIsLike() {
                return isLike;
            }

            public void setIsLike(boolean isLike) {
                this.isLike = isLike;
            }

            public static class CommentUserBean {
                /**
                 * memberID : 46
                 * userName : qubao
                 * nickName : null
                 * photo : http://192.168.3.30:8888/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07?type=1
                 * isDriver : false
                 * isPassenger : false
                 */

                private int memberID;
                private String userName;
                private Object nickName;
                private String photo;
                private boolean isDriver;
                private boolean isPassenger;
                private boolean isAuthor;

                public int getUserIdentify() {
                    return userIdentify;
                }

                public void setUserIdentify(int userIdentify) {
                    this.userIdentify = userIdentify;
                }

                private int userIdentify;
                public boolean isAuthor() {
                    return isAuthor;
                }

                public void setAuthor(boolean author) {
                    isAuthor = author;
                }
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

                public Object getNickName() {
                    return nickName;
                }

                public void setNickName(Object nickName) {
                    this.nickName = nickName;
                }

                public String getPhoto() {
                    return photo;
                }

                public void setPhoto(String photo) {
                    this.photo = photo;
                }

                public boolean isIsDriver() {
                    return isDriver;
                }

                public void setIsDriver(boolean isDriver) {
                    this.isDriver = isDriver;
                }

                public boolean isIsPassenger() {
                    return isPassenger;
                }

                public void setIsPassenger(boolean isPassenger) {
                    this.isPassenger = isPassenger;
                }
            }
        }
    }
}
