package com.theblind.privatenotes.action.menu;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.vfs.VirtualFile;
import com.theblind.privatenotes.action.ActionHandle;
import com.theblind.privatenotes.action.ActionHandleFactory;
import org.jetbrains.annotations.NotNull;

public class OtherOperateAnAction extends AnAction {

    ActionHandle editHandle;


    public OtherOperateAnAction() {
        editHandle = ActionHandleFactory.getActionHandle(ActionHandle.Operate.EDIT);
    }

    @Override
    public void update(@NotNull AnActionEvent anActionEvent) {
        super.update(anActionEvent);
        Project project = CommonDataKeys.PROJECT.getData(anActionEvent.getDataContext());
        Editor editor = CommonDataKeys.EDITOR.getData(anActionEvent.getDataContext());
        VirtualFile virtualFile = CommonDataKeys.VIRTUAL_FILE.getData(anActionEvent.getDataContext());
        String title = ActionHandle.Operate.EDIT.getTitle();
        anActionEvent.getPresentation().setEnabledAndVisible(editHandle.isVisible(project, editor, virtualFile));
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        DefaultActionGroup actionGroup = (DefaultActionGroup) ActionManager.getInstance().getAction("Private.Notes.OtherOperate");

        ListPopup listPopup = JBPopupFactory.getInstance().createActionGroupPopup("[Note] 其它操作", actionGroup, anActionEvent.getDataContext(), JBPopupFactory.ActionSelectionAid.SPEEDSEARCH, false);
        listPopup.showInBestPositionFor(anActionEvent.getDataContext());
    }


}
