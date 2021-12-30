package com.theblind.privatenotes.core;

public class Note {
    private int lineNumber;
    private String content;

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content.trim();
    }

}
