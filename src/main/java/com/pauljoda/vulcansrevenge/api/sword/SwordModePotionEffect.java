package com.pauljoda.vulcansrevenge.api.sword;

import com.pauljoda.vulcansrevenge.api.sword.SwordMode;
import com.pauljoda.vulcansrevenge.common.tools.sword.ItemVulcanSword;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

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
 * @since 11/30/17
 */
public abstract class SwordModePotionEffect extends SwordMode {

    // Potion
    protected Potion potion;

    /**
     * Main Constructor
     */
    public SwordModePotionEffect(String name, Potion toApply) {
        super(name);
        potion = toApply;
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
    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase victim, EntityLivingBase player) {
        ItemVulcanSword.verifyStackTag(stack);
        int charge = stack.getTagCompound().getInteger(ItemVulcanSword.NBT_CHARGE);
        if (charge > 5)
            victim.addPotionEffect(new PotionEffect(
                    potion,
                    200,
                    100));
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
    @SuppressWarnings("ConstantConditions")
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemVulcanSword.verifyStackTag(playerIn.getHeldItem(handIn));
        int charge = playerIn.getHeldItem(handIn).getTagCompound().getInteger(ItemVulcanSword.NBT_CHARGE);
        if (!worldIn.isRemote && charge > 10) {
            List<EntityLiving> entitiesInRange = new ArrayList<>();
            entitiesInRange.addAll(worldIn.getEntitiesWithinAABB(EntityLiving.class,
                    new AxisAlignedBB(
                            playerIn.posX - 5,
                            playerIn.posY - 1,
                            playerIn.posZ - 5,
                            playerIn.posX + 5,
                            playerIn.posY + 3,
                            playerIn.posZ + 5)));
            for (EntityLiving mob : entitiesInRange)
                mob.addPotionEffect(new PotionEffect(
                        potion,
                        50,
                        100));
            ItemVulcanSword.drainSwordCharge(playerIn.getHeldItem(handIn), 10);
            return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}