package com.pauljoda.vulcansrevenge.client;

import com.pauljoda.vulcansrevenge.client.gui.GuiRadialMenu;
import com.pauljoda.vulcansrevenge.api.client.IRadialMenuProvider;
import com.pauljoda.vulcansrevenge.lib.Reference;
import com.pauljoda.vulcansrevenge.managers.EventManager;
import com.teambr.nucleus.util.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

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
public class KeybindHandler {

    private static final KeybindHandler instance = new KeybindHandler();

    private final KeyBinding radialMenu =
            new KeyBinding(ClientUtils.translate("vulcansrevenge.text.radialMenuKey"), Keyboard.KEY_G, Reference.MOD_NAME);

    public void registerBindings() {
        ClientRegistry.registerKeyBinding(radialMenu);
        EventManager.registerEvent(instance);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (radialMenu.isPressed()) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            if (!player.getHeldItemMainhand().isEmpty()
                    && player.getHeldItemMainhand().getItem() instanceof IRadialMenuProvider) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiRadialMenu(player.getHeldItemMainhand()));
            }
        }
    }

    /*******************************************************************************************************************
     * Accessors and Mutators                                                                                          *
     *******************************************************************************************************************/

    public KeyBinding getRadialMenu() {
        return radialMenu;
    }

    public static KeybindHandler getInstance() {
        return instance;
    }
}
