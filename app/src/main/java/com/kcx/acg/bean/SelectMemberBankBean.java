package com.kcx.acg.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jb on 2018/9/20.
 */
public class SelectMemberBankBean implements Serializable {


    /**
     * errorCode : 200
     * errorMsg :
     * returnData : [{"accountNo":"8888","bankID":2,"bankName":"中国农业银行","logo":"http://192.168.3.80:8060/SystemConfigIco/BankLogo/NongYeBank.png","memberBankID":99},{"accountNo":"8888","bankID":6,"bankName":"交通银行","logo":"http://192.168.3.80:8060/SystemConfigIco/BankLogo/JiaoTongBank.png","memberBankID":100}]
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
         * accountNo : 8888
         * bankID : 2
         * bankName : 中国农业银行
         * logo : http://192.168.3.80:8060/SystemConfigIco/BankLogo/NongYeBank.png
         * memberBankID : 99
         */

        private String accountNo;
        private int bankID;
        private String bankName;
        private String logo;
        private int memberBankID;

        public String getAccountNo() {
            return accountNo;
        }

        public void setAccountNo(String accountNo) {
            this.accountNo = accountNo;
        }

        public int getBankID() {
            return bankID;
        }

        public void setBankID(int bankID) {
            this.bankID = bankID;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public int getMemberBankID() {
            return memberBankID;
        }

        public void setMemberBankID(int memberBankID) {
            this.memberBankID = memberBankID;
        }
    }
}
