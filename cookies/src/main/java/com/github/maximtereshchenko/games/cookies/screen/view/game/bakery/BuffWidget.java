package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Buff;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.ColoredButton;
import com.github.maximtereshchenko.games.cookies.screen.view.game.PopUpFrame;
import com.github.maximtereshchenko.games.cookies.screen.view.game.SameTopHeightTooltipWidget;

final class BuffWidget extends ColoredButton {

    private final BakeryService bakeryService;
    private final Buff buff;

    BuffWidget(
        AssetManager assetManager,
        Assets assets,
        BakeryService bakeryService,
        Buff buff
    ) {
        var stack = new Stack();
        stack.add(
            switch (buff) {
                case FRENZY, CLICK_FRENZY -> new BuffIcon(
                    assetManager,
                    assets,
                    buff
                );
                case BUILDING_SPECIAL -> new BuildingSpecialBuffIcon(
                    assetManager,
                    assets,
                    bakeryService
                );
            }
        );
        stack.add(
            new BuffDurationWidget(
                assetManager,
                assets,
                bakeryService,
                buff
            )
        );
        super(assetManager, assets, stack);
        this.bakeryService = bakeryService;
        this.buff = buff;
        var tooltipWidget = new SameTopHeightTooltipWidget(
            this,
            new PopUpFrame(
                assetManager,
                assets,
                new BuffTooltipPanel(
                    assetManager,
                    assets,
                    bakeryService,
                    buff
                )
            )
        );
        tooltipWidget.getContainer().minWidth(250);
        addListener(tooltipWidget);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setVisible(
            bakeryService.buffInterval(buff)
                .remainingSeconds() != 0
        );
    }
}
