package com.github.maximtereshchenko.games.bricks.event;

public record LevelCompleted(
    String difficulty,
    int level,
    int stars
) implements Event {}
