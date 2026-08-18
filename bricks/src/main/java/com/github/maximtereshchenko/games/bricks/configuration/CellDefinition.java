package com.github.maximtereshchenko.games.bricks.configuration;

public sealed interface CellDefinition
    permits BrickDefinition, EmptyCellDefinition, WallDefinition {}
