package com.pauljoda.vulcansrevenge.api.sword;

import com.pauljoda.vulcansrevenge.api.VulcansRevengeAPI;
import com.pauljoda.vulcansrevenge.lib.Reference;
import com.teambr.nucleus.util.ClientUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
public abstract class SwordMode implements IForgeRegistryEntry<SwordMode> {

    /*******************************************************************************************************************
     * Variables                                                                                                       *
     *******************************************************************************************************************/

    public String modeName;

    /*******************************************************************************************************************
     * Constructors                                                                                                    *
     *******************************************************************************************************************/

    /**
     * Main Constructor
     * @param name The name of the sword mode
     */
    public SwordMode(String name) {
        this.modeName = name;
    }

    /*******************************************************************************************************************
     * Abstract Methods                                                                                                *
     *******************************************************************************************************************/

    /**
     * Called when an entity is hit
     * You should handle the following:
     *  - Damage victim
     *  - Modify charge to sword
     *  - Misc effects (eg sounds)
     * @param stack The stack that holds the sword
     * @param victim Entity being attacked
     * @param player The player attacking
     * @return True if action done
     */
    public abstract boolean hitEntity(ItemStack stack, EntityLivingBase victim, EntityLivingBase player);

    /**
     * The base damage this mode does
     * @return Damage done
     */
    public abstract int getBaseDamage();

    /**
     * Get the amount to remove from speed
     * @return -3.9 - 0 with -3.9 meaning its really, really slow
     */
    public abstract float getAttackSpeed();

    /*******************************************************************************************************************
     * Sword Mode                                                                                                      *
     *******************************************************************************************************************/

    /**
     * Called when the stack updates, use for things that should be done each tick
     * @param stack The sword stack
     * @param worldIn The world
     * @param entityIn The entity
     * @param itemSlot The item slot
     * @param isSelected if selected
     */
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

    }

    /**
     * Called when the player right clicks with this mode enabled
     * @param worldIn The world
     * @param playerIn The player
     * @param handIn The hand currently in
     * @return Pass or not
     */
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    /**
     * Used to get the tool tip info to provide
     *
     * Each object is one line, provide useful info here about the sword mode
     * @return A list of strings to display
     */
    @SideOnly(Side.CLIENT)
    public List<String> getToolTipInfo() {
        return new ArrayList<>();
    }

    /**
     * Texture path
     *
     * @return The resource location of the texture for this mode
     */
    @SideOnly(Side.CLIENT)
    public ResourceLocation getTexturePath() {
        return new ResourceLocation(VulcansRevengeAPI.MOD_ID, "items/sword/" + modeName.toLowerCase());
    }

    /**
     * Gets the name to display about this mode
     * @return The translated name to display. Used for displaying info
     */
    @SuppressWarnings("ConstantConditions")
    @SideOnly(Side.CLIENT)
    public String getModeDisplayName() {
        return ClientUtils.translate(getRegistryName().toString() + ".displayName");
    }

    /**
     * Color associated with this mode
     * @return The color of this mode
     */
    @SideOnly(Side.CLIENT)
    public Color getColor() {
        return new Color(0, 0, 0);
    }

    /*******************************************************************************************************************
     * IForgeRegistryEntry                                                                                             *
     *******************************************************************************************************************/

    /**
     * Sets a unique name for this Item. This should be used for uniquely identify the instance of the Item.
     * This is the valid replacement for the atrocious 'getUnlocalizedName().substring(6)' stuff that everyone does.
     * Unlocalized names have NOTHING to do with unique identifiers. As demonstrated by vanilla blocks and items.
     * <p>
     * The supplied name will be prefixed with the currently active mod's modId.
     * If the supplied name already has a prefix that is different, it will be used and a warning will be logged.
     * <p>
     * If a name already exists, or this Item is already registered in a registry, then an IllegalStateException is thrown.
     * <p>
     * Returns 'this' to allow for chaining.
     *
     * @param name Unique registry name
     * @return This instance
     */
    @Override
    public SwordMode setRegistryName(ResourceLocation name) {
        return this;
    }

    /**
     * A unique identifier for this entry, if this entry is registered already it will return it's official registry name.
     * Otherwise it will return the name set in setRegistryName().
     * If neither are valid null is returned.
     *
     * @return Unique identifier or null.
     */
    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return new ResourceLocation(VulcansRevengeAPI.MOD_ID + ":" + modeName);
    }

    @Override
    public Class<SwordMode> getRegistryType() {
        return SwordMode.class;
    }
}
