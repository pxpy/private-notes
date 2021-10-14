package com.theblind.privatenotes.action;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.SyntheticElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.theblind.privatenotes.core.NoteFile;
import com.theblind.privatenotes.ui.PrivateNotesEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class DeleteNotesIntentionAction extends BaseIntentionAction {

    public DeleteNotesIntentionAction() {
        super("[Note] 删除私人注释");
    }

    @Override
    public boolean startInWriteAction() {
        return false;
    }
    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile psiFile) {
        try {
            return   noteFileService.noteExist(psiFile.getVirtualFile().getCanonicalPath(),
                                                editor.getDocument().getLineNumber(editor.getCaretModel().getOffset()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile psiFile) throws IncorrectOperationException {
        System.out.println("进来了");




    }
}
