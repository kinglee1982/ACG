package com.kcx.acg.manager;

import com.kcx.acg.base.SysApplication;
import com.kcx.acg.bean.MySettingBean;
import com.kcx.acg.utils.SPUtil;

public class SettingManager {
    private static final String KEY_SYSTEM_SETTINGS = "key_system_settings";

    private static MySettingBean settingBean;
    private static volatile SettingManager settingManager;

    private SettingManager() {

    }

    public static SettingManager getInstances() {
        if (settingManager == null) {
            synchronized (SettingManager.class) {
                if(settingManager == null){
                    settingManager = new SettingManager();
                }
            }
        }
        return settingManager;
    }

    public MySettingBean getSettingBean() {
        return (MySettingBean) SPUtil.readObject(SysApplication.getContext(), KEY_SYSTEM_SETTINGS);
    }

    public void setSettingBean(MySettingBean settingBean) {
        SPUtil.saveObject(SysApplication.getContext(), KEY_SYSTEM_SETTINGS, settingBean);
    }
}
