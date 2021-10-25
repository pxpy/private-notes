package com.theblind.privatenotes.core.service.impl;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.theblind.privatenotes.core.Config;
import com.theblind.privatenotes.core.service.ConfigService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public class ConfigServiceImpl implements ConfigService {

    volatile Config configCache = null;

    @Override
    public Config get() {
        if (Objects.isNull(configCache)) {
            ConfigPersistenceThroughIdea persistence = ApplicationManager.getApplication().getService(ConfigPersistenceThroughIdea.class);
            configCache = persistence.getConfig();
        }
        return configCache;
    }

    @Override
    public void save(Config config) {
        ConfigPersistenceThroughIdea persistence = ApplicationManager.getApplication().getService(ConfigPersistenceThroughIdea.class);
        persistence.setConfig(config);
        configCache = config;
    }


    @State(name = "Config", storages = {@Storage(value = "privateNotes-config.xml",
            roamingType = RoamingType.DISABLED)})
    public static class ConfigPersistenceThroughIdea implements PersistentStateComponent<ConfigPersistenceThroughIdea> {
        private Config config = new Config();

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
            XmlSerializerUtil.copyBean(configPersistenceThroughIdea, this);
        }
    }


}
