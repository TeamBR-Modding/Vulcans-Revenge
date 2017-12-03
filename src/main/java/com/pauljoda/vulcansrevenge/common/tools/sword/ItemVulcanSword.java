package com.pauljoda.vulcansrevenge.common.tools.sword;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.pauljoda.vulcansrevenge.VulcansRevenge;
import com.pauljoda.vulcansrevenge.api.sword.SwordMode;
import com.pauljoda.vulcansrevenge.api.client.IRadialMenuProvider;
import com.pauljoda.vulcansrevenge.lib.Reference;
import com.pauljoda.vulcansrevenge.managers.PacketManager;
import com.pauljoda.vulcansrevenge.network.SetSwordModePacket;
import com.pauljoda.vulcansrevenge.registry.VulcanRegistries;
import com.teambr.nucleus.annotation.IRegisterable;
import com.teambr.nucleus.client.gui.GuiColor;
import com.teambr.nucleus.common.IAdvancedToolTipProvider;
import com.teambr.nucleus.helper.ItemRenderHelper;
import com.teambr.nucleus.util.ClientUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
public class ItemVulcanSword extends ItemSword implements IRegisterable<Item>, IRadialMenuProvider<SwordMode>, IAdvancedToolTipProvider {

    // Tool Material
    public static ToolMaterial TOOL_MATERIAL_VULCAN_SWORD =
            EnumHelper.addToolMaterial(Reference.MOD_ID + ":vulcan_sword", 4, 100, 8.0F, 0, 0);

    // String to access current mode on NBT
    public static final String NBT_MODE = "CurrentMode";
    public static final String NBT_OWNER = "Owner";
    public static final String NBT_CHARGE = "Charge";

    public static final int MAX_CHARGE = 10000;

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

    /**
     * Called each tick
     *
     * @param stack      The stack
     * @param worldIn    The world
     * @param entityIn   The entity
     * @param itemSlot   Slot number
     * @param isSelected If holding
     */
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
        verifyStackDamage(stack);
        verifyStackTag(stack);

        // Assign owner if none found
        if (!worldIn.isRemote
                && stack.getTagCompound().getUniqueId(NBT_OWNER) != null
                && entityIn instanceof EntityPlayer) {
            stack.getTagCompound().setUniqueId(NBT_OWNER, entityIn.getUniqueID());
        }

        // Slowdown those who are not the owner
        if (!worldIn.isRemote
                && stack.getTagCompound().getUniqueId(NBT_OWNER) != null
                && !stack.getTagCompound().getUniqueId(NBT_OWNER).equals(entityIn.getUniqueID())
                && entityIn instanceof EntityPlayer) {
            ((EntityPlayer) entityIn).addPotionEffect(new PotionEffect(
                    Potion.getPotionFromResourceLocation("minecraft:slowness"),
                    150,
                    10)
            );
        }

        getSwordMode(stack).onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    /**
     * Called when an entity is hit, add extra stuff here
     *
     * @param stack    The stack
     * @param target   Who was hit
     * @param attacker Who hit
     * @return True if action performed
     */
    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        verifyStackTag(stack);
        verifyStackDamage(stack);
        getSwordMode(stack).hitEntity(stack, target, attacker);
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
        verifyStackDamage(playerIn.getHeldItem(handIn));
        verifyStackTag(playerIn.getHeldItem(handIn));
        return getSwordMode(playerIn.getHeldItem(handIn)).onItemRightClick(worldIn, playerIn, handIn);
    }

    /**
     * Sets attributes MC needs
     *
     * @param slot  Slot
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
                    new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", getSwordMode(stack).getAttackSpeed(), 0));
        }

        return multimap;
    }

    /**
     * Determine if the player switching between these two item stacks
     * @param oldStack The old stack that was equipped
     * @param newStack The new stack
     * @param slotChanged If the current equipped slot was changed,
     *                    Vanilla does not play the animation if you switch between two
     *                    slots that hold the exact same item.
     * @return True to play the item change animation
     */
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    /**
     * Determines if the durability bar should be rendered for this item.
     * Defaults to vanilla stack.isDamaged behavior.
     * But modders can use this for any data they wish.
     *
     * @param stack The current Item Stack
     * @return True if it should render the 'durability' bar.
     */
    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        verifyStackTag(stack);
        return stack.getTagCompound().getInteger(NBT_CHARGE) != MAX_CHARGE;
    }

    /**
     * Queries the percentage of the 'Durability' bar that should be drawn.
     *
     * @param stack The current ItemStack
     * @return 0.0 for 100% (no damage / full bar), 1.0 for 0% (fully damaged / empty bar)
     */
    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        verifyStackTag(stack);
        return 1.0F - (stack.getTagCompound().getInteger(NBT_CHARGE) * 1.0F) / (float) MAX_CHARGE;
    }

    /**
     * Returns the packed int RGB value used to render the durability bar in the GUI.
     * Defaults to a value based on the hue scaled based on {@link #getDurabilityForDisplay}, but can be overriden.
     *
     * @param stack Stack to get durability from
     * @return A packed RGB value for the durability colour (0x00RRGGBB)
     */
    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        verifyStackTag(stack);
        int charge = stack.getTagCompound().getInteger(NBT_CHARGE);
        int oneQuarter = MAX_CHARGE / 4;
        if (charge > oneQuarter * 3)
            return getSwordMode(stack).getColor().getRGB();
        else if (charge > oneQuarter * 2)
            return getSwordMode(stack).getColor().darker().getRGB();
        else
            return getSwordMode(stack).getColor().darker().darker().getRGB();
    }
    
    /*******************************************************************************************************************
     * Helper Methods                                                                                                  *
     *******************************************************************************************************************/

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
            nbtTagCompound.setInteger(NBT_CHARGE, MAX_CHARGE);
            stack.setTagCompound(nbtTagCompound);
        }
    }

    /**
     * Helper method to ensure damage stays away
     *
     * @param stack Stack
     */
    public static void verifyStackDamage(ItemStack stack) {
        if (stack.getItemDamage() > 0)
            stack.setItemDamage(0);
    }

    /**
     * Drain the sword of a give amount
     *
     * @param stack  The stack
     * @param amount How much to drain
     */
    public static void drainSwordCharge(ItemStack stack, int amount) {
        verifyStackTag(stack);
        int charge = stack.getTagCompound().getInteger(NBT_CHARGE);
        stack.getTagCompound().setInteger(NBT_CHARGE, Math.max(charge - amount, 0));
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

    /**
     * Used to define our attributes, not needed as we will apply manually via stack one
     */
    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        return HashMultimap.create();
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
        ItemRenderHelper.registerItem(this);
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

    /**
     * Gets the tip to display
     *
     * @param entry Entry
     * @return A tool tip list to disply on right side of screen
     */
    @Override
    public List<String> getTooltip(Object entry) {
        return ((SwordMode) entry).getToolTipInfo();
    }

    /*******************************************************************************************************************
     * IAdvanceToolTipProvider                                                                                         *
     *******************************************************************************************************************/

    /**
     * Get the tool tip to present when shift is pressed
     *
     * @param stack The itemstack
     * @return The list to display
     */
    @Nullable
    @Override
    public List<String> getAdvancedToolTip(@Nonnull ItemStack stack) {
        verifyStackTag(stack);
        return getSwordMode(stack).getToolTipInfo();
    }

    /**
     * Used to get the tip to display
     *
     * @param stack
     * @return The tip to display
     */
    @Nullable
    @Override
    public List<String> getToolTip(@Nonnull ItemStack stack) {
        verifyStackTag(stack);

        // Preserve extra stuff
        List<String> bellowTip =
                ClientUtils.isShiftPressed() ?
                        getAdvancedToolTip(stack) :
                        displayShiftForInfo(stack) ?
                                Collections.singletonList(ClientUtils.translate("nucleus.text.shiftInfo")) :
                                new ArrayList<>();

        // Add top info
        List<String> mainTip = new ArrayList<>();
        mainTip.add("");

        // Add owner
        if (stack.getTagCompound().getUniqueId(NBT_OWNER) != null) {
            mainTip.add(GuiColor.ORANGE + ClientUtils.translate("vulcansrevenge.owner.text"));

            if (Minecraft.getMinecraft().getIntegratedServer() != null &&
                    Minecraft.getMinecraft().getIntegratedServer()
                    .getPlayerProfileCache().getProfileByUUID(stack.getTagCompound()
                            .getUniqueId(NBT_OWNER)) != null)
                mainTip.add(GuiColor.WHITE + " " +
                        Minecraft.getMinecraft().getIntegratedServer()
                                .getPlayerProfileCache().getProfileByUUID(stack.getTagCompound()
                                .getUniqueId(NBT_OWNER)).getName());
            else
                mainTip.add(ClientUtils.translate(" " + "vulcansrevenge.unknown.text"));
        }

        // Add power
        mainTip.add("");
        mainTip.add(GuiColor.ORANGE + ClientUtils.translate("vulcansrevenge.charge.text"));
        mainTip.add(GuiColor.WHITE + "  " +
                ClientUtils.formatNumber(stack.getTagCompound().getInteger(NBT_CHARGE)) + " / " +
                ClientUtils.formatNumber(MAX_CHARGE));

        // Fill in extra
        mainTip.add("");
        mainTip.addAll(bellowTip);

        return mainTip;
    }
}
