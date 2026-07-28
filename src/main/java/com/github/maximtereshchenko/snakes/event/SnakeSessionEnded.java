package com.github.maximtereshchenko.snakes.event;

import com.github.maximtereshchenko.snakes.session.SessionMetric;

import java.util.Map;

public record SnakeSessionEnded(Map<SessionMetric, Integer> statistics) implements ApplicationEvent {}
