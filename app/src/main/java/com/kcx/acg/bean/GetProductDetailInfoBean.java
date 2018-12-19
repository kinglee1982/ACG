package com.kcx.acg.bean;

import java.util.List;

/**
 */

public class GetProductDetailInfoBean {
    /**
     * errorCode : 200
     * errorMsg : 请求成功
     * returnData : {"product":{"title":"习呆呆性感cosplay写真套图绯色天使","naCode":"na123451","productID":1,"isUnShelved":false,"description":"empty","sourceType":1,"level":1},"count":18,"detailList":[{"productDetailID":1,"productID":1,"url":"http://192.168.3.30:9001/jueduifucong_image/6128--xidai1.jpg","type":1,"thumbnailUrl":""},{"productDetailID":2,"productID":1,"url":"http://192.168.3.30:9001/jueduifucong_image/6128--xidai2.jpg","type":1,"thumbnailUrl":""},{"productDetailID":3,"productID":1,"url":"http://192.168.3.30:9001/jueduifucong_image/6128--xidai3.jpg","type":1,"thumbnailUrl":""},{"productDetailID":4,"productID":1,"url":"http://192.168.3.30:9001/jueduifucong_image/6128--xidai4.jpg","type":1,"thumbnailUrl":""},{"productDetailID":5,"productID":1,"url":"http://192.168.3.30:9001/jueduifucong_image/6128--xidai5.jpg","type":1,"thumbnailUrl":""},{"productDetailID":6,"productID":1,"url":"http://192.168.3.30:9001/jueduifucong_image/6128--xidai6.jpg","type":1,"thumbnailUrl":""},{"productDetailID":7,"productID":1,"url":"http://192.168.3.30:9001/jueduifucong_image/6128--xidai7.jpg","type":1,"thumbnailUrl":""}]}
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
         * detailList : [{"productDetailID":1,"productID":1,"url":"http://192.168.3.30:9001/jueduifucong_image/6128--xidai1.jpg","type":1,"thumbnailUrl":""},{"productDetailID":2,"productID":1,"url":"http://192.168.3.30:9001/jueduifucong_image/6128--xidai2.jpg","type":1,"thumbnailUrl":""},{"productDetailID":3,"productID":1,"url":"http://192.168.3.30:9001/jueduifucong_image/6128--xidai3.jpg","type":1,"thumbnailUrl":""},{"productDetailID":4,"productID":1,"url":"http://192.168.3.30:9001/jueduifucong_image/6128--xidai4.jpg","type":1,"thumbnailUrl":""},{"productDetailID":5,"productID":1,"url":"http://192.168.3.30:9001/jueduifucong_image/6128--xidai5.jpg","type":1,"thumbnailUrl":""},{"productDetailID":6,"productID":1,"url":"http://192.168.3.30:9001/jueduifucong_image/6128--xidai6.jpg","type":1,"thumbnailUrl":""},{"productDetailID":7,"productID":1,"url":"http://192.168.3.30:9001/jueduifucong_image/6128--xidai7.jpg","type":1,"thumbnailUrl":""}]
         */

        private ProductBean product;
        private int count;
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

        public static class DetailListBean {
            /**
             * productDetailID : 1
             * productID : 1
             * url : http://192.168.3.30:9001/jueduifucong_image/6128--xidai1.jpg
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
