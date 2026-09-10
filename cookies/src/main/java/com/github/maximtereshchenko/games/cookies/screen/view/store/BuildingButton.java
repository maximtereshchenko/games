package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.screen.view.BigDecimalFormatter;

final class BuildingButton extends Button {

    private final Style style;
    private final BakeryService bakeryService;
    private final Building building;

    BuildingButton(
        Skin skin,
        I18NBundle bundle,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Building building,
        int index
    ) {
        var buttonStyle = skin.get(
            String.valueOf(index % 4),
            Style.class
        );
        super(buttonStyle);
        this.style = buttonStyle;
        this.bakeryService = bakeryService;
        this.building = building;
        add(
            new BuildingIcon(
                skin,
                "%s-button".formatted(building.name()),
                bakeryService,
                building,
                0.5f
            )
        );
        add(
            new TransactionDetailsWidget(
                skin,
                bundle,
                bigDecimalFormatter,
                bakeryService,
                building
            )
        ).growX();
        add(
            new BuildingCountLabel(
                skin,
                bakeryService,
                building
            )
        )
            .padRight(4);
        addListener(
            new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    bakeryService.completeTransaction(building);
                }
            }
        );
        addListener(
            new TooltipWidget(
                skin,
                new BuildingTooltipPanel(
                    skin,
                    bundle,
                    bigDecimalFormatter,
                    bakeryService,
                    building
                )
            )
        );
        addAction(
            Actions.sequence(
                Actions.fadeOut(0),
                Actions.fadeIn(0.5f)
            )
        );
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        var isDisabled = !bakeryService.canAfford(building);
        if (isDisabled() == isDisabled) {
            return;
        }
        setDisabled(isDisabled);
        addAction(Actions.color(color(isDisabled), 0.5f));
    }

    private Color color(boolean isDisabled) {
        if (isDisabled) {
            return style.disabledColor;
        }
        return style.enabledColor;
    }

    private static final class Style extends ButtonStyle {

        Color enabledColor;
        Color disabledColor;
    }
}
