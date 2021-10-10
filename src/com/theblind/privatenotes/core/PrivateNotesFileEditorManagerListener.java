package com.theblind.privatenotes.core;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.vfs.VirtualFile;
import com.theblind.privatenotes.core.service.NoteFileService;
import org.jetbrains.annotations.NotNull;

public class PrivateNotesFileEditorManagerListener implements FileEditorManagerListener {

    NoteFileService noteFileService = PrivateNotesFactory.getNoteFileService();

    @Override
    public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
        //noteFileService.removeCache(file.getCanonicalPath());
    }

    @Override
    public void selectionChanged(@NotNull FileEditorManagerEvent event) {
        noteFileService.loadCache(event.getNewFile().getCanonicalPath());
    }
}
