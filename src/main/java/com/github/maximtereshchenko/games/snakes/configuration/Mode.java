package com.github.maximtereshchenko.games.snakes.configuration;

import com.badlogic.gdx.graphics.Color;
import com.github.maximtereshchenko.games.snakes.session.Colored;
import com.github.maximtereshchenko.games.snakes.session.RelativeDirection;
import com.github.maximtereshchenko.games.snakes.session.WorldDimensions;

import java.util.List;
import java.util.Map;
import java.util.Set;

public record Mode(
    String name,
    WorldDimensions worldDimensions,
    float interfaceViewportHeight,
    List<Object[]> entities,
    Set<RelativeDirection> legalTurnDirections, //TODO
    Map<Colored, Color> palette,
    ModeUnlockRequirements modeUnlockRequirements
) {}
