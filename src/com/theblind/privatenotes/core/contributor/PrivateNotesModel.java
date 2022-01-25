package com.theblind.privatenotes.core.contributor;

import com.intellij.ide.util.gotoByName.FilteringGotoByModel;
import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PrivateNotesModel extends FilteringGotoByModel<FileType> {


    protected PrivateNotesModel(@NotNull Project project, @NotNull ChooseByNameContributor[] contributors) {
        super(project, contributors);
    }

    @Nullable
    @Override
    protected FileType filterValueFor(NavigationItem navigationItem) {
        return null;
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @Override
    public String getPromptText() {
        return null;
    }

    @NotNull
    @Override
    public String getNotInMessage() {
        return null;
    }

    @NotNull
    @Override
    public String getNotFoundMessage() {
        return null;
    }

    @Nullable
    @Override
    public String getCheckBoxName() {
        return null;
    }

    @Override
    public boolean loadInitialCheckBoxState() {
        return false;
    }

    @Override
    public void saveInitialCheckBoxState(boolean b) {

    }

    @NotNull
    @Override
    public String[] getSeparators() {
        return new String[0];
    }

    @Nullable
    @Override
    public String getFullName(@NotNull Object o) {
        return null;
    }

    @Override
    public boolean willOpenEditor() {
        return false;
    }
}
