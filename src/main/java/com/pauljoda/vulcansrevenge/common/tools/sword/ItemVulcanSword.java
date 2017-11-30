package com.pauljoda.vulcansrevenge.common.tools.sword;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.pauljoda.vulcansrevenge.VulcansRevenge;
import com.pauljoda.vulcansrevenge.api.sword.SwordMode;
import com.pauljoda.vulcansrevenge.common.tools.IRadialMenuProvider;
import com.pauljoda.vulcansrevenge.lib.Reference;
import com.pauljoda.vulcansrevenge.managers.PacketManager;
import com.pauljoda.vulcansrevenge.network.SetSwordModePacket;
import com.pauljoda.vulcansrevenge.registry.VulcanRegistries;
import com.teambr.nucleus.annotation.IRegisterable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import java.awt.*;
import java.util.Objects;

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
public class ItemVulcanSword extends ItemSword implements IRegisterable<Item>, IRadialMenuProvider<SwordMode> {

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
        this.getAttackDamage();
    }

    /*******************************************************************************************************************
     * ItemVulcanSword                                                                                                 *
     *******************************************************************************************************************/

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
        verifyStackDamage(stack);
        verifyStackTag(stack);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        verifyStackTag(stack);
        verifyStackDamage(stack);
        getSwordMode(stack).hitEntity(stack, target, attacker);
        return false;
    }

    /**
     * Ensures we have a tag in proper format
     *
     * @param stack The stack
     */

    public static void verifyStackTag(ItemStack stack) {
        // Verify we have a tag
        if (!stack.hasTagCompound() || Objects.equals(stack.getTagCompound().getString(NBT_MODE), "")) {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            nbtTagCompound.setString(NBT_MODE, "vulcansrevenge:normal");
            stack.setTagCompound(nbtTagCompound);
        }
    }

    /**
     * Helper method to ensure damage stays away
     * @param stack Stack
     */
    public static void verifyStackDamage(ItemStack stack) {
        if(stack.getItemDamage() > 0)
            stack.setItemDamage(0);
    }

    /**
     * Set the stack to the provided mode
     *
     * @param stack The stack to set
     * @param mode  The sword mode
     */
    public static void setSwordMode(ItemStack stack, SwordMode mode) {
        verifyStackTag(stack);
        stack.getTagCompound().setString(NBT_MODE, mode.getRegistryName().toString());
    }

    /**
     * Get the current mode of the itemstack
     *
     * @param stack
     * @return
     */
    public static SwordMode getSwordMode(ItemStack stack) {
        verifyStackTag(stack);
        SwordMode mode =
                GameRegistry.findRegistry(SwordMode.class)
                        .getValue(new ResourceLocation(stack.getTagCompound().getString(NBT_MODE)));
        return mode == null ? VulcanRegistries.SWORD_MODE_REGISTRY.getValue(0) : mode;
    }

    /**
     * Used to define our attributes, not needed as we will apply manually via stack one
     */
    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        return HashMultimap.create();
    }

    /**
     * Sets attributes MC needs
     * @param slot Slot
     * @param stack Stack
     * @return List of attributes
     */
    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.create();

        verifyStackTag(stack);

        if (slot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
                    new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", getSwordMode(stack).getBaseDamage(), 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
                    new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier",  getSwordMode(stack).getAttackSpeed(), 0));
        }

        return multimap;
    }

    /*******************************************************************************************************************
     * Builder Plate stuff                                                                                             *
     *******************************************************************************************************************/

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        boolean bool = super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
        verifyStackDamage(stack);
        return bool;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
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

    /*******************************************************************************************************************
     * IRadialMenuProvider                                                                                             *
     *******************************************************************************************************************/

    /**
     * Returns the class for the registry that populates the radial menu
     *
     * @return The registry class, eg SwordMode.class
     */
    @Override
    public Class<SwordMode> getRegistryClass() {
        return SwordMode.class;
    }

    /**
     * Get the name to display for the given entry
     *
     * @param entry The entry
     * @return Display name, translated
     */
    @Override
    public String getDisplayNameForEntry(Object entry) {
        return ((SwordMode) entry).getModeDisplayName();
    }

    /**
     * The color associated with this entry
     *
     * @param entry The entry
     * @return The color to display
     */
    @Override
    public Color getDisplayColorForEntry(Object entry) {
        return ((SwordMode) entry).getColor();
    }

    /**
     * Get the stack to display for the provided entry
     *
     * @param entry The entry
     * @return The stack to return or EMPTY for none
     */
    @Override
    public ItemStack getDisplayStackForEntry(Object entry) {
        ItemStack stack = new ItemStack(this);
        verifyStackTag(stack);
        setSwordMode(stack, (SwordMode) entry);
        return stack;
    }

    /**
     * Called when the user clicks this mode
     *
     * @param entry The object clicked
     */
    @Override
    public void objectClicked(ItemStack stack, Object entry) {
        setSwordMode(stack, (SwordMode) entry);
        PacketManager.net.sendToServer(new SetSwordModePacket(getSwordMode(stack).getRegistryName()));
    }
}
