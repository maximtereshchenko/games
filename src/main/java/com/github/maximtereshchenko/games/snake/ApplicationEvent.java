package com.github.maximtereshchenko.games.snake;

sealed interface ApplicationEvent
    permits AssetsLoaded,
    ModeSelected,
    SnakeSessionEnded,
    TitleScreenFinished {}
