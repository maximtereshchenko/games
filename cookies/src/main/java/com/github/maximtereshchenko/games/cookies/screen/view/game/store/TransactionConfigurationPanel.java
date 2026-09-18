package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.TransactionMode;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class TransactionConfigurationPanel extends Table {

    TransactionConfigurationPanel(
        AssetManager assetManager,
        Assets assets,
        BakeryService bakeryService,
        Transaction transaction
    ) {
        background(
            assetManager.get(assets.game().skin())
                .get(Style.class)
                .background
        );
        var allCheckbox = new AllCheckbox(
            assetManager,
            assets,
            bakeryService
        );
        var oneCheckbox = new TransactionAmountNumberCheckbox(
            assetManager,
            assets,
            bakeryService,
            1
        );
        var tenCheckbox = new TransactionAmountNumberCheckbox(
            assetManager,
            assets,
            bakeryService,
            10
        );
        var hundredCheckbox = new TransactionAmountNumberCheckbox(
            assetManager,
            assets,
            bakeryService,
            100
        );
        var buyCheckbox = new BuyCheckbox(
            assetManager,
            assets,
            bakeryService,
            allCheckbox,
            hundredCheckbox
        );
        var sellCheckbox = new SellCheckbox(
            assetManager,
            assets,
            bakeryService
        );
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
