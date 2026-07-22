package com.github.maximtereshchenko.games.snakes.event;

import com.github.maximtereshchenko.games.snakes.configuration.Mode;

public record ModeSelected(Mode mode) implements ApplicationEvent {}
