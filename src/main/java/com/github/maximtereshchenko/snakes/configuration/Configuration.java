package com.github.maximtereshchenko.snakes.configuration;

import java.util.List;

public record Configuration(
    String preferencesName,
    Assets assets,
    float defaultMusicVolume,
    List<Mode> modes
) {}
