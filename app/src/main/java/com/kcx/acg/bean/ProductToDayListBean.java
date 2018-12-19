package com.kcx.acg.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jb on 2018/9/26.
 */
public class ProductToDayListBean implements Serializable {


    /**
     * errorCode : 200
     * errorMsg :
     * returnData : {"list":[{"canViewType":1,"coverPicUrl":"http://192.168.1.80:8060/Imgages/20180927/22edf1f7731f4a54b403cb1d90935196.png","productID":4897,"title":"S_少女制服合集Ⅳ"}],"total":1}
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
         * list : [{"canViewType":1,"coverPicUrl":"http://192.168.1.80:8060/Imgages/20180927/22edf1f7731f4a54b403cb1d90935196.png","productID":4897,"title":"S_少女制服合集Ⅳ"}]
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
             * canViewType : 1
             * coverPicUrl : http://192.168.1.80:8060/Imgages/20180927/22edf1f7731f4a54b403cb1d90935196.png
             * productID : 4897
             * title : S_少女制服合集Ⅳ
             */

            private int canViewType;
            private String coverPicUrl;
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
