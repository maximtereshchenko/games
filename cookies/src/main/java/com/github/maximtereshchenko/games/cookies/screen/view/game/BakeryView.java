package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.view.game.bakery.BakeryPanel;
import com.github.maximtereshchenko.games.cookies.screen.view.game.notification.NotificationOverlay;
import com.github.maximtereshchenko.games.cookies.screen.view.game.store.StorePanel;

import java.util.Random;

public final class BakeryView extends Stack {

    public BakeryView(
        Skin skin,
        I18NBundle bundle,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Random random
    ) {
        var table = new Table();
        table.setBackground(skin.get(Style.class).background);
        table.defaults().growY();
        table.add(
                new BakeryPanel(
                    skin,
                    bundle,
                    bigDecimalFormatter,
                    bakeryService,
                    random
                )
            )
            .width(Value.percentWidth(0.3f, this));
        addBeam(table, skin);
        table.add(
                new NavigablePanel(
                    skin,
                    bundle,
                    bigDecimalFormatter,
                    bakeryService,
                    random
                )
            )
            .growX();
        addBeam(table, skin);
        table.add(
            new FocusableScrollPane<>(
                skin,
                new StorePanel(
                    skin,
                    bundle,
                    bigDecimalFormatter,
                    bakeryService
                )
            )
        );
        add(table);
        add(
            new NotificationOverlay(
                skin,
                bundle,
                bakeryService
            )
        );
    }

    private void addBeam(Table table, Skin skin) {
        table.add(new BeamWidget(skin, "vertical"))
            .width(Value.prefWidth);
    }

    private static final class Style {

        Drawable background;
    }
}
