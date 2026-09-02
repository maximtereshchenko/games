package com.github.maximtereshchenko.games.cookies.domain;

public record GeneratorBought(Building generator, int newAmount) implements Event {}
