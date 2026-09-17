package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Buff;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

final class BuffOverlay extends Table {

    private final List<BuffWidget> buffWidgets;

    BuffOverlay(
        AssetManager assetManager,
        Assets assets,
        BakeryService bakeryService
    ) {
        this.buffWidgets = new ArrayList<>();
        pad(16);
        top();
        right();
        for (var buff : Buff.values()) {
            buffWidgets.add(
                new BuffWidget(
                    assetManager,
                    assets,
                    bakeryService,
                    buff
                )
            );
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        buffWidgets.sort(
            Comparator.comparing(BuffWidget::isVisible)
                .reversed()
        );
        clearChildren();
        for (var buffWidget : buffWidgets) {
            add(buffWidget).row();
        }
    }
}
