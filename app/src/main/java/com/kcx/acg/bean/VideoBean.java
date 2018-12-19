package com.kcx.acg.bean;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;

import java.io.Serializable;

/**
 * Created by zjb on 2018/12/7.
 */
public class VideoBean implements Serializable {

    private TransferObserver transferObserver;
    private int productDetailID;
    private int productID;
    private String s3Key;
    private Long videoDuration;
    private String url;
    private String name;
    private Long size;
    private boolean isNullFile = false;

    public boolean isNullFile() {
        return isNullFile;
    }

    public void setNullFile(boolean nullFile) {
        isNullFile = nullFile;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public TransferObserver getTransferObserver() {
        return transferObserver;
    }

    public void setTransferObserver(TransferObserver transferObserver) {
        this.transferObserver = transferObserver;
    }

    public int getProductDetailID() {
        return productDetailID;
    }

    public void setProductDetailID(int productDetailID) {
        this.productDetailID = productDetailID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public Long getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(Long videoDuration) {
        this.videoDuration = videoDuration;
    }


}
