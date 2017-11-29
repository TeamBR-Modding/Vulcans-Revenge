package com.pauljoda.vulcansrevenge.registry;

import com.pauljoda.vulcansrevenge.api.sword.SwordMode;
import com.pauljoda.vulcansrevenge.common.sword.SwordModeHeavy;
import com.pauljoda.vulcansrevenge.common.sword.SwordModeNormal;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistry;

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
public class VulcanRegistries {

    // Sword Modes
    public static ForgeRegistry<SwordMode> SWORD_MODE_REGISTRY;

    @SubscribeEvent
    public void registerSwordModes(RegistryEvent.Register<SwordMode> event) {
        event.getRegistry().register(new SwordModeNormal());
        event.getRegistry().register(new SwordModeHeavy());
    }
}
