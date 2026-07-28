package com.github.maximtereshchenko.snakes.session;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.ecs.*;
import com.github.maximtereshchenko.ecs.System;
import com.github.maximtereshchenko.snakes.configuration.Mode;

final class InterfaceRenderingSystem implements System {

    private final Iterable<Entity> interfaceTextEntities;
    private final Viewport viewport;
    private final SpriteBatch spriteBatch;
    private final ScaledFont scaledFont;
    private final Mode mode;

    InterfaceRenderingSystem(
        World world,
        Viewport viewport,
        SpriteBatch spriteBatch,
        ScaledFont scaledFont,
        Mode mode
    ) {
        this.interfaceTextEntities = world.entities(
            new Query()
                .all(
                    InterfaceText.class,
                    InterfacePosition.class,
                    Colored.class
                )
        );
        this.viewport = viewport;
        this.spriteBatch = spriteBatch;
        this.scaledFont = scaledFont;
        this.mode = mode;

    }

    @Override
    public void update(WorldEdit worldEdit, float deltaTimeSeconds) {
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        for (var interfaceEntity : interfaceTextEntities) {
            var interfaceText = interfaceEntity.component(InterfaceText.class);
            scaledFont.use(
                interfaceText.scale,
                bitmapFont -> draw(
                    bitmapFont,
                    interfaceText.value,
                    interfaceEntity.component(Colored.class),
                    interfaceEntity.component(InterfacePosition.class)
                )
            );
        }
        spriteBatch.end();
    }

    private void draw(
        BitmapFont bitmapFont,
        String text,
        Colored colored,
        InterfacePosition interfacePosition
    ) {
        bitmapFont.setColor(mode.palette().get(colored));
        bitmapFont.draw(
            spriteBatch,
            text,
            interfacePosition.x,
            interfacePosition.y
        );
    }
}
