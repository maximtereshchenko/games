package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.bricks.UserProfile;
import com.github.maximtereshchenko.games.bricks.configuration.Blueprints;
import com.github.maximtereshchenko.games.bricks.configuration.CellDefinition;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;
import com.github.maximtereshchenko.games.bricks.event.Event;
import com.github.maximtereshchenko.games.bricks.screen.view.Indicator;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.event.EventBus;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class SessionFactory {

    private final Configuration configuration;
    private final EventBus<Event> eventBus;
    private final PhysicsObjectFactory physicsObjectFactory;
    private final AssetManager assetManager;
    private final UserProfile userProfile;

    public SessionFactory(
        Configuration configuration,
        EventBus<Event> eventBus,
        PhysicsObjectFactory physicsObjectFactory,
        AssetManager assetManager,
        UserProfile userProfile
    ) {
        this.configuration = configuration;
        this.eventBus = eventBus;
        this.physicsObjectFactory = physicsObjectFactory;
        this.assetManager = assetManager;
        this.userProfile = userProfile;
    }

    public Registry registry(
        Viewport viewport,
        Indicator livesIndicator,
        Indicator starsIndicator,
        Blueprints blueprints,
        List<List<CellDefinition>> cellDefinitions,
        World world,
        String difficulty,
        int level
    ) {
        var registry = new Registry();
        var random = ThreadLocalRandom.current();
        var assets = configuration.assets();
        registry.addSystems(
            new PaddleSpawningSystem(registry, blueprints),
            new LayoutSystem(
                registry,
                configuration,
                cellDefinitions,
                blueprints
            ),
            new PaddleMovementSystem(
                registry,
                configuration,
                viewport
            ),
            new BallDetachingSystem(registry),
            new BallLaunchingSystem(registry, world),
            new BonusResettingSystem(registry),
            new BallResettingSystem(registry, blueprints),
            new PhysicsSynchronizationSystem(registry),
            new PhysicsSystem(registry, world),
            new RegistrySynchronizationSystem(registry),
            new SpeedNormalizationSystem(registry),
            new PaddleCollisionSystem(registry),
            new BrickCollisionSystem(registry),
            new BonusCollisionSystem(registry),
            new PlayBallSoundCollisionSystem(
                registry,
                configuration,
                assetManager,
                userProfile
            ),
            new PlaySoundSystem(
                registry.entities(
                    new Query().all(Bonus.class, Activated.class)
                ),
                assets.bonusSound(),
                assetManager,
                userProfile
            ),
            new BonusSpawningSystem(
                registry,
                blueprints,
                random
            ),
            new StarSpawningSystem(
                registry,
                configuration,
                blueprints,
                random
            ),
            new PaddleWideningSystem(registry),
            new PaddleShorteningSystem(registry),
            new WidthResettingSystem(registry),
            new BarrierSpawningSystem(registry, blueprints),
            new BarrierTimeExtendingSystem(registry),
            new BarrierRemovalSystem(registry),
            new MultiplyBallsBonusSystem(registry),
            new SpawnBallsBonusSystem(registry),
            new BallSpawningSystem(registry, blueprints),
            new LifeIncrementingSystem(registry),
            new DecrementLivesBonusSystem(registry),
            new StarIncrementingSystem(registry),
            new FixtureWidthUpdatingSystem(
                registry,
                world,
                physicsObjectFactory
            ),
            new OutOfBoundsEntityRemovalSystem(
                registry,
                configuration
            ),
            new FixtureRemovalSystem(registry, world),
            new BoundariesFixtureSystem(
                registry,
                physicsObjectFactory,
                world
            ),
            new RectangleFixtureSystem(
                registry,
                world,
                physicsObjectFactory
            ),
            new CircleFixtureSystem(
                registry,
                world,
                physicsObjectFactory
            ),
            new SensorFixtureSystem(
                registry,
                world,
                physicsObjectFactory
            ),
            new PaddleJointSystem(
                registry,
                world,
                physicsObjectFactory
            ),
            new BallLossLifeDecrementingSystem(registry),
            new LifeDecrementingSystem(registry),
            new PlaySoundSystem(
                registry.entities(
                    new Query().all(DecrementLivesCommand.class)
                ),
                assets.loseSound(),
                assetManager,
                userProfile
            ),
            new LevelFailedPublishingSystem(
                registry,
                eventBus
            ),
            new LevelCompletedPublishingSystem(
                registry,
                eventBus,
                difficulty,
                level
            ),
            new IndicatorSynchronizationSystem<>(
                registry,
                livesIndicator,
                Lives.class,
                lives -> lives.value
            ),
            new IndicatorSynchronizationSystem<>(
                registry,
                starsIndicator,
                CollectedStars.class,
                collectedStars -> collectedStars.value
            ),
            new ComponentRemovalSystem(
                registry,
                LayoutPolicy.class,
                BodyDef.BodyType.class,
                Sensor.class,
                CollisionGroupIndex.class,
                Collisions.class,
                UpdateWidthCommand.class,
                DecrementLivesCommand.class
            )
        );
        return registry;
    }
}
