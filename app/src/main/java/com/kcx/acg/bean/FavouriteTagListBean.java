package com.kcx.acg.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jb on 2018/9/25.
 */
public class FavouriteTagListBean implements Serializable {


    /**
     * errorCode : 200
     * errorMsg : 请求成功
     * returnData : {"total":30,"list":[{"tagID":56,"name":"制服","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":55,"name":"巨乳","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":54,"name":"女仆","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":53,"name":"内衣","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":52,"name":"胖次","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":51,"name":"性感","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":50,"name":"足控","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":49,"name":"美腿","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":48,"name":"少女映画","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":47,"name":"少女","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":46,"name":"套图","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":45,"name":"丝袜","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":44,"name":"劳力士","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":43,"name":"伯爵","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":42,"name":"沛纳海","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":41,"name":"宝格丽","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":40,"name":" 宝珀","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":39,"name":"欧米茄","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":38,"name":"梵克雅宝","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":37,"name":"朗格","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"}]}
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
         * total : 30
         * list : [{"tagID":56,"name":"制服","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":55,"name":"巨乳","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":54,"name":"女仆","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":53,"name":"内衣","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":52,"name":"胖次","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":51,"name":"性感","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":50,"name":"足控","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":49,"name":"美腿","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":48,"name":"少女映画","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":47,"name":"少女","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":46,"name":"套图","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":45,"name":"丝袜","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":44,"name":"劳力士","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":43,"name":"伯爵","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":42,"name":"沛纳海","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":41,"name":"宝格丽","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":40,"name":" 宝珀","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":39,"name":"欧米茄","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":38,"name":"梵克雅宝","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"},{"tagID":37,"name":"朗格","tagPhoto":"http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg"}]
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
             * tagID : 56
             * name : 制服
             * tagPhoto : http://192.168.3.30:9001/banciyuan_image/6595462278030557448--6fxdfpz0w76ps7f1cda10xy5kabjgmun.jpg
             */

            private int tagID;
            private String name;
            private String tagPhoto;

            public int getTagID() {
                return tagID;
            }

            public void setTagID(int tagID) {
                this.tagID = tagID;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
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
