package com.kcx.acg.bean;


import com.luck.picture.lib.entity.LocalMedia;
import java.io.Serializable;
import java.util.List;

/**
 * Created by zjb on 2018/12/4.
 */
public class LocalMediaBean implements Serializable {
    private List<LocalMedia> localMediaVideoList;

    public List<LocalMedia> getLocalMediaVideoList() {
        return localMediaVideoList;
    }

    public void setLocalMediaVideoList(List<LocalMedia> localMediaVideoList) {
        this.localMediaVideoList = localMediaVideoList;
    }

}
