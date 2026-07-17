package com.github.maximtereshchenko.games.snakes.event;

public sealed interface ApplicationEvent
    permits AssetsLoaded,
    ModeSelected,
    SnakeSessionEnded,
    StatisticsScreenFinished,
    StatisticsRequested,
    TitleScreenFinished,
    CreditsScreenFinished,
    CreditsRequested {}
