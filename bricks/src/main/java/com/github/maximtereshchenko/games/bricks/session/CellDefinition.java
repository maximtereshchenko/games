package com.github.maximtereshchenko.games.bricks.session;

sealed interface CellDefinition permits BrickDefinition, EmptyCellDefinition, WallDefinition {}
