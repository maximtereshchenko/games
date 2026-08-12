package com.github.maximtereshchenko.games.bricks.session;

import java.util.List;

record BonusSpawnPolicy(
    float chance,
    List<Object[]> components
) {}
