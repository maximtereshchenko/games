package com.github.maximtereshchenko.games.cookies.domain;

public record TransactionModeUpdated(TransactionType type, TransactionCount count) implements Event {}
