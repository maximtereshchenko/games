package com.github.maximtereshchenko.games.snakes.configuration;

import com.github.maximtereshchenko.games.snakes.session.Direction;
import com.github.maximtereshchenko.games.snakes.session.Position;
import com.github.maximtereshchenko.games.snakes.session.WorldDimensions;

import java.util.List;

public record Configuration(
    String preferencesName,
    WorldDimensions worldDimensions,
    float interfaceViewportHeight,
    int snakeFoodGrowth,
    Position snakeHeadPosition,
    Direction snakeHeadForwardDirection,
    int snakeLength,
    Assets assets,
    float defaultMusicVolume,
    List<Mode> modes
) {}
