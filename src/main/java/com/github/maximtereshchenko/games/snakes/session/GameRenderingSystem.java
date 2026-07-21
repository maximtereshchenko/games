package com.github.maximtereshchenko.games.snakes.session;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.snakes.Mode;
import dev.dominion.ecs.api.Dominion;

final class GameRenderingSystem implements System {

    private final Viewport viewport;
    private final ShapeRenderer shapeRenderer;
    private final Dominion dominion;
    private final Mode mode;

    GameRenderingSystem(
        Viewport viewport,
        ShapeRenderer shapeRenderer,
        Dominion dominion,
        Mode mode
    ) {
        this.viewport = viewport;
        this.shapeRenderer = shapeRenderer;
        this.dominion = dominion;
        this.mode = mode;
    }

    @Override
    public void run(float deltaTime) {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        var palette = mode.palette();
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
