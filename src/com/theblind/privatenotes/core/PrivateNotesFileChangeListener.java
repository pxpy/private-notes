package com.theblind.privatenotes.core;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiTreeChangeEvent;
import com.intellij.psi.PsiTreeChangeListener;
import com.intellij.psi.impl.source.PsiFileImpl;
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
        System.out.println(Thread.currentThread().getName());
        psiTreeChangeEvent.getFile().clearCaches();
        psiTreeChangeEvent.getFile().getVirtualFile().getBOM();
        noteFileService.refreshVersion(psiTreeChangeEvent.getFile().getVirtualFile().getCanonicalPath());
    }

    @Override
    public void childMoved(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

    }


    @Override
    public void propertyChanged(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        //修改文件名称时
        if ("fileName".equals(psiTreeChangeEvent.getPropertyName())) {
            System.out.println(Thread.currentThread().getName());
            psiTreeChangeEvent.getSource();
            psiTreeChangeEvent.getOldValue();

            PsiFileImpl element = (PsiFileImpl) psiTreeChangeEvent.getElement();
           String newPath=element.getViewProvider().getVirtualFile().getCanonicalPath();

            noteFileService.refreshVersion(newPath,psiTreeChangeEvent.getOldValue());
        }

    }
}
