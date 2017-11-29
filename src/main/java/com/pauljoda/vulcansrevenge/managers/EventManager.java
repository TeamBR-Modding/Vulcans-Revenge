package com.pauljoda.vulcansrevenge.managers;

import com.pauljoda.vulcansrevenge.registry.VulcanRegistries;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;

/**
 * This file was created for VulcansRevenge
 * <p>
 * VulcansRevenge is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 **
 * @author Paul Davis - pauljoda
 * @since 11/28/17
 */
public class EventManager {

    public static void registerEvents(Side side) {
        registerEvent(new RegistrationManager());
        registerEvent(new VulcanRegistries());
    }

    public static void registerEvent(Object obj) {
        MinecraftForge.EVENT_BUS.register(obj);
    }
}
