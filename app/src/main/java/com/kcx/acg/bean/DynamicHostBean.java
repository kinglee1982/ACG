package com.kcx.acg.bean;

import java.io.Serializable;

/**
 * Created by zjb on 2018/11/6.
 */
public class DynamicHostBean implements Serializable {


    /**
     * errorCode : 200
     * errorMsg : 请求成功
     * returnData : {"countryCode":"86","countryName":"中国","name":"中国百度-安卓","url":"http://www.baidu.com/"}
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
         * countryCode : 86
         * countryName : 中国
         * name : 中国百度-安卓
         * url : http://www.baidu.com/
         */

        private String countryCode;
        private String countryName;
        private String name;
        private String url;

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
