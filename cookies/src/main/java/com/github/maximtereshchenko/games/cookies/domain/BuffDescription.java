package com.github.maximtereshchenko.games.cookies.domain;

public sealed interface BuffDescription permits FrenzyDescription {

    float durationSeconds();
}
