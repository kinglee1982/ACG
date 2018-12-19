package com.kcx.acg.bean;


import com.luck.picture.lib.entity.LocalMedia;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zjb on 2018/12/4.
 */
public class LocalMediaVideoBean implements Serializable {
    private long duration=0;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }


}
