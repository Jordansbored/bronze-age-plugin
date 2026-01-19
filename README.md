# Bronze Age Plugin

A Hytale plugin that adds tin ore, bronze alloy crafting, and bronze-tier tools with custom visual effects.

## Installation

1. Download `BronzeAge-1.0.0.jar` from the [releases page](https://github.com/Jordansbored/bronze-age-plugin/releases)
2. Place the JAR file in your Hytale mods folder:
   - `~/.var/app/com.hypixel.HytaleLauncher/data/Hytale/UserData/Mods/` (Linux)
   - `%APPDATA%\Hytale\UserData\Mods\` (Windows)
3. Launch Hytale and enjoy!

## Features

### Ore Generation
- **Tin Ore** generates naturally in the world
- Navy blue metallic ore texture
- Smelt in a furnace to create tin ingots

### Tin Ingots
- Smelted from tin ore (10 seconds)
- Used for bronze alloy crafting and tin arrows

### Alloy Furnace
The heavy industrial **Alloy Furnace** combines copper and tin to create bronze:
- 2 Copper Ingots + 1 Tin Ingot → 1 Bronze Ingot (60 seconds)
- Custom copper/bronze tinted textures
- **Dramatic slag spark effect**: Hot orange sparks fly out and cool to black ash
- Enhanced fire particles with brighter orange glow (1.5x intensity, 8 block range)
- 3 fuel slots with 2x fuel consumption

### Bronze Tools
All bronze tools are **Uncommon** quality (green) with 250 durability:
- **Bronze Pickaxe**: 3 bronze + 6 wood + 1 fibre
- **Bronze Hatchet**: 3 bronze + 6 wood + 3 fibre
- **Bronze Shovel**: 1 bronze + 4 wood + 1 fibre
- **Bronze Hoe**: 2 bronze + 4 wood + 1 fibre

### Tin Arrows
- **Tin Arrow**: 8 sticks + 1 tin ingot → 8 arrows (7 damage)
- Navy blue metallic arrow tips

### Construction Materials
- **Unfired Stone**: 2 coal + 4 rock = 4 unfired stone (workbench)
- **Fired Stone**: Smelt unfired stone in furnace (15 seconds)
- Dark black textures for industrial look

### Bronze Decorations
Bronze decoration blocks crafted at the Builders Workbench:
- **Bronze Bars**: 1 Bronze Ingot → 2 bars
- **Bronze Bars Corner**: Corner piece variant
- **Bronze Bars Platform**: Platform piece variant
- **Bronze Chains**: Decorative hanging chains
- **Vertical Bronze Chains**: Vertical chain variant
- **Small Bronze Chain**: Climbable chain (2 per ingot)
- **Stack of Bronze Ingots**: Decorative ingot pile
- All use copper-orange metallic bronze color (#B87333)

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

### Builders Workbench
| Output | Inputs |
|--------|--------|
| Bronze Bars | 1 Bronze Ingot (x2 output) |
| Bronze Bars Corner | Parent: Bronze Bars |
| Bronze Bars Platform | 1 Bronze Ingot (x2 output) |
| Bronze Chains | 1 Bronze Ingot |
| Vertical Bronze Chains | Parent: Bronze Chains |
| Small Bronze Chain | 1 Bronze Ingot (x2 output, climbable) |
| Stack of Bronze Ingots | 1 Bronze Ingot |

## Visual Effects

### Alloy Furnace Effects
- Custom copper/bronze tinted textures for block and icon
- Multi-layered fire effects (3x fire layers + campfire particles)
- **Slag spark system**: Molten orange sparks that cool to black ash
- High-intensity orange glow (1.5x, 8 block range)
- 300 spawn rate / 75 max concurrent particles for dramatic effect

### Custom Textures
- Tin items: Navy blue metallic finish
- Bronze tools: Orange/copper metallic with preserved wood handles
- Fired stone: Dark black industrial look
- Alloy Furnace: Copper/bronze tinted furnace block

## Requirements

- Hytale game client
- **Coal Ore Mod** (required for crafting Unfired Stone)
- No other mods required

## Credits

- Created by Jordansbored
- Custom metallic textures for tin and bronze items
- Custom particle effects for Alloy Furnace

## Source

https://github.com/Jordansbored/bronze-age-plugin
