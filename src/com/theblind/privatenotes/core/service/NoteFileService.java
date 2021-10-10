package com.theblind.privatenotes.core.service;

import com.theblind.privatenotes.core.NoteFile;

import java.io.File;
import java.util.List;

public interface NoteFileService {

    NoteFile get(File file);
    NoteFile get(String path);

    String getNote(File file,int lineNumber);
    String getNote(String path, int lineNumber);

    boolean exist(File file);
    boolean exist(String path);
    boolean noteExist(File file, int lineNumber);
    boolean noteExist(String path, int lineNumber);

    void saveNote(NoteFile noteFile);
    void saveNote(String path, int lineNumber, String note);
    void saveNote(File file, int lineNumber, String note);
    void saveNote(List<NoteFile> noteFileList);

    void loadCache(String path);
    void removeCache(String path);

    void refreshVersion(String path,Object... params);
    String generateVersion(Object... params);


}
