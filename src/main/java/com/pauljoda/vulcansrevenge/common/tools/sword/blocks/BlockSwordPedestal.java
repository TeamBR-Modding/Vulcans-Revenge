package com.pauljoda.vulcansrevenge.common.tools.sword.blocks;

import com.pauljoda.vulcansrevenge.common.BaseBlock;
import com.pauljoda.vulcansrevenge.common.tools.sword.tile.TileSwordPedestal;
import com.teambr.nucleus.util.InventoryUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

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
@SuppressWarnings("deprecation")
public class BlockSwordPedestal extends BaseBlock {

    private static AxisAlignedBB BB = new AxisAlignedBB(2 / 16F, 0F, 5 / 16F, 14 / 16F, 3 / 16F, 11 / 16F);

    /**
     * Constructor
     */
    public BlockSwordPedestal() {
        super(Material.ROCK, "block_sword_pedestal", TileSwordPedestal.class);
    }


    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BB;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if(!worldIn.isRemote) {
            TileSwordPedestal pedestal = (TileSwordPedestal) worldIn.getTileEntity(pos);
            if (pedestal.getStackInSlot(0).isEmpty()
                    && !playerIn.getHeldItemMainhand().isEmpty()
                    && playerIn.getHeldItemMainhand().getItem() == Items.IRON_SWORD) {
                pedestal.setStackInSlot(0, new ItemStack(Items.IRON_SWORD));
                playerIn.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
            } else if (!pedestal.getStackInSlot(0).isEmpty() && playerIn.getHeldItemMainhand().isEmpty()) {
                playerIn.setHeldItem(EnumHand.MAIN_HAND, pedestal.getStackInSlot(0).copy());
                pedestal.setStackInSlot(0, ItemStack.EMPTY);
            }
            return true;
        }

        return true;
    }

    /*******************************************************************************************************************
     * Misc                                                                                                            *
     *******************************************************************************************************************/

    @Override
    protected float getHardness() {
        return -1;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    /**
     * Get the geometry of the queried face at the given position and state. This is used to decide whether things like
     * buttons are allowed to be placed on the face, or how glass panes connect to the face, among other things.
     * <p>
     * Common values are {@code SOLID}, which is the default, and {@code UNDEFINED}, which represents something that
     * does not fit the other descriptions and will generally cause other things not to connect to the face.
     *
     * @return an approximation of the form of the given face
     */
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(Blocks.AIR);
    }
}