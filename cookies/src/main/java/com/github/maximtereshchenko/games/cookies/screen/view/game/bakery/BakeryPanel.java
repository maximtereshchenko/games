package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;

import java.util.Random;

public final class BakeryPanel extends Container<Stack> {

    public BakeryPanel(
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Random random
    ) {
        fill();
        clip();
        var cookieWidget = new CookieWidget(
            assetManager,
            assets,
            bigDecimalFormatter,
            bakeryService,
            random
        );
        var fallingCookiesWidget = new FallingCookiesWidget(
            assetManager,
            assets,
            bakeryService,
            random
        );
        cookieWidget.addListener(
            new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    fallingCookiesWidget.addFallingCookieWidget();
                }
            }
        );
        var stack = new Stack();
        stack.add(new BottomOverlayWidget(assetManager, assets));
        stack.add(fallingCookiesWidget);
        stack.add(cookieWidget);
        stack.add(
            new BakingStatisticsWidget(
                assetManager,
                assets,
                bigDecimalFormatter,
                bakeryService
            )
        );
        stack.add(new MilkWidget(assetManager, assets, bakeryService));
        stack.add(
            new GoldenCookieOverlay(
                assetManager,
                assets,
                bigDecimalFormatter,
                bakeryService,
                random
            )
        );
        stack.add(
            new BuffOverlay(
                assetManager,
                assets,
                bakeryService
            )
        );
        setActor(stack);
    }
}
