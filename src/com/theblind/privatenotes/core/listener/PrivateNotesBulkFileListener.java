package com.theblind.privatenotes.core.listener;

import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileContentChangeEvent;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.openapi.vfs.newvfs.events.VFilePropertyChangeEvent;
import com.theblind.privatenotes.core.PrivateNotesFactory;
import com.theblind.privatenotes.core.service.NoteFileService;
import com.theblind.privatenotes.core.util.PrivateNotesUtil;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * 批量文件修改 监听
 *
 * @author theblind
 */
public class PrivateNotesBulkFileListener implements BulkFileListener {

    NoteFileService noteFileService = PrivateNotesFactory.getNoteFileService();


    @Override
    public void after(@NotNull List<? extends VFileEvent> events) {
        events.stream()
                .filter(this::needFilter)
                .forEach(fileEvent -> {
                    if (fileEvent instanceof VFilePropertyChangeEvent) {
                        this.afterPropertyChange(((VFilePropertyChangeEvent) fileEvent));
                    } else if (fileEvent instanceof VFileContentChangeEvent) {
                        this.afterContentChange(((VFileContentChangeEvent) fileEvent));
                    }
                });
    }

    private boolean needFilter(VFileEvent fileEvent) {
        String canonicalPath = fileEvent.getFile().getCanonicalPath();
        return Files.isDirectory(Paths.get(canonicalPath)) ? false : Files.exists(Paths.get(canonicalPath));
    }


    private void afterPropertyChange(VFilePropertyChangeEvent changeEvent) {
        if (changeEvent.isRename()) {
            try {
                noteFileService.updateFileName(changeEvent.getNewPath(), (String) changeEvent.getOldValue(), PrivateNotesUtil.getBytes((changeEvent).getFile().getInputStream()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void afterContentChange(VFileContentChangeEvent changeEvent) {
        String canonicalPath = changeEvent.getFile().getCanonicalPath();
        try {
            noteFileService.updateVersion(canonicalPath, PrivateNotesUtil.getBytes(changeEvent.getFile().getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
