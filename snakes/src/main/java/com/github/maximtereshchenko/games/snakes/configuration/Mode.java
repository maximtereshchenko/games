package com.github.maximtereshchenko.games.snakes.configuration;

import com.badlogic.gdx.graphics.Color;
import com.github.maximtereshchenko.games.snakes.session.PaletteColor;
import com.github.maximtereshchenko.games.snakes.session.WorldDimensions;

import java.util.Map;

public record Mode(
    String name,
    WorldDimensions worldDimensions,
    float interfaceViewportHeight,
    String entities,
    Map<PaletteColor, Color> palette,
    ModeUnlockRequirements modeUnlockRequirements
) {}
