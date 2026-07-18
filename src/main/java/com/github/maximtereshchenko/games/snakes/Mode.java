package com.github.maximtereshchenko.games.snakes;

import com.badlogic.gdx.graphics.Color;
import com.github.maximtereshchenko.games.snakes.session.Colored;
import com.github.maximtereshchenko.games.snakes.session.RelativeDirection;

import java.util.Map;
import java.util.Set;

public record Mode(
    String name,
    float gameInterval,
    int headMovementSidewaysCycle,
    int headMovementSidewaysInterval,
    Set<RelativeDirection> legalTurnDirections,
    Map<Colored, Color> palette,
    ModeUnlockRequirements modeUnlockRequirements
) {}
