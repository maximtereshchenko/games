# Snakes

A Java / [libGDX](https://libgdx.com/) remake
of [Snakes On a Cartesian Plane](https://www.kongregate.com/en/games/netgrind/snakes-on-a-cartesian-plane)
by [Netgrind](https://netgrindgames.com/games/snake.html) — an exploration of game development and
entity–component–system (ECS) architecture.

## Motivation

This project started as a way to practice designing games around ECS. After hitting bugs in the
originally chosen third-party ECS ([Dominion](https://dominion-dev.github.io/)), I built a small
archetype-based ECS of my own and learned how to shape gameplay so it fits that model. I also
practiced data-driven design by externalising configuration and game modes into JSON files instead
of hard-coding each variant in Java.

The original goal was to implement every mode from the Flash game. I even decompiled the original to
study its logic — which did **not** use ECS. Fitting that many rule variations into one shared core
became the hard part; that complexity was a natural stopping point. What remains is a solid subset
of modes on a single ECS-driven session loop, plus practice with libGDX for desktop game development
in Java.

## Architecture

- **Custom archetype ECS** (`com.github.maximtereshchenko.ecs`) — entities are bags of components
  grouped into archetype tables; systems query by component mask and update the world each frame /
  turn.
- **One session core for all modes** — movement, collisions, food, warping, growth, air supply, UI
  sync, and rendering are shared systems. Modes differ mainly by which components and policies are
  spawned from JSON (`classic.json`, `viper.json`, …), not by separate game loops.
- **Data-driven configuration** — `configuration.json` defines unlock rules, palettes, and world
  size; per-mode entity files (`classic.json`, `viper.json`, …) describe the starting world so modes
  can be authored as data.
- **Screens & UI** — loading, title, main (mode select), statistics, credits, settings (music
  volume), and the in-session HUD (food count, air, etc.).

## Features

- Multiple unlockable snake modes with distinct rule twists
- Progress saving via libGDX Preferences (metrics, unlocked modes, settings)
- Statistics screen for cumulative progress
- Shared turn-based snake logic that supports every implemented mode
- World and interface rendering via ShapeRenderer / SpriteBatch
- Music with adjustable volume
- Localized strings via I18N bundles

## Game modes

Implemented modes and unlock requirements:

| Mode           | About                                                                                                       | Unlock                                          |
|----------------|-------------------------------------------------------------------------------------------------------------|-------------------------------------------------|
| **Classic**    | The basic snake game: eat food, grow, avoid your tail.                                                      | Launch the game (1 launch)                      |
| **Viper**      | Only turns clockwise — the Derek Zoolander of snakes.                                                       | Make 20 left turns in one session               |
| **Sidewinder** | Periodically drifts one cell sideways.                                                                      | Read the credits                                |
| **Sea**        | Limited air supply while diving for food that slowly drops off-screen; warping edges apply.                 | Unlock a quarter of modes (3)                   |
| **Hognose**    | Larger mouth / hitbox — you need more than a grazing touch to swallow food; several pellets can be present. | Finish a session with exactly one food consumed |
| **Mamushi**    | Eating food leaves a deadly wall where it was.                                                              | Consume an odd amount of food in one session    |
| **Trouser**    | The whole board starts as food — grow as big as you can.                                                    | Consume 100 food (total)                        |
| **Boa**        | Every fifth pellet shrinks the playable area (warping walls close in); growth is aggressive.                | Warp 200 times (total)                          |
| **Mamba**      | After warping, relative turn directions feel reversed — hard to tell if you’re coming or going.             | Unlock half of modes (6)                        |
| **Burmese**    | Food fades in over time — goals take a while to come into focus.                                            | Finish 10 sessions                              |
| **Solid**      | Two pellets at once; eating one warps you to the other.                                                     | Consume 200 food (total)                        |
| **Asp**        | Food appears as 3×3 clusters: the center is deadly, the surrounding cells are edible.                       | Finish 25 sessions                              |

## How to run

Requires Java 25 and Maven.

### Package project

```shell
mvn clean verify
```

### Build classpath

```shell
mvn dependency:build-classpath -Dmdep.outputFile=classpath.txt
```

### Run application

```shell
java -cp "$(cat classpath.txt):./target/snakes-1.0-SNAPSHOT.jar" com.github.maximtereshchenko.snakes.SnakesGameAdapter
```

## Credits

- **Cale Bradbury (@netgrind)** — original *Snakes On a Cartesian Plane*
