package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.event.EventBus;

import java.util.ArrayList;
import java.util.List;

final class StorePanel extends Table {

    StorePanel(
        Skin skin,
        I18NBundle bundle,
        EventBus<Event> eventBus
    ) {
        background(skin.get(Style.class).background);
        var label = new Label("Store", skin, "label_store");
        label.setAlignment(Align.center);
        var upgradesBeam = new StoreBeam(skin, "Upgrades");
        var upgradePanel = new UpgradePanel(skin);
        var buildingsBeam = new StoreBeam(skin, "Buildings");
        var amountPanel = new AmountPanel(skin);
        upgradesBeam.addListener(upgradePanel.eventListener());
        upgradePanel.addListener(upgradesBeam.eventListener());
        amountPanel.addListener(buildingsBeam.eventListener());
        var generatorButtons = generatorButtons(
            skin,
            bundle,
            eventBus,
            buildingsBeam.eventListener()
        );
        defaults()
            .width(generatorButtons.getFirst().getPrefWidth())
            .height(Value.prefHeight);
        add(label).growX().row();
        add(upgradesBeam).row();
        add(upgradePanel).row();
        add(buildingsBeam).row();
        add(amountPanel).row();
        for (var generatorButton : generatorButtons) {
            add(generatorButton).row();
        }
        top();
    }

    private List<GeneratorButton> generatorButtons(
        Skin skin,
        I18NBundle bundle,
        EventBus<Event> eventBus,
        EventListener eventListener
    ) {
        var generatorButtons = new ArrayList<GeneratorButton>();
        for (var generator : Generator.values()) {
            var generatorButton = new GeneratorButton(
                skin,
                bundle,
                generator,
                eventBus
            );
            generatorButton.addListener(eventListener);
            generatorButtons.add(generatorButton);
        }
        return generatorButtons;
    }

    private static final class Style {

        Drawable background;
    }
}
