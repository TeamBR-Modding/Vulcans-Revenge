package com.pauljoda.vulcansrevenge.common.tools;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.awt.*;

/**
 * This file was created for VulcansRevenge
 * <p>
 * VulcansRevenge is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 11/29/17
 */
@SideOnly(Side.CLIENT)
public interface IRadialMenuProvider<T extends IForgeRegistryEntry<T>> {

    /**
     * Returns the class for the registry that populates the radial menu
     * @return The registry class, eg SwordMode.class
     */
    Class<T> getRegistryClass();

    /**
     * Get the name to display for the given entry
     * @param entry The entry
     * @return Display name, translated
     */
    String getDisplayNameForEntry(Object entry);

    /**
     * The color associated with this entry
     * @param entry The entry
     * @return The color to display
     */
    Color getDisplayColorForEntry(Object entry);

    /**
     * Get the stack to display for the provided entry
     * @param entry The entry
     * @return The stack to return or EMPTY for none
     */
    ItemStack getDisplayStackForEntry(Object entry);

    /**
     * Called when the user clicks this mode
     * @param entry The object clicked
     */
    void objectClicked(ItemStack stack, Object entry);
}
