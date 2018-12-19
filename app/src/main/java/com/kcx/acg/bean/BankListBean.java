package com.kcx.acg.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jb on 2018/9/20.
 */
public class BankListBean implements Serializable {


    /**
     * errorCode : 200
     * errorMsg : 请求成功
     * returnData : [{"bankID":1,"logo":"http://192.168.3.80:8060/SystemConfigIco/BankLogo/GongShangBank.png","name":"中国工商银行"},{"bankID":2,"logo":"http://192.168.3.80:8060/SystemConfigIco/BankLogo/NongYeBank.png","name":"中国农业银行"},{"bankID":3,"logo":"http://192.168.3.80:8060/SystemConfigIco/BankLogo/ZhongGuoBank.png","name":"中国银行"},{"bankID":4,"logo":"http://192.168.3.80:8060/SystemConfigIco/BankLogo/JianSheBank.png","name":"中国建设银行"},{"bankID":5,"logo":"http://192.168.3.80:8060/SystemConfigIco/BankLogo/YouZhengBank.png","name":"中国邮政储蓄银行"},{"bankID":6,"logo":"http://192.168.3.80:8060/SystemConfigIco/BankLogo/JiaoTongBank.png","name":"交通银行"},{"bankID":7,"logo":"http://192.168.3.80:8060/SystemConfigIco/BankLogo/ZhaoShangBank.png","name":"招商银行"},{"bankID":8,"logo":"http://192.168.3.80:8060/SystemConfigIco/BankLogo/PuFaBank.png","name":"浦发银行"}]
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
         * bankID : 1
         * logo : http://192.168.3.80:8060/SystemConfigIco/BankLogo/GongShangBank.png
         * name : 中国工商银行
         */

        private int bankID;
        private String logo;
        private String name;

        public int getBankID() {
            return bankID;
        }

        public void setBankID(int bankID) {
            this.bankID = bankID;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
