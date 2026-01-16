package com.jordansbored.bronzeage;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.event.EventPriority;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.DefaultArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.events.ChunkPreLoadProcessEvent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.math.util.ChunkUtil;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * Bronze Age Plugin - Adds tin ore, bronze alloy crafting, and bronze-tier tools
 * 
 * Features:
 * - Tin ore natural generation (Y=10-50, rarer than coal)
 * - Alloy Furnace for smelting bronze (160x fuel consumption)
 * - Bronze tools (between stone and iron tier)
 * 
 * Tin Ore Generation:
 * - Spawns between Y=10 and Y=50
 * - 50% chunk chance (uncommon)
 * - 2-3 veins per chunk when spawning
 * 
 * Commands (Creative mode):
 * - /bronzeage spawn [size] - Spawns a tin ore vein at your location
 * - /bronzeage generate [radius] [count] - Generates multiple veins in an area
 */
public class BronzeAgePlugin extends JavaPlugin {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    
    // Tin ore block ID - defined in our pack as Ore_Tin_Stone
    private static final String TIN_ORE_BLOCK = "Ore_Tin_Stone";
    private static final String STONE_BLOCK = "Rock_Stone";
    
    // Tin ore generation settings (rarer than coal, deeper range)
    private static final int MIN_Y = 10;
    private static final int MAX_Y = 50;
    private static final int VEINS_PER_CHUNK = 2;  // 2-3 veins when spawning
    private static final int MIN_VEIN_SIZE = 2;
    private static final int MAX_VEIN_SIZE = 5;
    private static final double SPAWN_CHANCE = 0.5; // 50% chance per chunk (uncommon)
    
    private final Random random = new Random();
    
    // Cached block IDs for performance (initialized on first use)
    private int tinOreId = Integer.MIN_VALUE;
    private BlockType tinOreType = null;
    private int stoneId = Integer.MIN_VALUE;
    private int[] replaceableBlockIds = null;

    public BronzeAgePlugin(@Nonnull JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Bronze Age plugin loaded - version " + this.getManifest().getVersion().toString());
    }

    @Override
    protected void setup() {
        LOGGER.atInfo().log("Setting up Bronze Age plugin...");
        
        // Register chunk generation event for natural tin ore spawning
        // Use LATE priority so terrain is fully generated before we add ores
        this.getEventRegistry().registerGlobal(
            EventPriority.LATE, 
            ChunkPreLoadProcessEvent.class, 
            this::onChunkGenerated
        );
        
        // Register commands for manual ore spawning
        this.getCommandRegistry().registerCommand(new BronzeAgeCommand());
        
        LOGGER.atInfo().log("Bronze Age plugin setup complete!");
        LOGGER.atInfo().log("  - Tin ore generation: ENABLED (Y=" + MIN_Y + " to Y=" + MAX_Y + ", ~" + VEINS_PER_CHUNK + " veins/chunk, " + (int)(SPAWN_CHANCE*100) + "%% chance)");
        LOGGER.atInfo().log("  - Commands: /bronzeage spawn|generate");
    }
    
    /**
     * Called when a chunk is about to be loaded. If it's newly generated,
     * we add tin ore veins to it.
     */
    private void onChunkGenerated(@Nonnull ChunkPreLoadProcessEvent event) {
        // Only process newly generated chunks, not chunks loaded from disk
        if (!event.isNewlyGenerated()) {
            return;
        }
        
        WorldChunk chunk = event.getChunk();
        if (chunk == null) {
            return;
        }
        
        // Initialize block IDs on first use
        if (!initializeBlockIds()) {
            return;
        }
        
        // Get chunk coordinates (block coordinates of chunk corner)
        int chunkX = chunk.getX() << 5;  // Multiply by 32 (chunk size)
        int chunkZ = chunk.getZ() << 5;
        
        // Create a seeded random for this chunk so generation is deterministic
        // Use different seed multipliers than coal ore to avoid overlap
        long chunkSeed = ((long) chunk.getX() * 987654321L) + ((long) chunk.getZ() * 123456789L);
        Random chunkRandom = new Random(chunkSeed);
        
        // Chance for this chunk to have any tin ore at all (50% - uncommon)
        if (chunkRandom.nextDouble() > SPAWN_CHANCE) {
            return; // No tin ore in this chunk
        }
        
        int totalPlaced = 0;
        int veinsCreated = 0;
        
        // Generate veins in this chunk (2-3 veins when spawning)
        int numVeins = VEINS_PER_CHUNK + chunkRandom.nextInt(2); // 2-3 veins
        
        for (int i = 0; i < numVeins; i++) {
            // Random position within chunk
            int x = chunkX + chunkRandom.nextInt(32);
            int z = chunkZ + chunkRandom.nextInt(32);
            
            // Y level - tin is found at moderate depths
            int y = MIN_Y + chunkRandom.nextInt(MAX_Y - MIN_Y);
            
            // Random vein size (smaller than coal)
            int size = MIN_VEIN_SIZE + chunkRandom.nextInt(MAX_VEIN_SIZE - MIN_VEIN_SIZE + 1);
            
            int placed = generateVeinInChunk(chunk, x, y, z, size, chunkRandom);
            if (placed > 0) {
                totalPlaced += placed;
                veinsCreated++;
            }
        }
        
        if (veinsCreated > 0) {
            LOGGER.atFine().log("Generated %d tin ore veins (%d blocks) in chunk [%d, %d]", 
                veinsCreated, totalPlaced, chunk.getX(), chunk.getZ());
        }
    }
    
    /**
     * Initialize and cache block IDs for better performance.
     * @return true if initialization succeeded
     */
    private boolean initializeBlockIds() {
        if (tinOreId != Integer.MIN_VALUE) {
            return true; // Already initialized
        }
        
        tinOreType = BlockType.getAssetMap().getAsset(TIN_ORE_BLOCK);
        if (tinOreType == null) {
            LOGGER.atWarning().log("Tin ore block type '%s' not found! Natural generation disabled.", TIN_ORE_BLOCK);
            return false;
        }
        
        tinOreId = BlockType.getAssetMap().getIndex(TIN_ORE_BLOCK);
        stoneId = BlockType.getAssetMap().getIndex(STONE_BLOCK);
        
        // Cache all replaceable block IDs (same as coal ore)
        String[] replaceableBlockNames = {
            "Rock_Stone", "Rock_Stone_Cobble", "Rock_Stone_Mossy",
            "Rock_Sandstone", "Rock_Sandstone_Cobble",
            "Rock_Basalt", "Rock_Basalt_Cobble",
            "Rock_Marble", "Rock_Marble_Cobble",
            "Rock_Granite", "Rock_Granite_Cobble",
            "Dirt", "Dirt_Grass", "Dirt_Dry",
            "Gravel", "Clay"
        };
        
        replaceableBlockIds = new int[replaceableBlockNames.length];
        for (int i = 0; i < replaceableBlockNames.length; i++) {
            replaceableBlockIds[i] = BlockType.getAssetMap().getIndex(replaceableBlockNames[i]);
        }
        
        LOGGER.atInfo().log("Initialized tin ore generation - ore ID: %d, stone ID: %d", tinOreId, stoneId);
        return true;
    }
    
    /**
     * Generate a tin ore vein within a chunk during world generation.
     */
    private int generateVeinInChunk(WorldChunk chunk, int centerX, int centerY, int centerZ, int size, Random rand) {
        int placed = 0;
        
        // Generate a blob-like vein using multiple overlapping spheres
        for (int i = 0; i < size; i++) {
            float progress = (float) i / size;
            float angle1 = rand.nextFloat() * (float) Math.PI * 2;
            float angle2 = rand.nextFloat() * (float) Math.PI * 2;
            
            int offsetX = (int) (Math.cos(angle1) * progress * 2);
            int offsetY = (int) (Math.sin(angle1) * Math.cos(angle2) * progress * 2);
            int offsetZ = (int) (Math.sin(angle2) * progress * 2);
            
            int x = centerX + offsetX;
            int y = centerY + offsetY;
            int z = centerZ + offsetZ;
            
            // Place a small cluster at this position
            int clusterRadius = 1 + rand.nextInt(2);
            
            for (int dx = -clusterRadius; dx <= clusterRadius; dx++) {
                for (int dy = -clusterRadius; dy <= clusterRadius; dy++) {
                    for (int dz = -clusterRadius; dz <= clusterRadius; dz++) {
                        double dist = Math.sqrt(dx*dx + dy*dy + dz*dz);
                        if (dist <= clusterRadius + rand.nextFloat() * 0.5) {
                            int bx = x + dx;
                            int by = y + dy;
                            int bz = z + dz;
                            
                            // Bounds check
                            if (by < 1 || by > 310) continue;
                            
                            // Check if this block is in the current chunk
                            int blockChunkX = bx >> 5;
                            int blockChunkZ = bz >> 5;
                            if (blockChunkX != chunk.getX() || blockChunkZ != chunk.getZ()) {
                                continue; // Skip blocks outside this chunk
                            }
                            
                            // Try to place tin ore
                            if (placeTinOreInChunk(chunk, bx, by, bz)) {
                                placed++;
                            }
                        }
                    }
                }
            }
        }
        
        return placed;
    }
    
    /**
     * Place a single tin ore block in a chunk, only replacing stone-like blocks.
     */
    private boolean placeTinOreInChunk(WorldChunk chunk, int x, int y, int z) {
        try {
            int currentBlock = chunk.getBlock(x, y, z);
            
            // Check if current block is replaceable
            if (isReplaceableBlockId(currentBlock)) {
                // setBlock: x, y, z, blockId, blockType, rotation, filler, settings
                // settings: 4 = no particles, helps with performance during generation
                chunk.setBlock(x, y, z, tinOreId, tinOreType, 0, 0, 4);
                return true;
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Fast check if a block ID is replaceable (uses cached IDs).
     */
    private boolean isReplaceableBlockId(int blockId) {
        if (blockId == stoneId) return true;
        
        if (replaceableBlockIds != null) {
            for (int id : replaceableBlockIds) {
                if (id != Integer.MIN_VALUE && id == blockId) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    // ========== COMMANDS (for manual ore spawning) ==========
    
    /**
     * Main command collection for bronze age operations
     */
    private class BronzeAgeCommand extends AbstractCommandCollection {
        
        public BronzeAgeCommand() {
            super("bronzeage", "Bronze Age plugin commands - spawn tin ore");
            this.addAliases("ba", "tin");
            this.setPermissionGroup(GameMode.Creative);
            this.addSubCommand(new SpawnCommand());
            this.addSubCommand(new GenerateCommand());
        }
    }
    
    /**
     * Spawns a single tin ore vein at the player's location
     */
    private class SpawnCommand extends AbstractPlayerCommand {
        
        @Nonnull
        private final DefaultArg<Integer> sizeArg = this.withDefaultArg(
            "size", "Size of the vein (1-15)", ArgTypes.INTEGER, 5, "Vein size"
        );
        
        public SpawnCommand() {
            super("spawn", "Spawn a tin ore vein at your location");
        }
        
        @Override
        protected void execute(@Nonnull CommandContext context, @Nonnull Store<EntityStore> store, 
                             @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
            
            TransformComponent transform = store.getComponent(ref, TransformComponent.getComponentType());
            if (transform == null) {
                context.sendMessage(Message.raw("Could not get player position!"));
                return;
            }
            
            Vector3d pos = transform.getPosition();
            int x = (int) pos.x;
            int y = (int) pos.y - 2;
            int z = (int) pos.z;
            
            int size = Math.max(1, Math.min(15, sizeArg.get(context)));
            
            // Debug: Check if block type is found
            BlockType testType = BlockType.getAssetMap().getAsset(TIN_ORE_BLOCK);
            if (testType == null) {
                context.sendMessage(Message.raw("ERROR: Block type '" + TIN_ORE_BLOCK + "' not found!"));
                // List some available block types for debugging
                context.sendMessage(Message.raw("Checking for copper ore..."));
                BlockType copperType = BlockType.getAssetMap().getAsset("Ore_Copper_Stone");
                context.sendMessage(Message.raw("Ore_Copper_Stone: " + (copperType != null ? "FOUND" : "NOT FOUND")));
                return;
            }
            context.sendMessage(Message.raw("Block type found: " + TIN_ORE_BLOCK));
            
            world.execute(() -> {
                int placed = spawnTinOreVein(world, x, y, z, size);
                context.sendMessage(Message.raw("Spawned tin ore vein with " + placed + " blocks at (" + x + ", " + y + ", " + z + ")"));
            });
        }
    }
    
    /**
     * Generates multiple tin ore veins in an area around the player
     */
    private class GenerateCommand extends AbstractPlayerCommand {
        
        @Nonnull
        private final DefaultArg<Integer> radiusArg = this.withDefaultArg(
            "radius", "Radius to generate in", ArgTypes.INTEGER, 32, "Generation radius"
        );
        
        @Nonnull
        private final DefaultArg<Integer> countArg = this.withDefaultArg(
            "count", "Number of veins to generate", ArgTypes.INTEGER, 10, "Vein count"
        );
        
        public GenerateCommand() {
            super("generate", "Generate multiple tin ore veins in an area");
        }
        
        @Override
        protected void execute(@Nonnull CommandContext context, @Nonnull Store<EntityStore> store, 
                             @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
            
            TransformComponent transform = store.getComponent(ref, TransformComponent.getComponentType());
            if (transform == null) {
                context.sendMessage(Message.raw("Could not get player position!"));
                return;
            }
            
            Vector3d pos = transform.getPosition();
            int centerX = (int) pos.x;
            int centerZ = (int) pos.z;
            
            int radius = Math.max(1, Math.min(128, radiusArg.get(context)));
            int count = Math.max(1, Math.min(100, countArg.get(context)));
            
            context.sendMessage(Message.raw("Generating " + count + " tin ore veins in radius " + radius + "..."));
            
            world.execute(() -> {
                int totalPlaced = 0;
                int veinsCreated = 0;
                
                for (int i = 0; i < count; i++) {
                    int x = centerX + random.nextInt(radius * 2) - radius;
                    int z = centerZ + random.nextInt(radius * 2) - radius;
                    int y = MIN_Y + random.nextInt(MAX_Y - MIN_Y);
                    int size = MIN_VEIN_SIZE + random.nextInt(MAX_VEIN_SIZE - MIN_VEIN_SIZE + 1);
                    
                    int placed = spawnTinOreVein(world, x, y, z, size);
                    if (placed > 0) {
                        totalPlaced += placed;
                        veinsCreated++;
                    }
                }
                
                context.sendMessage(Message.raw("Generated " + veinsCreated + " veins with " + totalPlaced + " total tin ore blocks!"));
            });
        }
    }
    
    /**
     * Spawns a tin ore vein at the specified position (for commands).
     */
    private int spawnTinOreVein(World world, int centerX, int centerY, int centerZ, int size) {
        if (!initializeBlockIds()) {
            return 0;
        }
        
        int placed = 0;
        
        for (int i = 0; i < size; i++) {
            float progress = (float) i / size;
            float angle1 = random.nextFloat() * (float) Math.PI * 2;
            float angle2 = random.nextFloat() * (float) Math.PI * 2;
            
            int offsetX = (int) (Math.cos(angle1) * progress * 2);
            int offsetY = (int) (Math.sin(angle1) * Math.cos(angle2) * progress * 2);
            int offsetZ = (int) (Math.sin(angle2) * progress * 2);
            
            int x = centerX + offsetX;
            int y = centerY + offsetY;
            int z = centerZ + offsetZ;
            
            int clusterRadius = 1 + random.nextInt(2);
            
            for (int dx = -clusterRadius; dx <= clusterRadius; dx++) {
                for (int dy = -clusterRadius; dy <= clusterRadius; dy++) {
                    for (int dz = -clusterRadius; dz <= clusterRadius; dz++) {
                        double dist = Math.sqrt(dx*dx + dy*dy + dz*dz);
                        if (dist <= clusterRadius + random.nextFloat() * 0.5) {
                            int bx = x + dx;
                            int by = y + dy;
                            int bz = z + dz;
                            
                            if (by < 1 || by > 310) continue;
                            
                            if (placeTinOre(world, bx, by, bz)) {
                                placed++;
                            }
                        }
                    }
                }
            }
        }
        
        return placed;
    }
    
    /**
     * Places a single tin ore block (for commands), only replacing stone-like blocks.
     */
    private boolean placeTinOre(World world, int x, int y, int z) {
        try {
            long chunkIndex = ChunkUtil.indexChunkFromBlock(x, z);
            WorldChunk chunk = (WorldChunk) world.getNonTickingChunk(chunkIndex);
            
            if (chunk == null) {
                return false;
            }
            
            int currentBlock = chunk.getBlock(x, y, z);
            
            if (isReplaceableBlockId(currentBlock)) {
                chunk.setBlock(x, y, z, tinOreId, tinOreType, 0, 0, 4);
                return true;
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
