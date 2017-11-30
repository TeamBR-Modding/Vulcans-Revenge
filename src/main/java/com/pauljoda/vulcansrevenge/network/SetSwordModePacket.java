package com.pauljoda.vulcansrevenge.network;

import com.pauljoda.vulcansrevenge.api.sword.SwordMode;
import com.pauljoda.vulcansrevenge.common.tools.sword.ItemVulcanSword;
import com.pauljoda.vulcansrevenge.managers.ToolManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
public class SetSwordModePacket implements IMessage, IMessageHandler<SetSwordModePacket, IMessage> {

    // Variables
    String modeResource;

    // Stub for registration
    public SetSwordModePacket() {}

    public SetSwordModePacket(ResourceLocation resourceLocation) {
        modeResource = resourceLocation.toString();
    }

    /*******************************************************************************************************************
     * IMessage                                                                                                        *
     *******************************************************************************************************************/

    /**
     * Convert from the supplied buffer into your specific message type
     *
     * @param buf
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        modeResource = ByteBufUtils.readUTF8String(buf);
    }

    /**
     * Deconstruct your message into the supplied byte buffer
     *
     * @param buf
     */
    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, modeResource);
    }

    /*******************************************************************************************************************
     * IMessageHandler                                                                                                 *
     *******************************************************************************************************************/

    /**
     * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
     * is needed.
     *
     * @param message The message
     * @param ctx
     * @return an optional return message
     */
    @Override
    public IMessage onMessage(SetSwordModePacket message, MessageContext ctx) {
        if(ctx.side.isServer()) {
            if(message.modeResource != null) {
                EntityPlayer player = ctx.getServerHandler().player;
                if(player.getHeldItemMainhand().getItem() == ToolManager.vulcanSword) {
                    ItemVulcanSword.setSwordMode(player.getHeldItemMainhand(),
                            GameRegistry.findRegistry(SwordMode.class).getValue(new ResourceLocation(message.modeResource)));
                    player.inventory.markDirty();
                }
            }
        }
        return null;
    }
}
