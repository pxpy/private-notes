package com.theblind.privatenotes.core.cache;

import java.util.HashMap;
import java.util.Map;

public class CacheImpl implements Cache {

   static Map cache=new HashMap();


    @Override
    public void remove() {

    }

    @Override
    public <T> void putNote(String key, int lineNumber,T value) {
        final String notes="notes";
        if (cache.containsKey(notes)){
            HashMap<String,HashMap> notesCache = (HashMap)cache.get(notes);
            HashMap hashMap = notesCache.get(key);
            hashMap.put(lineNumber,value);
        }else {
            cache.put(notes, new HashMap<>());
        }
    }

    @Override
    public <T> T getNote(String key, int lineNumber) {
        final String notes="notes";
        if (cache.containsKey(notes)){
            HashMap<String,HashMap> notesCache = (HashMap)cache.get(notes);
            HashMap hashMap = notesCache.get(key);
            hashMap.get(lineNumber);
        }
        return null;
    }
}
