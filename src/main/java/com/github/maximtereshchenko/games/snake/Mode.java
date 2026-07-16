package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.graphics.Color;

import java.util.Map;
import java.util.Set;

record Mode(
    String name,
    float gameInterval,
    Set<LegalTurn> legalTurns,
    Map<Colored, Color> palette,
    ModeUnlockRequirements modeUnlockRequirements
) {}
