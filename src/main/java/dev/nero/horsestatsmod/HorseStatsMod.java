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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.inventory.HorseInventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;
import net.minecraft.util.text.*;
import net.minecraft.world.BossInfo;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.gui.GuiUtils;
import net.minecraftforge.fml.common.Mod;
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
    }

    private void onDrawForegroundEvent(final GuiContainerEvent.DrawForeground event) {
        if (event.getGuiContainer() instanceof HorseInventoryScreen) {
            try {
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

                double mouseX = Minecraft.getInstance().mouseHelper.getMouseX() - 2*event.getGuiContainer().getGuiLeft();
                double mouseY = Minecraft.getInstance().mouseHelper.getMouseY() - 2*event.getGuiContainer().getGuiTop();

                double rw = 30;
                double rh = 11;
                double ry = 6;
                double rx;

                // Stats
                rx = 60;
                // The skeleton horse name is too big and overrides "Stats:"
                if (!(Minecraft.getInstance().player.getRidingEntity() instanceof SkeletonHorseEntity))
                    this.renderText("Stats:", (int) rx, (int) ry, 0X444444);


                // Health
                rx += 30;
                this.renderText(
                        String.format("%.2f", health),
                        (int) rx, (int) ry,
                        getColor(health, 15, 30)
                );
                if (posInRect(mouseX, mouseY, (rx-2)*2,  (ry-2)*2, rw*2, rh*2)) {
                    drawHoveringText(
                            (int) mouseX/2, (int) mouseY/2,
                            "Health:", "15.0", "30.0", "player: 20.0"
                    );
                }

                // Jump
                rx += 30;
                this.renderText(
                        String.format("%.2f", jumpHeight),
                        (int) rx, (int) ry,
                        getColor(jumpHeight, 1.25, 5)
                );
                if (posInRect(mouseX, mouseY, (rx-2)*2,  (ry-2)*2, (rw-6)*2, rh*2)) {
                    drawHoveringText(
                            (int) mouseX/2, (int) mouseY/2,
                            "Jump (blocks):", "1.25", "5.0", "player: 1.25"
                    );
                }

                // Speed
                rx += 24;
                this.renderText(
                        String.format("%.2f", speed),
                        (int) rx, (int) ry,
                        getColor(speed, 4.8, 14.5)
                );
                if (posInRect(mouseX, mouseY, (rx-2)*2,  (ry-2)*2, rw*2, rh*2)) {
                    drawHoveringText(
                            (int) mouseX/2, (int) mouseY/2,
                            "Speed (m/s):", "4.8", "14.5",
                            "player: 4.317 (walk)", "player: 5.612 (sprint)", "player: 7.143 (sprint+jump)"
                    );
                }
            } catch (Exception e) {
                LOGGER.error("The player is looking into an horse inventory without riding it? Is that possible?");
            }
        }
    }

    private void drawHoveringText(int x, int y, String title, String min, String max, String... notes) {
        List<ITextProperties> textLines = new ArrayList<>();
        textLines.add(new StringTextComponent(title));
        textLines.add(new StringTextComponent(TextFormatting.RED + "min: " + min));
        textLines.add(new StringTextComponent(TextFormatting.GREEN + "max: " + max));
        for (String note : notes) {
            textLines.add(new StringTextComponent(note));
        }

        GuiUtils.drawHoveringText(
                new MatrixStack(), textLines,
                x,
                y,
                Minecraft.getInstance().getMainWindow().getWidth(),
                Minecraft.getInstance().getMainWindow().getHeight(),
                150, Minecraft.getInstance().fontRenderer
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
                x,
                y,
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
    private boolean posInRect(double px, double py, double rx, double ry, double rw, double rh) {
        return (px >= rx && px <= rx + rw) && (py >= ry && py <= ry + rh);
    }
}
