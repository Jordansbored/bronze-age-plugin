# Bronze Decoration Textures - Texture Recoloring Guide

This document explains how the bronze decoration textures were created for the Bronze Age plugin.

## Source Materials

The source textures were taken from the steel-age-plugin which had already recolored iron textures to steel:
- `Iron_Bars_Texture.png` → `Steel_Bars_Texture.png`
- `Iron_Chains_Large_Texture.png` → `Steel_Chains_Texture.png`
- `Iron_Chain_Small_Texture.png` → `Steel_Chain_Small_Texture.png`

## Recoloring Process

Bronze color is approximately `#B87333` (copper-orange). To convert steel (dark grey #5A5A5A) to bronze:

```bash
magick <steel_source>.png -modulate <brightness>,<saturation>,<hue> <bronze_output>.png
```

### Bronze Formula

```bash
# For block textures
magick Iron_Bars_Texture.png -modulate 75,110,100 Bronze_Bars_Texture.png

# For icons (lighter, since icons are smaller)
magick Deco_Steel_Bars.png -modulate 115,100,100 Deco_Bronze_Bars.png
```

**Parameters:**
- **75% brightness**: Slightly darker than steel (steel uses 55%)
- **110% saturation**: More vivid than steel (steel uses 85%)
- **100% hue**: Keep color position, modulate shifts it naturally

The `-modulate` command in ImageMagick adjusts:
- Brightness: <100 = darker, >100 = lighter
- Saturation: <100 = less vivid, >100 = more vivid
- Hue: Rotation around color wheel (100 = unchanged)

## Files Created

### Block Textures (Common/Blocks/Tinkering/)
- `Bronze_Bars_Texture.png`
- `Bronze_Bars_Platform_Texture.png`
- `Bronze_Chains_Texture.png`
- `Bronze_Chain_Small_Texture.png`
- `Bronze_Ingot_Stack_Texture.png`

### Icons (Common/Icons/ItemsGenerated/)
- `Deco_Bronze_Bars.png`
- `Deco_Bronze_Bars_Corner.png`
- `Deco_Bronze_Bars_Platforms.png`
- `Deco_Bronze_Chains.png`
- `Deco_Bronze_Chains_Vertical.png`
- `Deco_Bronze_Chain_Small.png`
- `Deco_Bronze_Stack.png`

## Color Reference

| Material | Hex Color | RGB |
|----------|-----------|-----|
| Iron | #5A5A5A | 90, 90, 90 |
| Steel | #5A5A5A (darker) | 90, 90, 90 |
| Bronze | #B87333 | 184, 115, 51 |

## Workflow

1. Extract source textures from steel-age-plugin texture-work/
2. Apply ImageMagick modulate command
3. Copy textures to plugin resources directory
4. Update JSON files to reference new textures
5. Build and test in-game

## Troubleshooting

- **Too dark?**: Increase brightness percentage
- **Too washed out?**: Increase saturation percentage
- **Wrong color tone?**: Adjust hue (100-120 for warm copper)
- **Wood handles changed color?**: Use RGB channel shift method instead
