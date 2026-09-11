package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.TransactionMode;

final class TransactionConfigurationPanel extends Table {

    TransactionConfigurationPanel(
        Skin skin,
        I18NBundle bundle,
        Transaction transaction
    ) {
        background(skin.get(Style.class).background);
        var allCheckbox = new AllCheckbox(skin, bundle);
        var oneCheckbox = new TransactionAmountNumberCheckbox(skin, 1);
        var tenCheckbox = new TransactionAmountNumberCheckbox(skin, 10);
        var hundredCheckbox = new TransactionAmountNumberCheckbox(skin, 100);
        var buyCheckbox = new BuyCheckbox(
            skin,
            bundle,
            allCheckbox,
            hundredCheckbox
        );
        var sellCheckbox = new SellCheckbox(skin, bundle);
        var transactionModeRadioGroup = new RadioGroup<>(
            buyCheckbox,
            sellCheckbox
        );
        var transactionAmountRadioGroup = new RadioGroup<>(
            oneCheckbox,
            tenCheckbox,
            hundredCheckbox,
            allCheckbox
        );
        defaults().growX().uniformX();
        add(buySellTable(buyCheckbox, sellCheckbox));
        add(oneCheckbox);
        add(tenCheckbox);
        add(hundredCheckbox);
        add(allCheckbox);
        addListener(
            new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    var transactionMode = transactionModeRadioGroup.getChecked();
                    var amount = transactionAmountRadioGroup.getChecked();
                    if (transactionMode != null && amount != null) {
                        transaction.update(
                            transactionMode(transactionMode),
                            amount.value()
                        );
                    }
                }

                private TransactionMode transactionMode(
                    TransactionConfigurationCheckbox checkbox
                ) {
                    if (checkbox == buyCheckbox) {
                        return TransactionMode.BUY;
                    }
                    return TransactionMode.SELL;
                }
            }
        );
    }

    private Table buySellTable(
        TransactionConfigurationCheckbox buyCheckbox,
        TransactionConfigurationCheckbox sellCheckbox
    ) {
        var table = new Table();
        table.defaults().pad(5);
        table.add(buyCheckbox).row();
        table.add(sellCheckbox);
        return table;
    }

    private static final class Style {

        Drawable background;
    }
}
