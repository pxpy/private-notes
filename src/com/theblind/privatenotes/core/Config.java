package com.theblind.privatenotes.core;


import cn.hutool.core.util.StrUtil;

import java.awt.*;
import java.io.File;

public class Config {
    final String rootPath = System.getProperty("user.home") + File.separator + ".privateNotes";
    final String defaultUserPath = rootPath + File.separator + "default";

    /**
     * 是否开启 同步
     */
    boolean syncEnabled = false;

    /**
     * 邮箱  用于 登录账号
     */
    String email;

    /**
     * 注释颜色
     */
    String noteColor="128,128,128,255";

    /**
     * 注释字体
     */
    String noteFont;

    /**
     * 私人注释标记
     */
    String mark = "◄";

    /**
     *标记颜色
     */
    String markColor = "0,0,255,255";
    


    public String getUserSavePath() {
        return StrUtil.isEmpty(email) ? defaultUserPath : (rootPath + File.separator + email);
    }

    public boolean isSyncEnabled() {
        return syncEnabled;
    }

    public void setSyncEnabled(boolean syncEnabled) {
        this.syncEnabled = syncEnabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNoteColor() {
        return noteColor;
    }

    public void setNoteColor(String noteColor) {
        this.noteColor = noteColor;
    }

    public String getNoteFont() {
        return noteFont;
    }

    public void setNoteFont(String noteFont) {
        this.noteFont = noteFont;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getMarkColor() {
        return markColor;
    }

    public void setMarkColor(String markColor) {
        this.markColor = markColor;
    }

    public static Color asColor(final String color) {
        final String[] rgb = color.split(",");
        return new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]), Integer.parseInt(rgb[3]));
    }

    public static String byColor(final Color color) {
        return String.format("%d,%d,%d,%d", color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
}
