package com.kcx.acg.bean;

import java.io.Serializable;
import java.util.List;

/**
 */

public class GetProductInfoBean {


    /**
     * errorCode : 200
     * errorMsg : 请求成功
     * returnData : {"product":{"title":"习呆呆性感cosplay写真套图绯色天使","naCode":"na123451","productID":1,"isUnShelved":false,"description":"empty","sourceType":1,"level":1},"count":18,"picCount":7,"videoCount":11,"tagList":[{"tagID":765,"tagName":"校服"},{"tagID":764,"tagName":"胶带"},{"tagID":737,"tagName":"清新"},{"tagID":736,"tagName":"欧美"},{"tagID":765,"tagName":"校服"},{"tagID":764,"tagName":"胶带"},{"tagID":737,"tagName":"清新"},{"tagID":736,"tagName":"欧美"}],"detailList":[{"productDetailID":43717,"productID":1,"url":"http://192.168.3.30:9001/bcy_video/6596128015795618056.mp4","type":2,"thumbnailUrl":"http://192.168.3.30:9001/jueduifucong_image/6028--sh034.jpg"},{"productDetailID":43718,"productID":1,"url":"http://192.168.3.30:9001/bcy_video/6596148189470916872.mp4","type":2,"thumbnailUrl":"http://192.168.3.30:9001/jueduifucong_image/6028--sh034.jpg"},{"productDetailID":43719,"productID":1,"url":"http://192.168.3.30:9001/bcy_video/6596279862447571213.mp4","type":2,"thumbnailUrl":"http://192.168.3.30:9001/jueduifucong_image/6028--sh034.jpg"},{"productDetailID":43720,"productID":1,"url":"http://192.168.3.30:9001/bcy_video/6596562034165285133.mp4","type":2,"thumbnailUrl":"http://192.168.3.30:9001/jueduifucong_image/6028--sh034.jpg"},{"productDetailID":43722,"productID":1,"url":"http://192.168.3.30:9001/bcy_video/6596128015795618056.mp4","type":2,"thumbnailUrl":"http://192.168.3.30:9001/jueduifucong_image/6028--sh034.jpg"},{"productDetailID":43723,"productID":1,"url":"http://192.168.3.30:9001/bcy_video/6596148189470916872.mp4","type":2,"thumbnailUrl":"http://192.168.3.30:9001/jueduifucong_image/6028--sh034.jpg"},{"productDetailID":43724,"productID":1,"url":"http://192.168.3.30:9001/bcy_video/6596279862447571213.mp4","type":2,"thumbnailUrl":"http://192.168.3.30:9001/jueduifucong_image/6028--sh034.jpg"},{"productDetailID":43725,"productID":1,"url":"http://192.168.3.30:9001/bcy_video/6596562034165285133.mp4","type":2,"thumbnailUrl":"http://192.168.3.30:9001/jueduifucong_image/6028--sh034.jpg"},{"productDetailID":43706,"productID":1,"url":"http://192.168.3.30:9001/bcy_video/6595714898762662147.mp4","type":2,"thumbnailUrl":"http://192.168.3.30:9001/jueduifucong_image/6028--sh034.jpg"},{"productDetailID":43716,"productID":1,"url":"http://192.168.3.30:9001/bcy_video/6595714898762662147.mp4","type":2,"thumbnailUrl":"http://192.168.3.30:9001/jueduifucong_image/6028--sh034.jpg"},{"productDetailID":43721,"productID":1,"url":"http://192.168.3.30:9001/bcy_video/6595714898762662147.mp4","type":2,"thumbnailUrl":"http://192.168.3.30:9001/jueduifucong_image/6028--sh034.jpg"},{"productDetailID":1,"productID":1,"url":"http://192.168.3.30:9001/jueduifucong_image/6128--xidai1.jpg","type":1,"thumbnailUrl":""},{"productDetailID":2,"productID":1,"url":"http://192.168.3.30:9001/jueduifucong_image/6128--xidai2.jpg","type":1,"thumbnailUrl":""},{"productDetailID":3,"productID":1,"url":"http://192.168.3.30:9001/jueduifucong_image/6128--xidai3.jpg","type":1,"thumbnailUrl":""},{"productDetailID":4,"productID":1,"url":"http://192.168.3.30:9001/jueduifucong_image/6128--xidai4.jpg","type":1,"thumbnailUrl":""},{"productDetailID":5,"productID":1,"url":"http://192.168.3.30:9001/jueduifucong_image/6128--xidai5.jpg","type":1,"thumbnailUrl":""},{"productDetailID":6,"productID":1,"url":"http://192.168.3.30:9001/jueduifucong_image/6128--xidai6.jpg","type":1,"thumbnailUrl":""},{"productDetailID":7,"productID":1,"url":"http://192.168.3.30:9001/jueduifucong_image/6128--xidai7.jpg","type":1,"thumbnailUrl":""}],"author":{"memberID":38,"nickName":null,"userName":"阿卡珊瑚","photo":"http://192.168.3.30:8888/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07?type=1"},"isAttentionedAuthor":false,"isLogin":true,"loginMemberInfo":{"id":46,"userName":"qubao","nickName":null,"userLevel":1,"disabled":false},"isFavourited":false}
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
         * product : {"title":"习呆呆性感cosplay写真套图绯色天使","naCode":"na123451","productID":1,"isUnShelved":false,"description":"empty","sourceType":1,"level":1}
         * count : 18
         * picCount : 7
         * videoCount : 11
         * tagList : [{"tagID":765,"tagName":"校服"},{"tagID":764,"tagName":"胶带"},{"tagID":737,"tagName":"清新"},{"tagID":736,"tagName":"欧美"},{"tagID":765,"tagName":"校服"},{"tagID":764,"tagName":"胶带"},{"tagID":737,"tagName":"清新"},{"tagID":736,"tagName":"欧美"}]
         * detailList : [{"productDetailID":43717,"productID":1,"url":"http://192.168.3.30:9001/bcy_video/6596128015795618056.mp4","type":2,"thumbnailUrl":"http://192.168.3.30:9001/jueduifucong_image/6028--sh034.jpg"},{"productDetailID":43718,"productID":1,"url":"http://192.168.3.30:9001/bcy_video/6596148189470916872.mp4","type":2,"thumbnailUrl":"http://192.168.3.30:9001/jueduifucong_image/6028--sh034.jpg"},{"productDetailID":43719,"productID":1,"url":"http://192.168.3.30:9001/bcy_video/6596279862447571213.mp4","type":2,"thumbnailUrl":"http://192.168.3.30:9001/jueduifucong_image/6028--sh034.jpg"},{"productDetailID":43720,"productID":1,"url":"http://192.168.3.30:9001/bcy_video/6596562034165285133.mp4","type":2,"thumbnailUrl":"http://192.168.3.30:9001/jueduifucong_image/6028--sh034.jpg"},{"productDetailID":43722,"productID":1,"url":"http://192.168.3.30:9001/bcy_video/6596128015795618056.mp4","type":2,"thumbnailUrl":"http://192.168.3.30:9001/jueduifucong_image/6028--sh034.jpg"},{"productDetailID":43723,"productID":1,"url":"http://192.168.3.30:9001/bcy_video/6596148189470916872.mp4","type":2,"thumbnailUrl":"http://192.168.3.30:9001/jueduifucong_image/6028--sh034.jpg"},{"productDetailID":43724,"productID":1,"url":"http://192.168.3.30:9001/bcy_video/6596279862447571213.mp4","type":2,"thumbnailUrl":"http://192.168.3.30:9001/jueduifucong_image/6028--sh034.jpg"},{"productDetailID":43725,"productID":1,"url":"http://192.168.3.30:9001/bcy_video/6596562034165285133.mp4","type":2,"thumbnailUrl":"http://192.168.3.30:9001/jueduifucong_image/6028--sh034.jpg"},{"productDetailID":43706,"productID":1,"url":"http://192.168.3.30:9001/bcy_video/6595714898762662147.mp4","type":2,"thumbnailUrl":"http://192.168.3.30:9001/jueduifucong_image/6028--sh034.jpg"},{"productDetailID":43716,"productID":1,"url":"http://192.168.3.30:9001/bcy_video/6595714898762662147.mp4","type":2,"thumbnailUrl":"http://192.168.3.30:9001/jueduifucong_image/6028--sh034.jpg"},{"productDetailID":43721,"productID":1,"url":"http://192.168.3.30:9001/bcy_video/6595714898762662147.mp4","type":2,"thumbnailUrl":"http://192.168.3.30:9001/jueduifucong_image/6028--sh034.jpg"},{"productDetailID":1,"productID":1,"url":"http://192.168.3.30:9001/jueduifucong_image/6128--xidai1.jpg","type":1,"thumbnailUrl":""},{"productDetailID":2,"productID":1,"url":"http://192.168.3.30:9001/jueduifucong_image/6128--xidai2.jpg","type":1,"thumbnailUrl":""},{"productDetailID":3,"productID":1,"url":"http://192.168.3.30:9001/jueduifucong_image/6128--xidai3.jpg","type":1,"thumbnailUrl":""},{"productDetailID":4,"productID":1,"url":"http://192.168.3.30:9001/jueduifucong_image/6128--xidai4.jpg","type":1,"thumbnailUrl":""},{"productDetailID":5,"productID":1,"url":"http://192.168.3.30:9001/jueduifucong_image/6128--xidai5.jpg","type":1,"thumbnailUrl":""},{"productDetailID":6,"productID":1,"url":"http://192.168.3.30:9001/jueduifucong_image/6128--xidai6.jpg","type":1,"thumbnailUrl":""},{"productDetailID":7,"productID":1,"url":"http://192.168.3.30:9001/jueduifucong_image/6128--xidai7.jpg","type":1,"thumbnailUrl":""}]
         * author : {"memberID":38,"nickName":null,"userName":"阿卡珊瑚","photo":"http://192.168.3.30:8888/WebApi/Image/GetImage/2018-08-211288bab21ba54b96988bd42f704d1e07?type=1"}
         * isAttentionedAuthor : false
         * isLogin : true
         * loginMemberInfo : {"id":46,"userName":"qubao","nickName":null,"userLevel":1,"disabled":false}
         * isFavourited : false
         */

        private ProductBean product;
        private int count;
        private int picCount;
        private int videoCount;

        private AuthorBean author;
        private boolean isAttentionedAuthor;
        private boolean isLogin;
        private LoginMemberInfoBean loginMemberInfo;
        private boolean isFavourited;
        private List<TagListBean> tagList;
        private List<DetailListBean> detailList;

        public ProductBean getProduct() {
            return product;
        }

        public void setProduct(ProductBean product) {
            this.product = product;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
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

        public boolean isIsFavourited() {
            return isFavourited;
        }

        public void setIsFavourited(boolean isFavourited) {
            this.isFavourited = isFavourited;
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
             * title : 习呆呆性感cosplay写真套图绯色天使
             * naCode : na123451
             * productID : 1
             * isUnShelved : false
             * description : empty
             * sourceType : 1
             * level : 1
             */

            private String title;
            private String naCode;
            private int productID;
            private boolean isUnShelved;
            private String description;
            private int sourceType;
            private int level;

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
        }

        public static class AuthorBean {
            /**
             * memberID : 38
             * nickName : null
             * userName : 阿卡珊瑚
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

        public static class LoginMemberInfoBean {
            /**
             * id : 46
             * userName : qubao
             * nickName : null
             * userLevel : 1
             * disabled : false
             */

            private int id;
            private String userName;
            private Object nickName;
            private int userLevel;
            private boolean disabled;

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

        public static class DetailListBean implements Serializable{
            /**
             * productDetailID : 43717
             * productID : 1
             * url : http://192.168.3.30:9001/bcy_video/6596128015795618056.mp4
             * type : 2
             * thumbnailUrl : http://192.168.3.30:9001/jueduifucong_image/6028--sh034.jpg
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
