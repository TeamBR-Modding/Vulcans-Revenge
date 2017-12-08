package com.pauljoda.vulcansrevenge.common.tools.sword.tile;

import com.teambr.nucleus.common.tiles.InventoryHandler;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

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
public class TileSwordPedestal extends InventoryHandler {
    /**
     * The initial size of the inventory
     *
     * @return How big to make the inventory on creation
     */
    @Override
    protected int getInventorySize() {
        return 1;
    }

    /**
     * Used to define if an item is valid for a slot
     *
     * @param index The slot id
     * @param stack The stack to check
     * @return True if you can put this there
     */
    @Override
    protected boolean isItemValidForSlot(int index, ItemStack stack) {
        return stack.getItem() == Items.IRON_SWORD;
    }

    @Override
    protected void onInventoryChanged(int slot) {
        markForUpdate(6);
    }

    /**
     * Used to set the value of a field
     *
     * @param id    The field id
     * @param value The value of the field
     */
    @Override
    public void setVariable(int id, double value) {

    }

    /**
     * Used to get the field on the server, this will fetch the server value and overwrite the current
     *
     * @param id The field id
     * @return The value on the server, now set to ourselves
     */
    @Override
    public Double getVariable(int id) {
        return null;
    }
}
