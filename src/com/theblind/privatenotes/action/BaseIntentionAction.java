package com.theblind.privatenotes.action;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.ide.scratch.ScratchFileService;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiUtilCore;
import com.theblind.privatenotes.core.PrivateNotesFactory;
import com.theblind.privatenotes.core.service.NoteFileService;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public abstract class BaseIntentionAction implements IntentionAction {
    public String myText = "";

    public NoteFileService noteFileService = PrivateNotesFactory.getNoteFileService();

    public BaseIntentionAction(String myText) {
        this.myText = myText;
    }

    @Override
    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    public String getText() {
        return myText;
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


    public static boolean canModify(PsiElement element) {
        return element.getManager().isInProject(element) || ScratchFileService.isInScratchRoot(PsiUtilCore.getVirtualFile(element));
    }
}

