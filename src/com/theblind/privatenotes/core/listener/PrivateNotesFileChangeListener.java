package com.theblind.privatenotes.core.listener;

import com.intellij.psi.PsiTreeChangeEvent;
import com.intellij.psi.PsiTreeChangeListener;
import com.theblind.privatenotes.core.PrivateNotesFactory;
import com.theblind.privatenotes.core.service.NoteFileService;
import org.jetbrains.annotations.NotNull;

public class PrivateNotesFileChangeListener implements PsiTreeChangeListener {


    NoteFileService noteFileService = PrivateNotesFactory.getNoteFileService();

    @Override
    public void beforeChildAddition(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

    }

    @Override
    public void beforeChildRemoval(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

    }

    @Override
    public void beforeChildReplacement(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

    }

    @Override
    public void beforeChildMovement(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

    }

    @Override
    public void beforeChildrenChange(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

    }

    @Override
    public void beforePropertyChange(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

    }

    @Override
    public void childAdded(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

    }

    @Override
    public void childRemoved(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

    }

    @Override
    public void childReplaced(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

    }

    @Override
    public void childrenChanged(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        }

    @Override
    public void childMoved(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

    }


    @Override
    public void propertyChanged(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

    }
}
