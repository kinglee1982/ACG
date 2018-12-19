package com.kcx.acg.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zjb on 2018/11/30.
 */
public class SaveProductVO implements Serializable {


    /**
     * aCoinPrice : 0
     * coverPicUrl : string
     * description : string
     * isNeedPay : true
     * productDetails : [{"bucketName":"string","productDetailID":0,"s3URL":"string","thumbnailUrl":"string","type":0}]
     * productID : 0
     * tags : [{"productTagID":0,"tagID":0}]
     * title : string
     */

    private int aCoinPrice;
    private String coverPicUrl;
    private String description;
    private boolean isNeedPay;
    private int productID;
    private String title;
    private List<ProductDetailsBean> productDetails;
    private List<TagsBean> tags;

    public int getACoinPrice() {
        return aCoinPrice;
    }

    public void setACoinPrice(int aCoinPrice) {
        this.aCoinPrice = aCoinPrice;
    }

    public String getCoverPicUrl() {
        return coverPicUrl;
    }

    public void setCoverPicUrl(String coverPicUrl) {
        this.coverPicUrl = coverPicUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIsNeedPay() {
        return isNeedPay;
    }

    public void setIsNeedPay(boolean isNeedPay) {
        this.isNeedPay = isNeedPay;
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

    public List<ProductDetailsBean> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<ProductDetailsBean> productDetails) {
        this.productDetails = productDetails;
    }

    public List<TagsBean> getTags() {
        return tags;
    }

    public void setTags(List<TagsBean> tags) {
        this.tags = tags;
    }

    public static class ProductDetailsBean {
        /**
         * bucketName : string
         * productDetailID : 0
         * s3URL : string
         * thumbnailUrl : string
         * type : 0
         */

        private String s3Key;
        private int productDetailID;
        private int productID;
        private String s3URL;
        private String thumbnailUrl;
        private int type;
        private Long videoDuration;
        private String name;
        private long size;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }


        public int getProductID() {
            return productID;
        }

        public void setProductID(int productID) {
            this.productID = productID;
        }

        public String getS3Key() {
            return s3Key;
        }

        public void setS3Key(String s3Key) {
            this.s3Key = s3Key;
        }

        public Long getVideoDuration() {
            return videoDuration;
        }

        public void setVideoDuration(Long videoDuration) {
            this.videoDuration = videoDuration;
        }

        public int getProductDetailID() {
            return productDetailID;
        }

        public void setProductDetailID(int productDetailID) {
            this.productDetailID = productDetailID;
        }

        public String getS3URL() {
            return s3URL;
        }

        public void setS3URL(String s3URL) {
            this.s3URL = s3URL;
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
    }

    public static class TagsBean {
        /**
         * productTagID : 0
         * tagID : 0
         */

        private int productTagID;
        private int tagID;

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
    }
}
