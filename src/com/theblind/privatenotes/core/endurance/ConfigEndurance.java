package com.theblind.privatenotes.core.endurance;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.RoamingType;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.theblind.privatenotes.core.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name="Config",storages = {@Storage(value = "privateNotes-config.xml",
        roamingType = RoamingType.DISABLED)})
public class ConfigEndurance implements PersistentStateComponent<ConfigEndurance> {

    Config config;


    @Nullable
    @Override
    public ConfigEndurance getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ConfigEndurance configEndurance) {
        XmlSerializerUtil.copyBean(configEndurance,this);
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

}
