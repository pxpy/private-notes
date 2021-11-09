package com.theblind.privatenotes.core;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NoteFile {

    private String version;
    private String fileName;
    private String fileSimpleName;
    private String fileType;
    private Map<Integer, String> notes;


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFileSimpleName() {
        return fileSimpleName;
    }

    public void setFileSimpleName(String fileSimpleName) {
        this.fileSimpleName = fileSimpleName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Map<Integer, String> getNotes() {
        return notes;
    }

    public void setNotes(Map<Integer, String> notes) {
        this.notes = notes;
    }

    public String getNode(int lineNumber) {
        if (Objects.isNull(notes)) {
            return null;
        }
        return notes.get(lineNumber);
    }

    public int getNodeSize(){
        return notes.size();
    }

    public void setNode(int lineNumber, String content) {
        if (Objects.isNull(notes)) {
            notes = new HashMap<>();
        }
        notes.put(lineNumber, content);
    }

    public void removeNode(int lineNumber) {
        if (Objects.nonNull(notes)) {
            notes.remove(lineNumber);
        }
    }

}
