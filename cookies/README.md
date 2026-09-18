# Cookies

A Java / [libGDX](https://libgdx.com/) remake
of [Cookie Clicker](https://orteil.dashnet.org/cookieclicker/)
by [Orteil](https://orteil.dashnet.org/) — an exploration of libGDX Scene2D for building
interactable, dynamic, and pleasant UIs.

## Motivation

This project started as a way to learn how far Scene2D can go for real game interfaces: custom
widgets, skins, layout, animations, tooltips, overlays, and a lot of state that has to stay in sync
on screen. Cookie Clicker was a good original to clone because the game logic is relatively simple
math — no entity–component–system required — while the UI is moderately complex: a clickable cookie,
a store that unlocks over time, upgrades, achievements, golden cookies, and panels that appear,
scroll, and react to hover and click.

Along the way I also practiced customising libGDX skins and bitmap fonts, wiring sound effects,
externalising buildings, upgrades, and achievements into JSON, and keeping domain math separate from
the actor tree.

## Architecture

- **Domain beside the stage** — `BakeryService` owns baking, prices, upgrades, achievements, golden
  cookies, and buffs. Scene2D widgets read that state each frame and send clicks, purchases, and
  settings back into the service. There is no ECS; the session is idle-game math plus a Stage.
- **Data-driven configuration** — `configuration.json` defines building base prices and rates,
  upgrade prices and unlock rules, achievement requirements, milk, golden-cookie spawn timing, and
  buff multipliers. Names, flavour text, and UI copy live in I18N bundles.
- **One bakery screen after loading** — a loading screen then a three-column Scene2D layout: the
  bakery (cookie, milk, buffs, golden cookies), a navigable middle column (building display,
  statistics, options), and a store (upgrades and buildings). Custom widgets, actions, and a skin
  drive hover, press, scroll, pop-up frames, and notifications rather than leaving defaults in
  place.

## Features

- Big cookie you can click or hold; baking power, hover/press scale, particles, and falling cookies
- Sixteen buildings that bake idle cookies, with a visual strip of owned buildings in the middle
  column
- Store with buy/sell amounts of 1, 10, 100, or all; buildings stay locked (shown as `???`) until
  they are in reach
- Upgrades that unlock from progress (building counts, clicks, milk, golden cookies) and multiply
  baking rate, clicks, kittens, or golden cookies
- Achievements with toast notifications; milk rises with unlocks and kitten upgrades scale
  production from that milk
- Golden cookies that spawn on a timer: Frenzy, Click frenzy, Lucky, and building specials, plus
  duration widgets while buffs are active
- Offline baking on load from the last save timestamp
- Progress saving via libGDX Preferences (balance, buildings, upgrades, achievements, volume),
  flushed on exit and from a manual Save button
- Statistics panel (cookies baked, clicks, golden cookies, upgrades, achievements, milk)
- Options panel with volume
- Tooltips for buildings, upgrades, and achievements
- Custom texture atlas, skin, and bitmap fonts
- Sound effects with adjustable volume
- Localized strings via I18N bundles

## Buildings

Prices grow with owned count (1.15× per extra building). Selling returns 25% of the buy price for
those units.

| Building                 | Base price | Base cookies / s |
|--------------------------|------------|------------------|
| **Cursor**               | 15         | 0.1              |
| **Grandma**              | 100        | 1                |
| **Farm**                 | 1,100      | 8                |
| **Mine**                 | 1.2×10⁴    | 47               |
| **Factory**              | 1.3×10⁵    | 260              |
| **Bank**                 | 1.4×10⁶    | 1,400            |
| **Temple**               | 2×10⁷      | 7,800            |
| **Wizard tower**         | 3.3×10⁸    | 44,000           |
| **Shipment**             | 5.1×10⁹    | 260,000          |
| **Alchemy lab**          | 7.5×10¹⁰   | 1.6×10⁶          |
| **Portal**               | 1×10¹²     | 1×10⁷            |
| **Time machine**         | 1.4×10¹³   | 6.5×10⁷          |
| **Antimatter condenser** | 1.7×10¹⁴   | 4.3×10⁸          |
| **Prism**                | 2.1×10¹⁵   | 2.9×10⁹          |
| **Chancemaker**          | 2.6×10¹⁶   | 2.1×10¹⁰         |
| **Fractal engine**       | 3.1×10¹⁷   | 1.5×10¹¹         |

## How to run

Requires Java 25 and Maven.

### Package project

```shell
./mvnw clean install
```

### Build classpath

```shell
./mvnw -pl cookies dependency:build-classpath -Dmdep.outputFile=./classpath.txt
```

### Run application

```shell
java -cp "$(cat ./cookies/classpath.txt):./cookies/target/cookies-1.0-SNAPSHOT.jar" com.github.maximtereshchenko.games.cookies.CookiesGameAdapter
```

## Credits

- **[Orteil](https://orteil.dashnet.org/) (Julien Thiennot)** — original
  *[Cookie Clicker](https://orteil.dashnet.org/cookieclicker/)*
