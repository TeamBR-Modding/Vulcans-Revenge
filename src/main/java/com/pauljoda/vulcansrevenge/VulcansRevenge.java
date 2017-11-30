package com.pauljoda.vulcansrevenge;

import com.pauljoda.vulcansrevenge.api.sword.SwordMode;
import com.pauljoda.vulcansrevenge.common.CommonProxy;
import com.pauljoda.vulcansrevenge.managers.EventManager;
import com.pauljoda.vulcansrevenge.lib.Reference;
import com.pauljoda.vulcansrevenge.managers.PacketManager;
import com.pauljoda.vulcansrevenge.managers.ToolManager;
import com.pauljoda.vulcansrevenge.registry.VulcanRegistries;
import com.teambr.nucleus.data.RegistrationData;
import com.teambr.nucleus.helper.RegistrationHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

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

    public static CreativeTabs tabVulcansRevenge = new CreativeTabs("vulcans_revenge:tools") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ToolManager.vulcanSword);
        }
    };

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        RegistrationHelper.fillRegistrationData(event, registrationData);

        // Create Registries
        VulcanRegistries.SWORD_MODE_REGISTRY =
                (ForgeRegistry<SwordMode>) new RegistryBuilder<SwordMode>()
                        .setName(new ResourceLocation(Reference.MOD_ID, "sword_mode"))
                        .setIDRange(1, Integer.MAX_VALUE - 1)
                        .setType(SwordMode.class).create();

        EventManager.registerEvents(event.getSide());
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent event) {
        // Init Network Packets
        PacketManager.initPackets();

        // Call proxies
        proxy.init(event);
    }

    @Mod.EventHandler
    public static void posInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
