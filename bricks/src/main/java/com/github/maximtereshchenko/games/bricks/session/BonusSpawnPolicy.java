package com.github.maximtereshchenko.games.bricks.session;

import java.util.List;
import java.util.Map;

record BonusSpawnPolicy(
    float chance,
    Map<List<Object>, Float> componentChances
) {}
