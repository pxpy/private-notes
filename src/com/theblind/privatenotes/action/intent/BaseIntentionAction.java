package com.theblind.privatenotes.action.intent;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.theblind.privatenotes.action.ActionHandle;
import com.theblind.privatenotes.action.ActionHandleFactory;
import com.theblind.privatenotes.core.PrivateNotesFactory;
import com.theblind.privatenotes.core.service.NoteFileService;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public abstract class BaseIntentionAction implements IntentionAction {
    public String myText;
    ActionHandle actionHandle;

    public BaseIntentionAction(ActionHandle.Operate operate) {
        this.myText = operate.getTitle();
        actionHandle= ActionHandleFactory.getActionHandle(operate);
    }

    @Override
    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    public String getText() {
        return myText;
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile psiFile) {
        return actionHandle.isVisible(project, editor,  psiFile.getVirtualFile());
    }

    @NotNull
    @Override
    public @IntentionFamilyName String getFamilyName() {
        return "私有注释";
    }


    @Override
    public boolean startInWriteAction() {
        return false;
    }

    @Override
    public String toString() {
        return getText();
    }


    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile psiFile) {
        actionHandle.execute(project, editor, psiFile.getVirtualFile());
    }

}

