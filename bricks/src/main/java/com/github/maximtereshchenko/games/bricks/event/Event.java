package com.github.maximtereshchenko.games.bricks.event;

public sealed interface Event permits LevelCompleted, LevelFailed {}
