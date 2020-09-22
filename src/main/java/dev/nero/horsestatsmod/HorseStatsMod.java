/*
* Copyright (C) 2020 @N3ROO on Github (Lilian Gallon)
* This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
* License as published by the Free Software Foundation; version 2. This program is distributed in the hope that it will
* be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
* PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General
* Public License along with this program.
*/

package dev.nero.horsestatsmod;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.nero.horsestatsmod.config.TheModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.HorseInventoryScreen;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;
import net.minecraft.util.text.*;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.gui.GuiUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod("horsestatsmod")
public class HorseStatsMod
{
    // If you need to log some stuff
    private static final Logger LOGGER = LogManager.getLogger();

    public HorseStatsMod() {
        MinecraftForge.EVENT_BUS.addListener(this::onDrawForegroundEvent);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, TheModConfig.CLIENT_SPEC);
    }

    private void onDrawForegroundEvent(final GuiContainerEvent.DrawForeground event) {
        if (event.getGuiContainer() instanceof HorseInventoryScreen) {
            try {
                // 1. GET THE STATISTICS OF THAT RIDDEN HORSE
                AbstractHorseEntity horse = (AbstractHorseEntity) Minecraft.getInstance().player.getRidingEntity();

                double health = horse.getAttribute(Attributes.field_233818_a_).getValue();
                double jumpHeight = horse.getAttribute(Attributes.field_233830_m_).getValue();
                double speed = horse.getAttribute(Attributes.field_233821_d_).getValue();

                jumpHeight = (
                        - 0.1817584952 * Math.pow(jumpHeight, 3) +
                        3.689713992 * Math.pow(jumpHeight, 2) +
                        2.128599134 * jumpHeight - 0.343930367
                ); // convert to blocks
                speed = speed * 43; // convert to m/s

                // 2. DISPLAY THE STATS

                // Mouse position (relative to the top left of the container) to know when to render the hovering text
                // This - 2*blabla shifts the mouse position so that (0, 0) is actually the top left of the container
                // Why event.getGuiContainer().getGuiLeft() is not already the left position of the container? I have
                // no clue lol
                int mouseX = (
                        (int) Minecraft.getInstance().mouseHelper.getMouseX()
                        - 2 * event.getGuiContainer().getGuiLeft()
                );
                int mouseY = (
                        (int) Minecraft.getInstance().mouseHelper.getMouseY()
                        - 2 * event.getGuiContainer().getGuiTop()
                );

                if (TheModConfig.displayStats()) {
                    // Show the stats on the GUI
                    displayStatsAndHoveringTexts(
                            event.getGuiContainer(),
                            mouseX, mouseY,
                            health, jumpHeight, speed);
                } else {
                    // Show the stats only if the mouse is on the horse's name
                    displayStatsInHoveringText(event.getGuiContainer(), mouseX, mouseY, health, jumpHeight, speed);
                }

            } catch (Exception e) {
                LOGGER.error("The player is looking into an horse inventory without riding it? Is that possible?");
            }
        }
    }

    private void displayStatsInHoveringText(
            ContainerScreen guiContainer,
            double mouseX, double mouseY,
            double health, double jumpHeight, double speed) {
        // TODO: inspect 2* everywhere
        // todo double -> int
        int rx = 0;
        int ry = 0;
        int rw = 300;
        int rh = 11;

        AbstractGui.func_238467_a_(
                new MatrixStack(),
                rx, ry,
                rx + rw, rx + rh,
                0x88666666
        );
    }

    private void displayStatsAndHoveringTexts(
            ContainerScreen guiContainer,
            int mouseX, int mouseY,
            double health, double jumpHeight, double speed) {

        // The boxes positions (x,y) and dimensions (w,h) defining when to display the hovering text relative to the
        // top left of the container
        int rx;
        int ry = 12;
        int rw = 59;
        int rh = 22;

        // Starts at x=120 by displaying "Stats:" (if it fits the GUI)
        rx = 120;

        // 7 is the maximum number of letters for "Stats" to be displayed, because otherwise it overlaps with
        // the horse's name
        if (!(Minecraft.getInstance().player.getRidingEntity().getDisplayName().getString().length() > 8))
            this.renderText("Stats:", rx, ry, 0X444444);

        // Health (60 units shift to the right)
        rx += 60;
        this.renderText(
                String.format("%.2f", health),
                rx, ry,
                TheModConfig.coloredStats() ? getColor(health, 15, 30) : 0X444444
        );

        if (posInRect(mouseX, mouseY, rx - 4,  ry - 4, rw, rh)) {
            drawHoveringText(
                    guiContainer,
                    mouseX, mouseY,
                    "Health:", "15.0", "30.0", "player: 20.0"
            );
        }

        // Jump (60 units shift to the right as well)
        rx += 60;
        this.renderText(
                String.format("%.2f", jumpHeight),
                rx, ry,
                TheModConfig.coloredStats() ? getColor(jumpHeight, 1.25, 5) : 0X444444
        );
        if (posInRect(mouseX, mouseY, rx - 4,  ry - 2, rw-12, rh)) { // -12 because max is x.xx and not xx.xx
            drawHoveringText(
                    guiContainer,
                    mouseX, mouseY,
                    "Jump (blocks):", "1.25", "5.0", "player: 1.25"
            );
        }

        // Speed (48 units shift to the right, not the same as before because jump max is x.xx and not xx.xx)
        rx += 48;
        this.renderText(
                String.format("%.2f", speed),
                rx, ry,
                TheModConfig.coloredStats() ? getColor(speed, 4.8, 14.5) : 0X444444
        );
        if (posInRect(mouseX, mouseY, rx-4,  ry-4, rw, rh)) {
            drawHoveringText(
                    guiContainer,
                    mouseX, mouseY,
                    "Speed (m/s):", "4.8", "14.5",
                    "player: 4.317 (walk)", "player: 5.612 (sprint)", "player: 7.143 (sprint+jump)"
            );
        }
    }

    private void drawHoveringText(Screen screen, int x, int y, String title, String min, String max, String... notes) {
        List<ITextComponent> textLines = new ArrayList<>();
        textLines.add(new StringTextComponent(title));
        textLines.add(new StringTextComponent(TextFormatting.RED + "min: " + min));
        textLines.add(new StringTextComponent(TextFormatting.GREEN + "max: " + max));
        for (String note : notes) {
            textLines.add(new StringTextComponent(note));
        }

        screen.func_243308_b(
                new MatrixStack(),
                textLines,
                // why /2? bc it works that way, I did not inspect the mc code in depth to understand
                x/2, y/2
        );
    }

    /**
     * @param val the value
     * @param min the min possible value
     * @param max the max possible value
     * @return an hex color according to the percentage of val in min/max
     */
    private int getColor(double val, double min, double max) {
        val = val - min;
        max = max - min;

        double p = 100d * val / max;

        if (p <= 25) {
            return 0xd12300;
        } else if (p > 25 && p <= 50) {
            return 0xd18800;
        } else if (p > 50 && p <= 75) {
            return 0xfae314;
        } else {
            return 0x77bf04;
        }
    }

    /**
     * Renders some text with shadow. The position is relative to the current selected container. If none, it's relative
     * to the screen.
     * @param text the text that you want to write,
     * @param x the x position (from left to right),
     * @param y the y position (from top to bottom),
     * @param color the color in hex (00-FF), following this format: RRGGBB (R:red, G:green, B:blue). Ex: 0xFFFFFF
     */
    private void renderText(String text, int x, int y, int color) {
        Minecraft.getInstance().fontRenderer.func_238421_b_(
                new MatrixStack(),
                text,
                // same comment as in this#drawHoveringText, sounds like everything is multiplied by two at some point
                x/2, y/2,
                color
        );
    }

    /**
     * @param px x position to test
     * @param py y position to test
     * @param rx x position of rect
     * @param ry y position of rect
     * @param rw width of rect
     * @param rh height of rect
     * @return true if the given position is in the given rectangle
     */
    private boolean posInRect(int px, int py, int rx, int ry, int rw, int rh) {
        return (px >= rx && px <= rx + rw) && (py >= ry && py <= ry + rh);
    }
}
