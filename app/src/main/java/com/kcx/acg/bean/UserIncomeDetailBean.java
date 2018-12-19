package com.kcx.acg.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jb on 2018/9/21.
 */
public class UserIncomeDetailBean implements Serializable {


    /**
     * errorCode : 200
     * errorMsg : 请求成功
     * returnData : {"list":[{"createTime":"2018-09-21 10:50:34","income":"200","type":2,"typeName":"2"},{"createTime":"2018-09-21 10:50:26","income":"100","type":1,"typeName":"1"}],"total":2}
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
         * list : [{"createTime":"2018-09-21 10:50:34","income":"200","type":2,"typeName":"2"},{"createTime":"2018-09-21 10:50:26","income":"100","type":1,"typeName":"1"}]
         * total : 2
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
             * createTime : 2018-09-21 10:50:34
             * income : 200
             * type : 2
             * typeName : 2
             */

            private String createTime;
            private String income;
            private int type;
            private String typeName;
            private boolean isIncome;

            public boolean isIncome() {
                return isIncome;
            }

            public void setIncome(boolean income) {
                isIncome = income;
            }


            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getIncome() {
                return income;
            }

            public void setIncome(String income) {
                this.income = income;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }
        }
    }
}
