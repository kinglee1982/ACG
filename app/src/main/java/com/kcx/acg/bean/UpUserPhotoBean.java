package com.kcx.acg.bean;

import java.io.Serializable;

/**
 * Created by jb on 2018/10/8.
 */
public class UpUserPhotoBean implements Serializable {


    /**
     * errorCode : 200
     * errorMsg : 上传成功
     * returnData : {"photoRoute":"http://192.168.3.80:8888/WebApi/Image/GetImage/2018-10-087b6ff0bc2f3b4300b4eb0e55805b2187?type=1"}
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
         * photoRoute : http://192.168.3.80:8888/WebApi/Image/GetImage/2018-10-087b6ff0bc2f3b4300b4eb0e55805b2187?type=1
         */

        private String photoRoute;

        public String getPhotoRoute() {
            return photoRoute;
        }

        public void setPhotoRoute(String photoRoute) {
            this.photoRoute = photoRoute;
        }
    }
}
