package com.github.maximtereshchenko.games.bricks.event;

public sealed interface Event permits AssetsLoaded, DifficultySelected, DifficultySelectionRequested, LevelCompleted, LevelFailed, LevelSelected {}
