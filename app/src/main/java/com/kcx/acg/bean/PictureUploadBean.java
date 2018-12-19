package com.kcx.acg.bean;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;

import java.io.Serializable;

/**
 * Created by zjb on 2018/11/24.
 */
public class PictureUploadBean implements Serializable {
    private int id;
    private int progress;
    private String imagePath;
    private TransferState state;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public TransferState getState() {
        return state;
    }

    public void setState(TransferState state) {
        this.state = state;
    }


}
