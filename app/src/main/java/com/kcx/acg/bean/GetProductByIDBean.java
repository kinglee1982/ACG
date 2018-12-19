package com.kcx.acg.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zjb on 2018/12/5.
 */
public class GetProductByIDBean implements Serializable {

    /**
     * errorCode : 200
     * errorMsg :
     * returnData : {"aCoinPrice":123,"auditFailureReason":"","auditState":0,"authorID":68,"buyTimes":0,"commentTimes":0,"coverPicUrl":"https://nicoacg-sg.s3.dualstack.ap-southeast-1.amazonaws.com/images/cover/20181205/bfa5b8fa-c925-4662-ac56-d922946d3392.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20181205T081739Z&X-Amz-SignedHeaders=host&X-Amz-Expires=899&X-Amz-Credential=AKIAJ7JS6CKZ4JP5AMHA%2F20181205%2Fap-southeast-1%2Fs3%2Faws4_request&X-Amz-Signature=692138ca7f1368d1e4445a3d9f90d478dfd4c7b6bfb0b6faefe0f52ea823e362","createTime":"2018-12-05 16:18:07","description":"KKK裤头","favoriteTimes":0,"id":7483,"isNeedPay":true,"isUnShelved":false,"level":0,"naCode":"na1070230827645792256","pictureList":[{"productDetailID":190116,"productID":7483,"thumbnailUrl":"","type":1,"url":"https://nicoacg-sg.s3.dualstack.ap-southeast-1.amazonaws.com/videos/macgn/20181205/bff32d59-5994-4890-bf13-5a008c430258.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20181205T081807Z&X-Amz-SignedHeaders=host&X-Amz-Expires=899&X-Amz-Credential=AKIAJ7JS6CKZ4JP5AMHA%2F20181205%2Fap-southeast-1%2Fs3%2Faws4_request&X-Amz-Signature=bbc878a831424d963a1d021d14d282ad1ee58c4fe7da811eeca801b5caf477ca"}],"productIncomes":"0","productTagList":[{"tagID":735,"tagName":"古风"},{"tagID":747,"tagName":"旗袍"}],"sharedTimes":0,"sourceType":2,"title":"我就是我","videoList":[{"productDetailID":190117,"productID":7483,"thumbnailUrl":"","type":2,"url":"https://nicoacg-sg.s3.dualstack.ap-southeast-1.amazonaws.com/images/macgn/20181205/86f84ade-6638-43cc-a28c-cbbfac463454.mp4?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20181205T081807Z&X-Amz-SignedHeaders=host&X-Amz-Expires=899&X-Amz-Credential=AKIAJ7JS6CKZ4JP5AMHA%2F20181205%2Fap-southeast-1%2Fs3%2Faws4_request&X-Amz-Signature=278ff5833830750798d2c7bc13ba9d2c8882c9256e2baa91ae9610630e6798fc","videoDuration":7360}],"viewTimes":0}
     根据作品ID获取作品信息 返回值说明：
     ID：作品ID,
     Title：作品标题，
     SourceType：来源（1：转载，2：原创），
     Level：作品等级，
     CoverPicUrl：封面，
     NaCode：na号，
     AuditState：审核状态，
     CreateTime：创建时间，
     AuditTime：审核时间，
     Description：描述，
     AuthorID：作者ID，
     ACoinPrice：作品价格，
     IsUnShelved：是否下架，
     UpdateTime：更新时间，
     UpShelfTime：上架时间，
     IsNeedPay：是否需要支付，
     ViewTimes：观看次数，
     FavoriteTimes：收藏次数，
     CommentTimes：评论次数，
     SharedTimes：分享次数
     ，BuyTimes:购买次数，
     ProductIncomes:收益，
     AuditFailureReason:审核失败原因
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
         * aCoinPrice : 123
         * auditFailureReason :
         * auditState : 0
         * authorID : 68
         * buyTimes : 0
         * commentTimes : 0
         * coverPicUrl : https://nicoacg-sg.s3.dualstack.ap-southeast-1.amazonaws.com/images/cover/20181205/bfa5b8fa-c925-4662-ac56-d922946d3392.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20181205T081739Z&X-Amz-SignedHeaders=host&X-Amz-Expires=899&X-Amz-Credential=AKIAJ7JS6CKZ4JP5AMHA%2F20181205%2Fap-southeast-1%2Fs3%2Faws4_request&X-Amz-Signature=692138ca7f1368d1e4445a3d9f90d478dfd4c7b6bfb0b6faefe0f52ea823e362
         * createTime : 2018-12-05 16:18:07
         * description : KKK裤头
         * favoriteTimes : 0
         * id : 7483
         * isNeedPay : true
         * isUnShelved : false
         * level : 0
         * naCode : na1070230827645792256
         * pictureList : [{"productDetailID":190116,"productID":7483,"thumbnailUrl":"","type":1,"url":"https://nicoacg-sg.s3.dualstack.ap-southeast-1.amazonaws.com/videos/macgn/20181205/bff32d59-5994-4890-bf13-5a008c430258.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20181205T081807Z&X-Amz-SignedHeaders=host&X-Amz-Expires=899&X-Amz-Credential=AKIAJ7JS6CKZ4JP5AMHA%2F20181205%2Fap-southeast-1%2Fs3%2Faws4_request&X-Amz-Signature=bbc878a831424d963a1d021d14d282ad1ee58c4fe7da811eeca801b5caf477ca"}]
         * productIncomes : 0
         * productTagList : [{"tagID":735,"tagName":"古风"},{"tagID":747,"tagName":"旗袍"}]
         * sharedTimes : 0
         * sourceType : 2
         * title : 我就是我
         * videoList : [{"productDetailID":190117,"productID":7483,"thumbnailUrl":"","type":2,"url":"https://nicoacg-sg.s3.dualstack.ap-southeast-1.amazonaws.com/images/macgn/20181205/86f84ade-6638-43cc-a28c-cbbfac463454.mp4?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20181205T081807Z&X-Amz-SignedHeaders=host&X-Amz-Expires=899&X-Amz-Credential=AKIAJ7JS6CKZ4JP5AMHA%2F20181205%2Fap-southeast-1%2Fs3%2Faws4_request&X-Amz-Signature=278ff5833830750798d2c7bc13ba9d2c8882c9256e2baa91ae9610630e6798fc","videoDuration":7360}]
         * viewTimes : 0
         */

        private int aCoinPrice;
        private String auditFailureReason;
        private int auditState;
        private int authorID;
        private int buyTimes;
        private int commentTimes;
        private String coverPicUrl;
        private String createTime;
        private String description;
        private int favoriteTimes;
        private int id;
        private boolean isNeedPay;
        private boolean isUnShelved;
        private int level;
        private String naCode;
        private String productIncomes;
        private int sharedTimes;
        private int sourceType;
        private String title;
        private int viewTimes;
        private List<PictureListBean> pictureList;
        private List<ProductTagListBean> productTagList;
        private List<VideoListBean> videoList;

        public int getACoinPrice() {
            return aCoinPrice;
        }

        public void setACoinPrice(int aCoinPrice) {
            this.aCoinPrice = aCoinPrice;
        }

        public String getAuditFailureReason() {
            return auditFailureReason;
        }

        public void setAuditFailureReason(String auditFailureReason) {
            this.auditFailureReason = auditFailureReason;
        }

        public int getAuditState() {
            return auditState;
        }

        public void setAuditState(int auditState) {
            this.auditState = auditState;
        }

        public int getAuthorID() {
            return authorID;
        }

        public void setAuthorID(int authorID) {
            this.authorID = authorID;
        }

        public int getBuyTimes() {
            return buyTimes;
        }

        public void setBuyTimes(int buyTimes) {
            this.buyTimes = buyTimes;
        }

        public int getCommentTimes() {
            return commentTimes;
        }

        public void setCommentTimes(int commentTimes) {
            this.commentTimes = commentTimes;
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getFavoriteTimes() {
            return favoriteTimes;
        }

        public void setFavoriteTimes(int favoriteTimes) {
            this.favoriteTimes = favoriteTimes;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isIsNeedPay() {
            return isNeedPay;
        }

        public void setIsNeedPay(boolean isNeedPay) {
            this.isNeedPay = isNeedPay;
        }

        public boolean isIsUnShelved() {
            return isUnShelved;
        }

        public void setIsUnShelved(boolean isUnShelved) {
            this.isUnShelved = isUnShelved;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getNaCode() {
            return naCode;
        }

        public void setNaCode(String naCode) {
            this.naCode = naCode;
        }

        public String getProductIncomes() {
            return productIncomes;
        }

        public void setProductIncomes(String productIncomes) {
            this.productIncomes = productIncomes;
        }

        public int getSharedTimes() {
            return sharedTimes;
        }

        public void setSharedTimes(int sharedTimes) {
            this.sharedTimes = sharedTimes;
        }

        public int getSourceType() {
            return sourceType;
        }

        public void setSourceType(int sourceType) {
            this.sourceType = sourceType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getViewTimes() {
            return viewTimes;
        }

        public void setViewTimes(int viewTimes) {
            this.viewTimes = viewTimes;
        }

        public List<PictureListBean> getPictureList() {
            return pictureList;
        }

        public void setPictureList(List<PictureListBean> pictureList) {
            this.pictureList = pictureList;
        }

        public List<ProductTagListBean> getProductTagList() {
            return productTagList;
        }

        public void setProductTagList(List<ProductTagListBean> productTagList) {
            this.productTagList = productTagList;
        }

        public List<VideoListBean> getVideoList() {
            return videoList;
        }

        public void setVideoList(List<VideoListBean> videoList) {
            this.videoList = videoList;
        }

        public static class PictureListBean implements Serializable{
            /**
             * productDetailID : 190116
             * productID : 7483
             * thumbnailUrl :
             * type : 1
             * url : https://nicoacg-sg.s3.dualstack.ap-southeast-1.amazonaws.com/videos/macgn/20181205/bff32d59-5994-4890-bf13-5a008c430258.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20181205T081807Z&X-Amz-SignedHeaders=host&X-Amz-Expires=899&X-Amz-Credential=AKIAJ7JS6CKZ4JP5AMHA%2F20181205%2Fap-southeast-1%2Fs3%2Faws4_request&X-Amz-Signature=bbc878a831424d963a1d021d14d282ad1ee58c4fe7da811eeca801b5caf477ca
             */

            private int productDetailID;
            private int productID;
            private String thumbnailUrl;
            private int type;
            private String url;
            private String s3Key;

            public String getS3Key() {
                return s3Key;
            }

            public void setS3Key(String s3Key) {
                this.s3Key = s3Key;
            }


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

            public String getThumbnailUrl() {
                return thumbnailUrl;
            }

            public void setThumbnailUrl(String thumbnailUrl) {
                this.thumbnailUrl = thumbnailUrl;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class ProductTagListBean {
            /**
             * tagID : 735
             * tagName : 古风
             */

            private int tagID;
            private String tagName;
            private int productTagID;

            public int getProductTagID() {
                return productTagID;
            }

            public void setProductTagID(int productTagID) {
                this.productTagID = productTagID;
            }


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

        public static class VideoListBean implements Serializable {
            /**
             * productDetailID : 190117
             * productID : 7483
             * thumbnailUrl :
             * type : 2
             * url : https://nicoacg-sg.s3.dualstack.ap-southeast-1.amazonaws.com/images/macgn/20181205/86f84ade-6638-43cc-a28c-cbbfac463454.mp4?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20181205T081807Z&X-Amz-SignedHeaders=host&X-Amz-Expires=899&X-Amz-Credential=AKIAJ7JS6CKZ4JP5AMHA%2F20181205%2Fap-southeast-1%2Fs3%2Faws4_request&X-Amz-Signature=278ff5833830750798d2c7bc13ba9d2c8882c9256e2baa91ae9610630e6798fc
             * videoDuration : 7360
             */

            private String name;
            private int productDetailID;
            private int productID;
            private String s3Key;
            private Long size;
            private String thumbnailUrl;
            private int type;
            private String url;
            private long videoDuration;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Long getSize() {
                return size;
            }

            public void setSize(Long size) {
                this.size = size;
            }


            public String getS3Key() {
                return s3Key;
            }

            public void setS3Key(String s3Key) {
                this.s3Key = s3Key;
            }


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

            public String getThumbnailUrl() {
                return thumbnailUrl;
            }

            public void setThumbnailUrl(String thumbnailUrl) {
                this.thumbnailUrl = thumbnailUrl;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public long getVideoDuration() {
                return videoDuration;
            }

            public void setVideoDuration(long videoDuration) {
                this.videoDuration = videoDuration;
            }
        }
    }
}
