package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;

import java.time.Clock;

public final class StatisticsPanel extends Table {

    public StatisticsPanel(
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Clock clock
    ) {
        var bundle = assetManager.get(assets.game().bundle());
        var style = assetManager.get(assets.game().skin()).get(Style.class);
        addPanel(
            assetManager,
            assets,
            style,
            bundle.get("statistics.sub-title.general"),
            new GeneralStatisticsPanel(
                assetManager,
                assets,
                bigDecimalFormatter,
                bakeryService,
                clock
            )
        );
        addPanel(
            assetManager,
            assets,
            style,
            bundle.get("statistics.sub-title.upgrades"),
            new ActiveUpgradePanel(
                assetManager,
                assets,
                bigDecimalFormatter,
                bakeryService
            )
        );
        addPanel(
            assetManager,
            assets,
            style,
            bundle.get("statistics.sub-title.achievements"),
            new AchievementPanel(
                assetManager,
                assets,
                bakeryService
            )
        );
    }

    private void addPanel(
        AssetManager assetManager,
        Assets assets,
        Style style,
        String subTitle,
        Table panel
    ) {
        addSubTitle(
            assetManager,
            assets,
            style,
            subTitle
        );
        add(panel)
            .colspan(2)
            .pad(12)
            .growX()
            .row();
    }

    private void addSubTitle(
        AssetManager assetManager,
        Assets assets,
        Style style,
        String subTitle
    ) {
        addSubTitleSeparator(style);
        add(
            new Label(
                subTitle,
                assetManager.get(assets.game().skin()),
                "statistics-sub-title"
            )
        )
            .colspan(2)
            .pad(12)
            .left()
            .row();
        addSubTitleSeparator(style);
    }

    private void addSubTitleSeparator(Style style) {
        add(new Image(style.subTitleSeparator))
            .growX();
        add().growX().row();
    }

    private static final class Style {

        Drawable subTitleSeparator;
    }
}
