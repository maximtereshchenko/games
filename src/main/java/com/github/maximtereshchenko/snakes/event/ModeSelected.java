package com.github.maximtereshchenko.snakes.event;

import com.github.maximtereshchenko.snakes.configuration.Mode;

public record ModeSelected(Mode mode) implements ApplicationEvent {}
