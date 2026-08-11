package com.github.maximtereshchenko.games.snakes.session;

import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class LocalizationSystem implements System {

    private final Iterable<Entity> interfaceTextEntities;
    private final I18NBundle bundle;

    LocalizationSystem(Registry registry, I18NBundle bundle) {
        this.interfaceTextEntities = registry.entities(
            new Query().all(LocalizableInterfaceText.class, InterfaceText.class)
        );
        this.bundle = bundle;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
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
