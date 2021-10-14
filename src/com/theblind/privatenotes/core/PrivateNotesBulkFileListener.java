package com.theblind.privatenotes.core;

import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.theblind.privatenotes.core.service.NoteFileService;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * 批量文件修改 监听
 * @author theblind
 */
public class PrivateNotesBulkFileListener implements BulkFileListener {

    NoteFileService noteFileService = PrivateNotesFactory.getNoteFileService();


    @Override
    public void after(@NotNull List<? extends VFileEvent> events) {
        //在文件保存时 刷新文件版本
        events.stream().forEach((fileEvent)->{
            String canonicalPath = null;
            try {
                canonicalPath = fileEvent.getFile().getCanonicalPath();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(Files.isDirectory(Paths.get(canonicalPath))){
                return;
            }
            String path = fileEvent.getPath();
            try {
                 noteFileService.refreshVersion(canonicalPath,fileEvent.getFile().getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


}
