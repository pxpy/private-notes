package com.theblind.privatenotes.ui;

import javax.swing.*;
import java.awt.*;

public class PrivateNotesEditorForm {
    private JEditorPane editorPane1;
    private JPanel panel1;
    private JScrollPane scrollPane;

    public PrivateNotesEditorForm() {
        panel1.setPreferredSize(new Dimension(200,90));
        scrollPane.setPreferredSize(new Dimension(220,110));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        editorPane1.setPreferredSize(new Dimension(220,110));
        editorPane1.setBorder(null);
    }

    public void top(){
        JScrollBar jScrollBar = scrollPane.getVerticalScrollBar();//获得垂直滚动条
        jScrollBar.setValue(1);//设置垂直滚动条位置
    }
    public JEditorPane getEditorPane1() {

        return editorPane1;
    }

    public JScrollPane getScrollPane() {

        return scrollPane;
    }

    public void setScrollPane(JScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public void setEditorPane1(JEditorPane editorPane1) {

        this.editorPane1 = editorPane1;
    }

    public JPanel getPanel1() {
        return panel1;
    }



    public void setPanel1(JPanel panel1) {
        this.panel1 = panel1;
    }
}
