package com.github.maximtereshchenko.games.snakes.session;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class InterfaceTextCenterAlignmentSystem implements System {

    private final Iterable<Entity> centerAlignedEntities;
    private final Viewport viewport;
    private final ScaledFont scaledFont;
    private final GlyphLayout glyphLayout;

    InterfaceTextCenterAlignmentSystem(
        World world,
        Viewport viewport,
        ScaledFont scaledFont,
        GlyphLayout glyphLayout
    ) {
        this.centerAlignedEntities = world.entities(
            new Query()
                .all(
                    CenterAligned.class,
                    InterfaceText.class,
                    InterfacePosition.class
                )
        );
        this.viewport = viewport;
        this.scaledFont = scaledFont;
        this.glyphLayout = glyphLayout;
    }

    @Override
    public void update(WorldEdit worldEdit, float deltaTimeSeconds) {
        for (var centerAlignedEntity : centerAlignedEntities) {
            var interfaceText = centerAlignedEntity.component(InterfaceText.class);
            scaledFont.use(
                interfaceText.scale,
                bitmapFont -> centerAlignedEntity.component(InterfacePosition.class).x =
                    centeredX(bitmapFont, interfaceText.value)
            );
        }
    }

    private float centeredX(BitmapFont bitmapFont, String text) {
        glyphLayout.setText(bitmapFont, text);
        return (viewport.getWorldWidth() - glyphLayout.width) / 2;
    }
}
