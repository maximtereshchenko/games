package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Scheduler;

record SnakeSession(Dominion dominion, Scheduler scheduler) {}
