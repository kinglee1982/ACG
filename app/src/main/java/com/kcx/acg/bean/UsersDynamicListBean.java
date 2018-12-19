package com.kcx.acg.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jb on 2018/9/25.
 */
public class UsersDynamicListBean implements Serializable {
    /**
     * errorCode : 200
     * errorMsg : 请求成功
     * returnData : {"list":[{"author":{"memberID":5,"nickName":"","photo":"http://192.168.1.80:8899/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07jpg?type=1","userName":"LRY"},"canViewType":1,"coverPicUrl":"http://192.168.1.80:8060/Imgages/20181008/d19a1ad26255440692d9bb0179ee925f.png","createTime":"2018-09-13 20:33:23","productID":2423,"title":"SS_ElyEE子兽耳萝莉巨乳玉藻前黑丝Cosplay本子福利","type":0},{"author":{"memberID":5,"nickName":"","photo":"http://192.168.1.80:8899/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07jpg?type=1","userName":"LRY"},"canViewType":1,"coverPicUrl":"http://192.168.1.80:8060/Imgages/20181008/99c38366a0ec4320abaddd10af6a9935.png","createTime":"2018-09-13 20:33:23","productID":2431,"title":"SS_缘之空双马尾黑丝萝莉兔女郎春日野穹Cosplay福利图","type":0},{"author":{"memberID":5,"nickName":"","photo":"http://192.168.1.80:8899/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07jpg?type=1","userName":"LRY"},"canViewType":1,"coverPicUrl":"http://192.168.1.80:8060/Imgages/20181008/f376926ed625448fbd8578dcb701bc43.png","createTime":"2018-09-13 20:33:23","productID":3868,"title":"SS_DVA.兔女郎","type":0},{"author":{"memberID":1411,"nickName":"","photo":"80b41752ce924edc8414d29677d43e61.jpg","userName":"神秘人彬彬"},"canViewType":1,"coverPicUrl":"http://192.168.1.80:8060/Imgages/20181008/55a7bfbac4e24383a725043b6b7c1db7.png","createTime":"2018-09-13 20:33:23","productID":2518,"title":"SS_王者荣耀妲己cosplay福利 萝莉百合图片","type":0},{"author":{"memberID":1411,"nickName":"","photo":"80b41752ce924edc8414d29677d43e61.jpg","userName":"神秘人彬彬"},"canViewType":1,"coverPicUrl":"http://192.168.1.80:8060/Imgages/20181008/8d49a30f3bd04953a723ca9ad94035f9.png","createTime":"2018-09-13 20:33:23","productID":2519,"title":"SS_王者荣耀妲己cosplay福利 制服御姐白丝图片","type":0},{"author":{"memberID":1411,"nickName":"","photo":"80b41752ce924edc8414d29677d43e61.jpg","userName":"神秘人彬彬"},"canViewType":1,"coverPicUrl":"http://192.168.1.80:8060/Imgages/20180926/dd58de1db8df4c2c87220dfdf541cd9c.png","createTime":"2018-09-13 20:33:23","productID":3001,"title":"S_王者荣耀cosplay女英雄 王者荣耀妲己cosplay图片","type":0},{"author":{"memberID":1411,"nickName":"","photo":"80b41752ce924edc8414d29677d43e61.jpg","userName":"神秘人彬彬"},"canViewType":1,"coverPicUrl":"http://192.168.1.80:8060/Imgages/20181008/67293597456e4de3bd0bd318f744f0ed.png","createTime":"2018-09-13 20:33:23","productID":3837,"title":"SS_王者荣耀cosplay女仆咖啡妲己美女图片","type":0},{"author":{"memberID":1411,"nickName":"","photo":"80b41752ce924edc8414d29677d43e61.jpg","userName":"神秘人彬彬"},"canViewType":1,"coverPicUrl":"http://192.168.1.80:8060/Imgages/20181008/d45ba78bb05842c5bfc1fe59a07814e6.png","createTime":"2018-09-13 20:33:23","productID":3843,"title":"SS_游戏美女王者荣耀cosplay女仆妲己白丝美腿图片","type":0}],"total":28}
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
         * list : [{"author":{"memberID":5,"nickName":"","photo":"http://192.168.1.80:8899/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07jpg?type=1","userName":"LRY"},"canViewType":1,"coverPicUrl":"http://192.168.1.80:8060/Imgages/20181008/d19a1ad26255440692d9bb0179ee925f.png","createTime":"2018-09-13 20:33:23","productID":2423,"title":"SS_ElyEE子兽耳萝莉巨乳玉藻前黑丝Cosplay本子福利","type":0},{"author":{"memberID":5,"nickName":"","photo":"http://192.168.1.80:8899/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07jpg?type=1","userName":"LRY"},"canViewType":1,"coverPicUrl":"http://192.168.1.80:8060/Imgages/20181008/99c38366a0ec4320abaddd10af6a9935.png","createTime":"2018-09-13 20:33:23","productID":2431,"title":"SS_缘之空双马尾黑丝萝莉兔女郎春日野穹Cosplay福利图","type":0},{"author":{"memberID":5,"nickName":"","photo":"http://192.168.1.80:8899/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07jpg?type=1","userName":"LRY"},"canViewType":1,"coverPicUrl":"http://192.168.1.80:8060/Imgages/20181008/f376926ed625448fbd8578dcb701bc43.png","createTime":"2018-09-13 20:33:23","productID":3868,"title":"SS_DVA.兔女郎","type":0},{"author":{"memberID":1411,"nickName":"","photo":"80b41752ce924edc8414d29677d43e61.jpg","userName":"神秘人彬彬"},"canViewType":1,"coverPicUrl":"http://192.168.1.80:8060/Imgages/20181008/55a7bfbac4e24383a725043b6b7c1db7.png","createTime":"2018-09-13 20:33:23","productID":2518,"title":"SS_王者荣耀妲己cosplay福利 萝莉百合图片","type":0},{"author":{"memberID":1411,"nickName":"","photo":"80b41752ce924edc8414d29677d43e61.jpg","userName":"神秘人彬彬"},"canViewType":1,"coverPicUrl":"http://192.168.1.80:8060/Imgages/20181008/8d49a30f3bd04953a723ca9ad94035f9.png","createTime":"2018-09-13 20:33:23","productID":2519,"title":"SS_王者荣耀妲己cosplay福利 制服御姐白丝图片","type":0},{"author":{"memberID":1411,"nickName":"","photo":"80b41752ce924edc8414d29677d43e61.jpg","userName":"神秘人彬彬"},"canViewType":1,"coverPicUrl":"http://192.168.1.80:8060/Imgages/20180926/dd58de1db8df4c2c87220dfdf541cd9c.png","createTime":"2018-09-13 20:33:23","productID":3001,"title":"S_王者荣耀cosplay女英雄 王者荣耀妲己cosplay图片","type":0},{"author":{"memberID":1411,"nickName":"","photo":"80b41752ce924edc8414d29677d43e61.jpg","userName":"神秘人彬彬"},"canViewType":1,"coverPicUrl":"http://192.168.1.80:8060/Imgages/20181008/67293597456e4de3bd0bd318f744f0ed.png","createTime":"2018-09-13 20:33:23","productID":3837,"title":"SS_王者荣耀cosplay女仆咖啡妲己美女图片","type":0},{"author":{"memberID":1411,"nickName":"","photo":"80b41752ce924edc8414d29677d43e61.jpg","userName":"神秘人彬彬"},"canViewType":1,"coverPicUrl":"http://192.168.1.80:8060/Imgages/20181008/d45ba78bb05842c5bfc1fe59a07814e6.png","createTime":"2018-09-13 20:33:23","productID":3843,"title":"SS_游戏美女王者荣耀cosplay女仆妲己白丝美腿图片","type":0}]
         * total : 28
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
             * author : {"memberID":5,"nickName":"","photo":"http://192.168.1.80:8899/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07jpg?type=1","userName":"LRY"}
             * canViewType : 1
             * coverPicUrl : http://192.168.1.80:8060/Imgages/20181008/d19a1ad26255440692d9bb0179ee925f.png
             * createTime : 2018-09-13 20:33:23
             * productID : 2423
             * title : SS_ElyEE子兽耳萝莉巨乳玉藻前黑丝Cosplay本子福利
             * type : 0
             */

            private AuthorBean author;
            private int canViewType;
            private String coverPicUrl;
            private String createTime;
            private int productID;
            private String title;
            private int type;

            public AuthorBean getAuthor() {
                return author;
            }

            public void setAuthor(AuthorBean author) {
                this.author = author;
            }

            public int getCanViewType() {
                return canViewType;
            }

            public void setCanViewType(int canViewType) {
                this.canViewType = canViewType;
            }

            public String getCoverPicUrl() {
                return coverPicUrl;
            }

            public void setCoverPicUrl(String coverPicUrl) {
                this.coverPicUrl = coverPicUrl;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public int getProductID() {
                return productID;
            }

            public void setProductID(int productID) {
                this.productID = productID;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public static class AuthorBean {
                /**
                 * memberID : 5
                 * nickName :
                 * photo : http://192.168.1.80:8899/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07jpg?type=1
                 * userName : LRY
                 */

                private int memberID;
                private String nickName;
                private String photo;
                private String userName;
                private int userIdentify;

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

                public String getNickName() {
                    return nickName;
                }

                public void setNickName(String nickName) {
                    this.nickName = nickName;
                }

                public String getPhoto() {
                    return photo;
                }

                public void setPhoto(String photo) {
                    this.photo = photo;
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
}
