package com.github.maximtereshchenko.games.cookies.domain;

interface UnlockRequirement {

    boolean isSatisfied(PlayerProgress playerProgress);
}
