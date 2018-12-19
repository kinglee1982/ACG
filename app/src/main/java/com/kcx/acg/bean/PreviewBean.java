package com.kcx.acg.bean;

import java.util.List;

/**
 */

public class PreviewBean {


    /**
     * errorCode : 200
     * errorMsg : 请求成功
     * returnData : {"product":{"title":"未闻花名面码Cosplay（我们仍未知道那天所看见的花的名字+本间芽衣子+cosplay）","naCode":"na12345833","productID":833,"isUnShelved":false,"description":"帮我实现一个愿望吧","sourceType":1,"level":3,"aCoinPrice":0},"picCount":12,"videoCount":0,"tagList":[],"detailList":[{"productDetailID":9178,"productID":833,"url":"http://192.168.3.30:9001/cosplay_la/7115--096fb015-1133-c89a-f98f-abf6ed7f2e09.jpg","type":1,"thumbnailUrl":""},{"productDetailID":9179,"productID":833,"url":"http://192.168.3.30:9001/cosplay_la/7115--28042059-e133-c89a-f96b-164edf4a67fd.jpg","type":1,"thumbnailUrl":""},{"productDetailID":9180,"productID":833,"url":"http://192.168.3.30:9001/cosplay_la/7115--46bc0705-a133-c89a-f9a7-44b4592cc656.jpg","type":1,"thumbnailUrl":""},{"productDetailID":9181,"productID":833,"url":"http://192.168.3.30:9001/cosplay_la/7115--7328dca1-4133-c89a-f9af-584510ef74e7.jpg","type":1,"thumbnailUrl":""},{"productDetailID":9182,"productID":833,"url":"http://192.168.3.30:9001/cosplay_la/7115--7e913db6-6133-c89a-f981-bbd8386394ef.jpg","type":1,"thumbnailUrl":""},{"productDetailID":9183,"productID":833,"url":"http://192.168.3.30:9001/cosplay_la/7115--8799b9db-6133-c89a-f97d-295aa3284411.jpg","type":1,"thumbnailUrl":""}],"author":{"memberID":5,"nickName":null,"userName":"LRY","photo":"http://192.168.3.30:8888/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07?type=1"},"isAttentionedAuthor":false,"isFavourited":false,"isLogin":true,"loginMemberInfo":{"id":46,"userName":"qubao","nickName":null,"userLevel":1,"disabled":false,"totalACoin":0,"aCoinDisabled":false}}
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
         * product : {"title":"未闻花名面码Cosplay（我们仍未知道那天所看见的花的名字+本间芽衣子+cosplay）","naCode":"na12345833","productID":833,"isUnShelved":false,"description":"帮我实现一个愿望吧","sourceType":1,"level":3,"aCoinPrice":0}
         * picCount : 12
         * videoCount : 0
         * tagList : []
         * detailList : [{"productDetailID":9178,"productID":833,"url":"http://192.168.3.30:9001/cosplay_la/7115--096fb015-1133-c89a-f98f-abf6ed7f2e09.jpg","type":1,"thumbnailUrl":""},{"productDetailID":9179,"productID":833,"url":"http://192.168.3.30:9001/cosplay_la/7115--28042059-e133-c89a-f96b-164edf4a67fd.jpg","type":1,"thumbnailUrl":""},{"productDetailID":9180,"productID":833,"url":"http://192.168.3.30:9001/cosplay_la/7115--46bc0705-a133-c89a-f9a7-44b4592cc656.jpg","type":1,"thumbnailUrl":""},{"productDetailID":9181,"productID":833,"url":"http://192.168.3.30:9001/cosplay_la/7115--7328dca1-4133-c89a-f9af-584510ef74e7.jpg","type":1,"thumbnailUrl":""},{"productDetailID":9182,"productID":833,"url":"http://192.168.3.30:9001/cosplay_la/7115--7e913db6-6133-c89a-f981-bbd8386394ef.jpg","type":1,"thumbnailUrl":""},{"productDetailID":9183,"productID":833,"url":"http://192.168.3.30:9001/cosplay_la/7115--8799b9db-6133-c89a-f97d-295aa3284411.jpg","type":1,"thumbnailUrl":""}]
         * author : {"memberID":5,"nickName":null,"userName":"LRY","photo":"http://192.168.3.30:8888/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07?type=1"}
         * isAttentionedAuthor : false
         * isFavourited : false
         * isLogin : true
         * loginMemberInfo : {"id":46,"userName":"qubao","nickName":null,"userLevel":1,"disabled":false,"totalACoin":0,"aCoinDisabled":false}
         */

        private ProductBean product;
        private int picCount;
        private int videoCount;
        private AuthorBean author;
        private boolean isAttentionedAuthor;
        private boolean isFavourited;
        private boolean isLogin;
        private LoginMemberInfoBean loginMemberInfo;
        private int canViewType;

        public int getCanViewType() {
            return canViewType;
        }

        public void setCanViewType(int canViewType) {
            this.canViewType = canViewType;
        }

        private List<TagListBean> tagList;
        private List<DetailListBean> detailList;

        public ProductBean getProduct() {
            return product;
        }

        public void setProduct(ProductBean product) {
            this.product = product;
        }

        public int getPicCount() {
            return picCount;
        }

        public void setPicCount(int picCount) {
            this.picCount = picCount;
        }

        public int getVideoCount() {
            return videoCount;
        }

        public void setVideoCount(int videoCount) {
            this.videoCount = videoCount;
        }

        public AuthorBean getAuthor() {
            return author;
        }

        public void setAuthor(AuthorBean author) {
            this.author = author;
        }

        public boolean isIsAttentionedAuthor() {
            return isAttentionedAuthor;
        }

        public void setIsAttentionedAuthor(boolean isAttentionedAuthor) {
            this.isAttentionedAuthor = isAttentionedAuthor;
        }

        public boolean isIsFavourited() {
            return isFavourited;
        }

        public void setIsFavourited(boolean isFavourited) {
            this.isFavourited = isFavourited;
        }

        public boolean isIsLogin() {
            return isLogin;
        }

        public void setIsLogin(boolean isLogin) {
            this.isLogin = isLogin;
        }

        public LoginMemberInfoBean getLoginMemberInfo() {
            return loginMemberInfo;
        }

        public void setLoginMemberInfo(LoginMemberInfoBean loginMemberInfo) {
            this.loginMemberInfo = loginMemberInfo;
        }

        public List<TagListBean> getTagList() {
            return tagList;
        }

        public void setTagList(List<TagListBean> tagList) {
            this.tagList = tagList;
        }


        public List<DetailListBean> getDetailList() {
            return detailList;
        }

        public void setDetailList(List<DetailListBean> detailList) {
            this.detailList = detailList;
        }

        public static class ProductBean {
            /**
             * title : 未闻花名面码Cosplay（我们仍未知道那天所看见的花的名字+本间芽衣子+cosplay）
             * naCode : na12345833
             * productID : 833
             * isUnShelved : false
             * description : 帮我实现一个愿望吧
             * sourceType : 1
             * level : 3
             * aCoinPrice : 0
             */

            private String title;
            private String naCode;
            private int productID;
            private boolean isUnShelved;
            private String description;
            private int sourceType;
            private int level;
            private int aCoinPrice;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getNaCode() {
                return naCode;
            }

            public void setNaCode(String naCode) {
                this.naCode = naCode;
            }

            public int getProductID() {
                return productID;
            }

            public void setProductID(int productID) {
                this.productID = productID;
            }

            public boolean isIsUnShelved() {
                return isUnShelved;
            }

            public void setIsUnShelved(boolean isUnShelved) {
                this.isUnShelved = isUnShelved;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
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

            public int getACoinPrice() {
                return aCoinPrice;
            }

            public void setACoinPrice(int aCoinPrice) {
                this.aCoinPrice = aCoinPrice;
            }
        }

        public static class AuthorBean {
            /**
             * memberID : 5
             * nickName : null
             * userName : LRY
             * photo : http://192.168.3.30:8888/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07?type=1
             */

            private int memberID;
            private Object nickName;
            private String userName;
            private String photo;
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

            public Object getNickName() {
                return nickName;
            }

            public void setNickName(Object nickName) {
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
        }
        public static class TagListBean {
            /**
             * tagID : 765
             * tagName : 校服
             */

            private int tagID;
            private String tagName;

            public int getTagID() {
                return tagID;
            }

            public void setTagID(int tagID) {
                this.tagID = tagID;
            }

            public String getTagName() {
                return tagName;
            }

            public void setTagName(String tagName) {
                this.tagName = tagName;
            }
        }
        public static class LoginMemberInfoBean {
            /**
             * id : 46
             * userName : qubao
             * nickName : null
             * userLevel : 1
             * disabled : false
             * totalACoin : 0
             * aCoinDisabled : false
             */

            private int id;
            private String userName;
            private Object nickName;
            private int userLevel;
            private boolean disabled;
            private int totalACoin;
            private boolean aCoinDisabled;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public int getUserLevel() {
                return userLevel;
            }

            public void setUserLevel(int userLevel) {
                this.userLevel = userLevel;
            }

            public boolean isDisabled() {
                return disabled;
            }

            public void setDisabled(boolean disabled) {
                this.disabled = disabled;
            }

            public int getTotalACoin() {
                return totalACoin;
            }

            public void setTotalACoin(int totalACoin) {
                this.totalACoin = totalACoin;
            }

            public boolean isACoinDisabled() {
                return aCoinDisabled;
            }

            public void setACoinDisabled(boolean aCoinDisabled) {
                this.aCoinDisabled = aCoinDisabled;
            }
        }

        public static class DetailListBean {
            /**
             * productDetailID : 9178
             * productID : 833
             * url : http://192.168.3.30:9001/cosplay_la/7115--096fb015-1133-c89a-f98f-abf6ed7f2e09.jpg
             * type : 1
             * thumbnailUrl :
             */

            private int productDetailID;
            private int productID;
            private String url;
            private int type;
            private String thumbnailUrl;

            public int getProductDetailID() {
                return productDetailID;
            }

            public void setProductDetailID(int productDetailID) {
                this.productDetailID = productDetailID;
            }

            public int getProductID() {
                return productID;
            }

            public void setProductID(int productID) {
                this.productID = productID;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getThumbnailUrl() {
                return thumbnailUrl;
            }

            public void setThumbnailUrl(String thumbnailUrl) {
                this.thumbnailUrl = thumbnailUrl;
            }
        }
    }
}
