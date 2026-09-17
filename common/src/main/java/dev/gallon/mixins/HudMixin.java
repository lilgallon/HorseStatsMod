package dev.gallon.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Hud;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public abstract class HudMixin {
    @Inject(method = "setOverlayMessage", at = @At("HEAD"), cancellable = true, remap = false)
    private void horseStatsMod$suppressMountHint(Component message, boolean animate, CallbackInfo ci) {
        var player = Minecraft.getInstance().player;
        // Cancel at submission time so the hint cannot flash between client ticks.
        if (player != null && player.getVehicle() instanceof AbstractHorse
                && message.getContents() instanceof TranslatableContents contents
                && "mount.onboard".equals(contents.getKey())) {
            ci.cancel();
        }
    }
}
