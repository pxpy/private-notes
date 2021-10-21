package com.theblind.privatenotes.action;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupListener;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import com.theblind.privatenotes.core.util.IdeaApiUtil;
import com.theblind.privatenotes.core.util.PrivateNotesUtil;
import com.theblind.privatenotes.ui.PrivateNotesEditorForm;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class EditNoteIntentionAction extends BaseIntentionAction {

    public EditNoteIntentionAction() {
        super("[Note] 编辑私人注释");
    }


    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile psiFile) {
        try {
            return noteFileService.noteExist(psiFile.getVirtualFile().getCanonicalPath(),
                    editor.getDocument().getLineNumber(editor.getCaretModel().getOffset()), PrivateNotesUtil.getBytes(psiFile.getVirtualFile().getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile psiFile) throws IncorrectOperationException {
        PrivateNotesEditorForm remarkEditor = new PrivateNotesEditorForm();
        JEditorPane editorPane = remarkEditor.getEditorPane1();
        VirtualFile virtualFile = psiFile.getVirtualFile();
        Integer selLineNumber = IdeaApiUtil.getSelLineNumber(editor);
        try {
            editorPane.setText(noteFileService.getNote(virtualFile.getCanonicalPath(),
                    selLineNumber,
                    IdeaApiUtil.getBytes(virtualFile)));
        } catch (Exception e) {
            PrivateNotesUtil.errLog(e, project);
        }

        IdeaApiUtil.showBalloon(editorPane, myText, new JBPopupListener() {
            @Override
            public void onClosed(@NotNull LightweightWindowEvent event) {
                try {
                    noteFileService.saveNote(virtualFile.getCanonicalPath(),
                            selLineNumber, editorPane.getText(), PrivateNotesUtil.getBytes(virtualFile.getInputStream()));
                } catch (Exception e) {
                    PrivateNotesUtil.errLog(e, project);
                }
            }
        }, editor);
        editorPane.requestFocus();
    }
}
