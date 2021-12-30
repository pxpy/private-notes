package com.theblind.privatenotes.action.anaction;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupListener;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;
import com.intellij.openapi.vfs.VirtualFile;
import com.theblind.privatenotes.action.ActionHandle;
import com.theblind.privatenotes.core.PrivateNotesFactory;
import com.theblind.privatenotes.core.service.NoteFileService;
import com.theblind.privatenotes.core.util.IdeaApiUtil;
import com.theblind.privatenotes.core.util.PrivateNotesUtil;
import com.theblind.privatenotes.ui.PrivateNotesEditorForm;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class EditNoteAnActionByLine extends BaseAnAction {


    int selLineNumber;

    public static final NoteFileService noteFileService = PrivateNotesFactory.getNoteFileService();


    public EditNoteAnActionByLine(int line) {
        super(ActionHandle.Operate.EDIT);
        this.selLineNumber=line;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        Project project = CommonDataKeys.PROJECT.getData(anActionEvent.getDataContext());
        Editor editor = CommonDataKeys.EDITOR.getData(anActionEvent.getDataContext());
        VirtualFile virtualFile = CommonDataKeys.VIRTUAL_FILE.getData(anActionEvent.getDataContext());
        PrivateNotesEditorForm remarkEditor = new PrivateNotesEditorForm();
        JScrollPane scrollPane = remarkEditor.getScrollPane();
        JEditorPane editorPane = remarkEditor.getEditorPane1();

        try {
            editorPane.setText(noteFileService.getNote(virtualFile.getCanonicalPath(),
                    selLineNumber,
                    IdeaApiUtil.getBytes(virtualFile)));
        } catch (Exception e) {
            PrivateNotesUtil.errLog(e, project);
        }

        IdeaApiUtil.showBalloon(remarkEditor.getPanel1(), ActionHandle.Operate.EDIT.getTitle(), new JBPopupListener() {
            @Override
            public void onClosed(@NotNull LightweightWindowEvent event) {
                try {
                    noteFileService.saveNote(virtualFile.getCanonicalPath(),
                            selLineNumber, editorPane.getText(), IdeaApiUtil.getBytes(virtualFile));
                } catch (Exception e) {
                    PrivateNotesUtil.errLog(e, project);
                }
            }
        }, editor);
        //editorPane.requestFocus();
        remarkEditor.top();

    }
}
