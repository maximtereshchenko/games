package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.*;

import java.util.List;
import java.util.Optional;

final class BuildingTooltipPanel extends TooltipPanel {

    private final Transaction transaction;
    private final BigDecimalFormatter bigDecimalFormatter;
    private final BakeryService bakeryService;
    private final Building building;

    BuildingTooltipPanel(
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        Transaction transaction,
        BakeryService bakeryService,
        Building building
    ) {
        this.transaction = transaction;
        this.bigDecimalFormatter = bigDecimalFormatter;
        this.bakeryService = bakeryService;
        this.building = building;
        super(assetManager, assets);
    }

    @Override
    protected Image icon(AssetManager assetManager, Assets assets) {
        return new BuildingTooltipIcon(
            assetManager,
            assets,
            bakeryService,
            building
        );
    }

    @Override
    protected Label name(AssetManager assetManager, Assets assets) {
        return new BuildingNameLabel(
            assetManager,
            assets,
            "building-name-tooltip",
            bakeryService,
            building
        );
    }

    @Override
    protected Optional<Table> value(
        AssetManager assetManager,
        Assets assets
    ) {
        var table = new Table();
        table.add(new SmallCookieIcon(assetManager, assets));
        table.add(
            new TransactionValueLabel(
                assetManager,
                assets,
                bigDecimalFormatter,
                transaction,
                building
            )
        );
        return Optional.of(table);
    }

    @Override
    protected List<Badge> badges(
        AssetManager assetManager,
        Assets assets
    ) {
        return List.of(
            new BuildingCountBadge(
                assetManager,
                assets,
                bakeryService,
                building
            )
        );
    }

    @Override
    protected Optional<Label> description(
        AssetManager assetManager,
        Assets assets
    ) {
        return Optional.empty();
    }

    @Override
    protected Optional<FlavorTextLabel> flavorText(
        AssetManager assetManager,
        Assets assets
    ) {
        return Optional.of(
            new BuildingFlavorTextLabel(
                assetManager,
                assets,
                bakeryService,
                building
            )
        );
    }

    @Override
    protected Optional<Actor> footer(
        AssetManager assetManager,
        Assets assets
    ) {
        return Optional.empty();
    }
}
