package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.*;
import com.github.maximtereshchenko.ecs.System;

import java.util.List;

abstract class LocalizableInterfaceTextSystem implements System {

    private final Iterable<Entity> localizableInterfaceTextEntities;

    LocalizableInterfaceTextSystem(World world, Class<?> tag) {
        this.localizableInterfaceTextEntities = world.entities(
            new Query().all(tag, LocalizableInterfaceText.class)
        );
    }

    @Override
    public final void update(WorldEdit worldEdit, float deltaTimeSeconds) {
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
