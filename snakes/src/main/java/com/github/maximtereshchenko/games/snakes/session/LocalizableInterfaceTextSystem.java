package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

import java.util.List;

abstract class LocalizableInterfaceTextSystem implements System {

    private final Iterable<Entity> localizableInterfaceTextEntities;

    LocalizableInterfaceTextSystem(Registry registry, Class<?> tag) {
        this.localizableInterfaceTextEntities = registry.entities(
            new Query().all(tag, LocalizableInterfaceText.class)
        );
    }

    @Override
    public final void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var localizableInterfaceTextEntity : localizableInterfaceTextEntities) {
            var variables = localizableInterfaceTextEntity.component(
                    LocalizableInterfaceText.class
                )
                .variables();
            variables.clear();
            addVariables(variables);
        }
    }

    abstract void addVariables(List<Object> variables);
}
