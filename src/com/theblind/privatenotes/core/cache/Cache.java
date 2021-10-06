package com.theblind.privatenotes.core.cache;

public interface Cache {
    <T> void putNote(String key,int lineNumber, T value);

    <T> T getNote(String key, int lineNumber);

    void remove();

}
