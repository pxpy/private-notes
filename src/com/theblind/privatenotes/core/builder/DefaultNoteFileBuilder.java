package com.theblind.privatenotes.core.builder;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.digest.MD5;
import com.theblind.privatenotes.core.NoteFile;

import java.io.File;

public class DefaultNoteFileBuilder extends NoteFileBuilder {

    @Override
    public NoteFile build(File file) {
        MD5 md5 = MD5.create();
        return build(file.getName(), md5.digestHex16(FileUtil.readBytes(file)));
    }

    @Override
    public NoteFile build(String fileName, String version) {
        String[] split = fileName.split(".");
        NoteFile noteFile = new NoteFile();
        noteFile.setVersion(version);
        noteFile.setFileName(fileName);
        noteFile.setFileSimpleName(split[0]);
        noteFile.setFileType(split[1]);
        return noteFile;
    }
}
