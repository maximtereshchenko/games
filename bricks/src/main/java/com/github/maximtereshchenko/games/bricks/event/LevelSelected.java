package com.github.maximtereshchenko.games.bricks.event;

public record LevelSelected(String difficulty, int level) implements Event {}
