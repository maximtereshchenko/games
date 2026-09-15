package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Buff;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

final class BuffOverlay extends Table {

    private final List<BuffWidget> buffWidgets;

    BuffOverlay(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService
    ) {
        this.buffWidgets = new ArrayList<>();
        pad(16);
        top();
        right();
        for (var buff : Buff.values()) {
            buffWidgets.add(
                new BuffWidget(
                    skin,
                    bundle,
                    bakeryService,
                    buff
                )
            );
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        buffWidgets.sort(Comparator.comparing(BuffWidget::isVisible));
        clearChildren();
        for (var buffWidget : buffWidgets) {
            add(buffWidget).row();
        }
    }
}
