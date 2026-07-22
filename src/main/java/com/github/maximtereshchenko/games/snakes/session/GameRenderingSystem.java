package com.github.maximtereshchenko.games.snakes.session;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.snakes.configuration.Mode;
import dev.dominion.ecs.api.Dominion;

import java.util.Comparator;

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
        dominion.findEntitiesWith(Colored.class, Position.class)
            .stream()
            .sorted(Comparator.comparing(results -> !results.entity().has(Background.class)))
            .forEach(results -> draw(results.comp1(), results.comp2()));
        shapeRenderer.end();
    }

    private void draw(Colored colored, Position position) {
        shapeRenderer.setColor(mode.palette().get(colored));
        shapeRenderer.rect(position.x, position.y, 1, 1);
    }
}
