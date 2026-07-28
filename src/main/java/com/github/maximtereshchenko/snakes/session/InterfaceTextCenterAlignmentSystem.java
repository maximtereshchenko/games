package com.github.maximtereshchenko.snakes.session;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class InterfaceTextCenterAlignmentSystem extends TurnBasedSystem {

    private final Iterable<Entity> centerAlignedEntities;
    private final Viewport viewport;
    private final BitmapFont bitmapFont;

    InterfaceTextCenterAlignmentSystem(
        World world,
        Viewport viewport,
        BitmapFont bitmapFont
    ) {
        super(world);
        this.centerAlignedEntities = world.entities(
            new Query().all(CenterAligned.class, InterfaceText.class, InterfacePosition.class)
        );
        this.viewport = viewport;
        this.bitmapFont = bitmapFont;
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var centerAlignedEntity : centerAlignedEntities) {
            centerAlignedEntity.component(InterfacePosition.class).x = centeredX(
                centerAlignedEntity.component(InterfaceText.class).value
            );
        }
    }

    private float centeredX(String text) {
        return (viewport.getWorldWidth() - new GlyphLayout(bitmapFont, text).width) / 2;
    }
}
