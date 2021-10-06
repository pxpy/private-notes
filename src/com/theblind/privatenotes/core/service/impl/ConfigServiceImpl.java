package com.theblind.privatenotes.core.service.impl;

import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.theblind.privatenotes.core.Config;
import com.theblind.privatenotes.core.service.ConfigService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class ConfigServiceImpl implements ConfigService {
    @Override
    public Config get() {
        ConfigPersistenceThroughIdea persistence = ServiceManager.getService(ConfigPersistenceThroughIdea.class);
        return persistence.getConfig();
    }

    @Override
    public void save(Config config) {
        ConfigPersistenceThroughIdea persistence = ServiceManager.getService(ConfigPersistenceThroughIdea.class);
        persistence.setConfig(config);
    }

    @State(name="Config",storages = {@Storage(value = "privateNotes-config.xml",
            roamingType = RoamingType.DISABLED)})
    static class  ConfigPersistenceThroughIdea implements PersistentStateComponent<ConfigPersistenceThroughIdea>{
        Config config;

        public Config getConfig() {
            return config;
        }

        public void setConfig(Config config) {
            this.config = config;
        }

        @Nullable
        @Override
        public ConfigPersistenceThroughIdea getState() {
            return this;
        }

        @Override
        public void loadState(@NotNull ConfigPersistenceThroughIdea configPersistenceThroughIdea) {
            XmlSerializerUtil.copyBean(configPersistenceThroughIdea,this);
        }



    }









}
