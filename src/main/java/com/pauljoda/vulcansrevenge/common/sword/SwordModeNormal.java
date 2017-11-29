package com.pauljoda.vulcansrevenge.common.sword;

import com.pauljoda.vulcansrevenge.api.sword.SwordMode;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

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
public class SwordModeNormal extends SwordMode {

    /**
     * Main Constructor
     */
    public SwordModeNormal() {
        super("normal");
    }

    /**
     * Called when an entity is hit
     * You should handle the following:
     * - Damage victim
     * - Modify charge to sword
     * - Misc effects (eg sounds)
     *
     * @param stack  The stack that holds the sword
     * @param victim Entity being attacked
     * @param attacker The player attacking
     * @return True if action done
     */
    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase victim, EntityLivingBase attacker) {
        // Make sure we don't have damage
        SwordMode.verifyStackDamage(stack);

        if(attacker instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) attacker;

        }
        return true;
    }
}
