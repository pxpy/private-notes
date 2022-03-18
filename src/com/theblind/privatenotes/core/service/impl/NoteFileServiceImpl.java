package com.theblind.privatenotes.core.service.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.LRUCache;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.json.JSONUtil;
import com.intellij.openapi.diagnostic.LogUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.theblind.privatenotes.core.Config;
import com.theblind.privatenotes.core.NoteFile;
import com.theblind.privatenotes.core.PrivateNotesFactory;
import com.theblind.privatenotes.core.service.ConfigService;
import com.theblind.privatenotes.core.service.NoteFileService;
import com.theblind.privatenotes.core.util.IdeaApiUtil;
import com.theblind.privatenotes.core.util.JsonUtil;
import com.theblind.privatenotes.core.util.PrivateNotesUtil;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class NoteFileServiceImpl implements NoteFileService {


    ConfigService configService = PrivateNotesFactory.getConfigService();

    MD5 md5 = MD5.create();

    Map<String, String> versionCache = new HashMap();

    //存满了就移除最久未使用的元素
    LRUCache<String, NoteFile> noteFileCache = CacheUtil.<String, NoteFile>newLRUCache(5);


    @Override
    public NoteFile get(File file, Object... params) throws Exception {
        Config config = configService.get();

        File noteFile = getAbsolutePath(config, file.getName(), generateVersionByCache(file, params[0])).toFile();
        if (noteFile.exists()) {
            synchronized (NoteFileService.class) {
                return noteFileCache.get(noteFile.getName(), () -> {
                    FileReader fileReader = new FileReader(noteFile);
                    return JSONUtil.toBean(fileReader.readString(), NoteFile.class);
                });
            }
        }

        return null;
    }

    @Override
    public NoteFile get(String path, Object... params) throws Exception {
        return get(new File(path), params);
    }

    public File getStorage(Config config, File file, String version) {

        File noteFile = getAbsolutePath(config, file.getName(), version).toFile();
        return noteFile;
    }

    @Override
    public String getNote(File file, int lineNumber, Object... params) throws Exception {
        NoteFile noteFile = get(file, params);
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
    public String generateVersion(boolean cacheInvalid, Object... params) throws Exception {
        if (!(params[0] instanceof File)) {
            throw new RuntimeException("params[0] not instanceof String");
        }
        String ver;
        File file = (File) params[0];
        // 是否走缓存
        if (!cacheInvalid && Objects.nonNull(ver = versionCache.get(file.getCanonicalPath()))) {
            return ver;
        }

        if (file.exists()) {
            ver = md5.digestHex16(file);
        } else {
            if (!(params[1] instanceof byte[])) {
                throw new RuntimeException("params[1] not instanceof byte[]");
            }
            ver = md5.digestHex16((byte[]) params[1]);
        }
        versionCache.put(file.getCanonicalPath(), ver);
        return ver;
    }

    @Override
    public String generateVersionByCache(Object... params) throws Exception {
        return generateVersion(false, params);
    }


    @Override
    public void saveNote(NoteFile noteFile) throws Exception {
        Config config = configService.get();

        File file = getAbsolutePath(config, noteFile).toFile();
        // 创建文件
        FileUtil.touch(file);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(JsonUtil.toJson(noteFile));

    }


    @Override
    public void saveNote(String path, int lineNumber, String note, Object... params) throws Exception {
        this.saveNote(new File(path), lineNumber, note, params);

    }

    @Override
    public void saveNote(File file, int lineNumber, String note, Object... params) throws Exception {
        if (StrUtil.isBlank(note)) {
            return;
        }
        NoteFile noteFile = get(file, params);
        if (Objects.isNull(noteFile)) {
            noteFile = new NoteFile();
            String fileName = file.getName();
            String[] split = fileName.split("\\.");
            String defaultType = " ";
            if (split.length == 2) {
                defaultType = split[1];
            }
            noteFile.setFileName(fileName);
            noteFile.setFileType(defaultType);
            noteFile.setFileSimpleName(split[0]);
            noteFile.setVersion(generateVersionByCache(file, params[0]));
        }
        noteFile.setNode(lineNumber, note.trim());
        saveNote(noteFile);
    }


    @Override
    public void saveNote(List<NoteFile> noteFileList) {

    }

    @Override
    public void delNote(String path, int lineNumber, Object... params) throws Exception {
        delNote(new File(path), lineNumber, params);
    }

    @Override
    public void delNote(File file, int lineNumber, Object... params) throws Exception {
        NoteFile noteFile = get(file, params);
        if (noteFile.getNodeSize() == 1) {
            delNoteFile(noteFile);
            return;
        }
        if (noteFile != null) {
            noteFile.removeNode(lineNumber);
            saveNote(noteFile);
        }
    }

    @Override
    public void delNoteFile(NoteFile noteFile) throws Exception {
        Config config = configService.get();
        File localFile = getAbsolutePath(config, noteFile.getFileName(), noteFile.getVersion()).toFile();
        String name = localFile.getName();
        synchronized (NoteFileService.class) {
            FileUtil.del(localFile);
            noteFileCache.remove(name);
        }
    }

    @Override
    public void loadCache(String path, Object... params) throws Exception {

    }

    @Override
    public void removeCache(String path, Object... params) {


    }


    @Override
    public void wrapDownNote(String path, int lineNumber, Object... params) throws Exception {
        updateNoteLineNumber(path, lineNumber, ++lineNumber, params);
    }


    @Override
    public void continueToWrapDown(String path, int lineNumber, int wrapCount, Object... params) throws Exception {
        NoteFile noteFile = get(path, params);
        Map<Integer, String> notes = noteFile.getNotes();

        TreeMap<Integer, String> sort = MapUtil.sort(notes, (k1, k2) -> k2 - k1);
        SortedMap<Integer, String> integerStringSortedMap = sort.subMap(sort.firstKey(), lineNumber - 1);
        integerStringSortedMap.forEach((k, v) -> {
            notes.remove(k);
            k += wrapCount;
            notes.put(k, v);
        });
        saveNote(noteFile);
    }

    @Override
    public void continueToWrapUp(String path, int lineNumber, int wrapCount, Object... params) throws Exception {
        NoteFile noteFile = get(path, params);
        Map<Integer, String> notes = noteFile.getNotes();
        TreeMap<Integer, String> sort = MapUtil.sort(notes);
        SortedMap<Integer, String> integerStringSortedMap = sort.subMap(lineNumber, sort.lastKey() + 1);
        integerStringSortedMap.forEach((k, v) -> {
            notes.remove(k);
            k -= wrapCount;
            notes.put(k, v);
        });
        saveNote(noteFile);
    }


    public void updateNoteLineNumber(String path, int lineNumber, int newLineNumber, Object... params) throws Exception {
        NoteFile noteFile = get(path, params);
        String node = noteFile.getNode(lineNumber);

        noteFile.removeNode(lineNumber);
        noteFile.setNode(newLineNumber, node);
        saveNote(noteFile);
    }

    @Override
    public void updateVersion(String path, Object... params) throws Exception {
        Config config = configService.get();
        File file = new File(path);

        String beforeVersion = generateVersionByCache(file, params[0]);
        File oldStorage = getStorage(config, file, beforeVersion);
        String version = generateVersion(true, file, params[0]);
        File storage = getStorage(config, file, version);
        if (!oldStorage.exists()) {
            return;
        }


        oldStorage.renameTo(storage);
        NoteFile noteFile = get(file, params);
        noteFile.setVersion(version);
        saveNote(noteFile);
    }

    @Override
    public void updateFileName(String nowPath, String oldFileName, Object... params) throws Exception {
        Config config = configService.get();

        File file = new File(nowPath);
        String version = generateVersionByCache(file);
        File nowStorageFile = getAbsolutePath(config, file.getName(), version).toFile();
        Path path = Paths.get(nowStorageFile.getParent(), oldFileName);
        File beforeFile = path.toFile();
        File oldStorage = getStorage(config, beforeFile, version);
        oldStorage.renameTo(nowStorageFile);
    }


    public Path getAbsolutePath(Config config, NoteFile noteFile) {
        String ruleName = fileNamingRules(noteFile);
        //index(ruleName),
        return Paths.get(config.getUserSavePath(), ruleName + ".txt");
    }

    public Path getAbsolutePath(Config config, String fileName, String version) {
        String[] nameSplit = fileName.split("\\.");
        String defaultType = " ";
        if (nameSplit.length == 2) {
            defaultType = nameSplit[1];
        }

        //index(ruleName)
        return Paths.get(config.getUserSavePath(), fileNamingRules(nameSplit[0], defaultType, version) + ".txt");
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

    int hash(String key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    String index(String key) {
        final int size = 6;
        return String.valueOf(hash(key) & size);
    }


}
