package com.pauljoda.vulcansrevenge.managers;

import com.pauljoda.vulcansrevenge.world.WorldGenSwordAlter;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
public class WorldGenManager {

    public static void preInit() {
        GameRegistry.registerWorldGenerator(new WorldGenSwordAlter(), 0);
    }
}
