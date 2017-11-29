package com.pauljoda.vulcansrevenge.common.sword;

import com.pauljoda.vulcansrevenge.VulcansRevenge;
import com.pauljoda.vulcansrevenge.api.sword.SwordMode;
import com.pauljoda.vulcansrevenge.lib.Reference;
import com.teambr.nucleus.annotation.IRegisterable;
import com.teambr.nucleus.helper.ItemRenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * This file was created for VulcansRevenge
 * <p>
 * VulcansRevenge is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 11/28/17
 */
public class ItemVulcanSword extends ItemSword implements IRegisterable<Item> {

    // Tool Material
    public static ToolMaterial TOOL_MATERIAL_VULCAN_SWORD =
            EnumHelper.addToolMaterial(Reference.MOD_ID + ":vulcan_sword", 4, 100, 8.0F, 0, 0);

    // String to access current mode on NBT
    public static final String NBT_MODE = "CurrentMode";

    /**
     * Main Constructor
     */
    public ItemVulcanSword() {
        super(TOOL_MATERIAL_VULCAN_SWORD);
        setCreativeTab(VulcansRevenge.tabVulcansRevenge);
        setMaxStackSize(1);
        setUnlocalizedName(Reference.MOD_ID + ":" + "item_vulcan_sword");
        setRegistryName(Reference.MOD_ID, "item_vulcan_sword");
    }

    /*******************************************************************************************************************
     * ItemVulcanSword                                                                                                 *
     *******************************************************************************************************************/

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
        SwordMode.verifyStackDamage(stack);

        // Verify we have a tag
        if (!stack.hasTagCompound()) {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            nbtTagCompound.setString(NBT_MODE, "vulcansrevenge:normal");
            stack.setTagCompound(nbtTagCompound);
        }
    }

    /*******************************************************************************************************************
     * Registration                                                                                                    *
     *******************************************************************************************************************/

    /**
     * Registers an object to the ForgeRegistry
     *
     * @param registry The Block Forge Registry
     */
    @Override
    public void registerObject(IForgeRegistry<Item> registry) {
        registry.register(this);
    }

    /**
     * Register the renderers for the block/item
     */
    @Override
    public void registerRender() {

    }
}
