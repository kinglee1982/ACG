package com.kcx.acg.bean;

import java.util.List;

/**
 */

public class HomeBean {


    /**
     * errorCode : 200
     * errorMsg : 请求成功
     * returnData : {"total":536,"list":[{"id":76,"coverPicUrl":"http://192.168.3.30:9001/jueduifucong_image/3373--jueduifucong1-6.jpg","level":1,"naCode":"na1234576","title":"cosplay写真_【K】猫Neko_黑川之吾辈は猫である","isCanView":1},{"id":77,"coverPicUrl":"http://192.168.3.30:9001/jueduifucong_image/3223--jueduifucong01-30.jpg","level":1,"naCode":"na1234577","title":"rinamiCOSPLAY写真套图_博丽灵梦&Saber","isCanView":1},{"id":78,"coverPicUrl":"http://192.168.3.30:9001/jueduifucong_image/3151--jueduifucong1-3.jpg","level":1,"naCode":"na1234578","title":"cosplay小木曾雪菜唯美写真集","isCanView":1},{"id":79,"coverPicUrl":"http://192.168.3.30:9001/jueduifucong_image/3334--jueduifucong01-38.jpg","level":1,"naCode":"na1234579","title":"Ram&Rem红蓝女仆姐妹花cosplay写真套图","isCanView":1},{"id":80,"coverPicUrl":"http://192.168.3.30:9001/jueduifucong_image/5985--20170909232527_59b407e7bba53.jpg","level":1,"naCode":"na1234580","title":"模特李猩一性感写真套图","isCanView":1},{"id":81,"coverPicUrl":"http://192.168.3.30:9001/jueduifucong_image/3088--jueduifucong01-22.jpg","level":1,"naCode":"na1234581","title":"螺旋猫cosplay写真套图舰队C舰娘岛风","isCanView":1},{"id":82,"coverPicUrl":"http://192.168.3.30:9001/jueduifucong_image/2996--jueduifucong01-16.jpg","level":1,"naCode":"na1234582","title":"一骑当千cosplay关羽云长_性感诱惑写真","isCanView":1},{"id":83,"coverPicUrl":"http://192.168.3.30:9001/jueduifucong_image/3322--jueduifucong01-37.jpg","level":1,"naCode":"na1234583","title":"真白ゆきcosplay写真套图合集","isCanView":1},{"id":84,"coverPicUrl":"http://192.168.3.30:9001/jueduifucong_image/3141--jueduifucong1-2.jpg","level":1,"naCode":"na1234584","title":"K-ON秋山澪女仆cosplay写真套图","isCanView":1},{"id":85,"coverPicUrl":"http://192.168.3.30:9001/cosplay_la/3463--044d9150-4133-c640-11a8-19216406fd84.jpg","level":1,"naCode":"na1234585","title":"[爱密社]杨晨晨sugar性感兔女郎黑丝制服诱惑_D.Va cosplay","isCanView":1}]}
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
         * total : 536
         * list : [{"id":76,"coverPicUrl":"http://192.168.3.30:9001/jueduifucong_image/3373--jueduifucong1-6.jpg","level":1,"naCode":"na1234576","title":"cosplay写真_【K】猫Neko_黑川之吾辈は猫である","isCanView":1},{"id":77,"coverPicUrl":"http://192.168.3.30:9001/jueduifucong_image/3223--jueduifucong01-30.jpg","level":1,"naCode":"na1234577","title":"rinamiCOSPLAY写真套图_博丽灵梦&Saber","isCanView":1},{"id":78,"coverPicUrl":"http://192.168.3.30:9001/jueduifucong_image/3151--jueduifucong1-3.jpg","level":1,"naCode":"na1234578","title":"cosplay小木曾雪菜唯美写真集","isCanView":1},{"id":79,"coverPicUrl":"http://192.168.3.30:9001/jueduifucong_image/3334--jueduifucong01-38.jpg","level":1,"naCode":"na1234579","title":"Ram&Rem红蓝女仆姐妹花cosplay写真套图","isCanView":1},{"id":80,"coverPicUrl":"http://192.168.3.30:9001/jueduifucong_image/5985--20170909232527_59b407e7bba53.jpg","level":1,"naCode":"na1234580","title":"模特李猩一性感写真套图","isCanView":1},{"id":81,"coverPicUrl":"http://192.168.3.30:9001/jueduifucong_image/3088--jueduifucong01-22.jpg","level":1,"naCode":"na1234581","title":"螺旋猫cosplay写真套图舰队C舰娘岛风","isCanView":1},{"id":82,"coverPicUrl":"http://192.168.3.30:9001/jueduifucong_image/2996--jueduifucong01-16.jpg","level":1,"naCode":"na1234582","title":"一骑当千cosplay关羽云长_性感诱惑写真","isCanView":1},{"id":83,"coverPicUrl":"http://192.168.3.30:9001/jueduifucong_image/3322--jueduifucong01-37.jpg","level":1,"naCode":"na1234583","title":"真白ゆきcosplay写真套图合集","isCanView":1},{"id":84,"coverPicUrl":"http://192.168.3.30:9001/jueduifucong_image/3141--jueduifucong1-2.jpg","level":1,"naCode":"na1234584","title":"K-ON秋山澪女仆cosplay写真套图","isCanView":1},{"id":85,"coverPicUrl":"http://192.168.3.30:9001/cosplay_la/3463--044d9150-4133-c640-11a8-19216406fd84.jpg","level":1,"naCode":"na1234585","title":"[爱密社]杨晨晨sugar性感兔女郎黑丝制服诱惑_D.Va cosplay","isCanView":1}]
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
             * id : 76
             * coverPicUrl : http://192.168.3.30:9001/jueduifucong_image/3373--jueduifucong1-6.jpg
             * level : 1
             * naCode : na1234576
             * title : cosplay写真_【K】猫Neko_黑川之吾辈は猫である
             * isCanView : 1
             */

            private int id;
            private String coverPicUrl;
            private int level;
            private String naCode;
            private String title;
            private int canViewType;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCoverPicUrl() {
                return coverPicUrl;
            }

            public void setCoverPicUrl(String coverPicUrl) {
                this.coverPicUrl = coverPicUrl;
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

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getCanViewType() {
                return canViewType;
            }

            public void setCanViewType(int canViewType) {
                this.canViewType = canViewType;
            }
        }
    }
}
