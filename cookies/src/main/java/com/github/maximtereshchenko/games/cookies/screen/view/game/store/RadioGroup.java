package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;

final class RadioGroup<T extends TransactionConfigurationCheckbox> extends ButtonGroup<T> {

    @SafeVarargs
    RadioGroup(T... checkBoxes) {
        super(checkBoxes);
        setMaxCheckCount(1);
        setMinCheckCount(1);
        setChecked(checkBoxes[0].getText().toString());
    }
}
