package dev.gallon.services;

import dev.gallon.domain.HorseStats;
import dev.gallon.domain.I18nKeys;
import dev.gallon.domain.ModConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static dev.gallon.domain.HorseStats.*;

public class DisplayService {

    public static void displayOverlayStats(@NotNull ModConfig config, @NotNull HorseStats stats) {
        Minecraft.getInstance().gui.setOverlayMessage(buildOverlayMessage(config, stats), false);
    }

    public static void displayContainerStats(
            @NotNull GuiGraphics guiGraphics,
            @NotNull ModConfig config,
            @NotNull HorseStats stats,
            int containerWidth,
            int containerMouseX,
            int containerMouseY
    ) {
        if (config.getDisplayStatsInInventory()) {
            displayStatsAndHoveringTexts(
                    guiGraphics, config, stats, containerMouseX, containerMouseY
            );
        } else {
            displayStatsInHoveringText(
                    guiGraphics, config, stats, containerWidth, containerMouseX, containerMouseY
            );
        }
    }

    private static void displayStatsInHoveringText(
            @NotNull GuiGraphics guiGraphics,
            @NotNull ModConfig config,
            @NotNull HorseStats stats,
            int containerWidth,
            int containerMouseX,
            int containerMouseY
    ) {
        // This represents a rectangle that contains all the header of the container (horse name)
        final int RX = 3;
        final int RY = 3;
        final int RW = containerWidth - 6;
        final int RH = 14;

        if (posInRect(containerMouseX, containerMouseY, RX, RY, RW, RH)) {
            List<Component> textLines = new ArrayList<>();

            // Health
            textLines.add(
                    (config.getDisplayMinMax() && !config.getStatsInPercentage()) ?
                            Component.literal(
                                    I18n.get(I18nKeys.HEALTH) + ": " +
                                            ChatFormatting.RED + MIN_HEALTH +
                                            ChatFormatting.RESET + "/" +
                                            getColorTextFormat(stats.health(), MIN_HEALTH, MAX_HEALTH) + String.format("%,.2f", stats.health()) +
                                            ChatFormatting.RESET + "/" +
                                            ChatFormatting.GREEN + MAX_HEALTH
                            )
                            : Component.literal(
                            I18n.get(I18nKeys.HEALTH) + ": " +
                                    getColorTextFormat(stats.health(), MIN_HEALTH, MAX_HEALTH) + stats.getHealthStr(config.getStatsInPercentage()) +
                                    ChatFormatting.RESET
                    )
            );

            // Jump height
            textLines.add(
                    (config.getDisplayMinMax() && !config.getStatsInPercentage()) ?
                            Component.literal(
                                    I18n.get(I18nKeys.JUMP_HEIGHT) + ": " +
                                            ChatFormatting.RED + MIN_JUMP_HEIGHT +
                                            ChatFormatting.RESET + "/" +
                                            getColorTextFormat(stats.jumpHeight(), MIN_JUMP_HEIGHT, MAX_JUMP_HEIGHT) + String.format("%,.2f", stats.jumpHeight()) +
                                            ChatFormatting.RESET + "/" +
                                            ChatFormatting.GREEN + MAX_JUMP_HEIGHT)
                            : Component.literal(
                            I18n.get(I18nKeys.JUMP_HEIGHT) + ": " +
                                    getColorTextFormat(stats.jumpHeight(), MIN_JUMP_HEIGHT, MAX_JUMP_HEIGHT) + stats.getJumpHeightStr(config.getStatsInPercentage()) +
                                    ChatFormatting.RESET
                    )
            );

            // Speed
            textLines.add(
                    (config.getDisplayMinMax() && !config.getStatsInPercentage()) ?
                            Component.literal(
                                    I18n.get(I18nKeys.SPEED) + ": " +
                                            ChatFormatting.RED + MIN_SPEED +
                                            ChatFormatting.RESET + "/" +
                                            getColorTextFormat(stats.speed(), MIN_SPEED, MAX_SPEED) + String.format("%,.2f", stats.speed()) +
                                            ChatFormatting.RESET + "/" +
                                            ChatFormatting.GREEN + MAX_SPEED)
                            : Component.literal(
                            I18n.get(I18nKeys.SPEED) + ": " +
                                    getColorTextFormat(stats.speed(), MIN_SPEED, MAX_SPEED) + stats.getSpeedStr(config.getStatsInPercentage()) +
                                    ChatFormatting.RESET
                    )
            );

            // Slots
            if (stats.slots().isPresent()) {
                textLines.add(
                        config.getDisplayMinMax() ?
                                Component.literal(
                                        I18n.get(I18nKeys.SLOTS) + ": " +
                                                ChatFormatting.RED + MIN_SLOTS +
                                                ChatFormatting.RESET + "/" +
                                                getColorTextFormat(stats.slots().get(), MIN_SLOTS, MAX_SLOTS) + stats.slots().get() +
                                                ChatFormatting.RESET + "/" +
                                                ChatFormatting.GREEN + MAX_SLOTS)
                                : Component.literal(
                                I18n.get(I18nKeys.SLOTS) + ": " +
                                        getColorTextFormat(stats.slots().get(), MIN_SLOTS, MAX_SLOTS) + stats.slots().get() +
                                        ChatFormatting.RESET
                        )
                );
            }

            // Owner
            if (stats.owner().isPresent()) {
                textLines.add(
                        Component.literal(
                                I18n.get(I18nKeys.OWNER) + ": " + stats.owner().get() +
                                        ChatFormatting.RESET
                        )
                );
            }

            drawHoveringText(
                    guiGraphics,
                    containerMouseX,
                    containerMouseY,
                    textLines
            );
        }
    }

    private static void displayStatsAndHoveringTexts(
            @NotNull GuiGraphics guiGraphics,
            @NotNull ModConfig config,
            @NotNull HorseStats stats,
            int containerMouseX,
            int containerMouseY
    ) {
        // The boxes positions (x,y) and dimensions (w,h) defining when to display the hovering text relative to the
        // top left of the container
        int rx;
        int ry = 6;
        int rw = 29;
        int rh = 11;

        // dirty, should be refactored, but I had to be fast for release
        boolean drawHealth = false;
        boolean drawJump = false;
        boolean drawSpeed = false;

        // Starts at x=60 by displaying "Stats:" (if it fits the GUI)
        rx = 60;

        // 7 is the maximum number of letters for "Stats" to be displayed, because otherwise it overlaps with
        // the horse's name

        // It is possible to open the GUI without riding a horse!
        if (stats.name().length() <= 8) {
            drawText(guiGraphics, I18n.get(I18nKeys.STATS) + ":", rx, ry, 0Xff444444);
        }

        // Health (30 units shift to the right)
        rx += (config.getStatsInPercentage() ? 33 : 30);
        drawText(guiGraphics,
                stats.getHealthStr(config.getStatsInPercentage()),
                rx, ry,
                config.getColoredStats() ? getColorHex(stats.health(), MIN_HEALTH, MAX_HEALTH) : 0Xff444444
        );
        if (posInRect(containerMouseX, containerMouseY, rx - 2, ry - 2, rw, rh)) { // -12 because max is x.xx and not xx.xx
            drawHealth = true;
        }

        // Jump (30 units shift to the right as well)
        rx += (config.getStatsInPercentage() ? 24 : 30);
        drawText(guiGraphics,
                stats.getJumpHeightStr(config.getStatsInPercentage()),
                rx, ry,
                config.getColoredStats() ? getColorHex(stats.jumpHeight(), MIN_JUMP_HEIGHT, MAX_JUMP_HEIGHT) : 0Xff444444
        );
        if (posInRect(containerMouseX, containerMouseY, rx - 2, ry - 2, rw - 6, rh)) { // -12 because max is x.xx and not xx.xx
            drawJump = true;
        }

        // Speed (24 units shift to the right, not the same as before because jump max is x.xx and not xx.xx)
        rx += 24;
        drawText(guiGraphics,
                stats.getSpeedStr(config.getStatsInPercentage()),
                rx, ry,
                config.getColoredStats() ? getColorHex(stats.speed(), MIN_SPEED, MAX_SPEED) : 0Xff444444
        );
        if (posInRect(containerMouseX, containerMouseY, rx - 2, ry - 2, rw, rh)) {
            drawSpeed = true;
        }

        // owner
        if (stats.owner().isPresent()) {
            rx += (config.getStatsInPercentage() ? 24 : 30);
            drawText(guiGraphics,
                    stats.owner().get(),
                    rx, ry,
                    0Xff444444
            );
        }

        if (config.getStatsInPercentage()) {
            if (drawHealth) {
                drawHoveringText(guiGraphics, containerMouseX, containerMouseY, I18n.get(I18nKeys.HEALTH));
            } else if (drawJump) {
                drawHoveringText(guiGraphics, containerMouseX, containerMouseY, I18n.get(I18nKeys.JUMP_HEIGHT));
            } else if (drawSpeed) {
                drawHoveringText(guiGraphics, containerMouseX, containerMouseY, I18n.get(I18nKeys.SPEED));
            }
        } else {
            if (drawHealth) {
                drawHoveringText(guiGraphics, containerMouseX, containerMouseY,
                        I18n.get(I18nKeys.HEALTH) + " (" + I18n.get(I18nKeys.HEALTH) + "):", MIN_HEALTH.toString(), MAX_HEALTH.toString(), I18n.get("horsestatsmod.player") + ": 20"
                );
            } else if (drawJump) {
                drawHoveringText(guiGraphics,
                        containerMouseX, containerMouseY,
                        I18n.get(I18nKeys.JUMP_HEIGHT) + " (" + I18n.get("horsestatsmod.blocks") + "):", MIN_JUMP_HEIGHT.toString(), MAX_JUMP_HEIGHT.toString(), I18n.get("horsestatsmod.player") + ": 1.25"
                );
            } else if (drawSpeed) {
                drawHoveringText(guiGraphics,
                        containerMouseX, containerMouseY,
                        I18n.get(I18nKeys.SPEED) + " (" + I18n.get("horsestatsmod.meters_per_seconds") + "):", MIN_SPEED.toString(), MAX_SPEED.toString(),
                        I18n.get("horsestatsmod.player") + ": 4.317 (" + I18n.get("horsestatsmod.walk") + ")",
                        I18n.get("horsestatsmod.player") + ": 5.612 (" + I18n.get("horsestatsmod.sprint") + ")",
                        I18n.get("horsestatsmod.player") + ": 7.143 (" + I18n.get("horsestatsmod.sprint") + "+" + I18n.get(I18nKeys.JUMP_HEIGHT) + ")"
                );
            }
        }
    }

    private static Component buildOverlayMessage(@NotNull ModConfig config, @NotNull HorseStats stats) {
        return (config.getDisplayMinMax() && !config.getStatsInPercentage()) ?
                Component.literal(
                        I18n.get(I18nKeys.HEALTH) + ": " +
                                ChatFormatting.RED + MIN_HEALTH +
                                ChatFormatting.RESET + "/" +
                                getColorTextFormat(stats.health(), MIN_HEALTH, MAX_HEALTH) + String.format("%,.2f", stats.health()) +
                                ChatFormatting.RESET + "/" +
                                ChatFormatting.GREEN + MAX_HEALTH + ChatFormatting.RESET + " " +
                                I18n.get(I18nKeys.JUMP_HEIGHT) + ": " +
                                ChatFormatting.RED + MIN_JUMP_HEIGHT +
                                ChatFormatting.RESET + "/" +
                                getColorTextFormat(stats.jumpHeight(), MIN_JUMP_HEIGHT, MAX_JUMP_HEIGHT) + String.format("%,.2f", stats.jumpHeight()) +
                                ChatFormatting.RESET + "/" +
                                ChatFormatting.GREEN + MAX_JUMP_HEIGHT + ChatFormatting.RESET + " " +
                                I18n.get(I18nKeys.SPEED) + ": " +
                                ChatFormatting.RED + MIN_SPEED +
                                ChatFormatting.RESET + "/" +
                                getColorTextFormat(stats.speed(), MIN_SPEED, MAX_SPEED) + String.format("%,.2f", stats.speed()) +
                                ChatFormatting.RESET + "/" +
                                ChatFormatting.GREEN + MAX_SPEED + ChatFormatting.RESET + " " +
                                (stats.slots().isEmpty() ? "" : (
                                        I18n.get(I18nKeys.SLOTS) + ": " +
                                                ChatFormatting.RED + MIN_SLOTS +
                                                ChatFormatting.RESET + "/" +
                                                getColorTextFormat(stats.slots().get(), MIN_SLOTS, MAX_SLOTS) + stats.slots().get() +
                                                ChatFormatting.RESET + "/" +
                                                ChatFormatting.GREEN + MAX_SLOTS
                                )) + ChatFormatting.RESET + " " +
                                (stats.owner().isEmpty() ? "" : (
                                        I18n.get(I18nKeys.OWNER) + ": " + stats.owner().get()
                                ))
                ) :
                Component.literal(
                        I18n.get(I18nKeys.HEALTH) + ": " +
                                getColorTextFormat(stats.health(), MIN_HEALTH, MAX_HEALTH) + stats.getHealthStr(config.getStatsInPercentage()) +
                                ChatFormatting.RESET + " " +
                                I18n.get(I18nKeys.JUMP_HEIGHT) + ": " +
                                getColorTextFormat(stats.jumpHeight(), MIN_JUMP_HEIGHT, MAX_JUMP_HEIGHT) + stats.getJumpHeightStr(config.getStatsInPercentage()) +
                                ChatFormatting.RESET + " " +
                                I18n.get(I18nKeys.SPEED) + ": " +
                                getColorTextFormat(stats.speed(), MIN_SPEED, MAX_SPEED) + stats.getSpeedStr(config.getStatsInPercentage()) +
                                ChatFormatting.RESET + " " +
                                (stats.slots().isEmpty() ? "" : (
                                        I18n.get(I18nKeys.SLOTS) + ": " +
                                                getColorTextFormat(stats.slots().get(), MIN_SLOTS, MAX_SLOTS) + stats.slots().get() +
                                                ChatFormatting.RESET
                                )) +
                                ChatFormatting.RESET + " " +
                                (stats.owner().isEmpty() ? "" : (
                                        I18n.get(I18nKeys.OWNER) + ": " + stats.owner().get()
                                ))
                );
    }

    /**
     * Renders some text with shadow. The position is relative to the current selected container. If none, it's relative
     * to the screen.
     *
     * @param guiGraphics guiGraphics to draw
     * @param text        the text that you want to write,
     * @param x           the x position (from left to right),
     * @param y           the y position (from top to bottom),
     * @param color       the color in hex (00-FF), following this format: RRGGBB (R:red, G:green, B:blue). Ex: 0xFFFFFF
     */
    private static void drawText(GuiGraphics guiGraphics, String text, int x, int y, int color) {
        guiGraphics.drawString(
                Minecraft.getInstance().font,
                text,
                x, y,
                color,
                false
        );
    }

    private static void drawHoveringText(GuiGraphics guiGraphics, int x, int y, String title, String min, String max, String... notes) {
        List<Component> textLines = new ArrayList<>();
        textLines.add(Component.literal(title));
        textLines.add(Component.literal(ChatFormatting.RED + I18n.get("horsestatsmod.min") + ": " + min));
        textLines.add(Component.literal(ChatFormatting.GREEN + I18n.get("horsestatsmod.max") + ": " + max));
        for (String note : notes) {
            textLines.add(Component.literal(note));
        }

        drawHoveringText(guiGraphics, x, y, textLines);
    }

    private static void drawHoveringText(GuiGraphics guiGraphics, int x, int y, String title) {
        List<Component> textLines = new ArrayList<>();
        textLines.add(Component.literal(title));
        drawHoveringText(guiGraphics, x, y, textLines);
    }

    private static void drawHoveringText(GuiGraphics guiGraphics, int x, int y, List<Component> textLines) {
        guiGraphics.renderTooltip(
                Minecraft.getInstance().font,
                textLines.stream()
                        .map(component -> ClientTooltipComponent.create(component.getVisualOrderText()))
                        .collect(Collectors.toList()),
                x, //(int) (x / getGuiScale()),
                y, //(int) (y / getGuiScale())
                DefaultTooltipPositioner.INSTANCE,
                null
        );
    }

    /**
     * @param val the value
     * @param min the min possible value
     * @param max the max possible value
     * @return a hex color according to the percentage of val in min/max
     */
    private static int getColorHex(double val, double min, double max) {
        double p = getPercentage(val, min, max);

        if (p <= 25) {
            return 0xffd12300;
        } else if (p > 25 && p <= 50) {
            return 0xffd18800;
        } else if (p > 50 && p <= 75) {
            return 0xfffae314;
        } else {
            return 0xff77bf04;
        }
    }

    /**
     * @param val the value
     * @param min the min possible value
     * @param max the max possible value
     * @return a ChatFormatting color according to the percentage of val in min/max
     */
    private static ChatFormatting getColorTextFormat(double val, double min, double max) {
        double p = getPercentage(val, min, max);

        if (p <= 25) {
            return ChatFormatting.RED;
        } else if (p > 25 && p <= 50) {
            return ChatFormatting.GOLD;
        } else if (p > 50 && p <= 75) {
            return ChatFormatting.YELLOW;
        } else {
            return ChatFormatting.GREEN;
        }
    }

    /**
     * Basic percentage function
     *
     * @param val the value
     * @param min the min possible value
     * @param max the max possible value
     * @return the percentage (0<=x<=100)
     */
    private static double getPercentage(double val, double min, double max) {
        return 100d * (val - min) / (max - min);
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
    private static boolean posInRect(int px, int py, int rx, int ry, int rw, int rh) {
        // These multiplications are here to match the rectangle drawn by calling AbstractGui.fillRect(a, b, c, d, col)
        // The rectangle given is multiplied by the gui scale to match the rectangle that would be drawn with the
        // same parameters.
        // When creating a rectangle, I use the fillRect function to visualize the rectangle. Then I use this function
        // to check if the mouse is inside or not. This multiplication prevents doing a lot more when calling this
        // function. Just ignore that, act like if it worked as intended. It's a little hack.
        return (px >= rx && px <= rx + rw) && (py >= ry && py <= ry + rh);
    }
}
