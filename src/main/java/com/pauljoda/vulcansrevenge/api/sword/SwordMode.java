package com.pauljoda.vulcansrevenge.api.sword;

import com.pauljoda.vulcansrevenge.api.VulcansRevengeAPI;
import com.pauljoda.vulcansrevenge.lib.Reference;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

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

    /*******************************************************************************************************************
     * Sword Mode                                                                                                      *
     *******************************************************************************************************************/

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
     * Helper method to ensure damage stays away
     * @param stack Stack
     */
    public static void verifyStackDamage(ItemStack stack) {
        if(stack.getItemDamage() > 0)
            stack.setItemDamage(0);
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
