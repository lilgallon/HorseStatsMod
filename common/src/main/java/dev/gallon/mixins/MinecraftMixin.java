package dev.gallon.mixins;

import dev.gallon.HorseStatsMod;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "pickBlockOrEntity", at = @At("HEAD"))
    private void showHorseStatsOnMiddleClick(CallbackInfo ci) {
        HorseStatsMod.onMiddleClickEvent((Minecraft) (Object) this);
    }
}
