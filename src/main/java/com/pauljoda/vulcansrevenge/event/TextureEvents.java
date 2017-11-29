package com.pauljoda.vulcansrevenge.event;

import com.pauljoda.vulcansrevenge.api.sword.SwordMode;
import com.pauljoda.vulcansrevenge.lib.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
public class TextureEvents {
    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {
        for(SwordMode swordMode : GameRegistry.findRegistry(SwordMode.class)) {
            event.getMap().registerSprite(swordMode.getTexturePath());
        }
    }
}
