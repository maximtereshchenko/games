package com.github.maximtereshchenko.games.bricks.session;

import java.util.Map;

record BonusSpawnPolicy(
    float chance,
    Map<String, Float> bonusChances
) {}
