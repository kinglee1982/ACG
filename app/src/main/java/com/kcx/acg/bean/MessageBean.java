package com.kcx.acg.bean;

import java.io.Serializable;

/**
 * 消息通知实体
 * Created by jb on 2018/9/4.
 */
public class MessageBean implements Serializable {

    private String title;
    private String time;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
