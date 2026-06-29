package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import dev.dominion.ecs.api.Dominion;

final class StandaloneRenderingSystem {

    private final FitViewport fitViewport;
    private final ShapeRenderer shapeRenderer;
    private final Dominion dominion;

    StandaloneRenderingSystem(
        FitViewport fitViewport,
        ShapeRenderer shapeRenderer,
        Dominion dominion
    ) {
        this.fitViewport = fitViewport;
        this.shapeRenderer = shapeRenderer;
        this.dominion = dominion;
    }

    void render() {
        ScreenUtils.clear(Color.BLACK);
        fitViewport.apply();
        shapeRenderer.setProjectionMatrix(fitViewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(
            0,
            0,
            fitViewport.getScreenWidth(),
            fitViewport.getScreenHeight()
        );
        for (var result : dominion.findEntitiesWith(Visible.class, Position.class)) {
            shapeRenderer.setColor(result.comp1().color());
            var position = result.comp2();
            shapeRenderer.rect(position.x, position.y, 1, 1);
        }
        shapeRenderer.end();
    }

    void dispose() {
        shapeRenderer.dispose();
    }
}
