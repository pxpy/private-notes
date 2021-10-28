package com.theblind.privatenotes.core.util;

import cn.hutool.core.thread.ThreadUtil;
import com.intellij.notification.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.JBPopupListener;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.SyntheticElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class IdeaApiUtil {



    public static void showBalloon(JComponent jComponent, String title, JBPopupListener jbPopupListener, Editor editor) {
        IdeaApiUtil.showBalloon(jComponent, title, jbPopupListener, true, true, editor);
    }


    public static void showBalloon(JComponent jComponent, String title, JBPopupListener jbPopupListener, boolean hideOnClickOutside, boolean dialogMode, Editor editor) {
        final JBPopupFactory popupFactory = JBPopupFactory.getInstance();
        final Balloon balloon = popupFactory.createDialogBalloonBuilder(jComponent, title)
                .setHideOnClickOutside(hideOnClickOutside)
                .setDialogMode(dialogMode)
                .createBalloon();
        balloon.addListener(jbPopupListener);
        balloon.show(popupFactory.guessBestPopupLocation(editor), Balloon.Position.below);
    }

    public static Integer getSelLineNumber(@NotNull Editor editor) {
        return editor.getDocument().getLineNumber(editor.getCaretModel().getOffset());
    }

    public static byte[] getBytes(@NotNull VirtualFile virtualFile) throws IOException {
        InputStream inputStream = virtualFile.getInputStream();
        byte[] bytes = new byte[inputStream.available()];
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
            bufferedInputStream.read(bytes);
        }
        return bytes;
    }


    /**
     * 通知
     *
     * @param content     通知内容
     * @param type warning,info,error
     */

    public static void showNotification(String content, NotificationType type, Project project) {
        //BALLOON：自动消失 NotificationGroup
        Notification notification = new Notification("PNGroup", "Private Notes Message", content, type);
        ThreadUtil.execute(()->{
            Notifications.Bus.notify(notification, project);
        });
    }

    public static void showErrNotification(String content, Project project) {
        showNotification(content, NotificationType.ERROR, project);
    }

    public static void showInfoNotification(String content, Project project) {
        showNotification(content, NotificationType.INFORMATION, project);
    }


    /**
     * 颜色选择
     * @param placement 选择器放置位置
     * @param lc 需要监听的组件
     */
    public static void chooseColorListener(JComponent placement, JComponent lc) {
        lc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                final Color chooseColor = JColorChooser.showDialog(
                        placement, "Choose color", lc.getForeground());

                if (null != chooseColor)
                    lc.setForeground(chooseColor);
            }
        });
    }
}
