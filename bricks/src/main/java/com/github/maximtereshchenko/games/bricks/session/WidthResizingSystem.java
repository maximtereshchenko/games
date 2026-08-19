package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;
import com.github.maximtereshchenko.games.ecs.System;

abstract class WidthResizingSystem implements System {

    void resize(
        RegistryEdit registryEdit,
        Entity entity,
        Rectangle rectangle,
        float width
    ) {
        var newHalfWidth = width / 2;
        if (rectangle.halfWidth != newHalfWidth) {
            rectangle.halfWidth = newHalfWidth;
            registryEdit.addComponents(entity.id(), Resized.INSTANCE);
        }
    }
}
