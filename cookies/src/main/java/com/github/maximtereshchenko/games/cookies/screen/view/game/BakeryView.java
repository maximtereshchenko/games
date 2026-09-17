package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.bakery.BakeryPanel;
import com.github.maximtereshchenko.games.cookies.screen.view.game.notification.NotificationOverlay;
import com.github.maximtereshchenko.games.cookies.screen.view.game.store.StorePanel;

import java.time.Clock;
import java.util.Random;

public final class BakeryView extends Stack {

    public BakeryView(
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Random random,
        Clock clock
    ) {
        var table = new Table();
        table.setBackground(assetManager.get(assets.game().skin()).get(Style.class).background);
        table.defaults().growY();
        table.add(
                new BakeryPanel(
                    assetManager,
                    assets,
                    bigDecimalFormatter,
                    bakeryService,
                    random
                )
            )
            .width(Value.percentWidth(0.3f, this));
        addBeam(table, assetManager, assets);
        table.add(
                new NavigablePanel(
                    assetManager,
                    assets,
                    bigDecimalFormatter,
                    bakeryService,
                    random,
                    clock
                )
            )
            .growX();
        addBeam(table, assetManager, assets);
        table.add(
            new FocusableScrollPane<>(
                assetManager,
                assets,
                new StorePanel(
                    assetManager,
                    assets,
                    bigDecimalFormatter,
                    bakeryService,
                    random
                )
            )
        );
        add(table);
        add(
            new NotificationOverlay(
                assetManager,
                assets,
                bakeryService
            )
        );
    }

    private void addBeam(Table table, AssetManager assetManager, Assets assets) {
        table.add(new BeamWidget(assetManager, assets, "vertical"))
            .width(Value.prefWidth);
    }

    private static final class Style {

        Drawable background;
    }
}
