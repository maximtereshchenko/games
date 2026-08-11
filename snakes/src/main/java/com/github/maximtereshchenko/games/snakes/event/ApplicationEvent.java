package com.github.maximtereshchenko.games.snakes.event;

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
