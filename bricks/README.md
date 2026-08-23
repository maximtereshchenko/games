# Bricks

A Java / [libGDX](https://libgdx.com/) Arkanoid/Breakout clone inspired
by [Brickmania](http://dong.digital/brickmania) — an educational project for integrating a physics
engine ([Box2D](https://box2d.org/) via
[gdx-box2d](https://libgdx.com/wiki/extensions/physics/box2d)) into an entity–component–system (ECS)
architecture.

## Motivation

This project started as a way to learn how a physics engine and ECS can share one session loop:
gameplay lives in components and systems, while Box2D owns collision, bounce, and rigid-body motion.
Along the way I also practiced customising libGDX skins and bitmap fonts, wiring sound effects and
music, shipping more than one translation, and drawing custom textures for bricks, bonuses, and HUD
icons.

## Architecture

- **Custom archetype ECS** (`com.github.maximtereshchenko.games.ecs`) — entities are bags of
  components grouped into archetype tables; systems query by component mask and update the registry
  each frame.
- **Box2D beside the registry** — a `World` (zero gravity, arcade restitution) runs on a fixed
  timestep. Systems create bodies, fixtures, sensors, and joints from ECS components (`Rectangle`,
  `Circle`, `Sensor`, Box2D `BodyType`). A contact listener writes collisions back as components so
  paddle, brick, and bonus logic stay ECS-native.
- **Sync both ways** — ECS velocity drives Box2D bodies; Box2D positions update `WorldPosition` for
  rendering. Ball speed is renormalised after steps so bounces stay arcade-like. The ball starts
  welded to the kinematic paddle until launch.
- **One session core for all difficulties** — movement, physics, collisions, bonuses, lives, stars,
  HUD sync, and rendering are shared systems. Difficulties differ by which blueprint overlays they
  merge in, not by separate game loops.
- **Data-driven configuration** — `configuration.json` lists assets, world size, and progress keys;
  `common-blueprints.json` plus per-difficulty files describe paddle, ball, bricks, walls, barrier,
  and bonuses; `level-1.json` … `level-10.json` are grids of empty cells, coloured bricks, and
  walls.
- **Screens & UI** — loading, title, settings (music and sound volume), difficulty select, level
  select (unlocks and stars), and the in-session HUD (lives and stars). Skin, fonts, and Scene2D
  widgets are customised rather than left at libGDX defaults.

## Features

- Four difficulties that change brick density, ball size and speed, and which bonuses can drop
- Ten levels per difficulty, unlocked in sequence; up to three stars recorded per level
- Lives, falling bonuses (helpful and harmful), and a temporary floor barrier
- Progress saving via libGDX Preferences (unlocks, stars, volumes)
- Custom texture atlas, skin, and bitmap font
- Music and sound effects with adjustable volume
- Localized strings via I18N bundles (English and Russian)

## Difficulties

Shared layouts; difficulty mainly changes how the board is instantiated and how the ball/bonuses
behave:

| Difficulty     | About                                                                                   |
|----------------|-----------------------------------------------------------------------------------------|
| **Easy**       | One brick per cell, larger slower ball; only helpful bonuses.                           |
| **Dense**      | Each cell is a 2×2 cluster of smaller bricks; smaller ball; bonuses drop less often.    |
| **Hard**       | Easy-sized bricks, faster ball; the drop table includes lose-a-life and shorten-paddle. |
| **Super hard** | Dense layout plus Hard speed and mixed bonuses.                                         |

Bonuses include extra life, lose a life, widen/shorten paddle, spawn a timed barrier, multiply
existing balls, spawn extra balls, and collectible stars (up to three per session).

## Controls

- Move the mouse to move the paddle
- Left-click to launch the ball

## How to run

Requires Java 25 and Maven.

### Package project

```shell
mvn clean install
```

### Build classpath

```shell
mvn -pl bricks dependency:build-classpath -Dmdep.outputFile=./classpath.txt
```

### Run application

```shell
java -cp "$(cat ./bricks/classpath.txt):./bricks/target/bricks-1.0-SNAPSHOT.jar" com.github.maximtereshchenko.games.bricks.BricksGameAdapter
```

To run with the Russian translation:

```shell
java -Duser.language=ru -cp "$(cat ./bricks/classpath.txt):./bricks/target/bricks-1.0-SNAPSHOT.jar" com.github.maximtereshchenko.games.bricks.BricksGameAdapter
```

## Credits

- **[Brickmania](http://dong.digital/brickmania)**
