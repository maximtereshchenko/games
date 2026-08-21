package com.github.maximtereshchenko.games.bricks.screen.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;
import com.github.maximtereshchenko.games.bricks.session.TextureRenderer;
import com.github.maximtereshchenko.games.ecs.Registry;

public final class SessionView extends Table {

    public SessionView(
        Configuration configuration,
        Indicator starsIndicator,
        Indicator livesIndicator,
        Viewport viewport,
        Registry registry,
        AssetManager assetManager
    ) {
        add(starsIndicator)
            .align(Align.left)
            .pad(Value.percentHeight(0.01f, this));
        add(livesIndicator)
            .align(Align.right)
            .pad(Value.percentHeight(0.01f, this))
            .row();
        add(
            new SessionWidget(
                viewport,
                registry,
                new TextureRenderer(
                    registry,
                    configuration,
                    assetManager
                )
            )
        )
            .colspan(2)
            .expand()
            .fill();
    }
}