package com.kcx.acg.bean;

import java.io.Serializable;
import java.util.List;

/**
 */

public class HotTagBean {


    /**
     * errorCode : 200
     * errorMsg :
     * returnData : {"list":[{"tagID":782,"tagName":"多人","tagPhoto":""},{"tagID":780,"tagName":"露出","tagPhoto":""}],"total":56}
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
         * list : [{"tagID":782,"tagName":"多人","tagPhoto":""},{"tagID":780,"tagName":"露出","tagPhoto":""}]
         * total : 56
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

        public static class ListBean implements Serializable{
            /**
             * tagID : 782
             * tagName : 多人
             * tagPhoto :
             */

            private int tagID;
            private String tagName;
            private String tagPhoto;

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

            public String getTagPhoto() {
                return tagPhoto;
            }

            public void setTagPhoto(String tagPhoto) {
                this.tagPhoto = tagPhoto;
            }
        }
    }
}
