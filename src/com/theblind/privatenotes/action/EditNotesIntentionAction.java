package com.theblind.privatenotes.action;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.JBPopupListener;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.SyntheticElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.theblind.privatenotes.core.NoteFile;
import com.theblind.privatenotes.ui.PrivateNotesEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class EditNotesIntentionAction extends BaseIntentionAction {

    public EditNotesIntentionAction() {
        super("[Note] 编辑私人注释");
    }

    @Override
    public boolean startInWriteAction() {
        return false;
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile psiFile) {
        return   noteFileService.noteExist(psiFile.getVirtualFile().getCanonicalPath(),
                                            editor.getDocument().getLineNumber(editor.getCaretModel().getOffset()));
    }

    public static PsiClass getTargetClass(@NotNull Editor editor, @NotNull PsiFile file) {
        int offset = editor.getCaretModel().getOffset();
        PsiElement element = file.findElementAt(offset);
        if (element != null) {
            // 当前类
            PsiClass target = PsiTreeUtil.getParentOfType(element, PsiClass.class);

            return target instanceof SyntheticElement ? null : target;
        }
        return null;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile psiFile) throws IncorrectOperationException {
        final PrivateNotesEditor remarkEditor = new PrivateNotesEditor();

        final JEditorPane editorPane = remarkEditor.getEditorPane1();
        int lineNumber = editor.getDocument().getLineNumber(editor.getCaretModel().getOffset());
        editorPane.setText(noteFileService.getNote(psiFile.getVirtualFile().getCanonicalPath(),editor.getDocument().getLineNumber(editor.getCaretModel().getOffset())));

        final JBPopupFactory popupFactory = JBPopupFactory.getInstance();
        final Balloon balloon = popupFactory.createDialogBalloonBuilder(remarkEditor.getPanel1(), myText)
               // .setFadeoutTime(5000)
                .setDialogMode(true)
                //.setLayer()
                .setRequestFocus(true)
                .setHideOnClickOutside(true)
                .createBalloon();
        //弹出窗 Balloon
        balloon.show(popupFactory.guessBestPopupLocation(editor), Balloon.Position.below);
        editorPane.requestFocus();

        balloon.addListener(new JBPopupListener() {
            @Override
            public void onClosed(@NotNull LightweightWindowEvent event) {
                String canonicalPath = psiFile.getVirtualFile().getCanonicalPath();
                NoteFile noteFile = new NoteFile();
                noteFile.setVersion(noteFileService.generateVersion(canonicalPath));


                noteFileService.saveNote(canonicalPath,
                        lineNumber ,editorPane.getText());
            }
        });

     /*   editorPane.addKeyListener(new KeyListener() {
            final long interval = 300;
            long prev = System.currentTimeMillis();

            @Override
            public void keyTyped(KeyEvent e) {
                // Skipped.
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // Skipped.
            }

            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println("hahah");
                if (e.getKeyCode() == 27) {
                    if (System.currentTimeMillis() - prev < interval)
                        balloon.hide(); // Two consecutive times
                    prev = System.currentTimeMillis();
                    //if (StrUtil.isEmpty()) return; // Skipped.

                    String canonicalPath = psiFile.getVirtualFile().getCanonicalPath();
                    NoteFile noteFile = new NoteFile();
                    noteFile.setVersion(noteFileService.generateVersion(canonicalPath));


                    noteFileService.saveNote(canonicalPath,
                            editor.getDocument().getLineNumber(editor.getCaretModel().getOffset()),editorPane.getText());

                    balloon.hide();
                }
            }
        });*/





       /* ActionGroup actionGroup = new ActionGroup() {
            @NotNull
            @Override
            public AnAction[] getChildren(@Nullable AnActionEvent anActionEvent) {
                AnAction anAction = new AnAction() {

                    @Override
                    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {

                    }
                };
                return new AnAction[0];
            }
        };
*/
        // 获取plugin.xml配置的事件组
      //  DefaultActionGroup group = (DefaultActionGroup) ActionManager.getInstance().getAction("Test-Group-id");
// 清空所有事件
       // group.removeAll();
// 添加一个事件AnAction
      /*  group.add(new AnAction("新增") {
            @Override
            public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
                System.out.println("this is new Anaction.....");
                final JBPopupFactory popupFactory = JBPopupFactory.getInstance();
                final Balloon balloon = popupFactory.createDialogBalloonBuilder(remarkEditor.getPanel1(), myText)
                        .setFadeoutTime(5000)
                        .setDialogMode(true)
                        .setRequestFocus(true)
                        .createBalloon();

                balloon.show(popupFactory.guessBestPopupLocation(editor), Balloon.Position.below);
                editorPane.requestFocus();



                editorPane.addKeyListener(new KeyListener() {
                    final long interval = 300;
                    long prev = System.currentTimeMillis();

                    @Override
                    public void keyTyped(KeyEvent e) {
                        // Skipped.
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        // Skipped.
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                        System.out.println("hahah");
                        if (e.getKeyCode() == 27) {
                            if (System.currentTimeMillis() - prev < interval)
                                balloon.hide(); // Two consecutive times
                            prev = System.currentTimeMillis();
                            //if (StrUtil.isEmpty(editorPane.getText())) return; // Skipped.

                            balloon.hide();
                        }
                    }
                });
            }
        });*/
// 获取工程的选项对象ListPoup
     //   JBPopupFactory popupFactory = JBPopupFactory.getInstance();
       // ListPopup myPopup = JBPopupFactory.getInstance().createActionGroupPopup("MyPopup", group, e.getDataContext(),
       //         JBPopupFactory.ActionSelectionAid.SPEEDSEARCH, false);
// ListPopub的显示位置，showCenteredInCurrentWindow当前窗口中间弹出，showInCenterOf在一个组件中间弹出

//        myPopup.showInCenterOf(component);


    }
}
