package com.kcx.acg.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jb on 2018/9/21.
 */
public class FavouriteProductListBean implements Serializable {


    /**
     * errorCode : 200
     * errorMsg :
     * returnData : {"list":[{"canViewType":3,"coverPicUrl":"https://img1.nicoacg1.com/images/cover/201809/7419_f965809efdf84c0c909d611b2c103d10.png","createTime":"2018-10-23 17:16:17","isUnShelved":false,"level":3,"naCode":"Na123454877","productID":7419,"title":"_3_特惠_夏美酱_夏美伊斯塔凛"},{"canViewType":1,"coverPicUrl":"https://img1.nicoacg1.com/images/cover/201809/5266_edbefed11e4743feaf8f24f8d939f0bd.png","createTime":"2018-10-19 17:23:41","isUnShelved":false,"level":1,"naCode":"Na123455266","productID":5266,"title":"1_穹妹兔女郎"},{"canViewType":1,"coverPicUrl":"https://img1.nicoacg1.com/images/cover/201809/5160_93cf9203042e4baf8a6215f36197b16c.png","createTime":"2018-10-19 17:23:24","isUnShelved":false,"level":1,"naCode":"Na123455160","productID":5160,"title":"1_LANCER空气娘_kuuki"}],"total":3}
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
         * list : [{"canViewType":3,"coverPicUrl":"https://img1.nicoacg1.com/images/cover/201809/7419_f965809efdf84c0c909d611b2c103d10.png","createTime":"2018-10-23 17:16:17","isUnShelved":false,"level":3,"naCode":"Na123454877","productID":7419,"title":"_3_特惠_夏美酱_夏美伊斯塔凛"},{"canViewType":1,"coverPicUrl":"https://img1.nicoacg1.com/images/cover/201809/5266_edbefed11e4743feaf8f24f8d939f0bd.png","createTime":"2018-10-19 17:23:41","isUnShelved":false,"level":1,"naCode":"Na123455266","productID":5266,"title":"1_穹妹兔女郎"},{"canViewType":1,"coverPicUrl":"https://img1.nicoacg1.com/images/cover/201809/5160_93cf9203042e4baf8a6215f36197b16c.png","createTime":"2018-10-19 17:23:24","isUnShelved":false,"level":1,"naCode":"Na123455160","productID":5160,"title":"1_LANCER空气娘_kuuki"}]
         * total : 3
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
             * canViewType : 3
             * coverPicUrl : https://img1.nicoacg1.com/images/cover/201809/7419_f965809efdf84c0c909d611b2c103d10.png
             * createTime : 2018-10-23 17:16:17
             * isUnShelved : false
             * level : 3
             * naCode : Na123454877
             * productID : 7419
             * title : _3_特惠_夏美酱_夏美伊斯塔凛
             */

            private int canViewType;
            private String coverPicUrl;
            private String createTime;
            private boolean isUnShelved;
            private int level;
            private String naCode;
            private int productID;
            private String title;

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
        }
    }
}
