package com.pauljoda.vulcansrevenge.world;

import com.pauljoda.vulcansrevenge.lib.Reference;
import com.pauljoda.vulcansrevenge.managers.ConfigManager;
import com.teambr.nucleus.util.WorldUtils;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.fml.common.IWorldGenerator;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;
import java.util.Random;

/**
 * This file was created for VulcansRevenge
 * <p>
 * VulcansRevenge is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 12/8/17
 */
public class WorldGenSwordAlter implements IWorldGenerator {
    /**
     * Generate some world
     *
     * @param random         the chunk specific {@link Random}.
     * @param chunkX         the chunk X coordinate of this chunk.
     * @param chunkZ         the chunk Z coordinate of this chunk.
     * @param world          : additionalData[0] The minecraft {@link World} we're generating for.
     * @param chunkGenerator : additionalData[1] The {@link IChunkProvider} that is generating.
     * @param chunkProvider  : additionalData[2] {@link IChunkProvider} that is requesting the world generation.
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if(random.nextInt(1000) == 50) {
            if (ArrayUtils.contains(ConfigManager.sword_alter_dimensions.getIntList(), world.provider.getDimension())) {
                int x = chunkX * 16;
                int z = chunkZ * 16;
                int y = 255;
                while(world.isAirBlock(new BlockPos(x, y, z)))
                    y--;
                y++;

                if (world.getBiome(new BlockPos(x, y, z)) == Biomes.PLAINS
                        || world.getBiome(new BlockPos(x, y, z)) == Biomes.EXTREME_HILLS) {
                    // Add the alter
                    TemplateManager templateManager = ((WorldServer) world).getStructureTemplateManager();
                    Template alter = templateManager.getTemplate(world.getMinecraftServer(), new ResourceLocation(Reference.MOD_ID, "sword_alter"));
                    alter.addBlocksToWorld(world, new BlockPos(x, y, z), new PlacementSettings());

                    // Fill with a pillar
                    List<BlockPos> floorList = WorldUtils.getMatchingFloor(alter);
                    BlockPos origin = new BlockPos(x + 15, y, z + 15);
                    origin = origin.down();

                    while(WorldUtils.doesContainAirBlock(world, floorList, origin)) {
                        for(BlockPos pos : floorList)
                            world.setBlockState(new BlockPos(
                                    origin.getX() + pos.getX() - 15,
                                    origin.getY() + pos.getY(),
                                    origin.getZ() + pos.getZ() - 15
                            ), random.nextInt(6) > 1 ? Blocks.STONEBRICK.getDefaultState() :
                                    Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY));
                        origin = origin.down();
                    }
                }
            }
        }
    }
}
