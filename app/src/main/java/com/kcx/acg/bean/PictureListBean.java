package com.kcx.acg.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zjb on 2018/12/6.
 */
public class PictureListBean implements Serializable {
    private List<PictureBean> pictureList;

    public List<PictureBean> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<PictureBean> pictureList) {
        this.pictureList = pictureList;
    }

}
