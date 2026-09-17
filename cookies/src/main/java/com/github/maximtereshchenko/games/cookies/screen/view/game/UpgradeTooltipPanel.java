package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Upgrade;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

import java.util.List;
import java.util.Optional;

public abstract class UpgradeTooltipPanel extends TooltipPanel {

    private final BigDecimalFormatter bigDecimalFormatter;
    private final BakeryService bakeryService;
    private final Upgrade upgrade;

    protected UpgradeTooltipPanel(
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Upgrade upgrade
    ) {
        this.bigDecimalFormatter = bigDecimalFormatter;
        this.bakeryService = bakeryService;
        this.upgrade = upgrade;
        super(assetManager, assets);
    }

    @Override
    protected final Image icon(AssetManager assetManager, Assets assets) {
        return new UpgradeIcon(assetManager, assets, upgrade);
    }

    @Override
    protected final Label name(AssetManager assetManager, Assets assets) {
        return new UpgradeNameLabel(
            assetManager,
            assets,
            upgrade
        );
    }

    @Override
    protected final Optional<Table> value(
        AssetManager assetManager,
        Assets assets
    ) {
        var table = new Table();
        table.add(new SmallCookieIcon(assetManager, assets));
        table.add(
            price(
                assetManager,
                assets,
                bigDecimalFormatter,
                bakeryService,
                upgrade
            )
        );
        return Optional.of(table);
    }

    @Override
    protected List<Badge> badges(AssetManager assetManager, Assets assets) {
        return List.of(
            new Badge(
                assetManager,
                assets,
                assetManager.get(assets.game().bundle()).get("upgrade.tooltip.badge")
            )
        );
    }

    @Override
    protected final Optional<Label> description(
        AssetManager assetManager,
        Assets assets
    ) {
        return Optional.of(
            new UpgradeDescriptionLabel(
                assetManager,
                assets,
                upgrade
            )
        );
    }

    @Override
    protected final Optional<FlavorTextLabel> flavorText(
        AssetManager assetManager,
        Assets assets
    ) {
        return Optional.of(
            new UpgradeFlavorTextLabel(
                assetManager,
                assets,
                upgrade
            )
        );
    }

    protected abstract Label price(
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Upgrade upgrade
    );
}
