package com.github.maximtereshchenko.snakes.session;

import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.ecs.*;
import com.github.maximtereshchenko.ecs.System;

final class LocalizationSystem implements System {

    private final Iterable<Entity> interfaceTextEntities;
    private final I18NBundle bundle;

    LocalizationSystem(World world, I18NBundle bundle) {
        this.interfaceTextEntities = world.entities(
            new Query().all(LocalizableInterfaceText.class, InterfaceText.class)
        );
        this.bundle = bundle;
    }

    @Override
    public void update(WorldEdit worldEdit, float deltaTimeSeconds) {
        for (var interfaceTextEntity : interfaceTextEntities) {
            var localizableInterfaceText =
                interfaceTextEntity.component(LocalizableInterfaceText.class);
            interfaceTextEntity.component(InterfaceText.class)
                .value = bundle.format(
                localizableInterfaceText.key(),
                localizableInterfaceText.variables()
                    .toArray()
            );
        }
    }
}
