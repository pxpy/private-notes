package com.theblind.privatenotes.core.service;

import com.theblind.privatenotes.core.Config;

public interface ConfigService {
    Config get();
    void save(Config config);
}
