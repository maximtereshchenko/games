package com.github.maximtereshchenko.snakes.event;

import com.github.maximtereshchenko.snakes.session.SessionStatistics;

import java.util.Map;

public record SnakeSessionEnded(Map<SessionStatistics, Integer> statistics) implements ApplicationEvent {}
