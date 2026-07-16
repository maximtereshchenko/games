package com.github.maximtereshchenko.games.snake;

import java.util.Map;

record SnakeSessionEnded(Map<SessionStatistics, Integer> statistics) implements ApplicationEvent {}
