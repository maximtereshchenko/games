package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

import java.util.List;
import java.util.Optional;

public abstract class TooltipPanel extends Table {

    protected TooltipPanel(
        AssetManager assetManager,
        Assets assets
    ) {
        pad(8, 16, 8, 16);
        add(icon(assetManager, assets))
            .width(Value.prefWidth)
            .padRight(6);
        add(header(assetManager, assets))
            .growX()
            .row();
        addSeparator(assetManager, assets);
        description(assetManager, assets)
            .ifPresent(
                description -> add(description)
                    .colspan(2)
                    .growX()
                    .padBottom(8)
                    .row()
            );
        flavorText(assetManager, assets)
            .ifPresent(
                flavorText -> add(flavorText)
                    .colspan(2)
                    .padBottom(8)
                    .growX()
                    .row()
            );
        footer(assetManager, assets)
            .ifPresent(footer -> addFooter(assetManager, assets, footer));

    }

    protected abstract Image icon(AssetManager assetManager, Assets assets);

    protected abstract Label name(AssetManager assetManager, Assets assets);

    protected abstract Optional<Table> value(AssetManager assetManager, Assets assets);

    protected abstract List<Badge> badges(AssetManager assetManager, Assets assets);

    protected abstract Optional<Label> description(AssetManager assetManager, Assets assets);

    protected abstract Optional<FlavorTextLabel> flavorText(AssetManager assetManager, Assets assets);

    protected abstract Optional<Actor> footer(AssetManager assetManager, Assets assets);

    private void addSeparator(AssetManager assetManager, Assets assets) {
        add(new PopUpSeparator(assetManager, assets))
            .colspan(2)
            .growX()
            .padBottom(8)
            .row();
    }

    private void addFooter(AssetManager assetManager, Assets assets, Actor actor) {
        addSeparator(assetManager, assets);
        add(actor)
            .colspan(2)
            .growX();
    }

    private Table header(
        AssetManager assetManager,
        Assets assets
    ) {
        var table = new Table();
        table.add(name(assetManager, assets))
            .growX();
        table.add(value(assetManager, assets).orElse(null))
            .row();
        table.add(badgeLine(assetManager, assets))
            .colspan(2)
            .left();
        return table;
    }

    private Table badgeLine(
        AssetManager assetManager,
        Assets assets
    ) {
        var table = new Table();
        for (var badge : badges(assetManager, assets)) {
            table.add(badge).padRight(4);
        }
        return table;
    }
}
