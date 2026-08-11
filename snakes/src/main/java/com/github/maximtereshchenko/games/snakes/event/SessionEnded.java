package com.github.maximtereshchenko.games.snakes.event;

import com.github.maximtereshchenko.games.snakes.session.SessionMetric;

import java.util.Map;

public record SessionEnded(Map<SessionMetric, Integer> statistics) implements ApplicationEvent {}
