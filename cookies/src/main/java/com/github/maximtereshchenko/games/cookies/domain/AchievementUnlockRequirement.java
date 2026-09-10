package com.github.maximtereshchenko.games.cookies.domain;

sealed interface AchievementUnlockRequirement extends UnlockRequirement
    permits CumulativeBakedUnlockRequirement {}
