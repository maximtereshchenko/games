package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;

import java.util.List;

record SnakeSession(Dominion dominion, List<System> systems) {}
