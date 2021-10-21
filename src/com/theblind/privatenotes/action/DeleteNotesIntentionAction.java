package com.theblind.privatenotes.action;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import com.theblind.privatenotes.core.util.IdeaApiUtil;
import com.theblind.privatenotes.core.util.PrivateNotesUtil;
import org.jetbrains.annotations.NotNull;

public class DeleteNotesIntentionAction extends BaseIntentionAction {

    public DeleteNotesIntentionAction() {
        super("[Note] 删除私人注释");
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile psiFile) {
        VirtualFile virtualFile = psiFile.getVirtualFile();
        try {
            return noteFileService.noteExist(virtualFile.getCanonicalPath(),
                    IdeaApiUtil.getSelLineNumber(editor), IdeaApiUtil.getBytes(virtualFile));
        } catch (Exception e) {
            PrivateNotesUtil.errLog(e, project);
        }
        return false;
    }


    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile psiFile) throws IncorrectOperationException {
        VirtualFile virtualFile = psiFile.getVirtualFile();
        try {
            noteFileService.delNote(virtualFile.getCanonicalPath(), IdeaApiUtil.getSelLineNumber(editor), IdeaApiUtil.getBytes(virtualFile));
        } catch (Exception e) {
            PrivateNotesUtil.errLog(e, project);
        }
    }
}
