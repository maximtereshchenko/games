package com.github.maximtereshchenko.games.cookies.screen.view.generator;

import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;

final class RadioGroup extends ButtonGroup<CheckBox> {

    RadioGroup(CheckBox... checkBoxes) {
        super(checkBoxes);
        setMaxCheckCount(1);
        setMinCheckCount(1);
        setChecked(checkBoxes[0].getText().toString());
    }
}
