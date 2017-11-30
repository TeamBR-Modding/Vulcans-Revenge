package com.pauljoda.vulcansrevenge.client.gui;

import com.pauljoda.vulcansrevenge.client.KeybindHandler;
import com.pauljoda.vulcansrevenge.api.client.IRadialMenuProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
public class GuiRadialMenu extends GuiScreen {

    /*******************************************************************************************************************
     * Variables                                                                                                       *
     *******************************************************************************************************************/

    // Reference to stack that opened this
    private ItemStack heldStack;
    private IRadialMenuProvider<?> menuProvider;
    private Object currentSelected = null;

    // Rendering variables
    private int timeOpen = 0;
    private int selected = -1;

    /**
     * Main constructor
     * @param stack The stack that opened this gui
     */
    public GuiRadialMenu(ItemStack stack) {
        this.heldStack = stack;
        this.menuProvider = (IRadialMenuProvider<?>) heldStack.getItem();
    }

    /*******************************************************************************************************************
     * GuiScreen                                                                                                       *
     *******************************************************************************************************************/

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();

        final int x = width / 2;
        final int y = height / 2;
        final int maxRadius = 80;

        final float angle = mouseAngle(x, y, mouseX, mouseY);
        final float distance = mouseDistance(x, y, mouseX, mouseY);

        final int highlight = 5;

        GlStateManager.enableBlend();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);

        final int segments = GameRegistry.findRegistry(menuProvider.getRegistryClass()).getEntries().size();

        float totalDeg = 0F;
        final float degPer = 360F / segments;

        final List<int[]> stringPosition    = new ArrayList<>();
        final List<String> displayStrings   = new ArrayList<>();
        final List<ItemStack> displayStacks = new ArrayList<>();

        boolean wasSelected = false;

        // Gather information for display

        int seg = 0;
        for (Object obj : GameRegistry.findRegistry(menuProvider.getRegistryClass()).getValues()) {
            boolean mouseOverSection = distance <= maxRadius && (angle > totalDeg && angle < totalDeg + degPer);
            float radius = Math.max(0F, Math.min(timeOpen * 10F, maxRadius));

            GL11.glBegin(GL11.GL_TRIANGLE_FAN);

            Color color = menuProvider.getDisplayColorForEntry(obj);

            if(mouseOverSection) {
                selected = seg;
                currentSelected = obj;
                wasSelected = true;
                radius += highlight;
                color.brighter();
            }

            float r = (color.getRed()   / 255F);
            float g = (color.getGreen() / 255F);
            float b = (color.getBlue()  / 255F);
            float a = 0.4F;

            GlStateManager.color(r, g, b, a);
            GL11.glVertex2i(x, y);
            float i = degPer;
            while(i >= 0) {
                double rad = ((i + totalDeg) / 180F * Math.PI);
                double xp = x + Math.cos(rad) * radius;
                double yp = y + Math.sin(rad) * radius;
                if(i == (int)(degPer / 2))
                    stringPosition.add(new int[]{seg, (int)xp, (int)yp, mouseOverSection ? 'n' : 'r'});
                GL11.glVertex2d(xp, yp);
                i -= 1;
            }
            totalDeg += degPer;

            GL11.glVertex2i(x, y);
            GL11.glEnd();

            seg++;

            // Add display stack and name
            displayStacks.add(menuProvider.getDisplayStackForEntry(obj));
            displayStrings.add(menuProvider.getDisplayNameForEntry(obj));
        }

        if (!wasSelected)
            selected = -1;

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableTexture2D();

        // Actually draw the things in the menu
        for(int[] pos : stringPosition) {
            int slot = pos[0];
            float xp = pos[1];
            float yp = pos[2];
            char c = (char)pos[3];

            ItemStack displayStack = displayStacks.get(slot);
            if(!displayStack.isEmpty()) {
                float xsp = xp - 4;
                float ysp = yp;
                String name = "\u00a7" + c + displayStrings.get(slot);
                int width = fontRenderer.getStringWidth(name);

                float mod = 0.6F;
                int xdp = (int)((xp - x) * mod + x);
                int ydp = (int)((yp - y) * mod + y);

                RenderHelper.enableGUIStandardItemLighting();
                GlStateManager.pushMatrix();
                GlStateManager.translate(xdp - 10, ydp - 10, 2);
                GlStateManager.scale(1.25, 1.25, 1.25);
                Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(displayStack, 0, 0);
                GlStateManager.popMatrix();
                RenderHelper.disableStandardItemLighting();

                if(xsp < x)
                    xsp -= width - 8;
                if(ysp < y)
                    ysp -= 9;

                fontRenderer.drawStringWithShadow(name, xsp, ysp, 0xFFFFFF);
            }
        }

        // Draw Selected stack
        // Might add this later, for now no
        /**
        if(currentSelected != null) {
            ItemStack displayStack = menuProvider.getDisplayStackForEntry(currentSelected);
            if(!displayStack.isEmpty()) {
                RenderHelper.enableGUIStandardItemLighting();
                GlStateManager.pushMatrix();

                GlStateManager.translate(x + 130, y - 100, 2);
                GlStateManager.scale(3, 3, 3);
                Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(displayStack, 0, 0);

                GlStateManager.popMatrix();
                RenderHelper.disableStandardItemLighting();

                fontRenderer.drawStringWithShadow(menuProvider.getDisplayNameForEntry(currentSelected),
                        x + 145 - (fontRenderer.getStringWidth(menuProvider.getDisplayNameForEntry(currentSelected)) / 2),
                        y - 45,
                        menuProvider.getDisplayColorForEntry(currentSelected).getRGB());
            }
        }
         **/

        GlStateManager.popMatrix();
    }

    @Override
    public void updateScreen() {
        if (!isKeyDown(KeybindHandler.getInstance().getRadialMenu())) {
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
        timeOpen += 1;
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        if(Mouse.getEventButton() != -1
                && Mouse.getEventButtonState()
                && currentSelected != null)
            menuProvider.objectClicked(heldStack, currentSelected);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private boolean isKeyDown(KeyBinding keyBinding) {
        int key = keyBinding.getKeyCode();
        if (key < 0) {
            int button = 100 + key;
            return Mouse.isButtonDown(button);
        }
        return Keyboard.isKeyDown(key);
    }

    private float mouseDistance(int x, int y, int mx, int my) {
        return  (int)Math.abs(Math.sqrt(((mx - x) * (mx - x)) + ((my - y) * (my - y))));
    }

    private float mouseAngle(int x, int y, int mx, int my) {
        Vector2f baseVec = new Vector2f(1F, 0F);
        Vector2f mouseVec = new Vector2f(mx - x, my - y);
        Float angle = (float)(Math.acos(Vector2f.dot(baseVec, mouseVec) / (baseVec.length() * mouseVec.length())) * (180F / Math.PI));
        return my < y ? 360F - angle : angle;
    }
}
