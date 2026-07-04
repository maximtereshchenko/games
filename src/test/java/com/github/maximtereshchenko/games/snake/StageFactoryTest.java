package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;
import static org.mockito.Mockito.*;

final class StageFactoryTest {

    private final AssetManager assetManager = mock();
    private final I18NBundle bundle = mock();
    private final Skin skin = mock();
    private final SpriteBatch spriteBatch = mock();
    private final ApplicationEvents applicationEvents = mock();
    private final StageFactory stageFactory = new StageFactory(
        assetManager,
        spriteBatch,
        applicationEvents
    );

    @BeforeEach
    void setUp() {
        Gdx.graphics = mock();
        Gdx.files = mock();
    }

    @Test
    void whenTitleStage_thenTitleStageCreated() {
        try (
            var screenViewport = mockConstruction(ScreenViewport.class);
            var _ = mockConstruction(
                Label.class,
                (_, context) -> assertThat(context.arguments())
                    .anySatisfy(argument -> assertThat(argument).isEqualTo(skin))
            )
        ) {
            when(assetManager.get(Assets.I18N_BUNDLE)).thenReturn(bundle);
            when(assetManager.get(Assets.SKIN)).thenReturn(skin);
            var stage = stageFactory.titleStage();
            assertThat(stage.getViewport()).isEqualTo(screenViewport.constructed().getFirst());
            assertThat(stage.getBatch()).isEqualTo(spriteBatch);
            assertThat(stage.getActors())
                .singleElement()
                .asInstanceOf(type(Table.class))
                .extracting(
                    Table::getWidth,
                    Table::getHeight,
                    table -> table.getChildren().size
                )
                .containsExactly(
                    stage.getWidth(),
                    stage.getHeight(),
                    2
                );
            var inputEvent = new InputEvent();
            inputEvent.setType(InputEvent.Type.keyDown);
            inputEvent.setKeyCode(Input.Keys.SPACE);
            stage.getRoot().fire(inputEvent);
            verify(applicationEvents).publish(ApplicationEvent.CONTINUED_PAST_TITLE_SCREEN);
            verify(bundle).get("title.name");
            verify(bundle).get("title.continue");
        }
    }
}