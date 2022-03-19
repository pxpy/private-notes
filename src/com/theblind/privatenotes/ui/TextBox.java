package com.theblind.privatenotes.ui;

import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.*;

public class TextBox {
    private JEditorPane editorPane1;
    private JPanel panel;
    private JScrollPane scroll;
    private JPanel topPanel;
    private JButton nav;
    private JButton refresh;


    public TextBox(Project project, com.intellij.openapi.vfs.VirtualFile mapperFile, Integer lineNumber) {
        panel.setPreferredSize(new Dimension(400, 180));
        panel.setBorder(null);
        // panel.setBorder(new RoundBorder());

        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setBorder(null);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        editorPane1.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        nav.setBorder(null);
        refresh.setBorder(null);
        nav.setContentAreaFilled(false);//设置按钮透明
        refresh.setContentAreaFilled(false);//设置按钮透明


    }

    public JEditorPane getEditorPane1() {
        return editorPane1;
    }


    public JPanel getPanel() {
        return panel;
    }


    public JScrollPane getScroll() {
        return scroll;
    }


    public JButton getNav() {
        return nav;
    }

    public JButton getRefresh() {
        return refresh;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
