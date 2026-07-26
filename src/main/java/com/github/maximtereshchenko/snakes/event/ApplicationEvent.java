package com.github.maximtereshchenko.snakes.event;

public sealed interface ApplicationEvent
    permits AssetsLoaded,
    ModeSelected,
    SnakeSessionEnded,
    StatisticsScreenFinished,
    StatisticsRequested,
    TitleScreenFinished,
    CreditsScreenFinished,
    CreditsRequested,
    SettingsScreenFinished,
    SettingsRequested {}
