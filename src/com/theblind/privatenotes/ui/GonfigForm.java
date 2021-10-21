package com.theblind.privatenotes.ui;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.util.NlsContexts;
import com.theblind.privatenotes.core.Config;
import com.theblind.privatenotes.core.PrivateNotesFactory;
import com.theblind.privatenotes.core.service.ConfigService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GonfigForm implements SearchableConfigurable {
    final ConfigService configService = PrivateNotesFactory.getConfigService();

    private JPanel mainPane;
    private JTabbedPane tabbedPane1;
    private JTextField markText;
    private JTextField emailText;
    private JLabel markLabel;
    private JLabel emailLabel;
    private JLabel markLabSel;
    private JLabel noteLabSel;


    @NotNull
    @Override
    public String getId() { //组件id
        return "plugins.privateNotes";
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {//组件名称
        return "私人注释";
    }

    @Nullable
    @Override
    public JComponent createComponent() {//创建面板

        Config config = configService.get();
        emailText.setText(config.getEmail());

        noteLabSel.setForeground(Config.asColor(config.getNoteColor()));
        markLabSel.setForeground(Config.asColor(config.getMarkColor()));
        markLabSel.setText(config.getMark());
        markText.setText(config.getMark());
        //设置颜色选择监听
        this.chooseColorListener(noteLabSel);
        this.chooseColorListener(markLabSel);

        markText.getDocument().addDocumentListener(new DocumentListener() {

            void updatePreview() {
                markLabSel.setText(markText.getText());
            }

            @Override
            public void insertUpdate(final DocumentEvent e) {
                updatePreview();
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                updatePreview();
            }

            @Override
            public void changedUpdate(final DocumentEvent e) {
                updatePreview();
            }
        });
        return this.mainPane;
    }

    @Override
    public boolean isModified() {//判断配置是否发生变化
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {//配置中点击确定的时候
        Config config = configService.get();
        config.setEmail(emailText.getText());
        config.setMark(markText.getText());
        config.setNoteColor(Config.byColor(noteLabSel.getForeground()));
        config.setMarkColor(Config.byColor(noteLabSel.getForeground()));
        configService.save(config);
    }



    private void chooseColorListener(final JComponent jComponent) {
        jComponent.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                final Color chooseColor = JColorChooser.showDialog(
                        mainPane, "Choose color", jComponent.getForeground());

                if (null != chooseColor)
                    jComponent.setForeground(chooseColor);
            }
        });
    }

}
