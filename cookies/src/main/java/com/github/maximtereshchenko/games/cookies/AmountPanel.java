package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

final class AmountPanel extends Table {

    AmountPanel(Skin skin) {
        background(skin.getTiledDrawable("tile_noise"));
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
        buttonGroup(buy, sell);
        buttonGroup(one, ten, hundred, all);
        defaults().growX().uniformX();
        add(buySellTable(buy, sell));
        add(one);
        add(ten);
        add(hundred);
        add(all);
    }

    private void buttonGroup(CheckBox... checkBoxes) {
        var amountGroup = new ButtonGroup<>(checkBoxes);
        amountGroup.setMaxCheckCount(1);
        amountGroup.setMinCheckCount(1);
        amountGroup.setChecked(checkBoxes[0].getText().toString());
    }

    private Table buySellTable(CheckBox buy, CheckBox sell) {
        var buySell = new Table();
        buySell.defaults().pad(5);
        buySell.add(buy).row();
        buySell.add(sell);
        return buySell;
    }

    private CheckBox checkBox(Skin skin, String text) {
        return new CheckBox(text, skin, "button_amount");
    }
}
