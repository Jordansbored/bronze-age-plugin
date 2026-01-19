# Bronze Age Plugin for Hytale

A Hytale plugin that adds tin ore, bronze alloy crafting, and bronze-tier tools.

## Features

### New Ore
- **Tin Ore** - Uncommon, spawns Y=10-50, silver/grey tint

Generates naturally using `ChunkPreLoadProcessEvent` (same approach as Coal Ore plugin).

### Alloy Furnace
A heavy industrial furnace that consumes massive fuel to forge alloys.

**Stats (160x normal furnace fuel):**
- 160x fuel consumption (2 coal = 1 bronze ingot, or 32 sticks)
- ~100 seconds smelt time per bronze ingot
- Requires 2 copper + 1 tin per bronze ingot

**Effects while running:**
- Heavy smoke particles billowing from top
- Glowing/pulsing ember effect
- Loud furnace rumble sound
- Fire particles around base
- Screen shake when nearby (subtle)
- Light emission (bright orange glow)

### Bronze Tools
| Tool | Damage/Speed | Durability |
|------|--------------|------------|
| Bronze Pickaxe | Between stone & iron | ~250 |
| Bronze Axe | Between stone & iron | ~250 |
| Bronze Sword | Between stone & iron | ~250 |
| Bronze Shovel | Between stone & iron | ~250 |
| Bronze Hoe | Between stone & iron | ~250 |

### Ingots & Raw Materials
- Raw Tin (ore drop)
- Tin Ingot (smelt raw tin)
- Bronze Ingot (alloy furnace output)

Copper ore and copper ingots are already in the base game.

### Bronze Decorations
Bronze decoration blocks crafted at the Builders Workbench (StructuralCrafting):
- **Bronze Bars**: Windows, barriers (2 per ingot)
- **Bronze Bars Corner**: Corner piece variant
- **Bronze Bars Platform**: Platform/shelf variant (2 per ingot)
- **Bronze Chains**: Decorative hanging chains
- **Vertical Bronze Chains**: Vertical chain variant
- **Small Bronze Chain**: Climbable, used for climbing (2 per ingot)
- **Stack of Bronze Ingots**: Decorative ingot pile

All decoration blocks use:
- Iron bar/chain/blockymodel files (shared models)
- Bronze-tinted textures (orange/copper #B87333)
- Metal particle effects and sounds

## Progression

```
[Base Game] Copper Ingot ───────────────────────────────┐
                                                        ├→ [Alloy Furnace] → Bronze Ingot → Bronze Tools
Mine Tin Ore → Raw Tin → [Furnace] → Tin Ingot ────────┘
```

## Technical Approach

### Tin Ore Generation
Reuse coal ore pattern:
- Register `ChunkPreLoadProcessEvent` listener
- Spawn ore veins in newly generated chunks
- Tin: 2-3 veins/chunk, Y=10-50, 50% chunk chance (uncommon)

### Alloy Furnace
Adapt from QuadFurnace/ProcessingBench:
- Custom block with inventory state
- 2 input slots (copper ingot x2, tin ingot x1)
- 1 fuel slot (consumes 160x normal rate - 2 coal per bronze)
- 1 output slot (bronze ingot)
- ProcessingBenchState for smelting logic
- ~100 second smelt time

**Particle/Sound Effects:**
- Use `WorldNotificationHandler` for particles
- Smoke: `sendParticle()` at furnace top, continuous while active
- Embers: Fire particles around base
- Sound: Looping furnace rumble while smelting
- Light: Dynamic light source when active (if possible via block state)

### Items & Tools
JSON definitions in `Server/Item/Items/`:
- Tool stats (damage, speed, durability)
- Crafting recipes
- Localization strings

## File Structure

```
bronze-age-plugin/
├── src/main/java/com/jordansbored/bronzeage/
│   ├── BronzeAgePlugin.java        # Main plugin, tin ore generation
│   ├── AlloyFurnaceBlock.java      # Furnace block logic
│   └── AlloyFurnaceState.java      # Inventory & smelting state
├── src/main/resources/
│   ├── manifest.json
│   ├── Server/
│   │   ├── Item/Items/
│   │   │   ├── Ore/                # Tin ore block
│   │   │   ├── Ingot/              # Tin, Bronze ingots
│   │   │   ├── Tool/               # Bronze tools
│   │   │   └── Decoration/         # Bronze bars, chains
│   │   └── Languages/en-US/
│   ├── Common/
│   │   ├── Icons/                  # Item/block icons
│   │   └── Blocks/Tinkering/       # Bronze textures
├── build.gradle
└── gradle.properties
```

## TODO

- [ ] Set up Gradle project (copy from coal-ore-pack)
- [ ] Add tin ore generation
- [ ] Create tin ingot item
- [ ] Build alloy furnace block
- [ ] Add bronze ingot
- [ ] Create bronze tools
- [ ] Balance ore rarity & tool stats
- [ ] Test full progression
