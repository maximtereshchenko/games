package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.dominion.ecs.api.Dominion;

final class GameRenderingSystem implements System {

    private final Viewport viewport;
    private final ShapeRenderer shapeRenderer;
    private final Dominion dominion;

    GameRenderingSystem(
        Viewport viewport,
        ShapeRenderer shapeRenderer,
        Dominion dominion
    ) {
        this.viewport = viewport;
        this.shapeRenderer = shapeRenderer;
        this.dominion = dominion;
    }

    @Override
    public void run(float deltaTime) {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(
            0,
            0,
            viewport.getScreenWidth(),
            viewport.getScreenHeight()
        );
        for (var result : dominion.findEntitiesWith(Visible.class, Position.class)) {
            shapeRenderer.setColor(result.comp1().color());
            var position = result.comp2();
            shapeRenderer.rect(position.x, position.y, 1, 1);
        }
        shapeRenderer.end();
    }
}
