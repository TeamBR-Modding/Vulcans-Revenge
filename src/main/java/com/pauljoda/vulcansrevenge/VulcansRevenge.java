package com.pauljoda.vulcansrevenge;

import com.pauljoda.vulcansrevenge.common.CommonProxy;
import com.pauljoda.vulcansrevenge.lib.Reference;
import com.teambr.nucleus.data.RegistrationData;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
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
@Mod(
        modid        = Reference.MOD_ID,
        name         = Reference.MOD_NAME,
        dependencies =  Reference.DEPENDENCIES
)
public class VulcansRevenge {

    @Mod.Instance
    public static VulcansRevenge INSTANCE;

    // Registration Data
    public static RegistrationData registrationData = new RegistrationData(Reference.MOD_NAME);

    // Proxy
    @SidedProxy(
            clientSide = "com.pauljoda.vulcansrevenge.client.ClientProxy",
            serverSide = "com.pauljoda.vulcansrevenge.common.CommonProxy"
    )
    public static CommonProxy proxy;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) { }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent event) { }

    @Mod.EventHandler
    public static void posInit(FMLPostInitializationEvent event) { }
}
