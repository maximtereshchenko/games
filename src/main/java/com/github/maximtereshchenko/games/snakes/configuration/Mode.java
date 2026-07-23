package com.github.maximtereshchenko.games.snakes.configuration;

import com.badlogic.gdx.graphics.Color;
import com.github.maximtereshchenko.games.snakes.session.Colored;
import com.github.maximtereshchenko.games.snakes.session.WorldDimensions;

import java.util.List;
import java.util.Map;

public record Mode(
    String name,
    WorldDimensions worldDimensions,
    float interfaceViewportHeight,
    List<Object[]> entities,
    Map<Colored, Color> palette,
    ModeUnlockRequirements modeUnlockRequirements
) {}
