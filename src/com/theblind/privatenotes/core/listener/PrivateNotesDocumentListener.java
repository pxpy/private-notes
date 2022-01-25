package com.theblind.privatenotes.core.listener;

import cn.hutool.core.math.MathUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.DocumentUtil;
import com.theblind.privatenotes.core.NoteFile;
import com.theblind.privatenotes.core.PrivateNotesFactory;
import com.theblind.privatenotes.core.service.NoteFileService;
import com.theblind.privatenotes.core.util.IdeaApiUtil;
import com.theblind.privatenotes.core.util.PrivateNotesUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PrivateNotesDocumentListener implements DocumentListener {

    NoteFileService noteFileService = PrivateNotesFactory.getNoteFileService();

    @Override
    public void beforeDocumentChange(@NotNull DocumentEvent event) {
        try {
            Document document = event.getDocument();
            int offset = event.getOffset();
            int lineNumber1 = document.getLineNumber(offset);

            CharSequence newFragment = event.getNewFragment();
            CharSequence oldFragment = event.getOldFragment();
            String newStr = String.valueOf(newFragment);
            String oldStr = String.valueOf(oldFragment);

            int c1 = warpCount(newStr);
            int c2 = warpCount(oldStr);
            if (c1 != 0 || c2 != 0) {

                TextRange lineTextRange = DocumentUtil.getLineTextRange(document, lineNumber1);
                int warpLineNumber = warpLineNumber(newStr, oldStr, lineNumber1, lineTextRange, offset);
                VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(document);
                if (Objects.isNull(virtualFile)) {
                    return;
                }

                String canonicalPath = virtualFile.getCanonicalPath();
                NoteFile noteFile = noteFileService.get(canonicalPath, IdeaApiUtil.getBytes(virtualFile));
                if (Objects.isNull(noteFile)) {
                    return;
                }

                if (canWrapDown(newStr, oldStr)) {
                    int warpCount = (int) NumberUtil.sub(c1, c2);
                    noteFileService.continueToWrapDown(canonicalPath, warpLineNumber, warpCount
                            , IdeaApiUtil.getBytes(virtualFile));

                } else {
                    int warpCount = (int) NumberUtil.sub(c2, c1);
                    noteFileService.continueToWrapUp(canonicalPath, warpLineNumber, warpCount
                            , IdeaApiUtil.getBytes(virtualFile));
                }

            }
        } catch (Exception e) {
            //PrivateNotesUtil.errLog(e, null);
        }
    }


    boolean canWrapDown(String newText, String oldText) {
        return newText.length() > oldText.length() ? true : false;
    }


    int warpCount(String text) {
        int count = StrUtil.count(text, "\n");
        if (count == 0) return count;
        return count;
    }

    int warpLineNumber(String newText, String oldText, int lineNumber, TextRange lineTextRange, Integer offset) {
        int warpLineNumber = lineNumber;
        if (newText.length() < oldText.length()) warpLineNumber += 1;
        else if (offset < lineTextRange.getEndOffset()) {
            return warpLineNumber;
        } else if (lineTextRange.getLength() == 0) {
            return warpLineNumber;
        } else if (offset == lineTextRange.getEndOffset()) {
            return warpLineNumber + 1;
        } else if (!StrUtil.endWith(newText, "\n")) {
            return warpLineNumber + 1;
        }
        return warpLineNumber;
    }

}
