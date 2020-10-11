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
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.HorseInventoryScreen;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.util.text.*;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.gui.GuiUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod(HorseStatsMod.MODID)
public class HorseStatsMod
{
    public static final String MODID = "horsestatsmod";

    // If you need to log some stuff
    private static final Logger LOGGER = LogManager.getLogger();

    private final double MIN_HEALTH = 15;
    private final double MAX_HEALTH = 30;
    private final double MIN_JUMP_HEIGHT = 1.25;
    private final double MAX_JUMP_HEIGHT = 5;
    private final double MIN_SPEED = 4.8;
    private final double MAX_SPEED = 14.5;

    private double health;
    private double jumpHeight;
    private double speed;

    public HorseStatsMod() {
        MinecraftForge.EVENT_BUS.addListener(this::onEntityInteractEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onDrawForegroundEvent);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, TheModConfig.CLIENT_SPEC);
    }

    private void onEntityInteractEvent(final PlayerInteractEvent.EntityInteractSpecific event) {
        if (event.getTarget() instanceof AbstractHorseEntity) {
            if (!((AbstractHorseEntity) event.getTarget()).isTame()) {

                this.getHorseStats((AbstractHorseEntity) event.getTarget());

                Minecraft.getInstance().ingameGUI.setOverlayMessage(
                    TheModConfig.displayMinMax() ?
                    new StringTextComponent(
                    "Health: " +
                            TextFormatting.RED + MIN_HEALTH +
                            TextFormatting.RESET + "/" +
                            getColorTextFormat(health, MIN_HEALTH, MAX_HEALTH) + String.format("%,.2f", health) +
                            TextFormatting.RESET + "/" +
                            TextFormatting.GREEN + MAX_HEALTH + TextFormatting.RESET + " " +
                            "Jump: " +
                            TextFormatting.RED + MIN_JUMP_HEIGHT +
                            TextFormatting.RESET + "/" +
                            getColorTextFormat(jumpHeight, MIN_JUMP_HEIGHT, MAX_JUMP_HEIGHT) + String.format("%,.2f", jumpHeight) +
                            TextFormatting.RESET + "/" +
                            TextFormatting.GREEN + MAX_JUMP_HEIGHT + TextFormatting.RESET + " " +
                            "Speed: " +
                            TextFormatting.RED + MIN_SPEED +
                            TextFormatting.RESET + "/" +
                            getColorTextFormat(speed, MIN_SPEED, MAX_SPEED) + String.format("%,.2f", speed) +
                            TextFormatting.RESET + "/" +
                            TextFormatting.GREEN + MAX_SPEED
                    ) :
                    new StringTextComponent(
                            "Health: " +
                                getColorTextFormat(health, MIN_HEALTH, MAX_HEALTH) + String.format("%,.2f", health) +
                                TextFormatting.RESET + " " +
                                "Jump: " +
                                getColorTextFormat(jumpHeight, MIN_JUMP_HEIGHT, MAX_JUMP_HEIGHT) + String.format("%,.2f", jumpHeight) +
                                TextFormatting.RESET + " " +
                                "Speed: " +
                                getColorTextFormat(speed, MIN_SPEED, MAX_SPEED) + String.format("%,.2f", speed) +
                                TextFormatting.RESET
                    ),
                    false
                );
            }
        }
    }

    private void onDrawForegroundEvent(final GuiContainerEvent.DrawForeground event) {
        if (event.getGuiContainer() instanceof HorseInventoryScreen) {
            // 1. GET THE STATISTICS OF THAT RIDDEN HORSE

            // The horse attribute is private in HorseInventoryScreen, and the event does not have the information
            // either (that's normal because that's a render event). So we need to retrieve it using an other way
            AbstractHorseEntity horse = null;
            try {
                // If the player is riding it
                horse = (AbstractHorseEntity) Minecraft.getInstance().player.getRidingEntity();
            } catch (Exception e1) {
                // If the player is interacting with it by crouching + right clicking it
                try {
                    horse = (AbstractHorseEntity) Minecraft.getInstance().pointedEntity;
                } catch (Exception e2) {
                    LOGGER.error("Could not get the horse information");
                }
            }

            if (horse == null) return;

            getHorseStats(horse);

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
                displayStatsAndHoveringTexts(mouseX, mouseY);
            } else {
                // Show the stats only if the mouse is on the horse's name
                displayStatsInHoveringText(event.getGuiContainer(), mouseX, mouseY);
            }
        }
    }

    private void getHorseStats(AbstractHorseEntity horse) {
        health = horse.getAttribute(Attributes.field_233818_a_).getValue();
        jumpHeight = horse.getAttribute(Attributes.field_233830_m_).getValue();
        speed = horse.getAttribute(Attributes.field_233821_d_).getValue();

        jumpHeight = (
                - 0.1817584952 * Math.pow(jumpHeight, 3) +
                        3.689713992 * Math.pow(jumpHeight, 2) +
                        2.128599134 * jumpHeight - 0.343930367
        ); // convert to blocks
        speed = speed * 43; // convert to m/s
    }

    private void displayStatsInHoveringText(ContainerScreen guiContainer, int mouseX, int mouseY) {

        // todo double -> int
        // This represents a rectangle that contains all the header of the container (horse name)
        final int RX = 3;
        final int RY = 3;
        final int RW = guiContainer.getXSize() - 6;
        final int RH = 14;

        if (posInRect(mouseX, mouseY, RX, RY, RW, RH)) {
            List<ITextComponent> textLines = new ArrayList<>();

            // Health
            textLines.add(
                TheModConfig.displayMinMax() ?
                        new StringTextComponent(
                        "Health: " +
                            TextFormatting.RED + MIN_HEALTH +
                            TextFormatting.RESET + "/" +
                            getColorTextFormat(health, MIN_HEALTH, MAX_HEALTH) + String.format("%,.2f", health) +
                            TextFormatting.RESET + "/" +
                            TextFormatting.GREEN + MAX_HEALTH
                        )
                : new StringTextComponent(
                "Health: " +
                    getColorTextFormat(health, MIN_HEALTH, MAX_HEALTH) + String.format("%,.2f", health) +
                    TextFormatting.RESET
                )
            );

            // Jump height
            textLines.add(
                TheModConfig.displayMinMax() ?
                    new StringTextComponent(
                    "Jump height: " +
                    TextFormatting.RED + MIN_JUMP_HEIGHT +
                    TextFormatting.RESET + "/" +
                    getColorTextFormat(jumpHeight, MIN_JUMP_HEIGHT, MAX_JUMP_HEIGHT) + String.format("%,.2f", jumpHeight) +
                    TextFormatting.RESET + "/" +
                    TextFormatting.GREEN + MAX_JUMP_HEIGHT)
                : new StringTextComponent(
                "Jump height: " +
                    getColorTextFormat(jumpHeight, MIN_JUMP_HEIGHT, MAX_JUMP_HEIGHT) + String.format("%,.2f", jumpHeight) +
                    TextFormatting.RESET
                )
            );

            // Speed
            textLines.add(
                TheModConfig.displayMinMax() ?
                    new StringTextComponent(
                "Speed: " +
                    TextFormatting.RED + MIN_SPEED +
                    TextFormatting.RESET + "/" +
                    getColorTextFormat(speed, MIN_SPEED, MAX_SPEED) + String.format("%,.2f", speed) +
                    TextFormatting.RESET + "/" +
                    TextFormatting.GREEN + MAX_SPEED)
                : new StringTextComponent(
                    "Speed: " +
                        getColorTextFormat(speed, MIN_SPEED, MAX_SPEED) + String.format("%,.2f", speed) +
                        TextFormatting.RESET
                )
            );

            this.drawHoveringText(mouseX, mouseY, textLines);
        }
    }

    private void displayStatsAndHoveringTexts(int mouseX, int mouseY) {

        // The boxes positions (x,y) and dimensions (w,h) defining when to display the hovering text relative to the
        // top left of the container
        int rx;
        int ry = 6;
        int rw = 29;
        int rh = 11;

        // Starts at x=60 by displaying "Stats:" (if it fits the GUI)
        rx = 60;

        // 7 is the maximum number of letters for "Stats" to be displayed, because otherwise it overlaps with
        // the horse's name
        if (!(Minecraft.getInstance().player.getRidingEntity().getDisplayName().getString().length() > 8))
            this.renderText("Stats:", rx, ry, 0X444444);

        // Health (30 units shift to the right)
        rx += 30;
        this.renderText(
                String.format("%.2f", health),
                rx, ry,
                TheModConfig.coloredStats() ? getColorHex(health, MIN_HEALTH, MAX_HEALTH) : 0X444444
        );

        if (posInRect(mouseX, mouseY, rx - 2,  ry - 2, rw, rh)) {
            drawHoveringText(
                    mouseX, mouseY,
                    "Health:", "15.0", "30.0", "player: 20.0"
            );
        }

        // Jump (30 units shift to the right as well)
        rx += 30;
        this.renderText(
                String.format("%.2f", jumpHeight),
                rx, ry,
                TheModConfig.coloredStats() ? getColorHex(jumpHeight, MIN_JUMP_HEIGHT, MAX_JUMP_HEIGHT) : 0X444444
        );
        if (posInRect(mouseX, mouseY, rx - 2,  ry - 2, rw-6, rh)) { // -12 because max is x.xx and not xx.xx
            drawHoveringText(
                    mouseX, mouseY,
                    "Jump (blocks):", "1.25", "5.0", "player: 1.25"
            );
        }

        // Speed (24 units shift to the right, not the same as before because jump max is x.xx and not xx.xx)
        rx += 24;
        this.renderText(
                String.format("%.2f", speed),
                rx, ry,
                TheModConfig.coloredStats() ? getColorHex(speed, MIN_SPEED, MAX_SPEED) : 0X444444
        );
        if (posInRect(mouseX, mouseY, rx-2,  ry-2, rw, rh)) {
            drawHoveringText(
                    mouseX, mouseY,
                    "Speed (m/s):", "4.8", "14.5",
                    "player: 4.317 (walk)", "player: 5.612 (sprint)", "player: 7.143 (sprint+jump)"
            );
        }
    }

    private void drawHoveringText(int x, int y, String title, String min, String max, String... notes) {
        List<ITextComponent> textLines = new ArrayList<>();
        textLines.add(new StringTextComponent(title));
        textLines.add(new StringTextComponent(TextFormatting.RED + "min: " + min));
        textLines.add(new StringTextComponent(TextFormatting.GREEN + "max: " + max));
        for (String note : notes) {
            textLines.add(new StringTextComponent(note));
        }

        this.drawHoveringText(x, y, textLines);
    }

    private void drawHoveringText(int x, int y, List<ITextComponent> textLines) {

        GuiUtils.drawHoveringText(
                new MatrixStack(),
                textLines,
                // why /2? bc it works that way, I did not inspect the mc code in depth to understand
                x/2, y/2,
                Minecraft.getInstance().getMainWindow().getWidth(),
                Minecraft.getInstance().getMainWindow().getHeight(),150,
                Minecraft.getInstance().fontRenderer
        );
    }

    /**
     * @param val the value
     * @param min the min possible value
     * @param max the max possible value
     * @return an hex color according to the percentage of val in min/max
     */
    private int getColorHex(double val, double min, double max) {
        double p = this.getPercentage(val, min, max);

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
     * @param val the value
     * @param min the min possible value
     * @param max the max possible value
     * @return a TextFormatting color according to the percentage of val in min/max
     */
    private TextFormatting getColorTextFormat(double val, double min, double max) {
        double p = this.getPercentage(val, min, max);

        if (p <= 25) {
            return TextFormatting.RED;
        } else if (p > 25 && p <= 50) {
            return TextFormatting.GOLD;
        } else if (p > 50 && p <= 75) {
            return TextFormatting.YELLOW;
        } else {
            return TextFormatting.GREEN;
        }
    }

    /**
     * Basic percentage function
     * @param val the value
     * @param min the min possible value
     * @param max the max possible value
     * @return the percentage (0<=x<=100)
     */
    private double getPercentage(double val, double min, double max) {
        return 100d * (val-min) / (max-min);
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
                x, y,
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
        // These multiplications are here to match the rectangle drawn by calling AbstractGui.fillRect(a, b, c, d, col)
        // This method multiplies everything by two (the other drawing methods do the same thing, I don't know why. If
        // you know, please open an issue and explain that). So the rectangle given will be multiplied by 2 to match
        // the rectangle that would be drawn with the same parameters.
        // When creating a rectangle, I use the fillRect function to visualize the rectangle. Then I use this function
        // to check if the mouse is inside or not. This multiplication prevents doing a lot more when calling this
        // function. Just ignore that, act like if it worked as intended. It's a little hack.
        rx *= 2;
        ry *= 2;
        rw *= 2;
        rh *= 2;
        return (px >= rx && px <= rx + rw) && (py >= ry && py <= ry + rh);
    }
}
