package com.github.maximtereshchenko.snakes.configuration;

import com.badlogic.gdx.graphics.Color;
import com.github.maximtereshchenko.snakes.session.Colored;
import com.github.maximtereshchenko.snakes.session.WorldDimensions;

import java.util.Map;

public record Mode(
    String name,
    WorldDimensions worldDimensions,
    float interfaceViewportHeight,
    String entities,
    Map<Colored, Color> palette,
    ModeUnlockRequirements modeUnlockRequirements
) {}
