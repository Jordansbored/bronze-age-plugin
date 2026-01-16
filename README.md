# Bronze Age Plugin

A Hytale plugin that adds tin ore, bronze alloy crafting, and bronze-tier tools.

## Installation

1. Download `BronzeAge-1.0.0.jar` from the [releases page](https://github.com/Jordansbored/bronze-age-plugin/releases)
2. Place the JAR file in your Hytale mods folder:
   - `~/.var/app/com.hypixel.HytaleLauncher/data/Hytale/UserData/Mods/` (Linux)
   - `%APPDATA%\Hytale\UserData\Mods\` (Windows)
3. Launch Hytale and enjoy!

## Features

### Ore Generation
- **Tin Ore** generates naturally in the world
- Mine tin ore and smelt it in a furnace to get tin ingots

### Bronze Alloy Crafting
The **Alloy Furnace** combines copper and tin to create bronze:
- 2 Copper Ingots + 1 Tin Ingot → 1 Bronze Ingot (60 seconds)

### Bronze Tools
All bronze tools are **Uncommon** quality (green) with 250 durability:
- **Bronze Pickaxe**: 3 bronze + 6 wood + 1 fibre
- **Bronze Hatchet**: 3 bronze + 6 wood + 3 fibre
- **Bronze Shovel**: 1 bronze + 4 wood + 1 fibre
- **Bronze Hoe**: 2 bronze + 4 wood + 1 fibre

### Tin Arrows
- **Tin Arrow**: 8 sticks + 1 tin ingot → 8 arrows (7 damage)

### Alloy Furnace Construction
Build the Alloy Furnace using fired stone and iron:
1. **Unfired Stone**: 2 coal + 4 rock → 4 unfired stone (workbench)
2. **Fired Stone**: Smelt unfired stone in furnace → fired stone
3. **Alloy Furnace**: 16 fired stone + 8 iron ingots → Alloy Furnace (workbench)

## Crafting Chain

```
Coal + Stone → Unfired Stone → (Furnace) → Fired Stone → Alloy Furnace
                                          ↓
                    Tin Ore → Tin Ingot ← Copper Ingot ← (Alloy Furnace)
                                                              ↓
                                                        Bronze Ingot → Bronze Tools
```

## Recipes

### Smelting
| Input | Output | Time |
|-------|--------|------|
| Tin Ore | Tin Ingot | 10s |
| Unfired Stone | Fired Stone | 15s |
| 2 Copper Ingot + 1 Tin Ingot | Bronze Ingot | 60s (Alloy Furnace) |

### Workbench
| Output | Inputs |
|--------|--------|
| 4 Unfired Stone | 2 Coal + 4 Rock |
| Alloy Furnace | 16 Fired Stone + 8 Iron Ingot |
| 8 Tin Arrows | 8 Stick + 1 Tin Ingot |
| Bronze Pickaxe | 3 Bronze + 6 Wood + 1 Fibre |
| Bronze Hatchet | 3 Bronze + 6 Wood + 3 Fibre |
| Bronze Shovel | 1 Bronze + 4 Wood + 1 Fibre |
| Bronze Hoe | 2 Bronze + 4 Wood + 1 Fibre |

## Requirements

- Hytale game client
- No other mods required

## Credits

- Created by Jordansbored
- Textures: Custom metallic textures for tin and bronze items

## Source

https://github.com/Jordansbored/bronze-age-plugin
