package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Upgrade;

final class UpgradeButton extends Container<ImageButton> {

    private final Style style;
    private final BakeryService bakeryService;
    private final Upgrade upgrade;

    UpgradeButton(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService,
        Upgrade upgrade
    ) {
        var buttonStyle = skin.get(upgrade.name(), Style.class);
        super(new ImageButton(buttonStyle));
        this.style = buttonStyle;
        this.bakeryService = bakeryService;
        this.upgrade = upgrade;
        background(style.background);
        addListener(
            new TooltipWidget(
                skin,
                new UpgradeTooltipPanel(
                    skin,
                    bundle,
                    bakeryService,
                    upgrade
                )
            )
        );
        addListener(
            new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    bakeryService.buyUpgrade(upgrade);
                    remove();
                }
            }
        );
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        var isDisabled = !bakeryService.canAfford(upgrade);
        var imageButton = getActor();
        imageButton.setDisabled(isDisabled);
        imageButton.getImage().setColor(color(isDisabled));
    }

    private Color color(boolean isDisabled) {
        if (isDisabled) {
            return style.disabledColor;
        }
        return style.enabledColor;
    }

    private static final class Style extends ImageButton.ImageButtonStyle {

        Drawable background;
        Color enabledColor;
        Color disabledColor;
    }
}
