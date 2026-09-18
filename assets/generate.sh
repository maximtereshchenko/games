#!/usr/bin/env bash

set -Eeuo pipefail

assets=$(realpath ./assets)
raw=$assets/raw
tmp=$assets/tmp
game=$tmp/textures/game
textures=$assets/textures
resources=$(realpath ./cookies/src/main/resources)

tier_names=(plain berrylium blueberrylium chalcedhoney buttergold sugarmuck jetmint cherrysilver hazelrald)

executeTool() {
  java -XstartOnFirstThread -cp "$(cat ./tools/target/classpath.txt):./tools/target/classes" com.github.maximtereshchenko.games.Main "$@"
}

slice() {
  executeTool slice "$raw/$1" "$2" "$3" "$4"
}

copy_tmp() {
  cp "$tmp/$1" "$game/$2"
}

copy_icon() {
  copy_tmp "icon$1.png" "$2.png"
}

tier_offset() {
  case $1 in
    plain) echo 0 ;;
    berrylium) echo 27 ;;
    blueberrylium) echo 63 ;;
    chalcedhoney) echo 438 ;;
    buttergold) echo 474 ;;
    sugarmuck) echo 510 ;;
    jetmint) echo 546 ;;
    cherrysilver) echo 580 ;;
    hazelrald) echo 614 ;;
    *)
      echo "unknown material tier: $1" >&2
      return 1
      ;;
  esac
}

copy_tiered_icons() {
  local base=$1 stem=$2
  shift 2
  local tier
  for tier in "$@"; do
    copy_icon "$((base + $(tier_offset "$tier")))" "$stem-$tier"
  done
}

copy_building_icons() {
  while (( $# >= 2 )); do
    copy_tmp "buildingIcon$1.png" "$2.png"
    copy_tmp "buildingIcon$(($1 + 1)).png" "$2-disabled.png"
    shift 2
  done
}

copy_icons() {
  while (( $# >= 2 )); do
    copy_icon "$1" "$2"
    shift 2
  done
}

copy_sequential_icons() {
  local index=$1 prefix=$2 name
  shift 2
  for name in "$@"; do
    copy_icon "$index" "$prefix$name"
    index=$((index + 1))
  done
}

apply_marble_gradients() {
  local names colors tile i
  names=(black white red coral blush)
  colors=(
    '{"r":0,"g":0,"b":0,"a":0}'
    '{"r":1,"g":1,"b":1,"a":0}'
    '{"r":1,"g":0,"b":0,"a":0}'
    '{"r":1,"g":0.5,"b":0.5,"a":0}'
    '{"r":1,"g":0.75,"b":0.75,"a":0}'
  )
  for tile in 0 1 2 3; do
    for i in "${!names[@]}"; do
      executeTool applyBordersGradient "$tmp/marbleTile${tile}.jpg" "${colors[i]}" "$game/marble-gradient-${names[i]}_${tile}.jpg"
    done
  done
}

generate_atlas() {
  local pack=$1
  mkdir -p "$tmp/textures/$pack"
  cp -r "$textures/common/" "$tmp/textures/$pack"
  cp -r "$textures/$pack/" "$tmp/textures/$pack"
  executeTool generateTextureAtlas "$tmp/textures/$pack" "$resources" "$pack"
}

mvn -pl tools clean compile
mvn -pl tools dependency:build-classpath -Dmdep.outputFile=./target/classpath.txt

executeTool generateBitmapFont "$raw/tahoma.ttf" '{"size":22,"hinting":"None"}' "$resources"
executeTool generateBitmapFont "$raw/merriweather.ttf" '{"size":60,"hinting":"None","shadowOffsetY":6,"shadowColor":{"r":0,"g":0,"b":0,"a":0.3}}' "$resources"
executeTool generateBitmapFont "$raw/georgia.ttf" '{"size":24,"hinting":"None"}' "$resources"

rm -rf "$tmp"
slice buildingIcon.png 64 64 "$tmp"
slice marbleTile.jpg 300 64 "$tmp"
slice icon.png 48 48 "$tmp"
slice frame.png 60 60 "$tmp"
slice cursor.png 32 32 "$tmp"
slice panelMenu.png 100 48 "$tmp"
mkdir -p "$game"
slice progress_.png 48 48 "$game"

apply_marble_gradients

executeTool generateEmptyPixel "$game/empty.png"
executeTool generateDoubleEndedLinearGradient "$game/gradient-line-left-right.9.png"
executeTool generateLinearGradient "$game/gradient-line-right.9.png"
executeTool generateEmptyCircle "$game/empty-circle.9.png"

for name in \
  grandma garden-bed mine-entrance factory-building bank-building temple-building tower \
  rocket-building alchemy-device portal-building time-machine antimatter-condenser prism \
  lucky-horseshoe fractal-engine
do
  executeTool stripEmptyBorders "$raw/$name.png" "$game/$name.png"
done

executeTool multiplyColor "$textures/game/golden-cookie.png" 1.25 0 0 "$game/golden-cookie-bright.png"
executeTool multiplyColor "$textures/game/borders.9.png" 1.25 1 1 "$game/borders-bright.9.png"
executeTool multiplyColor "$textures/game/borders.9.png" 0.75 1 1 "$game/borders-dark.9.png"

copy_building_icons \
  0 hand-tilted \
  4 grandma-face-detailed \
  12 farm \
  16 mine \
  20 factory \
  24 bank \
  28 temple \
  32 wizard-tower \
  36 shipment \
  40 alchemy-lab \
  44 portal \
  48 time-machine-icon \
  52 antimatter-condenser-icon \
  56 prism-icon \
  60 chancemaker \
  64 fractal-engine-icon

for i in 0 1 2 3; do
  copy_tmp "marbleTile${i}.jpg" "marble_${i}.jpg"
done

copy_tmp panelMenu2.png wooden-logs-left-bottom.png
copy_tmp panelMenu6.png wooden-logs-left-bottom-bright.png
copy_tmp panelMenu3.png wooden-logs-right-bottom.png
copy_tmp panelMenu7.png wooden-logs-right-bottom-bright.png

copy_icon 0 hand-plain_0
copy_icon 204 hand-plain_1
copy_icon 205 hand-plain_2
copy_tiered_icons 0 hand "${tier_names[@]}"
copy_tiered_icons 1 rolling-pin "${tier_names[@]}"
copy_tiered_icons 2 watering-pot "${tier_names[@]}"
copy_tiered_icons 3 pickaxe "${tier_names[@]}"
copy_tiered_icons 4 gears "${tier_names[@]}"
copy_tiered_icons 5 rocket "${tier_names[@]}"
copy_tiered_icons 6 alembic "${tier_names[@]}"
copy_tiered_icons 7 portal "${tier_names[@]}"
copy_tiered_icons 8 hourglass "${tier_names[@]}"
copy_tiered_icons 9 flask plain berrylium blueberrylium buttergold
copy_tiered_icons 11 mouse-pointer "${tier_names[@]}"
copy_tiered_icons 13 atom "${tier_names[@]}"
copy_tiered_icons 14 prism "${tier_names[@]}"
copy_tiered_icons 15 bag "${tier_names[@]}"
copy_tiered_icons 16 idol "${tier_names[@]}"
copy_tiered_icons 17 wizard-hat "${tier_names[@]}"
copy_tiered_icons 18 kitten plain berrylium blueberrylium chalcedhoney buttergold
copy_tiered_icons 19 die "${tier_names[@]}"
copy_tiered_icons 20 fractal "${tier_names[@]}"

column_tiers=(plain berrylium blueberrylium chalcedhoney)
for i in "${!column_tiers[@]}"; do
  copy_icon "$((206 + i))" "column-${column_tiers[i]}"
done

copy_icons \
  426 set-square \
  210 set-square-plain \
  211 set-square-berrylium \
  212 set-square-blueberrylium \
  213 set-square-buttergold \
  432 set-square-sugarmuck \
  241 monk \
  231 four-leaf-clover \
  226 golden-cookies \
  227 golden-cookie-heap \
  228 golden-cookie-frame \
  229 four-leaf-clover-frame \
  239 question-mark \
  317 grandma-face \
  484 cookie-buttergold \
  180 cookie-cosmic-sparkles

copy_sequential_icons 169 cookie-cyan-outline_ 0 1 2 3
copy_sequential_icons 173 cookie-halo- cyan green yellow orange pink blue matrix-green
copy_sequential_icons 187 cookie-eyeball- red-hypnotic large-red-ring cluster brown-white-ring pink green

copy_icons \
  99 cookie-medium \
  101 cookie-plain \
  106 cookie-sugar \
  99 cookie-oatmeal-raisin \
  100 cookie-peanut-butter \
  102 cookie-coconut \
  104 cookie-macadamia-nut \
  936 cookie-almond \
  937 cookie-hazelnut \
  938 cookie-walnut \
  269 cookie-cashew \
  103 cookie-white-chocolate \
  270 cookie-milk-chocolate \
  105 cookie-double-chip \
  107 cookie-white-chocolate-macadamia-nut \
  108 cookie-all-chocolate \
  109 cookie-dark-chocolate-coated \
  110 cookie-white-chocolate-coated \
  134 cookie-eclipse \
  135 cookie-zebra \
  136 cookie-snickerdoodles \
  137 cookie-stroopwafels \
  138 cookie-macaroons \
  139 cookie-empire-biscuits \
  111 cookie-madeleines \
  112 cookie-palmiers \
  146 cookie-palets \
  147 cookie-sables \
  152 cookie-gingerbread-men \
  117 cookie-gingerbread-trees \
  125 cookie-pure-black-chocolate \
  160 cookie-pure-white-chocolate \
  126 cookie-ladyfingers \
  161 cookie-tuiles \
  127 cookie-chocolate-stuffed-biscuits \
  162 cookie-checker \
  128 cookie-butter \
  163 cookie-cream \
  369 cookie-gingersnaps \
  295 cookie-cinnamon

copy_tmp frame0.png frame-brown.png
copy_tmp frame1.png frame-brown-dark.png
copy_tmp frame2.png frame-brown-bright.png
copy_tmp cursor0.png cursor.png

generate_atlas game
generate_atlas loading
rm -rf ./cookies/target
