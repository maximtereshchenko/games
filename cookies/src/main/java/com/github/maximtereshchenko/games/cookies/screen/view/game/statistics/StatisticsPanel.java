package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;

import java.time.Clock;

public final class StatisticsPanel extends Container<Table> {

    public StatisticsPanel(
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Clock clock
    ) {
        var bundle = assetManager.get(assets.game().bundle());
        super(new Table());
        fill();
        var style = assetManager.get(assets.game().skin()).get(Style.class);
        background(style.bottomBackground);
        var table = getActor();
        table.background(style.topBackground);
        table.pad(6);
        table.top();
        addTitle(assetManager, assets, table, style);
        table.add().pad(12).row();
        addPanel(
            assetManager,
            assets,
            table,
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
            table,
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
            table,
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
        Table table,
        Style style,
        String subTitle,
        Table panel
    ) {
        addSubTitle(
            assetManager,
            assets,
            table,
            style,
            subTitle
        );
        table.add(panel)
            .colspan(4)
            .pad(12)
            .growX()
            .row();
    }

    private void addSubTitle(AssetManager assetManager, Assets assets, Table table, Style style, String subTitle) {
        addSubTitleSeparator(table, style);
        table.add(
                new Label(
                    subTitle,
                    assetManager.get(assets.game().skin()),
                    "statistics-sub-title"
                )
            )
            .pad(12)
            .colspan(2)
            .left()
            .row();
        addSubTitleSeparator(table, style);
    }

    private void addSubTitleSeparator(Table table, Style style) {
        table.add(new Image(style.subTitleSeparator))
            .growX()
            .colspan(2)
            .row();
    }

    private void addTitle(AssetManager assetManager, Assets assets, Table table, Style style) {
        addTitleSeparator(table, style);
        table.add(
                new Label(
                    assetManager.get(assets.game().bundle()).get("statistics.title"),
                    assetManager.get(assets.game().skin()),
                    "statistics-title"
                )
            )
            .pad(12)
            .colspan(4)
            .row();
        addTitleSeparator(table, style);
    }

    private void addTitleSeparator(Table table, Style style) {
        table.add().growX();
        table.add(new Image(style.titleSeparator))
            .growX()
            .colspan(2);
        table.add().growX().row();
    }

    private static final class Style {

        Drawable bottomBackground;
        Drawable topBackground;
        Drawable titleSeparator;
        Drawable subTitleSeparator;
    }
}
