package com.theblind.privatenotes.core.util;

import com.intellij.notification.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.JBPopupListener;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.SyntheticElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class IdeaApiUtil {

    public static PsiClass getTargetClass(@NotNull Editor editor, @NotNull PsiFile file) {
        int offset = editor.getCaretModel().getOffset();
        PsiElement element = file.findElementAt(offset);
        if (element != null) {
            // 当前类
            PsiClass target = PsiTreeUtil.getParentOfType(element, PsiClass.class);
            return target instanceof SyntheticElement ? null : target;
        }
        return null;
    }

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
     * @param messageType warning,info,error
     * @param displayType NONE：无弹框，不展示,BALLOON：自动消失,STICKY_BALLOON：用户点击关闭按钮消失,TOOL_WINDOW：实例效果同STICKY_BALLOON
     */

    public static void showNotification(String content, MessageType messageType, NotificationDisplayType displayType, boolean isLogByDefault, Project project) {
        NotificationGroup notificationGroup = new NotificationGroup("testid", displayType, isLogByDefault);
        Notification notification = notificationGroup.createNotification(content, messageType);
        Notifications.Bus.notify(notification, project);
    }

    public static void showErrNotification(String content, Project project) {
        NotificationGroup notificationGroup = new NotificationGroup("testid", NotificationDisplayType.BALLOON, true);
        Notification notification = notificationGroup.createNotification(content, MessageType.ERROR);
        Notifications.Bus.notify(notification, project);
    }

    public static void showInfoNotification(String content, Project project) {
        NotificationGroup notificationGroup = new NotificationGroup("testid", NotificationDisplayType.BALLOON, true);
        Notification notification = notificationGroup.createNotification(content, MessageType.INFO);
        Notifications.Bus.notify(notification, project);
    }

}
