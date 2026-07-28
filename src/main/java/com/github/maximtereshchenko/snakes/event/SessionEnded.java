package com.github.maximtereshchenko.snakes.event;

import com.github.maximtereshchenko.snakes.session.SessionMetric;

import java.util.Map;

public record SessionEnded(Map<SessionMetric, Integer> statistics) implements ApplicationEvent {}
