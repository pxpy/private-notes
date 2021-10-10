package com.theblind.privatenotes.core.service.impl;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.json.JSONUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.theblind.privatenotes.core.Config;
import com.theblind.privatenotes.core.NoteFile;
import com.theblind.privatenotes.core.PrivateNotesFactory;
import com.theblind.privatenotes.core.service.ConfigService;
import com.theblind.privatenotes.core.service.NoteFileService;
import com.theblind.privatenotes.core.util.JsonUtil;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class NoteFileServiceImpl implements NoteFileService {


    ConfigService configService = PrivateNotesFactory.getConfigService();
    MD5 md5 = MD5.create();
    Map<String,String> versionCache=new HashMap();


    @Override
    public NoteFile get(File file) {
        if(!file.exists()){
            return null;
        }
        Config config = new Config();
        String fullName = file.getName();
        String[] split = fullName.split("\\.");
        File noteFile = getAbsolutePath(config, split[0],split[1],generateVersion(file.getAbsolutePath())).toFile();
        try {

        if (noteFile.exists()) {

            FileReader fileReader = new FileReader(noteFile);
            return JSONUtil.toBean(fileReader.readString(), NoteFile.class);
        }

        }catch (IORuntimeException exception){

        }
        return null;
    }

    @Override
    public NoteFile get(String path) {
        return  get(new File(path));
    }

    public File getStorage(Config config,File file,String version){
        String[] split = file.getName().split("\\.");
        File noteFile = getAbsolutePath(config, split[0],split[1],version).toFile();
        return noteFile;
    }

    @Override
    public String getNote(File file, int lineNumber) {
        NoteFile noteFile = get(file);
        if(Objects.isNull(noteFile)){
            return null;
        }
        return noteFile.getNode(lineNumber);
    }


    @Override
    public String getNote(String path, int lineNumber) {
        return getNote(new File(path),lineNumber);
    }



    @Override
    public boolean exist(String path) {
        return  exist(new File(path));
    }

    @Override
    public boolean exist(File file) {
        return false;
    }


    @Override
    public boolean noteExist(File file, int lineNumber) {
        return getNote(file,lineNumber)==null?false:true;
    }

    @Override
    public boolean noteExist(String path, int lineNumber) {
        return noteExist(new File(path), lineNumber);
    }





    @Override
    public String generateVersion(Object... params) {
        String path = (String) params[0];
        byte[] bytes = new FileReader(path).readBytes();
        return  md5.digestHex16(bytes);
    }


    @Override
    public void saveNote(NoteFile noteFile) {
        Config config = new Config();

        if (StringUtil.isEmpty(config.getMail())) {
            Path defaultUserSavePath = Paths.get(System.getProperty("user.home"), "privateNotes", "default");
            File defaultUserSaveFile = defaultUserSavePath.toFile();
            File file = getAbsolutePath(config, noteFile).toFile();
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(JsonUtil.toJson(noteFile));

        }
    }


    @Override
    public void saveNote(String path, int lineNumber, String note) {
        this.saveNote(new File(path),lineNumber,note);
    }

    @Override
    public void saveNote(File file, int lineNumber, String note) {
        NoteFile noteFile = get(file);
       if(Objects.isNull(noteFile)){
           noteFile=new NoteFile();
           String fileName = file.getName();
           String[] split = fileName.split("\\.");
           noteFile.setFileName(fileName);
           noteFile.setFileType(split[1]);
           noteFile.setFileSimpleName(split[0]);
           noteFile.setVersion(generateVersion(file.getAbsolutePath()));
       }
        noteFile.setNode(lineNumber,note);
        saveNote(noteFile);
    }

    public Path getAbsolutePath(Config config, NoteFile noteFile) {
        String ruleName = fileNamingRules(noteFile);
        //index(ruleName),
        return Paths.get(config.getUserSavePath(),  ruleName + ".txt");
    }

    public Path getAbsolutePath(Config config, String fileName, String type, String version) {
        String ruleName = fileNamingRules(fileName, type, version);
        //index(ruleName)
        return Paths.get(config.getUserSavePath(),  ruleName + ".txt");
    }

    public String fileNamingRules(NoteFile noteFile) {
        return fileNamingRules(noteFile.getFileSimpleName(), noteFile.getFileType(), noteFile.getVersion());
    }

    public String fileNamingRules(String fileName, String type, String version) {
        return fileName + type + version;
    }


    @Override
    public void saveNote(List<NoteFile> noteFileList) {

    }


    @Override
    public void loadCache(String path) {
        versionCache.put(path,generateVersion(path));
    }

    @Override
    public void removeCache(String path) {

    }

    @Override
    public void refreshVersion(String path, Object... params) {
        Config config = new Config();
        String version = generateVersion(path);
        File file = new File(path);
        File storage = getStorage(config, file,version);

        File beforeFile;
        String beforeVersion=null;

        Path p1 = null;
        //文件名修改
        if(Objects.nonNull(params)&& params.length!=0){
            String oldFileName = (String) params[0];
            beforeVersion=version;
            String[] split = oldFileName.split("\\.");
            beforeFile=Paths.get(storage.getParent(), oldFileName).toFile();

        }else {
             beforeVersion = versionCache.get(path);
             beforeFile=file;
        }
        versionCache.remove(beforeFile.getPath());
        versionCache.put(path,version);
        File oldStorage =getStorage(config, beforeFile,beforeVersion);
        oldStorage.renameTo(storage);
    }

    public static void main(String[] args) {
        NoteFileServiceImpl noteFileService = new NoteFileServiceImpl();
        NoteFile noteFile = new NoteFile();
        noteFile.setFileName("Test.java");
        noteFile.setFileType("java");
        noteFile.setFileSimpleName("Test");
        noteFile.setVersion("asdasdadasdad");
        noteFileService.saveNote(noteFile);

        //NoteFile noteFile1 = noteFileService.get(noteFile.getFileSimpleName(), noteFile.getFileType(), noteFile.getVersion());
        //System.out.println(JSONUtil.toJsonStr(noteFile1));

       /* final int[] HASH_INCREMENT = {0x61c88647};
        int size = 5;
        int i = "F.Java".hashCode();
        List<String> strings = Arrays.asList("F.java", "NoteFileServiceImpl.java", "NoteFileService.Java", "main.java","main.java","A","String.java","StringUtils.java");
        strings.stream().forEach((v) -> {

            int hashCode = hash(v);
            int idx = hashCode & 6;
            System.out.println("斐波那契散列：" + idx + " 普通散列：" + (String.valueOf(v).hashCode() & 6));
            HASH_INCREMENT[0] = HASH_INCREMENT[0] + HASH_INCREMENT[0];
        });*/


    }

    int hash(String key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    String index(String key) {
        final int size = 6;
        return String.valueOf(hash(key) & size);
    }


}
