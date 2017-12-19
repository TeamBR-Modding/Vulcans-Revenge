package com.pauljoda.vulcansrevenge.common.tools.sword.tile;

import com.teambr.nucleus.common.tiles.InventoryHandler;
import com.teambr.nucleus.util.ClientUtils;
import com.teambr.nucleus.util.TimeUtils;
import net.minecraft.command.CommandTitle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentString;

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
 * @since 12/8/17
 */
public class TileSwordPedestal extends InventoryHandler {

    /*******************************************************************************************************************
     * Variables                                                                                                       *
     *******************************************************************************************************************/

    // Range
    private static final int RADIUS = 15;

    // Players Visited
    private List<String> playersGreeted = new ArrayList<>();

    // Players in area
    private List<EntityPlayer> players = new ArrayList<>();

    /*******************************************************************************************************************
     * UpdatingTile                                                                                                    *
     *******************************************************************************************************************/

    @Override
    protected void onServerTick() {
        super.onServerTick();
        updatePlayerList();

        // Greet new players
        greetNewPlayers();
    }

    /*******************************************************************************************************************
     * Helpers                                                                                                         *
     *******************************************************************************************************************/

    /**
     * Updates the list of players to those within range
     */
    private void updatePlayerList() {
        players = world.getPlayers(EntityPlayer.class,
                player ->
                        player != null
                                && player.getDistance(pos.getX(), pos.getY(), pos.getZ()) < RADIUS + 1
                                && player.posY < pos.getY() + 3);
    }

    /**
     * Greets players that have not seen this alter
     */
    private void greetNewPlayers() {
        players.forEach(
                player -> {
                    if (!playersGreeted.contains(player.getDisplayNameString())) {
                        player.sendMessage(new TextComponentString(
                                ClientUtils.translate("vulcansrevenge.vulcan.greeting")
                                        .replaceAll("#playerName", player.getDisplayNameString())));
                        playersGreeted.add(player.getDisplayNameString());
                    }
                }
        );
    }

    /*******************************************************************************************************************
     * InventoryHandler                                                                                                *
     *******************************************************************************************************************/

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

    /*******************************************************************************************************************
     * Syncable                                                                                                        *
     *******************************************************************************************************************/

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        // Get greeted players
        NBTTagList playersGreetedList = compound.getTagList("GreetedPlayers", 10);
        playersGreeted.clear();
        playersGreetedList.forEach(tag -> playersGreeted.add(((NBTTagString) tag).getString()));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);

        // Add greeted players
        NBTTagList playersGreetedList = new NBTTagList();
        for (String name : playersGreeted)
            playersGreetedList.appendTag(new NBTTagString(name));
        compound.setTag("GreetedPlayers", playersGreetedList);

        return compound;
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
