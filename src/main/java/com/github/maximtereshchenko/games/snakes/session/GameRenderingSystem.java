package com.github.maximtereshchenko.games.snakes.session;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.dominion.ecs.api.Dominion;

import java.util.Map;

final class GameRenderingSystem implements System {

    private final Viewport viewport;
    private final ShapeRenderer shapeRenderer;
    private final Dominion dominion;
    private final Map<Colored, Color> palette;

    GameRenderingSystem(
        Viewport viewport,
        ShapeRenderer shapeRenderer,
        Dominion dominion,
        Map<Colored, Color> palette
    ) {
        this.viewport = viewport;
        this.shapeRenderer = shapeRenderer;
        this.dominion = dominion;
        this.palette = palette;
    }

    @Override
    public void run(float deltaTime) {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(palette.get(Colored.BACKGROUND));
        shapeRenderer.rect(
            0,
            0,
            viewport.getWorldWidth(),
            viewport.getWorldHeight()
        );
        for (var result : dominion.findEntitiesWith(Colored.class, Position.class)) {
            shapeRenderer.setColor(palette.get(result.comp1()));
            var position = result.comp2();
            shapeRenderer.rect(position.x, position.y, 1, 1);
        }
        shapeRenderer.end();
    }
}
