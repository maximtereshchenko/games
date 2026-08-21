package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.bricks.screen.view.Indicator;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

import java.util.function.Function;

final class IndicatorSynchronizationSystem<T> implements System {

    private final Iterable<Entity> entities;
    private final Indicator indicator;
    private final Class<T> componentType;
    private final Function<T, Integer> function;

    IndicatorSynchronizationSystem(
        Registry registry,
        Indicator indicator,
        Class<T> componentType,
        Function<T, Integer> function
    ) {
        this.entities = registry.entities(
            new Query().all(componentType)
        );
        this.indicator = indicator;
        this.componentType = componentType;
        this.function = function;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var entity : entities) {
            var component = entity.component(componentType);
            indicator.update(function.apply(component));
        }
    }
}
