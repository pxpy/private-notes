package com.theblind.privatenotes.core.util;

import cn.hutool.core.thread.ThreadUtil;
import com.intellij.notification.*;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.*;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
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


    public static JBPopup showComponent(JComponent body, JComponent focusComponent, String title, Icon cancelIcon) {
        ComponentPopupBuilder componentPopupBuilder = JBPopupFactory.getInstance().createComponentPopupBuilder(body, focusComponent);
        JBPopup popup = componentPopupBuilder.setCancelKeyEnabled(true)
                .setCancelOnOtherWindowOpen(false)
                .setCancelOnWindowDeactivation(false)
                .setTitle(title)
                .setMinSize(new Dimension(200, 200))
                .setCancelButton(new IconButton("关闭", cancelIcon))
                .setResizable(true)
                .setRequestFocus(true)
                .setCancelOnClickOutside(false)
                .setShowBorder(false)
                .setMovable(true).createPopup();
        return popup;
    }

    public static void showComponent(String title, JComponent body, JComponent focusComponent, Editor editor, Icon cancelIcon) {
        JBPopup jbPopup = showComponent(body, focusComponent, title, cancelIcon);
        jbPopup.showInBestPositionFor(editor);
    }

    public static void showBalloon(JComponent jComponent, String title, JBPopupListener jbPopupListener, boolean hideOnClickOutside, boolean dialogMode, Editor editor) {
        JBPopupFactory popupFactory = JBPopupFactory.getInstance();
        Balloon balloon = popupFactory.createDialogBalloonBuilder(jComponent, title)
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
     * @param content 通知内容
     * @param type    warning,info,error
     */

    public static void showNotification(String content, NotificationType type, Project project) {
        //BALLOON：自动消失 NotificationGroup
        Notification notification = new Notification("PNGroup", "Private Notes Message", content, type);
        ThreadUtil.execute(() -> {
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
     *
     * @param placement 选择器放置位置
     * @param lc        需要监听的组件
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


    public static void to(Project project, VirtualFile mapperFile, Integer lineNumber) {
        OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(project, mapperFile);
        Editor editor = FileEditorManager.getInstance(project).openTextEditor(openFileDescriptor, true);

        CaretModel caretModel = editor.getCaretModel();
        LogicalPosition logicalPosition = caretModel.getLogicalPosition();
        logicalPosition.leanForward(true);
        LogicalPosition logical = new LogicalPosition(lineNumber, logicalPosition.column);
        caretModel.moveToLogicalPosition(logical);
        SelectionModel selectionModel = editor.getSelectionModel();
        selectionModel.selectLineAtCaret();
    }
}
