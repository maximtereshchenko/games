package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.screen.view.BigDecimalFormatter;
import com.github.maximtereshchenko.games.cookies.screen.view.SmallCookieIcon;

import java.util.List;
import java.util.Optional;

final class BuildingTooltipPanel extends TooltipPanel {

    private final BakeryService bakeryService;
    private final Building building;

    BuildingTooltipPanel(
        Skin skin,
        I18NBundle bundle,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Building building
    ) {
        this.bakeryService = bakeryService;
        this.building = building;
        super(skin, bundle, bigDecimalFormatter);
    }

    @Override
    Image icon(Skin skin) {
        return new BuildingTooltipIcon(
            skin,
            bakeryService,
            building
        );
    }

    @Override
    Label name(Skin skin, I18NBundle bundle) {
        return new BuildingNameLabel(
            skin,
            "building-name-tooltip",
            bundle,
            bakeryService,
            building
        );
    }

    @Override
    Optional<Table> value(
        Skin skin,
        BigDecimalFormatter bigDecimalFormatter
    ) {
        var table = new Table();
        table.add(new SmallCookieIcon(skin));
        table.add(
            new TransactionValueLabel(
                skin,
                bigDecimalFormatter,
                bakeryService,
                building
            )
        );
        return Optional.of(table);
    }

    @Override
    List<Badge> badges(
        Skin skin,
        I18NBundle bundle
    ) {
        return List.of(
            new BuildingCountBadge(
                skin,
                bundle,
                bakeryService,
                building
            )
        );
    }

    @Override
    Optional<Label> description(
        Skin skin,
        I18NBundle bundle
    ) {
        return Optional.empty();
    }

    @Override
    Optional<FlavorTextLabel> flavorText(
        Skin skin,
        I18NBundle bundle
    ) {
        return Optional.of(
            new BuildingFlavorTextLabel(
                skin,
                bundle,
                bakeryService,
                building
            )
        );
    }

    @Override
    Optional<Actor> footer(
        Skin skin,
        I18NBundle bundle
    ) {
        return Optional.empty();
    }
}
