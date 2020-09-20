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
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("horsestatsmod")
public class HorseStatsMod
{
    // If you need to log some stuff
    private static final Logger LOGGER = LogManager.getLogger();

    public HorseStatsMod() {
        MinecraftForge.EVENT_BUS.addListener(this::onDrawForegroundEvent);
    }

    private void onDrawForegroundEvent(final GuiContainerEvent.DrawForeground event) {
        // It draws the search box and it also highlights the found slots
        /*
        if (this.isSearchableContainer(event.getGuiContainer())) {
            // Render search text
            RenderUtils.renderText(
                    this.isSearching ?
                            "Search: " + searchText + "_" :
                            "Search disabled (" + keybindToString(this.focusKeybinding) + ")",
                    0,
                    - Minecraft.getInstance().fontRenderer.FONT_HEIGHT - 5,
                    this.isSearching ? 0xFFFFFF : 0x888888
            );

            // Highlight slots
            for (SlotPos slotPos : this.highlightedSlots) {
                RenderUtils.highlightSlot(slotPos, 0x772fb000); // green with transparency
            }
        }*/
    }

    /**
     * Renders some text with shadow. The position is relative to the current selected container. If none, it's relative
     * to the screen.
     * @param text the text that you want to write,
     * @param x the x position (from left to right),
     * @param y the y position (from top to bottom),
     * @param color the color in hex (00-FF), following this format: RRGGBB (R:red, G:green, B:blue). Ex: 0xFFFFFF
     */
    public static void renderText(String text, int x, int y, int color) {
        Minecraft.getInstance().fontRenderer.func_238405_a_(
                new MatrixStack(),
                text,
                x,
                y,
                color
        );
    }
}
