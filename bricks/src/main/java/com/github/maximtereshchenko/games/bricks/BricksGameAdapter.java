package com.github.maximtereshchenko.games.bricks;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.github.maximtereshchenko.games.bricks.configuration.CellDefinition;
import com.github.maximtereshchenko.games.bricks.configuration.CellDefinitionDeserializer;
import com.github.maximtereshchenko.games.bricks.configuration.ConfigurationDeserializers;
import com.github.maximtereshchenko.games.bricks.configuration.ConfigurationReader;
import com.github.maximtereshchenko.games.bricks.event.Event;
import com.github.maximtereshchenko.games.bricks.screen.ScreenFactory;
import com.github.maximtereshchenko.games.bricks.session.BricksBlueprints;
import com.github.maximtereshchenko.games.bricks.session.PhysicsObjectFactory;
import com.github.maximtereshchenko.games.bricks.session.SessionFactory;
import com.github.maximtereshchenko.games.event.EventBus;
import tools.jackson.core.type.TypeReference;

import java.util.Set;

final class BricksGameAdapter implements ApplicationListener {

    private BricksGame original;

    static void main() {
        World.setVelocityThreshold(0);
        new Lwjgl3Application(new BricksGameAdapter());
    }

    @Override
    public void create() {
        var shapeRenderer = new ShapeRenderer();
        var spriteBatch = new SpriteBatch();
        var textureAtlas = new TextureAtlas(Gdx.files.classpath("textures.atlas"));
        var eventBus = new EventBus<Event>();
        var configurationDeserializers =
            new ConfigurationDeserializers();
        configurationDeserializers.addDeserializer(
            CellDefinition.class,
            new CellDefinitionDeserializer()
        );
        var configurationReader = new ConfigurationReader(
            configurationDeserializers
        );
        var screenFactory = new ScreenFactory(
            new SessionFactory(
                shapeRenderer,
                spriteBatch,
                textureAtlas,
                eventBus,
                new PhysicsObjectFactory()
            )
        );
        original = new BricksGame(
            Set.of(
                shapeRenderer,
                spriteBatch,
                textureAtlas
            )
        );
        eventBus.subscribe(original);
        original.setScreen(
            screenFactory.sessionScreen(
                new BricksBlueprints()
                    .blueprints(
                        configurationReader.value(
                            "common-blueprints.json",
                            new TypeReference<>() {}
                        )
                    )
                    .merged(
                        configurationReader.value(
                            "easy-difficulty-blueprints.json",
                            new TypeReference<>() {}
                        )
                    ),
                configurationReader.value(
                    "level-1.json",
                    new TypeReference<>() {}
                )
            )
        );
    }

    @Override
    public void resize(int width, int height) {
        original.resize(width, height);
    }

    @Override
    public void render() {
        original.render();
    }

    @Override
    public void pause() {
        original.pause();
    }

    @Override
    public void resume() {
        original.resume();
    }

    @Override
    public void dispose() {
        original.dispose();
    }
}
