package com.theblind.privatenotes.ui;

import javax.swing.*;
import java.awt.*;

public class PrivateNotesEditorForm {
    private JEditorPane editorPane1;
    private JPanel panel1;

    public JEditorPane getEditorPane1() {
        panel1.setPreferredSize(new Dimension(200,90));
        editorPane1.setPreferredSize(new Dimension(200,90));
        return editorPane1;
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
