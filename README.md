# Games

A collection of Java / [libGDX](https://libgdx.com/) desktop clones. Each game is a small
educational
project: pick a genre whose original is well known, rebuild a slice of it, and use that to practise
a different part of game development.

Shared pieces live in sibling modules: `ecs` (custom archetype ECS), `common` (screens, events,
JSON configuration), and `tools` (asset helpers). The playable projects are below.

## Projects

- **[Snakes](snakes/README.md)** — remake of *Snakes On a Cartesian Plane*; ECS-driven modes and
  data-driven configuration
- **[Bricks](bricks/README.md)** — Arkanoid/Breakout clone inspired by *Brickmania*; Box2D wired
  into the same ECS session loop
- **[Cookies](cookies/README.md)** — *Cookie Clicker* clone; Scene2D for an interactable idle-game
  UI, with game logic kept as simple math

## How to build

Requires Java 25 and Maven.

```shell
./mvnw clean verify
```

How to run each game is in that project's README.
