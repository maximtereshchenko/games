package com.github.maximtereshchenko.snakes.session;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.snakes.configuration.Mode;
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
        for (var result : dominion.findEntitiesWith(Colored.class, Position.class, Background.class)) {
            draw(result.comp1(), result.comp2());
        }
        for (var result : dominion.findEntitiesWith(Colored.class, Position.class).without(Background.class)) {
            draw(result.comp1(), result.comp2());
        }
        shapeRenderer.end();
    }

    private void draw(Colored colored, Position position) {
        shapeRenderer.setColor(mode.palette().get(colored));
        shapeRenderer.rect(position.x, position.y, 1, 1);
    }
}
