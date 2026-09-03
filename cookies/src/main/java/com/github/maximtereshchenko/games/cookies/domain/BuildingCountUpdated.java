package com.github.maximtereshchenko.games.cookies.domain;

public record BuildingCountUpdated(Building building, int count) implements Event {}
