package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Upgrade;
import com.github.maximtereshchenko.games.cookies.screen.BigDecimalFormatter;
import com.github.maximtereshchenko.games.cookies.screen.view.SmallCookieIcon;

import java.util.List;
import java.util.Optional;

final class UpgradeTooltipPanel extends TooltipPanel {

    private final BakeryService bakeryService;
    private final Upgrade upgrade;

    UpgradeTooltipPanel(
        Skin skin,
        I18NBundle bundle,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Upgrade upgrade
    ) {
        this.bakeryService = bakeryService;
        this.upgrade = upgrade;
        super(skin, bundle, bigDecimalFormatter);
    }

    @Override
    Image icon(Skin skin) {
        return new UpgradeIcon(skin, upgrade);
    }

    @Override
    Label name(Skin skin, I18NBundle bundle) {
        return new UpgradeNameLabel(
            skin,
            bundle,
            upgrade
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
            new UpgradePriceLabel(
                skin,
                bigDecimalFormatter,
                bakeryService,
                upgrade
            )
        );
        return Optional.of(table);
    }

    @Override
    List<Badge> badges(Skin skin, I18NBundle bundle) {
        return List.of(
            new Badge(
                skin,
                bundle.get("store.upgrade.badge")
            )
        );
    }

    @Override
    Optional<Label> description(
        Skin skin,
        I18NBundle bundle
    ) {
        return Optional.of(
            new UpgradeDescriptionLabel(
                skin,
                bundle,
                upgrade
            )
        );
    }

    @Override
    Optional<FlavorTextLabel> flavorText(
        Skin skin,
        I18NBundle bundle
    ) {
        return Optional.of(
            new UpgradeFlavorTextLabel(
                skin,
                bundle,
                upgrade
            )
        );
    }

    @Override
    Optional<Actor> footer(
        Skin skin,
        I18NBundle bundle
    ) {
        return Optional.of(
            new UpgradeTooltipFooterLabel(
                skin,
                bundle
            )
        );
    }
}
