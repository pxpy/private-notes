package com.theblind.privatenotes.core;

import com.theblind.privatenotes.core.service.ConfigService;
import com.theblind.privatenotes.core.service.NoteFileService;
import com.theblind.privatenotes.core.service.impl.ConfigServiceImpl;
import com.theblind.privatenotes.core.service.impl.NoteFileServiceImpl;


public class PrivateNotesFactory {

    String DEFAULT_USER_Folder="default";
    private static final ConfigService CONFIG_SERVICE=new ConfigServiceImpl();
    private static final NoteFileService NOTE_FILE_SERVICE=new NoteFileServiceImpl();

    public static ConfigService getConfigService() {
        return CONFIG_SERVICE;
    }

    public static NoteFileService getNoteFileService(){
      return NOTE_FILE_SERVICE;
    }



}
