package com.kcx.acg.bean;

import java.util.List;

/**
 */

public class GetSearchTitleBean {

    /**
     * errorCode : 200
     * errorMsg :
     * returnData : [{"productID":2202,"title":"清纯美女恋与制作人女主角星之迟迟Cosplay图片"},{"productID":1503,"title":"恋与制作人 女主cos"},{"productID":1405,"title":"原作:恋与制作人 角色:女主 Cn:yui金鱼"},{"productID":675,"title":"恋与制作人 许墨"},{"productID":467,"title":"恋与制作人 白起·锐气"}]
     */

    private int errorCode;
    private String errorMsg;
    private List<ReturnDataBean> returnData;

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

    public List<ReturnDataBean> getReturnData() {
        return returnData;
    }

    public void setReturnData(List<ReturnDataBean> returnData) {
        this.returnData = returnData;
    }

    public static class ReturnDataBean {
        /**
         * productID : 2202
         * title : 清纯美女恋与制作人女主角星之迟迟Cosplay图片
         */

        private int productID;
        private String title;

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
