package com.github.maximtereshchenko.snakes.screen.view.main;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.snakes.screen.view.BasicButton;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class NavigationView extends Table {

    private final BasicButton statisticsButton;
    private final BasicButton settingsButton;
    private final BasicButton creditsButton;

    public NavigationView(I18NBundle bundle, Skin skin) {
        statisticsButton = new BasicButton(
            bundle.get("screens.main.buttons.statistics.name"),
            skin
        );
        settingsButton = new BasicButton(
            bundle.get("screens.main.buttons.settings.name"),
            skin
        );
        creditsButton = new BasicButton(
            bundle.get("screens.main.buttons.credits.name"),
            skin
        );
        defaults().growX().uniformX().pad(3);
        add(statisticsButton);
        add(settingsButton);
        add(creditsButton);
    }

    public void onHover(BiConsumer<TextButton, Target> consumer) {
        setListeners(BasicButton::onHover, consumer);
    }

    public void onClick(Consumer<Target> consumer) {
        setListeners(BasicButton::onClick, (_, target) -> consumer.accept(target));
    }

    private void setListeners(
        BiConsumer<BasicButton, Runnable> listener,
        BiConsumer<TextButton, Target> consumer
    ) {
        listener.accept(
            statisticsButton,
            () -> consumer.accept(statisticsButton, Target.STATISTICS)
        );
        listener.accept(
            settingsButton,
            () -> consumer.accept(settingsButton, Target.SETTINGS)
        );
        listener.accept(
            creditsButton,
            () -> consumer.accept(creditsButton, Target.CREDITS)
        );
    }

    public enum Target {
        STATISTICS, SETTINGS, CREDITS
    }
}
