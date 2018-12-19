package com.kcx.acg.bean;

import java.io.Serializable;

/**
 * Created by jb on 2018/9/18.
 */
public class BankCardBean implements Serializable {

    private String url;
    private String cardNum;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

}
