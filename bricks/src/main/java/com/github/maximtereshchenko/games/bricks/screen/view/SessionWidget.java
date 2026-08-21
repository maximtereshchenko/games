package com.github.maximtereshchenko.games.bricks.screen.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.bricks.session.TextureRenderer;
import com.github.maximtereshchenko.games.ecs.Registry;

final class SessionWidget extends Widget {

    private final Viewport viewport;
    private final Registry registry;
    private final TextureRenderer textureRenderer;

    SessionWidget(
        Viewport viewport,
        Registry registry,
        TextureRenderer textureRenderer
    ) {
        this.viewport = viewport;
        this.registry = registry;
        this.textureRenderer = textureRenderer;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        registry.update(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        updateViewport();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        textureRenderer.draw(batch);
    }

    private void updateViewport() {
        var bottomLeft = new Vector2();
        var topRight = new Vector2(getWidth(), getHeight());
        project(bottomLeft);
        project(topRight);
        viewport.update(
            Math.round(topRight.x - bottomLeft.x),
            Math.round(topRight.y - bottomLeft.y),
            true
        );
        viewport.setScreenPosition(
            viewport.getScreenX() + Math.round(bottomLeft.x),
            viewport.getScreenY() + Math.round(bottomLeft.y)
        );
        viewport.apply();
    }

    private void project(Vector2 vector2) {
        localToStageCoordinates(vector2);
        getStage().getViewport().project(vector2);
    }
}
