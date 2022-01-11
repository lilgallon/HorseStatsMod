/*
* Copyright (C) 2020 @lilgallon on Github (Lilian Gallon)
* This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
* License as published by the Free Software Foundation; version 2. This program is distributed in the hope that it will
* be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
* PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General
* Public License along with this program.
*/

package dev.nero.horsestatsmod;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.nero.horsestatsmod.config.TheModConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraftforge.client.event.ContainerScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static net.minecraftforge.network.NetworkConstants.IGNORESERVERONLY;

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
    private final double MIN_SLOTS = 3;
    private final double MAX_SLOTS = 15;

    private double health;
    private double jumpHeight;
    private double speed;
    private int slots;
    private String owner;

    // Used to override the current overlay message if ours is already being displayed
    // Prevent "mounted" overlay text to override the stats message
    private Component overlayMessage;
    private int overlayMessageTime;

    private static final LoadingCache<UUID, Optional<String>> usernameCache = CacheBuilder
            .newBuilder()
            .expireAfterWrite(6, TimeUnit.HOURS)
            .build(new CacheLoader<>() {
                @Override
                public @NotNull Optional<String> load(@NotNull UUID key) {
                    CompletableFuture.runAsync(() -> {
                        GameProfile playerProfile = new GameProfile(key, null);
                        playerProfile = Minecraft.getInstance().getMinecraftSessionService().fillProfileProperties(playerProfile, false);
                        usernameCache.put(key, Optional.ofNullable(playerProfile.getName()));
                        System.out.println(key);
                    });

                    return Optional.of(I18n.get("horsestatsmod.loading"));
                }
            });

    public HorseStatsMod() {
        // Make sure the mod being absent on the other network side does not cause the client to display
        // the server as incompatible
        ModLoadingContext.get().registerExtensionPoint(
                IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(() -> IGNORESERVERONLY, (remote, isServer)-> true)
        );

        MinecraftForge.EVENT_BUS.addListener(this::onEntityInteractEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onDrawForegroundEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onRenderTickEvent);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, TheModConfig.CLIENT_SPEC);
    }

    private void onRenderTickEvent(TickEvent.RenderTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            if (this.overlayMessageTime > 0)
            {
                -- this.overlayMessageTime;
                Minecraft.getInstance().gui.overlayMessageTime = this.overlayMessageTime;
                if (!Minecraft.getInstance().gui.overlayMessageString.getString().equals(this.overlayMessage.getString()))
                {
                    Minecraft.getInstance().gui.overlayMessageString = this.overlayMessage;
                }
            }
        }
    }

    private void onEntityInteractEvent(final PlayerInteractEvent.EntityInteractSpecific event) {
        if (event.getTarget() instanceof AbstractHorse) {
            this.getHorseStats((AbstractHorse) event.getTarget());

            this.setOverlayMessage(
                TheModConfig.displayMinMax() ?
                new TextComponent(
                        I18n.get("horsestatsmod.health") + ": " +
                        ChatFormatting.RED + MIN_HEALTH +
                        ChatFormatting.RESET + "/" +
                        getColorTextFormat(health, MIN_HEALTH, MAX_HEALTH) + String.format("%,.2f", health) +
                        ChatFormatting.RESET + "/" +
                        ChatFormatting.GREEN + MAX_HEALTH + ChatFormatting.RESET + " " +
                        I18n.get("horsestatsmod.jump") + ": " +
                        ChatFormatting.RED + MIN_JUMP_HEIGHT +
                        ChatFormatting.RESET + "/" +
                        getColorTextFormat(jumpHeight, MIN_JUMP_HEIGHT, MAX_JUMP_HEIGHT) + String.format("%,.2f", jumpHeight) +
                        ChatFormatting.RESET + "/" +
                        ChatFormatting.GREEN + MAX_JUMP_HEIGHT + ChatFormatting.RESET + " " +
                        I18n.get("horsestatsmod.speed") + ": " +
                        ChatFormatting.RED + MIN_SPEED +
                        ChatFormatting.RESET + "/" +
                        getColorTextFormat(speed, MIN_SPEED, MAX_SPEED) + String.format("%,.2f", speed) +
                        ChatFormatting.RESET + "/" +
                        ChatFormatting.GREEN + MAX_SPEED + ChatFormatting.RESET + " " +
                        (slots == -1 ? "" : (
                            I18n.get("horsestatsmod.slots") + ": " +
                            ChatFormatting.RED + MIN_SLOTS +
                            ChatFormatting.RESET + "/" +
                            getColorTextFormat(slots, MIN_SLOTS, MAX_SLOTS) + slots +
                            ChatFormatting.RESET + "/" +
                            ChatFormatting.GREEN + MAX_SLOTS
                        )) + ChatFormatting.RESET + " " +
                        (owner == null ? "" : (
                                I18n.get("horsestatsmod.owner") + ": " + owner
                        ))
                ) :
                new TextComponent(
                        I18n.get("horsestatsmod.health") + ": " +
                            getColorTextFormat(health, MIN_HEALTH, MAX_HEALTH) + String.format("%,.2f", health) +
                            ChatFormatting.RESET + " " +
                            I18n.get("horsestatsmod.jump") + ": " +
                            getColorTextFormat(jumpHeight, MIN_JUMP_HEIGHT, MAX_JUMP_HEIGHT) + String.format("%,.2f", jumpHeight) +
                            ChatFormatting.RESET + " " +
                            I18n.get("horsestatsmod.speed") + ": " +
                            getColorTextFormat(speed, MIN_SPEED, MAX_SPEED) + String.format("%,.2f", speed) +
                            ChatFormatting.RESET + " " +
                            (slots == -1 ? "" : (
                                I18n.get("horsestatsmod.slots") + ": " +
                                getColorTextFormat(slots, MIN_SLOTS, MAX_SLOTS) + slots +
                                ChatFormatting.RESET
                            )) +
                            ChatFormatting.RESET + " " +
                            (owner == null ? "" : (
                                    I18n.get("horsestatsmod.owner") + ": " + owner
                            ))
                )
            );
        }
    }

    private void onDrawForegroundEvent(final ContainerScreenEvent.DrawForeground event) {
        if (event.getContainerScreen() instanceof HorseInventoryScreen) {
            // 1. GET THE STATISTICS OF THAT RIDDEN HORSE

            // The horse attribute is private in HorseInventoryScreen (see accesstransformer.cfg)
            AbstractHorse horse = ((HorseInventoryScreen) event.getContainerScreen()).horse;
            getHorseStats(horse);

            // 2. DISPLAY THE I18n.get("horsestatsmod.stats")
            // Mouse position (relative to the top left of the container) to know when to render the hovering text
            // This - scale*blabla shifts the mouse position so that (0, 0) is actually the top left of the container
            int mouseX = (
                    (int) Minecraft.getInstance().mouseHandler.xpos()
                    - Minecraft.getInstance().options.guiScale * event.getContainerScreen().getGuiLeft()
            );
            int mouseY = (
                    (int) Minecraft.getInstance().mouseHandler.ypos()
                    - Minecraft.getInstance().options.guiScale * event.getContainerScreen().getGuiTop()
            );

            if (TheModConfig.displayStats()) {
                // Show the stats on the GUI
                displayStatsAndHoveringTexts(horse, mouseX, mouseY);
            } else {
                // Show the stats only if the mouse is on the horse's name
                displayStatsInHoveringText(((HorseInventoryScreen) event.getContainerScreen()), mouseX, mouseY);
            }
        }
    }

    private void setOverlayMessage(Component message) {
        this.overlayMessage = message;
        this.overlayMessageTime = 120;
        Minecraft.getInstance().gui.setOverlayMessage(message, false);
    }

    private void getHorseStats(AbstractHorse horse) {
        health = horse.getAttribute(Attributes.MAX_HEALTH).getValue();
        jumpHeight = horse.getAttribute(Attributes.JUMP_STRENGTH).getValue();
        speed = horse.getAttribute(Attributes.MOVEMENT_SPEED).getValue();

        final UUID ownerUUID = horse.getOwnerUUID();
        if (ownerUUID != null) {
            owner = usernameCache.getUnchecked(ownerUUID).orElse(I18n.get("horsestatsmod.loading"));
        } else {
            owner = null;
        }

        if (horse instanceof Llama)
            slots = ((Llama) horse).getInventoryColumns() * 3;
        else
            slots = -1;

        jumpHeight = (
                - 0.1817584952 * Math.pow(jumpHeight, 3) +
                        3.689713992 * Math.pow(jumpHeight, 2) +
                        2.128599134 * jumpHeight - 0.343930367
        ); // convert to blocks
        speed = speed * 43; // convert to m/s
    }

    private void displayStatsInHoveringText(HorseInventoryScreen guiContainer, int mouseX, int mouseY) {

        // todo double -> int
        // This represents a rectangle that contains all the header of the container (horse name)
        final int RX = 3;
        final int RY = 3;
        final int RW = guiContainer.getXSize() - 6;
        final int RH = 14;

        if (posInRect(mouseX, mouseY, RX, RY, RW, RH)) {
            List<Component> textLines = new ArrayList<>();

            // Health
            textLines.add(
                TheModConfig.displayMinMax() ?
                        new TextComponent(
                        I18n.get("horsestatsmod.health") + ": " +
                            ChatFormatting.RED + MIN_HEALTH +
                            ChatFormatting.RESET + "/" +
                            getColorTextFormat(health, MIN_HEALTH, MAX_HEALTH) + String.format("%,.2f", health) +
                            ChatFormatting.RESET + "/" +
                            ChatFormatting.GREEN + MAX_HEALTH
                        )
                : new TextComponent(
                        I18n.get("horsestatsmod.health") + ": " +
                    getColorTextFormat(health, MIN_HEALTH, MAX_HEALTH) + String.format("%,.2f", health) +
                    ChatFormatting.RESET
                )
            );

            // Jump height
            textLines.add(
                TheModConfig.displayMinMax() ?
                    new TextComponent(
                    I18n.get("horsestatsmod.jump") + ": " +
                    ChatFormatting.RED + MIN_JUMP_HEIGHT +
                    ChatFormatting.RESET + "/" +
                    getColorTextFormat(jumpHeight, MIN_JUMP_HEIGHT, MAX_JUMP_HEIGHT) + String.format("%,.2f", jumpHeight) +
                    ChatFormatting.RESET + "/" +
                    ChatFormatting.GREEN + MAX_JUMP_HEIGHT)
                : new TextComponent(
                I18n.get("horsestatsmod.jump") + ": " +
                    getColorTextFormat(jumpHeight, MIN_JUMP_HEIGHT, MAX_JUMP_HEIGHT) + String.format("%,.2f", jumpHeight) +
                    ChatFormatting.RESET
                )
            );

            // Speed
            textLines.add(
                TheModConfig.displayMinMax() ?
                    new TextComponent(
                I18n.get("horsestatsmod.speed") + ": " +
                    ChatFormatting.RED + MIN_SPEED +
                    ChatFormatting.RESET + "/" +
                    getColorTextFormat(speed, MIN_SPEED, MAX_SPEED) + String.format("%,.2f", speed) +
                    ChatFormatting.RESET + "/" +
                    ChatFormatting.GREEN + MAX_SPEED)
                : new TextComponent(
                    I18n.get("horsestatsmod.speed") + ": " +
                        getColorTextFormat(speed, MIN_SPEED, MAX_SPEED) + String.format("%,.2f", speed) +
                        ChatFormatting.RESET
                )
            );

            // Slots
            if (slots != -1) {
                textLines.add(
                    TheModConfig.displayMinMax() ?
                        new TextComponent(
                            I18n.get("horsestatsmod.slots") + ": " +
                                ChatFormatting.RED + MIN_SLOTS +
                                ChatFormatting.RESET + "/" +
                                getColorTextFormat(speed, MIN_SLOTS, MAX_SLOTS) + slots +
                                ChatFormatting.RESET + "/" +
                                ChatFormatting.GREEN + MAX_SLOTS)
                        : new TextComponent(
                            I18n.get("horsestatsmod.slots") + ": " +
                            getColorTextFormat(speed, MIN_SLOTS, MAX_SLOTS) + slots +
                            ChatFormatting.RESET
                    )
                );
            }

            // Owner
            if (owner != null) {
                textLines.add(
                        new TextComponent(
                        I18n.get("horsestatsmod.owner") + ": " + owner +
                                ChatFormatting.RESET
                        )
                );
            }

            this.drawHoveringText(mouseX, mouseY, textLines);
        }
    }

    private void displayStatsAndHoveringTexts(AbstractHorse horse, int mouseX, int mouseY) {

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

        // It is possible to open the GUI without riding a horse!
        if (!(horse.getDisplayName().getString().length() > 8))
            this.renderText(I18n.get("horsestatsmod.stats") + ":", rx, ry, 0X444444);

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
                    I18n.get("horsestatsmod.health") + ":", "15.0", "30.0", I18n.get("horsestatsmod.player") + ": 20.0"
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
                    I18n.get("horsestatsmod.jump") + " (" + I18n.get("horsestatsmod.blocks") + "):", "1.25", "5.0", I18n.get("horsestatsmod.player") +  ": 1.25"
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
                    I18n.get("horsestatsmod.speed") + " (" + I18n.get("horsestatsmod.metersperseconds") + "):", "4.8", "14.5",
                    I18n.get("horsestatsmod.player") + ": 4.317 (" + I18n.get("horsestatsmod.walk") + ")",
                    I18n.get("horsestatsmod.player") + ": 5.612 (" + I18n.get("horsestatsmod.sprint") + ")",
                    I18n.get("horsestatsmod.player") + ": 7.143 (" + I18n.get("horsestatsmod.sprint") + "+" + I18n.get("horsestatsmod.jump") + ")"
            );
        }

        // owner
        rx += 30;
        this.renderText(
                owner,
                rx, ry,
                0X444444
        );
    }

    private void drawHoveringText(int x, int y, String title, String min, String max, String... notes) {
        List<Component> textLines = new ArrayList<>();
        textLines.add(new TextComponent(title));
        textLines.add(new TextComponent(ChatFormatting.RED + I18n.get("horsestatsmod.min") + ": " + min));
        textLines.add(new TextComponent(ChatFormatting.GREEN + I18n.get("horsestatsmod.max") + ": " + max));
        for (String note : notes) {
            textLines.add(new TextComponent(note));
        }

        this.drawHoveringText(x, y, textLines);
    }

    private void drawHoveringText(int x, int y, List<Component> textLines) {
        Minecraft.getInstance().screen.renderTooltip(
                new PoseStack(),
                textLines,
                java.util.Optional.empty(),
                x/Minecraft.getInstance().options.guiScale, y/Minecraft.getInstance().options.guiScale,
                // Minecraft.getInstance().getWindow().getWidth(),
                // Minecraft.getInstance().getWindow().getHeight(),150,
                Minecraft.getInstance().font
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
     * @return a ChatFormatting color according to the percentage of val in min/max
     */
    private ChatFormatting getColorTextFormat(double val, double min, double max) {
        double p = this.getPercentage(val, min, max);

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
        Minecraft.getInstance().font.draw(
                new PoseStack(),
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
        // The rectangle given is multiplied by the gui scale to match the rectangle that would be drawn with the
        // same parameters.
        // When creating a rectangle, I use the fillRect function to visualize the rectangle. Then I use this function
        // to check if the mouse is inside or not. This multiplication prevents doing a lot more when calling this
        // function. Just ignore that, act like if it worked as intended. It's a little hack.
        rx *= Minecraft.getInstance().options.guiScale;
        ry *= Minecraft.getInstance().options.guiScale;
        rw *= Minecraft.getInstance().options.guiScale;
        rh *= Minecraft.getInstance().options.guiScale;
        return (px >= rx && px <= rx + rw) && (py >= ry && py <= ry + rh);
    }
}
