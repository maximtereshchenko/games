package com.github.maximtereshchenko.snakes.event;

public sealed interface ApplicationEvent
    permits AssetsLoaded,
    ModeSelected,
    SessionEnded,
    StatisticsScreenFinished,
    StatisticsRequested,
    TitleScreenFinished,
    CreditsScreenFinished,
    CreditsRequested,
    SettingsScreenFinished,
    SettingsRequested {}
