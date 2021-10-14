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
    Map<String, String> versionCache = new HashMap();


    @Override
    public NoteFile get(File file, Object... params) throws Exception {
        if (!file.exists()) {
            return null;
        }
        Config config = new Config();
        String fullName = file.getName();
        String[] split = fullName.split("\\.");
        File noteFile = getAbsolutePath(config, split[0], split[1], generateVersion(file.getAbsolutePath())).toFile();

        if (noteFile.exists()) {
            FileReader fileReader = new FileReader(noteFile);
            return JSONUtil.toBean(fileReader.readString(), NoteFile.class);
        }

        return null;
    }

    @Override
    public NoteFile get(String path, Object... params) throws Exception {
        return get(new File(path), params);
    }

    public File getStorage(Config config, File file, String version) {
        String[] split = file.getName().split("\\.");
        File noteFile = getAbsolutePath(config, split[0], split[1], version).toFile();
        return noteFile;
    }

    @Override
    public String getNote(File file, int lineNumber, Object... params) throws Exception {
        NoteFile noteFile = get(file, params[0]);
        if (Objects.isNull(noteFile)) {
            return null;
        }
        return noteFile.getNode(lineNumber);
    }


    @Override
    public String getNote(String path, int lineNumber, Object... params) throws Exception {
        return getNote(new File(path), lineNumber, params);
    }


    @Override
    public boolean exist(String path, Object... params) {
        return exist(new File(path), params);
    }

    @Override
    public boolean exist(File file, Object... params) {
        return false;
    }


    @Override
    public boolean noteExist(File file, int lineNumber, Object... params) throws Exception {
        return getNote(file, lineNumber, params) == null ? false : true;
    }

    @Override
    public boolean noteExist(String path, int lineNumber, Object... params) throws Exception {
        return noteExist(new File(path), lineNumber, params);
    }


    @Override
    public String generateVersion(Object... params) throws Exception {
        if (!(params[0] instanceof String)) {
            throw new RuntimeException("params[0] not instanceof String");
        }
        if (!(params[1] instanceof byte[])) {
            throw new RuntimeException("params[1] not instanceof byte[]");
        }

        File file = (File) params[0];

        if (file.exists()) {
            return md5.digestHex16(file);
        } else {
            return md5.digestHex16((byte[]) params[1]);
        }
    }


    @Override
    public void saveNote(NoteFile noteFile) throws Exception {
        Config config = new Config();

        if (StringUtil.isEmpty(config.getMail())) {
            File file = getAbsolutePath(config, noteFile).toFile();
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(JsonUtil.toJson(noteFile));

        }
    }


    @Override
    public void saveNote(String path, int lineNumber, String note, Object... params) throws Exception {
        this.saveNote(new File(path), lineNumber, note, params);

    }

    @Override
    public void saveNote(File file, int lineNumber, String note, Object... params) throws Exception {
        NoteFile noteFile = get(file);
        if (Objects.isNull(noteFile)) {
            noteFile = new NoteFile();
            String fileName = file.getName();
            String[] split = fileName.split("\\.");
            noteFile.setFileName(fileName);
            noteFile.setFileType(split[1]);
            noteFile.setFileSimpleName(split[0]);
            noteFile.setVersion(generateVersion(file.getAbsolutePath(), params[0]));
        }
        noteFile.setNode(lineNumber, note);
        saveNote(noteFile);
    }

    public Path getAbsolutePath(Config config, NoteFile noteFile) {
        String ruleName = fileNamingRules(noteFile);
        //index(ruleName),
        return Paths.get(config.getUserSavePath(), ruleName + ".txt");
    }

    public Path getAbsolutePath(Config config, String fileName, String type, String version) {
        String ruleName = fileNamingRules(fileName, type, version);
        //index(ruleName)
        return Paths.get(config.getUserSavePath(), ruleName + ".txt");
    }

    /**
     * 根据规则 生成文件名称
     *
     * @param noteFile
     * @return
     */
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
    public void loadCache(String path, Object... params) throws Exception {
        versionCache.put(path, generateVersion(new File(path), params[0]));
    }

    @Override
    public void removeCache(String path, Object... params) {

    }

    @Override
    public void refreshVersion(String path, Object... params) throws Exception {
        Config config = new Config();
        File file = new File(path);
        String version = generateVersion(path, params[0]);
        File storage = getStorage(config, file, version);

        File beforeFile = null;
        String beforeVersion = null;

        //文件名修改
        if (Objects.nonNull(params) && params.length != 0) {
            String oldFileName = (String) params[0];
            beforeVersion = version;
            String[] split = oldFileName.split("\\.");
            beforeFile = Paths.get(storage.getParent(), oldFileName).toFile();

        } else {
            //文件内容改变
            beforeVersion = versionCache.get(path);
            beforeFile = file;
        }

        versionCache.remove(beforeFile.getPath());
        versionCache.put(path, version);

        File oldStorage = getStorage(config, beforeFile, beforeVersion);
        oldStorage.renameTo(storage);

        NoteFile noteFile = get(file, params);
        noteFile.setVersion(version);
        try {
            saveNote(noteFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
