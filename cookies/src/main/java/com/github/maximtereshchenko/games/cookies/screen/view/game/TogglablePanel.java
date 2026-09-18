package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

public final class TogglablePanel extends Container<Table> {

    public TogglablePanel(
        AssetManager assetManager,
        Assets assets,
        String title,
        Actor content
    ) {
        super(new Table());
        fill();
        var style = assetManager.get(assets.game().skin())
            .get(Style.class);
        background(style.bottomBackground);
        var table = getActor();
        table.background(style.topBackground);
        table.pad(6);
        table.top();
        addTitleSeparator(table, style);
        table.add(
                new Label(
                    title,
                    assetManager.get(assets.game().skin()),
                    "togglable-panel-title"
                )
            )
            .pad(12)
            .colspan(4)
            .row();
        addTitleSeparator(table, style);
        table.add().colspan(4).pad(12).row();
        table.add(content)
            .colspan(4)
            .grow();
    }

    private void addTitleSeparator(Table table, Style style) {
        table.add().growX();
        table.add(new Image(style.titleSeparator))
            .growX()
            .colspan(2);
        table.add().growX().row();
    }

    private static final class Style {

        Drawable bottomBackground;
        Drawable topBackground;
        Drawable titleSeparator;
    }
}
