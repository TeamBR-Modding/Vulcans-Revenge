package com.pauljoda.vulcansrevenge.common;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * This file was created for VulcansRevenge
 * <p>
 * VulcansRevenge is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 11/23/17
 */
public class CommonProxy {

    /**
     * Called before things are registered, so register things
     * @param event The event passed from the main mod
     */
    public void preInit(FMLPreInitializationEvent event) { }

    /**
     * Called once things are registered but don't have full values, add values
     * @param event Event from mod
     */
    public void init(FMLInitializationEvent event) { }

    /**
     * Called once all registration and creation is done, should adjust to things now but no change to effect others
     * @param event Event from mod
     */
    public void postInit(FMLPostInitializationEvent event) { }
}
