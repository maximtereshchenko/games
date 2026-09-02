package com.github.maximtereshchenko.games.cookies.domain;

public record GeneratorBought(Generator generator, int newAmount) implements Event {}
