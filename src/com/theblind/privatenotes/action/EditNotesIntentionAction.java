package com.theblind.privatenotes.action;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
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
import com.intellij.util.IncorrectOperationException;
import com.theblind.privatenotes.core.NoteFile;
import com.theblind.privatenotes.core.util.PrivateNotesUtil;
import com.theblind.privatenotes.ui.PrivateNotesEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

public class EditNotesIntentionAction extends BaseIntentionAction {

    public EditNotesIntentionAction() {
        super("[Note] 编辑私人注释");
    }


    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile psiFile) {
        try {
            return noteFileService.noteExist(psiFile.getVirtualFile().getCanonicalPath(),
                    editor.getDocument().getLineNumber(editor.getCaretModel().getOffset()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile psiFile) throws IncorrectOperationException {
        final PrivateNotesEditor remarkEditor = new PrivateNotesEditor();

        final JEditorPane editorPane = remarkEditor.getEditorPane1();
        int lineNumber = editor.getDocument().getLineNumber(editor.getCaretModel().getOffset());
        final VirtualFile virtualFile = psiFile.getVirtualFile();
        try {

            editorPane.setText(noteFileService.getNote(virtualFile.getCanonicalPath(),
                    editor.getDocument().getLineNumber(editor.getCaretModel().getOffset()),
                    PrivateNotesUtil.getBytes(virtualFile.getInputStream())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        final JBPopupFactory popupFactory = JBPopupFactory.getInstance();
        final Balloon balloon = popupFactory.createDialogBalloonBuilder(remarkEditor.getPanel1(), myText)
                // .setFadeoutTime(5000)
                .setDialogMode(true)
                //.setLayer()
                .setRequestFocus(true)
                .setHideOnClickOutside(true)
                .createBalloon();
        //弹出窗 Balloon
        balloon.show(popupFactory.guessBestPopupLocation(editor), Balloon.Position.below);
        editorPane.requestFocus();

        balloon.addListener(new JBPopupListener() {
            @Override
            public void onClosed(@NotNull LightweightWindowEvent event) {
                try {
                    noteFileService.saveNote(virtualFile.getCanonicalPath(),
                            lineNumber, editorPane.getText(), PrivateNotesUtil.getBytes(virtualFile.getInputStream()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
