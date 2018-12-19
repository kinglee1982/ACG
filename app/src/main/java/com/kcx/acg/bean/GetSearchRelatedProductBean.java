package com.kcx.acg.bean;

import java.util.List;

/**
 */

public class GetSearchRelatedProductBean {

    /**
     * errorCode : 200
     * errorMsg :
     * returnData : {"list":[{"productID":5273,"title":"螺旋猫tomiaCOS_Rapunzel","coverPicUrl":"http://192.168.3.30:8060/Imgages\\20180926\\e8d677188e024f2cb97fe4408587b3c3.png","canViewType":1},{"productID":5227,"title":"文芷-VioletWen","coverPicUrl":"http://192.168.3.30:8060/1ba19e49-c00d-48d0-a97a-f5f4c5e00d8f","canViewType":1},{"productID":5220,"title":"尼禄_正裝Formal","coverPicUrl":"http://192.168.3.30:8060/Imgages\\20180927\\b775b7ddac5e4302b7131f14e09165c8.png","canViewType":1},{"productID":5216,"title":"夏美酱&柳侑绮_cosplay龙女仆","coverPicUrl":"http://192.168.3.30:8060/c4f785cc-d7f8-489f-9370-5b05c32fae1f","canViewType":1},{"productID":5212,"title":"合照-Multi","coverPicUrl":"http://192.168.3.30:8060/Imgages\\20180927\\ffe0adbd43bd4dacbc5dc15049bc538b.png","canViewType":1},{"productID":5207,"title":"偶像大师灰姑娘女孩渋谷凛-cn矮乐多Aliga","coverPicUrl":"http://192.168.3.30:8060/e2fcad25-c716-4571-bda4-8c726115ddb1","canViewType":1},{"productID":5201,"title":"三三play_超级索尼子","coverPicUrl":"http://192.168.3.30:8060/Imgages\\20180927\\73b6bf777aca4c77bd7c515526a942f4.png","canViewType":1},{"productID":5196,"title":"もぎゅっと\u201clove\u201dで接近中！","coverPicUrl":"http://192.168.3.30:8060/2e8fdf58-8366-4fec-bfbd-4212b9ff831a","canViewType":1},{"productID":5194,"title":"UZUKI COLLECTIONⅣ","coverPicUrl":"http://192.168.3.30:8060/bc0bffa7-11bb-47a4-b952-c55b6fdc0008","canViewType":1},{"productID":5193,"title":"UZUKI COLLECTIONⅢ","coverPicUrl":"http://192.168.3.30:8060/0e7403aa-67fa-46c6-a742-aed85d527293","canViewType":1}],"total":2820}
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
         * list : [{"productID":5273,"title":"螺旋猫tomiaCOS_Rapunzel","coverPicUrl":"http://192.168.3.30:8060/Imgages\\20180926\\e8d677188e024f2cb97fe4408587b3c3.png","canViewType":1},{"productID":5227,"title":"文芷-VioletWen","coverPicUrl":"http://192.168.3.30:8060/1ba19e49-c00d-48d0-a97a-f5f4c5e00d8f","canViewType":1},{"productID":5220,"title":"尼禄_正裝Formal","coverPicUrl":"http://192.168.3.30:8060/Imgages\\20180927\\b775b7ddac5e4302b7131f14e09165c8.png","canViewType":1},{"productID":5216,"title":"夏美酱&柳侑绮_cosplay龙女仆","coverPicUrl":"http://192.168.3.30:8060/c4f785cc-d7f8-489f-9370-5b05c32fae1f","canViewType":1},{"productID":5212,"title":"合照-Multi","coverPicUrl":"http://192.168.3.30:8060/Imgages\\20180927\\ffe0adbd43bd4dacbc5dc15049bc538b.png","canViewType":1},{"productID":5207,"title":"偶像大师灰姑娘女孩渋谷凛-cn矮乐多Aliga","coverPicUrl":"http://192.168.3.30:8060/e2fcad25-c716-4571-bda4-8c726115ddb1","canViewType":1},{"productID":5201,"title":"三三play_超级索尼子","coverPicUrl":"http://192.168.3.30:8060/Imgages\\20180927\\73b6bf777aca4c77bd7c515526a942f4.png","canViewType":1},{"productID":5196,"title":"もぎゅっと\u201clove\u201dで接近中！","coverPicUrl":"http://192.168.3.30:8060/2e8fdf58-8366-4fec-bfbd-4212b9ff831a","canViewType":1},{"productID":5194,"title":"UZUKI COLLECTIONⅣ","coverPicUrl":"http://192.168.3.30:8060/bc0bffa7-11bb-47a4-b952-c55b6fdc0008","canViewType":1},{"productID":5193,"title":"UZUKI COLLECTIONⅢ","coverPicUrl":"http://192.168.3.30:8060/0e7403aa-67fa-46c6-a742-aed85d527293","canViewType":1}]
         * total : 2820
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
             * productID : 5273
             * title : 螺旋猫tomiaCOS_Rapunzel
             * coverPicUrl : http://192.168.3.30:8060/Imgages\20180926\e8d677188e024f2cb97fe4408587b3c3.png
             * canViewType : 1
             */

            private int productID;
            private String title;
            private String coverPicUrl;
            private int canViewType;

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

            public String getCoverPicUrl() {
                return coverPicUrl;
            }

            public void setCoverPicUrl(String coverPicUrl) {
                this.coverPicUrl = coverPicUrl;
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
