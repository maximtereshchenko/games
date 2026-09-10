package com.github.maximtereshchenko.games.cookies.screen.view;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Upgrade;

import java.util.List;
import java.util.Optional;

public abstract class UpgradeTooltipPanel extends TooltipPanel {

    private final BakeryService bakeryService;
    private final Upgrade upgrade;

    protected UpgradeTooltipPanel(
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
    protected final Image icon(Skin skin) {
        return new UpgradeIcon(skin, upgrade);
    }

    @Override
    protected final Label name(Skin skin, I18NBundle bundle) {
        return new UpgradeNameLabel(
            skin,
            bundle,
            upgrade
        );
    }

    @Override
    protected final Optional<Table> value(
        Skin skin,
        BigDecimalFormatter bigDecimalFormatter
    ) {
        var table = new Table();
        table.add(new SmallCookieIcon(skin));
        table.add(
            price(
                skin,
                bigDecimalFormatter,
                bakeryService,
                upgrade
            )
        );
        return Optional.of(table);
    }

    @Override
    protected List<Badge> badges(Skin skin, I18NBundle bundle) {
        return List.of(
            new Badge(
                skin,
                bundle.get("store.upgrade.badge")
            )
        );
    }

    @Override
    protected final Optional<Label> description(
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
    protected final Optional<FlavorTextLabel> flavorText(
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

    protected abstract Label price(
        Skin skin,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Upgrade upgrade
    );
}
