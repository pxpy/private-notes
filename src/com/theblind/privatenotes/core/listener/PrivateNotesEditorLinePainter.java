package com.theblind.privatenotes.core.listener;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.util.StrUtil;
import com.intellij.openapi.editor.EditorLinePainter;
import com.intellij.openapi.editor.LineExtensionInfo;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.theblind.privatenotes.core.Config;
import com.theblind.privatenotes.core.PrivateNotesFactory;
import com.theblind.privatenotes.core.service.ConfigService;
import com.theblind.privatenotes.core.service.NoteFileService;
import com.theblind.privatenotes.core.util.IdeaApiUtil;
import com.theblind.privatenotes.core.util.PrivateNotesUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * abcdef
 * 行尾拓展
 */
public class PrivateNotesEditorLinePainter extends EditorLinePainter {

    NoteFileService noteFileService = PrivateNotesFactory.getNoteFileService();
    ConfigService configService = PrivateNotesFactory.getConfigService();

    TimedCache<String, Object> timedCache = CacheUtil.newTimedCache(60 * 1000);

    @Nullable
    @Override
    public Collection<LineExtensionInfo> getLineExtensions(@NotNull Project project, @NotNull VirtualFile virtualFile, int i) {
        Config config = configService.get();

        List<LineExtensionInfo> result = new ArrayList<>();//✍
        try {
            String note = noteFileService.getNote(virtualFile.getPath(), i, IdeaApiUtil.getBytes(virtualFile));
            if (Objects.isNull(note)) {
                return null;
            }
            Integer maxCharNum = config.getMaxCharNum();
            if (note.length()>maxCharNum) {
                note = StrUtil.builder().append(note, 0, maxCharNum ).append("...").toString();
            }

            result.add(new LineExtensionInfo(String.format(" %s ", config.getMark()),
                    new TextAttributes(null, null, Config.asColor(config.getMarkColor()), null, Font.PLAIN)));
            result.add(new LineExtensionInfo(note,
                    new TextAttributes(null, null, Config.asColor(config.getNoteColor()), null, Font.PLAIN)));
            return result;
        } catch (Exception e) {
            //防止异常被 重复输出
            if (!timedCache.containsKey(virtualFile.getPath())) {
                timedCache.put(virtualFile.getPath(), null);
                PrivateNotesUtil.errLog(e, project);
            }
        }
        return null;
    }

}
