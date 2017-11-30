package com.pauljoda.vulcansrevenge.common.tools.sword.modes;

import com.pauljoda.vulcansrevenge.api.damage.DamageTypes;
import com.pauljoda.vulcansrevenge.api.sword.SwordMode;
import com.pauljoda.vulcansrevenge.common.tools.sword.ItemVulcanSword;
import com.teambr.nucleus.client.gui.GuiColor;
import com.teambr.nucleus.util.ClientUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
@SuppressWarnings("ConstantConditions")
public class SwordModeHeavy extends SwordMode {

    /**
     * Main Constructor
     */
    public SwordModeHeavy() {
        super("heavy");
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
     * @param player The player attacking
     * @return True if action done
     */
    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase victim, EntityLivingBase player) {
        ItemVulcanSword.verifyStackTag(stack);
        int charge = stack.getTagCompound().getInteger(ItemVulcanSword.NBT_CHARGE);

        if(charge > 0 && player instanceof EntityPlayer) {
            victim.attackEntityFrom(
                    new DamageTypes.EntityDamageSourceVulcan("player", player), 10);
            ItemVulcanSword.drainSwordCharge(stack, 1);
        }

        ItemVulcanSword.drainSwordCharge(stack, 20);
        return true;
    }

    /**
     * Called when the player right clicks with this mode enabled
     *
     * @param worldIn  The world
     * @param playerIn The player
     * @param handIn   The hand currently in
     * @return Pass or not
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemVulcanSword.verifyStackTag(playerIn.getHeldItem(handIn));
        int charge = playerIn.getHeldItem(handIn).getTagCompound().getInteger(ItemVulcanSword.NBT_CHARGE);
        if(!worldIn.isRemote && charge > 50) {
            List<Entity> entitiesInRange = new ArrayList<>();
            entitiesInRange.addAll(worldIn.getEntitiesWithinAABB(EntityLiving.class,
                    new AxisAlignedBB(
                            playerIn.posX - 5,
                            playerIn.posY - 1,
                            playerIn.posZ - 5,
                            playerIn.posX + 5,
                            playerIn.posY + 3,
                            playerIn.posZ + 5)));
            for(Entity mob : entitiesInRange)
                mob.attackEntityFrom(new DamageTypes.EntityDamageSourceVulcan("player", playerIn), 5);
            ItemVulcanSword.drainSwordCharge(playerIn.getHeldItem(handIn), 50);
            return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    /**
     * The base damage this mode does
     *
     * @return Damage done
     */
    @Override
    public int getBaseDamage() {
        return 40;
    }

    /**
     * Get the amount to remove from speed
     *
     * @return -3.9 - 0 with -3.9 meaning its really, really slow
     */
    @Override
    public float getAttackSpeed() {
        return -3.5F;
    }

    /**
     * Color associated with this mode
     * @return The color of this mode
     */
    @SideOnly(Side.CLIENT)
    @Override
    public Color getColor() {
        return new Color(47, 0, 255);
    }

    /**
     * Used to get the tool tip info to provide
     * <p>
     * Each object is one line, provide useful info here about the sword mode
     *
     * @return A list of strings to display
     */
    @Override
    public List<String> getToolTipInfo() {
        List<String> toolTip = new ArrayList<>();

        toolTip.add(GuiColor.BLUE +
                ClientUtils.translate("vulcansrevenge.hiteffect.text"));
        toolTip.add(GuiColor.LIGHTGRAY + ClientUtils.translate("vulcansrevenge:heavy.hitEffect"));

        toolTip.add("");
        toolTip.add(GuiColor.BLUE +
                ClientUtils.translate("vulcansrevenge.rightClick.text"));
        toolTip.add(GuiColor.LIGHTGRAY + ClientUtils.translate("vulcansrevenge:heavy.rightClick"));

        return toolTip;
    }
}
