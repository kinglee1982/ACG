package com.kcx.acg.bean;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 */

public class TabEntity implements CustomTabEntity {
    private String tabTitle;
    private int selectedIcon;
    private int unSelectedIcon;

    public TabEntity(String tabTitle, int selectedIcon, int unSelectedIcon) {
        this.tabTitle = tabTitle;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }

    @Override
    public String getTabTitle() {
        return tabTitle;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unSelectedIcon;
    }
}
