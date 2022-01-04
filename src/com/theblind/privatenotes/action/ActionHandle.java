package com.theblind.privatenotes.action;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupListener;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import com.theblind.privatenotes.core.PrivateNotesFactory;
import com.theblind.privatenotes.core.service.NoteFileService;
import com.theblind.privatenotes.core.util.IdeaApiUtil;
import com.theblind.privatenotes.core.util.PrivateNotesUtil;
import com.theblind.privatenotes.ui.PrivateNotesEditorForm;
import com.theblind.privatenotes.ui.TextBox;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ActionHandle {

    public static final NoteFileService noteFileService = PrivateNotesFactory.getNoteFileService();


    protected Operate operate;

    public ActionHandle(Operate operate) {
        this.operate = operate;
    }

    public enum Operate {
        ADD("[Note] 添加"), DEL("[Note] 删除"), EDIT("[Note] 编辑"), WRAP("[Note] 换行"), COPY("[Note] 复制"),DETAILED("[Note] 详细");


        Operate(String title) {
            this.title = title;
        }

        private final String title;

        public String getTitle() {
            return title;
        }

    }

    public boolean isVisible(@NotNull Project project, Editor editor, VirtualFile virtualFile) {
        if (virtualFile == null) return false;
        if (editor == null) return false;
        try {
            boolean noteExist = noteFileService.noteExist(virtualFile.getCanonicalPath(),
                    editor.getDocument().getLineNumber(editor.getCaretModel().getOffset()), IdeaApiUtil.getBytes(virtualFile));

            if (Operate.ADD == operate) return !noteExist;

            return noteExist;
        } catch (Exception e) {
            PrivateNotesUtil.errLog(e, project);
        }
        return false;
    }

    public abstract void execute(@NotNull Project project, Editor editor, VirtualFile virtualFile);


    public static class AddActionHandle extends ActionHandle {

        static AtomicBoolean OPEN = new AtomicBoolean(false);

        public AddActionHandle() {
            super(Operate.ADD);
        }

        @Override
        public void execute(@NotNull Project project, Editor editor, VirtualFile virtualFile) {
            if (OPEN.get()) {
                return;
            }
            OPEN.set(true);
            PrivateNotesEditorForm remarkEditor = new PrivateNotesEditorForm();
            JScrollPane scrollPane = remarkEditor.getScrollPane();
            JEditorPane editorPane = remarkEditor.getEditorPane1();
            Integer selLineNumber = IdeaApiUtil.getSelLineNumber(editor);
            IdeaApiUtil.showBalloon(remarkEditor.getPanel1(), operate.getTitle(), new JBPopupListener() {
                @Override
                public void onClosed(@NotNull LightweightWindowEvent event) {
                    try {
                        OPEN.set(false);
                        noteFileService.saveNote(virtualFile.getCanonicalPath(), selLineNumber
                                , editorPane.getText(), IdeaApiUtil.getBytes(virtualFile));
                    } catch (Exception e) {
                        PrivateNotesUtil.errLog(e, project);
                    }
                }
            }, editor);

            editorPane.requestFocus();
        }
    }


    public static class EditActionHandle extends ActionHandle {
        static AtomicBoolean OPEN = new AtomicBoolean(false);
        public EditActionHandle() {
            super(Operate.EDIT);
        }

        @Override
        public void execute(@NotNull Project project, Editor editor, VirtualFile virtualFile) {
            if (OPEN.get()) {
                return;
            }
            OPEN.set(true);
            PrivateNotesEditorForm remarkEditor = new PrivateNotesEditorForm();
            JScrollPane scrollPane = remarkEditor.getScrollPane();
            JEditorPane editorPane = remarkEditor.getEditorPane1();
            Integer selLineNumber = IdeaApiUtil.getSelLineNumber(editor);
            try {
                editorPane.setText(noteFileService.getNote(virtualFile.getCanonicalPath(),
                        selLineNumber,
                        IdeaApiUtil.getBytes(virtualFile)));
            } catch (Exception e) {
                PrivateNotesUtil.errLog(e, project);
            }

            IdeaApiUtil.showBalloon(remarkEditor.getPanel1(), Operate.EDIT.getTitle(), new JBPopupListener() {
                @Override
                public void onClosed(@NotNull LightweightWindowEvent event) {
                    try {
                        OPEN.set(false);
                        noteFileService.saveNote(virtualFile.getCanonicalPath(),
                                selLineNumber, editorPane.getText(), IdeaApiUtil.getBytes(virtualFile));
                    } catch (Exception e) {
                        PrivateNotesUtil.errLog(e, project);
                    }
                }
            }, editor);

            editorPane.requestFocus();
            remarkEditor.top();

        }
    }


    public static class DetailedActionHandle extends ActionHandle {

        public DetailedActionHandle() {
            super(Operate.DETAILED);
        }

        @Override
        public void execute(@NotNull Project project, Editor editor, VirtualFile virtualFile) {

            Integer selLineNumber = IdeaApiUtil.getSelLineNumber(editor);
            TextBox textBox = new TextBox(project,virtualFile,selLineNumber);
            JEditorPane editorPane1 = textBox.getEditorPane1();
            try {
                editorPane1.setText(noteFileService.getNote(virtualFile.getCanonicalPath(),
                        selLineNumber,
                        IdeaApiUtil.getBytes(virtualFile)));
            } catch (Exception e) {
                PrivateNotesUtil.errLog(e, project);
            }

            textBox.getNav().addActionListener(event -> IdeaApiUtil.to(project,virtualFile,selLineNumber));
            textBox.getRefresh().addActionListener(event ->{
                try {
                    editorPane1.setText(noteFileService.getNote(virtualFile.getCanonicalPath(),
                            selLineNumber,
                            IdeaApiUtil.getBytes(virtualFile)));
                } catch (Exception e) {
                    PrivateNotesUtil.errLog(e, project);
                }
            });
            editorPane1.setEditable(false);
            textBox.getPanel().setMinimumSize(new Dimension(200, 200));
            IdeaApiUtil.showComponent(  String.format("[Note] %s %s行",virtualFile.getName(),selLineNumber),textBox.getPanel(), editorPane1,editor, IconLoader.findIcon("/icon/close.png"));

        }
    }

    public static class CopyActionHandle extends ActionHandle {
        public CopyActionHandle() {
            super(Operate.COPY);
        }

        @Override
        public void execute(@NotNull Project project, Editor editor, VirtualFile virtualFile) {

            try {
                CopyPasteManager.getInstance().setContents(new StringSelection(noteFileService.getNote(virtualFile.getPath(), IdeaApiUtil.getSelLineNumber(editor), IdeaApiUtil.getBytes(virtualFile))));
            } catch (Exception e) {
                PrivateNotesUtil.errLog(e, project);
            }

        }
    }

    public static class DelActionHandle extends ActionHandle {

        public DelActionHandle() {
            super(Operate.DEL);
        }

        @Override
        public void execute(@NotNull Project project, Editor editor, VirtualFile virtualFile) {

            try {
                noteFileService.delNote(virtualFile.getCanonicalPath(), IdeaApiUtil.getSelLineNumber(editor), IdeaApiUtil.getBytes(virtualFile));
            } catch (Exception e) {
                PrivateNotesUtil.errLog(e, project);
            }
        }
    }

    public static class WrapActionHandle extends ActionHandle {

        public WrapActionHandle() {
            super(Operate.WRAP);
        }

        @Override
        public void execute(@NotNull Project project, Editor editor, VirtualFile virtualFile) {
            PrivateNotesEditorForm remarkEditor = new PrivateNotesEditorForm();

            JEditorPane editorPane = remarkEditor.getEditorPane1();
            Integer selLineNumber = IdeaApiUtil.getSelLineNumber(editor);

            try {
                noteFileService.wrapNote(virtualFile.getCanonicalPath(), selLineNumber
                        , IdeaApiUtil.getBytes(virtualFile));
            } catch (Exception e) {
                PrivateNotesUtil.errLog(e, project);
            }
            editorPane.requestFocus();
        }
    }


}
