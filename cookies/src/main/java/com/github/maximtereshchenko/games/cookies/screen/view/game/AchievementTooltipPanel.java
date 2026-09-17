package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.github.maximtereshchenko.games.cookies.domain.Achievement;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

import java.util.List;
import java.util.Optional;

public final class AchievementTooltipPanel extends TooltipPanel {

    private final BakeryService bakeryService;
    private final Achievement achievement;

    public AchievementTooltipPanel(
        AssetManager assetManager,
        Assets assets,
        BakeryService bakeryService,
        Achievement achievement
    ) {
        this.bakeryService = bakeryService;
        this.achievement = achievement;
        super(assetManager, assets);
    }

    @Override
    protected Image icon(AssetManager assetManager, Assets assets) {
        return new AchievementIcon(
            assetManager,
            assets,
            bakeryService,
            achievement
        );
    }

    @Override
    protected Label name(
        AssetManager assetManager,
        Assets assets
    ) {
        return new AchievementNameLabel(
            assetManager,
            assets,
            bakeryService,
            achievement
        );
    }

    @Override
    protected Optional<Table> value(AssetManager assetManager, Assets assets) {
        return Optional.empty();
    }

    @Override
    protected List<Badge> badges(
        AssetManager assetManager,
        Assets assets
    ) {
        return List.of(
            new Badge(
                assetManager,
                assets,
                assetManager.get(assets.game().bundle()).get("achievement.tooltip.badge")
            ),
            new AchievementBadge(
                assetManager,
                assets,
                bakeryService,
                achievement
            )
        );
    }

    @Override
    protected Optional<Label> description(
        AssetManager assetManager,
        Assets assets
    ) {
        return Optional.of(
            new AchievementDescriptionLabel(
                assetManager,
                assets,
                bakeryService,
                achievement
            )
        );
    }

    @Override
    protected Optional<FlavorTextLabel> flavorText(
        AssetManager assetManager,
        Assets assets
    ) {
        return Optional.empty();
    }

    @Override
    protected Optional<Actor> footer(
        AssetManager assetManager,
        Assets assets
    ) {
        return Optional.empty();
    }
}
