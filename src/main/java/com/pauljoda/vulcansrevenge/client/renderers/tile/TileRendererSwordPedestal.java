package com.pauljoda.vulcansrevenge.client.renderers.tile;

import com.pauljoda.vulcansrevenge.common.tools.sword.tile.TileSwordPedestal;
import com.pauljoda.vulcansrevenge.managers.ToolManager;
import com.teambrmodding.assistedprogression.managers.ItemManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

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
public class TileRendererSwordPedestal<T extends TileSwordPedestal> extends TileEntitySpecialRenderer<T> {
    @Override
    public void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + -0.17D, y + 1.4, z + 0.5D);
        GlStateManager.scale(2, 2, 2);
        ItemStack stack = te.getStackInSlot(0);
        if (!stack.isEmpty()) {
            EntityItem entityItem = new EntityItem(getWorld(), 0.0, 0.0, 0.0, stack);
            entityItem.motionX = 0;
            entityItem.motionY = 0;
            entityItem.motionZ = 0;
            entityItem.hoverStart = 0;
            entityItem.rotationYaw = 0;

            GlStateManager.pushMatrix();

            RenderHelper.enableGUIStandardItemLighting();
            RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();

            renderManager.setRenderShadow(false);
            GlStateManager.pushAttrib();

            GlStateManager.rotate(225, 0, 0, 1);
            renderManager.renderEntity(entityItem, 0, 0, 0,
                    0.0F, 0, true);

            GlStateManager.popAttrib();
            GlStateManager.enableLighting();
            renderManager.setRenderShadow(true);
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();
    }
}
