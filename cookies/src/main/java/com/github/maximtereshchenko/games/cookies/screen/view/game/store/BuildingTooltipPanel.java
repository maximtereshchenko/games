package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.screen.view.game.*;

import java.util.List;
import java.util.Optional;

final class BuildingTooltipPanel extends TooltipPanel {

    private final Transaction transaction;
    private final BigDecimalFormatter bigDecimalFormatter;
    private final BakeryService bakeryService;
    private final Building building;

    BuildingTooltipPanel(
        Skin skin,
        I18NBundle bundle,
        BigDecimalFormatter bigDecimalFormatter,
        Transaction transaction,
        BakeryService bakeryService,
        Building building
    ) {
        this.transaction = transaction;
        this.bigDecimalFormatter = bigDecimalFormatter;
        this.bakeryService = bakeryService;
        this.building = building;
        super(skin, bundle);
    }

    @Override
    protected Image icon(Skin skin) {
        return new BuildingTooltipIcon(
            skin,
            bakeryService,
            building
        );
    }

    @Override
    protected Label name(Skin skin, I18NBundle bundle) {
        return new BuildingNameLabel(
            skin,
            "building-name-tooltip",
            bundle,
            bakeryService,
            building
        );
    }

    @Override
    protected Optional<Table> value(
        Skin skin
    ) {
        var table = new Table();
        table.add(new SmallCookieIcon(skin));
        table.add(
            new TransactionValueLabel(
                skin,
                bigDecimalFormatter,
                transaction,
                building
            )
        );
        return Optional.of(table);
    }

    @Override
    protected List<Badge> badges(
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
    protected Optional<Label> description(
        Skin skin,
        I18NBundle bundle
    ) {
        return Optional.empty();
    }

    @Override
    protected Optional<FlavorTextLabel> flavorText(
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
    protected Optional<Actor> footer(
        Skin skin,
        I18NBundle bundle
    ) {
        return Optional.empty();
    }
}
