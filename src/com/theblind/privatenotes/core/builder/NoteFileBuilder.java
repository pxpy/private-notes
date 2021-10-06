package com.theblind.privatenotes.core.builder;

import com.theblind.privatenotes.core.NoteFile;

import java.io.File;

public abstract class NoteFileBuilder {
    public abstract NoteFile build(File file);
    public abstract NoteFile build(String fileName, String version);

}
