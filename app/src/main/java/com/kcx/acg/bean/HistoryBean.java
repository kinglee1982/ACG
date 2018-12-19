package com.kcx.acg.bean;

public class HistoryBean {

    private boolean flag;
    private GetMyBrowseHistoryBean.ReturnDataBean.ListBean bean;

    public GetMyBrowseHistoryBean.ReturnDataBean.ListBean getBean() {
        return bean;
    }

    public void setBean(GetMyBrowseHistoryBean.ReturnDataBean.ListBean bean) {
        this.bean = bean;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }


}
