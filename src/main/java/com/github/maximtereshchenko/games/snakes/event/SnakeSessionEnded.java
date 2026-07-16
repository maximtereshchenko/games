package com.github.maximtereshchenko.games.snakes.event;

import com.github.maximtereshchenko.games.snakes.session.SessionStatistics;

import java.util.Map;

public record SnakeSessionEnded(Map<SessionStatistics, Integer> statistics) implements ApplicationEvent {}
