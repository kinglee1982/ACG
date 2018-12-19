package com.kcx.acg.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jb on 2018/9/25.
 */
public class UsersFavouriteTagListBean implements Serializable {


    /**
     * errorCode : 200
     * errorMsg :
     * returnData : [{"name":"百达翡丽","tagID":36,"tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"name":"朗格","tagID":37,"tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"name":"梵克雅宝","tagID":38,"tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"name":"欧米茄","tagID":39,"tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"name":" 宝珀","tagID":40,"tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"}]
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
         * name : 百达翡丽
         * tagID : 36
         * tagPhoto : http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg
         */

        private String name;
        private int tagID;
        private String tagPhoto;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getTagID() {
            return tagID;
        }

        public void setTagID(int tagID) {
            this.tagID = tagID;
        }

        public String getTagPhoto() {
            return tagPhoto;
        }

        public void setTagPhoto(String tagPhoto) {
            this.tagPhoto = tagPhoto;
        }
    }
}
