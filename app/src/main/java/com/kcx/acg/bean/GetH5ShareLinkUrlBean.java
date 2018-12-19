package com.kcx.acg.bean;

/**
 * Created by  o on 2018/10/22.
 */

public class GetH5ShareLinkUrlBean {
    /**
     * errorCode : 200
     * errorMsg : 请求成功
     * returnData : {"title":"1_文芷-VioletWen","content":"","link":"","imgUrl":"http://192.168.1.80:8060/Imgages/20180927/a5a800a8a3c54e66838bf21a1c9a4aa2.png"}
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
         * title : 1_文芷-VioletWen
         * content :
         * link :
         * imgUrl : http://192.168.1.80:8060/Imgages/20180927/a5a800a8a3c54e66838bf21a1c9a4aa2.png
         */

        private String title;
        private String content;
        private String link;
        private String imgUrl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }
}
