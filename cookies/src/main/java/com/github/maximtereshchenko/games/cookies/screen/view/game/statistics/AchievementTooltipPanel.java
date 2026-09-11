package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.Achievement;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.view.game.Badge;
import com.github.maximtereshchenko.games.cookies.screen.view.game.FlavorTextLabel;
import com.github.maximtereshchenko.games.cookies.screen.view.game.TooltipPanel;

import java.util.List;
import java.util.Optional;

final class AchievementTooltipPanel extends TooltipPanel {

    private final BakeryService bakeryService;
    private final Achievement achievement;

    AchievementTooltipPanel(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService,
        Achievement achievement
    ) {
        this.bakeryService = bakeryService;
        this.achievement = achievement;
        super(skin, bundle);
    }

    @Override
    protected Image icon(Skin skin) {
        return new AchievementIcon(
            skin,
            bakeryService,
            achievement
        );
    }

    @Override
    protected Label name(
        Skin skin,
        I18NBundle bundle
    ) {
        return new AchievementNameLabel(
            skin,
            bundle,
            bakeryService,
            achievement
        );
    }

    @Override
    protected Optional<Table> value(Skin skin) {
        return Optional.empty();
    }

    @Override
    protected List<Badge> badges(
        Skin skin,
        I18NBundle bundle
    ) {
        return List.of(
            new Badge(
                skin,
                bundle.get(
                    "statistics.achievement.tooltip.badge"
                )
            ),
            new AchievementBadge(
                skin,
                bundle,
                bakeryService,
                achievement
            )
        );
    }

    @Override
    protected Optional<Label> description(
        Skin skin,
        I18NBundle bundle
    ) {
        return Optional.of(
            new AchievementDescriptionLabel(
                skin,
                bundle,
                bakeryService,
                achievement
            )
        );
    }

    @Override
    protected Optional<FlavorTextLabel> flavorText(
        Skin skin,
        I18NBundle bundle
    ) {
        return Optional.empty();
    }

    @Override
    protected Optional<Actor> footer(
        Skin skin,
        I18NBundle bundle
    ) {
        return Optional.empty();
    }

}
