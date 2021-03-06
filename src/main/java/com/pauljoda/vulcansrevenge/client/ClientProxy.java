package com.pauljoda.vulcansrevenge.client;

import com.pauljoda.vulcansrevenge.VulcansRevenge;
import com.pauljoda.vulcansrevenge.client.model.ModelVulcanSword;
import com.pauljoda.vulcansrevenge.client.renderers.tile.TileRendererSwordPedestal;
import com.pauljoda.vulcansrevenge.common.CommonProxy;
import com.pauljoda.vulcansrevenge.common.tools.sword.tile.TileSwordPedestal;
import com.pauljoda.vulcansrevenge.event.TextureEvents;
import com.pauljoda.vulcansrevenge.managers.EventManager;
import com.pauljoda.vulcansrevenge.managers.ToolManager;
import com.teambr.nucleus.annotation.IRegisterable;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.item.ItemMap;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import static com.pauljoda.vulcansrevenge.client.model.ModelVulcanSword.LOCATION;

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
public class ClientProxy extends CommonProxy {

    /**
     * Called before things are registered, so register things
     *
     * @param event The event passed from the main mod
     */
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        EventManager.registerEvent(new TextureEvents());

        // Sword Models
        ModelLoader.setCustomMeshDefinition(ToolManager.vulcanSword, stack -> LOCATION);
        ModelBakery.registerItemVariants(ToolManager.vulcanSword, LOCATION);
        ModelLoaderRegistry.registerLoader(ModelVulcanSword.LoaderVulcanSword.INSTANCE);
    }

    /**
     * Called once things are registered but don't have full values, add values
     *
     * @param event Event from mod
     */
    @Override
    public void init(FMLInitializationEvent event) {
        // Item Models
        VulcansRevenge.registrationData.getItems().forEach (item -> ((IRegisterable<?>)item).registerRender());
        VulcansRevenge.registrationData.getBlocks().forEach(item -> ((IRegisterable<?>)item).registerRender());

        // Keybinding
        KeybindHandler.getInstance().registerBindings();

        // Tiles
        ClientRegistry.bindTileEntitySpecialRenderer(TileSwordPedestal.class, new TileRendererSwordPedestal<>());
    }

    /**
     * Called once all registration and creation is done, should adjust to things now but no change to effect others
     *
     * @param event Event from mod
     */
    @Override
    public void postInit(FMLPostInitializationEvent event) { }
}
