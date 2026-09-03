package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

final class TransactionModePanel extends Table {

    TransactionModePanel(Skin skin) {
        background(skin.get(Style.class).background);
        var buy = checkBox(skin, "Buy");
        var sell = checkBox(skin, "Sell");
        var one = checkBox(skin, "1");
        var ten = checkBox(skin, "10");
        var hundred = checkBox(skin, "100");
        var all = checkBox(skin, "ALL");
        all.setVisible(false);
        buy.addListener(
            new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    all.setVisible(!buy.isChecked());
                    if (all.isChecked()) {
                        all.setChecked(false);
                        hundred.setChecked(true);
                    }
                }
            }
        );
        new RadioGroup(buy, sell);
        new RadioGroup(one, ten, hundred, all);
        defaults().growX().uniformX();
        add(buySellTable(buy, sell));
        add(one);
        add(ten);
        add(hundred);
        add(all);
    }

    @Override
    public float getPrefHeight() {
        return getMinHeight();
    }

    private Table buySellTable(CheckBox buy, CheckBox sell) {
        var table = new Table();
        table.defaults().pad(5);
        table.add(buy).row();
        table.add(sell);
        return table;
    }

    private CheckBox checkBox(Skin skin, String text) {
        return new CheckBox(text, skin, "button_transactionMode");
    }

    private static final class Style {

        Drawable background;
    }
}
