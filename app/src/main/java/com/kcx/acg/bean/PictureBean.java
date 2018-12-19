package com.kcx.acg.bean;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;

import java.io.Serializable;

/**
 * Created by zjb on 2018/12/6.
 */
public class PictureBean implements Serializable {

    private TransferObserver transferObserver;
    private int productDetailID;
    private String url;
    private String s3Key;

    public int getProductDetailID() {
        return productDetailID;
    }

    public void setProductDetailID(int productDetailID) {
        this.productDetailID = productDetailID;
    }


    public TransferObserver getTransferObserver() {
        return transferObserver;
    }

    public void setTransferObserver(TransferObserver transferObserver) {
        this.transferObserver = transferObserver;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }


}
