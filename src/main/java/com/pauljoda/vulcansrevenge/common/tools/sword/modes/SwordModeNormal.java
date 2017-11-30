package com.pauljoda.vulcansrevenge.common.tools.sword.modes;

import com.pauljoda.vulcansrevenge.api.sword.SwordMode;
import com.pauljoda.vulcansrevenge.common.tools.sword.ItemVulcanSword;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

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
        ItemVulcanSword.verifyStackDamage(stack);

        if(attacker instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) attacker;

        }
        return true;
    }

    /**
     * The base damage this mode does
     *
     * @return Damage done
     */
    @Override
    public int getBaseDamage() {
        return 10;
    }

    /**
     * Get the amount to remove from speed
     *
     * @return -3.9 - 0 with -3.9 meaning its really, really slow
     */
    @Override
    public float getAttackSpeed() {
        return -2.4F;
    }

    /**
     * Color associated with this mode
     * @return The color of this mode
     */
    @SideOnly(Side.CLIENT)
    @Override
    public Color getColor() {
        return new Color(255, 77, 47);
    }
}
