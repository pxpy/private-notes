package com.theblind.privatenotes.core;


import cn.hutool.core.util.StrUtil;

import java.io.File;

public class Config {


    public static final String ROOT_PATH = System.getProperty("user.home") + File.separator + ".privateNotes";
    public static final String DEFAULT_USER_PATH = ROOT_PATH + File.separator + "default";

    final String rootPath = System.getProperty("user.home") + File.separator + ".privateNotes";
    final String defaultUserPath = rootPath + File.separator + "default";

    /**
     * 是否开启 同步
     */
    boolean syncEnabled = false;

    /**
     * 邮箱  用于 登录账号
     */
    String mail;

    /**
     * 默认颜色
     */
    String defaultColor;

    /**
     * 默认字体
     */
    String defaultFont;




    public String getUserSavePath() {
        return StrUtil.isEmpty(mail) ? defaultUserPath:(rootPath + File.separator + mail);
    }

    public boolean isSyncEnabled() {
        return syncEnabled;
    }

    public void setSyncEnabled(boolean syncEnabled) {
        this.syncEnabled = syncEnabled;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(String defaultColor) {
        this.defaultColor = defaultColor;
    }

    public String getDefaultFont() {
        return defaultFont;
    }

    public void setDefaultFont(String defaultFont) {
        this.defaultFont = defaultFont;
    }
}
